package com.divrep.samples;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.divrep.*;
import com.divrep.common.DivRepSelectBox;
import com.divrep.common.DivRepTextBox;
import com.divrep.common.DivRepTextBox.SearchEventListener;
import com.divrep.samples.HelloWorldServlet.HelloWorld;
import com.divrep.validator.DivRepLengthValidator;

public class TextBox extends HttpServlet {
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
				final DivRepTextBox text = new DivRepTextBox(pageroot);
				text.setLabel("Name");
				/*
				text.setDisabled(true);
				text.setValue("Test");
				*/
				text.setSearchEventListener(new SearchEventListener() {
					private static final long serialVersionUID = 1L;

					public ArrayList<String> handleEvent(String term) {
						ArrayList<String> ret = new ArrayList<String>();
						ret.add(term + " San");
						ret.add(term + " Kun");
						ret.add(term + " Chan");
						return ret;
						
					}});
				text.addValidator(new DivRepLengthValidator(10, 20));
				text.render(out);
			}
		};
		
	}
}