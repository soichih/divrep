package com.divrep.samples;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.divrep.*;
import com.divrep.common.DivRepColorPicker;
import com.divrep.samples.ButtonServlet.ButtonDivRep;

public class ColorPickerServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException 
	{	
		final PrintWriter out = response.getWriter();
		out.write("<html><head>");

		//Load jQuery
		out.write("<script type=\"text/javascript\" src=\"jquery-1.7.1.min.js\"></script>");
		out.write("<script type=\"text/javascript\" src=\"jquery-ui-1.8.18.custom.min.js\"></script>");
		out.write("<link href=\"css/smoothness/jquery-ui-1.8.2.custom.css\" rel=\"stylesheet\" type=\"text/css\"/>");

		//Load DivRep Stuff
		out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"http://divrep.googlecode.com/files/divrep.css\" />");
		out.write("<script type=\"text/javascript\" src=\"http://divrep.googlecode.com/files/divrep.js\"></script>");
		
		out.write("</head><body><div id=\"content\">");		
		out.write("<h1>Color Picker Sample</h1>");
		
		new DivRepContainer(request) {
			public void initPage(DivRepPage pageroot) {
				DivRepColorPicker picker = new DivRepColorPicker(pageroot);
				picker.setLabel("Color Picker");
				picker.render(out);
			}
		};
		
		out.write("</div></body></html>");
	}

}

