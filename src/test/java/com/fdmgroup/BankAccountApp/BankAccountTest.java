package com.fdmgroup.BankAccountApp;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fdmgroup.BankAccountApp.exceptions.InsufficientFundsException;
import com.fdmgroup.BankAccountApp.pojos.BankAccount;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BankAccountTest {
	
	@Autowired
	BankAccount bankAccount;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test_Deposit5IncreasesBankAccountBalanceBy5() {
		bankAccount.deposit(new BigDecimal(5));
		
		assertEquals(new BigDecimal(5), bankAccount.getBalance());
	}
	
	@Test
	public void test_Withdraw5FromBankAccountWithBalanceOf10ResultsInBalanceOf5() throws InsufficientFundsException {
		bankAccount.setBalance(new BigDecimal(10));
		bankAccount.withdraw(new BigDecimal(5));
		
		assertEquals(new BigDecimal(5), bankAccount.getBalance());
	}
	
	@Test(expected = InsufficientFundsException.class)
	public void test_WhenWithdrawAmountGreaterThanCurrentBalanceThrowException() throws InsufficientFundsException{
		bankAccount.setBalance(new BigDecimal(10));
		bankAccount.withdraw(new BigDecimal(15));
	}

}
