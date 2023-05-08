package com.example.frontmicroservice.entity;

import lombok.*;

import java.util.UUID;

@Data
@Builder    // для тестов
@RequiredArgsConstructor
public class LoanOrderToDelete {
    @NonNull
    private Long userId;
    @NonNull
    private UUID orderId;
}
