package com.payment.wallet.dto;

import lombok.Data;

@Data
public class TransactionStatsDTO {
    private long totalTransactions;
    private long totalSuccessTransactions;
    private long totalFailedTransactions;
    private double totalVolume;
    private long totalUsers;
    private long activeBanks;

    public TransactionStatsDTO(long totalTransactions, long totalSuccessTransactions, long totalFailedTransactions, double totalVolume, long totalUsers, long activeBanks) {
        this.totalTransactions = totalTransactions;
        this.totalSuccessTransactions = totalSuccessTransactions;
        this.totalFailedTransactions = totalFailedTransactions;
        this.totalVolume = totalVolume;
        this.totalUsers = totalUsers;
        this.activeBanks = activeBanks;
    }
}
