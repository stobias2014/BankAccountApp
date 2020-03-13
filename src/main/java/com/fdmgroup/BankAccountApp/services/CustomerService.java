package com.fdmgroup.BankAccountApp.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fdmgroup.BankAccountApp.exceptions.DuplicateUsernameException;
import com.fdmgroup.BankAccountApp.pojos.Customer;

@Service
public class CustomerService {
	
	@Autowired
	CustomerRepository customerRepo;

	public void save(Customer customer) throws DuplicateUsernameException {
		try {
		customerRepo.save(customer);
		} catch (DataIntegrityViolationException dive) {
			throw new DuplicateUsernameException(); 
		}
		
	}

	public Customer find(Long customerId) {
		Optional<Customer> customer = customerRepo.findById(customerId);
		if(customer.isPresent()) {
			return customer.get();
		}
		return null;
	}
	
	public Customer findByUsername(String username) {
		Optional<Customer> customer = customerRepo.findByUsername(username);
		if(customer.isPresent()) {
			return customer.get(); 
		} else {
			throw new UsernameNotFoundException("Username not found");
		}
	}

	public void update(Customer customer) {
		if(customerRepo.existsById(customer.getCustomerId())) {
			customerRepo.save(customer);
		} else {
			// make updates to log instead
			// add exception for customer not found
			System.out.println("Customer does not exist.");
		}
		
	}

}
