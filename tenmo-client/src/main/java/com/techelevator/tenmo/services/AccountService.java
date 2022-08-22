package com.techelevator.tenmo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class AccountService {
    private final String BASE_URL = "http://localhost:8080";

    RestTemplate restTemplate = new RestTemplate();

    public double getBalance(String token, double id){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Double> response = restTemplate.exchange(
                BASE_URL+"/balance/"+id, HttpMethod.GET, entity, Double.class);

        return response.getBody();
    }
}
