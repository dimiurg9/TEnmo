package com.techelevator;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.activation.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JdbcAccountDaoTest extends BaseDaoTests {

    private static final Account ACCOUNT_1 = new Account(2001, 1001, new BigDecimal("16000.00"));
    private static final Account ACCOUNT_2 = new Account(2002, 1002, new BigDecimal("2000.00"));
    private static final Account ACCOUNT_3 = new Account(2003, 1003, new BigDecimal("3000.00"));

    List<Account> accounts = new ArrayList<>();

    private JdbcAccountDao sut;

    @Before
    public void setup() {sut = new JdbcAccountDao(dataSource);}

    @Test
    public void getAccount_return_correct_balance(){
        BigDecimal balance1 = sut.findBalanceByUserID(1001);
        Assert.assertEquals("Balance didn't match", new BigDecimal("16000.00"), balance1);
        BigDecimal balance2 = sut.findBalanceByUserID(1002);
        Assert.assertEquals("Balance didn't match", new BigDecimal("2000.00"), balance2);
        BigDecimal balance3 = sut.findBalanceByUserID(1003);
        Assert.assertEquals("Balance didn't match", new BigDecimal("3000.00"), balance3);
    }

    @Test
    public void findAllAccounts_return_list_accounts(){
        accounts.add(ACCOUNT_1);
        accounts.add(ACCOUNT_2);
        accounts.add(ACCOUNT_3);
        List<Account> receivingAll = sut.findAll();

        for (int i = 0; i < receivingAll.size(); i++){
            assertAccountsMatch(accounts.get(i), receivingAll.get(i));
        }
    }


    private void assertAccountsMatch(Account expected, Account actual) {
        Assert.assertEquals(expected.getBalance(), actual.getBalance());
        Assert.assertEquals(expected.getAccountId(), actual.getAccountId());
        Assert.assertEquals(expected.getUserId(), actual.getUserId());
    }



}
