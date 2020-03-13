package com.fdmgroup.BankAccountApp;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import com.fdmgroup.BankAccountApp.exceptions.DuplicateUsernameException;
import com.fdmgroup.BankAccountApp.pojos.BankAccount;
import com.fdmgroup.BankAccountApp.pojos.CheckingAccount;
import com.fdmgroup.BankAccountApp.pojos.Customer;
import com.fdmgroup.BankAccountApp.pojos.SavingsAccount;
import com.fdmgroup.BankAccountApp.services.BankAccountService;
import com.fdmgroup.BankAccountApp.services.CustomerService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersistenceTest {
	
	@Autowired
	ApplicationContext context;
	@Autowired
	CustomerService customerService;
	@Autowired
	BankAccountService bankAccountService;
	

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test_AddACustomerToDatabase() throws DuplicateUsernameException {
		Customer customer = context.getBean(Customer.class);
		customer.setFirstname("saul");
		customer.setLastname("tobias");
		customer.setEmail("email@email.com");
		customer.setPassword("password");
		customer.setUsername("saul.tobias");
		customerService.save(customer);
		
		assertEquals(customer, customerService.find(customer.getCustomerId()));
	}
	
	@Test
	public void test_UpdateACustomerInDatabase() throws DuplicateUsernameException {
		Customer customer = context.getBean(Customer.class);
		customer.setFirstname("brenda");
		customer.setLastname("tobias");
		customer.setEmail("email@email.com");
		customer.setPassword("password");
		customer.setUsername("brenda.tobias");
		customerService.save(customer);
		
		customerService.save(customer);
		
		customer.setFirstname("Saul");
		
		customerService.update(customer);
		
		assertEquals(customer, customerService.find(customer.getCustomerId()));
	}
	
	@Test
	public void test_AddABankAccountInDatabase() throws DuplicateUsernameException {
		BankAccount bankAccount = context.getBean("bankAccount", BankAccount.class);
		bankAccount.setBalance(new BigDecimal(0));
		bankAccount.setBankAccountName("bank1");
		bankAccount.setType("Checking Account");
		
		Customer customer = context.getBean(Customer.class);
		customer.setFirstname("kareli");
		customer.setLastname("tobias");
		customer.setEmail("email@email.com");
		customer.setPassword("password");
		customer.setUsername("kareli.tobias");
		
		Set<BankAccount> bankAccounts = new HashSet<>();
		bankAccounts.add(bankAccount);
		
		customer.setBankAccounts(bankAccounts);
		customerService.save(customer);
		
		bankAccount.setCustomer(customer);
		
		bankAccountService.save(bankAccount);
		
		assertEquals(bankAccount, bankAccountService.find(bankAccount.getBankAccountId()));
	}
	
	@Test
	public void test_AddACheckingAccount() throws DuplicateUsernameException {
		BankAccount bankAccount = context.getBean("checkingAccount", CheckingAccount.class);
		bankAccount.setBalance(new BigDecimal(0));
		bankAccount.setBankAccountName("bank2");
		bankAccount.setType("Checking Account");
		
		Customer customer = context.getBean(Customer.class);
		customer.setFirstname("maria");
		customer.setLastname("tobias");
		customer.setEmail("email@email.com");
		customer.setPassword("password");
		customer.setUsername("maria.tobias");
		
		Set<BankAccount> bankAccounts = new HashSet<>();
		bankAccounts.add(bankAccount);
		
		customer.setBankAccounts(bankAccounts);
		customerService.save(customer);
		
		bankAccount.setCustomer(customer);
		
		bankAccountService.save(bankAccount);
		
		assertEquals(bankAccount, bankAccountService.find(bankAccount.getBankAccountId()));
	}
	
	@Test
	public void test_AddASavingsAccount() throws DuplicateUsernameException {
		BankAccount bankAccount = context.getBean("savingsAccount", SavingsAccount.class);
		bankAccount.setBalance(new BigDecimal(0));
		bankAccount.setBankAccountName("bank3");
		bankAccount.setType("Savings Account");
		
		Customer customer = context.getBean(Customer.class);
		customer.setFirstname("perfecto");
		customer.setLastname("tobias");
		customer.setEmail("email@email.com");
		customer.setPassword("password");
		customer.setUsername("perfecto.tobias");
		
		Set<BankAccount> bankAccounts = new HashSet<>();
		bankAccounts.add(bankAccount);
		
		customer.setBankAccounts(bankAccounts);
		customerService.save(customer);
		
		bankAccount.setCustomer(customer);
		
		bankAccountService.save(bankAccount);
		
		assertEquals(bankAccount, bankAccountService.find(bankAccount.getBankAccountId()));
	}
	
	@Test
	public void test_FindBankAccountByAccountNameAndCustomerId() throws DuplicateUsernameException {
		BankAccount bankAccount = context.getBean("savingsAccount", SavingsAccount.class);
		bankAccount.setBalance(new BigDecimal(0));
		bankAccount.setBankAccountName("bank4");
		bankAccount.setType("Savings Account");
		
		Customer customer = context.getBean(Customer.class);
		customer.setFirstname("bernardo");
		customer.setLastname("tobias");
		customer.setEmail("email@email.com");
		customer.setPassword("password");
		customer.setUsername("bernardo.tobias");
		
		Set<BankAccount> bankAccounts = new HashSet<>();
		bankAccounts.add(bankAccount);
		
		customer.setBankAccounts(bankAccounts);
		customerService.save(customer);
		bankAccount.setCustomer(customer);
		
		bankAccountService.save(bankAccount);
		
		assertEquals(bankAccount, bankAccountService.findByAccountNameAndCustomerId(bankAccount.getBankAccountName(), customer));
	}
	
	@Test(expected = DuplicateUsernameException.class)
	public void test_AddCustomerWithDuplicateUsername_ThrowsException() throws DuplicateUsernameException {
		Customer customer = context.getBean(Customer.class);
		customer.setFirstname("saul");
		customer.setLastname("tobias");
		customer.setEmail("email@email.com");
		customer.setPassword("password");
		customer.setUsername("saul.tobias");
		
		customerService.save(customer);
	}
	
	@Test
	public void test_findByUsername_ReturnsUserFromDatabase() throws DuplicateUsernameException {
		Customer customer = context.getBean(Customer.class);
		customer.setFirstname("arellano");
		customer.setLastname("tobias");
		customer.setEmail("email@email.com");
		customer.setPassword("password");
		customer.setUsername("arellano.tobias");
		
		customerService.save(customer);
		
		assertEquals(customer, customerService.findByUsername(customer.getUsername()));
	}
	
	@Test(expected = UsernameNotFoundException.class)
	public void test_FindUsernameThatDoesNotExist_ThrowsException() {
		customerService.findByUsername("usernamethatdoesnotexistatall");
	}
	
	
}
