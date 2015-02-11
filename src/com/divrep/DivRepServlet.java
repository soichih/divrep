package com.divrep;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;

//This class is to process all DivRep Event that comes from DivRep client library.
//In order to avoid mistake by user, I am setting this final class.
final public class DivRepServlet extends HttpServlet { 
	private static final long serialVersionUID = 1L; 
	
    public DivRepServlet() {
        super();
        String delay = System.getProperty("divrep.delay");
		if(delay != null) {
			System.out.println("WARNING: divrep.delay is set to " + delay);
		}
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{	
		//simulate network latency
		String delay = System.getProperty("divrep.delay");
		if(delay != null) {
			try {
				Thread.sleep(Integer.valueOf(delay));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		response.setHeader("Cache-Control","no-cache, must-revalidate");
		response.setCharacterEncoding("UTF-8");
		
		//find the target node and process action
		String nodeid = request.getParameter("nodeid");	
		
		//get this session's divrep root and find the target node
		DivRepRoot root = DivRepRoot.getInstance(request.getSession());
		//synchronized(root) {
		DivRep div = root.findNode(nodeid);
		if(div == null) {
			//ooops.. maybe we lost something here?
			response.setContentType("text/javascript");
			PrintWriter writer = response.getWriter();
			writer.println("//couldn't find nodeid: " + nodeid);
			root.enumNodes(0, writer);
			writer.print("alert('Re-syncing with server...'); window.location.reload();");			
		} else {
			//update last accesed time
			DivRepPage pageroot = div.getPageRoot();
			pageroot.setAccessed();
			
			pageroot.setCurrentHttpServletRequest(request);
			pageroot.setCurrentHttpServletResponse(response);
			
			//dipatch divrep event handler
			try {
				div.doRequest(request, response);
			} catch(Exception e) {
				response.getWriter().print("alert('Oops.. Something went wrong while processing your request. Please contact your support staff.');");
				response.getWriter().print("console.log('"+StringEscapeUtils.escapeJavaScript(e.toString())+"');");
			}
			
			//reset session (for Google App Engine - serialization)
			root.setSession(request.getSession());
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
