package com.divrep.samples;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.divrep.*;
import com.divrep.common.DivRepCheckBox;
import com.divrep.common.DivRepDate;
import com.divrep.common.DivRepForm;
import com.divrep.common.DivRepMoneyAmount;
import com.divrep.common.DivRepTextArea;
import com.divrep.common.DivRepTextBox;

public class FormServlet extends HttpServlet {
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
		out.write("<h1>Sample Form</h1>");
		
		//Create DivRep form and render
		new DivRepContainer(request) {
			public void initPage(DivRepPage pageroot) {
				Form form = new Form(pageroot);
				form.render(out);
			}
		};
		
		out.write("</div></body></html>");
	}

	//Define our DivRep form
	class Form extends DivRepForm
	{
		DivRepTextBox name;
		DivRepTextBox auto;
		DivRepTextBox tel;
		DivRepTextArea note;
		DivRepCheckBox check;
		DivRepDate date;
		DivRepMoneyAmount money;
		
		//SomeDivRep extra;
		
		public Form(DivRep _parent) {
			//Second argument is the return address of this form after user hit submit (sucessfully) or cancel
			super(_parent, "http://www.iu.edu");
			
			name = new DivRepTextBox(this);
			name.setLabel("Full Name");
			name.setSampleValue("Soichi Hayashi €");
			name.setRequired(true);
			
			HashSet<String> values = new HashSet<String>();
			values.add("apple");
			values.add("orange");
			values.add("grape");
			values.add("banana");
			auto = new DivRepTextBox(this);
			auto.setAutoCompleteValues(values);
			auto.setLabel("Auto Complete Fields");
			auto.setSampleValue("Fruit");
			auto.setRequired(true);
			
			tel = new DivRepTextBox(this);
			tel.setLabel("Telephone Number (auto - formatted)");
			tel.setSampleValue("€ http://cnn.com hello \"mark");
			tel.setValue("€ http://cnn.com This is a test\r\nNew line\r\n\" quote mark.");
			tel.addEventListener(new TelephoneNumberFormatterEventListener());
			
			note = new DivRepTextArea(this);
			note.setLabel("Note");
			note.setSampleValue("€ http://cnn.com \" mark");
			note.setValue("€ http://cnn.com This is a test\r\nNew line\r\n\" quote mark.");
			note.setRequired(true);
			note.setWidth(0);
			//note.setDisabled(true);
			
			check = new DivRepCheckBox(this);
			check.setLabel("Do nothing (disabled)");
			check.setDisabled(true);
			
			date = new DivRepDate(this);
			
			money = new DivRepMoneyAmount(this);
			money.allowPercentage(true);
		}
		
		//When user clicks submit and if the form passes validations, this function will be called
		protected Boolean doSubmit() {
			//Do sometihng with the value
			alert("Thank you, " + name.getValue() + " for choosing " + auto.getValue() + "and the data is "  + date.getValue().toString());
			
			//return false to stay on the form
			return false;
		}
		
		class TelephoneNumberFormatterEventListener extends DivRepEventListener {
			public void handleEvent(DivRepEvent e) {
				String value = (String)e.value;
		        int length = value.length();
		        
		        //remove non digit
		        StringBuffer buffer = new StringBuffer(length);
		        for(int i = 0; i < length; i++) {
		            char ch = value.charAt(i);
		            if (Character.isDigit(ch)) {
		                buffer.append(ch);
		            }
		        }
		        value = buffer.toString();
		        //truncate at 9 digit (or 10 if starts with 1)
	        	if(value.length() > 0 && value.charAt(0) != '1') {
	        		if(value.length() > 11) {
	        			value = value.substring(1, 10);
	        		}
	        	} else {
	        		if(value.length() > 10) {
	        			value = value.substring(0, 9);
	        		}
		        }
	        	if(value.length() == 10) {
	        		value = "+1(" + value.substring(0,3) + ")" + value.substring(3,6) + "-" + value.substring(6);
	        	} else {
	        		tel.alert("Invalid telephone number");
	        		value = "";
	        	}
		   
				tel.setValue(value);
				tel.redraw();
			}
			
		}
	}
	

}

