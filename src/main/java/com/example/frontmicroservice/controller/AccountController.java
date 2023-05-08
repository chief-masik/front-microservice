package com.example.frontmicroservice.controller;

import com.example.frontmicroservice.entity.FormAccountDTO;
import com.example.frontmicroservice.entity.LoanOrderToCreate;
import com.example.frontmicroservice.entity.LoanOrderToDelete;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

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
            log.error("bindingResult has errors: " + problem);
            return "redirect:registration?problem=" + problem;
        }
        else {
            problem = accountService.saveAccount(accountForm);

            if (problem != null) return "redirect:http://localhost:8765/account/registration?problem=" + problem;
            else return "start-page";
        }
    }

    @GetMapping("/start-page")
    public String startPage() {

        return "start-page";
    }

    @GetMapping("/homepage")
    public String homepage(Model model) {
        Response<ResponseTariffs> responseTariffs = loanOrderService.getAllTariff();
        Response<ResponseOrders> responseOrders = loanOrderService.getOrdersById();

        model.addAttribute("tariffs", responseTariffs.getData().getTariffs());
        model.addAttribute("orders", responseOrders.getData().getOrders());

        return "/homepage";
    }

    @PostMapping("/newOrder")
    public String newOrder(@ModelAttribute("type") Long id, Model model) {

        LoanOrderToCreate loanOrderToCreate = LoanOrderToCreate.builder().userId(id).tariffId(1L).build();
        Response<ResponseOrderId> response = loanOrderService.newOrder(loanOrderToCreate);

        return "start-page";
    }
    @GetMapping("/getStatus")
    public String getStatus() {

        UUID uuid = UUID.fromString("72345817-3fcf-4bd7-aaf6-276960262e9c");
        Response<ResponseStatus> response = loanOrderService.getStatus(uuid);

        return "start-page";
    }
    @PostMapping("/deleteOrder")
    public String deleteOrder(@ModelAttribute("accountForm") @Valid LoanOrderToDelete loanOrderToDelete, BindingResult bindingResult) {

        Error error = loanOrderService.deleteOrder(loanOrderToDelete);

        if(error.getCode() == HttpStatus.OK.toString())
            System.out.println("/////////////////");

        return "start-page";
    }
}
