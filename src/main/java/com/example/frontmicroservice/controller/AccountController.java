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
import jakarta.validation.Valid;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String getRegistration(Model model) {

        model.addAttribute("accountForm", new FormAccountDTO());
        return "registration";
    }

    @PostMapping("/registration")
    public String postRegistration(@ModelAttribute("accountForm") @Valid FormAccountDTO accountForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        String problem = accountService.saveAccount(accountForm);

        if (problem != null) {
            redirectAttributes.addFlashAttribute("errors", problem);
            return "redirect:http://localhost:8765/account/registration";
        }
        else
            return "start-page";
    }

    @GetMapping("/start-page")
    public String startPage(@RequestParam(name = "error", required = false) String error, Model model) {
        model.addAttribute("error", error);
        return "start-page";
    }

    @GetMapping("/homepage")
    public String homepage(Model model) {

        userId = ((Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        model.addAttribute("selectTariff", new SelectTariff());
        model.addAttribute("loanOrderToDelete", new LoanOrderToDelete());

        Response<ResponseTariffs> responseTariffs = loanOrderService.getAllTariff();            // тарифы
        model.addAttribute("tariffs", responseTariffs.getData().getTariffs());
        Response<ResponseOrders> responseOrders = loanOrderService.getOrdersById(userId);       // заявки
        model.addAttribute("orders", responseOrders.getData().getOrders());

        return "homepage";
    }

    @PostMapping("/newOrder")
    public String newOrder(@ModelAttribute SelectTariff selectTariff, RedirectAttributes redirectAttributes) {

        LoanOrderToCreate loanOrderToCreate = LoanOrderToCreate.builder()
                .userId(userId)
                .tariffId(selectTariff.getTariffId()).build();

        Response<ResponseOrderId> response = loanOrderService.newOrder(loanOrderToCreate);
        if (response.getError() != null)
            redirectAttributes.addFlashAttribute("problem", response.getError().getCode());

        return "redirect:http://localhost:8765/account/homepage";
    }

    @PostMapping("/deleteOrder")
    public String deleteOrder(@ModelAttribute("accountForm") LoanOrderToDelete loanOrderToDelete, RedirectAttributes redirectAttributes) {

        loanOrderToDelete.setUserId(userId);

        Error error = loanOrderService.deleteOrder(loanOrderToDelete);
        if (!error.getCode().equals(HttpStatus.OK.toString()))
            redirectAttributes.addFlashAttribute("problem", error.getCode());

        return "redirect:http://localhost:8765/account/homepage";
    }

    @GetMapping("/getStatus")
    public String getStatus() {

        UUID uuid = UUID.fromString("72345817-3fcf-4bd7-aaf6-276960262e9c");
        Response<ResponseStatus> response = loanOrderService.getStatus(uuid);

        return "start-page";
    }
}
