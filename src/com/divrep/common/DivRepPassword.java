package com.divrep.common;

import java.io.PrintWriter;
import org.apache.commons.lang.StringEscapeUtils;

import com.divrep.DivRep;
import com.divrep.DivRepEvent;

public class DivRepPassword extends DivRepFormElement<String> {
	
	private int width = 400;
	public void setWidth(int _width)
	{
		width = _width;
	}
	
	public DivRepPassword(DivRep parent) {
		super(parent);
		addClass("divrep_password");
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
	
			String disabled_text = "";
			if(isDisabled()) {
				disabled_text = "disabled";
			}
			
			int random = (int)(Math.random()*10000);
			out.write("<input id=\""+getNodeID()+"_input"+random+"\" type=\"password\" style=\"width: "+width+
					"px;\" onkeyup=\"divrep('"+getNodeID()+"', event, this.value);\">");

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
		
		if(e.action.equals("keyup") || e.action.equals("change")) {
			setValue(((String)e.value).trim());
			setFormModified();
			validate();
		} else {
			System.out.println("unknown action: " + e.action);
		}
	}

}
