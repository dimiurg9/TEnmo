package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    List<Transfer> listAll(); // to list all the Transfers coming and going

    void createTransfer(Transfer transfer); // creates a transfer in the database

    List<Transfer> getTransfersByUser(long userId); // lists all transfers belonging to user

    List<Transfer> getPendingTransfers(long userId); //  gets only the transfers that are in pending

    Transfer getTransferById(int transferId); // gets transfer with transfer ID

    void updateTransfer(Transfer transfer); // updates the transfer in the database



}
