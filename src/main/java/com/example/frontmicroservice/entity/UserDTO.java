package com.example.frontmicroservice.entity;

import lombok.*;

/** обертка для отправки запроса к loan-service по адресу "/getOrders" */
@Data
@Builder
@NoArgsConstructor
@RequiredArgsConstructor
public class UserDTO {
    @NonNull
    private Long userId;
}
