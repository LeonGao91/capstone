
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    //
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

        <title>Home Page</title>

        <link href="css/boostrap/bootstrap.min.css" rel="stylesheet">
        <link href="css/OurCSS/homepage.css" rel="stylesheet">
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
                            <li class="active"><a href="">Main</a></li>
                            <li><a href="result.jsp">Result</a></li>
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
        <div id="records"> 
            <table class="table">
                <thead>
                    <tr>
                        <th>No.</th>
                        <th>Product <span class="glyphicon glyphicon-collapse-down"></span></th>
                        <th>Date <span class="glyphicon glyphicon-collapse-down"></span></th>
                        <th>Result <span class="glyphicon glyphicon-collapse-down"></span></th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <th scope="row">1</th>
                        <td>DELL XPS 13</td>
                        <td>04/07/2015</td>
                        <td>PASS</td>
                        <td><a href="result.jsp"><button type="button" class="btn btn-primary">View</button></a></td>
                    </tr>
                    <tr>
                        <th scope="row">2</th>
                        <td>DELL XPS 13</td>
                        <td>04/06/2015</td>
                        <td>FAIL</td>
                        <td><a href="result.jsp"><button type="button" class="btn btn-primary">View</button></a></td>
                    </tr>
                    <tr>
                        <th scope="row">3</th>
                        <td>DELL XPS 13</td>
                        <td>04/05/2015</td>
                        <td>PASS</td>
                        <td><a href="result.jsp"><button type="button" class="btn btn-primary">View</button></a></td>
                    </tr>
                    <tr>
                        <th scope="row">4</th>
                        <td>xDELL XPS 13</td>
                        <td>04/04/2015</td>
                        <td>PASS</td>
                        <td><a href="result.jsp"><button type="button" class="btn btn-primary">View</button></a></td>
                    </tr>
                    <tr>
                        <th scope="row">5</th>
                        <td>DELL XPS 13</td>
                        <td>03/30/2015</td>
                        <td>PASS</td>
                        <td><a href="result.jsp"><button type="button" class="btn btn-primary">View</button></a></td>
                    </tr>
                </tbody>
            </table>
 	</div>
 
    </body>
</html>
