package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.*;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

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
            System.out.println("Transaction was successful.");
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

    public Transfer getTransfersByTransferId(AuthenticatedUser authenticatedUser, int choice) { // added 8/26
        authToken = authenticatedUser.getToken();
        Transfer transfer = new Transfer();
        try{
            ResponseEntity<Transfer> response =
                    restTemplate.exchange(baseUrl +"/transfer/" + choice,HttpMethod.GET, makeAuthEntity(), Transfer.class);
            transfer = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfer;
    }

    public Transfer [] getTransfersByUserId(AuthenticatedUser authenticatedUser){ //  created 8/25
        Transfer[] transfers = null;
        long userId = authenticatedUser.getUser().getId();
        authToken = authenticatedUser.getToken();
        try {
            ResponseEntity<Transfer[]> response =
                    restTemplate.exchange(baseUrl + "user/"+userId, HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            transfers = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    public Transfer[] getPendingTransfersByUserId(AuthenticatedUser authenticatedUser){
        Transfer[] pendingTransfers = null;
        long userId =authenticatedUser.getUser().getId();
        authToken = authenticatedUser.getToken();
        try {
            ResponseEntity<Transfer[]> response =
                    restTemplate.exchange(baseUrl + "/user/pending/"+userId, HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            pendingTransfers = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return pendingTransfers;
    }

    public TransferType getTransferTypeById(AuthenticatedUser authenticatedUser, int id){
        TransferType transferType = null;
        authToken = authenticatedUser.getToken();
        try{
            ResponseEntity<TransferType> response =
                    restTemplate.exchange(baseUrl +"/transfertypes/id/" + id,HttpMethod.GET, makeAuthEntity(), TransferType.class);
            transferType = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transferType;
    }

    public TransferStatus getTransferStatusById(AuthenticatedUser authenticatedUser, int id){
        TransferStatus transferStatus = null;
        authToken = authenticatedUser.getToken();
        try{
            ResponseEntity<TransferStatus> response =
                    restTemplate.exchange(baseUrl +"/transferstatus/id/" + id,HttpMethod.GET, makeAuthEntity(), TransferStatus.class);
            transferStatus = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transferStatus;
    }

    public boolean transferIdIsValid(Transfer [] transfers, int selection) { //  added 8/26

        boolean valid = false;
        for (Transfer transfer : transfers) {
            if (transfer.getTransferId() == selection) {
                valid = true;
            }
        }
        return valid;
    }

    public void updateTransfer(AuthenticatedUser authenticatedUser, Transfer transfer) {
        authToken = authenticatedUser.getToken();
        try {
            restTemplate.exchange(baseUrl + "/update", HttpMethod.PUT, makeTransferEntity(transfer), Void.class);
        } catch (RestClientResponseException e) {
            if (e.getMessage().contains("You have insufficient funds for this transaction, or you tried to send money to yourself")) {
                System.out.println("You have insufficient funds for this transaction");
            } else {
                System.out.println("The request could not be made, try again");
            }
        }
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
