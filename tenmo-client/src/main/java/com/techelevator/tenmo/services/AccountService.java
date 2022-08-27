package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {
    private static final String API_BASE_URL = "http://localhost:8080";

    static RestTemplate restTemplate = new RestTemplate();

    public BigDecimal getBalance(String token, Long id){
        BigDecimal currentBalance = null;

        try{
            ResponseEntity<BigDecimal> response = restTemplate.exchange(
                    API_BASE_URL+"/balance/"+id,
                    HttpMethod.GET,
                    makeEntityAuth(token),
                    BigDecimal.class);

            currentBalance = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return currentBalance;
    }

    public Account getAccountByID(AuthenticatedUser authenticatedUser, long id){ // created 8/25
        String token = authenticatedUser.getToken();
        Account account = null;
        try{
            ResponseEntity<Account> response = restTemplate.exchange(
                    API_BASE_URL +"/account/user/"+id,
                    HttpMethod.GET,
                    makeEntityAuth(token),
                    Account.class);
            account = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }

        return account;

    }

    public Account getAccountByAccountId(AuthenticatedUser authenticatedUser, int accountId){
        String token = authenticatedUser.getToken();
        Account account = null;
        try{
            ResponseEntity<Account> response = restTemplate.exchange(
                    API_BASE_URL +"/account/accountid/"+accountId,
                    HttpMethod.GET,
                    makeEntityAuth(token),
                    Account.class);
            account = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }

        return account;

    }

    public long getUserIdByAccountId(AuthenticatedUser authenticatedUser, int accountId) {
        String token = authenticatedUser.getToken();
        long userId = 0;
        try {
            ResponseEntity<Long> response = restTemplate.exchange(API_BASE_URL + "/account/userid/" + accountId, HttpMethod.GET, makeEntityAuth(token), Long.class);
            userId = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return userId;
    }

    public static int getAccountIDByUserId(String token, long id){
        Account account = null;
        int accountId = 0;
        try{
            ResponseEntity<Account> response = restTemplate.exchange(
                    API_BASE_URL +"/account/user/" +id,
                    HttpMethod.GET,
                    makeEntityAuth(token),
                    Account.class);
            return response.getBody().getAccountId();
        }catch (ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return accountId;
    }

    public static HttpEntity<Void> makeEntityAuth(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        return entity;
    }
}
