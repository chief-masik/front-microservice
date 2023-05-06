package com.example.frontmicroservice.service;

import com.example.frontmicroservice.entity.FormAccountDTO;
import com.example.frontmicroservice.entity.LoanOrderCreatedDTO;
import com.example.frontmicroservice.entity.LoanOrderToCreateDTO;

public interface AccountService {
    public String saveAccount(FormAccountDTO accountDTO);
    public LoanOrderCreatedDTO createLoanOrder(LoanOrderToCreateDTO loanOrderDTO);
}
