package com.example.frontmicroservice.response;

import com.example.frontmicroservice.response.error.Error;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {
    private T data;
    private Error error;
}
