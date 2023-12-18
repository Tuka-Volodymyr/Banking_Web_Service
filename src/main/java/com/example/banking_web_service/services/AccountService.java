package com.example.banking_web_service.services;
import com.example.banking_web_service.repositories.repositoriesImpl.AccountData;
import com.example.banking_web_service.repositories.repositoriesImpl.SentCodeData;
import com.example.banking_web_service.model.dto.ChangePassword;
import com.example.banking_web_service.model.entities.Account;
import com.example.banking_web_service.model.entities.GlobalEvent;
import com.example.banking_web_service.model.entities.SentCode;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Data
public class AccountService {
    @Autowired
    private AccountData accountData;
    @Autowired
    private SentCodeData sentCodeData;
    @Autowired
    private EventService eventService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender javaMailSender;

    public GlobalEvent addAccount(Account account){
        accountData.existAccount(account);
        account.setLockStatus(false);
        account.setFailedAttempts(0);
        account.setRoles(List.of("ROLE_CUSTOMER"));
        if(accountData.accountTableEmpty())account.setRoles(List.of("ROLE_ADMINISTRATOR"));
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountData.saveAccount(account);
        return eventService.createAccount(account.getEmail());

    }
    public String changePassword(ChangePassword changePassword, String code){
        Account account=accountData.findAccountByEmail(changePassword.getEmail());
        Optional<SentCode> trueCode=sentCodeData.findCodeByAccountId(account.getId());
        sentCodeData.deleteCode();
        if(code!=null&&trueCode.isPresent()) {
            if(!trueCode.get().getCode().equals(code))
                throw  new ResponseStatusException(HttpStatus.BAD_REQUEST,"Code is incorrect!");
            account.setPassword(passwordEncoder.encode(changePassword.getPassword()));
            accountData.saveAccount(account);
            eventService.changePassword(account.getEmail());
            return account.getFullName() + " your password was changed!";
        }
        return sendCodeToEmail(account,changePassword);
    }
    public String sendCodeToEmail(Account account,ChangePassword changePassword){
        SentCode newSentCod=new SentCode(account.getId());
        sentCodeData.saveCode(newSentCod);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(changePassword.getEmail());
        message.setSubject("Password Reset");
        message.setText("Code: "+newSentCod.getCode());
        javaMailSender.send(message);
        return "Cod was sent to email: "+changePassword.getEmail()+"!\nEnter code to query params!";
    }

    public void resetAccountAttempts(String email){
        Account account=accountData.findAccountByEmail(email);
        account.setFailedAttempts(0);
        accountData.saveAccount(account);
    }

    public void increaseFailedAttempts(Account account){
        eventService.authenticationFailed(account.getEmail());
        account.setFailedAttempts(account.getFailedAttempts()+1);
        accountData.saveAccount(account);
    }
    public GlobalEvent lockAccount(String email) {
        Account account = accountData.findAccountByEmail(email);
        if(account.getRoles().contains("ROLE_ADMINISTRATOR"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Admin can`t be locked!");
        if (!account.isLockStatus()) {
            account.setLockStatus(true);
            account.setFailedAttempts(0);
            accountData.saveAccount(account);
            return eventService.lockAccount(account);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account hasn`t unlocked!");
    }
    public GlobalEvent unlockAccount(String email){
        Account account=accountData.findAccountByEmail(email);
        if(account.isLockStatus()){
            account.setLockStatus(false);
            account.setFailedAttempts(0);
            accountData.saveAccount(account);
            return eventService.unlockAccount(account);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Account hasn`t locked!");
    }

}
