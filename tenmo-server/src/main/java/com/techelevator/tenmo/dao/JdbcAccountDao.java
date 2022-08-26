package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT account_id, user_id, balance FROM account";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()){
            Account account = mapRowToAccount(results);
            accounts.add(account);
        }
        return accounts;
    }

    @Override
    public BigDecimal findBalanceByUserID(long userId) throws UsernameNotFoundException {

        String sql = "SELECT balance FROM account WHERE user_id = ?;";

        BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);

        if (balance != null){
            return balance;
        }else{
            throw new UsernameNotFoundException("User " + userId + " was not found.");
        }

    }

    @Override
    public Account getAccountById(int accountId) {
        Account account  = new Account();
        String sql = "SELECT account_id, user_id, balance FROM account WHERE account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        while (results.next()){
            account = mapRowToAccount(results);
        }
        return account;
    }

    @Override
    public Account getAccountByUserId(long userId) {
        Account account  = new Account();
        String sql = "SELECT account_id, user_id, balance FROM account WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,userId);
        while (results.next()){
            account = mapRowToAccount(results);
        }
        return account;
    }

    @Override
    public BigDecimal getBalance(long userId) {
        Account account = new Account();
        String sql = "SELECT a.account_id, a.user_id, a.balance \n" +
                "FROM account a\n" +
                "JOIN tenmo_user t\n" +
                "ON a.user_id = t.user_id \n" +
                "WHERE t.user_id = ? ;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while (results.next()){
            account = mapRowToAccount(results);
        }
        return account.getBalance();
    }



    @Override
    public void update(Account account){
        String sql = "UPDATE account SET balance = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, account.getBalance(), account.getUserId());
    }

    @Override
    public long getUserIdByAccountId(int accountId) {
        Account account = new Account();
        long userId =  0;
        String sql = "SELECT user_id FROM account WHERE account_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,accountId);
        while(results.next()){
            account = mapRowToAccount(results);
            userId = account.getUserId();
        }
        return userId;
    }

    private Account mapRowToAccount(SqlRowSet rowSet){ //  makes the account object from the database
        Account account = new Account();
        account.setAccountId(rowSet.getInt("account_id"));
        account.setUserId(rowSet.getLong("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        return account;
    }
}

