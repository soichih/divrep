package com.divrep.samples;

import java.io.PrintWriter;

public class SampleStaticContent {
	
	public static void renderHeader(PrintWriter out) {
		out.write("<div class=\"divrep_app_header\">DivRep Samples</div>");		
	}
	
	public static void renderSide(PrintWriter out) {
		out.write("<div class=\"divrep_app_side\">");
		out.write("<h2>Pages</h2>");
		out.write("<p><a href='home'>Home</a></p>");
		out.write("<br>");
		out.write("<p><a href='puzzle'>Puzzle Game</a></p>");
		out.write("<p><a href='calculator'>Prefix Calculator</a></p>");
		out.write("</div>");
	}
}
