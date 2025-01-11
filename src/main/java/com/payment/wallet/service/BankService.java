package com.payment.wallet.service;


import com.payment.wallet.entity.Bank;
import com.payment.wallet.repo.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankService {

    @Autowired
    private BankRepository bankRepository;

    public List<Bank> getAllBanks(){
        return bankRepository.findAll();
    }

    // Method to onboard a new bank
    public Bank onboardNewBank(String bankCode, String bankName) {
        // Check if the bank already exists
        Bank existingBank = bankRepository.findByBankCode(bankCode);
        if (existingBank != null) {
            throw new IllegalArgumentException("Bank with this code already exists");
        }

        // Create and save the new bank
        Bank newBank = new Bank(bankCode, bankName);
        return bankRepository.save(newBank);
    }

    // Method to find a bank by its code
    public Bank getBankByCode(String bankCode) {
        return bankRepository.findByBankCode(bankCode);
    }
}
