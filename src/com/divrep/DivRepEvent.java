package com.divrep;

import java.io.Serializable;

public class DivRepEvent implements Serializable {
	public String action;
	public String value;
	public DivRepEvent(String _action, String _value) {
		action = _action;
		value = _value;
	}
}
