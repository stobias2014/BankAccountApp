package com.fdmgroup.BankAccountApp.pojos;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fdmgroup.BankAccountApp.exceptions.InsufficientFundsException;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Component
@Scope("prototype")
public class BankAccount {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_account_id_gen")
	@SequenceGenerator(name = "bank_account_id_gen", sequenceName = "bank_account_id_seq")
	@Column(name = "bank_account_id", nullable = false)
	private Long bankAccountId;
	@NotNull
	private BigDecimal balance;
	@Column(name = "bank_account_name")
	@NotNull
	private String bankAccountName;
	@NotNull
	private String type;
	@ManyToOne
	@JoinColumn(name = "customer_fk_id")
	private Customer customer;
	
	public BankAccount() {
		this.balance = new BigDecimal(0.00);
	}
	
	public Long getBankAccountId() {
		return bankAccountId;
	}
	public void setBankAccountId(Long bankAccountId) {
		this.bankAccountId = bankAccountId;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	public String getBankAccountName() {
		return bankAccountName;
	}

	public void setBankAccountName(String bankAccountName) {
		this.bankAccountName = bankAccountName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public void deposit(BigDecimal amount) {
		this.balance = this.balance.add(amount);
	}
	
	public void withdraw(BigDecimal amount) throws InsufficientFundsException {
		if (this.balance.compareTo(amount) == -1) {
			throw new InsufficientFundsException();
		} else {
			this.balance = this.balance.subtract(amount);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((balance == null) ? 0 : balance.hashCode());
		result = prime * result + ((bankAccountId == null) ? 0 : bankAccountId.hashCode());
		result = prime * result + ((bankAccountName == null) ? 0 : bankAccountName.hashCode());
		result = prime * result + ((customer == null) ? 0 : customer.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BankAccount other = (BankAccount) obj;
		if (balance == null) {
			if (other.balance != null)
				return false;
		} else if (!balance.equals(other.balance))
			return false;
		if (bankAccountId == null) {
			if (other.bankAccountId != null)
				return false;
		} else if (!bankAccountId.equals(other.bankAccountId))
			return false;
		if (bankAccountName == null) {
			if (other.bankAccountName != null)
				return false;
		} else if (!bankAccountName.equals(other.bankAccountName))
			return false;
		if (customer == null) {
			if (other.customer != null)
				return false;
		} else if (!customer.equals(other.customer))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
}
