package com.example.banking_web_service.repositories.repositoriesImpl;

import com.example.banking_web_service.model.entities.SentCode;
import com.example.banking_web_service.repositories.SentCodeInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SentCodeData {
    @Autowired
    private SentCodeInfoRepository sentCodeInfoRepository;
    public Optional<SentCode> findCodeByAccountId(long accId){
        return sentCodeInfoRepository.findByAccountId(accId);
    }
    public void saveCode(SentCode sentCode){
        sentCodeInfoRepository.save(sentCode);
    }
    public void deleteCode(){
        sentCodeInfoRepository.deleteAll();
    }

}
