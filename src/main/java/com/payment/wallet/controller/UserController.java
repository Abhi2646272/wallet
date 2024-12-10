package com.payment.wallet.controller;

import com.payment.wallet.entity.Transaction;
import com.payment.wallet.entity.User;
import com.payment.wallet.service.TransactionService;
import com.payment.wallet.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    // DTO need to create for request and response


    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @PostMapping("/wallet/add-money")
    public ResponseEntity<Double> addMoney(@RequestBody Map<String, Object> payload) {
        String walletId = payload.get("wallet_id").toString();
        double amount = Double.parseDouble(payload.get("amount").toString());
        return ResponseEntity.ok(userService.addMoney(walletId, amount));
    }

    @GetMapping("/{wallet_id}/balance")
    public ResponseEntity<Double> getWalletBalance(@PathVariable String wallet_id) {
        return ResponseEntity.ok(userService.checkBalance(wallet_id));
    }

    @GetMapping("/wallet/{wallet_id}/transactions")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable String wallet_id) {
        return ResponseEntity.ok(userService.getTransactionHistory(wallet_id));
    }

    @PostMapping("/wallet/transfer")
    public ResponseEntity<String> transferMoney(@RequestBody Map<String, Object> payload) {
        String senderWalletId = payload.get("sender_wallet_id").toString();
        String receiverWalletId = payload.get("receiver_wallet_id").toString();
        double amount = Double.parseDouble(payload.get("amount").toString());
        transactionService.transferMoney(senderWalletId, receiverWalletId, amount);
        return ResponseEntity.ok("Transfer successful");
    }
}

