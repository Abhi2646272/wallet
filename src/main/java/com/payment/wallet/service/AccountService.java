package com.payment.wallet.service;

import com.payment.wallet.entity.Account;
import com.payment.wallet.repo.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    // Method to create and save an account
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    // Method to find an account by account number
    public Account getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    // Method to find an account by walletId
    public Account getAccountByWalletId(String walletId) {
        return accountRepository.findByWalletId(walletId);
    }
}
