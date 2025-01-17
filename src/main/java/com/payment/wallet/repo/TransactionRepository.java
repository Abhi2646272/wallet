package com.payment.wallet.repo;

import com.payment.wallet.entity.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByWalletId(String walletId);
    List<Transaction> findByStatus(String status);

    @Query(value = "{}", fields = "{amount : 1}")
    List<Transaction> findAllTransactionAmounts();
}
