package com.example.frontmicroservice.service.impl;

import com.example.frontmicroservice.adapter.LocalDateTimeAdapter;
import com.example.frontmicroservice.entity.LoanOrderToCreate;
import com.example.frontmicroservice.entity.LoanOrderToDelete;
import com.example.frontmicroservice.entity.UserDTO;
import com.example.frontmicroservice.response.Response;
import com.example.frontmicroservice.response.data.ResponseOrderId;
import com.example.frontmicroservice.response.data.ResponseOrders;
import com.example.frontmicroservice.response.data.ResponseStatus;
import com.example.frontmicroservice.response.data.ResponseTariffs;
import com.example.frontmicroservice.response.error.Error;
import com.example.frontmicroservice.service.LoanOrderService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class LoanOrderServiceImpl implements LoanOrderService {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    @Autowired
    private WebClient.Builder webClientBuilder;

    @Override
    @Cacheable(cacheNames = "cacheGetAllTariff")
    public Response<ResponseTariffs> getAllTariff() {

        ResponseEntity<String> responseEntity = webClientBuilder.build()
                .get().uri("/loan-service/getTariffs")
                .retrieve()
                .onStatus(HttpStatusCode::isError,response -> {
                    return Mono.empty();})
                .toEntity(String.class)
                .flatMap(response -> {
                    return Mono.just(ResponseEntity.status(response.getStatusCode()).body(response.getBody()));
                })
                .block();
        Response<ResponseTariffs> response = new Response<>();

        if(responseEntity.getStatusCode() == HttpStatus.OK)
            response = gson.fromJson(responseEntity.getBody(), new TypeToken<Response<ResponseTariffs>>() {}.getType());
        else {
            response.setError(Error.builder()
                    .code(responseEntity.getStatusCode().toString())
                    .message("service_is_unavailable").build());
            log.error("loan-service is unavailable");
        }

        return response;
    }

    @Override
    public Response<ResponseOrders> getOrdersById(Long userId) {

        final UserDTO user = UserDTO.builder().userId(userId).build();

        ResponseEntity<String> responseEntity = webClientBuilder.build()
                .post().uri("/loan-service/getOrders")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(user), UserDTO.class)
                .retrieve()
                .onStatus(HttpStatusCode::isError,response -> {
                    return Mono.empty();})
                .toEntity(String.class)
                .flatMap(response -> {
                    return Mono.just(ResponseEntity
                            .status(response.getStatusCode())
                            .body(response.getBody()));
                })
                .block();
        Response<ResponseOrders> response = new Response<>();

        if(responseEntity.getStatusCode() == HttpStatus.OK)
            response = gson.fromJson(responseEntity.getBody(), new TypeToken<Response<ResponseOrders>>() {}.getType());
        else {
            response.setError(Error.builder()
                    .code(responseEntity.getStatusCode().toString())
                    .message("service_is_unavailable").build());
            log.error("loan-service is unavailable");
        }
        return response;
    }

    @Override
    public Response<ResponseOrderId> newOrder(LoanOrderToCreate loanOrderToCreate) {
        ResponseEntity<String> responseEntity = webClientBuilder.build()
                .post().uri("/loan-service/order")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(loanOrderToCreate), LoanOrderToCreate.class)
                .retrieve()
                .onStatus(HttpStatusCode::isError,response -> {
                    return Mono.empty();})
                .toEntity(String.class)
                .flatMap(response -> {
                    return Mono.just(ResponseEntity
                            .status(response.getStatusCode())
                            .body(response.getBody()));
                })
                .block();

        return getResponse_whenThisJsonAndStatusCode(
                responseEntity.getBody(),
                responseEntity.getStatusCode(),
                ResponseOrderId.class
        );
    }

    @Override
    public Response<ResponseStatus> getStatus(UUID orderId) {

        ResponseEntity<String> responseEntity = webClientBuilder.build()
                .get().uri("/loan-service/getStatusOrder?orderId=" + orderId)
                .retrieve()
                .onStatus(HttpStatusCode::isError,response -> {
                    return Mono.empty();})
                .toEntity(String.class)
                .flatMap(response -> {
                    return Mono.just(ResponseEntity
                            .status(response.getStatusCode())
                            .body(response.getBody()));
                })
                .block();

        Response<ResponseStatus> response = getResponse_whenThisJsonAndStatusCode(
                responseEntity.getBody(),
                responseEntity.getStatusCode(),
                ResponseStatus.class
        );
        return response;
    }

    @Override
    public Error deleteOrder(LoanOrderToDelete loanOrderToDelete) {
        ResponseEntity<String> responseEntity = webClientBuilder.build()
                .method(HttpMethod.DELETE)
                .uri("/loan-service/deleteOrder")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(loanOrderToDelete), LoanOrderToDelete.class)
                .retrieve()
                .onStatus(HttpStatusCode::isError,response -> {
                    return Mono.empty();})
                .toEntity(String.class)
                .flatMap(response -> {
                    return Mono.just(ResponseEntity
                            .status(response.getStatusCode())
                            .body(response.getBody()));
                })
                .block();

        Error error = new Error();

        if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            error = gson.fromJson(responseEntity.getBody(), Error.class);
            log.warn("Error: Code - " + error.getCode() + ", Message - " + error.getMessage());
        }
        else if (responseEntity.getStatusCode() != HttpStatus.OK) {
            error = Error.builder()
                    .code(responseEntity.getStatusCode().toString())
                    .message("service_is_unavailable").build();
            log.error("loan-service is unavailable");
        }
        else {
            error.setCode(HttpStatus.OK.toString());
            log.info("Order successful deleted");
        }

        return error;
    }

    public <T> Response<T> getResponse_whenThisJsonAndStatusCode(String json, HttpStatusCode statusCode,  Class<T> tClass) {

        Response<T> response = new Response<>();

        if (statusCode == HttpStatus.OK) {
            Type responseType = TypeToken.getParameterized(Response.class, tClass).getType();
            response = gson.fromJson(json, responseType);
        }
        else if (statusCode == HttpStatus.BAD_REQUEST){
            response = gson.fromJson(json, Response.class);
            log.warn("Code - " + response.getError().getCode() + ", Message - " + response.getError().getMessage());
        }
        else {
            response.setError(Error.builder()
                    .code(statusCode.toString())
                    .message("service_is_unavailable")
                    .build());
            log.error("loan-service is unavailable");
        }
        return response;
    }
}
