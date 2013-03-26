package com.divrep.common;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.lang.StringEscapeUtils;

import com.divrep.DivRep;
import com.divrep.DivRepEvent;

public class DivRepButton extends DivRep {
	String title;
	
	String tooltip = null;
	public void setToolTip(String t) {
		tooltip = t;
	}
	
	private ArrayList<String> classes = new ArrayList<String>();
	public void addClass(String _class) {
		classes.add(_class);
	}
	protected void renderClass(PrintWriter out) {
		out.write("class=\"");
		for(String _class : classes) {
			out.write(_class);
			out.write(" ");
		}
		out.write("\" ");
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
	public void render(PrintWriter out) {
		//TODO -- support Hidden flag

		String js = "if(!$(this).hasClass(\"divrep_processing\")) { divrep(\""+getNodeID()+"\", event); } return false;";
		if(confirm) {
			js = "var answer = confirm(\""+confirm_message+"\");if(answer) {"+js+"} else return false;";
		}
		
		String tip = "";
		if(tooltip != null) {
			tip = "title=\""+StringEscapeUtils.escapeHtml(tooltip)+"\"";
		}
		
		switch(style) {
		case BUTTON:
			out.write("<input "+tip);
			renderClass(out);
			out.write("type='button' id='"+getNodeID()+"' onclick='"+js+"' value='"+StringEscapeUtils.escapeHtml(title)+"' />");
			break;
		case ALINK:
			out.write("<a "+tip);
			renderClass(out);
			out.write("href='' id='"+getNodeID()+"' onclick='"+js+"'>"+
				StringEscapeUtils.escapeHtml(title)+"</a>");
			break;
		case IMAGE:
			out.write("<a ");
			renderClass(out);
			out.write("href='' id='"+getNodeID()+"' onclick='"+js+"'><img "+tip+" src='"+StringEscapeUtils.escapeHtml(title)+"' alt='button'/></a>");
			break;
		case HTML:
			//TODO - need to add support for tooltip
			out.write("<div id='"+getNodeID()+"' onclick='"+js+"' ");
			renderClass(out);
			out.write(">");
			out.write(title);
			out.write("</div>");
			break;
		}
	}

	//user should override this to intercept click event.
	//or use event listener
	protected void onEvent(DivRepEvent e) {}

}
