package com.example.frontmicroservice.service;

import com.example.frontmicroservice.entity.FormAccountDTO;
import com.example.frontmicroservice.entity.LoanOrderCreated;
import com.example.frontmicroservice.entity.LoanOrderToCreate;

public interface AccountService {
    public String saveAccount(FormAccountDTO accountDTO);
}
