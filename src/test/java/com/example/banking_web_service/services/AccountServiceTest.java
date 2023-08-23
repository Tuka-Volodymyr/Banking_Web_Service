package com.example.banking_web_service.services;
import com.example.banking_web_service.data.AccountData;
import com.example.banking_web_service.data.CardData;
import com.example.banking_web_service.data.SentCodeData;
import com.example.banking_web_service.dto.ChangePassword;
import com.example.banking_web_service.entities.Account;
import com.example.banking_web_service.entities.GlobalEvent;
import com.example.banking_web_service.entities.SentCode;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.Assert.assertEquals;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccountServiceTest {
    public static Account adminAccount= new Account(
            1L,
            "Ira Tuka",
            "ira@gmail.com",
            "1111",
            false,
            0,
            List.of("ROLE_ADMINISTRATOR"));
    public static Account firstAccount= new Account(
            2L,
            "Volodymyr Tuka",
            "tuka@gmail.com",
            "1111",
            false,
            0,
            List.of("ROLE_CUSTOMER"));
    public static Account secondAccount= new Account(
            3L,
            "Suit Tuka",
            "suit@gmail.com",
            "1111",
            false,
            0,
            List.of("ROLE_CUSTOMER"));
    public static Account thirdAccount= new Account(
            3L,
            "Gef Tuka",
            "gef@gmail.com",
            "1111",
            false,
            0,
            List.of("ROLE_CUSTOMER"));
    public static Account firstLockAccount= new Account(
            4L,
            "Ralo Tuka",
            "ralo@gmail.com",
            "1111",
            true,
            0,
            List.of("ROLE_CUSTOMER"));
    public static Account secondLockAccount= new Account(
            4L,
            "Ter Tuka",
            "ter@gmail.com",
            "1111",
            true,
            0,
            List.of("ROLE_CUSTOMER"));
    public static Account accountWithLockCard = new Account(
            5L,
            "Ira Ride",
            "dif@gmal.com",
            "1111",
            false,
            0,
            List.of("ROLE_CUSTOMER"));
    public static GlobalEvent globalEventAddAdminAccount =new GlobalEvent(
            LocalDate.now().toString(),
            "CREATE_ACCOUNT",
            "Anonymous",
            "ira@gmail.com",
            "/bank/addAccount"
    );
    public static  GlobalEvent globalEventThirdLockAccount =new GlobalEvent(
            LocalDate.now().toString(),
            "LOCK_ACCOUNT",
            "gef@gmail.com",
            String.format("Lock account: %s",thirdAccount.getFullName()),
            "/bank/lock/account"
    );
    public static  GlobalEvent globalEventUnlockSecondLockAccount =new GlobalEvent(
            LocalDate.now().toString(),
            "UNLOCK_ACCOUNT",
            "ralo@gmail.com",
            String.format("Lock account: %s",secondLockAccount.getFullName()),
            "/bank/unlock/account"
    );
    private static SentCode sentCode =new SentCode(
            1L,
            "2345",
            2L
    );
    private static ChangePassword changePassword=new ChangePassword(
            "tuka@gmail.com",
            "5555"
    );
    private AccountService accountService;
    private AccountData accountDataMock;
    private EventService eventServiceMock;
    private SentCodeData sentCodeDataMock;
    private PasswordEncoder passwordEncoderMock;
    @Before
    public void setUp() {
        accountDataMock=mock(AccountData.class);
        eventServiceMock=mock(EventService.class);
        sentCodeDataMock=mock(SentCodeData.class);
        passwordEncoderMock=mock(PasswordEncoder.class);
        accountService=new AccountService();
        accountService.setAccountData(accountDataMock);
        accountService.setEventService(eventServiceMock);
        accountService.setSentCodeData(sentCodeDataMock);
        accountService.setPasswordEncoder(passwordEncoderMock);
    }
    @Test
    public void addAccountTest_success(){
        when(accountDataMock.accountTableEmpty()).thenReturn(true);
        when(eventServiceMock.createAccount(adminAccount.getEmail())).thenReturn(globalEventAddAdminAccount);
        GlobalEvent result=accountService.addAccount(adminAccount);
        assertEquals(globalEventAddAdminAccount,result);
    }


    @Test
    public void changePasswordTest_success(){
        when(accountDataMock.findAccountByEmail(firstAccount.getEmail())).thenReturn(firstAccount);
        when(sentCodeDataMock.findCodeByAccountId(firstAccount.getId())).thenReturn(Optional.of(sentCode));
        String result=accountService.changePassword(changePassword,"2345");
        assertEquals(firstAccount.getFullName() + " your password was changed!",result);
    }
    @Test
    public void changePasswordTest_incorrectCode_fail(){
        when(accountDataMock.findAccountByEmail(firstAccount.getEmail())).thenReturn(firstAccount);
        when(sentCodeDataMock.findCodeByAccountId(firstAccount.getId())).thenReturn(Optional.of(sentCode));
        ResponseStatusException result=assertThrows(ResponseStatusException.class,()->{
            accountService.changePassword(changePassword,"9999");
        });
        assertEquals(HttpStatus.BAD_REQUEST,result.getStatusCode());
    }
    @Test
    public void lockAccountTest_success(){
        when(accountDataMock.findAccountByEmail(thirdAccount.getEmail())).thenReturn(thirdAccount);
        when(eventServiceMock.lockAccount(thirdAccount)).thenReturn(globalEventThirdLockAccount);
        GlobalEvent result=accountService.lockAccount(thirdAccount.getEmail());
        assertEquals(globalEventThirdLockAccount,result);
    }
    @Test
    public void lockAccountTest_alreadyLock_fail(){
        when(accountDataMock.findAccountByEmail(firstLockAccount.getEmail())).thenReturn(firstLockAccount);
        when(eventServiceMock.lockAccount(firstLockAccount)).thenReturn(globalEventThirdLockAccount);
        ResponseStatusException result=assertThrows(ResponseStatusException.class,()->{
            accountService.lockAccount(firstLockAccount.getEmail());
        });
        assertEquals(HttpStatus.BAD_REQUEST,result.getStatusCode());
    }
    @Test
    public void lockAccountTest_withAdminRole_fail(){
        when(accountDataMock.findAccountByEmail(adminAccount.getEmail())).thenReturn(adminAccount);
        when(eventServiceMock.lockAccount(adminAccount)).thenReturn(globalEventThirdLockAccount);
        ResponseStatusException result=assertThrows(ResponseStatusException.class,()->{
            accountService.lockAccount(adminAccount.getEmail());
        });
        assertEquals(HttpStatus.BAD_REQUEST,result.getStatusCode());
    }
    @Test
    public void unlockAccountTest_success(){
        when(accountDataMock.findAccountByEmail(secondLockAccount.getEmail())).thenReturn(secondLockAccount);
        when(eventServiceMock.unlockAccount(secondLockAccount)).thenReturn(globalEventUnlockSecondLockAccount);
        GlobalEvent result=accountService.unlockAccount(secondLockAccount.getEmail());
        assertEquals(globalEventUnlockSecondLockAccount,result);
    }
    @Test
    public void unlockAccountTest_alreadyUnlock_fail(){
        when(accountDataMock.findAccountByEmail(secondAccount.getEmail())).thenReturn(secondAccount);
        when(eventServiceMock.unlockAccount(secondAccount)).thenReturn(globalEventThirdLockAccount);
        ResponseStatusException result=assertThrows(ResponseStatusException.class,()->{
            accountService.unlockAccount(secondAccount.getEmail());
        });
        assertEquals(HttpStatus.BAD_REQUEST,result.getStatusCode());
    }
}
