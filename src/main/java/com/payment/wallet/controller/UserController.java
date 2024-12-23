package com.payment.wallet.controller;

import com.payment.wallet.dto.*;
import com.payment.wallet.entity.Transaction;
import com.payment.wallet.entity.User;
import com.payment.wallet.service.TransactionService;
import com.payment.wallet.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody @Valid UserRegisterDTO userDTO) {

        return ResponseEntity.ok(userService.registerUser(userDTO));
    }

    @PostMapping("/wallet/add-money")
    public ResponseEntity<Double> addMoney(@RequestBody @Valid AddMoneyRequestDTO payload) {
        return ResponseEntity.ok(userService.addMoney(payload.getWalletId(), payload.getAmount()));
    }

    @GetMapping("/{walletId}/balance")
    public ResponseEntity<Double> getWalletBalance(@PathVariable String walletId) {
        return ResponseEntity.ok(userService.checkBalance(walletId));
    }

    @GetMapping("/wallet/{walletId}/transactions")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable String walletId) {
        return ResponseEntity.ok(userService.getTransactionHistory(walletId));
    }

    @PostMapping("/wallet/transfer")
    public ResponseEntity<String> transferMoney(@RequestBody @Valid TransferRequestDTO transferRequest) {
        transactionService.transferMoney(
                transferRequest.getSenderWalletId(),
                transferRequest.getReceiverWalletId(),
                transferRequest.getAmount()
        );
        return ResponseEntity.ok("Transfer successful");
    }
}


