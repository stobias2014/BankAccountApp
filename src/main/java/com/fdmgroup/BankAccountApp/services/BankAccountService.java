package com.fdmgroup.BankAccountApp.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.BankAccountApp.pojos.BankAccount;
import com.fdmgroup.BankAccountApp.pojos.Customer;

@Service
public class BankAccountService {
	@Autowired
	private BankAccountRepo bankAccountRepo;

	public void save(BankAccount bankAccount) {
		bankAccountRepo.save(bankAccount);
	}

	public BankAccount find(Long bankAccountId) {
		Optional<BankAccount> bankAccount = bankAccountRepo.findById(bankAccountId);
		if(bankAccount.isPresent()) {
			return bankAccount.get();
		}
		return null;
	}
	
	public BankAccount findByAccountNameAndCustomerId(String bankAccountName, Customer customer) {
		
		Optional<BankAccount> bankAccount = bankAccountRepo.findByAccountNameAndCustomerId(bankAccountName, customer);
		if(bankAccount.isPresent()) {
			return bankAccount.get();
		}
		
		return null;
	}
	
	public void update(BankAccount bankAccount) {
		if(bankAccountRepo.existsById(bankAccount.getBankAccountId())) {
			bankAccountRepo.save(bankAccount);
		} else {
			// make updates to log instead
			// add exception for bank account not found
			System.out.println("Bank Account does not exist");
		}
	}
	
}
