package com.example.banking_web_service.controllers;

import com.example.banking_web_service.configuration.AccountDetails;
import com.example.banking_web_service.configuration.PrincipalDetailsArgumentResolver;
import com.example.banking_web_service.dto.MoneyToCard;
import com.example.banking_web_service.entities.Account;
import com.example.banking_web_service.entities.CreditCard;
import com.example.banking_web_service.services.CardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@RunWith(MockitoJUnitRunner.class)
public class CardControllerTest {
    private MockMvc mockMvc;
    @Mock
    private CardService cardService;
    @InjectMocks
    private CardController cardController;
    public static CreditCard creditCardFirst=new CreditCard(
            1L, "4000003217809694", "08/2031", "432",
            2000,"Volodymyr Tuka","tuka@gmail.com");

    public static CreditCard creditCardSecond =new CreditCard(
            2L, "4000004300229048", "08/2031", "477",
            100,"Suit Tuka","suit@gmail.com");
    public MoneyToCard transaction1=new MoneyToCard("4000003217809694",50);



    private final ObjectMapper objectMapper=new ObjectMapper();
    private final ObjectWriter objectWriter=objectMapper.writer();
    public AccountDetails accountDetails;
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(cardController)
                .setCustomArgumentResolvers(new PrincipalDetailsArgumentResolver())
                .build();
        accountDetails=new AccountDetails(AccountControllerTest.accountFirst);
//
    }

    @Test
    public void addCreditCardTest_success() throws Exception {
//        Mockito.when(cardService.addCreditCard(accountDetails)).thenReturn(creditCardFirst.dto());
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = post("/bank/add/card")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isCreated());
    }
    @Test
    public void getInfoAboutCardTest_success() throws Exception {
//        Mockito.when(cardService.getInfoAboutCard(accountDetails)).thenReturn(creditCardFirst);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get("/bank/card/info")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk());
    }
    @Test
    public void getInfoAddMoneyTest_success() throws Exception {

        Mockito.when(cardService.addMoney(transaction1)).thenReturn(creditCardFirst.dto());
        String content=objectWriter.writeValueAsString(transaction1);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = post("/bank/card/balance/add/money")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance",is(creditCardFirst.dto().getBalance())));
    }
}
