package com.divrep.samples;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.divrep.*;
import com.divrep.common.DivRepButton;
import com.divrep.common.DivRepColorPicker;
import com.divrep.common.DivRepLocationSelector;
import com.divrep.common.DivRepLocationSelector.LatLng;
import com.divrep.samples.ButtonServlet.ButtonDivRep;

//for google map api doc, see ...
//https://developers.google.com/maps/documentation/javascript/tutorial

public class LocationSelector extends HttpServlet {
	protected void doGet(HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException 
	{	
		final PrintWriter out = response.getWriter();
		out.write("<!DOCTYPE html>\n");//HTML5
		out.write("<html><head>");

		//Load jQuery
		out.write("<script type=\"text/javascript\" src=\"jquery-1.7.1.min.js\"></script>");
		out.write("<script type=\"text/javascript\" src=\"jquery-ui-1.8.18.custom.min.js\"></script>");
		out.write("<link href=\"css/smoothness-1.8.18/jquery-ui-1.8.18.custom.css\" rel=\"stylesheet\" type=\"text/css\"/>");

		//Load DivRep Stuff
		out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/divrep.css\" />");
		out.write("<script type=\"text/javascript\" src=\"divrep.js\"></script>");

		out.write("</head><body><div id=\"content\">");		
		out.write("<h1>Location Selector Sample</h1>");
		
		new DivRepContainer(request) {
			public void initPage(DivRepPage pageroot) {
				final DivRepLocationSelector latlng = new DivRepLocationSelector(pageroot, "css/images/target.png", "AIzaSyAIommV_PJUjC36Y9wwdndFwqzz4QLhpTs");
				latlng.setHttps(true);//set to true to load gmap via https
				latlng.setLabel("Latitude / Longitude");
				latlng.setValue(latlng.new LatLng(41.85, -87.65, 8));
				latlng.setRequired(true);
				latlng.render(out);
				
				DivRepButton refresh = new DivRepButton(pageroot, "Invalidate");
				refresh.addEventListener(new DivRepEventListener() {

					@Override
					public void handleEvent(DivRepEvent e) {
						latlng.redraw();
					}});
				refresh.render(out);
			}
		};
		
		out.write("</div></body></html>");
	}

}

