package com.divrep.samples;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.divrep.DivRep;
import com.divrep.DivRepContainer;
import com.divrep.DivRepEvent;
import com.divrep.DivRepEventListener;
import com.divrep.DivRepPage;
import com.divrep.common.DivRepButton;

public class PuzzleGameServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{	
		final PrintWriter out = response.getWriter();
		out.write("<!doctype html><html><head>\n");

		out.write("<script type=\"text/javascript\" src=\"//code.jquery.com/jquery-1.10.2.min.js\"></script>");
		out.write("<script type=\"text/javascript\" src=\"//code.jquery.com/ui/1.10.3/jquery-ui.min.js\"></script>");
		out.write("<link href=\"//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/themes/smoothness/jquery-ui.css\" rel=\"stylesheet\" type=\"text/css\"/>");

		//Load DivRep Stuff
		out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/divrep.css\" />");
		out.write("<script type=\"text/javascript\" src=\"divrep.js\"></script>");
		
		out.write("<style> * { padding: 0px; margin: 0px; }");
		out.write(".puzzle .divrep_processing {background-image: url('css/images/animated_question_mark.gif');} ");
		out.write(".tile {background-image: url('css/images/question_mark.png'); background-position:0 -10px;}");
		out.write("</style>"); 

		out.write("</head>");
		out.write("<body class=\"divrep_app\">");
		
		SampleStaticContent.renderHeader(out);
		SampleStaticContent.renderSide(out);
		
		out.write("<div class=\"divrep_app_main\">");
		out.write("<h2>Puzzle Game</h2>");

		out.write("<div class=\"divrep_app_section\">");
		out.write("<h3>Who's Hiding!?</h3>");
		out.write("<div style=\"margin-left: 20px;\">");
		out.write("<p>Click on ? marks to reveal the picture below.. Whoever correctly gusses what the picture is wins.</p>");

		
		//This looks odd, but instantiating root DivRep objects inside initPage allowes
		//it to work correctly on clustered J2EE - including Google App Engine
		new DivRepContainer(request) {
			public void initPage(DivRepPage pageroot) {
				Puzzle puzzle = new Puzzle(pageroot);
				puzzle.render(out);	
			}
		};
		out.write("<br></div>");
		out.write("</div>"); //app_section
		
		/* -- test to see if I can serialize this
		FileOutputStream fos = new FileOutputStream("c:/test.out");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(pageroot);
		*/
	
		
		out.write("</div>");//app_main
		out.write("</body></html>");
	}


	class Puzzle extends DivRep 
	{
		//number of pieces to devide
		Integer x_devide = 8;
		Integer y_devide = 5;
		ArrayList<Piece> pieces = new ArrayList<Piece>();
		
		//size of the image
		Integer xsize = 720;
		Integer ysize = 450;
		
		List<String>image_urls = new ArrayList<String>();
		Integer current_image = 0;
		DivRepButton nextbutton;
		
		public Puzzle(DivRep _parent) {
			super(_parent);	
			image_urls.add("http://lh4.ggpht.com/_lj4j9umv09I/SKA5ww4kK5I/AAAAAAAAGXE/mQSAC9ygnvA/s720/DSC_0523.jpg");	
			image_urls.add("http://lh4.ggpht.com/_eFqlFWgp1V8/SLHqPP62szI/AAAAAAAAATg/1Mv5IpGHuDQ/s720/IMG_4980.JPG");
			image_urls.add("http://lh4.ggpht.com/_oBY4isNAkUs/SylRi5QXhSI/AAAAAAAAQMk/TaIw5JGBwnM/s720/Florida_0998.jpg");
			image_urls.add("http://lh5.ggpht.com/_leGIGlGp1Fw/SKcjl34KhZI/AAAAAAAADWA/edMibsg7cFA/s720/P160808_12.06.jpg");
			image_urls.add("http://lh4.ggpht.com/_vu7m_ZOxM9o/RYYVI3tyIQI/AAAAAAABO0M/S9ll4srj-pE/s720/DSC00070-e01.jpg");
			image_urls.add("http://lh4.ggpht.com/_mcX2E2nfhdw/RxYaiSwYp5I/AAAAAAAACAs/A-j8hVWSzaU/s720/DSC_0171.JPG");
		
			int xpiecesize = xsize / x_devide;
			int ypiecesize = ysize / y_devide;
			
			for(int y = 0; y < y_devide; ++y) {
				for(int x = 0;x < x_devide; ++x) {					
					pieces.add(y*x_devide+x, new Piece(this, xpiecesize, ypiecesize, xpiecesize*x,ypiecesize*y));
				}
			}
			
			nextbutton = new DivRepButton(this, "Next Puzzle");
			
			nextbutton.addEventListener(new DivRepEventListener() {
				public void handleEvent(DivRepEvent e) {
					Puzzle.this.nextimage();
					
				}});
			
		}

		public void nextimage()
		{
			current_image++;
			if(current_image == image_urls.size()) current_image = 0;
			
			for(int y = 0; y < y_devide; ++y) {
				for(int x = 0;x < x_devide; ++x) {	
					Piece p = pieces.get(y*x_devide+x);
					p.shown = false;
				}
			}
			
			Puzzle.this.redraw();
		}
		protected void onEvent(DivRepEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void render(PrintWriter out) {
			out.print("<div id=\""+getNodeID()+"\" class=\"puzzle\">");
			String url = image_urls.get(current_image);
			out.print("<table style=\"border-collapse:collapse; background-image: url('"+url+"');\">");
			
			//display all masks
			for(int y = 0; y < y_devide; ++y) {
				out.print("<tr>");
				for(int x = 0;x < x_devide; ++x) {
					Piece p = pieces.get(y*x_devide+x);
					p.render(out);
				}
				out.print("</tr>");
			}
			out.print("</table>");
			
			//out.write("<p>Image : " + url + "</p>");
			
			nextbutton.render(out);
			
			out.print("</div>");
		}
		
		class Piece extends DivRep
		{
			Boolean shown = false;
			Integer xsize, ysize;
			Integer xoffset, yoffset;
			
			public Piece(DivRep _parent, int _xsize, int _ysize, int _xoffset, int _yoffset) {
				super(_parent);
				xsize = _xsize;
				ysize = _ysize;
				xoffset = _xoffset;
				yoffset = _yoffset;
			}

			@Override
			protected void onEvent(DivRepEvent e) {
				shown = true;
				redraw();
			}

			@Override
			public void render(PrintWriter out) {
				if(!shown) {
					out.write("<td class=\"tile\" style=\"cursor: pointer; width: "+xsize+"px; height: "+ysize+"px;\" onclick=\"divrep('"+getNodeID()+"');\" id=\""+getNodeID()+"\">");
					out.write("&nbsp;</td>\n");
				} else {
					out.write("<td style=\"width: "+xsize+"px; height: "+ysize+"px;\">");
					out.write("&nbsp;</td>\n");
				
				}
			}
		}
		
	}
}
