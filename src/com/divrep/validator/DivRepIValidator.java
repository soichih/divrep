package com.divrep.validator;

import java.io.Serializable;

public interface DivRepIValidator<T> extends Serializable {
	
	//do the validation
	public Boolean isValid(T value);

	//return default error string such as "This must be a valid URL."
	public String getErrorMessage();
}
