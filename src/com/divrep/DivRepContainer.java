package com.divrep;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//user need to wrap all of divrep instantiation, initialization inside initPage()
//function so that all of that initialization will be serialized back to session
//if they are running on container that serialize session - such as Google App Engine
abstract public class DivRepContainer implements Serializable {
	DivRepRoot root;
	
	public DivRepContainer(HttpServletRequest request) throws ServletException {
		root = DivRepRoot.getInstance(request.getSession());
		String pagekey = generatePageKey(request);
		
		if(System.getProperty("divrep_invalidate_samepagekey") != null) {
			root.removePage(pagekey);
		}
		
		DivRepPage pageroot = root.initPage(pagekey);
		initPage(pageroot);
		
		//This is really the reason why this class exists.
		root.setSession(request.getSession());
	}
	
	//override this to do something more elaborate things
	protected String generatePageKey(HttpServletRequest request) {
		return request.getServletPath();
	}
	
	abstract public void initPage(DivRepPage pageroot) throws ServletException;
}
