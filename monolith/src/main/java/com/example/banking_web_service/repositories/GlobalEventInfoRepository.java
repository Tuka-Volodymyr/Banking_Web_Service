package com.example.banking_web_service.repositories;

import com.example.banking_web_service.model.entities.GlobalEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface GlobalEventInfoRepository extends JpaRepository<GlobalEvent,Long> {
    List<GlobalEvent> findAllByOrderById();
}
