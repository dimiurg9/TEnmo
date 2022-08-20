package com.techelevator.tenmo.controller;

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
//@PreAuthorize("isAuthenticated()")
@RequestMapping("/accounts") // after localhost:8080


public class AccountController {

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "", method = RequestMethod.GET) //  calls the service to get the list of all accounts
    public List<Account> getAccounts(){
        return accountService.getAccounts();
    }

    @RequestMapping(value = "account/{id}", method = RequestMethod.GET) // calls the service with the users userId to get the balance of that account
    public BigDecimal getBalance(@PathVariable int id){
        return accountService.getBalance(id);
    }




    private HttpEntity<Void> makeAuthEntity(){ //  not used yet, maybe useful for authentication
        HttpHeaders headers = new HttpHeaders();
        //headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(headers);
    }

}
