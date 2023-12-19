package com.example.banking_web_service.configuration;

import com.example.banking_web_service.repositories.repositoriesImpl.AccountData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class InfoAccountService implements UserDetailsService {
    @Autowired
    private AccountData accountData;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new AccountDetails(accountData.findAccountByEmail(username));
    }
}
