package com.fdmgroup.BankAccountApp.validator;

public class Validator {
	
	public static String validatePassword(String password, String confirmedPassword) {
		String noNumbers = password.replaceAll("[0-9]", "");
		String noUpperCase = password.replaceAll("[A-Z]", "");
		String noLowerCase = password.replaceAll("[a-z]", "");
		String noSpecial = password.replaceAll("\\s", "").replaceAll("[+=']", "");
		if (password.length() < 5 || password.length() > 30) {
			return "Password must be between 5 and 30 characters";
		} else if(noNumbers.length() == password.length() || noUpperCase.length() == password.length() || noLowerCase.length() == password.length()){
			return "Password must contain an upper case letter, a lower case letter, and a number";
		} else if(noSpecial.length() != password.length()){
			return "Password cannot contain ' ', +, =, or '";
		} else if(password.equals(confirmedPassword)){
			return null;
		} else {
			return "Passwords do not match";
		}
	}


}
