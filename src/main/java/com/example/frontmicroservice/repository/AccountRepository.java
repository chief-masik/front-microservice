package com.example.frontmicroservice.repository;

import com.example.frontmicroservice.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    String FIND_BY_NAME = "SELECT * FROM ACCOUNT WHERE NAME=?";
    String SAVE = "INSERT INTO ACCOUNT (NAME, PASSWORD, ROLE) VALUES (:name, :password, :role)";

    @Query(value = FIND_BY_NAME, nativeQuery = true)
    public Account findAccountByName(String name);
    @Modifying
    @Query(value = SAVE, nativeQuery = true)
    public int saveAccount(String name, String password, String role);
}
