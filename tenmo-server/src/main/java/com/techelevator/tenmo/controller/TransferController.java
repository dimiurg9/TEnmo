package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.TransferStatusDao;
import com.techelevator.tenmo.dao.TransferTypeDao;
import com.techelevator.tenmo.exceptions.InsufficientFunds;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.model.TransferType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(path = "/transfers")
//@PreAuthorize("isAuthorized")
public class TransferController {


    @Autowired
    private TransferDao transferDao;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private TransferStatusDao transferStatusDao;
    @Autowired
    private TransferTypeDao transferTypeDao;

    // TRANSFER METHODS

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Transfer> getTransfers(){
        return transferDao.listAll();
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public List<Transfer> getTransfersByUserId(@PathVariable Long id){
        return transferDao.getTransfersByUser(id);
    }

    @RequestMapping(value = "/transfer/{transferId}", method = RequestMethod.GET)
    public Transfer getTransferByTransferId(@PathVariable Integer transferId){
        return transferDao.getTransferById(transferId);
    }

    @RequestMapping(value = "/user/pending/{userId}", method = RequestMethod.GET)
    public List<Transfer> getPendingTransfers(@PathVariable long userId){
        return transferDao.getPendingTransfers(userId);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/createtransfer", method = RequestMethod.POST) //  used for sending transfers
    public void createTransfer(@RequestBody Transfer transfer) throws InsufficientFunds {

        if (transfer.getTransferTypeId() == 2) { //  if the user is sending and not requesting it automatically transfers
            BigDecimal amount = transfer.getAmount();

            Account accountFrom = accountDao.getAccountById(transfer.getAccountFrom());
            Account accountTo = accountDao.getAccountById(transfer.getAccountTo());

//ToDO
            BigDecimal newAccountFromBalance = accountFrom.getBalance().subtract(amount);
            if(newAccountFromBalance.compareTo(BigDecimal.ZERO) >= 0 & accountFrom.getUserId() != accountTo.getUserId()) {
                accountFrom.setBalance(newAccountFromBalance);
                BigDecimal newAccountToBalance = accountTo.getBalance().add(amount);
                accountTo.setBalance(newAccountToBalance);

                transferDao.createTransfer(transfer);

                accountDao.update(accountFrom);
                accountDao.update(accountTo);
            }else {
                throw new InsufficientFunds();

            }
        }else{
            transferDao.createTransfer(transfer); // if the sender is requesting and not sending, it creates a transfer
        }
    }

    @RequestMapping(path = "/update", method = RequestMethod.PUT) // if the user marks the transfer as approved, then the transfer happens and updates the transfer table
    public void updateTransfer(@RequestBody Transfer transfer) throws InsufficientFunds{
        if(transfer.getTransferStatusId() == transferStatusDao.getTransferStatusByDesc("Approved").getTransferStatusId()){
            BigDecimal amount = transfer.getAmount();

            Account accountFrom = accountDao.getAccountById(transfer.getAccountFrom());
            Account accountTo = accountDao.getAccountById(transfer.getAccountTo());

            BigDecimal newAccountFromBalance = accountFrom.getBalance().subtract(amount);
            if(newAccountFromBalance.compareTo(BigDecimal.ZERO) >= 0) {
                accountFrom.setBalance(newAccountFromBalance);
                BigDecimal newAccountToBalance = accountTo.getBalance().add(amount);
                accountTo.setBalance(newAccountToBalance);

                transferDao.updateTransfer(transfer);

                accountDao.update(accountFrom);
                accountDao.update(accountTo);
            }else{
                throw new InsufficientFunds();
            }
        }else{ //  if they mark it as denied, or pending then it just updates the transfer table
            transferDao.updateTransfer(transfer);
        }
    }

    // TRANSFER STATUS METHODS

    @RequestMapping(path = "/transferstatus", method = RequestMethod.GET)
    public List<TransferStatus> getAllTransferStatuses(){
        return transferStatusDao.getAllTransferStatuses();
    }


    @RequestMapping(path = "/transferstatus/id/{id}", method = RequestMethod.GET)
    public TransferStatus getTransferStatusId(@PathVariable int id){
        return transferStatusDao.getTransferStatusById(id);
    }

    @RequestMapping(path = "/transferstatus/desc/{desc}", method = RequestMethod.GET)
    public TransferStatus getTransferStatusDesc(@PathVariable String desc){
        return transferStatusDao.getTransferStatusByDesc(desc);
    }

    // TRANSFER TYPE METHODS

    @RequestMapping(path = "/transfertypes", method = RequestMethod.GET)
    public List<TransferType> getAllTransferTypes(){
        return transferTypeDao.getAllTransferTypes();
    }

    @RequestMapping(path = "/transfertypes/id/{id}", method = RequestMethod.GET)
    public TransferType getTransferTypeById(@PathVariable int id){
        return transferTypeDao.getTransferTypeById(id);
    }

    @RequestMapping(path = "/transfertypes/desc/{desc}", method = RequestMethod.GET)
    public TransferType getTransferTypeByDesc(@PathVariable String desc){
        return transferTypeDao.getTransferTypeByDesc(desc);
    }


}

