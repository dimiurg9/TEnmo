package com.techelevator.tenmo.services;

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
    private final String API_BASE_URL = "http://localhost:8080";

    RestTemplate restTemplate = new RestTemplate();

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

    public HttpEntity<Void> makeEntityAuth(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        return entity;
    }
}
