package com.divrep.samples;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.divrep.*;
import com.divrep.common.DivRepSelectBox;
import com.divrep.samples.HelloWorldServlet.HelloWorld;

public class SelectServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{	
		final PrintWriter out = response.getWriter();
		
		out.write("<script type=\"text/javascript\" src=\"//code.jquery.com/jquery-1.10.2.min.js\"></script>");
		out.write("<script type=\"text/javascript\" src=\"//code.jquery.com/ui/1.10.3/jquery-ui.min.js\"></script>");
		out.write("<link href=\"//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/themes/smoothness/jquery-ui.css\" rel=\"stylesheet\" type=\"text/css\"/>");
		
		//Load DivRep Stuff
		out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/divrep.css\" />");
		out.write("<script type=\"text/javascript\" src=\"divrep.js\"></script>");
		
		//Create Select component and render
		new DivRepContainer(request) {
			public void initPage(DivRepPage pageroot) {
				Select select = new Select(pageroot);
				select.render(out);
			}
		};
		
	}
	
	class Select extends DivRep
	{
		//Button for each styles
	    DivRepSelectBox select;
	    DivRepSelectBox select2;
	    DivRepSelectBox select3;
	    
	    public Select(DivRep _parent) {
	        super(_parent);

	        //setup select box options
	        LinkedHashMap<Integer, String> options = new LinkedHashMap<Integer, String>();
	        options.put(1, "Americano");
	        options.put(2, "Cappuccino");
	        options.put(3, "Caffe Latte");
	        options.put(4, "Frappe");
	        options.put(5, "Iced");
	        options.put(6, "Mocha");
	        options.put(7, "Oliang");
	        options.put(8, "Turkish");
	              
	        //instantiate the select box
	        select = new DivRepSelectBox(this, options);
	        select.addEventListener(new DivRepEventListener() {
	        	public void handleEvent(DivRepEvent e) {
	        		alert("Select 1 :: You have selected the item " + e.value);
	        		select.redraw();
	        	}
	        });
	        
	        LinkedHashMap<Integer, String> options2 = new LinkedHashMap<Integer, String>();
	        options2.put(1, "Americano");
	        options2.put(2, "Cappuccino");
	        options2.put(3, "Caffe Latte");
	        options2.put(4, "Frappe");
	        options2.put(5, "Iced");
	        options2.put(6, "Mocha");
	        options2.put(7, "Oliang");
	        options2.put(8, "Turkish");
	        
	        //instantiate the select box
	        select2 = new DivRepSelectBox(this, options2);
	        select2.addEventListener(new DivRepEventListener() {
	        	public void handleEvent(DivRepEvent e) {
	        		alert("Select 2 :: You have selected the item " + e.value);
	        		select.redraw();
	        	}
	        });
	        
	        select3 = new DivRepSelectBox(this, options2);
	        select3.setDisabled(true);
	        select3.setValue(3);
	        select3.setLabel("Disabled");
	    }

	    public void render(PrintWriter out) {
	        out.write("<div id=\""+getNodeID()+"\">");
	        select.render(out);
	        select2.render(out);
	        select3.render(out);
	        out.write("</div>");
	    }

		@Override
		protected void onEvent(DivRepEvent e) {
			// TODO Auto-generated method stub
			
		}
	}	
}