package com.payment.wallet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddMoneyRequestDTO {
    @NotBlank(message = "Wallet ID is required")
    private String walletId;

    @Positive(message = "Amount must be positive")
    private double amount;

    private String description;

    // Getters and Setters

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Positive(message = "Amount must be positive")
    public double getAmount() {
        return amount;
    }

    public void setAmount(@Positive(message = "Amount must be positive") double amount) {
        this.amount = amount;
    }

    public @NotBlank(message = "Wallet ID is required") String getWalletId() {
        return walletId;
    }

    public void setWalletId(@NotBlank(message = "Wallet ID is required") String walletId) {
        this.walletId = walletId;
    }
}