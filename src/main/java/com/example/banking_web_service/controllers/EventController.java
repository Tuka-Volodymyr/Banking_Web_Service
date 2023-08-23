package com.example.banking_web_service.controllers;

import com.example.banking_web_service.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {
    @Autowired
    private EventService eventService;
    @GetMapping("/bank/events")
    public ResponseEntity<?> getGlobalEvents(){
        return new ResponseEntity<>(eventService.getGlobalEvents(), HttpStatus.OK);
    }
    @GetMapping("/bank/history")
    public ResponseEntity<?> getHistoryTransaction(@AuthenticationPrincipal UserDetails userDetails){
        return new ResponseEntity<>(eventService.getOperationEvents(userDetails.getUsername()), HttpStatus.OK);
    }
}
