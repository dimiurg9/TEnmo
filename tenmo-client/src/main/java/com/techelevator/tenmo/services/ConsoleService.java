package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;

import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);
    private final TransferService transferService = new TransferService();
    private final AccountService accountService = new AccountService();
    private final UserService userService = new UserService();

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to  *");
        System.out.println("" +
                "  ____________                    \n" +
                " /_  __/ ____/___  ____ ___  ____ \n" +
                "  / / / __/ / __ \\/ __ `__ \\/ __ \\\n" +
                " / / / /___/ / / / / / / / / /_/ /\n" +
                "/_/ /_____/_/ /_/_/ /_/ /_/\\____/ \n" +
                "                                  ");
        System.out.println("*********************");

    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    public void printTransferHistory(AuthenticatedUser currentUser, Transfer transfer){

        int accountFrom = transfer.getAccountFrom();
        int accountFromUserId = accountService.getAccountByAccountId(currentUser, accountFrom).getUserId();

        int accountTo = transfer.getAccountTo();
        int accountToUserId = accountService.getAccountByAccountId(currentUser, accountTo).getUserId();


        User userFrom = userService.getUserByUserId(currentUser, accountFromUserId);
        String userFromName = userFrom.getUsername();
        long userFromId = userFrom.getId();

        User userTo = userService.getUserByUserId(currentUser, accountToUserId);
        String userToName = userTo.getUsername();
        long userToId = userTo.getId();


        System.out.println(String.format("%s          From: %s           \n" +
                "              To:   %s           $  %s",transfer.getTransferId(),userFromName,userToName, transfer.getAmount()));

    }

    public void printTransferDetails(AuthenticatedUser currentUser, Transfer transfer) {

        int id = transfer.getTransferId();
        BigDecimal amount = transfer.getAmount();
        int from = transfer.getAccountFrom();
        int to = transfer.getAccountTo();
        int transferTypeId = transfer.getTransferTypeId();
        int transferStatusId = transfer.getTransferStatusId();



        long fromUserId = accountService.getAccountByAccountId(currentUser, from).getUserId();
        String fromUserName = userService.getUserByUserId(currentUser, fromUserId).getUsername();

        long toUserId = accountService.getAccountByAccountId(currentUser, to).getUserId();
        String toUserName = userService.getUserByUserId(currentUser, toUserId).getUsername();

        String transferType = transferService.getTransferTypeById(currentUser,transferTypeId).getTransferTypeDesc();
        String transferStatus = transferService.getTransferStatusById(currentUser,transferStatusId).getTransferStatusDesc();


        System.out.println("-------------------------------");
        System.out.println("Transfer Details");
        System.out.println("-------------------------------");
        System.out.println("Id: " + id);
        System.out.println("From: " + fromUserName);
        System.out.println("To: " + toUserName);
        System.out.println("Type: " + transferType);
        System.out.println("Status: " + transferStatus);
        System.out.println("Amount: $" + amount);
    }

    public void approveOrDenyTransfer(AuthenticatedUser currentUser, Transfer transfer ){
        System.out.println("1: Approve");
        System.out.println("2: Reject");
        System.out.println("0: Don't approve or reject");
        System.out.println("---------");
        int choice = promptForInt("Please choose an option: ");

        if(choice == 1){
            transfer.setTransferStatusId(2);
            transferService.updateTransfer(currentUser, transfer);
        }else if(choice == 2){
            transfer.setTransferStatusId(3);
            transferService.updateTransfer(currentUser, transfer);
        }else{
            System.out.println("Transfer has not been updated");
        }

    }

    public void printNotEnoughMoney(){
        System.out.println("########################################");
        System.out.println("   Not enough money");
        System.out.println("#########################################");
    }

    public void printCannotSendToYourself(){
        System.out.println("########################################");
        System.out.println("   You can not send money to yourself");
        System.out.println("#########################################");
    }
    public void printAmountCannotBeZero(){
        System.out.println("########################################");
        System.out.println("   Amount should be number more than zero");
        System.out.println("#########################################");
    }


}
