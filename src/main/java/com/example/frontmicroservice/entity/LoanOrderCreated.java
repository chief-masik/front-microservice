package com.example.frontmicroservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class LoanOrderCreated {
    @NonNull
    private UUID orderId;
    @NonNull
    private Long tariffId;
    @NonNull
    private String status;
    @NonNull
    private LocalDateTime timeInsert;
    @NonNull
    private LocalDateTime timeUpdate;

}
