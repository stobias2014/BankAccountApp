package com.fdmgroup.BankAccountApp.pojos;

import javax.persistence.Entity;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Entity
@Component
@Scope("prototype")
public class CheckingAccount extends BankAccount {
	
	public CheckingAccount() {}

}
