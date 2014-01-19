package com.divrep.samples;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.divrep.common.DivRepButton;
import com.divrep.*;

public class ButtonServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException 
	{	
		final PrintWriter out = response.getWriter();
		out.write("<html><head>");
		
		out.write("<script type=\"text/javascript\" src=\"//code.jquery.com/jquery-1.10.2.min.js\"></script>");
		out.write("<script type=\"text/javascript\" src=\"//code.jquery.com/ui/1.10.3/jquery-ui.min.js\"></script>");
		out.write("<link href=\"//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/themes/smoothness/jquery-ui.css\" rel=\"stylesheet\" type=\"text/css\"/>");
		
		out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/divrep.css\" />");
		out.write("<script type=\"text/javascript\" src=\"divrep.js\"></script>");
		
		out.write("</head><body><div id=\"content\">");		
		
		new DivRepContainer(request) {
			public void initPage(DivRepPage pageroot) {
				ButtonDivRep divrep = new ButtonDivRep(pageroot);
				divrep.render(out);
			}
		};
		
		out.write("</div></body></html>");
	}
    class ButtonDivRep extends DivRep
    {
        //Button for each styles
        DivRepButton normal_button;
        DivRepButton link_button;
        DivRepButton image_button;
        DivRepButton html_button;
        
        DivRepButton open_button;

        public ButtonDivRep(DivRep _parent) {
            super(_parent);

            //instantiate the buttons
            normal_button = new DivRepButton(this, "Normal Button");
            normal_button.setToolTip("Hello this is a normal button.");
            normal_button.addEventListener(new DivRepEventListener() {
                public void handleEvent(DivRepEvent e) {
                    alert("You clicked a normal button");
                }
            });
            
            link_button = new DivRepButton(this, "Link Button");
            link_button.setToolTip("Hello this is a link button. Click me");
            link_button.setStyle(DivRepButton.Style.ALINK);
            link_button.addEventListener(new DivRepEventListener() {
                public void handleEvent(DivRepEvent e) {
                    alert("You clicked a link button");
                }
            });
            
            image_button = new DivRepButton(this, "css/images/osg_logo.png");
            image_button.setToolTip("Hello this is a image button.");
            image_button.setStyle(DivRepButton.Style.IMAGE);
            image_button.addEventListener(new DivRepEventListener() {
                public void handleEvent(DivRepEvent e) {
                    alert("You clicked a image button");
                }
            });

            html_button = new DivRepButton(this, "<b>html content</b><i>italic</i>");
            html_button.setStyle(DivRepButton.Style.HTML);
            html_button.addEventListener(new DivRepEventListener() {
                public void handleEvent(DivRepEvent e) {
                    alert("You clicked a HTML button");
                }
            });
            
            open_button = new DivRepButton(this, "Open New Page");
            open_button.addEventListener(new DivRepEventListener() {
                public void handleEvent(DivRepEvent e) {
                    open_button.open("http://digg.com", "digg");
                }
            });       
        }

        public void render(PrintWriter out) {
            out.write("<div id=\""+getNodeID()+"\">");

            //I have to render the button
            out.write("<h2>Normal Buttons</h2>");
            normal_button.render(out);
            open_button.render(out);
            
            out.write("<h2>Link Button</h2>");
            link_button.render(out);
           
            out.write("<h2>Image Button</h2>");
            image_button.render(out);
            
            out.write("<h2>HTML Button</h2>");
            html_button.render(out);
                      
            out.write("</div>");
        }

        @Override
        protected void onEvent(DivRepEvent e) {
            // TODO Auto-generated method stub              
        }
    }
}

