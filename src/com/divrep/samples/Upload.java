package com.divrep.samples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;

import com.divrep.*;
import com.divrep.common.DivRepSelectBox;
import com.divrep.common.DivRepStaticContent;
import com.divrep.common.DivRepTextBox;
import com.divrep.common.DivRepTextBox.SearchEventListener;
import com.divrep.common.DivRepUpload;
import com.divrep.samples.FormServlet.Form;
import com.divrep.samples.HelloWorldServlet.HelloWorld;
import com.divrep.validator.DivRepLengthValidator;

public class Upload extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{	
		final PrintWriter out = response.getWriter();
		
		out.write("<!DOCTYPE html>\n<html><head>");
		
		out.write("<script type=\"text/javascript\" src=\"//code.jquery.com/jquery-1.10.2.min.js\"></script>");
		out.write("<script type=\"text/javascript\" src=\"//code.jquery.com/ui/1.10.3/jquery-ui.min.js\"></script>");
		out.write("<link href=\"//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/themes/smoothness/jquery-ui.css\" rel=\"stylesheet\" type=\"text/css\"/>");

		//Load DivRep Stuff
		out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/divrep.css\" />");
		out.write("<script type=\"text/javascript\" src=\"divrep.js\"></script>");
		
		//jquery-fileupload plugins
		out.write("<script type=\"text/javascript\" src=\"jquery.iframe-transport.js\"></script>");	
		out.write("<script type=\"text/javascript\" src=\"jquery.fileupload.js\"></script>");	
		
		out.write("</head><body><div id=\"content\">");		
		out.write("<h1>Sample Upload Form</h1>");
		
		new DivRepContainer(request) {
			public void initPage(DivRepPage pageroot) {
				
				//place to display some status
				final DivRepStaticContent status = new DivRepStaticContent(pageroot, "<p>Waiting for you to upload something</p>");
				
				class SampleUploader extends DivRepUpload {
					public SampleUploader(DivRep parent) {
						super(parent);
						// TODO Auto-generated constructor stub
					}

					@Override
					public void onUpload(List<FileItem> files) {
						String st = "";
						for (FileItem file : files) {
							String fieldname = file.getFieldName();
							String filename = FilenameUtils.getName(file.getName());
							
							st += "<p>"+fieldname + " / "+ filename + "</p>";
							
							/* just some sample code to show how to read files from InputStream
							try {
								InputStream filecontent = file.getInputStream();
								
								// just dump
								BufferedReader in = new BufferedReader(new InputStreamReader(filecontent));
								String inputLine;
								while ((inputLine = in.readLine()) != null)	System.out.println(inputLine);
								in.close();
								
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							*/
							System.out.println(st);
							status.setHtml(st);
							status.redraw();
							//this.alert(st);
						}
					}
				}
				SampleUploader upload = new SampleUploader(pageroot);
				
				upload.render(out);
				status.render(out);
			}
		};
		
		out.write("</div></body></html>");
	}
}