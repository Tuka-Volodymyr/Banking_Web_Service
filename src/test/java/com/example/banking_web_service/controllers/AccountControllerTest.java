package com.example.banking_web_service.controllers;

import com.example.banking_web_service.configuration.AccountDetails;
import com.example.banking_web_service.configuration.PrincipalDetailsArgumentResolver;
import com.example.banking_web_service.dto.CreditCardDto;
import com.example.banking_web_service.entities.Account;
import com.example.banking_web_service.entities.CreditCard;
import com.example.banking_web_service.repositories.AccountInfoRepository;
import com.example.banking_web_service.services.AccountService;
import com.example.banking_web_service.services.CardService;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.ObjectWriter;


import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.*;


import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.hamcrest.Matchers.hasSize;
@RunWith(MockitoJUnitRunner.class)
public class AccountControllerTest {
    private MockMvc mockMvc;
    @Mock
    private AccountInfoRepository accountInfoRepository;
    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;
    public static Account accountFirst= new Account(1L, "Tuka", "tuka@gmail.com", "1111");
    public static Account accountSecond= new Account(1L, "Volodymyr Tuka", "tua@gmail.com", "1111");
    public static Account accountThird = new Account(1L, "Volodymyr Tuka", "tuka@gmal.com", "111");


    private final ObjectMapper objectMapper=new ObjectMapper();
    private final ObjectWriter objectWriter=objectMapper.writer();


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(accountController)
                .setCustomArgumentResolvers(new PrincipalDetailsArgumentResolver())
                .build();
    }


    @Test
    public void addAccountTest_success() throws Exception {

        Mockito.when(accountService.addAccount(accountFirst)).thenReturn("Account was created by "+ accountFirst.getFullName());
        String content=objectWriter.writeValueAsString(accountFirst);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = post("/bank/addAccount")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$",is("Account was created by Tuka")));
    }
    @Test
    public  void addAccountTest_fail() throws Exception{
        String content=objectWriter.writeValueAsString(accountThird);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = post("/bank/addAccount")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isBadRequest());
    }
}

