package com.divrep.samples;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Samples extends HttpServlet {
	protected void doGet(HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException 
	{	
		final PrintWriter out = response.getWriter();
		out.write("<html><head>\n");
		
		out.write("<script type=\"text/javascript\" src=\"//code.jquery.com/jquery-1.10.2.min.js\"></script>");
		out.write("<script type=\"text/javascript\" src=\"//code.jquery.com/ui/1.10.3/jquery-ui.min.js\"></script>");
		out.write("<link href=\"//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/themes/smoothness/jquery-ui.css\" rel=\"stylesheet\" type=\"text/css\"/>");

		//Load DivRep Stuff
		out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/divrep.css\" />");
		out.write("<script type=\"text/javascript\" src=\"divrep.js\"></script>");
		
		out.write("<style> * { padding: 0px; margin: 0px; }");
		out.write(".puzzle .divrep_processing {background-image: url('css/images/animated_question_mark.gif');} ");
		out.write(".tile {background-image: url('css/images/question_mark.png'); background-position:0 -10px;}");
		out.write(".logic {padding: 4px; border: 1px solid black;}");
		out.write("a img {border: 0px;cursor: pointer;}");
		out.write("div.screenshot {padding: 10px; border: 3px solid black; width: 400px;}");
		out.write("</style>"); 
		
		
		out.write("</head>");
		out.write("<body class=\"divrep_app\">");
		
		SampleStaticContent.renderHeader(out);
		SampleStaticContent.renderSide(out);
		
		out.write("<div class=\"divrep_app_main\">");
	
		out.write("<h2>Sample Applications</h2>");
				
		out.write("<div class=\"divrep_app_section\">");
		out.write("<h3>Puzzle Game</h3>");
		out.write("<div style=\"padding-left: 30px\">");
		out.write("<div class=\"screenshot\"><a href=\"puzzle\"><img src=\"screenshots/puzzle.png\"/></a></div>");
		out.write("</div><br/>");
		out.write("</div>");
				
		out.write("<div class=\"divrep_app_section\">");
		out.write("<h3>Calculator</h3>");
		out.write("<div style=\"padding-left: 30px\">");
		out.write("<div class=\"screenshot\"><a href=\"calculator\"><img src=\"screenshots/calc.png\"/></a></div>");
		out.write("</div><br/>");
		out.write("</div>");
				
		out.write("</div>");//app_main
		out.write("</body></html>");

	}
}
