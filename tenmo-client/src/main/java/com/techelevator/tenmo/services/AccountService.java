package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import org.springframework.http.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public class AccountService {

    RestTemplate restTemplate = new RestTemplate();

    public void getBalance(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Account> response = restTemplate.exchange(
                "http://localhost:8080/account/balance", HttpMethod.GET, entity, Account.class);
    }
}
