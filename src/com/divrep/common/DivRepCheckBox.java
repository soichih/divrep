package com.divrep.common;

import java.io.PrintWriter;

import com.divrep.DivRep;
import com.divrep.DivRepEvent;

import org.apache.commons.lang.StringEscapeUtils;

public class DivRepCheckBox extends DivRepFormElement<Boolean> {
	
	public DivRepCheckBox(DivRep parent) {
		super(parent);
		setValue(false);
		addClass("divrep_checkbox");
	}
	
	public void render(PrintWriter out) {
		out.write("<div ");
		renderClass(out);
		out.write("id=\""+getNodeID()+"\">");
		if(!isHidden()) {
			String checked = "";
			if(getValue() == true) { //value should never be null by design
				checked = "checked=checked";
			}
			out.write("<input");
			if(isDisabled()) {
				out.write(" disabled");
			}
			out.write(" type='checkbox' onclick='divrep(\""+getNodeID()+"\", event, this.checked);' "+checked+"/>");
			if(getLabel() != null) {
				out.write(" <label ");
				if(isDisabled()) {
					out.write("class=\"divrep_disabled\"");
				}
				out.write(">"+StringEscapeUtils.escapeHtml(getLabel())+"</label>");
			}
			error.render(out);
		}
		out.write("</div>");
	}

	//override setValue to handle null case..
	public void setValue(Boolean _value)	
	{ 
		if(_value == null) {
			//I went back and forth if I should throw exception, but since checkbox is the one
			//that deviates from the rest that handles "null state", so let's ignore it..
			return;
		}
		super.setValue(_value); 
	}
	
	public void onEvent(DivRepEvent e) {
		if(isDisabled()) {
			//if disabled, we shouldn't get any event from this. -- user tinckering with HTML??
			return;
		}
		if(((String)e.value).compareTo("true") == 0) {
			setValue(true);
		} else {
			setValue(false);
		}
		setFormModified();
		validate(); //I know checkbox almost never needs any validation, but just for consistency sake..
	}
}
