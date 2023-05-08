package com.example.frontmicroservice.service.impl;

import com.example.frontmicroservice.constant.ProblemEnum;
import com.example.frontmicroservice.constant.RoleEnum;
import com.example.frontmicroservice.entity.*;
import com.example.frontmicroservice.repository.AccountRepository;
import com.example.frontmicroservice.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public String saveAccount(FormAccountDTO formAccountDTO) {

        if (accountRepository.findAccountByName(formAccountDTO.getName()) != null)
            return ProblemEnum.USERNAME_NOT_AVAILABLE.toString();

        int a = accountRepository.saveAccount(formAccountDTO.getName(), bCryptPasswordEncoder.encode(formAccountDTO.getPassword()), RoleEnum.USER.toString());

        if (a != 1)
            return ProblemEnum.SOMETHING_WENT_WRONG.toString();

        return null;
    }

}
