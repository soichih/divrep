package com.divrep.samples;

import java.io.PrintWriter;

public class Common {
	static void outputJquery(PrintWriter out) {
		//Load jQuery
		out.write("<script type=\"text/javascript\" src=\"//code.jquery.com/jquery-1.10.2.min.js\"></script>");
		out.write("<script type=\"text/javascript\" src=\"//code.jquery.com/ui/1.10.3/jquery-ui.min.js\"></script>");
		out.write("<link href=\"//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/themes/smoothness/jquery-ui.css\" rel=\"stylesheet\" type=\"text/css\"/>");
	}
}
