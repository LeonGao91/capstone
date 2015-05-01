<%-- 
    Document   : newjsp
    Created on : Mar 2, 2015, 11:27:27 PM
    Author     : leon
--%>

<%@page import="java.util.Arrays"%>
<%@page import="java.util.LinkedList"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
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
    <script>
        google.load('visualization', '1.1', {packages: ['line']});
        google.setOnLoadCallback(drawChart);

        function drawChart() {

            var data = new google.visualization.DataTable();
            data.addColumn('string', 'Date');
            data.addColumn('number', 'Health');
            data.addColumn('number', 'Trust');

            data.addRows([
                ${sessionScope.currentProduct.getTimeSeriesData()}
            ]);

            var options = {
                chart: {
                },
                width: $('.main_panel').width() * 0.9,
                height: 300
            };

            var chart = new google.charts.Line(document.getElementById('linechart_material'));

            chart.draw(data, options);
        }
    </script>
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
                            SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");
                            Date d = date.parse(brief.getTest_date());
                            date.applyPattern("HH:mm:ss - MM/dd/yyyy");
                            out.print(name + " - " + date.format(d));
                        %>
                    <span class="caret"></span> </h3>
                </button>
                <ul id="test-menu" class="dropdown-menu" role="menu">
                    <% 
                        LinkedList<TestBrief> tests = product.getTests();
                        for(int i = 0; i < tests.size(); i++) {
                            TestBrief test = tests.get(i);
                            date = new SimpleDateFormat("yyyyMMddHHmmss");
                            d = date.parse(test.getTest_date());
                            date.applyPattern("HH:mm:ss - MM/dd/yyyy");
                            out.print("<li><form action='Detail?file=" 
                                + test.getResult_file()
                                + "&product=" + name
                                + "&test=" + i
                                + "' method='post'><button class='list-button' type='submit'><h4>" + date.format(d) + "</h4></button></form></li>");
                        }
                    %>
                </ul>
                    <%
                        if(brief.getPass_fail().equals("pass")){
                            out.print("<span id='passfail' class='label label-success' style='font-size:15px'>PASS!</span>");
                        }
                        else {
                            out.print("<span id='passfail' class='label label-danger' style='font-size:15px'>FAIL!</span>");
                        }
                    %>
        </div>
        
        <!-- newly added block
        <div class="panel panel-primary main_panel" style="margin-top: 2%">
            <div class="panel-heading">
                <h3 class="panel-title" align="center">Basic Information</h3>
            </div>

             <div class="row">
             </div>
        </div>
        -->
        <!-- newly added block ended-->

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
                <c:forEach var="line" items="${sessionScope.detail.getMessages()}">
                    ${line}<br>
                </c:forEach>
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
        	
	//read and write eye chart
	function drawEye(){
		var ctx1 = document.getElementById("eyeChart").getContext("2d");
	

                var data1 = {
                labels: ["TxVoltage", "TxTiming", "TxVoltage", "TxTiming"],
                datasets: [
                {
                    label: "Average",
                    fillColor: "rgba(51, 122, 183,0.3)",
                    strokeColor: "rgba(51, 122, 183,1)",
                    pointColor: "rgba(51, 122, 183,1)",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(51, 122, 183,1)",
                    data: [10,9,10,12]
                },
                {
                    label: "Minimum",
                    fillColor: "rgba(237,29,65,0.2)",
                    strokeColor: "rgba(237,29,65,0.6)",
                    pointColor: "rgba(237,29,65,0.6)",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(237,29,65,0.6)",
                    data: [7,6,7,10]
                },
                {
                    label: "Average",
                    fillColor: "rgba(51, 122, 183,0.3)",
                    strokeColor: "rgba(51, 122, 183,1)",
                    pointColor: "rgba(51, 122, 183,1)",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(51, 122, 183,1)",
                    data: [10,9,10,12]
                },
                {
                    label: "Minimum",
                    fillColor: "rgba(237,29,65,0.2)",
                    strokeColor: "rgba(237,29,65,0.6)",
                    pointColor: "rgba(237,29,65,0.6)",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(237,29,65,0.6)",
                    data: [7,6,7,10]
                }
                ]
                };
                var option = {
                    //Number - Point label font size in pixels
                    pointLabelFontSize : 12,
                };
		
                var read = new Chart(ctx1).Radar(data1,option);
                var write = new Chart(ctx2).Radar(data2,option);
	}
        function drawCombination(){
	var data = [
		{
                    label: 'Trust, Health',
                    strokeColor: '#337ab7',
                    data: [
                        <%
                            out.print(product.getCombinationData());
                        %>
                    ]
		},
		{
      			label: '', //fix the y-scale from 50-100
      			strokeColor: 'rgba(255, 255, 255, 0)',
      			pointColor: 'rgba(255, 255, 255, 0)',
      			pointStrokeColor: 'rgba(255, 255, 255, 0)',
      			data: [
        			{ x: 0, y: 50 }, { x: 100, y: 100 }, 
      			]		
    		}
				
	];

	var ctx = document.getElementById("combinationChart").getContext("2d");
	var myCombination = new Chart(ctx).Scatter(data, {
		scaleGridLineColor: "rgba(175, 175, 175, 0.5)",
		scaleLineColor: "rgba(175, 175, 175, 0.5)",
		bezierCurve: true,
		scaleShowGridLines: true,

	});
	}
	
	//read and write eye chart
	function drawEye(){
                <%
                    EyeChart tx = null, rx = null;
                    if(detail.getEyes().containsKey("tx".toLowerCase())) {
                        tx = detail.getEyes().get("tx".toLowerCase());
                    }
                    if(detail.getEyes().containsKey("rx".toLowerCase())) {
                        tx = detail.getEyes().get("rx".toLowerCase());
                    }
                    double[] nil = new double[]{0, 0, 0, 0};
                    double[] res = new double[4];
                %>
		var ctx1 = document.getElementById("eyeChartRead").getContext("2d");
                var ctx2 = document.getElementById("eyeChartWrite").getContext("2d");
	

                var data1 = {
                labels: ["TxVoltage", "TxTiming", "TxVoltage", "TxTiming"],
                datasets: [
                {
                    label: "MIN",
                    fillColor: "rgba(51, 122, 183,0.3)",
                    strokeColor: "rgba(51, 122, 183,1)",
                    pointColor: "rgba(51, 122, 183,1)",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(51, 122, 183,1)",
                    data: 
                    <%
                            if(tx == null) {
                                out.print(Arrays.toString(nil));
                            }
                            else {
                                double[] r = tx.getMin();
                                res[0] = r[2];
                                res[1] = r[1];
                                res[2] = r[3];
                                res[3] = r[0];
                                out.print(Arrays.toString(res));
                            }
                    %>
                },
                {
                    label: "MEAN",
                    fillColor: "rgba(237,29,65,0.2)",
                    strokeColor: "rgba(237,29,65,0.6)",
                    pointColor: "rgba(237,29,65,0.6)",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(237,29,65,0.6)",
                    data: <%
                            if(tx == null) {
                                out.print(Arrays.toString(nil));
                            }
                            else {
                                double[] r = tx.getMean();
                                res[0] = r[2];
                                res[1] = r[1];
                                res[2] = r[3];
                                res[3] = r[0];
                                out.print(Arrays.toString(res));
                            }
                    %>
                },
                {
                    label: "INTEL_MIN",
                    fillColor: "none",
                    strokeColor: "rgba(51, 122, 183,1)",
                    pointColor: "rgba(51, 122, 183,1)",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "none",
                    pointHighlightStroke: "rgba(51, 122, 183,1)",
                    data: 
                    <%
                            if(tx == null) {
                                out.print(Arrays.toString(nil));
                            }
                            else {
                                double[] r = tx.getIntel_min();
                                res[0] = r[2];
                                res[1] = r[1];
                                res[2] = r[3];
                                res[3] = r[0];
                                out.print(Arrays.toString(res));
                            }
                    %>
                },
                {
                    label: "INTEL_MEAN",
                    fillColor: "none",
                    strokeColor: "rgba(237,29,65,0.6)",
                    pointColor: "rgba(237,29,65,0.6)",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "none",
                    pointHighlightStroke: "rgba(237,29,65,0.6)",
                    data: <%
                            if(tx == null) {
                                out.print(Arrays.toString(nil));
                            }
                            else {
                                double[] r = tx.getIntel_mean();
                                res[0] = r[2];
                                res[1] = r[1];
                                res[2] = r[3];
                                res[3] = r[0];
                                out.print(Arrays.toString(res));
                            }
                    %>
                }
                ]
                };
                var data2 = {
                labels: ["RxVoltage", "RxTiming", "RxVoltage", "RxTiming"],
                datasets: [
                {
                    label: "MIN",
                    fillColor: "rgba(51, 122, 183,0.3)",
                    strokeColor: "rgba(51, 122, 183,1)",
                    pointColor: "rgba(51, 122, 183,1)",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(51, 122, 183,1)",
                    data: 
                    <%
                            if(rx == null) {
                                out.print(Arrays.toString(nil));
                            }
                            else {
                                double[] r = rx.getMin();
                                res[0] = r[2];
                                res[1] = r[1];
                                res[2] = r[3];
                                res[3] = r[0];
                                out.print(Arrays.toString(res));
                            }
                    %>
                },
                {
                    label: "MEAN",
                    fillColor: "rgba(237,29,65,0.2)",
                    strokeColor: "rgba(237,29,65,0.6)",
                    pointColor: "rgba(237,29,65,0.6)",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(237,29,65,0.6)",
                    data: <%
                            if(rx == null) {
                                out.print(Arrays.toString(nil));
                            }
                            else {
                                double[] r = rx.getMean();
                                res[0] = r[2];
                                res[1] = r[1];
                                res[2] = r[3];
                                res[3] = r[0];
                                out.print(Arrays.toString(res));
                            }
                    %>
                },
                {
                    label: "INTEL_MIN",
                    fillColor: "none",
                    strokeColor: "rgba(51, 122, 183,1)",
                    pointColor: "rgba(51, 122, 183,1)",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "none",
                    pointHighlightStroke: "rgba(51, 122, 183,1)",
                    data: 
                    <%
                            if(rx == null) {
                                out.print(Arrays.toString(nil));
                            }
                            else {
                                double[] r = rx.getIntel_min();
                                res[0] = r[2];
                                res[1] = r[1];
                                res[2] = r[3];
                                res[3] = r[0];
                                out.print(Arrays.toString(res));
                            }
                    %>
                },
                {
                    label: "INTEL_MEAN",
                    fillColor: "none",
                    strokeColor: "rgba(237,29,65,0.6)",
                    pointColor: "rgba(237,29,65,0.6)",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "none",
                    pointHighlightStroke: "rgba(237,29,65,0.6)",
                    data: <%
                            if(rx == null) {
                                out.print(Arrays.toString(nil));
                            }
                            else {
                                double[] r = rx.getIntel_mean();
                                res[0] = r[2];
                                res[1] = r[1];
                                res[2] = r[3];
                                res[3] = r[0];
                                out.print(Arrays.toString(res));
                            }
                    %>
                }
                ]
                };
                var option = {
                    //Number - Point label font size in pixels
                    pointLabelFontSize : 12,
                };
		
                var read = new Chart(ctx1).Radar(data1,option);
                var write = new Chart(ctx2).Radar(data2,option);
	}


	//switch the chart
	$("#changeEye").click(function(){
		$("<div class='row' id='eyeChart'><div class='col-md-6 center-block'><div class='eyeTitle'>Read</div><canvas id='eyeChartRead' class='center-block'></canvas></div><div class='col-md-6 center-block'><div class='eyeTitle'>Write</div><canvas id='eyeChartWrite' class='center-block'></canvas></div></div>").appendTo("#charts");
		drawEye();
		
		//remove other charts
		$("#linechart_material").hide();
		$("#combination").remove();

		//change title
		var title = $(this).children().children().html();
		$("#chart_header").html(title+"<span class='caret'></span>");
		
		//list control
		$(this).hide();
		$("#changeIncrement").show();
		$("#changeCombination").show();
		
	});

	$("#changeCombination").click(function(){
		$("<div id='combination'><canvas id='combinationChart'></canvas></div>").appendTo("#charts");
		drawCombination();

		$("#linechart_material").hide();
		$("#eyeChart").remove();

		var title = $(this).children().children().html();
		$("#chart_header").html(title+"<span class='caret'></span>");

		$(this).hide();
		$("#changeIncrement").show();
		$("#changeEye").show();
	});
	
	
	$("#changeIncrement").click(function(){
		$("#linechart_material").show();
		
		$("#eyeChart").remove();
		$("#combination").remove();

		var title = $(this).children().children().html();
		$("#chart_header").html(title+"<span class='caret'></span>");

		$(this).hide();
		$("#changeEye").show();
		$("#changeCombination").show();
	});
    });
    </script>
</html>
