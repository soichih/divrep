package com.divrep.samples;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.MaskFormatter;

import com.divrep.*;
import com.divrep.common.DivRepButton;
import com.divrep.common.DivRepColorPicker;
import com.divrep.common.DivRepDate;
import com.divrep.common.DivRepDialog;
import com.divrep.common.DivRepForm;
import com.divrep.common.DivRepTextArea;
import com.divrep.common.DivRepTextBox;

public class DialogServlet extends HttpServlet {
	
	DivRepDate date;
	DivRepTextBox text;
	
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
		out.write("<h1>Dialog Sample</h1>");
		
		new DivRepContainer(request) {
			public void initPage(DivRepPage pageroot) {
				final DivRepDialog dialog = new DivRepDialog(pageroot) {					
					public void onSubmit() {
						close();
						
						alert("thanks for selecting " + date.getValue().toString());
					}
					public void onCancel() {
						date.close();
						
						alert("you have clicked cancel");
					}
				};
				
				dialog.setHasCancelButton(true);
				dialog.setEnterToSubmit(true);
				dialog.setTitle("My title");
				
				date = new DivRepDate(dialog);
				DivRep content = new DivRep(dialog){
					
					protected void onEvent(DivRepEvent e) {
						// TODO Auto-generated method stub
						
					}

					public void render(PrintWriter out) {
						out.write("content here..hello\\world");
					}
				};
				
				text = new DivRepTextBox(dialog);
				text.setLabel("State");
				HashSet<String> values = new HashSet<String>();
				values.add("Arkansas");
				values.add("Tenessee");
				values.add("Texax");
				values.add("Florida");
				text.setAutoCompleteValues(values);
				
				dialog.render(out);
			
				DivRepButton button = new DivRepButton(pageroot, "Open Dialog");
				button.addEventListener(new DivRepEventListener() {
					public void handleEvent(DivRepEvent e) {
						dialog.open();
					}
				});
				button.render(out);
				
			}
		};
		
		out.write("</div></body></html>");
	}
	

}

