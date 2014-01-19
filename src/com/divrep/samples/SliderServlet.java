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
		
		out.write("<script type=\"text/javascript\" src=\"//code.jquery.com/jquery-1.10.2.min.js\"></script>");
		out.write("<script type=\"text/javascript\" src=\"//code.jquery.com/ui/1.10.3/jquery-ui.min.js\"></script>");
		out.write("<link href=\"//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/themes/smoothness/jquery-ui.css\" rel=\"stylesheet\" type=\"text/css\"/>");

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

