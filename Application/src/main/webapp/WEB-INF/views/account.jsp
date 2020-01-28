<%@page import="entity.BlobItemKeyStruct"%>
<%@page import="java.util.stream.Collectors"%>
<%@page import="java.util.stream.Collector"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"
	import="java.util.*,com.azure.storage.blob.models.*"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
	integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO"
	crossorigin="anonymous">
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"
	integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy"
	crossorigin="anonymous"></script>

<meta charset="ISO-8859-1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Account</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/site.css" rel="stylesheet">


<script type="text/javascript" src="js/functions.js"></script>

</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<h3>Il tuo account</h3>
				
				 <button onclick="isDirectory()">Try it</button> 
				
				<h5 id="message">da eliminare</h5>
				
				
			</div>
		</div>
		<div class="row">
			<div class="col-md-8">
				<table  class="table table-hover">
					<thead class="thead-dark">
						<tr>
							<th>Name</th>
							<th>Tag</th>
							<th>Action</th>
						</tr>
					</thead>
					<tbody>
						<%
							final List<BlobItemKeyStruct> blobs = (List<BlobItemKeyStruct>) session.getAttribute("Files");
							for (BlobItemKeyStruct b : blobs) {
								String key = b.getKey();
					
						%>
						<tr id="tableDataUser">
						
							<td style="width: 50%"><a href="<%=key%>"><label><%=b.getTrueName()%></label></a></td>
							
							<%
							try{	
								String tag = b.getItem().getMetadata().get("Tags").replaceAll("(?!\\s)\\W", "$0 ");
								if(tag!=null){ %>
							<td style="width: 30%">
							<span class="ellipsis" id="indentTags"><%=tag%></span>
							</td>
							<% } 
							}catch(Exception e){
								
							}%>
							 <td style="width: 20%"> 
							<a href="test"> <img id="deleteFile" src="img/trash.svg" alt="delete"/> </a> 
							<a href="test"> <img id="renameFile" src="img/renameFile.png" alt="rename"/> </a>
							<a href="test"> <img id="changeDirectory" src="img/changeDirectory.svg" alt="changeDir"/> </a>
							</td>
						</tr>
						<%
							}
						%>
						
					</tbody>
				</table>
			</div>
			<div class="col-md-4">
			<a href="/">
					<button type="submit" id="buttonLogOut"
						class="btn btn-danger btn-lg btn-block">Logout</button>
			</a>
			
				<div class="form-group">
					<form role="form" method="post" action="/account/search">
						<label for="InputSearch">Search:</label> <input type="text"
							class="form-control" name="query"/>
							
							<a href="test"> <img id="accountHome" src="img/homeAccountButton.svg" alt="accountHome"/> </a>
					</form>

				</div>
<!--  			
<div>
<form method="post" action="/account/search">Search: 
<input type="text" name="query">
</form>
</div>
-->


					<form role="form" method="post" id="createDirForm">
						<label for="InputSearch">New directory name:</label> <input
							type="text" name="dirName" class="form-control"> <input
							type="submit" class="form-control" value="Create"
							id="createDirSubmit" />
					</form>


<!--  			
<form method="post" id="createDirForm">Directory name: 
<input type="text" name="dirName"> 
<input type="submit" value="Crea cartella" id="createDirSubmit">
</form>
-->


					<form role="form" method="POST" enctype="multipart/form-data"
						id="fileUploadForm">
						<label for="InputSearch">Choose files to upload:</label> <input
							type="file" name="files" class="form-control"> <input
							type="submit" class="form-control" value="Upload" id="btnSubmit" />
					</form>


<!--		
				<form method="POST" enctype="multipart/form-data"
					id="fileUploadForm">
					<input type="text" name="extraField" /><br /> <br /> <input
						type="file" name="files" /><br /> <br /> <input type="submit"
						value="Submit" id="btnSubmit" />
				</form>
-->
				<h1>Ajax Post Result</h1>
				<pre>
    <label id="result"></label>
						</div>
			
			
			
			</div>
		</div>
		<div class="row">
			<div class="col-md-4">
				<img id="imageFooter" src="img/logo_standard.png"
					class="rounded-circle" />
			</div>
			<div class="col-md-4"></div>
			<div class="col-md-4">
				<dl>
					<dt>Professore:</dt>
					<dd>Vittorio Scarano</dd>
					<dt>Realizzato da:</dt>
					<dd>Luigi Crisci</dd>
					<dd>Giuseppe Di Palma</dd>
				</dl>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="webjars/jquery/2.2.4/jquery.min.js"></script>

	<script type="text/javascript" src="js/main.js"></script>
</body>
</html>

