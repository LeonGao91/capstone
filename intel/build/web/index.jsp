
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
                                    <li role="presentation"><a role="menuitem" tabindex="-1"  class="listItem">ALL</a></li>
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
                                    <li role="presentation"><a role="menuitem" tabindex="-1"  class="listItem">NOT READY</a></li> 
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
                                } else if(product.getPass_fail().toLowerCase().equals("fail")) {
                                    out.print("<td><span class='label label-danger'>" + product.getPass_fail().toUpperCase() + "</span></td>");
                                } else {
                                    out.print("<td><span class='label label-warning'>" + product.getPass_fail().toUpperCase() + "</span></td>");
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
                                    if (brief.getPass_fail().toLowerCase().equals("pass")) {
                                        out.print("<td><span class='label label-success'>" + brief.getPass_fail().toUpperCase() + "</span></td>");
                                    } else if(brief.getPass_fail().toLowerCase().equals("fail")) {
                                        out.print("<td><span class='label label-danger'>" + brief.getPass_fail().toUpperCase() + "</span></td>");
                                    } else {
                                        out.print("<td><span class='label label-warning'>" + brief.getPass_fail().toUpperCase() + "</span></td>");
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
                $('.expandable').nextAll('tr').each( function() {
                    if(!($(this).is('.expandable'))) $(this).hide();  //first hide all the rows which don't  belong to ".expandable"
                });
                $('.expandable').click(function () {
                    var color = $(this).css("background-color");  //get the background color of the row being clicked
                    $(this).nextAll('tr').each( function() { //expand rows with same product, and change the background color
                        if($(this).is('.expandable')) {
                            return false;
                        }
                        $(this).toggle(350);
                        $(this).css("background-color",color);
                    });
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

                var input = $(this).html();    //get the item that is chosen
                
                $("#mytable tr.expandable").each(function (){
                    var text = $(this).find("td:nth-child("+columnNo+")").text();  //get the content of the corresponding column in each expandable row
                    if (input==="ALL"){
                        $(this).css("display","table-row");  //display all the rows
                    }
                    else{
                        $(this).css("display",text.indexOf(input) === -1 ? 'none' : 'table-row'); //check if the chosen item mathces the current row's content, if yes, show it
                    }
                });

            });

        });
        

        //sort
        $(".date").sort(function(a,b){    //defualt sorting, the most recnet rows is shown on the top
                    return new Date($(a).html()) > new Date($(b).html());
                }).each(function(){
                    $("tbody").prepend($(this).parent());
        });

        </script>
    </body>
</html>
