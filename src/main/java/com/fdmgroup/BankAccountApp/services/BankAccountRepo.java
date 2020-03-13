package com.fdmgroup.BankAccountApp.services;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fdmgroup.BankAccountApp.pojos.BankAccount;
import com.fdmgroup.BankAccountApp.pojos.Customer;

public interface BankAccountRepo extends JpaRepository<BankAccount, Long> {
	
	@Query("SELECT b FROM BankAccount b WHERE b.bankAccountName = ?1 AND b.customer = ?2")
	Optional<BankAccount> findByAccountNameAndCustomerId(String bankAccountName, Customer customer);

}
