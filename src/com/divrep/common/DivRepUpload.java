package com.divrep.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringEscapeUtils;

import com.divrep.DivRep;
import com.divrep.DivRepEvent;

//this requires apache-common-io and apache-common-fileupload libs
//http://commons.apache.org/proper/commons-io/
//http://commons.apache.org/proper/commons-fileupload/

public class DivRepUpload extends DivRepFormElement<String> {

	//use this to set input class (like bootstra's input-xlarge)
	private ArrayList<String> input_classes = new ArrayList<String>();
	public void addInputClass(String _class) {
		input_classes.add(_class);
	}
	protected void renderInputClass(PrintWriter out) {
		out.write("class=\"");
		for(String _class : input_classes) {
			out.write(_class);
			out.write(" ");
		}
		out.write("\"");
	}

	//multiple not yet supported
	private boolean allow_multiple = false;
	public void setMultiple(boolean allow) {
		allow_multiple = allow;
	}
	
	public DivRepUpload(DivRep parent) {
		super(parent);
		addClass("divrep_upload");
	}
	
	public void render(PrintWriter out) {
		out.print("<div ");
		renderClass(out);
		out.write("id=\""+getNodeID()+"\">");
		if(!isHidden()) {
			if(getLabel() != null) {
				out.print("<label>"+StringEscapeUtils.escapeHtml(getLabel())+"</label><br/>");
			}
			
			String disabled_text = "";
			if(isDisabled()) {
				disabled_text = "disabled";
			}
			
			String multiple = "";
			if(allow_multiple) {
				multiple = "multiple=\"multiple\"";
			}
			
			int random = (int)(Math.random()*10000);
			out.write("<input ");
			renderInputClass(out);
			out.write("id=\""+getNodeID()+"_input"+random+"\" type=\"file\" "+multiple+" "+disabled_text+">");
			
			out.write("<script type=\"text/javascript\">\n");						
			out.write("$('#"+getNodeID()+"_input"+random+"').fileupload({\n");	
			out.write("        url: \"divrep?action=upload&nodeid="+getNodeID()+"\",\n");	
			//out.write("		   contentType: \"application/x-www-form-urlencoded; charset=UTF-8\",//IE doesn't set charset correctly..\n");
			out.write("        dataType: 'script',\n");	
			//out.write("        done: function (e, data) {\n");	
			//out.write("            console.log(data);\n");	
			//out.write("        },\n");	
			out.write(" 	   add: function (e, data) {\n");	
			//out.write("				data.context = $('<p/>').text('Uploading...').appendTo(document.body);\n");	
			out.write("    			data.submit();\n");	
			out.write("			},\n");	
			//out.write("        progressall: function (e, data) {\n");	
			//out.write("        	console.log(data);\n");	
			//out.write("        }\n");	
			out.write("});\n");	
			out.write("</script>");
			
			if(isRequired()) {
				out.print(lab.RequiredFieldNote());
			}
			error.render(out);
		}
		out.write("</div>");
	}
	
	public void onEvent(DivRepEvent e) {
		//ignore all event if it's ignored
		if(isDisabled()) return;
		
		if(e.action.equals("upload")) {
			try {
				List<FileItem> files = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(e.request);
				onUpload(files);
			} catch (FileUploadException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		} else {
			System.out.println("unknown action: " + e.action);
		}
	}
		
	//override this to process received files
	//following is just a sample of what you can do with FileItem
	public void onUpload(List<FileItem> files) {
		for (FileItem file : files) {
			String fieldname = file.getFieldName();
			String filename = FilenameUtils.getName(file.getName());
			
			System.out.println(fieldname + " / "+ filename);
			
			try {
				InputStream filecontent = file.getInputStream();
				/*
				// just dump
				BufferedReader in = new BufferedReader(new InputStreamReader(filecontent));
				String inputLine;
				while ((inputLine = in.readLine()) != null)	System.out.println(inputLine);
				in.close();
				*/
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
