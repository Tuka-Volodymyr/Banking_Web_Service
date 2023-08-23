package com.example.banking_web_service.services;


import com.example.banking_web_service.configuration.AccountDetails;
import com.example.banking_web_service.configuration.PrincipalDetailsArgumentResolver;
import com.example.banking_web_service.controllers.AccountControllerTest;
import com.example.banking_web_service.data.AccountData;
import com.example.banking_web_service.data.CardData;
import com.example.banking_web_service.dto.CreditCardDto;

import com.example.banking_web_service.dto.MoneyToCard;
import com.example.banking_web_service.entities.CreditCard;
import com.example.banking_web_service.entities.GlobalEvent;
import com.example.banking_web_service.entities.OperationEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;


import java.time.LocalDate;

import static com.example.banking_web_service.controllers.AccountControllerTest.*;
import static com.example.banking_web_service.controllers.CardControllerTest.*;

import static com.example.banking_web_service.services.AccountServiceTest.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class CardServiceTest {
    public static CreditCard firstCard=new CreditCard(
            1L,
            "4000003217809694",
            "08/2031",
            "432",
            2000,
            "Volodymyr Tuka",
            "tuka@gmail.com",
            false,
            0,
            400000);
    public static CreditCard secondCard =new CreditCard(
            2L,
            "4000004300229048",
            "08/2031",
            "477",
            0,
            "Suit Tuka",
            "suit@gmail.com",
            false,
            0,
            400000);
    public static CreditCard thirdCard =new CreditCard(
            3L,
            "4000004300229058",
            "08/2031",
            "477",
            900,
            "Rug Kutrapali",
            "rug@gmail.com",
            false,
            0,
            400000);
    public static CreditCard lockCreditCard =new CreditCard(
            4L,
            "4000004300229008",
            "08/2031",
            "477",
            100,
            "Ira Ride",
            "dif@gmal.com",
            true,
            0,
            400000);
    public  static OperationEvent operationEventWithTransaction500 =new OperationEvent(
            LocalDate.now().toString(),
            "TRANSACTION",
             500,
            1500,
            1400,
            "tuka@gmail.com",
            "rug@gmail.com",
            "/bank/card/balance/transfer/money"
    );
    public static GlobalEvent globalEventLockFirstCard =new GlobalEvent(
            LocalDate.now().toString(),
            "LOCK_CARD",
            firstCard.getEmailOfOwner(),
            String.format("Lock card: %s",firstCard.getCard()),
            "/bank/card/lock"
    );
    public static GlobalEvent globalEventUnlockCard =new GlobalEvent(
            LocalDate.now().toString(),
            "UNLOCK_CARD",
            accountWithLockCard.getEmail(),
            String.format("Unlock card: %s",lockCreditCard.getCard()),
            "/bank/card/unlock"
    );
    public static MoneyToCard transaction50=new MoneyToCard("4000003217809694",50);
    public static MoneyToCard transaction500=new MoneyToCard("4000004300229048",500);
    public static MoneyToCard transaction300000=new MoneyToCard("4000004300229048",300000);

    private CardData cardDataMock;
    private EventService eventServiceMock;
    private CardService cardService;
    private AccountData accountDataMock;
        @Before
        public void setUp() {
            cardDataMock = mock(CardData.class);
            eventServiceMock=mock(EventService.class);
            accountDataMock=mock(AccountData.class);
            cardService = new CardService();
            cardService.setCardData(cardDataMock);
            cardService.setEventService(eventServiceMock);
            cardService.setAccountData(accountDataMock);
        }
    @Test
    public void addCardTest_success(){
        when(accountDataMock.findAccountByEmail(secondAccount.getEmail())).thenReturn(secondAccount);
        CreditCardDto result = cardService.addCreditCard(secondAccount.getEmail());
        assertEquals(secondCard.dto().getBalance(),result.getBalance());
    }
    @Test
    public void addMoneyTest_success() {
        when(cardDataMock.findCardByCard(transaction50.getCard())).thenReturn(firstCard);
        when(cardDataMock.findCardByEmail(firstCard.getEmailOfOwner())).thenReturn(firstCard);
        CreditCardDto result = cardService.addMoney(transaction50);
        assertEquals(firstCard.dto(),result);
    }

    @Test
     public void transferMoneyTest_success(){
        AccountDetails accountDetails=new AccountDetails(firstAccount);
        when(cardDataMock.findCardByEmail(accountDetails.getUsername())).thenReturn(firstCard);
        when(cardDataMock.findCardByCard(transaction500.getCard())).thenReturn(thirdCard);
        when(cardDataMock.findCardByEmail(thirdCard.getEmailOfOwner())).thenReturn(thirdCard);
        when(eventServiceMock.transaction(firstCard,thirdCard,transaction500))
                .thenReturn(operationEventWithTransaction500);
        OperationEvent result=cardService.transferMoney(transaction500,accountDetails);
        assertEquals(operationEventWithTransaction500,result);
     }
    @Test
    public void transferMoneyTest_fail(){
        AccountDetails accountDetails=new AccountDetails(firstAccount);
        when(cardDataMock.findCardByEmail(accountDetails.getUsername())).thenReturn(firstCard);
        when(cardDataMock.findCardByCard(transaction300000.getCard())).thenReturn(thirdCard);
        when(cardDataMock.findCardByEmail(thirdCard.getEmailOfOwner())).thenReturn(thirdCard);
        when(eventServiceMock.transaction(firstCard,thirdCard,transaction300000))
                .thenReturn(operationEventWithTransaction500);
        ResponseStatusException result = assertThrows(ResponseStatusException.class, () -> {
            cardService.transferMoney(transaction300000,accountDetails);
        });
        assertEquals(HttpStatus.BAD_REQUEST,result.getStatusCode());
    }
    @Test
    public void lockCardTest_success(){
        AccountDetails accountDetails=new AccountDetails(secondAccount);
        when(cardDataMock.findCardByEmail(accountDetails.getUsername())).thenReturn(secondCard);
        when(eventServiceMock.lockCard(secondCard)).thenReturn(globalEventLockFirstCard);
        GlobalEvent result=cardService.lockCard(accountDetails.getUsername());
        assertEquals(result,globalEventLockFirstCard);
    }
    @Test
    public void lockCardTest_fail(){
        AccountDetails accountDetails=new AccountDetails(secondAccount);
        when(cardDataMock.findCardByEmail(accountDetails.getUsername())).thenReturn(secondCard);
        when(eventServiceMock.lockCard(secondCard)).thenReturn(globalEventLockFirstCard);
        ResponseStatusException result=assertThrows(ResponseStatusException.class,()->{
            cardService.lockCard(accountDetails.getUsername());
        });
        assertEquals(HttpStatus.BAD_REQUEST,result.getStatusCode());
    }
    @Test
    public void unlockCardTest_success(){
        AccountDetails accountDetails=new AccountDetails(accountWithLockCard);
        when(cardDataMock.findCardByEmail(accountDetails.getUsername())).thenReturn(lockCreditCard);
        when(eventServiceMock.unlockCard(lockCreditCard)).thenReturn(globalEventUnlockCard);
        GlobalEvent result=cardService.unlockCard(accountDetails.getUsername());
        assertEquals(result,globalEventUnlockCard);

    }
    @Test
    public void unlockCardTest_fail(){
        AccountDetails accountDetails=new AccountDetails(firstAccount);
        when(cardDataMock.findCardByEmail(accountDetails.getUsername())).thenReturn(firstCard);
        when(eventServiceMock.unlockCard(firstCard)).thenReturn(globalEventUnlockCard);
        ResponseStatusException result=assertThrows(ResponseStatusException.class,()->{
            cardService.unlockCard(accountDetails.getUsername());
        });
        assertEquals(HttpStatus.BAD_REQUEST,result.getStatusCode());
    }
}
