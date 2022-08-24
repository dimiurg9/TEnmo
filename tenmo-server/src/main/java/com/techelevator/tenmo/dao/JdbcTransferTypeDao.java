package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class JdbcTransferTypeDao implements TransferTypeDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferTypeDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<TransferType> getAllTransferTypes() {
        List<TransferType> transferTypes = new ArrayList<>();
        String sql = "SELECT transfer_type_id, transfer_type_desc FROM transfer_type";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        while (rowSet.next()){
            TransferType transferType = mapRowToTransferType(rowSet);
            transferTypes.add(transferType);
        }
        return transferTypes;
    }

    @Override
    public TransferType getTransferTypeById(int id) {
        TransferType transferType = new TransferType();
        String sql = "SELECT transfer_type_id, transfer_type_desc FROM transfer_type WHERE transfer_type_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
        if(rowSet.next()){
            transferType = mapRowToTransferType(rowSet);
        }
        return transferType;
    }

    @Override
    public TransferType getTransferTypeByDesc(String desc) {
        TransferType transferType = new TransferType();
        String sql = "SELECT transfer_type_id, transfer_type_desc FROM transfer_type WHERE transfer_type_desc = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, desc);
        if(rowSet.next()){
            transferType = mapRowToTransferType(rowSet);
        }
        return transferType;
    }

    public TransferType mapRowToTransferType(SqlRowSet rowSet){
        TransferType transferType = new TransferType();
        transferType.setTransferTypeId(rowSet.getInt("transfer_type_id"));
        transferType.setTransferTypeDesc(rowSet.getString("transfer_type_desc"));
        return transferType;
    }
}
