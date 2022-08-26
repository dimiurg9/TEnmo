package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class UserService {

    public static final String baseUrl = "http://localhost:8080";
    private RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;
    public User getUserByUserId(AuthenticatedUser currentUser, long id) {

        authToken = currentUser.getToken();
        User user = null;

        try{
            ResponseEntity<User> response = restTemplate.exchange(baseUrl + "/user/" + id, HttpMethod.GET, makeAuthEntity(), User.class);
            user = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return user;
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}
