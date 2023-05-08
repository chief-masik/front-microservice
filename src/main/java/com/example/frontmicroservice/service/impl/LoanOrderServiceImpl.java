package com.example.frontmicroservice.service.impl;

import com.example.frontmicroservice.adapter.LocalDateTimeAdapter;
import com.example.frontmicroservice.entity.Account;
import com.example.frontmicroservice.entity.LoanOrderToCreate;

import com.example.frontmicroservice.entity.LoanOrderToDelete;
import com.example.frontmicroservice.repository.AccountRepository;
import com.example.frontmicroservice.response.Response;
import com.example.frontmicroservice.response.data.ResponseOrderId;
import com.example.frontmicroservice.response.data.ResponseOrders;
import com.example.frontmicroservice.response.data.ResponseStatus;
import com.example.frontmicroservice.response.data.ResponseTariffs;
import com.example.frontmicroservice.response.error.Error;
import com.example.frontmicroservice.service.LoanOrderService;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.internal.bind.DefaultDateTypeAdapter;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
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
        log.warn(responseEntity.getBody());

        Response<ResponseTariffs> response = new Response<>();

        if(responseEntity.getStatusCode() == HttpStatus.OK)
            response = gson.fromJson(responseEntity.getBody(), new TypeToken<Response<ResponseTariffs>>() {}.getType());
        else
            response.setError(Error.builder()
                    .code(responseEntity.getStatusCode().toString())
                    .message("service_is_unavailable").build());

        return response;
    }

    @Override
    public Response<ResponseOrders> getOrdersById() {

        final Long id = ((Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        ResponseEntity<String> responseEntity = webClientBuilder.build()
                .post().uri("/loan-service/getOrders")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(id), Long.class)
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
        else
            response.setError(Error.builder()
                    .code(responseEntity.getStatusCode().toString())
                    .message("service_is_unavailable").build());

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

        Response<ResponseOrderId> response = getResponse_whenThisJsonAndStatusCode(
                responseEntity.getBody().toString(),
                responseEntity.getStatusCode(),
                ResponseOrderId.class
        );

        return response;
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
                responseEntity.getBody().toString(),
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

        if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST)
            error = gson.fromJson(responseEntity.getBody(), Error.class);
        else if (responseEntity.getStatusCode() != HttpStatus.OK)
            error = Error.builder()
                    .code(responseEntity.getStatusCode().toString())
                    .message("service_is_unavailable")
                    .build();
        else
            error.setCode(HttpStatus.OK.toString());

        return error;
    }

    public <T> Response<T> getResponse_whenThisJsonAndStatusCode(String json, HttpStatusCode statusCode,  Class<T> tClass) {

        Response<T> response = new Response<>();
        log.info("******************************************************************************");
        log.warn(json);
        log.warn(tClass.toString());
        log.info("******************************************************************************");

        if (statusCode == HttpStatus.OK) {
            Type responseType = TypeToken.getParameterized(Response.class, tClass).getType();// если 200
            response = gson.fromJson(json, responseType);
        }
        else if (statusCode == HttpStatus.BAD_REQUEST){         // если 400
            Error error = gson.fromJson(json, Error.class);
            response.setError(error);
        }
        else                                                    // если что-то другое
            response = new Response<>(
                    null,
                    Error.builder()
                            .code(statusCode.toString())
                            .message("service_is_unavailable").build()
            );

        log.warn(String.valueOf(response));
        log.info("******************************************************************************");

        return response;
    }
}
