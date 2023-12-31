package com.example.banking_web_service.repositories.repositoriesImpl;

import com.example.banking_web_service.model.entities.OperationEvent;
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
    public List<OperationEvent> findOperationEventByEmailObjectAndData(String object,String data){
        return operationEventInfoRepository.findByObjectAndDate(object,data);
    }

}
