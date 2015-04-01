<%-- 
    Document   : newjsp
    Created on : Mar 2, 2015, 11:27:27 PM
    Author     : leon
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Object userObj = session.getAttribute("validUser");
    if(userObj == null) {
        response.sendRedirect("login.jsp");
    }
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">

        <title>Result</title>

        <link href="css/boostrap/bootstrap.min.css" rel="stylesheet">
        <link href="css/OurCSS/homepage.css" rel="stylesheet">
        <link href="css/OurCSS/resultpage.css" rel="stylesheet">
    </head>

    <body>
        <div class="navigation-bar">
            <div class = "nav-header">
                <img id="header" src="media/logo.png">
            </div>
            <nav class="navbar navbar-default">
                <div class="container">
                    <div id="navbar">
                        <ul class="nav navbar-nav">
                            <li><a href="index.jsp">Main</a></li>
                            <li class="active"><a href="#">Result</a></li>
                        </ul>
                        <ul class="nav navbar-nav navbar-right">
                            <li>
                                <a id="user" href="">
                                <%
                                    String user = (String) userObj;
                                    out.print("Welcome " + user);
                                %>
                                </a>
                            <li>
                            <form id="logout_panel" class="navbar-form navbar-right" action="logout.jsp" method="post">
                                    <button id="logout_button" class="btn btn-success" type="submit" type="button" class="btn btn-default btn-lg" >
                                        Logout
                                    </button>
                            </form>
                        </ul>
                    </div>
                </div>
            </nav>
        </div>
        <div id="result_div">
            <div class="panel panel-default">
                <div class="panel-heading">Average</div>
                <div class="panel-body">
                    <%
                        if(request.getAttribute("error")!=null || request.getAttribute("average")==null){
                            out.print(request.getAttribute("userFolder") + ": ");
                            out.print("Empty");
                        }
                        else{
                            out.print(request.getAttribute("userFolder") + ": ");
                            out.print(request.getAttribute("average"));
                        }
                    %>
                </div>
            </div>
        </div>
    </body>
</html>
