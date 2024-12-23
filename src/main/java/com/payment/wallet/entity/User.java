package com.payment.wallet.entity;

import com.payment.wallet.enums.Role;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String name;
    @Email
    @UniqueElements
    private String email;
    @Pattern(regexp = "\\d{10}")
    @UniqueElements
    private String phoneNumber;
    private String walletId;
    private double balance = 0.0;
    private Role role; // USER or ADMIN
    private String password; // Encrypted

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public @Email String getEmail() {
        return email;
    }

    public void setEmail(@Email String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public @Pattern(regexp = "\\d{10}") String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@Pattern(regexp = "\\d{10}") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }
}


