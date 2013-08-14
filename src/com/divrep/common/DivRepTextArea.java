package com.divrep.common;

import java.io.PrintWriter;

import org.apache.commons.lang.StringEscapeUtils;

import com.divrep.DivRep;
import com.divrep.DivRepEvent;

public class DivRepTextArea extends DivRepFormElement<String>  {
	
	//set width to 0 for 100% width
	private int width = 400;
	public void setWidth(int _width) { width = _width; }
	private int height = 100;
	public void setHeight(int _height) { height = _height; }
	
	public DivRepTextArea(DivRep parent) { 
		super(parent);
		addClass("divrep_textarea");
	}
	
	public void onEvent(DivRepEvent e) {
		//ignore all event if it's ignored
		if(isDisabled()) return;
		
		setValue(((String)e.value).trim());
		setFormModified();
		validate();
	}
	
	public void render(PrintWriter out) {
		out.write("<div ");
		renderClass(out);
		out.write("id=\""+getNodeID()+"\">");
		if(!isHidden()) {
			if(getLabel() != null) {
				out.print("<label>"+StringEscapeUtils.escapeHtml(getLabel())+"</label><br/>");
			}
			String current_value;
			if(getValue() == null) {
				current_value = "";
			} else {
				current_value = getValue();
				//current_value = StringEscapeUtils.escapeHtml(value);
			}
			
			String sample;
			if(getSampleValue() == null) {
				sample = "";
			} else {
				sample = getSampleValue();
				//sample = StringEscapeUtils.escapeHtml(sample_value);
			}
			
			String disabled_text = "";
			if(isDisabled()) {
				disabled_text = "disabled";
			}
			
			int random = (int)(Math.random()*10000);
			out.print("<textarea id=\""+getNodeID()+"_input"+random+"\" style='");
			out.print("height: "+height+"px;");
			if(width == 0) {
				out.print("width: 100%;");
			} else {
				out.print("width: "+width+"px;");
			}
			out.print("' onchange='divrep(\""+getNodeID()+"\", event, this.value);' sample=\""+
					StringEscapeUtils.escapeHtml(sample)+"\" "+disabled_text+">");
			out.print(StringEscapeUtils.escapeHtml(current_value));
			out.print("</textarea>");

			out.write("<script type=\"text/javascript\">\n");

			out.write("var input = $(\"#"+getNodeID()+"_input"+random+"\");\n");

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
			out.write("</script>");
		
			if(isRequired()) {
				//out.print(" * Required");
				out.print(lab.RequiredFieldNote());
			}
			error.render(out);
		}
		out.print("</div>");
	}
}
