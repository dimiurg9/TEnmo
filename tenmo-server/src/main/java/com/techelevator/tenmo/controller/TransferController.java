package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.exceptions.InsufficientFunds;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.model.TransferType;
import com.techelevator.tenmo.services.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/transfers")
@PreAuthorize("isAuthenticated()")
public class TransferController {



    @Autowired
    TransferService transferService;

    // TRANSFER METHODS

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Transfer> getTransfers(){
        return transferService.getTransfers();
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public List<Transfer> getTransfersByUserId(@PathVariable Long id){
        return transferService.getTransfersByUserId(id);
    }

    @RequestMapping(value = "/transfer/{transferId}", method = RequestMethod.GET)
    public Transfer getTransferByTransferId(@PathVariable Integer transferId){
        return transferService.getTransferByTransferId(transferId);
    }

    @RequestMapping(value = "/user/pending/{userId}", method = RequestMethod.GET)
    public List<Transfer> getPendingTransfers(@PathVariable long userId){
        return transferService.getPendingTransfers(userId);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/createtransfer", method = RequestMethod.POST) //  used for sending transfers
    public void createTransfer(@RequestBody Transfer transfer) throws InsufficientFunds {
//        int type = transfer.getTransferTypeId();

        transferService.createTransfer(transfer);
    }

    @RequestMapping(path = "/update", method = RequestMethod.PUT) // if the user marks the transfer as approved, then the transfer happens and updates the transfer table
    public void updateTransfer(@RequestBody Transfer transfer) throws InsufficientFunds{
        transferService.updateTransfer(transfer);
    }

    // TRANSFER STATUS METHODS

    @RequestMapping(path = "/transferstatus", method = RequestMethod.GET)
    public List<TransferStatus> getAllTransferStatuses(){
        return transferService.getAllTransferStatuses();
    }


    @RequestMapping(path = "/transferstatus/id/{id}", method = RequestMethod.GET)
    public TransferStatus getTransferStatusId(@PathVariable int id){
        return transferService.getTransferStatusId(id);
    }

    @RequestMapping(path = "/transferstatus/desc/{desc}", method = RequestMethod.GET)
    public TransferStatus getTransferStatusDesc(@PathVariable String desc){
        return transferService.getTransferStatusDesc(desc);
    }

    // TRANSFER TYPE METHODS

    @RequestMapping(path = "/transfertypes", method = RequestMethod.GET)
    public List<TransferType> getAllTransferTypes(){
        return transferService.getAllTransferTypes();
    }

    @RequestMapping(path = "/transfertypes/id/{id}", method = RequestMethod.GET)
    public TransferType getTransferTypeById(@PathVariable int id){
        return transferService.getTransferTypeById(id);
    }

    @RequestMapping(path = "/transfertypes/desc/{desc}", method = RequestMethod.GET)
    public TransferType getTransferTypeByDesc(@PathVariable String desc){
        return transferService.getTransferTypeByDesc(desc);
    }


}

