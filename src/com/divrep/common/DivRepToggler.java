package com.divrep.common;

import java.io.PrintWriter;
import com.divrep.DivRep;
import com.divrep.DivRepEvent;
import com.divrep.i18n.Labels;

//User must override createContent() to initialize the sub content
abstract public class DivRepToggler extends DivRepFormElement {
	Labels lab = Labels.getInstance();
	
	String show_html;
	String hide_html;
	String loading_html;

	Boolean show = false;
	DivRep content = null;

	public DivRepToggler(DivRep parent) {
		super(parent);
		init();
	}
	
	//Override this to do lazy-rendering of the actual content
	abstract public DivRep createContent();
	
	private void init() {
		//set generic show/hide buttons
		show_html = "<span class=\"divrep_link\">"+lab.ShowDetail()+"</span>";
		hide_html = "<span class=\"divrep_link\">"+lab.HideDetail()+"</span>";
		loading_html = "<p class=\"divrep_loading\">"+lab.Loading()+"</p>"; //why P instead of SPAN? I can't get min-height on span and it will clip the loading icon..
	}
	
	public void setShow(Boolean _show)
	{
		show = _show;
		if(show && content == null) {
			content = createContent();
		}
	}
	public void setShowHtml(String html)
	{
		show_html = html;
	}
	public void setHideHtml(String html)
	{
		hide_html = html;
	}
	public void setLoading(String html)
	{
		loading_html = html;
	}
		
	public void render(PrintWriter out) 
	{
		if(content != null) {
			out.write("<div class=\"divrep_toggler\" id=\""+getNodeID()+"\">");
		
			//show button (hidden)
			out.write("<span id=\""+getNodeID()+"_show\" onclick=\"$('#"+content.getNodeID()+"').show();$('#"+getNodeID()+"_hide').show();$(this).hide();divrep('"+getNodeID()+"', event, 'show');\" class=\"divrep_hidden\">");
			out.write(show_html);
			out.write("</span>");
			
			//hide button
			out.write("<span id=\""+getNodeID()+"_hide\" onclick=\"$('#"+content.getNodeID()+"').hide();$(this).hide();$('#"+getNodeID()+"_show').show();divrep('"+getNodeID()+"', event, 'hide');\">");
			out.write(hide_html);
			out.write("</span>");
			
			content.render(out);
		} else {
			if(!show) {
				out.write("<div id=\""+getNodeID()+"\" onclick=\"$('#"+getNodeID()+"_loading').show();divrep('"+getNodeID()+"', event, 'show');\">");
				out.write(show_html);
				
				//loading
				out.write("<span id=\""+getNodeID()+"_loading\" class=\"divrep_hidden\">");
				out.write(loading_html);
				out.write("</span>");
			} else {
				//this should never happen
				out.write("Inconsistent toggler state... please report this bug.");
			}
		}
		out.write("</div>");
	}

	//event is fired only the first time user click "show". After that, on/off is handled solely at the client side (no update..)
	//if the content is shown by default, createContent() will be called during the first render() request.
	protected void onEvent(DivRepEvent e) 
	{
		if(e.value.equals("show")) {
			show = true;
			if(content == null) {
				content = createContent();
				redraw();	
			}
		} else if(e.value.equals("hide")) {
			show = false;
		}
	}
}
