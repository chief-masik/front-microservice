package com.example.frontmicroservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class LoanOrderToCreateDTO {
    @NonNull
    private Long userId;
    @NonNull
    private Long tariffId;
}