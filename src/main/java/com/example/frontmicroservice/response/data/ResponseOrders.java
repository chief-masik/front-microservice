package com.example.frontmicroservice.response.data;

import com.example.frontmicroservice.entity.LoanOrderCreated;
import com.example.frontmicroservice.entity.Tariff;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseOrders {
    private List<LoanOrderCreated> orders;
}
