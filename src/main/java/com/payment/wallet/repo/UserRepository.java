package com.payment.wallet.repo;

import com.payment.wallet.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    long count();
    Optional<User> findByEmail(String email);
    Optional<User> findByWalletId(String walletId);
}

