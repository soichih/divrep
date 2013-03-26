package com.divrep.samples;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.divrep.*;
import com.divrep.common.DivRepColorPicker;
import com.divrep.common.DivRepSlider;
import com.divrep.samples.SelectServlet.Select;

public class SliderServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
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
		
		out.write("</head><body><div id=\"content\">");		
		out.write("<h1>Slider Sample</h1>");
		
		//Create Slider and render
		new DivRepContainer(request) {
			public void initPage(final DivRepPage pageroot) {
				DivRepSlider slider = new DivRepSlider(pageroot);
				//slider.setListenSlideEvents(true);
				slider.render(out);
				slider.addEventListener(new DivRepEventListener(){
					public void handleEvent(DivRepEvent e) {
						pageroot.alert(e.value);
					}});
			}
		};
		
		out.write("</div></body></html>");
	}

}

