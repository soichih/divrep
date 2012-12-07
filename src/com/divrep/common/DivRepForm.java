package com.divrep.common;

import java.io.PrintWriter;

import com.divrep.DivRep;
import com.divrep.DivRepEvent;
import com.divrep.DivRepEventListener;
import com.divrep.i18n.Labels;

abstract public class DivRepForm extends DivRep {
	Labels lab = Labels.getInstance();
	
	//URL to go after cancel or submit button is selected
	private String origin_url;
	
	//Child class may access these to do the rendering
	protected DivRepButton submitbutton;
	protected DivRepButton cancelbutton;
	
	//private String error;
	private Boolean valid;
	
	//you can change the button text to something other than "Submit".
	public void setSubmitLabel(String label) {
		submitbutton.setTitle(label);
	}
	
	protected void setFormModified() {
		modified(true);
	}
	
	
	//set origin_url to null if you don't want a cancel button
	public DivRepForm(DivRep parent, String _origin_url)
	{
		super(parent);
		
		origin_url = _origin_url;
		
		submitbutton = new DivRepButton(this, lab.Submit());
		submitbutton.addEventListener(new DivRepEventListener() {
			public void handleEvent(DivRepEvent e) { submit(); }
		});
		submitbutton.addClass("divrep_submit");
		submitbutton.addClass("btn");
		submitbutton.addClass("btn-primary");
		
		if(origin_url != null) {
			cancelbutton = new DivRepButton(this, lab.Cancel());
			cancelbutton.setStyle(DivRepButton.Style.BUTTON);
			cancelbutton.addClass("btn");
			cancelbutton.addEventListener(new DivRepEventListener() {
				public void handleEvent(DivRepEvent e) { 
					modified(false);
					redirect(origin_url); 
				}
			});
		} else {
			cancelbutton = null;
		}
	}
	
	//derived class can call this function to force submit.
	protected void submit()
	{
		if(validate()) {
			if(doSubmit()) {
				modified(false);
				String redirect_url = getRedirect();
				if(redirect_url == null && origin_url != null) {
					//parent hasn't decided where to go from here - redirect to origin
					redirect(origin_url);
				}
			}
		} else {
			alert(lab.PleaseCorrectAndResubmit());
		}
	}
	
	abstract protected Boolean doSubmit();
	
	public boolean validate()
	{
		redraw();
		valid = true;
		
		//validate *all* elements
		for(DivRep child : childnodes) {
			if(child instanceof DivRepFormElement) { 
				DivRepFormElement element = (DivRepFormElement)child;
				if(element != null && !element.isHidden()) {
					if(!element.validate()) {
						valid = false;
					}
				}
			}
		}
		
		return valid;
	}
	
	protected void onEvent(DivRepEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void render(PrintWriter out) 
	{
		out.print("<div id=\""+getNodeID()+"\" class='form'>");	
		for(DivRep child : childnodes) {
			//we display submit / cancel button at the end
			if(child == submitbutton || child == cancelbutton) continue;
			if(child instanceof DivRepFormElement) {
				out.print("<div class=\"divrep_form_element\">");
				child.render(out);
				out.print("</div>");
			
			} else {
				//non form element..
				child.render(out);
			}
		}
		out.print("<div class=\"form-actions\">");
		submitbutton.render(out);
		if(cancelbutton != null) {
			out.print("&nbsp;");
			cancelbutton.render(out);
		}
		out.print("</div>");
		
		out.print("</div>");
	}
}
