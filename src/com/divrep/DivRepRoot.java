package com.divrep;

import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.http.HttpSession;

//Each session contains a single DivRepRoot instance.

public class DivRepRoot extends DivRep
{   
	/*
	static private int next_nodeid = 0;
	static public String getNewNodeID()
	{
		String nodeid = "divrep_"+next_nodeid;
		++next_nodeid;
		return nodeid;
	}
	*/
	static public String getNewNodeID()
	{
		//1) html id must start with letters not number.. 
		//2) node() is sometimes used to construct a unique functio name. function name can't contain '-' char - so we are removing it
		return "divrep_"+UUID.randomUUID().toString().replace("-", "");
	}
	
	public DivRepRoot() {
		super(null);
	}
	/*
	static public DivRepPage initPageRoot(HttpServletRequest request) {
		DivRepRoot root = DivRepRoot.getInstance(request.getSession());
		DivRepPage pageroot = root.initPage(request.getServletPath());
		return pageroot;
	}
	*/
	
	//for each session, there is only one DivRepRoot.
	static public DivRepRoot getInstance(HttpSession session)
	{
    	DivRepRoot root  = (DivRepRoot) session.getAttribute("divrep");
    	if(root == null) {
    		root = new DivRepRoot();
    	}
    	
    	//for non-serialized servlet, this ensures that any subsequent access to root will be stored on session
    	//for serialized servlet (lile Google App engine), user needs to reset session in order for divrep data structure
    	//to stick
    	session.setAttribute("divrep", root);
    	
    	return root;
	}
	
	//throws IllegalStateException - if this method is called on an invalidated session
	public void setSession(HttpSession session) throws IllegalStateException {
		session.setAttribute("divrep", this);
	}
	
	public void removePage(String pagekey) {
		for(DivRep divrep : childnodes) {
			DivRepPage page = (DivRepPage)divrep;
			if(page.getPageKey().equals(pagekey)) {
				//remove references
				System.out.println("Removing previous page with page key of :" + pagekey);
				this.remove(page);
				break;
			}
		}
	}
	
	public DivRepPage initPage(String pagekey)
	{
		/*
		 * I think I did this to reduce memory load, but this is causing people who opens 2 same URLs intentionally (maybe to compare content
		 * at the top and bottom?) to have constant re-syncing with sever issue
		//search and clear previous page with same pagekey
		removePage(pagekey);
		*/
		
		//if there are too many pages open, then close the oldest one
		if(childnodes.size() > 10) {
			//find oldest page
			DivRepPage last = null;
			for(DivRep divrep : childnodes) {
				DivRepPage page = (DivRepPage)divrep;
				if(last == null || page.getLastAccessed().compareTo(last.getLastAccessed()) < 0) {
					last = page;
				}
			}
			this.remove(last);
			System.out.println(last.getPageKey() + "(accessed "+last.getLastAccessed().toString()+") has been removed from this session due to too many pages.");
		}
		
		//insert new page and return
		DivRepPage page = new DivRepPage(this, pagekey);
		return page;
	}

	protected void onEvent(DivRepEvent e) {
		//root doesn't handle any event
	}

	public void render(PrintWriter out) {
		//root doesn't display anything
	}

}
