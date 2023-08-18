package com.example.banking_web_service.repositories;

import com.example.banking_web_service.entities.OperationEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationEventInfoRepository extends JpaRepository<OperationEvent,Long> {
    List<OperationEvent> findBySubjectOrObject(String subject,String object);

}
