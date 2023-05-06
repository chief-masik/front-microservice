package com.example.frontmicroservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class FormAccountDTO {
    @NonNull
    private String name;
    @NonNull
    private String password;
}