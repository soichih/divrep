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
import com.divrep.DivRepEventListener;
import com.divrep.DivRepPage;
import com.divrep.common.DivRepButton;
import com.divrep.common.DivRepStaticContent;
import com.divrep.common.DivRepTextBox;
import com.divrep.common.DivRepToggler;
import com.divrep.common.DivRepButton.Style;

public class CalculatorServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException 
	{	
		final PrintWriter out = response.getWriter();

		out.write("<!doctype html><html><head>");
		
		//Load jQuery
		out.write("<script type=\"text/javascript\" src=\"jquery-1.7.1.min.js\"></script>");

		out.write("<style type=\"text/css\"> * { padding: 0px; margin: 0px; }\n");
		out.write(".logic {padding: 4px; border: 1px solid black; color: black;}\n");
		out.write(".help {padding: 20px; background-color: #ccc; width: 70%;}\n");
		out.write(".help ol {margin-left: 30px;}\n");
		out.write("img {border: none;}");
		out.write("</style>"); 
		
		//Load DivRep Stuff
		out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/divrep.css\">");
		out.write("<script type=\"text/javascript\" src=\"divrep.js\"></script>");
		
		out.write("</head>");
		out.write("<body class=\"divrep_app\">");
		
		SampleStaticContent.renderHeader(out);
		SampleStaticContent.renderSide(out);
		
		out.write("<div class=\"divrep_app_main\">");
		out.write("<h2>Prefix Calculator</h2>");
		
		out.write("<div class=\"divrep_app_section\">");
		out.write("<h3>&nbsp;</h3>");
		out.write("<div style=\"margin-left: 30px;\">");
		
		new DivRepContainer(request) {
			public void initPage(DivRepPage pageroot) {
				CalculatorDE calc = new CalculatorDE(pageroot);
				calc.render(out);
				
				out.write("<br><div class=\"help divrep_round\">"); 
				UsageToggler help = new UsageToggler(pageroot);
				help.render(out);
				out.write("</div>");
				
			}
		};
		
		out.write("</div>"); //margin
		out.write("<br>");
		out.write("</div>"); //section
		
		out.write("</div>"); //main
		
		out.write("</body></html>");
	}
	
	class UsageToggler extends DivRepToggler {
		public UsageToggler(DivRep parent) {
			super(parent);
			setShowHtml("<span class=\"divrep_link\">Show Help</span>");
			setHideHtml("<span class=\"divrep_link\">Hide Help</span>");
		}

		public DivRep createContent() {
			return new DivRepStaticContent(this, 
			"<br>" + 
			"<p>In order to calculate something like <b>(10 x 2) + 8 ..</b></p>" +
			"<ol>"+
			"<li>Click + (addition) button. You will now see 2 control bars for top and bottom side of the addition.</li>" + 
			"<li>On the top control bar, click x (multiplication) button. Now you have the arithmetic structure to construct (10 x 2)</li>" + 
			"<li>On the very top control bar, click 123 to enter the constant. Enter 10.</li>" + 
			"<li>On the middle control bar, click 123 and enter 2</li>" +
			"<li>On the bottom control bar, click 123 and enter 8.</li>" +
			"<li>Click calculate button and you should see 28!</li>" +
			"</ol>" +
			"<br><p>You can use Remove button to conver the section of the arithmetic back to the control bar.</p>" +
			"<p>For example, you can convert constant 8 in above sample arithmetic to something like (100 / 4) by doing following.</p>" +
			"<ol>" +
			"<li>Click Remove button next to constant 8</li>" +
			"<li>Click / (division) on the control bar that just appeared.</li>" +
			"<li>Click 123(constant) buttons on both control bars.</li>" + 
			"<li>Enter 100, and 4 on each constant editor.</li>" +
			"<li>Now click calculate, and you should get 45</li>" +
			"</ol>");
		}
	}

	class CalculatorDE extends DivRep
	{
		CalcNode root;
		Result result;
		DivRepButton refresh;
		
		public CalculatorDE(DivRep _parent) {
			super(_parent);
			root = new Controller(this);
			result = new Result(this);
			
			refresh = new DivRepButton(this, "Recalculate");
			refresh.addEventListener(new DivRepEventListener() {
				public void handleEvent(DivRepEvent e) {
					root.redraw();
				}});
		}	
		protected void onEvent(DivRepEvent e) {
			// TODO Auto-generated method stub
		}
		public void render(PrintWriter out) {
			out.write("<div id=\""+getNodeID()+"\">");
			
			root.render(out);
			out.write("<br>");
			
			result.render(out);
			out.write("<br>");
			
			refresh.render(out);
			
			out.write("</div>");
		}
		
		class Result extends DivRep
		{
			public Result(DivRep _parent) {
				super(_parent);
			}

			@Override
			protected void onEvent(DivRepEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void render(PrintWriter out) {
				out.write("<div id=\""+getNodeID()+"\">");
				try {
					out.write("<br/>");
					Double ret = root.calculate();
					out.write(" = " + ret);
				} catch (Exception e) {
					out.write(e.getMessage());
				}
				out.write("</div>");
			}
		
		}
		
		abstract class CalcNode extends DivRep
		{
			public CalcNode(DivRep _parent) {
				super(_parent);
				// TODO Auto-generated constructor stub
			}

			protected void onEvent(DivRepEvent e) {
			}
		
			abstract public Double calculate() throws Exception;
		}
	
		
		class Controller extends CalcNode
		{
			CalcNode instance;
	
			DivRepButton becomepluslogic;
			DivRepButton becomeminuslogic;
			DivRepButton becomemultiplylogic;
			DivRepButton becomedividelogic;
			DivRepButton becomeconstant;
		
			DivRepButton clearbutton;
			
			public Controller(DivRep _parent) {
				super(_parent);
				
				instance = null;
				
				//selector components
				becomepluslogic = new DivRepButton(this, "+");
				becomepluslogic.addEventListener(new DivRepEventListener() {
					public void handleEvent(DivRepEvent e) {
						instance = new LogicAddition(Controller.this);
						redraw();
					}});
				
				becomeminuslogic = new DivRepButton(this, "-");
				becomeminuslogic.addEventListener(new DivRepEventListener() {
					public void handleEvent(DivRepEvent e) {
						instance = new LogicSubtraction(Controller.this);
						redraw();
					}});
				
				becomemultiplylogic = new DivRepButton(this, "x");
				becomemultiplylogic.addEventListener(new DivRepEventListener() {
					public void handleEvent(DivRepEvent e) {
						instance = new LogicMultiplication(Controller.this);
						redraw();
					}});
				
				becomedividelogic = new DivRepButton(this, "/");
				becomedividelogic.addEventListener(new DivRepEventListener() {
					public void handleEvent(DivRepEvent e) {
						instance = new LogicDivision(Controller.this);
						redraw();
					}});
				
				becomeconstant = new DivRepButton(this, "123");
				becomeconstant.addEventListener(new DivRepEventListener() {
					public void handleEvent(DivRepEvent e) {
						instance = new Constant(Controller.this);
						redraw();
					}});
				
				clearbutton = new DivRepButton(this, "css/images/delete.png");
				clearbutton.setStyle(Style.IMAGE);
				clearbutton.addEventListener(new DivRepEventListener() {
					public void handleEvent(DivRepEvent e) {
						instance = null;
						redraw();
						result.redraw();
					}});
			}
			
			public void render(PrintWriter out) {
				
				out.write("<div id=\""+getNodeID()+"\">");
				if(instance == null) {
					becomepluslogic.render(out);
					becomeminuslogic.render(out);
					becomemultiplylogic.render(out);
					becomedividelogic.render(out);
					becomeconstant.render(out);
				} else {
					out.write("<table><tr><td>");
					instance.render(out);
					out.write("</td><td>");
					clearbutton.render(out);
					out.write("</td></tr></table>");
				}	
				out.write("</div>");
				
			}
			public Double calculate() throws Exception { 
				if(instance == null) {
					throw new Exception("Please complete your equation"); 
				} else {
					return instance.calculate();
				}	
			}
		}
		
		class LogicAddition extends CalcNode
		{
			CalcNode left = new Controller(this);
			CalcNode right = new Controller(this);
			
			public LogicAddition(DivRep _parent) {
				super(_parent);
				// TODO Auto-generated constructor stub
			}
			public void render(PrintWriter out) {
				out.write("<div class=\"logic\" id=\""+getNodeID()+"\">");
				left.render(out);
				out.write("+");
				right.render(out);
				out.write("</div>");
			}
			public Double calculate() throws Exception { 
				return left.calculate() + right.calculate();
			}
		}
		
		class LogicSubtraction extends CalcNode
		{
			CalcNode left = new Controller(this);
			CalcNode right = new Controller(this);
			
			public LogicSubtraction(DivRep _parent) {
				super(_parent);
				// TODO Auto-generated constructor stub
			}
			public void render(PrintWriter out) {
				out.write("<div class=\"logic\" id=\""+getNodeID()+"\">");
				left.render(out);
				out.write("-");
				right.render(out);
				out.write("</div>");
			}
			public Double calculate() throws Exception { 
				return left.calculate() - right.calculate();
			}
		}
		
		class LogicMultiplication extends CalcNode
		{
			CalcNode left = new Controller(this);
			CalcNode right = new Controller(this);
			
			public LogicMultiplication(DivRep _parent) {
				super(_parent);
				// TODO Auto-generated constructor stub
			}
			public void render(PrintWriter out) {
				out.write("<div class=\"logic\" id=\""+getNodeID()+"\">");
				left.render(out);
				out.write("x");
				right.render(out);
				out.write("</div>");
			}
			public Double calculate() throws Exception { 
				return left.calculate() * right.calculate();
			}
		}
		
		class LogicDivision extends CalcNode
		{
			CalcNode left = new Controller(this);
			CalcNode right = new Controller(this);
			
			public LogicDivision(DivRep _parent) {
				super(_parent);
				// TODO Auto-generated constructor stub
			}
			public void render(PrintWriter out) {
				out.write("<div class=\"logic\" id=\""+getNodeID()+"\">");
				left.render(out);
				out.write("/");
				right.render(out);
				out.write("</div>");
			}
			public Double calculate() throws Exception { 
				return left.calculate() / right.calculate();
			}
		}
		
		class Constant extends CalcNode
		{
			DivRepTextBox constant = new DivRepTextBox(this);
			public Constant(DivRep _parent) {
				super(_parent);
				constant.addEventListener(new DivRepEventListener() {
					public void handleEvent(DivRepEvent e) {
						result.redraw();
						
					}});
				constant.setWidth(100);
			}

			public void render(PrintWriter out) {
				out.write("<div id=\""+getNodeID()+"\">");
				constant.render(out);
				out.write("</div>");
			}

			protected void onEvent(DivRepEvent e) {
			}
			
			public Double calculate() throws NumberFormatException { 
				if(constant.getValue() == null) {
					throw new NumberFormatException("Please populate all values");
				}
				return Double.parseDouble(constant.getValue()); 
			}
		}
	}
}
