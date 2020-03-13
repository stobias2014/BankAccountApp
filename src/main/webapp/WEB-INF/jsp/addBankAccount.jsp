<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add Bank Account</title>
</head>
<body>
	<header>
		<nav>
			<a href="home">My Home Page</a>
			<a href="logout">Log Out</a>
		</nav>
	</header>
	<h1>Add a New Bank Account</h1>
	<form action="confirmAddedAccount" method="post">
		<label>Enter Account Name: </label>
		<input type="text" name ="bankAccountName" autocomplete="off" required>
		<select name="bankAccount">
			<option value="Checking Account">Checking Account</option>
			<option value="Savings Account">Savings Account</option>
		</select>
		<input type="submit" value="Add Account">
	</form>
</body>
</html>