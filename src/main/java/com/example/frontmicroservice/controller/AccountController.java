package com.example.frontmicroservice.controller;

import com.example.frontmicroservice.entity.Account;
import com.example.frontmicroservice.entity.FormAccountDTO;
import com.example.frontmicroservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/registration")
    public String getRegistration(@RequestParam(name = "problem", required = false) String problem, Model model) {

        model.addAttribute("accountForm", new FormAccountDTO());
        if(problem != null)
            model.addAttribute("problem", problem);
        return "registration";
    }

    @PostMapping("/registration")
    public String postRegistration(@ModelAttribute("accountForm") FormAccountDTO formAccountDTO) {

        String problem = accountService.saveAccount(formAccountDTO);

        if (problem != null){
            return "redirect:registration?problem=" + problem;
        }
        return "start-page";
    }

    @GetMapping("/start-page")
    public String startPage() {

        return "start-page";
    }

    @GetMapping("/homepage")
    public String homepage() {

        return "/homepage";
    }

    @PostMapping("/")
    public String newOrder(Model model) {

        return "redirect:homepage";
    }
}
