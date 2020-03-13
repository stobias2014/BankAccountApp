<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="css/register.css" />
<title>Register Customer</title>
</head>
<body>
	<header>
		<nav>
			<a href="/">Home</a>
		</nav>
	</header>
	<h1>Registration</h1>
	<sf:form action="confirmCustomerAccount" method="post" modelAttribute="customer">
	<sf:label path="firstname">First Name: </sf:label> <br>
	<sf:input path="firstname" autocomplete="off" required="required" /> <br>
	<sf:label path="lastname">Last Name: </sf:label> <br>
	<sf:input path="lastname" autocomplete="off" required="required" /> <br>
	<sf:label path="username">Username: </sf:label> <br>
	<sf:input path="username" autocomplete="off" required="required" /> <br>
	<p class="badCreds"> ${ userNameAlreadyExists } </p>
	<sf:label path="email">Email: </sf:label> <br>
	<sf:input path="email" type="email" autocomplete="off" required="required" /> <br>
	<sf:label path="password">Password: </sf:label> <br>
	<sf:input path="password" type ="password" required="required" /> <br>
	<p class="badRegister"> ${ badRegister }</p>
	<label>Confirm Password: </label> <br>
	<input type="password" name="confirmPassword" required="required"> <br> <br>
	<select name = "bankaccount">
		<option value ="Checking Account">Checking Account</option>
		<option value ="Savings Account">Savings Account</option>
	</select>
	<br> <br>
	<input type = "submit" value="Register">
	</sf:form>
</body>
</html>