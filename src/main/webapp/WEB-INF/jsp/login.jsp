<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="css/register.css" />
<title>Login</title>
</head>
<body>
	<header>
		<nav>
			<a href="/">Home</a>
		</nav>
	</header>
	<h1>Log In</h1>
	<sf:form action="bankManagement" method="post" modelAttribute="customer">
		<sf:label path="username">Username: </sf:label> <br>
		<sf:input path="username" autocomplete="off" required="required"/> <br>
		<p class="badCreds"> ${ badUsername } </p>
		<sf:label path="password">Password</sf:label> <br>
		<sf:input path="password" type="password" autocomplete="off" required="required"/> <br>
		<p class="badCreds"> ${ badPassword } </p>
		<input type="submit" value="Log In">
	</sf:form>
	<br>
	<form action="register" method="get">
		<input type="submit" value="Register">
	</form>
	
</body>
</html>