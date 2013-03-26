package com.divrep;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;

public class DivRepPage extends DivRep
{
	//page key logically organizes all the pages. 
	//For eample, page?id=1 and page?id=2 are different pages, but page?id=1&foo=bar and page?id=2&foo=hoge are the same.
	//In that case, one page will have key of "page?id=1" regardless of what other parameters are.
	//this mechanism is used to free previously accessed same page, or free last accessed page.
	private String pagekey;
	public String getPageKey() { return pagekey; }
	//used for DivRepRoot to identify which page is last accessed and free it necessary.
	private Date last_accessed;
	public void setAccessed() {
		last_accessed = new Date();
	}
	public Date getLastAccessed() {
		return last_accessed;
	}
	
	public DivRepPage(DivRep _parent, String _pagekey) {
		super(_parent);
		pagekey = _pagekey;
		setAccessed();
	}
	
	//for some reason, I need to wrap the javascript inside Timeout or firefox causes select boxes to not work if 
	//alert() or prompt() type javascript is invoked when there are more than 1 select box on the page
    private StringBuffer js = new StringBuffer();
    public void addJavascript(String _js) { js.append(_js); }
	public void flushJavascript(PrintWriter out) {
		if(js.length() > 0) {
			out.write("setTimeout(\"");
			out.write(StringEscapeUtils.escapeJavaScript(js.toString()));
			out.write("\", 100);");
			js = new StringBuffer();
		}
	}
	
	//set the page to be in "modified" state - used by form (and maybe other in the future)
	//this control if the redirect should happen immediaterly or not
	private Boolean modified = false;
	public void setModified(Boolean b) {
		modified = b;
	}
	public Boolean isModified() { return modified; }
	
	@Override
	protected void onEvent(DivRepEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(PrintWriter out) {
		// TODO Auto-generated method stub
		
	}   
	
	private String redirect_url;
	public void setRedirect(String url) { redirect_url = url; }
	public String getRedirect() { return redirect_url; }
	

	//Servlet objects that event handles can use
	HttpServletRequest http_request;
	HttpServletResponse http_response;
	public HttpServletRequest getCurrentHttpServletRequest() {
		return http_request;
	}
	public void setCurrentHttpServletRequest(HttpServletRequest http_request) {
		this.http_request = http_request;
	}
	public HttpServletResponse getCurrentHttpServletResponse() {
		return http_response;
	}
	public void setCurrentHttpServletResponse(HttpServletResponse http_response) {
		this.http_response = http_response;
	}
}