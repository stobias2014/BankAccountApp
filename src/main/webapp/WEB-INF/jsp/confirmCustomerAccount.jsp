<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Confirm Customer Account</title>
</head>
<body>
	<header>
		<nav>
			<a href="/">Home</a>
		</nav>
	</header>
	<h1>Successful Registration for ${ customer.firstname } ${ customer.lastname } with a ${bankAccount.type}</h1>
	
	<h3>${ customer.email }</h3>
	<h3>${ customer.username }</h3>
	
	<a href="login">Log In</a>
</body>
</html>