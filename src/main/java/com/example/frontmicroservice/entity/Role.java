package com.example.frontmicroservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;

@Data
@Entity
@NoArgsConstructor
public class Role implements GrantedAuthority {
    @Id
    Long id;
    @NonNull
    String name;

    @Override
    public String getAuthority() {
        return getName();
    }
}
