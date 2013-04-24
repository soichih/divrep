package com.divrep.common;

import java.io.PrintWriter;
import java.util.ArrayList;

import org.apache.commons.lang.StringEscapeUtils;

import com.divrep.DivRep;
import com.divrep.DivRepEvent;
import com.divrep.i18n.Labels;
import com.divrep.validator.DivRepIValidator;

abstract public class DivRepFormElement<ValueType> extends DivRep {
	Labels lab = Labels.getInstance();
	
	//class used to render the parent div element (you can use it to render it in non-div-ish way like inline)
	//the derived element has to use this in order for it to actually take effect (of course)
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
		out.write("\"");
	}
	
	protected DivRepFormElement(DivRep parent) {
		super(parent);
	}
	
	//validation suite
	protected ArrayList<DivRepIValidator<ValueType>> validators = new ArrayList<DivRepIValidator<ValueType>>();
	public void addValidator(DivRepIValidator<ValueType> _validator) { validators.add(_validator); }
	protected ErrorDE error = new ErrorDE(this);
	protected class ErrorDE extends DivRep
	{
		public ErrorDE(DivRep _parent) {
			super(_parent);
			// TODO Auto-generated constructor stub
		}
		private String error;
		public void set(String _error) { error = _error; }
		public void clear() { error = null; }
		public String get() { return error; }
		protected void onEvent(DivRepEvent e) {
			// TODO Auto-generated method stub
			
		}
		public void render(PrintWriter out) {
			out.write("<div class=\"divrep_inline\" id=\""+getNodeID()+"\">");
			if(error != null) {
				out.write("<p class='divrep_elementerror divrep_round'><span class=\"divrep_icon_error\" style=\"float: left; margin-right: 0.3em;\"/>"+error+"</p>");
			}
			out.write("</div>");
		}
	}
	private Boolean valid = true;
	public void setValid(Boolean valid) {
		this.valid = valid;
	}
	public Boolean getValid() { 
		return valid; 
	}
	
	/*
	//set DivRep page's modified flag to true IF this element belongs to a form
	protected void setFormModified() {
		DivRep parent = getParent();
		while(parent != null) {
			if(parent instanceof DivRepForm) {
				modified(true);
				return;
			}
			parent = parent.getParent();
		}
	}
	*/

	
	
	//Override this to do custom validation (make sure to call super.validate() inside)
	//Also, you need to override this to do its own child element loop if one of the element is expected to be
	//dynamically removed. most likely, if you have dynamic elements, you are keeping up with your own list
	//of active elements. childnodes, on the other hand, doesn't handle removing of the element. Once it's there,
	//it's there forever. So unnecessary validation may occur unless you override this to only loop your own
	//elements
	public boolean validate()
	{
		boolean original = valid;
		
		//validate *all* child elements first
		boolean children_valid = true;
		for(DivRep child : childnodes) {
			if(child instanceof DivRepFormElement) { 
				DivRepFormElement element = (DivRepFormElement)child;
				if(element != null && !element.isHidden()) {
					if(!element.validate()) {
						children_valid = false;
						//continue validating other children
					}
				}
			}
		}
		
		if(children_valid) {
			//if child is valid, then let's validate myself..
			error.set(null);
			valid = true;
			
			if(value == null || value.toString().trim().length() == 0) {
				if(isRequired()) {
					error.set(lab.RequiredFieldMessage());
					valid = false;
				} 
			} else {
				//then run the optional validation
				for(DivRepIValidator<ValueType> validator : validators) {
					if(!validator.isValid(value)) {
						//bad..
						error.set(validator.getErrorMessage());
						valid = false;
						break;
					}
				}
			}
		} else {
			valid = false;
		}
		
		//why valid == false? because sometime error message can change to something else while it's set to true
		if(original != valid || valid == false) {
			error.redraw();
		}
		
		return valid;
	}
	
	private ValueType value;
	public void setValue(ValueType _value) { value = _value; }
	public ValueType getValue() { return value; }
	
	private ValueType sample_value;
	public void setSampleValue(ValueType _sample_value) { sample_value = _sample_value; }
	public ValueType getSampleValue() { return sample_value; }
	
	private String label;
	public void setLabel(String _label) { label = _label; }
	public String getLabel() { return label; }
	
	private Boolean hidden = false;
	public Boolean isHidden() { return hidden; }
	public void setHidden(Boolean b) { hidden = b; }
	
	private Boolean disabled = false;
	public Boolean isDisabled() { return disabled; }
	public void setDisabled(Boolean b) { disabled = b; }
	
	private Boolean required = false;
	public Boolean isRequired() { return required; }
	public void setRequired(Boolean b) { required = b; }

}
