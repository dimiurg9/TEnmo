package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

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

    public List<User> listUsers(AuthenticatedUser currentUser){
        authToken = currentUser.getToken();

        List<User> userList = null;

        try{
            ResponseEntity<List<User>> response = restTemplate.exchange(baseUrl + "/users/"+currentUser.getUser().getId(),
                    HttpMethod.GET,
                    makeAuthEntity(),
                    new ParameterizedTypeReference<List<User>>() {});
            userList = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }

        int i = 1;
        for (User user: userList){
            System.out.println(i+". "+user.getUsername());
            i++;
        }


        return userList;
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}
