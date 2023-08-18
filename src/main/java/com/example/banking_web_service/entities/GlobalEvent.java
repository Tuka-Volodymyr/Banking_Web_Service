package com.example.banking_web_service.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String date;
    private String action;
    private String subject;
    private String object;
    private String path;
    public GlobalEvent(String date, String action, String subject, String object, String path){
        this.date=date;
        this.action=action;
        this.subject=subject;
        this.object=object;
        this.path=path;
    }
}
