package com.payment.wallet.service;

import com.payment.wallet.dto.TransactionStatsDTO;
import com.payment.wallet.entity.Transaction;
import com.payment.wallet.repo.TransactionRepository;
import com.payment.wallet.repo.UserRepository;
import com.payment.wallet.security.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;

    public TransactionStatsDTO getTransactionStats() {
        long totalTransactions = transactionRepository.count();
        long totalSuccessTransactions = transactionRepository.findByStatus("Completed").size();
        long totalFailedTransactions = totalTransactions - totalSuccessTransactions;

        // Calculate total transaction volume
        List<Transaction> transactions = transactionRepository.findAllTransactionAmounts();
        double totalVolume = transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();

        long totalUsers = userRepository.count();
        long activeBanks = 3; // Hardcoded for now, modify as needed

        return new TransactionStatsDTO(totalTransactions, totalSuccessTransactions, totalFailedTransactions, totalVolume, totalUsers, activeBanks);
    }

    // Method to generate a unique transaction ID (Txn ID)
    public String generateTxnId(String bankCode) {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()); // Millisecond precision
        String randomPart = UUID.randomUUID().toString().substring(0, 6); // Generate a random part (6 characters)
        
        // Format: TXN-{bankCode}-{timestamp}-{randomPart}
        return "WLT"+randomPart;
    }

    // Method to generate a Unique Transaction Reference (UTR) number
    public String generateUtrNumber(String bankCode) {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()); // Second precision
        String randomPart = String.format("%04d", new Random().nextInt(10000)); // Generate a random 4-digit number
        
        // Format: UTR-{bankCode}-{timestamp}-{randomPart}
        return "UTR-" + bankCode + "-" + timestamp + "-" + randomPart;
    }

    public String saveEncryptedSenderName(String senderName) throws Exception {
        return EncryptionUtil.encrypt(senderName);
    }

    public String saveEncryptedReceiverName(String receiverName) throws Exception {
        return EncryptionUtil.encrypt(receiverName);
    }

    // Decrypt sender and receiver names before sending to the client
    public String getDecryptedSenderName(String encryptedSenderName) throws Exception {
        return EncryptionUtil.decrypt(encryptedSenderName);
    }

    public String getDecryptedReceiverName(String encryptedReceiverName) throws Exception {
        return EncryptionUtil.decrypt(encryptedReceiverName);
    }
}
