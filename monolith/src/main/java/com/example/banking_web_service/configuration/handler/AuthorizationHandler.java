package com.example.banking_web_service.configuration.handler;


import com.example.banking_web_service.services.EventService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class AuthorizationHandler implements AccessDeniedHandler {

    @Autowired
    private EventService eventService;
    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {
        eventService.authorizationFailed();
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied!");

    }
}