package com.example.frontmicroservice.controller;

import com.example.frontmicroservice.entity.*;
import com.example.frontmicroservice.response.Response;
import com.example.frontmicroservice.response.data.ResponseOrderId;
import com.example.frontmicroservice.response.data.ResponseOrders;
import com.example.frontmicroservice.response.data.ResponseStatus;
import com.example.frontmicroservice.response.data.ResponseTariffs;
import com.example.frontmicroservice.response.error.Error;
import com.example.frontmicroservice.service.AccountService;
import com.example.frontmicroservice.service.LoanOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {
    private Long userId;
    @Autowired
    AccountService accountService;
    @Autowired
    LoanOrderService loanOrderService;

    @GetMapping("/registration")
    public String getRegistration(@RequestParam(name = "problem", required = false) String problem, Model model) {

        model.addAttribute("accountForm", new FormAccountDTO());
        if(problem != null)
            model.addAttribute("problem", problem);
        return "registration";
    }

    @PostMapping("/registration")
    public String postRegistration(@ModelAttribute("accountForm") @Valid FormAccountDTO accountForm, BindingResult bindingResult) {
        String problem;
        log.info("bello!!!  " + bindingResult.toString());
        if (bindingResult.hasErrors()) {
            problem = bindingResult.getAllErrors().get(0).getDefaultMessage();
            log.error("accountForm не валидна: " + problem);
            return "redirect:registration?problem=" + problem;
        }
        else {
            problem = accountService.saveAccount(accountForm);

            if (problem != null)
                return "redirect:http://localhost:8765/account/registration?problem=" + problem;
            else
                return "start-page";
        }
    }

    @GetMapping("/start-page")
    public String startPage() {
        return "start-page";
    }

    @GetMapping("/homepage")
    public String homepage(@RequestParam(name = "problem", required = false) String problem, Model model) {

        userId = ((Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        model.addAttribute("selectTariff", new SelectTariff());
        model.addAttribute("loanOrderToDelete", new LoanOrderToDelete());
        if(problem != null)
            model.addAttribute("problem", problem);

        Response<ResponseTariffs> responseTariffs = loanOrderService.getAllTariff();            // тарифы
        model.addAttribute("tariffs", responseTariffs.getData().getTariffs());
        Response<ResponseOrders> responseOrders = loanOrderService.getOrdersById(userId);       // заявки
        model.addAttribute("orders", responseOrders.getData().getOrders());

        return "homepage";
    }

    @PostMapping("/newOrder")
    public String newOrder(@ModelAttribute SelectTariff selectTariff) {

        String problem = "";
        LoanOrderToCreate loanOrderToCreate = LoanOrderToCreate.builder()
                .userId(userId)
                .tariffId(selectTariff.getTariffId()).build();

        Response<ResponseOrderId> response = loanOrderService.newOrder(loanOrderToCreate);
        if (response.getError() != null)
            problem = "?problem=" + response.getError().getCode();

        return "redirect:http://localhost:8765/account/homepage" + problem;
    }

    @PostMapping("/deleteOrder")
    public String deleteOrder(@ModelAttribute("accountForm") LoanOrderToDelete loanOrderToDelete) {

        String problem = "";
        loanOrderToDelete.setUserId(userId);

        Error error = loanOrderService.deleteOrder(loanOrderToDelete);
        if (!error.getCode().equals(HttpStatus.OK.toString()))
            problem = "?problem=" + error.getCode();

        return "redirect:http://localhost:8765/account/homepage" + problem;
    }

    @GetMapping("/getStatus")
    public String getStatus() {

        UUID uuid = UUID.fromString("72345817-3fcf-4bd7-aaf6-276960262e9c");
        Response<ResponseStatus> response = loanOrderService.getStatus(uuid);

        return "start-page";
    }
}
