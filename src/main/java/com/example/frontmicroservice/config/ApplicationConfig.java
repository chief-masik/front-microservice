package com.example.frontmicroservice.config;

import com.example.frontmicroservice.constant.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableWebSecurity
public class ApplicationConfig {
    @Autowired
    UserDetailsService userDetailsService;

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder().baseUrl("http://localhost:8765");
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf().and()
                .authorizeHttpRequests()
                .requestMatchers("/start-page", "/new-order").permitAll()
                .requestMatchers("/account/homepage").authenticated()
                //.hasAnyRole("USER", "ADMIN")
                .anyRequest().permitAll()
                .and().formLogin()
                    .loginPage("http://localhost:8765/account/start-page")
                    .loginProcessingUrl("/account/perform_login")
                    .defaultSuccessUrl("http://localhost:8765/account/homepage")
                .and().logout()
                    .logoutUrl("/account/logout")
                    .logoutSuccessUrl("http://localhost:8765/account/start-page")
                .and().authenticationManager(authenticationManager(httpSecurity)).build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and().build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
