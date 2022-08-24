package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferType;

import java.util.List;

public interface TransferTypeDao { // this dao is only really used to see the transfer type table, and compare types.

    List<TransferType> getAllTransferTypes();

    TransferType getTransferTypeById(int id);

    TransferType getTransferTypeByDesc(String desc);
}
