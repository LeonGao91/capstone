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
        <script src="js/library/jquery-1.11.2.js"></script>
        <script src="js/library/bootstrap.min.js"></script>
        <script src="js/library/justGauge/justgage.1.0.1.min.js"></script>
        <script src="js/library/justGauge/raphael.2.1.0.min.js"></script>
        <script type="text/javascript" src="https://www.google.com/jsapi"></script>
        <script src="js/OurJS/lineChart.js"></script>
        <script src="js/library/eye/Chart.js"></script>
        <script src="js/OurJS/eyeChart.js"></script>
        
    </head>

    <body>
        <div class="navigation-bar">
            <div class = "nav-header">
                <img id="header" src="media/logo.png">
                <h2 id="head-title">INTELligence</h2>
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
        <!--
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
        -->
        <div id="title-wraper">
                <button id="title" type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                    <h3 id="title_head">DELL XPS 13 - 04/07/2015 <span class="caret"></span> </h3>
                </button>
                <ul id="test-menu" class="dropdown-menu" role="menu">
                    <li><a href="#"><h4>DELL XPS 13 - 04/08/2015  </h4></a></li>
                    <li><a href="#"><h4>DELL XPS 13 - 04/09/2015  </h4></a></li>
                    <li><a href="#"><h4>DELL XPS 13 - 04/10/2015  </h4></a></li>
                </ul>
                <span id="passfail" class="label label-success" style="font-size:15px">PASS!</span>
        </div>
        
        <div class="panel panel-primary main_panel" style="margin-top: 2%">
            <div class="panel-heading">
                <h3 class="panel-title" align="center">Scores</h3>
            </div>
            <div id="trust" class="gauge"></div>
            <div id="health" class="gauge"></div>
            <script>
                var g = new JustGage({
                  id: "trust",
                  value: 32,
                  min: 0,
                  max: 100,
                  title: "Trust",
                  label: "SCORE",
                  levelColors: ["D00000","00CC33"],
                  startAnimationTime : 2000
                });
                var g2 = new JustGage({
                  id: "health",
                  value: 91,
                  min: 0,
                  max: 100,
                  title: "Health",
                  label: "SCORE",
                  levelColors: ["D00000","00CC33"],
                  startAnimationTime : 2000
                });
            </script>
        </div>
        <div class="panel panel-danger main_panel">
            <div class="panel-heading">
                <h3 class="panel-title" align="center">Messages</h3>
            </div>
            <div class="panel-body">Trust score is too low</div>
        </div>
        <div class="panel panel-primary main_panel">
            <div class="panel-heading">
                <button id="chart-title" type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                <h3 id="chart_header" class="panel-title" align="center">Eye Diagram <span class="caret"></span></h3>
                </button>
                <ul id="chart-menu" class="dropdown-menu" role="menu">
                    <li><a href="#"><h4>Increment Chart  </h4></a></li>
                    <li><a href="#"><h4>Eye Diagram  </h4></a></li>
                </ul>
            </div>
            <!--<div id="linechart_material"></div>-->
            <br>
            <br>
            <canvas id="eyeChartRead" width="600" height="350"></canvas>
            <canvas id="eyeChartWrite" width="600" height="350"></canvas>
            <script>
                var ctx1 = document.getElementById("eyeChartRead").getContext("2d");
                var ctx2 = document.getElementById("eyeChartWrite").getContext("2d");
                var data1 = {
    labels: ["Voltage", "Timing", "Voltage", "Timing"],
    datasets: [
        {
            label: "Average",
            fillColor: "rgba(51, 122, 183,0.3)",
            strokeColor: "rgba(51, 122, 183,1)",
            pointColor: "rgba(51, 122, 183,1)",
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(51, 122, 183,1)",
            data: [11,8,16,9]
        },
        {
            label: "Minimum",
            fillColor: "rgba(237,29,65,0.2)",
            strokeColor: "rgba(237,29,65,0.6)",
            pointColor: "rgba(237,29,65,0.6)",
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(237,29,65,0.6)",
            data: [1,1,5,4]
        }
    ]
};
    var data2 = {
    labels: ["Voltage", "Timing", "Voltage", "Timing"],
    datasets: [
        {
            label: "Average",
            fillColor: "rgba(51, 122, 183,0.3)",
            strokeColor: "rgba(51, 122, 183,1)",
            pointColor: "rgba(51, 122, 183,1)",
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(51, 122, 183,1)",
            data: [9,10,12,11]
        },
        {
            label: "Minimum",
            fillColor: "rgba(237,29,65,0.2)",
            strokeColor: "rgba(237,29,65,0.6)",
            pointColor: "rgba(237,29,65,0.6)",
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(237,29,65,0.6)",
            data: [5,3,2,6]
        }
    ]
};
    var option = {
    

    //Number - Point label font size in pixels
    pointLabelFontSize : 15,

    
};
var read = new Chart(ctx1).Radar(data1,option);
var write = new Chart(ctx2).Radar(data2,option);
            </script>
        </div>
    </body>
</html>
