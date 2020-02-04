<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
    <!DOCTYPE html>
    <html>

    <head>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>

        <meta charset="ISO-8859-1">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Login AzureIAS</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/site.css" rel="stylesheet">
    </head>

    <body>
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-12">
                    <div class="page-header">
                        <img id="logoImageLogin" src="img/Logo-Microsoft-Azure.png" alt="logo" />
                        <!--  
					<h1 id="h1_title_login">
						Azure Cogito storage <small>Upload and ...</small>
					</h1>
-->
                    </div>
                </div>
                <div class="col-md-4">

                </div>
                <div class="col-md-4">
                    <h1 class="text-center">
					<strong>Sign In</strong>
				</h1>
                    <form method="post" action="/login" role="form">
                        <fieldset>
                            <div class="form-group">
                                <label for="exampleInputEmail1"> Email address </label>
                                <input type="email" placeholder="Email Address" class="form-control" id="exampleInputEmail1" name="mailAddress">
                                <div>
                                    <label for="exampleInputPassword"> Password </label>
                                    <input type="password" placeholder="Password" id="password" class="form-control" name="passwd" required>
                                </div>
                            </div>
                        </fieldset>
                        <button type="submit" id="buttonLoginx" class="btn btn-success btn-lg btn-block">Login</button>
                        <br>

                    </form> 
                    <form method="GET" action="/registration">
                        <button type="submit" id="buttonLoginx" class="btn btn-primary btn-lg btn-block">Sign up</button>
 					</form>
                </div>
                <div class="col-md-4">

                </div>
            </div>
            <br>
            <div class="row">

                <div class="col-md-4">
                    <img id="imageFooter" src="img/logo_standard.png" class="rounded-circle" />
                </div>
                <div class="col-md-4">
                </div>
                <div class="col-md-4" align="center">

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
    </body>

    </html>