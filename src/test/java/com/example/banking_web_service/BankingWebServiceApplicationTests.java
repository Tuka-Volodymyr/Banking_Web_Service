package com.example.banking_web_service;

import com.example.banking_web_service.controllers.AccountControllerTest;
import com.example.banking_web_service.controllers.CardControllerTest;
import com.example.banking_web_service.services.AccountServiceTest;
import com.example.banking_web_service.services.CardServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

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
