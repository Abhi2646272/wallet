package com.payment.wallet.service;

import com.payment.wallet.dto.UserRegisterDTO;
import com.payment.wallet.entity.Transaction;
import com.payment.wallet.entity.User;
import com.payment.wallet.enums.Role;
import com.payment.wallet.repo.TransactionRepository;
import com.payment.wallet.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User registerUser(UserRegisterDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());

        user.setRole(userDTO.getRole());

        user.setWalletId(UUID.randomUUID().toString());
        user.setBalance(0.0);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return userRepository.save(user);
    }

    public double addMoney(String walletId, double amount, String remark) {
        User user = userRepository.findByWalletId(walletId)
            .orElseThrow(() -> new RuntimeException("Wallet not found"));
        user.setBalance(user.getBalance() + amount);

        Transaction transaction = new Transaction();
        transaction.setWalletId(walletId);
        transaction.setTransactionType("Add Money");
        transaction.setAmount(amount);
        transaction.setDescription(remark);

        transactionRepository.save(transaction);
        userRepository.save(user);

        return user.getBalance();
    }

    public List<Transaction> getTransactionHistory(String walletId) {
        return transactionRepository.findByWalletId(walletId);
    }


    public Double checkBalance(String walletId) {
        User user = userRepository.findByWalletId(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
        return user.getBalance();
    }
}
