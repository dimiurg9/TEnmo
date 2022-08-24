package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    List<Account> findAll();

    BigDecimal findBalanceByUserID(Long userId) throws UsernameNotFoundException;

    BigDecimal getBalance(int userId);


}
