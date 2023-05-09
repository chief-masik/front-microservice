package com.example.frontmicroservice.entity;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class LoanOrderToDelete {

    private Long userId;
    @NonNull
    private UUID orderId;
}
