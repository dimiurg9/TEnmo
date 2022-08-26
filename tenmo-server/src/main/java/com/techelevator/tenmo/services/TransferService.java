package com.techelevator.tenmo.services;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.TransferStatusDao;
import com.techelevator.tenmo.dao.TransferTypeDao;
import com.techelevator.tenmo.exceptions.InsufficientFunds;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.model.TransferType;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.List;

@Service
public class TransferService {


    private final TransferDao transferDao;
    private final AccountDao accountDao;
    private final TransferStatusDao transferStatusDao;
    private final TransferTypeDao transferTypeDao;

    public TransferService(TransferDao transferDao, AccountDao accountDao, TransferStatusDao transferStatusDao, TransferTypeDao transferTypeDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
        this.transferStatusDao = transferStatusDao;
        this.transferTypeDao = transferTypeDao;
    }

    public List<Transfer> getTransfers(){ return transferDao.listAll(); }

    public List<Transfer> getTransfersByUserId(Long id){
        return transferDao.getTransfersByUser(id);
    }

    public Transfer getTransferByTransferId(Integer transferId){
        return transferDao.getTransferById(transferId);
    }

    public List<Transfer> getPendingTransfers(long userId){
        return transferDao.getPendingTransfers(userId);
    }

    public void createTransfer(Transfer transfer) throws InsufficientFunds {

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

    public void updateTransfer(Transfer transfer) throws InsufficientFunds{
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

    public List<TransferStatus> getAllTransferStatuses(){
        return transferStatusDao.getAllTransferStatuses();
    }

    public TransferStatus getTransferStatusId(int id){
        return transferStatusDao.getTransferStatusById(id);
    }

    public TransferStatus getTransferStatusDesc(String desc){
        return transferStatusDao.getTransferStatusByDesc(desc);
    }

    public List<TransferType> getAllTransferTypes(){
        return transferTypeDao.getAllTransferTypes();
    }

    public TransferType getTransferTypeById(int id){
        return transferTypeDao.getTransferTypeById(id);
    }

    public TransferType getTransferTypeByDesc(String desc){
        return transferTypeDao.getTransferTypeByDesc(desc);
    }
}
