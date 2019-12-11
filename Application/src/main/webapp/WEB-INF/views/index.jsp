<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
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

<title>Sign In Page</title>

<meta name="description"
	content="Source code generated using layoutit.com">
<meta name="author" content="LayoutIt!">

<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet">
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-2"></div>
			<div class="col-md-8">
				<h1 class="text-center">Sign In</h1>
				
				
				<form method="post" action="/login" role="form">
					<fieldset>
						<div class="form-group">

								<label for="exampleInputEmail1"> Email address* </label> 
								<input type="email" class="form-control" id="exampleInputEmail1" name="mailAddress">
						<div>
							<label for="exampleInputPassword"> Password* </label> 
							<input type="password" placeholder="Password" id="password" class="form-control" name="passwd" required>
						</div>

								
						</div class="form-group">



						
					</fieldset>
					<button type="submit" class="btn btn-primary">Login</button>
				</form>
				
				
			</div>
			<div class="col-md-2"></div>
		</div>
	</div>
</body>
</html>