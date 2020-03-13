<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="css/bankManagement.css" />
<title>Bank Management</title>
</head>
<body>
	<header>
		<nav>
			<a href="addBankAccount">Add an Account</a>
			<a href="logout">Logout</a>
		</nav>
	</header>
	<h1>Logged in: ${ customer.username } </h1>
	
	<h3> <u> Accounts </u> </h3>
	
	<c:forEach items="${ bankAccounts }" var="bankAccount">
		<p> <b> Account Name: </b> ${ bankAccount.bankAccountName } <br> <b>Balance:</b> $ ${ bankAccount.balance } </p>
	</c:forEach>
	
	<h3> <u> Deposit </u></h3>
	
	<form action="deposit" method="post">
			<label>Enter Account Name: </label>
			<input type="text" name="bankAccountName" autocomplete="off" required>
			<label>Enter Amount for Deposit: </label>
			<input type="number" name="depositAmount" step="any" min="0" autocomplete="off" required>
			<input type="submit" value="Deposit">
	</form>
	<p class="accountDoesNotExist"> ${ depositAccountDoesNotExist }</p>
	
	<br>
	
	<h3> <u> Withdraw </u> </h3>
	
	<form action ="withdraw" method="post">
		<label>Enter Account Name: </label>
		<input type="text" name="bankAccountName" autocomplete="off" required>
		<label>Enter Amount for Withdrawal: </label>
		<input type="number" name="withdrawAmount" step="any" min="0" autocomplete="off" required>
		<input type="submit" value="Withdraw">
	</form>
	<p class="accountDoesNotExist"> ${ withdrawAccountDoesNotExist }</p>
	<p class="insufficientFunds"> ${ insufficientFunds } </p>
	
</body>
</html>