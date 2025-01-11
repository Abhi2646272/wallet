package com.payment.wallet.repo;

import com.payment.wallet.entity.Bank;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends MongoRepository<Bank, String> {
    Bank findByBankCode(String bankCode);
}
