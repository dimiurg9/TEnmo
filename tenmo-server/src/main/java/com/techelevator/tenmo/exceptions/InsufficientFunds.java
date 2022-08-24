package com.techelevator.tenmo.exceptions;

public class InsufficientFunds extends Exception{
    public InsufficientFunds(){
        super("You have insufficient funds for this transaction, or you tried to send money to yourself");
    }
}
