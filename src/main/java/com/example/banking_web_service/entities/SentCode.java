package com.example.banking_web_service.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Random;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SentCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private Long accountId;
    public SentCode(long accountId){
        this.accountId=accountId;
        code=generateCod();
    }

    public String generateCod() {
        StringBuilder stringBuilderCod=new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 4; i++) {
            stringBuilderCod.append(rand.nextInt(10));
        }
        return stringBuilderCod.toString();
    }

}
