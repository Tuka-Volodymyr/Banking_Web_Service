package com.example.banking_web_service.dto;

import lombok.Data;

@Data
public class ChangeTurnoverLimit {
    private float limitOfTurnover;
    private String email;
}
