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
      <title>Sign Up Page</title>
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
            <h1 class="text-center">Sign Up</h1>
            <form method="post" action="/registrazione" role="form">
               <fieldset>
                  <div class="form-group">
                     <div class="form-group">
                        <label for="exampleInputFirst_Name"> First Name </label> <input
                           type="text" class="form-control" id="exampleInputFirstName" name="firstName">
                     </div>
                     <div class="form-group">
                        <label for="exampleInputLast_Name"> Last Name </label> 
                        <input type="text" class="form-control" id="exampleInputLastName" name="lastName">
                     </div>
                     <label for="exampleInputEmail1"> Email address* </label> 
                     <input type="email" class="form-control" id="exampleInputEmail1" name="mailAddress">
                     <div>
                        <label for="exampleInputPassword"> Password* </label> 
                        <input type="password" placeholder="Password" id="password" class="form-control" name="passwd" required>
                     </div>
                     <div>
                        <label for="exampleInputPassword"> Confirm Password* </label> 
                        <input
                           type="password" placeholder="Confirm Password"
                           id="confirm_password" class="form-control" required>
                     </div>
                     </div class="form-group">
               </fieldset>
               <button type="submit" class="btn btn-primary">Sign up</button>
            </form>
            </div>
            <script src="js/check_password.js"></script>
            <div class="col-md-2"></div>
         </div>
      </div>
   </body>
</html>

