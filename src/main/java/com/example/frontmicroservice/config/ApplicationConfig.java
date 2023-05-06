package com.example.frontmicroservice.config;

import com.example.frontmicroservice.constant.RoleEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ApplicationConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .authorizeHttpRequests()
                .requestMatchers("/start-page").permitAll()
                .requestMatchers("/account/homepage").hasAnyRole("USER", "ADMIN")
                .anyRequest().permitAll()
                .and().formLogin()
                    .loginPage("/account/start-page")
                    .loginProcessingUrl("/perform_login")
                    .defaultSuccessUrl("/account/homepage")
                .and().logout()
                    .logoutSuccessUrl("/account/start-page")
                .and().authenticationManager(authenticationManager(httpSecurity)).build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService())
                .and().build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {

        UserDetails user1 = User.withUsername("test_user1")
                .password(passwordEncoder().encode("123456"))
                .roles(RoleEnum.USER.toString())
                .build();
        UserDetails adm = User.withUsername("masik")
                .password(passwordEncoder().encode("951753QQw"))
                .roles(RoleEnum.ADMIN.toString())
                .build();
        return new InMemoryUserDetailsManager(user1, adm);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
