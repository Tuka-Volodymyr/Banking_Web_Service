package com.example.banking_web_service.repositories;

import com.example.banking_web_service.entities.SentCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SentCodeInfoRepository extends JpaRepository<SentCode,Long> {
    Optional<SentCode> findByAccountId(Long aLong);
}
