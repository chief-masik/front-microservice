package com.example.frontmicroservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** обертка для передачи информации в контроллер о выбранном тарифе на сайта */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectTariff {
    private Long tariffId;
}
