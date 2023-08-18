package com.example.banking_web_service.data;

import com.example.banking_web_service.entities.GlobalEvent;
import com.example.banking_web_service.repositories.GlobalEventInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GlobalEventData {
    @Autowired
    private GlobalEventInfoRepository globalEventInfoRepository;

    public void saveGlobalEvent(GlobalEvent globalEvent){
        globalEventInfoRepository.save(globalEvent);}
    public List<GlobalEvent> findAllEvent(){
        return globalEventInfoRepository.findAllByOrderById();
    }
}
