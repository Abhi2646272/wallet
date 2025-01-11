package com.payment.wallet.transaction;

import com.payment.wallet.entity.Bank;
import com.payment.wallet.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Service
public class AccountHelper {

    @Autowired
    private BankService bankService;

    // Method to generate wallet account number based on bankCode and userId
    public String generateWalletAccountNumberWithBank(String bankCode, String userId) {
        Bank bank = bankService.getBankByCode(bankCode);
        if (bank == null) {
            throw new IllegalArgumentException("Bank not found for the given bank code");
        }

        // Format: WALLET-{bankCode}-{userId}-{timestamp}-{uniqueId}
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String uniquePart = UUID.randomUUID().toString().substring(0, 6); // Extract part of UUID for uniqueness

        return "WALLET-" + bank.getBankCode() + "-" + userId + "-" + timestamp + "-" + uniquePart;
    }

    // Method to generate random wallet account number for a specific bank
    public String generateRandomWalletAccountNumberWithBank(String bankCode) {
        Bank bank = bankService.getBankByCode(bankCode);
        if (bank == null) {
            throw new IllegalArgumentException("Bank not found for the given bank code");
        }

        Random random = new Random();
        int randomNumber = random.nextInt(900000) + 100000; // Generating a random 6 digit number
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        return "WALLET-" + bank.getBankCode() + "-" + timestamp + "-" + randomNumber;
    }
}
