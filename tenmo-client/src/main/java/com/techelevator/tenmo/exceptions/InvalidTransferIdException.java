package com.techelevator.tenmo.exceptions;

public class InvalidTransferIdException extends Exception {

    public InvalidTransferIdException(){
        super("This Selection Is Not A Valid Transfer ID");
    }

}
