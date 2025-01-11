package com.payment.wallet.repo;

import com.payment.wallet.entity.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {
    // Add custom query methods if needed
    Account findByAccountNumber(String accountNumber);
    Account findByWalletId(String walletId);
}
