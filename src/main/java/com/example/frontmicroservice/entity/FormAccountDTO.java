package com.example.frontmicroservice.entity;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormAccountDTO {
    @NotBlank(message = "name_empty")
    @Pattern(regexp = "^[a-zA-Z0-9а-яА-Я. _-]{4,15}$", message = "incorrect_name")
    private String name;
    @NotBlank(message = "password_empty")
    @Pattern(regexp = "^[a-zA-Z0-9а-яА-Я.,:;_@$!%?&+-]{3,32}$", message = "incorrect_password")
    private String password;
}