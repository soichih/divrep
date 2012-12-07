package com.divrep.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;

import com.divrep.DivRep;
import com.divrep.DivRepEvent;

public class DivRepTextBox extends DivRepFormElement<String> {
	
	private int width = 400;
	
	private String event_mode = "onchange"; //it could be set to use onkeyup 
	public void setEventMode(String event_mode) {
		this.event_mode = event_mode;
	}
	
	public void setWidth(int _width)
	{
		width = _width;
	}
	
	public DivRepTextBox(DivRep parent) {
		super(parent);
		addClass("divrep_textbox");
	}
	
	//this takes effect next time you render the textbox
	private HashSet<String> autocomplete_values = null;
	public void setAutoCompleteValues(HashSet<String> autocomplete_values) {
		this.autocomplete_values = autocomplete_values;
	}
	
	//this takes effect next time you render the textbox
	public interface SearchEventListener extends Serializable {
		public ArrayList<String> handleEvent(String term);
	}
	private SearchEventListener search_event_listener = null;
	public void setSearchEventListener(SearchEventListener search_event_listener) {
		this.search_event_listener = search_event_listener;
	}

	public void render(PrintWriter out) {
		out.print("<div ");
		renderClass(out);
		out.write("id=\""+getNodeID()+"\">");
		if(!isHidden()) {
			if(getLabel() != null) {
				out.print("<label>"+StringEscapeUtils.escapeHtml(getLabel())+"</label><br/>");
			}
			
			String current_value = getValue();
			if(current_value == null) {
				current_value = "";
			}
			String sample = getSampleValue();
			if(sample == null) {
				sample = "";
			}
			
			String disabled_text = "";
			if(isDisabled()) {
				disabled_text = "disabled";
			}
			
			int random = (int)(Math.random()*10000);
			out.write("<input id=\""+getNodeID()+"_input"+random+"\" type=\"text\" style=\"width: "+width+
					"px;\" "+event_mode+"=\"divrep('"+getNodeID()+"', event, this.value);\" sample=\""+
					StringEscapeUtils.escapeHtml(sample)+"\"/ "+disabled_text+">");
			out.write("<script type=\"text/javascript\">\n");

				//out.write("$(document).ready(function() {");
			
				out.write("var input = $(\"#"+getNodeID()+"_input"+random+"\");\n");

				//out.write("input.val(\""+escapeDoubleQuotes(current_value)+"\");");
				out.write("input.val(\""+StringEscapeUtils.escapeJavaScript(current_value)+"\");");
				out.write("if(input.val() == \"\") {\n");
				out.write(" input.addClass(\"divrep_sample\");\n");
				out.write(" input.val(input.attr('sample'));\n");
				out.write("}\n");
				
				out.write("input.focus(function() {");
				out.write("	if($(this).hasClass(\"divrep_sample\")) {");
				out.write(" 	this.value = \"\";");
				out.write("		$(this).removeClass(\"divrep_sample\");");
				out.write(" }");
				out.write("});");
				
				out.write("input.blur(function() {");
				out.write(" if(this.value == \"\") {");
				out.write("		$(this).addClass(\"divrep_sample\");");
				out.write(" 	$(this).val($(this).attr('sample'));");
				out.write(" }");
				out.write("});");
				
				if(autocomplete_values != null) {
					StringBuffer source = new StringBuffer();
					for(String value : autocomplete_values) {
						if(source.length() > 0) {
							source.append(", ");
						}
						source.append("\"" + StringEscapeUtils.escapeJavaScript(value) + "\"");
					}
					out.write("input.autocomplete({source: ["+source.toString()+"], delay: 0 });");

					replaceOnChange(out);
				} else if(search_event_listener != null) {
					out.write("input.autocomplete({source: function(request, response) {");
					out.write("$.ajax({url: 'divrep', dataType: 'json', data: { nodeid: '"+getNodeID()+"', action: 'request', value : request.term }, success: response}); }");
					out.write(", delay: 200 });");
					replaceOnChange(out);
				}
								
				//out.write("});");//document.ready
			out.write("</script>");
			
			if(isRequired()) {
				out.print(lab.RequiredFieldNote());
			}
			error.render(out);
		}
		out.write("</div>");
	}
	
	private void replaceOnChange(PrintWriter out) {
		//This is used by autocomplete. autocomplete doesn't override onchange correctly, and this hack will
		//replace the onchange event with autocompleteselect event and masqerade it as change event
		out.write("input.attr('onchange', '');");
		out.write("input.blur(function() {divrep('"+getNodeID()+"', null, $(this).val(), 'change'); });");
		out.write("input.bind(\"autocompleteselect\", function(event, ui) {divrep('"+getNodeID()+"', event, ui.item.value, 'change'); });");
		
		//disable return key - jquery-ui native autocomplete really sucks...
		//user has to either enter return key or tab to select value, and user really shouldn't
		//be using return key to enter value since it could interfere with dialog OK behavior.
		out.write("input.bind(\"keydown\", function(e,ui) {if (e.keyCode == 13) return false;});");
	}
	
	public void onEvent(DivRepEvent e) {
		//ignore all event if it's ignored
		if(isDisabled()) return;
		
		if(e.action.equals("change") || e.action.equals("keyup")) {
			setValue(((String)e.value).trim());
			setFormModified();
			validate();
		} else {
			System.out.println("unknown action: " + e.action);
		}
	}
	
	public void onRequest(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("json");
		PrintWriter out;
		try {
			out = response.getWriter();
			ArrayList<String> values = search_event_listener.handleEvent(request.getParameter("value"));
			out.write("[");
			if(values != null) {
				boolean first = true;
				for(String value : values) {
					if(!first) {
						out.write(", ");
					}
					first = false;
					out.write("\"" + StringEscapeUtils.escapeJavaScript(value) + "\"");
				}
			}
			out.write("]");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Double getValueAsDouble()
	{
		if(getValue() == null) return null;
		if(getValue().length() == 0) return null;
		try {
			Double d = Double.parseDouble(getValue());
			return d;
		} catch(NumberFormatException e) {
			return null;
		}
	}
}
