package com.payment.wallet.controller;

import com.payment.wallet.entity.Transaction;
import com.payment.wallet.entity.User;
import com.payment.wallet.repo.TransactionRepository;
import com.payment.wallet.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/wallets")
    public ResponseEntity<List<User>> getAllWallets() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        // get all transaction
        List<Transaction> transactions = transactionRepository.findAll();

        // Return the list of transactions
        return ResponseEntity.ok(transactions);
    }

    // To do

    //  filtering api based on txn category
    //  transaction search by walletId/txnId
    //  Pagination
    //  user details view api for admin
    //  issue debit/credit card api
    //  chart analysis data processor
}
