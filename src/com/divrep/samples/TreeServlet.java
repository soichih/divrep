package com.divrep.samples;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.divrep.DivRep;
import com.divrep.DivRepContainer;
import com.divrep.DivRepEvent;
import com.divrep.DivRepPage;
import com.divrep.DivRepRoot;
import com.divrep.common.DivRepStaticContent;
import com.divrep.common.DivRepToggler;
import com.divrep.samples.TradingGameServlet.TradingGame;

public class TreeServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{	
		final PrintWriter out = response.getWriter();
		out.write("<html><head>");

		//Load jQuery
		out.write("<script type=\"text/javascript\" src=\"jquery-1.7.1.min.js\"></script>");

		//Load DivRep Stuff
		out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/divrep.css\" />");
		out.write("<script type=\"text/javascript\" src=\"divrep.js\"></script>");
		
		out.write("</head><body><div id=\"content\">");		
		out.write("<h1>Toggler can be used to create a tree</h1>");

		//Create Tree component and render
		new DivRepContainer(request) {
			public void initPage(DivRepPage pageroot) {
				Tree tree = new Tree(pageroot);
				tree.render(out);
			}
		};
		
		
		out.write("</div></body></html>");
	}
	
	class Tree extends DivRep
	{
		DivRepToggler root1;
		DivRepToggler root2;
		DivRepToggler root3;
		
		public Tree(DivRep _parent) {
			super(_parent);
			root1 = new DivRepToggler(this) {
				public DivRep createContent() {
					return new DivRepStaticContent(this, "<div class=\"divrep_indent\">Static child content 1</div>");
				}
			};
			
			root2 = new DivRepToggler(this) {
				public DivRep createContent() {
					return new DivRepStaticContent(this, "<div class=\"divrep_indent\">Static child content 2 (show by default)</div>");
				}
			};
			
			root3 = new DivRepToggler(this) {
				public DivRep createContent() {
					return new Tree(this);
				}
			};
			//you can show/hide content by default
			root2.setShow(true);
		}

		@Override
		protected void onEvent(DivRepEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void render(PrintWriter out) {
			out.write("<div id=\""+getNodeID()+"\" class=\"divrep_indent\">");
			root1.render(out);
			root2.render(out);
			root3.render(out);
			out.write("</div>");
		}
	}
}

	