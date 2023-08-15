package com.example.banking_web_service.repositories;

import com.example.banking_web_service.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountInfoRepository extends JpaRepository<Account,Long> {
    Optional<Account> findByEmail(String email);
}
