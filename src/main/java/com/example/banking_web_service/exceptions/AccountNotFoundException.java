package com.example.banking_web_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "Account not found")
public class AccountNotFoundException extends RuntimeException{
}
