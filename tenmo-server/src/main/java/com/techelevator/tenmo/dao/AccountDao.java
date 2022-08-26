package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    List<Account> findAll();

    Account getAccountById(int accountId); // gets an account with users id

    Account getAccountByUserId(long userId);

    BigDecimal findBalanceByUserID(long userId) throws UsernameNotFoundException;

    BigDecimal getBalance(long userId);

    void update(Account account); //  used to update the account in the database to the new amount after transfer.

    long getUserIdByAccountId(int accountId);

}
