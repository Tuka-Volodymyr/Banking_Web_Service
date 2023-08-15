package com.example.banking_web_service.repositories;

import com.example.banking_web_service.entities.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardInfoRepository extends JpaRepository<CreditCard,Long> {

}
