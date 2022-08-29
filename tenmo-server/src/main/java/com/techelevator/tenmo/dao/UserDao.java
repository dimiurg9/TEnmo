package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface UserDao {

    List<User> findAll(int id);

    public BigDecimal findBalanceByUserID(long userId);

    User findByUsername(String username);

    int findIdByUsername(String username);

    User getUserByByUserId(long id);

    boolean create(String username, String password);
}
