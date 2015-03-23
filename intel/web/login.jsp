
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    Object userObj = session.getAttribute("validUser");
    
    if(userObj != null) {
        response.sendRedirect("index.jsp");
    }
%>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">

        <title>Home Page</title>
        <link href="css/boostrap/bootstrap.min.css" rel="stylesheet">
        <link href="css/OurCSS/homepage.css" rel="stylesheet">
        <link href="css/OurCSS/login.css" rel="stylesheet">
    </head>

    <body>
        <div class="navigation-bar">
            <div class = "nav-header">
                <img id="header" src="media/logo.png">
            </div>
        </div>
        <div class="container">
            <div class="row">
                <div class="col-sm-6 col-md-4 col-md-offset-4">
                    <div class="account-wall">
                        <%
                            if(request.getAttribute("validate") == null) {

                            }
                            else if(request.getAttribute("validate").equals("false")){
                                out.print("<p id='form-info'>Invalid id or password</p>");
                            }
                            else {
                                out.print("<p id='form-info'>Please login again</p>");
                            }
                        %>
                        <form class="form-signin" action="userLogin" method="POST">
                        <input name="id" type="text" class="form-control" placeholder="user ID" required autofocus>
                        <input name="pw" type="password" class="form-control" placeholder="Password" required>
                        <button class="btn btn-lg btn-primary btn-block" type="submit">
                            Sign in</button>
                        </form>
                        <a href="#" class="text-center new-account">Create an account </a> 
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
