package com.example.frontmicroservice.entity;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@RequiredArgsConstructor
public class LoanOrderToCreate {
    @NonNull
    private Long userId;
    @NonNull
    private Long tariffId;
}