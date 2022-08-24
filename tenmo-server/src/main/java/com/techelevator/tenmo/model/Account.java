package com.techelevator.tenmo.model;

import com.techelevator.tenmo.exceptions.InsufficientFunds;

import java.math.BigDecimal;

public class Account {

    private int accountId;
    private long userId;
    private BigDecimal balance; // changing to big decimal to handle money exchange better

    public Account() {
    }

    public Account(int accountId, long userId, BigDecimal balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void sendBucks(BigDecimal amount) throws InsufficientFunds {
        BigDecimal updatedBalance = new BigDecimal(String.valueOf(balance)).subtract(amount);
        if(updatedBalance.compareTo(BigDecimal.ZERO) >= 0){
            this.balance = updatedBalance;
        }else{
            throw new InsufficientFunds();
        }
    }
    public void receiveBucks(BigDecimal amount){
        this.balance = new BigDecimal(String.valueOf(balance)).add(amount);
    }
}

