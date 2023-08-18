package com.example.banking_web_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoneyToCard {
    @Size(min = 16,max = 16)
    private String card;
    @NotBlank
    private float amount;
}
