package com.example.banking_web_service.data;

import com.example.banking_web_service.entities.OperationEvent;
import com.example.banking_web_service.repositories.OperationEventInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OperationEventData {
    @Autowired
    private OperationEventInfoRepository operationEventInfoRepository;

    public void saveOperationEvent(OperationEvent operationEvent){operationEventInfoRepository.save(operationEvent);}

    public List<OperationEvent> findOperationEventByEmail(String subject,String object){
       return operationEventInfoRepository.findBySubjectOrObject(subject,object);
    }
}
