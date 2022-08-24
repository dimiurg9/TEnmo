package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferStatus;

import java.util.List;

public interface TransferStatusDao { // this dao is only really used to check the descriptions of the transfers for updating

    List<TransferStatus> getAllTransferStatuses();

    TransferStatus getTransferStatusById(int id);

    TransferStatus getTransferStatusByDesc(String desc);

}
