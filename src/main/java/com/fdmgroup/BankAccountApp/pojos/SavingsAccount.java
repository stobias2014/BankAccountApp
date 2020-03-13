package com.fdmgroup.BankAccountApp.pojos;

import javax.persistence.Entity;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Entity
@Scope("prototype")
public class SavingsAccount extends BankAccount {
	
	public SavingsAccount() {}

}
