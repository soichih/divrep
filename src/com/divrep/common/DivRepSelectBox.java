package com.divrep.common;

import java.io.PrintWriter;
import java.util.Map;
import java.util.LinkedHashMap;

import org.apache.commons.lang.StringEscapeUtils;

import com.divrep.DivRep;
import com.divrep.DivRepEvent;

public class DivRepSelectBox extends DivRepFormElement<Integer>
{		
	LinkedHashMap<String, Map<Integer, String>> keyvalues_groups;
	
	String null_label = "(Please Select)";
	public void setNullLabel(String label) {
		null_label = label;
	}
	
	public DivRepSelectBox(DivRep parent, Map<Integer, String> keyvalues) {
		super(parent);
		addClass("divrep_selectbox");
		keyvalues_groups = new LinkedHashMap<String, Map<Integer, String>>();
		keyvalues_groups.put("", keyvalues);
	}
	
	public DivRepSelectBox(DivRep parent)
	{
		super(parent);
		addClass("divrep_selectbox");
		keyvalues_groups = new LinkedHashMap<String, Map<Integer, String>>();
	}
	public void addGroup(String name, LinkedHashMap<Integer, String> keyvalues) {
		keyvalues_groups.put(name, keyvalues);
	}
	
	public void setValues(Map<Integer, String> keyvalues) {
		keyvalues_groups = new LinkedHashMap<String, Map<Integer, String>>();
		keyvalues_groups.put("", keyvalues);
	}
	
	//show (Please Select) item with null value
	private Boolean hasnull = true;
	public void setHasNull(Boolean b) { hasnull = b; }
	
	
	public void render(PrintWriter out) 
	{
		out.write("<div ");
		renderClass(out);
		out.write(" id=\""+getNodeID()+"\">");
		if(!isHidden()) {
			if(getLabel() != null) {
				out.write("<label>"+StringEscapeUtils.escapeHtml(getLabel())+"</label><br/>");
			}

			
			String disabled_text = "";
			if(isDisabled()) {
				disabled_text = "disabled='disabled'";
			}
			
			String random_id = "id_" + (int)(Math.random()*10000);			
			out.write("<select id='"+random_id+"' onchange='divrep(\""+getNodeID()+"\", event, this.value);' "+disabled_text+">");
			if(hasnull) {
				out.write("<option value=\"\">"+StringEscapeUtils.escapeHtml(null_label)+"</option>");
			}
			for(String groupname : keyvalues_groups.keySet()) {
				Map<Integer, String> keyvalues = keyvalues_groups.get(groupname);
				if(groupname.length() != 0) {
					out.write("<optgroup label=\""+StringEscapeUtils.escapeJavaScript(groupname)+"\">");
				}
				for(Integer v : keyvalues.keySet()) {
					String name = keyvalues.get(v);
					String selected = "";
					if (getValue() != null) {
						if(v.equals(getValue())) {
							selected = "selected=\"selected\"";
						}
					}
					
					//if disabled, show only selected value 
					if(isDisabled() && selected.equals("")) continue;
					out.write("<option value=\""+v+"\" "+selected+">"+StringEscapeUtils.escapeHtml(name)+"</option>");
				}
				if(groupname.length() != 0) {
					out.write("</optgroup>");
				}
			}
			out.print("</select>");
			if(isRequired()) {
				out.print(lab.RequiredFieldNote());
			}
			error.render(out);
		}
		out.print("</div>");
	}
	
	public void onEvent(DivRepEvent event) {
		//ignore all event if it's ignored
		if(isDisabled()) return;
		
		try {
			setValue(Integer.parseInt((String)event.value));
			//TODO - maybe make sure that the value returned is actually one of the value in the list
			//someone could be spoofing the divrep() event to return value that isn't allowed.
		} catch (NumberFormatException e) {
			setValue(null);
		}
		setFormModified();
		validate();
	}
}
