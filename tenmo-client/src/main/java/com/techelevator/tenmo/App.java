package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.math.BigDecimal;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService();
    private final TransferService transferService = new TransferService();
    private final UserService userService = new UserService();

    //TODO: to test
    public static AuthenticatedUser getCurrentUser() {
        return currentUser;
    }

    private static AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucksByRequest(2, 2);
            } else if (menuSelection == 5) {
                sendBucksByRequest(1, 1);
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() {
    //TODO call account service
        BigDecimal currentBalance = null;

        currentBalance = accountService.getBalance(currentUser.getToken(),currentUser.getUser().getId());

        if (currentBalance != null){
            System.out.println(currentBalance);
        }else {
            consoleService.printErrorMessage();
        }

    }

    private void viewTransferHistory() {
        Transfer[] transferHistory = transferService.getTransfersByUserId(currentUser); // creates the array of transfers
        System.out.println("-------------------------------");
        System.out.println("Transfers");
        System.out.println("ID     From/To          Amount");
        System.out.println("-------------------------------");

        for (Transfer transfer : transferHistory) { //  loops through array
            consoleService.printTransferHistory(currentUser, transfer); //  uses console service to print the transfers neatly
        }

        int choice = consoleService.promptForInt("Enter transfer ID to view details of transfer (0 to Exit) "); // asks user if they want to see transaction

        if (transferService.transferIdIsValid(transferHistory, choice)) {  // if the transaction choice is valid
            consoleService.printTransferDetails(currentUser, transferService.getTransfersByTransferId(currentUser, choice)); // prints the details
        }else if(choice == 0 ){ // if choice equals zero, exits the process and kicks back to main menu
            System.out.println();
            System.out.println("Exiting...");
        }else{
            System.out.println();
            System.out.println("Please Try Again, Selection Not Valid"); // tells them the choice wasn't valid and kicks back to menu
        }
    }

	private void viewPendingRequests() {
        System.out.println("-------------------------------");
        System.out.println("Transfers");
        System.out.println("ID     From/To          Amount");
        System.out.println("-------------------------------");
        Transfer[] transfers = transferService.getPendingTransfersByUserId(currentUser);

        for (Transfer transfer : transfers) { //  loops through array

            consoleService.printTransferHistory(currentUser, transfer); //  uses console service to print the transfers neatly
        }

        int choice = consoleService.promptForInt("Enter transfer ID to approve or deny transfer (0 to Exit) "); // asks user for transfer id of request they would like to make choice on

        if (transferService.transferIdIsValid(transfers, choice)) {  // if the transaction choice is valid
            consoleService.approveOrDenyTransfer(currentUser, transferService.getTransfersByTransferId(currentUser, choice)); // prints the details
        }else if(choice == 0 ){ // if choice equals zero, exits the process and kicks back to main menu
            System.out.println();
            System.out.println("Exiting...");
        }else{
            System.out.println();
            System.out.println("Please Try Again, Selection Not Valid"); // tells them the choice wasn't valid and kicks back to menu
        }

    }

    public void sendBucksByRequest(int transferType, int transferStatus){
        List<User> userList = userService.listUsers(currentUser);
        BigDecimal amount = null;

        int currentUserAccountID = AccountService.getAccountIDByUserId(currentUser.getToken(), currentUser.getUser().getId());
        int toWhom = 0;

        BigDecimal ballanceOfCurrentUser = accountService.getBalance(currentUser.getToken(),currentUser.getUser().getId());


        Transfer transfer = null;
        if (transferType == 1){

            toWhom = consoleService.promptForMenuSelection("Who to request (enter number): ");
            long selectedUserID = userList.get(toWhom - 1).getId();
            amount = BigDecimal.valueOf(consoleService.promptForMenuSelection("How much to transfer: "));
            int selectedUserAccountID = AccountService.getAccountIDByUserId(currentUser.getToken(),selectedUserID);
            transfer = new Transfer(transferType, transferType, Integer.parseInt(selectedUserAccountID+"") ,currentUserAccountID,  amount);
        }else if (transferType == 2){

            toWhom = consoleService.promptForMenuSelection("Who to send (enter number): ");
            amount = BigDecimal.valueOf(consoleService.promptForMenuSelection("How much to transfer: "));
            long selectedUserID = userList.get(toWhom - 1).getId();
            int selectedUserAccountID = AccountService.getAccountIDByUserId(currentUser.getToken(),selectedUserID);
            transfer = new Transfer(transferType, transferType, currentUserAccountID,  Integer.parseInt(selectedUserAccountID+"") , amount);
        }



        if (currentUserAccountID == toWhom) {
            consoleService.printCannotSendToYourself();
            mainMenu();
        }
        if (ballanceOfCurrentUser.doubleValue() < amount.doubleValue()){
            consoleService.printNotEnoughMoney();
            mainMenu();
        }
        if (amount.doubleValue() <= 0){
            consoleService.printAmountCannotBeZero();
            mainMenu();
        }
        else {transferService.sendBucks(currentUser, transfer);}
    }

}
