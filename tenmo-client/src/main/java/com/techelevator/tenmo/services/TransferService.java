package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class TransferService {
    private static final String API_BASE_URL = "http://localhost:8080";
    static RestTemplate restTemplate = new RestTemplate();

    public static void createTransfer(Transfer transfer, String token) {
        HttpEntity<Transfer> entity = makeEntity(transfer);
        Transfer returnedTransfer = null;
        returnedTransfer = restTemplate.postForObject(API_BASE_URL + "/transfers/createtransfer", entity, Transfer.class );
//                try{
//                        returnedTransfer = restTemplate.postForObject(API_BASE_URL + "/transfers/createtransfer", entity, Transfer.class );
//                }
//                catch (RestClientException e){
//                        System.out.println("something went wrong (clinent > TransferService > createTransfer");
//                }
        System.out.println(returnedTransfer.getTransferId());


    }
    private static HttpEntity<Transfer> makeEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(transfer, headers);
    }

}

//        private static final String API_BASE_URL = "http://localhost:8080";
//        static RestTemplate restTemplate = new RestTemplate();
//        private static String authToken = null;
//
//        public void setAuthToken(String authToken) {
//                this.authToken = authToken;
//        }
//
//        public static void createTransfer(Transfer transfer, String token) {
//                HttpEntity<Transfer> entity = makeTransferEntity(transfer);
//                Transfer returnedTransfer = null;
//                restTemplate.postForObject(API_BASE_URL + "/transfers/createtransfer", entity, Transfer.class);
////                return returnedTransfer;
//        }
//
//        private static HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
//                HttpHeaders headers = new HttpHeaders();
//                headers.setContentType(MediaType.APPLICATION_JSON);
//                headers.setBearerAuth(authToken);
//                return new HttpEntity<>(transfer, headers);
//        }
//        private HttpEntity<Void> makeAuthEntity() {
//                HttpHeaders headers = new HttpHeaders();
//                headers.setBearerAuth(authToken);
//                return new HttpEntity<>(headers);
//        }



