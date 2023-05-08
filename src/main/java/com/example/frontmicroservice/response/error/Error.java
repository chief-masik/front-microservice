package com.example.frontmicroservice.response.error;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Error {
    @NonNull
    private String code;
    private String message;
}
