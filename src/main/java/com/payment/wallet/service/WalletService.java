package com.payment.wallet.service;

import com.payment.wallet.entity.Transaction;
import com.payment.wallet.entity.User;
import com.payment.wallet.repo.TransactionRepository;
import com.payment.wallet.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class WalletService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService transactionService;


    @Transactional
    public String transferMoney(String senderWalletId, String receiverWalletId, double amount, String remark) {
        // Check if both sender and receiver wallets exist
        User sender = userRepository.findByWalletId(senderWalletId)
                .orElseThrow(() -> new RuntimeException("Sender wallet not found"));
        User receiver = userRepository.findByWalletId(receiverWalletId)
                .orElseThrow(() -> new RuntimeException("Receiver wallet not found"));

        // Check if sender has sufficient balance
        if (sender.getBalance() < amount) {
            return "Insufficient funds in sender's wallet";
        }
        String txnId = transactionService.generateTxnId("WLT");
        // Create sender's transaction
        Transaction senderTransaction = new Transaction();
        senderTransaction.setTransactionId(txnId);
        senderTransaction.setSenderWalletId(sender.getName());
        senderTransaction.setReceiverWalletId(receiver.getName());
        senderTransaction.setWalletId(senderWalletId);
        senderTransaction.setTransactionType("Transfer (Dr.)");
        senderTransaction.setAmount(amount); // Deduct from sender
        senderTransaction.setDescription(remark);
        senderTransaction.setStatus("Pending");
        senderTransaction.setTimestamp(LocalDateTime.now());
        senderTransaction.setDescription("Transfer to " + receiver.getName());

        // Create receiver's transaction
        Transaction receiverTransaction = new Transaction();
        receiverTransaction.setTransactionId(txnId);
        receiverTransaction.setWalletId(receiverWalletId);
        receiverTransaction.setReceiverWalletId(receiver.getName());
        receiverTransaction.setSenderWalletId(sender.getName());
        receiverTransaction.setTransactionType("Transfer (Cr.)");
        receiverTransaction.setAmount(amount); // Add to receiver
        receiverTransaction.setStatus("Pending");
        receiverTransaction.setTimestamp(LocalDateTime.now());
        receiverTransaction.setDescription("Transfer from " + sender.getName());

        try {
            // Update the balances
            sender.setBalance(sender.getBalance() - amount);
            receiver.setBalance(receiver.getBalance() + amount);

            // Save the transaction records to the database
            transactionRepository.save(senderTransaction);
            transactionRepository.save(receiverTransaction);

            // Save the updated users
            userRepository.save(sender);
            userRepository.save(receiver);

            // Update the transaction status to successful
            senderTransaction.setStatus("SUCCESS");
            receiverTransaction.setStatus("SUCCESS");


            // Save the updated transactions
            transactionRepository.save(senderTransaction);
            transactionRepository.save(receiverTransaction);

            return "Transfer successful";
        } catch (Exception e) {
            // In case of failure, handle rollback and mark transactions as failed
            senderTransaction.setStatus("Failed");
            receiverTransaction.setStatus("Failed");
            senderTransaction.setFailureReason(e.getMessage());
            receiverTransaction.setFailureReason(e.getMessage());

            // Save failed transactions
            transactionRepository.save(senderTransaction);
            transactionRepository.save(receiverTransaction);

            throw new RuntimeException("Transaction failed: " + e.getMessage());
        }
    }
}
