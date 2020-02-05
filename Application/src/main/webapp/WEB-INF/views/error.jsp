<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Error</title>
</head>
<body>
	<% 	Exception e = (((Exception)request.getSession().getAttribute("exception")));
		StackTraceElement[] stackTrace = e.getStackTrace(); %>
	<h2>Exception occurred on server</h2>
	<p>Stack Trace: </p>
	<p> <%= e.getMessage() %><br>
	<% for(StackTraceElement st: stackTrace){
		%>
		<%=st.toString()%><br>
	<% } %>
	</p>
	</body>
</html>