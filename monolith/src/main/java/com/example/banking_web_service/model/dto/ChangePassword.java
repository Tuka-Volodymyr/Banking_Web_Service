package com.example.banking_web_service.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePassword {
    @Email
    @Pattern(regexp = ".+\\.com")
    private String email;
    @Size(min = 4)
    private String password;
}
