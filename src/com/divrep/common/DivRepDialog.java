package com.divrep.common;

import java.io.PrintWriter;
import com.divrep.i18n.Labels;
import org.apache.commons.lang.StringEscapeUtils;
import com.divrep.DivRep;
import com.divrep.DivRepEvent;

//This DivRep requires jQuery-UI
abstract public class DivRepDialog extends DivRep {

	public class DivRepDialogException extends RuntimeException {
		String msg;
		public DivRepDialogException(String msg) {
			this.msg = msg;
		}
	}
	
	Boolean has_cancelbutton = false;
	Boolean enter_to_submit = false;
	
	Boolean rendered = false;
	Integer height = null;
	Integer width = null;
	String title = "untitled";
	
	public void setHeight(Integer h) { height = h; }
	public void setWidth(Integer w) { width = w; }
	
	public void setTitle(String title) {
		this.title = title;
		if(rendered) {
			//if already rendered, I need to dynamically update title
			js("$(\"#"+getNodeID()+"_dialog\").dialog('option', 'title', \""+StringEscapeUtils.escapeHtml(title)+"\");");
		} 
	}

	public DivRepDialog(DivRep parent) {
		super(parent);
	}
	
	public void setHasCancelButton(Boolean has_cancelbutton) {
		if(rendered) {
			throw new DivRepDialogException("Can't set has_cancelbuttotn flag after the dialog is rendered.");
		}
		this.has_cancelbutton = has_cancelbutton;
	}
	public void setEnterToSubmit(Boolean enter_to_submit) {
		if(rendered) {
			throw new DivRepDialogException("Can't set enter_to_submit flag after the dialog is rendered.");
		}
		this.enter_to_submit = enter_to_submit;
	}
	
	public void open() {
		js("if(!$(\"#"+getNodeID()+"_dialog\").dialog('isOpen')) $(\"#"+getNodeID()+"_dialog\").dialog('open');");
	}
	
	public void close() {
		js("if($(\"#"+getNodeID()+"_dialog\").dialog('isOpen')) $(\"#"+getNodeID()+"_dialog\").dialog('close');");
	}

	protected void onEvent(DivRepEvent e) {
		if(e.value.equals("cancel")) {
			onCancel();
		} else if(e.value.equals("submit")) {
			onSubmit();
		}
	}
	
	//dialog can't be redrawn it must be rendered once and only once.
	//make sure to render this to where it's never redrawn or it will misbehaves - mostly due to jQuery-UI limitation..
	public void redraw() {
		alert("DivRepDialog can't be redrawn.");
	}

	abstract public void onCancel();
	abstract public void onSubmit();
	
	public void render(PrintWriter out) {
		//dialog frame can be rendered only once
		if(!rendered) {
			out.write("<div id=\""+getNodeID()+"\">");
	
			out.write("<div class=\"divrep_hidden\" id=\""+getNodeID()+"_dialog\" title=\""+StringEscapeUtils.escapeHtml(title)+"\">");
			//show all child divrep components
			for(DivRep child : childnodes) {
				child.render(out);
			}
			out.write("</div>");
	
			//jQuery Dialog fires close event when user click ok.
			//it is also fired when user cancel the dialog. we are capturing close event to run onCancel event,
			//so in order to avoid onCancel event to fire when user click ok due to close event firing, 
			//I keep up with ok button via this attribute
			
			out.write("<script type=\"text/javascript\">");			
			out.write("$(function() {");
				String width_str = (width==null?"'auto'":width.toString());
				String height_str = (height==null?"'auto'":height.toString());
				//for some reason, I need to wrap divrep() call for close event around timeout... or Firefox will blow up.
				out.write("$(\"#"+getNodeID()+"_dialog\").dialog({open: function(event, ui) {$('#"+getNodeID()+"').removeAttr('oked');}, close: function(event, ui) {if(!$('#"+getNodeID()+"').attr('oked')) setTimeout(\"divrep('"+getNodeID()+"', null, 'cancel');\", 0);}, autoOpen: false, closeOnEscape: true, bgiframe: true, resizable: false, width: "+width_str+", height: "+height_str+", modal: true");
			
				out.write(",buttons: {");
				out.write(Labels.getInstance().OK() + ": function() {$('#"+getNodeID()+"').attr('oked', true); divrep('"+getNodeID()+"', null, 'submit');}");
				if(has_cancelbutton) {
					out.write("," + Labels.getInstance().Cancel() + ": function() {divrep('"+getNodeID()+"', null, 'cancel'); $(this).dialog('close');}");			
				}
				out.write("}");
			
				out.write("});");
				
				if(enter_to_submit) {
					//This feature doesn't work on IE
					out.write("if(!$.browser.msie) {");
						out.write("$(\"#"+getNodeID()+"_dialog\").bind(\"keydown\", function(e) {if (e.keyCode == 13) {$(\":focus\").blur(); $('#"+getNodeID()+"').attr('oked', true); divrep('"+getNodeID()+"', e, 'submit');}});");
					out.write("}");
				}
				
			out.write("});");
			out.write("</script>");			

			out.write("</div>");
			rendered = true;
		}
	}

}
