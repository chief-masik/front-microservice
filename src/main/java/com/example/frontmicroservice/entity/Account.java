package com.example.frontmicroservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private String password;
    @NonNull
    private String role;
}
