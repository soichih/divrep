package com.divrep.common;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaFactory;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.apache.commons.lang.StringEscapeUtils;

import com.divrep.DivRep;
import com.divrep.DivRepEvent;

public class DivRepReCaptcha extends DivRepFormElement<String>  {
	
	private ReCaptcha recaptcha;
	private boolean validated = false;
	private String public_key;
	
	private String challenge;
	private String uresponse;
	
	public DivRepReCaptcha(DivRep parent, String public_key, String private_key) { 
		super(parent);
		//addClass("divrep_textarea");
		recaptcha = ReCaptchaFactory.newReCaptcha(public_key, private_key, false);
		this.public_key = public_key;
	}
	
	public void onEvent(DivRepEvent e) {
		//ignore all event if it's ignored
		if(isDisabled()) return;
	    challenge = e.action;
	    uresponse = e.value;
	    validate();
	}
	
	public void render(PrintWriter out) {
		out.write("<div ");
		renderClass(out);
		out.write("id=\""+getNodeID()+"\">");
		if(!isHidden()) {
			if(validated == false) {
				out.write("<script src=\"//www.google.com/recaptcha/api/js/recaptcha_ajax.js\" type=\"text/javascript\"></script>");
				out.write("<div id=\""+getNodeID()+"_recaptcha\">");
				out.print(recaptcha.createRecaptchaHtml(null, null));
				/*
				if(isRequired()) {
					out.print(lab.RequiredFieldNote());
				}
				*/
				out.write("</div>"); //_recaptcha
				error.render(out);
				out.write("<script>");
				out.write("Recaptcha.create('"+public_key+"', '"+getNodeID()+"_recaptcha', {"
						+ "callback: function() {        "
						+ 	"$('#recaptcha_response_field').change(function(e){"
						+ 	"divrep('"+getNodeID()+"', e, Recaptcha.get_response(), Recaptcha.get_challenge());"
						+ 	"});    "
						+ "}"
						+ "});");
				out.write("</script>");
			} else {
				out.print("<p>You are human!</p>");
			}
		}
		out.print("</div>");
	}
	
	public boolean validate()
	{
		//once validated - always validated
		if(validated == true) return true;
		
		if(challenge == null || uresponse == null) {
	    	error.set("This is a required field.");
	    	error.redraw();
	    	return false;
		} else {
			//let's do validation for real
		    HttpServletRequest req = getPageRoot().getCurrentHttpServletRequest();
		    ReCaptchaResponse reCaptchaResponse = recaptcha.checkAnswer(req.getRemoteAddr(), challenge, uresponse);
		    if (reCaptchaResponse.isValid()) {
		    	validated = true;
		    	redraw();
		    } else {
		    	error.set("Opps! Please try again!");
		    	redraw(); //redraw the whole thing to get new captcha
		    }
		    return validated;
		}
	}
}
