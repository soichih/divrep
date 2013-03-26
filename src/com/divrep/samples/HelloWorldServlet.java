package com.divrep.samples;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.divrep.*;
import com.divrep.samples.FormServlet.Form;

public class HelloWorldServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException 
	{
		final PrintWriter out = response.getWriter();
		out.write("<html><head>");
	
		//Load jQuery
		out.write("<script type=\"text/javascript\" src=\"jquery-1.7.1.min.js\"></script>");
		out.write("<script type=\"text/javascript\" src=\"jquery-ui-1.8.18.custom.min.js\"></script>");
		out.write("<link href=\"css/smoothness-1.8.18/jquery-ui-1.8.18.custom.css\" rel=\"stylesheet\" type=\"text/css\"/>");

		//Load DivRep Stuff
		out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/divrep.css\" />");
		out.write("<script type=\"text/javascript\" src=\"divrep.js\"></script>");
		
		out.write("<style>");
		out.write(".clickme {padding: 10px; margin: 10px; background-color: #ccc;}");
		out.write(".divrep_processing .clickme {color: red;}");
		out.write("</style>");
				
		out.write("</head><body><div id=\"content\">");
		
		//Create HelloWorld component and render
		new DivRepContainer(request) {
			public void initPage(DivRepPage pageroot) {
				final HelloWorld hello = new HelloWorld(pageroot);
				hello.render(out);
			}
		};
		
		out.write("</div></body></html>");
	}

	class HelloWorld extends DivRep
	{
			int counter = 0;
	        public HelloWorld(DivRep _parent) {
	                super(_parent);
	                // TODO Auto-generated constructor stub
	        }

	        public void render(PrintWriter out) {
			       out.write("<div id=\""+getNodeID()+"\" onclick=\"divrep(this.id);\">");
			       out.write("<p class=\"clickme\">Click ME</p>");
			       out.write("<p>You have clicked " + counter + " times</p>");    
			       out.write("</div>"); 
	        }

			protected void onEvent(DivRepEvent e) {
				counter++;
				redraw();
			}      
	}

}
