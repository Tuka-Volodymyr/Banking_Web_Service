package com.example.banking_web_service.controllers;



import com.example.banking_web_service.services.AccountService;


import org.junit.Before;
import org.junit.Test;

import static com.example.banking_web_service.services.AccountServiceTest.*;
import static org.junit.Assert.*;

import org.mockito.MockitoAnnotations;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class AccountControllerTest {
private AccountController accountController;
private AccountService accountServiceMock;
    @Before
    public void setUp() {
        accountController=new AccountController();
        accountServiceMock=mock(AccountService.class);
        accountController.setAccountService(accountServiceMock);
    }
    @Test
    public void addAccountTest_success() {
        when(accountServiceMock.addAccount(firstAccount)).thenReturn(globalEventAddAdminAccount);
        ResponseEntity<?> result=accountController.addAccount(firstAccount);
        assertEquals(new ResponseEntity<>(globalEventAddAdminAccount, HttpStatus.CREATED),result);
}
}

