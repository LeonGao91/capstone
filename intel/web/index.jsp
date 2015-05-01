
<%@page import="java.util.LinkedList"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Map"%>
<%@page import="dataAnalyticsModel.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        <script src="js/library/jquery-1.11.2.js"></script>
        <script src="js/library/bootstrap.min.js"></script>
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
            

            <table id="mytable" class="table order-table">
                <thead>
                    <tr>
                        <th>
                            <div class="dropdown">
                                <button class="btn btn-default dropdown-toggle filter" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-expanded="true">Product  <span class="caret"></span></button>
                                <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
                                    <c:forEach var="item" items="${sessionScope.summary.getProducts()}">
                                        <li role="presentation"><a role="menuitem" tabindex="-1"  class="listItem"><c:out value="${item}"/></a></li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </th>
                        <th class="arrow_col"></th>
                        <th id="sort" down="1">Date  <span class="glyphicon glyphicon-chevron-down"></span></th>
                        <th>
                            <div class="dropdown">
                                <button class="btn btn-default dropdown-toggle filter" type="button" id="dropdownMenu2" data-toggle="dropdown" aria-expanded="true">Result  <span class="caret"></span></button>
                                <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
                                    <li role="presentation"><a role="menuitem" tabindex="-1"  class="listItem">PASS</a></li>
                                    <li role="presentation"><a role="menuitem" tabindex="-1"  class="listItem">FAIL</a></li> 
                                    <li role="presentation"><a role="menuitem" tabindex="-1"  class="listItem">ALL</a></li>  <!--remember to add "all"-->
                                </ul>
                            </div>
                        </th>
                        <th></th>
                        
                    </tr>
                </thead>
                <tbody>
                    <%
                        //print out all elements
                        Object o =  session.getAttribute("summary");
                        if(o != null) {
                            TestSummary summary = (TestSummary) o;
                            for(Map.Entry<String, TestProduct> e : summary.getElements()) {
                                TestProduct product = e.getValue();
                                LinkedList<TestBrief> tests = product.getElements();
                                out.print("<tr class='expandable'>");
                                out.print("<td  style='width: 15%'> " + e.getKey() + " </td>");
                                out.print("<td><span class='glyphicon glyphicon-triangle-bottom' aria-hidden='true'></span></td>");
                                SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");
                                Date d = date.parse(product.getLast_test_date());
                                date.applyPattern("HH:mm:ss - MM/dd/yyyy");
                                out.print("<td>" + date.format(d) + "</td>");
                                if (product.getPass_fail().equals("pass")) {
                                    out.print("<td><span class='label label-success'>" + product.getPass_fail().toUpperCase() + "</span></td>");
                                } else {
                                    out.print("<td><span class='label label-danger'>" + product.getPass_fail().toUpperCase() + "</span></td>");
                                }
                                out.print("<td><form action='Detail?file=" 
                                            + product.getLastTest()
                                            + "&product=" + e.getKey()
                                            + "&test=" + (tests.size() - 1)
                                            + "' method='post'><button type='submit' class='btn btn-primary'>View</button></form></td></tr>");
                                
                                for (int i = tests.size() - 1; i >= 0; i--) {
                                    TestBrief brief = tests.get(i);
                                    out.print("<tr>");
                                    out.print("<td>" + e.getKey() + "</td>");
                                    out.print("<td></td>");
                                    date = new SimpleDateFormat("yyyyMMddHHmmss");
                                    d = date.parse(brief.getTest_date());
                                    date.applyPattern("HH:mm:ss - MM/dd/yyyy");
                                    out.print("<td>" + date.format(d) + "</td>");
                                    if (brief.getPass_fail().equals("pass")) {
                                        out.print("<td><span class='label label-success'>" + brief.getPass_fail().toUpperCase() + "</span></td>");
                                    } else {
                                        out.print("<td><span class='label label-danger'>" + brief.getPass_fail().toUpperCase() + "</span></td>");
                                    }
                                    out.print("<td><form action='Detail?file=" 
                                            + brief.getResult_file()
                                            + "&product=" + e.getKey()
                                            + "&test=" + i
                                            + "' method='post'><button type='submit' class='btn btn-primary'>View</button></form></td></tr>");
                                }
                            }
                        }
                    %>
                    
                </tbody>
            </table>
 	</div>
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js"></script>
        <script>
            //expandable table
            $(document).ready(function() {
                $('tr.expandable:even').addClass('odd');
                $('.expandable').click(function () {
                    var color = $(this).css("background-color");
                    $(this).nextAll('tr').each( function() {
                        if($(this).is('.expandable')) {
                            return false;
                        }
                        $(this).toggle(350);
                        $(this).css("background-color",color);
                    });
                });

                $('.expandable').nextAll('tr').each( function() {
                    if(!($(this).is('.expandable'))) $(this).hide();
                });

            });
            

        //filter

        $(".filter").click(function(){


            if ($(this).text()==='Product  '){
                var columnNo = 1;
            }
            else if ($(this).text()==='Result  '){
                var columnNo = 4;
            }


            $(".listItem").click(function(){

                var input = $(this).html();

                $("#mytable tr.expandable").each(function (){
                    var text = $(this).find("td:nth-child("+columnNo+")").text();
                    if (input==="ALL"){
                        $(this).css("display","table-row");
                    }
                    else{
                        $(this).css("display",text.indexOf(input) === -1 ? 'none' : 'table-row');
                    }
                });

            });

        });
        

        //sort

        $("#sort").click(function(){
            var th = $(this);
            var down = th.attr("down");
            if (down==="1"){ 
                th.children().attr("class","glyphicon glyphicon-chevron-up"); 
                th.attr("down","0");
                $(".date").sort(function(a,b){
                    return new Date($(a).html()) < new Date($(b).html());
                }).each(function(){
                    $("tbody").prepend($(this).parent());
                })
            }
            else{ 
                th.children().attr("class","glyphicon glyphicon-chevron-down"); 
                th.attr("down","1");
                $(".date").sort(function(a,b){
                    return new Date($(a).html()) > new Date($(b).html());
                }).each(function(){
                    $("tbody").prepend($(this).parent());
                })
            }

        })
        /**
                //filter
        (function(document) {
	'use strict';

	var LightTableFilter = (function(Arr) {

		var _input;

		function _onInputEvent(e) {
			_input = e.target;
			var tables = document.getElementsByClassName(_input.getAttribute('data-table'));
			Arr.forEach.call(tables, function(table) {
				Arr.forEach.call(table.tBodies, function(tbody) {
					Arr.forEach.call(tbody.rows, _filter);
				});
			});
		}

		function _filter(row) {
			var text = row.textContent.toLowerCase(), val = _input.value.toLowerCase();
			row.style.display = text.indexOf(val) === -1 ? 'none' : 'table-row';
		}

		return {
			init: function() {
				var inputs = document.getElementsByClassName('light-table-filter');
				Arr.forEach.call(inputs, function(input) {
					input.oninput = _onInputEvent;
				});
			}
		};
	})(Array.prototype);

	document.addEventListener('readystatechange', function() {
		if (document.readyState === 'complete') {
			LightTableFilter.init();
		}
	});

})(document);
**/
        </script>
    </body>
</html>
