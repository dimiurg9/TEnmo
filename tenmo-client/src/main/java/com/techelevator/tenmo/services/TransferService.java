package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class TransferService {

    public static final String baseUrl = "http://localhost:8080/transfers/";
    private RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    public Transfer getTransferByUserId(AuthenticatedUser authenticatedUser){
        long userId = authenticatedUser.getUser().getId();
        authToken = authenticatedUser.getToken();
        Transfer transfer = null;

        try{
            ResponseEntity<Transfer> response =
                    restTemplate.exchange(baseUrl + "/user/" + userId, HttpMethod.GET, makeAuthEntity(), Transfer.class );
            transfer = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return transfer;
    }

    public boolean sendBucks(AuthenticatedUser authenticatedUser, Transfer transfer){
        boolean tOrF = false;
        long userId = authenticatedUser.getUser().getId();
        authToken = authenticatedUser.getToken();
        HttpEntity entity = makeTransferEntity(transfer);

        try{
            restTemplate.exchange(baseUrl +"/createtransfer",HttpMethod.POST, entity, Transfer.class);
            tOrF = true;
            return tOrF;
        }catch (RestClientResponseException e ){
            if(e.getMessage().contains("You have insufficient funds for this transaction, or you tried to send money to yourself")){
                System.out.println("You have insufficient funds for this transaction, or you tried to send money to yourself");
            }else{
                System.out.println("The request could not be made, try again");
            }
        }
        return tOrF;
    }



    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfer, headers);
    }



    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

}
