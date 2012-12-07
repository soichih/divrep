package com.divrep.common;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;

import com.divrep.DivRep;
import com.divrep.DivRepEvent;

//You need to include jQuery-UI js/css files like following
/*
		out.write("<script type=\"text/javascript\" src=\"jquery-ui-1.7.2.custom.min.js\"></script>");
		out.write("<link href=\"css/ui-lightness/jquery-ui-1.7.2.custom.css\" rel=\"stylesheet\" type=\"text/css\"/>");
 */
public class DivRepDate extends DivRepFormElement<Date>
{
	DateFormat df_incoming;
	//SimpleDateFormat df_incoming = new SimpleDateFormat("M/d/yyyy");
	
	//this is just the format that jquery-datepicker uses to communicate the date
	String jquery_format = "m/d/yy";
	
	String minDate = null;
	public DivRepDate(DivRep parent) {
		super(parent);
		setValue(new Date());//today
		getValue().setTime((getValue().getTime() / (1000L*60)) * (1000L*60)); //round to nearest minute
		
		df_incoming = DateFormat.getDateInstance(DateFormat.SHORT);
		if(!(df_incoming instanceof SimpleDateFormat)) {
			//if user is using some weird date format, revert to U.S. standard format..
			//so that I can communicate with jQuery format
			df_incoming = new SimpleDateFormat("M/d/yy");
		}
		
		jquery_format = convertToJQueryFormat((SimpleDateFormat)df_incoming);
	}

	public String convertToJQueryFormat(SimpleDateFormat format) {
		String java_format = format.toPattern();
		String jquery_format = java_format;
		jquery_format = jquery_format.replaceAll("yy", "y");
		jquery_format = jquery_format.replaceAll("MM", "mm");
		jquery_format = jquery_format.replaceAll("M", "m");
		//alert("Converting from [" + java_format + "] to [" + jquery_format + "]");
		return jquery_format;
	}

	public void setMinDate(Date d) {
		minDate = "new Date("+d.getTime()+")";
	}
	
	protected void onEvent(DivRepEvent e) {
		if(e.action.equals("select")) {
			//use jquery datepicker incoming format to parse
			try {
				setValue(df_incoming.parse((String)e.value));
			} catch(ParseException e2) {
				//this shouldn't really happen.. but..
				alert(e2.getMessage());
			}
		} else {
			//use locale format
			try {
				setValue(df_incoming.parse((String)e.value));
				
				//Although user can change the text value, internally, datepicker still holds the previous value (maybe because it can't parse it?)
				//I tried setting it by setDate function, but this doesn't quite work.. I am giving this up for now.
				//js("$(\"#"+getNodeID()+" .datepicker\").datepicker('setDate', new Date("+value.getTime()+")).val('"+df.format(value)+"');");
			} catch (ParseException e1) {
				alert(e1.getMessage() + ". " + lab.InvalidDateFormat());
			}
		}
		setFormModified();
		redraw();
	}
	
	//sometime datepicker remains open - like canceling the dialog that contains the datepicker...
	//you can call this function to force it to close it
	public void close() {
		js("$(\"#"+getNodeID()+" .datepicker\").datepicker('hide', 'fast');");
	}

	public void render(PrintWriter out) {
		out.write("<div class=\"divrep_date\" id=\""+getNodeID()+"\">");
		if(!isHidden()) {
			if(getLabel() != null) {
				out.print("<label>"+StringEscapeUtils.escapeHtml(getLabel())+"</label><br/>");
			}
		
			String str = df_incoming.format(getValue());
			out.write("<input type=\"text\" class=\"datepicker\" value=\""+str+"\" onchange=\"divrep('"+getNodeID()+"', null, $(this).val());\"/>");	
			
			//setup the datepicker
			out.write("<script type=\"text/javascript\">");
			out.write("$(document).ready(function() { $(\"#"+getNodeID()+" .datepicker\").datepicker({" +
				"onSelect: function(value) {divrep('"+getNodeID()+"', null, value, 'select');},"+
				"dateFormat: '"+jquery_format+"',"+
				"showOn: 'button',"+
				"beforeShow: function() {$('#ui-datepicker-div').maxZIndex(); },"+
				"changeYear: true,"+
				"constrainInput: false,"+
				"showOtherMonths: true,"+
				"selectOtherMonths: true,"+
				"defaultDate: new Date("+getValue().getTime()+"),"+
				"changeMonth: true"
			);
			out.write("});});");
			out.write("</script>");
			
			error.render(out);
		}
		out.write("</div>");
	}
}