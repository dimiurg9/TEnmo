package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.math.BigDecimal;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService();
    private final TransferService transferService = new TransferService();

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
        System.out.println(currentUser.getToken());
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
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
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
        Transfer[] transferHistory = transferService.getTransfersByUserId(currentUser);
        System.out.println("-------------------------------");
        System.out.println("Transfers");
        System.out.println("ID     From/To          Amount");
        System.out.println("-------------------------------");

        for (Transfer transfer : transferHistory) {
            consoleService.printTransferHistory(currentUser, transfer);
        }

        int choice = consoleService.promptForInt("Enter transfer ID to view details of transfer, or enter 0 to cancel ");

//            if (transferService.transferIdIsValid(transferHistory, choice)) { // <----------------trying to figure out exception handling here
        consoleService.printTransferDetails(currentUser, transferService.getTransfersByTransferId(currentUser, choice));
//            } else {
//                System.out.println("Please Try Again, Selection Not Valid");
//            }
		
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

	private void sendBucks() {
		// TODO Auto-generated method stub
        int currentUserAccountID = AccountService.getAccountIDByUserId(currentUser.getToken(), currentUser.getUser().getId());
        int toWhom = consoleService.promptForMenuSelection("Who to send (enter number): ");
        BigDecimal ballanceOfCurrentUser = accountService.getBalance(currentUser.getToken(),currentUser.getUser().getId());
        BigDecimal amount = BigDecimal.valueOf(consoleService.promptForMenuSelection("How much to transfer: "));
        Transfer transfer = new Transfer(2, 1, currentUserAccountID, toWhom, amount);
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

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}



}
