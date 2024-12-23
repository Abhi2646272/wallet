package com.payment.wallet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TransferRequestDTO {
    @NotBlank(message = "Sender wallet ID is required")
    private String senderWalletId;

    @NotBlank(message = "Receiver wallet ID is required")
    private String receiverWalletId;

    @Positive(message = "Amount must be positive")
    private double amount;

    // Getters and Setters

    @Positive(message = "Amount must be positive")
    public double getAmount() {
        return amount;
    }

    public void setAmount(@Positive(message = "Amount must be positive") double amount) {
        this.amount = amount;
    }

    public @NotBlank(message = "Receiver wallet ID is required") String getReceiverWalletId() {
        return receiverWalletId;
    }

    public void setReceiverWalletId(@NotBlank(message = "Receiver wallet ID is required") String receiverWalletId) {
        this.receiverWalletId = receiverWalletId;
    }

    public @NotBlank(message = "Sender wallet ID is required") String getSenderWalletId() {
        return senderWalletId;
    }

    public void setSenderWalletId(@NotBlank(message = "Sender wallet ID is required") String senderWalletId) {
        this.senderWalletId = senderWalletId;
    }
}