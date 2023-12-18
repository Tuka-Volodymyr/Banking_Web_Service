package com.example.banking_web_service.configuration.handler;

import com.example.banking_web_service.model.entities.Account;
import com.example.banking_web_service.repositories.AccountInfoRepository;
import com.example.banking_web_service.services.AccountService;
import com.example.banking_web_service.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;


import java.util.Optional;

@Component
public class AuthenticationEventHandler {
    @Autowired
    private AccountService accountService;
    @Autowired
    private EventService eventService;
    @Autowired
    private AccountInfoRepository accountInfoRepository;
    public final int maxFailedAttempts = 3;
    @EventListener
    public void onSuccess( AuthenticationSuccessEvent success) {
        UserDetails details = (UserDetails) success.getAuthentication().getPrincipal();
        accountService.resetAccountAttempts(details.getUsername());
    }
    @EventListener
    public void onFailure( AbstractAuthenticationFailureEvent failures) {
        String email = (String) failures.getAuthentication().getPrincipal();
        Optional<Account> optionalAccount=accountInfoRepository.findByEmailIgnoreCase(email);
        if (optionalAccount.isPresent()) {
            Account account=optionalAccount.get();
            if(account.isLockStatus()){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Account is lock!!!");
            }
            accountService.increaseFailedAttempts(account);
            if (account.getFailedAttempts() >= maxFailedAttempts&&!account.getRoles().contains("ROLE_ADMINISTRATOR")) {
                accountService.lockAccount(account.getEmail());
                eventService.bruteForce(account.getEmail());
            }
        } else {
            eventService.authenticationFailed(email);
        }
    }
}
