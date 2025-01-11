package com.payment.wallet.controller;

import com.payment.wallet.dto.TransactionStatsDTO;
import com.payment.wallet.entity.Bank;
import com.payment.wallet.entity.Transaction;
import com.payment.wallet.entity.User;
import com.payment.wallet.repo.TransactionRepository;
import com.payment.wallet.repo.UserRepository;
import com.payment.wallet.service.BankService;
import com.payment.wallet.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private BankService bankService;

    @Autowired
    private TransactionService transactionService;

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

    @PostMapping("/onboard-bank")
    public Bank onboardNewBank(@RequestParam String bankCode, @RequestParam String bankName) {
        return bankService.onboardNewBank(bankCode, bankName);
    }

    @GetMapping("/transactionstats")
    public TransactionStatsDTO getTransactionStats() {
        return transactionService.getTransactionStats();
    }

    // To do

    //  filtering api based on txn category
    //  transaction search by walletId/txnId
    //  Pagination
    //  user details view api for admin
    //  issue debit/credit card api
    //  chart analysis data processor
}
