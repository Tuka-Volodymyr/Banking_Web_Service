package com.example.banking_web_service.configuration;

import com.example.banking_web_service.data.AccountData;
import com.example.banking_web_service.entities.Account;
import com.example.banking_web_service.repositories.AccountInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class InfoAccountService implements UserDetailsService {
    @Autowired
    private AccountData accountData;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new AccountDetails(accountData.findAccountByEmail(username));
    }
}
