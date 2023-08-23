package com.example.banking_web_service.configuration;

import com.example.banking_web_service.handler.AuthorizationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {
    @Autowired
    private AuthorizationHandler authorizationHandler;
    @Bean
    public UserDetailsService userDetailsService(){
        return new InfoAccountService();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity.csrf().disable()
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/bank/addAccount","/error/**",
                                "/bank/card/balance/add/money","bank/change/password")
                        .permitAll()
                        .requestMatchers("/bank/lock/account","/bank/unlock/account",
                                "/bank/events", "/bank/card/change/limitOfTurnover")
                        .hasRole("ADMINISTRATOR")
                        .requestMatchers("/bank/**").hasRole("CUSTOMER"))
                .httpBasic()
                .and()
                .exceptionHandling(exceptionHandling->exceptionHandling.accessDeniedHandler(authorizationHandler))
                .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

}
