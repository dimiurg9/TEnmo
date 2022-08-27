package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
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


    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/accounts", method = RequestMethod.GET) //  calls the service to get the list of all accounts
    public List<Account> getAccounts(){
        return accountService.getAccounts();
    }

    @RequestMapping(value = "/account/accountid/{id}", method = RequestMethod.GET)
    public Account getAccountByAccountId(@PathVariable int id){
        return accountService.getAccountByAccountId(id);
    }

    @RequestMapping(path = "/balance/{id}", method = RequestMethod.GET)
    public BigDecimal balance(@PathVariable long id){ return accountService.findBalanceByUserID(id);} // changed 8/25 to use service instead of dao

    @RequestMapping(path = "/account/user/{id}", method = RequestMethod.GET) // added 8/25
    public Account getAccountByUserId(@PathVariable long id){
        return accountService.getAccountById(id);
    }

    @RequestMapping(path = "/account/userid/{accountId}", method = RequestMethod.GET)
    public long getUserIdByAccountId(@PathVariable int accountId){
        return accountService.getUserIdByAccountId(accountId);
    }

}
