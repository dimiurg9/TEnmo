package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.model.TransferType;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

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

//    public boolean transferIdIsValid(Transfer [] transfers, int selection)  { //  added 8/26
//
//        boolean valid = false;
//        try{
//            for (Transfer transfer: transfers) {
//                if (transfer.getTransferId() == selection){
//                    valid = true;
//                }else{
//                    valid = false;
//                }
//            }
//
//        }catch (){
//
//        }
//    }



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
