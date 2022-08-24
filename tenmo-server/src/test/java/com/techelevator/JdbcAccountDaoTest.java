package com.techelevator;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
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

//    private static SingleConnectionDataSource dataSource;
//    private static JdbcTemplate jdbcTemplate;
    private static DataSource dataSource;

    private static final Account ACCOUNT_1 = new Account(2001, 1001, BigDecimal.valueOf(1000));
    private static final Account ACCOUNT_2 = new Account(2002, 1002, BigDecimal.valueOf(2000));
    private static final Account ACCOUNT_3 = new Account(2003, 1003, BigDecimal.valueOf(3000));


    private JdbcAccountDao sut;

//    @Before
//    public void setup() {sut = new JdbcAccountDao(dataSource);} //providing error. Casted into JdbcTemplate

//    @Before
//    public void setup() {sut = new JdbcAccountDao((javax.sql.DataSource) dataSource);}

    @Before
    public void setup() {sut = new JdbcAccountDao((javax.sql.DataSource) dataSource);}

    @Test
    public void getAccount_return_correct_balance(){

        BigDecimal balance = sut.getBalance(1001);
        Assert.assertEquals("Balance didn't match", 1000, balance);
        BigDecimal balance2 = sut.getBalance(1002);
        Assert.assertEquals("Balance didn't match", 2000, balance);
        BigDecimal balance3 = sut.getBalance(1003);
        Assert.assertEquals("Balance didn't match", 3000, balance);
    }
}
