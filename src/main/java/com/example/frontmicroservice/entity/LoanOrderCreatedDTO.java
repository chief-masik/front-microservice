package com.example.frontmicroservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class LoanOrderCreatedDTO {
    @NonNull
    private Long account_id;
    @NonNull
    private String status;
    @NonNull
    UUID orderId;
}
