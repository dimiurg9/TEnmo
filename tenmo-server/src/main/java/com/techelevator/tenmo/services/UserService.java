package com.techelevator.tenmo.services;

import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {
    private RestTemplate restTemplate = new RestTemplate();
    private JdbcTemplate jdbcTemplate = new JdbcTemplate();

    private JdbcUserDao userDao;

    public UserService(JdbcUserDao userDao) {
        this.userDao = userDao;
    }

    public User getUserByUserId(long id){
        return userDao.getUserByByUserId(id);
    }
}