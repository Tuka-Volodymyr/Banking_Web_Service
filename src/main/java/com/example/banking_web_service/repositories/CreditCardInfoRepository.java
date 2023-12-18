package com.example.banking_web_service.repositories;

import com.example.banking_web_service.model.entities.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreditCardInfoRepository extends JpaRepository<CreditCard,Long> {
    Optional<CreditCard> findByEmailOfOwner(String email);
    Optional<CreditCard> findByCard(String card);

}
