package com.example.frontmicroservice.repository;

import com.example.frontmicroservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Long> {
    String FIND_BY_NAME = "SELECT * FROM ROLE WHERE NAME=?";

    @Query(value = FIND_BY_NAME, nativeQuery = true)
    public Role findRoleByName(String name);
}
