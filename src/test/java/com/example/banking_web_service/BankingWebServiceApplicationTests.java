package com.example.banking_web_service;

import com.example.banking_web_service.controllers.AccountControllerTest;
import com.example.banking_web_service.controllers.CardControllerTest;
import com.example.banking_web_service.data.AccountData;
import com.example.banking_web_service.entities.Account;
import com.example.banking_web_service.entities.CreditCard;
import com.example.banking_web_service.services.AccountServiceTest;
import com.example.banking_web_service.services.CardService;
import com.example.banking_web_service.services.CardServiceTest;
import junit.textui.TestRunner;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AccountServiceTest.class,
        CardServiceTest.class,
        CardControllerTest.class,
        AccountControllerTest.class
})
class BankingWebServiceApplicationTests {

}
