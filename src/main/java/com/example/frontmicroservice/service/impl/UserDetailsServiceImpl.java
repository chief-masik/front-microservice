package com.example.frontmicroservice.service.impl;

import com.example.frontmicroservice.entity.Account;
import com.example.frontmicroservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findAccountByName(username);

        if(account == null)
            throw new UsernameNotFoundException("User not found");

        return User.builder()
                .username(account.getName())
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
    }
}