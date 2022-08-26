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
		// TODO Auto-generated method stub
		
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {
		// TODO Auto-generated method stub


        //        BigDecimal transferId = BigDecimal.valueOf(0.00);
//        transfer.setTransferTypeId(2);
//        transfer.setTransferStatusId(2);
//        transfer.setAccountFrom(Math.toIntExact(currentUser.getUser().getId()));
        int toWhom = consoleService.promptForMenuSelection("Who to send (enter number): ");
//        transfer.setAccountTo(toWhom);
        double amount = consoleService.promptForMenuSelection("How much to transfer: ");
//        transfer.setAmount(BigDecimal.valueOf(amount));
        Transfer transfer = new Transfer(2, 1, 2001, toWhom, BigDecimal.valueOf(amount));
        transferService.sendBucks(currentUser, transfer);



//        Transfer newCreatedTransfer = TransferService.createTransfer(transfer, token);
//        System.out.println(newCreatedTransfer.getTransferId());
		
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}



}
