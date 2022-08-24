package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferStatusDao implements TransferStatusDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferStatusDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<TransferStatus> getAllTransferStatuses() {
        List<TransferStatus> transferStatusList = new ArrayList<>();
        String sql = "SELECT transfer_status_id, transfer_status_desc FROM transfer_status";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        while(rowSet.next()){
            TransferStatus transferStatus = new TransferStatus();
            transferStatus = mapRowToTransferStatus(rowSet);
            transferStatusList.add(transferStatus);
        }
        return transferStatusList;
    }


    @Override
    public TransferStatus getTransferStatusById(int id) {
        TransferStatus transferStatus = new TransferStatus();
        String sql = "SELECT transfer_status_id, transfer_status_desc FROM transfer_status WHERE transfer_status_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
        while(rowSet.next()){
            transferStatus = mapRowToTransferStatus(rowSet);
        }
        return transferStatus;
    }

    @Override
    public TransferStatus getTransferStatusByDesc(String desc) {
        TransferStatus transferStatus = new TransferStatus();
        String sql = "SELECT transfer_status_id, transfer_status_desc FROM transfer_status WHERE transfer_status_desc = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql,desc);
        if(rowSet.next()){
            transferStatus = mapRowToTransferStatus(rowSet);
        }
        return transferStatus;
    }

    public TransferStatus mapRowToTransferStatus(SqlRowSet rowSet){
        TransferStatus transferStatus = new TransferStatus();
        transferStatus.setTransferStatusId(rowSet.getInt("transfer_status_id"));
        transferStatus.setTransferStatusDesc(rowSet.getString("transfer_status_desc"));
        return transferStatus;
    }
}
