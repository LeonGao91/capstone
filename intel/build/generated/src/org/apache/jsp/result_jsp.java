package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class result_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");

    Object userObj = session.getAttribute("validUser");
    if(userObj == null) {
        response.sendRedirect("login.jsp");
    }

      out.write("\r\n");
      out.write("<!DOCTYPE html>\r\n");
      out.write("<html lang=\"en\">\r\n");
      out.write("    <head>\r\n");
      out.write("        <meta charset=\"utf-8\">\r\n");
      out.write("        <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n");
      out.write("\r\n");
      out.write("        <title>Result</title>\r\n");
      out.write("\r\n");
      out.write("        <link href=\"css/boostrap/bootstrap.min.css\" rel=\"stylesheet\">\r\n");
      out.write("        <link href=\"css/OurCSS/homepage.css\" rel=\"stylesheet\">\r\n");
      out.write("        <link href=\"css/OurCSS/resultpage.css\" rel=\"stylesheet\">\r\n");
      out.write("        <script src=\"js/library/justGauge/justgage.1.0.1.min.js\"></script>\r\n");
      out.write("        <script src=\"js/library/justGauge/raphael.2.1.0.min.js\"></script>\r\n");
      out.write("        <script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>\r\n");
      out.write("        <script src=\"js/OurJS/lineChart.js\"></script>\r\n");
      out.write("    </head>\r\n");
      out.write("\r\n");
      out.write("    <body>\r\n");
      out.write("        <div class=\"navigation-bar\">\r\n");
      out.write("            <div class = \"nav-header\">\r\n");
      out.write("                <img id=\"header\" src=\"media/logo.png\">\r\n");
      out.write("            </div>\r\n");
      out.write("            <nav class=\"navbar navbar-default\">\r\n");
      out.write("                <div class=\"container\">\r\n");
      out.write("                    <div id=\"navbar\">\r\n");
      out.write("                        <ul class=\"nav navbar-nav\">\r\n");
      out.write("                            <li><a href=\"index.jsp\">Main</a></li>\r\n");
      out.write("                            <li class=\"active\"><a href=\"#\">Result</a></li>\r\n");
      out.write("                        </ul>\r\n");
      out.write("                        <ul class=\"nav navbar-nav navbar-right\">\r\n");
      out.write("                            <li>\r\n");
      out.write("                                <a id=\"user\" href=\"\">\r\n");
      out.write("                                ");

                                    String user = (String) userObj;
                                    out.print("Welcome " + user);
                                
      out.write("\r\n");
      out.write("                                </a>\r\n");
      out.write("                            <li>\r\n");
      out.write("                            <form id=\"logout_panel\" class=\"navbar-form navbar-right\" action=\"logout.jsp\" method=\"post\">\r\n");
      out.write("                                    <button id=\"logout_button\" class=\"btn btn-success\" type=\"submit\" type=\"button\" class=\"btn btn-default btn-lg\" >\r\n");
      out.write("                                        Logout\r\n");
      out.write("                                    </button>\r\n");
      out.write("                            </form>\r\n");
      out.write("                        </ul>\r\n");
      out.write("                    </div>\r\n");
      out.write("                </div>\r\n");
      out.write("            </nav>\r\n");
      out.write("        </div>\r\n");
      out.write("        <!--\r\n");
      out.write("        <div id=\"result_div\">\r\n");
      out.write("            <div class=\"panel panel-default\">\r\n");
      out.write("                <div class=\"panel-heading\">Average</div>\r\n");
      out.write("                <div class=\"panel-body\">\r\n");
      out.write("                    ");

                        if(request.getAttribute("error")!=null || request.getAttribute("average")==null){
                            out.print(request.getAttribute("userFolder") + ": ");
                            out.print("Empty");
                        }
                        else{
                            out.print(request.getAttribute("userFolder") + ": ");
                            out.print(request.getAttribute("average"));
                        }
                    
      out.write("\r\n");
      out.write("                </div>\r\n");
      out.write("            </div>\r\n");
      out.write("        </div>\r\n");
      out.write("        -->\r\n");
      out.write("        <h3 id=\"title\">DELL XPS 13 - 04/07/2015  <span class=\"label label-success\">PASS!</span></h3>\r\n");
      out.write("        <div class=\"panel panel-primary main_panel\" style=\"margin-top: 2%\">\r\n");
      out.write("            <div class=\"panel-heading\">\r\n");
      out.write("                <h3 class=\"panel-title\" align=\"center\">Scores</h3>\r\n");
      out.write("            </div>\r\n");
      out.write("            <div id=\"trust\" class=\"gauge\"></div>\r\n");
      out.write("            <div id=\"health\" class=\"gauge\"></div>\r\n");
      out.write("            <script>\r\n");
      out.write("                var g = new JustGage({\r\n");
      out.write("                  id: \"trust\",\r\n");
      out.write("                  value: 32,\r\n");
      out.write("                  min: 0,\r\n");
      out.write("                  max: 100,\r\n");
      out.write("                  title: \"Trust\",\r\n");
      out.write("                  label: \"SCORE\",\r\n");
      out.write("                  levelColors: [\"D00000\",\"00CC33\"],\r\n");
      out.write("                  startAnimationTime : 2000\r\n");
      out.write("                });\r\n");
      out.write("                var g2 = new JustGage({\r\n");
      out.write("                  id: \"health\",\r\n");
      out.write("                  value: 91,\r\n");
      out.write("                  min: 0,\r\n");
      out.write("                  max: 100,\r\n");
      out.write("                  title: \"Health\",\r\n");
      out.write("                  label: \"SCORE\",\r\n");
      out.write("                  levelColors: [\"D00000\",\"00CC33\"],\r\n");
      out.write("                  startAnimationTime : 2000\r\n");
      out.write("                });\r\n");
      out.write("            </script>\r\n");
      out.write("        </div>\r\n");
      out.write("        <div class=\"panel panel-danger main_panel\">\r\n");
      out.write("            <div class=\"panel-heading\">\r\n");
      out.write("                <h3 class=\"panel-title\" align=\"center\">Messages</h3>\r\n");
      out.write("            </div>\r\n");
      out.write("            <div class=\"panel-body\">Trust score is too low</div>\r\n");
      out.write("        </div>\r\n");
      out.write("        <div class=\"panel panel-primary main_panel\">\r\n");
      out.write("            <div class=\"panel-heading\">\r\n");
      out.write("                <h3 class=\"panel-title\" align=\"center\">Incremental Chart</h3>\r\n");
      out.write("            </div>\r\n");
      out.write("            <div id=\"linechart_material\"></div>\r\n");
      out.write("        </div>\r\n");
      out.write("        \r\n");
      out.write("\r\n");
      out.write("    </body>\r\n");
      out.write("</html>\r\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
