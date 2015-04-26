<%-- 
    Document   : newjsp
    Created on : Mar 2, 2015, 11:27:27 PM
    Author     : leon
--%>

<%@page import="dataAnalyticsModel.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    Object userObj = session.getAttribute("validUser");
    if(userObj == null) {
        response.sendRedirect("login.jsp");
    }
    Object o = session.getAttribute("detail");
    Object o2 = session.getAttribute("currentProduct");
    Object o3 = session.getAttribute("currentTest");
    TestDetail detail = new TestDetail();
    TestProduct product = new TestProduct();
    TestBrief brief = new TestBrief();
    if(o == null) {
        response.sendRedirect("index.jsp");
    }
    else {
        detail = (TestDetail) o;
    }
    if(o2 == null) {
        response.sendRedirect("index.jsp");
    }
    else {
        product = (TestProduct) o2;
    }
    if(o3 == null) {
        response.sendRedirect("index.jsp");
    }
    else {
        brief = (TestBrief) o3;
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
        <script src="js/OurJS/result.js"></script>
        <!--combination chart-->
        <script src="js/library/Chart.Scatter.js"></script>
        
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
        <div id="title-wraper">
                <button id="title" type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                    <h3 id="title_head">
                        <%  
                            String name = (String) session.getAttribute("productName");
                            out.print(name);
                        %>
                    <span class="caret"></span> </h3>
                </button>
                <ul id="test-menu" class="dropdown-menu" role="menu">
                    <li><a href="#"><h4>DELL XPS 13 - 04/08/2015  </h4></a></li>
                    <li><a href="#"><h4>DELL XPS 13 - 04/09/2015  </h4></a></li>
                    <li><a href="#"><h4>DELL XPS 13 - 04/10/2015  </h4></a></li>
                </ul>
                    <%
                        if(brief.getPass_fail().equals("pass")){
                            out.print("<span id='passfail' class='label label-success' style='font-size:15px'>PASS!</span>");
                        }
                        else {
                            out.print("<span id='passfail' class='label label-danger' style='font-size:15px'>FAIL!</span>");
                        }
                    %>
               
                <span class="label label-warning basic" style="margin-right: 10%">
                    3 REPEATS</span>
                <span class="label label-info basic" style="margin-right: 1%">5 SYSTEMS</span>
        </div>


            
            

        
        <div class="panel panel-primary main_panel" style="margin-top: 2%">
            <div class="panel-heading">
                <h3 class="panel-title" align="center">Scores</h3>
            </div>
            <div class="row">
                <div id="trust" class="gauge"></div>
                <div id="health" class="gauge"></div>
            </div>
             <div class="row">
                <div class="col-md-6 center-block">
                    <!-- pop up window -->
                   <button type="button" class="btn btn-primary center-block view-item" data-toggle="modal" data-target="#trustDetail">View</button>

                        <div id="trustDetail" class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
                            <div class="modal-dialog modal-lg">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                                        <h4 class="modal-title" id="myLargeModalLabel">Trust detail</h4>
                                    </div>
                                    <div class="modal-body">

                                        <table class="table pop-up-table">
                                            <thead>
                                                <tr>
                                                    <th>Item</th>
                                                    <th>Score</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="item" items="${sessionScope.detail.getTrust_detail_keySet()}">
                                                    <tr>
                                                    <td><c:out value="${item}"/></td>
                                                    <td><c:out value="${sessionScope.detail.getTrust_detail().get(item)}"/></td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>

                                    </div>
                                </div>
                            </div>
                        </div>
                </div>
                
                 <div class="col-md-6  text-center">
                    <button type="button" class="btn btn-primary center-block view-item" data-toggle="modal" data-target="#healthDetail">View</button>
                        <div id="healthDetail" class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
                            <div class="modal-dialog modal-lg">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                                        <h4 class="modal-title" id="myLargeModalLabel">Health detail</h4>
                                    </div>
                                    <div class="modal-body">

                                        <table class="table pop-up-table">
                                            <thead>
                                                <tr>
                                                    <th>Item</th>
                                                    <th>Score</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="item" items="${sessionScope.detail.getHealth_detail_keySet()}">
                                                    <tr>
                                                    <td><c:out value="${item}"/></td>
                                                    <td><c:out value="${sessionScope.detail.getHealth_detail().get(item)}"/></td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>

                                    </div>
                                </div>
                            </div>
                        </div>
                </div>
            </div>
        </div>
        
        <div class="panel panel-danger main_panel">
            <div class="panel-heading">
                <h3 class="panel-title" align="center">Messages</h3>
            </div>
            <div class="panel-body">
                ${sessionScope.detail.getMessages()}
            </div>
        </div>
        
        <div class="panel panel-primary main_panel">
            <div class="panel-heading">
                <button id="chart-title" type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                <h3 id="chart_header" class="panel-title" align="center">Health/Trust Chart <span class="caret"></span></h3>
                </button>
                <ul id="chart-menu" class="dropdown-menu" role="menu">
                    <li id="changeEye"><a><h4>Eye Chart  </h4></a></li>
                    <li id="changeCombination"><a><h4>Combination Chart </h4></a></li>
                    <li id="changeIncrement" style="display:none"><a><h4>Health/Trust Chart </h4></a></li>
                    
                </ul>
            </div>
            
            <div id="charts">
            <div id="linechart_material"></div>

            <!--<div class="row" id="eyeChart">
                <div class="col-md-6 center-block">
                    <div class="eyeTitle">Read</div>
                        <canvas id="eyeChartRead" class="center-block"></canvas>    
                    </div>
                    <div class="col-md-6 center-block">
                        <div class="eyeTitle">Write</div>
                        <canvas id="eyeChartWrite" class="center-block"></canvas>
                    </div>
            </div>

            
            <div id="combination">
                <canvas id="combinationChart"></canvas>
            </div>-->
            
            </div><!--charts-->
        </div>
    </body>
    <script>
    $(document).ready(function() {
        var g = new JustGage({
        id: "trust",
        value: ${sessionScope.detail.getTrust()},
        min: 0,
        max: 100,
        title: "Trust",
        label: "SCORE",
        levelColors: ["D00000","00CC33"],
        startAnimationTime : 2000
      });
        var g2 = new JustGage({
          id: "health",
          value: ${sessionScope.detail.getHealth()},
          min: 0,
          max: 100,
          title: "Health",
          label: "SCORE",
          levelColors: ["D00000","00CC33"],
          startAnimationTime : 2000
        });
    });
    </script>
</html>
