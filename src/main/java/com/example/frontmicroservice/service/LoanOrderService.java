package com.example.frontmicroservice.service;

import com.example.frontmicroservice.entity.LoanOrderToCreate;
import com.example.frontmicroservice.entity.LoanOrderToDelete;
import com.example.frontmicroservice.response.Response;
import com.example.frontmicroservice.response.data.ResponseOrderId;
import com.example.frontmicroservice.response.data.ResponseOrders;
import com.example.frontmicroservice.response.data.ResponseStatus;
import com.example.frontmicroservice.response.data.ResponseTariffs;
import com.example.frontmicroservice.response.error.Error;

import java.util.UUID;

public interface LoanOrderService {
    public Response<ResponseTariffs> getAllTariff();
    public Response<ResponseOrders> getOrdersById(Long userId);
    public Response<ResponseOrderId> newOrder(LoanOrderToCreate loanOrderToCreate);
    public Response<ResponseStatus> getStatus(UUID orderId);
    public Error deleteOrder(LoanOrderToDelete loanOrderToDelete);
}
