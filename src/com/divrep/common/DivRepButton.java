package com.divrep.common;

import java.io.PrintWriter;
import java.util.HashSet;

import org.apache.commons.lang.StringEscapeUtils;

import com.divrep.DivRep;
import com.divrep.DivRepEvent;
import com.divrep.DivRepEventListener;

public class DivRepButton extends DivRepFormElement {
	String title;
	
	String tooltip = null;
	public void setToolTip(String t) {
		tooltip = t;
	}

	
	static public enum Style { BUTTON, ALINK, IMAGE, HTML };
	Style style = Style.BUTTON;
	public void setStyle(Style _style) { style = _style; }
	
	Boolean confirm = false;
	String confirm_message = null;
	public void setConfirm(Boolean b, String message) {
		confirm = b;
		confirm_message = message;
	}
	
	//if the button style is IMAGE, _title will be used as URL
	public DivRepButton(DivRep parent, String _title) {
		super(parent);
		setTitle(_title);
		addClass("divrep_button");
	}
	public void setTitle(String _title) {
		title = _title;
	}
	public void setDisabled(Boolean b) {
		super.setDisabled(b);
		if(b) {
			addClass("disabled");
		} else {
			removeClass("disabled");
		}
	}
	
	public void render(PrintWriter out) {
		if(isHidden()) return; //TODO - not sure if we need to display display: none button

		String js = "if(!$(this).hasClass(\"divrep_processing\")) { divrep(\""+getNodeID()+"\", event); } return false;";
		if(confirm) {
			js = "var answer = confirm(\""+confirm_message+"\");if(answer) {"+js+"} else return false;";
		}
		
		String tip = "";
		if(tooltip != null) {
			tip = "data-toggle=\"tooltip\" title=\""+StringEscapeUtils.escapeHtml(tooltip)+"\"";
		}
		
		switch(style) {
		case BUTTON:
			out.write("<input "+tip);
			renderClass(out);
			out.write("type='button' id='"+getNodeID()+"' value='"+StringEscapeUtils.escapeHtml(title)+"'");
			if(isDisabled()) out.write(" disabled");
			else out.write(" onclick='"+js+"'");
			out.write(" />");
			break;
		case ALINK:
			out.write("<a "+tip);
			renderClass(out);
			out.write("href='javascript:void(0)' id='"+getNodeID()+"'");
			if(!isDisabled()) out.write(" onclick='"+js+"'");
			out.write(">");
			out.write(StringEscapeUtils.escapeHtml(title)+"</a>");
			break;
		case IMAGE:
			out.write("<a "+tip);
			renderClass(out);
			out.write("href='javascript:void(0)' id='"+getNodeID()+"'");
			if(!isDisabled()) out.write(" onclick='"+js+"'");
			out.write(">");
			out.write("<img src='"+StringEscapeUtils.escapeHtml(title)+"' alt='button'/></a>");
			break;
		case HTML:
			//TODO - need to add support for tooltip
			out.write("<div id='"+getNodeID()+"'");
			renderClass(out);
			if(!isDisabled()) out.write(" onclick='"+js+"'");
			out.write(">");
			out.write(title);
			out.write("</div>");
			break;
		}
	}
	
	protected void onClick(DivRepEvent e) {}

	//final to prevent child class to override onEvent directly.. Use onClick instead.. 
	protected final void onEvent(DivRepEvent e) {
		if(isDisabled()) return;
		onClick(e);
	}
	
	@Deprecated 
	//danger: use onClick instead (which doesn't fire if button is disabled)
	public final void addEventListener(DivRepEventListener listener) {
		super.addEventListener(listener);
	}

}
