package com.techelevator.tenmo.services;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private RestTemplate restTemplate = new RestTemplate();
    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    private JdbcAccountDao accountDao;

    public AccountService(JdbcAccountDao accountDao){
        this.accountDao = accountDao;
    }


    public List<Account> getAccounts(){ //  this will get all the accounts in the database, hopefully can use for transfer service
        return accountDao.findAll();
    }

    public Account getAccountByAccountId(int accountId){
        return accountDao.getAccountById(accountId);
    }

    public Account getAccountById(long id){
        return accountDao.getAccountByUserId(id);
    }

    public BigDecimal findBalanceByUserID(long userId){
        return accountDao.findBalanceByUserID(userId);
    }

    public long getUserIdByAccountId(int accountId){
        return accountDao.getUserIdByAccountId(accountId);
    }

    public BigDecimal getBalance(int userId){ //  will get the balance in BigDecimal format by calling the dao
        return accountDao.getBalance(userId);
    }



//    public void getBalance(String token){
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(token);
//        HttpEntity<Void> entity = new HttpEntity<>(headers);
//        ResponseEntity<Account> response = restTemplate.exchange(
//                "http://localhost:8080/account/balance", HttpMethod.GET, entity, Account.class);
}



