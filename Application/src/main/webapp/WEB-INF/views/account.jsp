<%@page import="java.util.stream.Collectors"%>
<%@page import="java.util.stream.Collector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="controller.*,entity.*,java.util.*,com.azure.storage.blob.models.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Account</title>
</head>
<body>
	<h1> Ciao</h1>
	
	<table>
	<%
		final HashMap<BlobItem,String> blobs = (HashMap<BlobItem,String>)session.getAttribute("Files");
		if(blobs==null)
			System.out.println("Blobs è null\n");
		Iterator<BlobItem> i=blobs.keySet().stream().iterator();
		while(i.hasNext()){
			BlobItem b=i.next();
			String key=blobs.get(b);
			%>
			<tr>
				<td><a href="<%=key%>"><%=b.getName()%></a> </td>
			</tr>
			<%
		}
	%>
	</table>
</body>
</html>