package com.divrep;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DivRepEvent implements Serializable {
	public String action;
	public String value;
	public HttpServletRequest request;
	public HttpServletResponse response;
	public DivRepEvent(String _action, String _value, HttpServletRequest request, HttpServletResponse response) {
		action = _action;
		value = _value;
		this.request = request;
		this.response = response;
	}
}
