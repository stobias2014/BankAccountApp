package com.fdmgroup.BankAccountApp.controller;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import com.fdmgroup.BankAccountApp.exceptions.DuplicateUsernameException;
import com.fdmgroup.BankAccountApp.exceptions.InsufficientFundsException;
import com.fdmgroup.BankAccountApp.pojos.BankAccount;
import com.fdmgroup.BankAccountApp.pojos.CheckingAccount;
import com.fdmgroup.BankAccountApp.pojos.Customer;
import com.fdmgroup.BankAccountApp.pojos.SavingsAccount;
import com.fdmgroup.BankAccountApp.services.BankAccountService;
import com.fdmgroup.BankAccountApp.services.CustomerService;
import com.fdmgroup.BankAccountApp.validator.Validator;

@org.springframework.stereotype.Controller
public class Controller {
	
	@Autowired
	private ApplicationContext context;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private BankAccountService bankAccountService;
	@Autowired
	private PasswordEncoder encoder;
	
	@GetMapping("/")
	public String toHome() {
		return "index";
	}
	
	@RequestMapping("/register")
	public String toRegister(Model model) {
		Customer customer = context.getBean(Customer.class);
		model.addAttribute("customer", customer);
		return "register";
	}
	
	@PostMapping("/confirmCustomerAccount")
	public String toConfirmCustomerAccount(ModelMap modelMap, HttpServletRequest req, Customer customer) {
		
		String passwordValidation = Validator.validatePassword(customer.getPassword(), req.getParameter("confirmPassword"));
		if (passwordValidation != null) {
			modelMap.addAttribute("badRegister", passwordValidation);
			return "register";
		}
		
		customer.setPassword(encoder.encode(customer.getPassword()));

		
		String bankAccountChoice = req.getParameter("bankaccount");
		System.out.println(bankAccountChoice);
		BankAccount bankAccount = null;
		
		if(bankAccountChoice.equals("Checking Account")) {
			bankAccount = context.getBean("checkingAccount", CheckingAccount.class);
			bankAccount.setType(bankAccountChoice);
			bankAccount.setBankAccountName(customer.getFirstname() + " Checking Account");
			System.out.println("Chcking Account created");
		}
		else if(bankAccountChoice.equals("Savings Account")) {
			bankAccount = context.getBean("savingsAccount", SavingsAccount.class);
			bankAccount.setType(bankAccountChoice);
			bankAccount.setBankAccountName(customer.getFirstname() + " Savings Account");
			System.out.println("Savings Account created");
		}
		
		System.out.println(bankAccount.getType());
		
		Set<BankAccount> bankAccounts = new HashSet<>();
		bankAccounts.add(bankAccount);
		customer.setBankAccounts(bankAccounts);
		
		
		try {
			customerService.save(customer);
		} catch (DuplicateUsernameException e) {
			modelMap.addAttribute("userNameAlreadyExists", "Username already exists, try another username.");
			return "register";
		}
		
		bankAccount.setCustomer(customerService.find(customer.getCustomerId()));
		bankAccountService.save(bankAccount);
		
		modelMap.addAttribute("customer", customer);
		modelMap.addAttribute("bankAccount", bankAccount);
		
		return "confirmCustomerAccount";
	}
	
	@RequestMapping("/login")
	public String toLogin(Model model) {
		Customer customer = context.getBean(Customer.class);
		model.addAttribute("customer", customer);
		return "login";
	}
	
	@RequestMapping("/home")
	public String toCustomerHomePage(ModelMap modelMap, HttpServletRequest req, Customer customer, HttpSession session) {
		customer = (Customer) req.getSession().getAttribute("customer");
		modelMap.addAttribute("bankAccounts", customer.getBankAccounts());
		session = req.getSession();
		session.setAttribute("customer", customer);
		return "bankManagement";
	}
	
	@PostMapping("/bankManagement")
	public String toBankManagement(ModelMap modelMap, HttpServletRequest req, Customer customer) {
		BankAccount bankAccount = null;
		try {

			if (req.getParameter("username") != null) {
				customer = customerService.findByUsername(req.getParameter("username"));
			} else {
				customer = (Customer) req.getSession().getAttribute("customer");
			}

		} catch (UsernameNotFoundException unfe) {
			modelMap.addAttribute("badUsername", "Username not found, try again");
			return "login";
		}
		
		if(encoder.matches(req.getParameter("password"), customer.getPassword())) {
			HttpSession session = req.getSession();
			modelMap.addAttribute("customer", customer);
			modelMap.addAttribute("bankAccount", bankAccount);
			modelMap.addAttribute("bankAccounts", customer.getBankAccounts());
			session.setAttribute("customer", customer);
			return "bankManagement";
		}
		
		modelMap.addAttribute("badPassword", "Incorrect Password, please try again");
		
		return "login";
	}
	
	@PostMapping("/deposit")
	public String toDeposit(ModelMap modelMap, HttpServletRequest req, Customer customer) {
		customer = (Customer) req.getSession().getAttribute("customer");
		System.out.println(customer);
		String bankAccountName = req.getParameter("bankAccountName");
		System.out.println(bankAccountName);
		BigDecimal amount = new BigDecimal(req.getParameter("depositAmount"));
		
		System.out.println(customer.getBankAccounts());
		
		for(BankAccount bankAccount : customer.getBankAccounts()) {
			System.out.println(bankAccount.getBankAccountName());
			if(bankAccount.getBankAccountName().equals(bankAccountName)) {
				bankAccount.deposit(amount);
				bankAccountService.update(bankAccount);
				
				HttpSession session = req.getSession();
				
				
				modelMap.addAttribute("bankAccounts", customer.getBankAccounts());
				session.setAttribute("customer", customer);
				
				return "bankManagement";
			}
		}
		
		HttpSession session = req.getSession();
		
		modelMap.addAttribute("depositAccountDoesNotExist", "Account does not exist, please enter another account name");
		modelMap.addAttribute("bankAccounts", customer.getBankAccounts());
		session.setAttribute("customer", customer);
		
		return "bankManagement";
	}
	
	@PostMapping("/withdraw")
	public String toWithdraw(ModelMap modelMap, HttpServletRequest req, Customer customer, HttpSession session) {
		customer = (Customer) req.getSession().getAttribute("customer");
		String bankAccountName = req.getParameter("bankAccountName");
		
		BigDecimal amount = new BigDecimal(req.getParameter("withdrawAmount"));
		
		for(BankAccount bankAccount : customer.getBankAccounts()) {
			if(bankAccount.getBankAccountName().equals(bankAccountName)) {
				try {
					bankAccount.withdraw(amount);
					bankAccountService.update(bankAccount);
					session = req.getSession();
					modelMap.addAttribute("bankAccounts", customer.getBankAccounts());
					session.setAttribute("customer", customer);
					return "bankManagement";
				} catch (InsufficientFundsException e) {
					modelMap.addAttribute("bankAccounts", customer.getBankAccounts());
					modelMap.addAttribute("insufficientFunds", "Insufficient funds, please enter another amount");
					session = req.getSession();
					session.setAttribute("customer", customer);
					return "bankManagement";
				}
			}
			
		}
		
		session = req.getSession();
		modelMap.addAttribute("withdrawAccountDoesNotExist", "Account does not exist, please enter another account name");
		modelMap.addAttribute("bankAccounts", customer.getBankAccounts());
		session.setAttribute("customer", customer);
		
		return "bankManagement";
		
		
	}
	
	@RequestMapping("/addBankAccount")
	public String toAddBankAccount(ModelMap modelMap, HttpServletRequest req, Customer customer, HttpSession session) {
		customer = (Customer) req.getSession().getAttribute("customer");
		session = req.getSession();
		modelMap.addAttribute("customer", customer);
		session.setAttribute("customer", customer);
		return "addBankAccount";
	}
	
	@PostMapping("/confirmAddedAccount")
	public String toConfirmAddedAccount(ModelMap modelMap, HttpServletRequest req, Customer customer, HttpSession session) {
		BankAccount bankAccount = null;
		customer = (Customer) req.getSession().getAttribute("customer");
		String bankAccountChoice = req.getParameter("bankAccount");
		if(bankAccountChoice.equals("Checking Account")) {
			bankAccount = context.getBean("checkingAccount", CheckingAccount.class);
			bankAccount.setType(bankAccountChoice);
			bankAccount.setBankAccountName(req.getParameter("bankAccountName"));
		} else if(bankAccountChoice.equals("Savings Account")) {
			bankAccount = context.getBean("savingsAccount", SavingsAccount.class);
			bankAccount.setType(bankAccountChoice);
			bankAccount.setBankAccountName(req.getParameter("bankAccountName"));
		}
		
		customer.getBankAccounts().add(bankAccount);
		customerService.update(customer);
		
		bankAccount.setCustomer(customerService.find(customer.getCustomerId()));
		bankAccountService.save(bankAccount);
		session = req.getSession();
		modelMap.addAttribute("customer", customer);
		modelMap.addAttribute("bankAccount", bankAccount);
		session.setAttribute("customer", customer);
		
		return "confirmAddedAccount";
	}
	
	@RequestMapping("/logout")
	public String toLogout(SessionStatus status) {
		status.setComplete();
		return "redirect:/";
		
	}
	

}
