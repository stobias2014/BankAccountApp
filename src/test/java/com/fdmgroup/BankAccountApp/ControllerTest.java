package com.fdmgroup.BankAccountApp;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fdmgroup.BankAccountApp.controller.Controller;
import com.fdmgroup.BankAccountApp.exceptions.DuplicateUsernameException;
import com.fdmgroup.BankAccountApp.pojos.BankAccount;
import com.fdmgroup.BankAccountApp.pojos.Customer;
import com.fdmgroup.BankAccountApp.services.BankAccountService;
import com.fdmgroup.BankAccountApp.services.CustomerService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {
	
	@Autowired
	private ApplicationContext context;
	@Autowired
	private WebApplicationContext webctx;
	@Autowired
	private Controller controller;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private BankAccountService bankAccountService;
	@Autowired
	private MockMvc mockmvc;
	@Autowired
	PasswordEncoder encoder;
	
	@Before
	public void setUp() throws Exception {
		mockmvc = MockMvcBuilders.webAppContextSetup(webctx).build();
	}

	@Test
	public void test_WhenApplicationStarts_GoToIndex() throws Exception{
		mockmvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("index"))
		.andExpect(forwardedUrl("/WEB-INF/jsp/index.jsp"));
	}
	
	@Test
	public void test_WhenGetRegister_GoToRegister() throws Exception{
		mockmvc.perform(get("/register")).andExpect(status().isOk()).andExpect(view().name("register"))
		.andExpect(forwardedUrl("/WEB-INF/jsp/register.jsp"));
	}
	
	@Test
	public void test_WhenGetLogin_GoToLogin() throws Exception{
		mockmvc.perform(get("/login")).andExpect(status().isOk()).andExpect(view().name("login"))
		.andExpect(forwardedUrl("/WEB-INF/jsp/login.jsp"));
	}
	
	@Test
	public void test_WhenGetHome_GoToBankManagement() throws Exception{
		mockmvc.perform(get("/home").sessionAttr("customer", webctx.getBean(Customer.class))).andExpect(status().isOk()).andExpect(view().name("bankManagement"))
		.andExpect(forwardedUrl("/WEB-INF/jsp/bankManagement.jsp"))
		.andExpect(model().attribute("bankAccounts", webctx.getBean(Customer.class).getBankAccounts()));
	}
	
	@Test
	public void test_WhenGetAddBankAccount_GoToAddBankAccount() throws Exception{
		mockmvc.perform(get("/addBankAccount").sessionAttr("customer", webctx.getBean(Customer.class))).andExpect(status().isOk()).andExpect(view().name("addBankAccount"))
		.andExpect(forwardedUrl("/WEB-INF/jsp/addBankAccount.jsp"))
		.andExpect(model().attribute("customer", webctx.getBean(Customer.class)));
	}
	
	@Test
	public void test_WhenPostToConfirmCustomerAccount_ValidationNotNull_PasswordDoesNotContainUppercaseLowercaseAndNumber_GoToRegister() throws Exception {
		mockmvc.perform(post("/confirmCustomerAccount").param("password", "password").param("confirmPassword", "password"))
		.andExpect(status().isOk()).andExpect(view().name("register"))
		.andExpect(forwardedUrl("/WEB-INF/jsp/register.jsp"));
	}
	
	@Test
	public void test_WhenPostToConfirmCustomerAccount_ValidationNotNull_PasswordTooShort_GoToRegister() throws Exception {
		mockmvc.perform(post("/confirmCustomerAccount").param("password", "pas").param("confirmPassword", "pas"))
		.andExpect(status().isOk()).andExpect(view().name("register"))
		.andExpect(forwardedUrl("/WEB-INF/jsp/register.jsp"));
	}
	
	@Test
	public void test_WhenPostToConfirmCustomerAccount_ValidationNotNull_PasswordContainsSpecialCharacter_GoToRegister() throws Exception {
		mockmvc.perform(post("/confirmCustomerAccount").param("password", "Password1+").param("confirmPassword", "Password1+"))
		.andExpect(status().isOk()).andExpect(view().name("register"))
		.andExpect(forwardedUrl("/WEB-INF/jsp/register.jsp"));
	}
	
	@Test
	public void test_WhenPostToConfirmCustomerAccount_ValidationNotNull_PasswordsDoNotMatch_GoToRegister() throws Exception {
		mockmvc.perform(post("/confirmCustomerAccount").param("password", "Password1").param("confirmPassword", "Password123"))
		.andExpect(status().isOk()).andExpect(view().name("register"))
		.andExpect(forwardedUrl("/WEB-INF/jsp/register.jsp"));
	}
	
	@Test
	public void test_WhenPostToConfirmCustomerAccountWithCheckingAccountInput_ValidationNull_GoToConfirmCustomerAccount() throws Exception {
		mockmvc.perform(post("/confirmCustomerAccount")
				.param("lastname", "lastname")
				.param("username", "username")
				.param("email", "email")
				.param("password", "Password1")
				.param("confirmPassword", "Password1")
				.param("firstname", "firstname")
				.param("bankaccount", "Checking Account"))
		.andExpect(status().isOk()).andExpect(view().name("confirmCustomerAccount"))
		.andExpect(forwardedUrl("/WEB-INF/jsp/confirmCustomerAccount.jsp"));
	}
	
	@Test
	public void test_WhenPostToConfirmCustomerAccountWithSavingsAccountInput_ValidationNull_GoToConfirmCustomerAccount() throws Exception {
		mockmvc.perform(post("/confirmCustomerAccount")
				.param("lastname", "lastname")
				.param("username", "username1")
				.param("email", "email")
				.param("password", "Password1")
				.param("confirmPassword", "Password1")
				.param("firstname", "firstname")
				.param("bankaccount", "Savings Account"))
		.andExpect(status().isOk()).andExpect(view().name("confirmCustomerAccount"))
		.andExpect(forwardedUrl("/WEB-INF/jsp/confirmCustomerAccount.jsp"));
	}
	
	@Test
	public void test_WhenPostToConfirmCustomerAccountThatAlreadyExists_GoToRegister() throws Exception, DuplicateUsernameException {
		Customer customer = context.getBean(Customer.class);
		customer.setFirstname("firstName");
		customer.setLastname("lastName");
		customer.setUsername("repeatusername");
		customer.setEmail("email@email.com");
		customer.setPassword("myPassword");
		customerService.save(customer);
		
		mockmvc.perform(post("/confirmCustomerAccount")
				.param("lastname", "lastname")
				.param("username", "repeatusername")
				.param("email", "email")
				.param("password", "Password1")
				.param("confirmPassword", "Password1")
				.param("firstname", "firstname")
				.param("bankaccount", "Savings Account"))
		.andExpect(status().isOk()).andExpect(view().name("register"))
		.andExpect(forwardedUrl("/WEB-INF/jsp/register.jsp"));
	}
	
	@Test
	public void test_WhenPostToBankManagement_UsernameDoesNotExist_GoToLogin() throws Exception, UsernameNotFoundException {
		mockmvc.perform(post("/bankManagement").param("username", "usernameDOnOTeXIST").param("password", "password"))
			.andExpect(status().isOk()).andExpect(view().name("login"))
			.andExpect(forwardedUrl("/WEB-INF/jsp/login.jsp"));
	}
	
	@Test
	public void test_WhenPostToBankManagement_PasswordDoesNotMatchr_GoToLogin() throws Exception, UsernameNotFoundException {
		mockmvc.perform(post("/bankManagement").param("username", "username").param("password", "password"))
			.andExpect(status().isOk()).andExpect(view().name("login"))
			.andExpect(forwardedUrl("/WEB-INF/jsp/login.jsp"));
	}
	
	
	@Test
	public void test_WhenPostToBankManagement_GoToBankManagement() throws Exception, UsernameNotFoundException {
		String encodedPassword = encoder.encode("testPassword");
		Customer customer = context.getBean(Customer.class);
		customer.setFirstname("testFirstName");
		customer.setLastname("testLastName");
		customer.setUsername("testUsername");
		customer.setEmail("testEmail");
		customer.setPassword(encodedPassword);
		customerService.save(customer);
		
		mockmvc.perform(post("/bankManagement").param("username", customer.getUsername()).param("password", "testPassword"))
			.andExpect(status().isOk()).andExpect(view().name("bankManagement"))
			.andExpect(forwardedUrl("/WEB-INF/jsp/bankManagement.jsp"));
	}
	
	@Test
	public void test_WhenPostToDeposit_NoBankAccountForCustomer_GoToBankManagement() throws BeansException, Exception {
		Customer customer = webctx.getBean(Customer.class);
		Set<BankAccount> bankAccounts = new HashSet<>();
		customer.setBankAccounts(bankAccounts);
		
		mockmvc.perform(post("/deposit").sessionAttr("customer", customer)
				.param("bankAccountName", "bankAccountName")
				.param("depositAmount", "5"))
		.andExpect(status().isOk()).andExpect(view().name("bankManagement"))
		.andExpect(forwardedUrl("/WEB-INF/jsp/bankManagement.jsp"))
		.andExpect(model().attribute("bankAccounts", customer.getBankAccounts()));
	}
	

	
	@Test
	public void test_WhenPostToWithdraw_NoBankAccountForCustomer_GoToBankManagement() throws BeansException, Exception {
		Customer customer = webctx.getBean(Customer.class);
		Set<BankAccount> bankAccounts = new HashSet<>();
		customer.setBankAccounts(bankAccounts);
		
		mockmvc.perform(post("/withdraw").sessionAttr("customer", customer)
				.param("bankAccountName", "bankAccountName")
				.param("withdrawAmount", "5"))
		.andExpect(status().isOk()).andExpect(view().name("bankManagement"))
		.andExpect(forwardedUrl("/WEB-INF/jsp/bankManagement.jsp"))
		.andExpect(model().attribute("bankAccounts", customer.getBankAccounts()));
	}
	
	@Test
	public void test_WhenPostToAddAccount_AddACheckingAccount_GoToConfirmAddedAccount() throws BeansException, Exception {
		Customer customer = webctx.getBean(Customer.class);
		customer.setFirstname("Some");
		customer.setLastname("Name");
		customer.setEmail("someemail");
		customer.setUsername("someusername");
		customer.setPassword("somepassword");
		Set<BankAccount> bankAccounts = new HashSet<>();
		customer.setBankAccounts(bankAccounts);
		customerService.save(customer);
		
		mockmvc.perform(post("/confirmAddedAccount").sessionAttr("customer", customer)
				.param("bankAccount", "Checking Account")
				.param("bankAccountName", "My Checking Account"))
		.andExpect(status().isOk()).andExpect(view().name("confirmAddedAccount"))
		.andExpect(forwardedUrl("/WEB-INF/jsp/confirmAddedAccount.jsp"));
	}
	
	@Test
	public void test_WhenPostToAddAccount_AddASavingsAccount_GoToConfirmAddedAccount() throws BeansException, Exception {
		Customer customer = webctx.getBean(Customer.class);
		customer.setFirstname("Some");
		customer.setLastname("Name");
		customer.setEmail("someemail");
		customer.setUsername("someusernamethatdidntexistbefore");
		customer.setPassword("somepassword");
		Set<BankAccount> bankAccounts = new HashSet<>();
		customer.setBankAccounts(bankAccounts);
		customerService.save(customer);
		
		mockmvc.perform(post("/confirmAddedAccount").sessionAttr("customer", customer)
				.param("bankAccount", "Savings Account")
				.param("bankAccountName", "My Savings Account"))
		.andExpect(status().isOk()).andExpect(view().name("confirmAddedAccount"))
		.andExpect(forwardedUrl("/WEB-INF/jsp/confirmAddedAccount.jsp"));
	}
	
	
	@Test
	public void test_WhenLogOut_GoToIndex() throws Exception{
		mockmvc.perform(get("/logout")).andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/"))
		.andExpect(redirectedUrl("/"));
	}

}
