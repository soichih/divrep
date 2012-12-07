package com.divrep.common;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;

import com.divrep.DivRep;
import com.divrep.DivRepEvent;
import com.divrep.common.DivRepTextBox;

public class DivRepMoneyAmount extends DivRepTextBox {
	static NumberFormat nf = NumberFormat.getCurrencyInstance();
	static NumberFormat pf = NumberFormat.getPercentInstance();
	Boolean allow_percentage;
	
	public DivRepMoneyAmount(DivRep parent) {
		super(parent);
		allow_percentage = false;
	}
	
	public void allowPercentage(Boolean allow_percentage) {
		this.allow_percentage = allow_percentage;
	}

	public void onEvent(DivRepEvent e) {
		String value = e.value.trim();
		setValue("");
		try {
			BigDecimal b = new BigDecimal(value);
			setValue(nf.format(b));
		} catch(NumberFormatException ne) {
			try {
				Number n = nf.parse(value);
				setValue(nf.format(n));
			} catch (ParseException pe1) {
				if(allow_percentage) {
					try {
						Number n = pf.parse(value);
						setValue(pf.format(n));
					} catch(ParseException pe2) {
						//any other idea?
					}
				}
			}
		}
		
		setFormModified();
		validate();
		
		redraw();
	}
	
	private static final long serialVersionUID = 1L;

}
