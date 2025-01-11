package com.payment.wallet.controller;

import com.payment.wallet.dto.*;
import com.payment.wallet.entity.Transaction;
import com.payment.wallet.service.AuthService;
import com.payment.wallet.service.WalletService;
import com.payment.wallet.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {
    @Autowired
    private UserService userService;

    @Autowired
    private WalletService walletService;

    private static final Logger logger = Logger.getLogger(AuthService.class.getName());


    @PostMapping("/add-money")
    public ResponseEntity<Double> addMoney(@RequestBody @Valid  AddMoneyRequestDTO payload) {
        return ResponseEntity.ok(userService.addMoney(payload.getWalletId(), payload.getAmount(), payload.getDescription()));
    }

    @GetMapping("/{walletId}/balance")
    public ResponseEntity<Double> getWalletBalance(@PathVariable String walletId) {
//        return ResponseEntity.ok(userService.checkBalance(walletId));
        logger.info("Received request to get balance for wallet ID: {}"+ walletId);
        try {
            Double balance = userService.checkBalance(walletId);
            logger.info("Successfully retrieved balance for wallet ID: {}. Balance: {}" + walletId + " "+ balance);
            return ResponseEntity.ok(balance);
        } catch (RuntimeException e) {
            e.printStackTrace();
//            logger.error("Error retrieving balance for wallet ID: {}", walletId, e);
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/{walletId}/transactions")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable String walletId) {
        return ResponseEntity.ok(userService.getTransactionHistory(walletId));
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(@RequestBody @Valid TransferRequestDTO transferRequest) {
        walletService.transferMoney(
                transferRequest.getSenderWalletId(),
                transferRequest.getReceiverWalletId(),
                transferRequest.getAmount(),
                transferRequest.getDescription()
        );
        return ResponseEntity.ok("Transfer successful");
    }
}


