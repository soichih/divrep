package com.divrep.validator;

import java.io.Serializable;

public class DivRepLengthValidator implements DivRepIValidator<String>
{
	int min, max;
	String message;
	
	public DivRepLengthValidator(int min, int max) {
		this.min = min;
		this.max = max;
	}
	
	public Boolean isValid(String value) {
		int len = value.trim().length();
		
		if(len < min) {
			message = "Too short. Minimum of "+min+" characters required. Currently it is "+len+" characters";
			return false;
		}
		if(len > max) {
			message = "Too long. Up to "+max+" characters allowed. Currently it is "+len+" characters";
			return false;
		}
		return true;
	}
	
	public String getErrorMessage()
	{
		return message;
	}
}
