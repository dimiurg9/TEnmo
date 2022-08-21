package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepositoryExtensionsKt;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {

    private UserDao userDao;

    public AccountController(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "", method = RequestMethod.GET) //  calls the service to get the list of all accounts
    public List<Account> getAccounts(){
        return accountService.getAccounts();
    }

    @RequestMapping(path = "/balance/{id}", method = RequestMethod.GET)
    public double balance(@PathVariable Long id){

        return userDao.findBallanceByUserID(id);
    }


    private HttpEntity<Void> makeAuthEntity(){ //  not used yet, maybe useful for authentication
        HttpHeaders headers = new HttpHeaders();
        //headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(headers);
    }

}
