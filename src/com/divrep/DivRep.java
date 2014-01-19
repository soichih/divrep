package com.divrep;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringEscapeUtils;

import com.divrep.DivRepPage;
import com.divrep.DivRepRoot;

public abstract class DivRep implements Serializable {    
	private String nodeid;
	private Boolean needupdate = false;
	private DivRep parent;
	public DivRep getParent() { return parent; }
	
	public DivRep(DivRep _parent) {
		nodeid = DivRepRoot.getNewNodeID();
		
		//only DivRepRoot ctor can pass null and get away with it.. 
		if(_parent == null) {
			if(this instanceof DivRepRoot) {
				return;
			}
			throw new NullPointerException("Please pass parent DivRep pointer. (You have passed null)");
		}

		parent = _parent;
		parent.add(this);	
	}
	
	//simply travel through the hirarchy and find the DivRepPage
	public DivRepPage getPageRoot()
	{
		if(this instanceof DivRepPage) {
			return (DivRepPage)this;
		}
		return parent.getPageRoot();
	}
	
	protected ArrayList<DivRep> childnodes = new ArrayList<DivRep>();
	public void add(DivRep child)
	{
		synchronized(childnodes) {
			childnodes.add(child);
		}
	}
	public void remove(DivRep child)
	{
		synchronized(childnodes) {
			childnodes.remove(child);
		}
	}
	 
	public void alert(String msg)
	{
		js("alert('"+StringEscapeUtils.escapeJavaScript(msg)+"');");
	}
	
	//don't use this to redirect current page by setting _self to the target (use redirect instead)
	public void open(String url, String target)
	{
		//maybe we can add this later to the 3rd argument of open.
		//String options = "resizable,location,menubar,toolbar,scrollbars,status";
		js("window.open('"+url+"','"+target+"');");
	}
	
	//calling this on render() won't work if the render is called by user.. 
	public void js(String _js)
	{
		getPageRoot().addJavascript(_js);
	}
	
	//set the modified state of the current page
	public void modified(Boolean b)
	{
		getPageRoot().setModified(b);
	}
	
	//override this to set modified(true); if you are form
	protected void setFormModified() {
		if(parent != null) {
			parent.setFormModified();
		}
	}
	
	public void redirect(String url) {
		//if we emit redirect, we don't want to emit anything else.. just jump!
		getPageRoot().setRedirect(url);
	}
	
	//set container(jquery selector) to null if you want to scroll the whole page.
	public void scrollToShow(String container) {
		js("var targetOffset = $('#"+nodeid+"').offset().top;");
		if(container == null) {
			js("$('html,body').animate({scrollTop: targetOffset}, 500);");
		} else {
			js("targetOffset -= $('"+container+"').offset().top;");
			js("$('"+container+"').scrollTop(targetOffset);");
		}
	}

	private ArrayList<DivRepEventListener> event_listeners = new ArrayList<DivRepEventListener>();
	public void addEventListener(DivRepEventListener listener)
	{
		event_listeners.add(listener);
	}
	protected void notifyListener(DivRepEvent e)
	{
		//notify event listener
		for(DivRepEventListener listener : event_listeners) {
			listener.handleEvent(e);
		}
	}
	
	public String getNodeID() { return nodeid; }

	class Base64Writer extends PrintWriter
	{
		public void write(char[] buf, int off, int len) {
			// TODO Auto-generated method stub
			super.write(buf, off, len);
		}
		public void write(char[] buf) {
			//super.write(StringEscapeUtils.escapeJavaScript(String.valueOf(buf)));
			escapeAndWrite(String.valueOf(buf));
		}
		public void write(int c) {
			//super.write(StringEscapeUtils.escapeJavaScript(String.valueOf((char)c)));
			escapeAndWrite(String.valueOf((char)c));
		}
		public void write(String s) {
			//String rep = StringEscapeUtils.escapeJavaScript(s);
			//super.write(rep);
			escapeAndWrite(s);
		}
		//prevent accidentally using prinln
		public void println(String s) {
			print(s);
		}

		OutputStream out;
		public Base64Writer(PrintWriter out2) {
			super(out2);
		}
		
		private void escapeAndWrite(String str) {
			String encoded = Base64.encodeBase64String(str.getBytes());
			super.write(encoded);
			//super.write(" ");//prevent colliding with next string
		}
		
	}
	
	//DivRep calls this on *root* upon completion of event handler
	protected void outputUpdatecode(PrintWriter out)
	{
		String code = "";
		
		//find child nodes who needs update
		if(needupdate) {
			out.write("divrep_replace(\""+nodeid+"\", \"");
			Base64Writer writer = new Base64Writer(out);
			render(writer);	
			out.write("\");");
			
			//I don't need to update any of my child now..
			setNeedupdate(false);
		} else {
			//see if any of my children needs update
			for(DivRep d : childnodes) {
				d.outputUpdatecode(out);
			}
		}
	}
	
	//recursively set mine and my children's needupdate flag
	public void setNeedupdate(Boolean b)
	{
		needupdate = b;
		synchronized(childnodes) {
			for(DivRep node : childnodes) {
				node.setNeedupdate(b);
			}
		}
	}
	
	//recursively do search
	public DivRep findNode(String _nodeid)
	{
		if(nodeid.equals(_nodeid)) return this;
		synchronized(childnodes) {
			for(DivRep child : childnodes) {
				DivRep node = child.findNode(_nodeid);
				if(node != null) return node;
			}
		}
		return null;
	}
	
	abstract public void render(PrintWriter out);
		
	public void doRequest(HttpServletRequest request, HttpServletResponse response) throws IOException
	{		
		String action = request.getParameter("action");		
		if(action == null) {
			action = "event";
		}
		String value = request.getParameter("value");
		
		if(System.getProperty("debug") != null) {
			System.out.println(getClass().getName()+ " action=" + action + " nodeid=" + nodeid + " value=" + value);
		}
		
		if(action.equals("load")) {
			PrintWriter writer = response.getWriter();
			response.setContentType("text/html");
			render(writer);
		} else if(action.equals("request")) {
			//request only.. don't route to event handler
			//it could be any content type - let handler decide
			onRequest(request, response);
		} else {
			//normal divrep event
			PrintWriter out = response.getWriter();
			response.setContentType("text/html"); //I need to set this to text/html instead of text/javascript to prevent IE8/9 from downloading javascript instead of executing it.
			DivRepEvent e = new DivRepEvent(action, value, request, response);
			DivRepPage page = getPageRoot();
			
			//handle my event handler
			onEvent(e);
			notifyListener(e);
						
			//output page modified flag - I need to do this immediately or divrep_redirect call on other thread will be called first and the
			//flag will not get update in time
			if(page.isModified()) {
				out.write("divrep_modified(true);");
			} else {
				out.write("divrep_modified(false);");			
			}
			
			//if redirect is set, we don't need to do any update
			if(page.getRedirect() != null) {
				out.write("divrep_redirect(\""+getRedirect()+"\");");
				setRedirect(null);
				return;
			}

			page.outputUpdatecode(out);
			page.flushJavascript(out);//needs to emit *after* divrep_replace(s)
		}
	}
	
	//events are things like click, drag, change.. you are responsible for updating 
	//the internal state of the target div, and framework will call outputUpdatecode()
	//to emit re-load request which will then re-render the divs that are changed.
	//Override this to handle local events (for remote events, use listener)
	abstract protected void onEvent(DivRepEvent e);
	
	//set action=request to call onRequest without doing any of the usual divrep event handling / output
	protected void onRequest(HttpServletRequest request, HttpServletResponse response) {}
	
	//currently catch all for all post request made on a specific divrep node (used for upload, for example)
	protected void onPost(HttpServletRequest request, HttpServletResponse response) {}
	
	//only set needupdate on myself - since load will redraw all its children
	//once drawing is done, needudpate = false will be performec recursively
	public void redraw() {
		needupdate = true;
	}

	public void setRedirect(String url) 
	{ 
		getPageRoot().setRedirect(url); 
	}
	public String getRedirect() 
	{ 
		return getPageRoot().getRedirect();
	}
	
		
	//used to dump all divrep node as comment when findNode fails for debugging purpose.
	public void enumNodes(int depth, PrintWriter out) {

		out.write("//");
		for(int i = 0;i < depth; ++i) {
			out.write(" ");
		}
		out.write(getNodeID() + " " + getClass().getName());
		out.write("\n");
		
		for(DivRep node : childnodes) {
			node.enumNodes(depth + 1, out);
		}

	}
}
