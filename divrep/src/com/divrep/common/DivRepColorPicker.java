package com.divrep.common;

import java.awt.Color;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringEscapeUtils;

import com.divrep.DivRep;
import com.divrep.DivRepEvent;
import com.divrep.DivRepEventListener;

//This DivRep requires jQuery-UI
public class DivRepColorPicker extends DivRepFormElement<Color> {

	ArrayList<Color> preset_colors = new ArrayList<Color>();
	
	class PresetColor extends DivRep
	{

		Color color;
		public PresetColor(DivRep parent, Color c) {
			super(parent);
			color = c;
		}

		protected void onEvent(DivRepEvent e) {
			setValue(color);
			DivRepColorPicker.this.redraw();
		}

		public void render(PrintWriter out) {
			out.write("<span onclick=\"divrep(this.id, event);\" class=\"divrep_round\" style=\"background-color: #"+String.format("%06x", (color.getRGB() & 0x00ffffff))+"\" id=\""+getNodeID()+"\">");
			out.write("&nbsp;&nbsp;&nbsp;&nbsp;");
			out.write("</span>");
		}
		
	}
	
	public DivRepColorPicker(DivRep parent) {
		super(parent);
		setValue(Color.white);
		
		addPresetColor(Color.RED);
		addPresetColor(Color.ORANGE);
		addPresetColor(Color.PINK);
		addPresetColor(Color.GREEN);
		addPresetColor(new Color(0,127,0));
		addPresetColor(Color.MAGENTA);
		addPresetColor(Color.CYAN);
		addPresetColor(Color.BLUE);
		addPresetColor(Color.YELLOW);
		addPresetColor(Color.BLACK);	
		addPresetColor(Color.DARK_GRAY);
		addPresetColor(Color.GRAY);
		addPresetColor(Color.LIGHT_GRAY);
		addPresetColor(Color.WHITE);
	}
	
	public void addPresetColor(Color c) {
		//make sure the same color doesn't already exist
		for(Color color : preset_colors) {
			if(color.equals(c)) return;
		}
		
		preset_colors.add(c);
	}

	protected void onEvent(DivRepEvent e) {
		setValue(new Color(Integer.parseInt(e.value)));
	}
	
	public void render(PrintWriter out) {
		out.write("<div class=\"divrep_colorpicker\" id=\""+getNodeID()+"\">");
		if(getLabel() != null) {
			out.write("<label>"+StringEscapeUtils.escapeHtml(getLabel())+"</label><br/>");
		}
		
		//preset colors
		//sort colors by hue
		Collections.sort(preset_colors, new Comparator<Color>(){
			public int compare(Color c1, Color c2) {
				float f1[] = Color.RGBtoHSB(c1.getRed(), c1.getGreen(), c1.getBlue(), null);
				float f2[] = Color.RGBtoHSB(c2.getRed(), c2.getGreen(), c2.getBlue(), null);
				
				if(f1[0] < f1[0]) return -1;
				if(f1[0] > f2[0]) return 1;
				return 0;
			}});
		out.write("<div class=\"divrep_colorpicker_preset\">");
		int i = 0;
		for(Color color : preset_colors) {
			PresetColor preset = new PresetColor(this, color);
			preset.render(out);
			++i;
			if(i%16 == 0) {
				out.write("<br/>");
			}
		}
		out.write("</div>");

		//current color
		out.write("<div class=\"divrep_colorpicker_color divrep_round\" id=\""+getNodeID()+"_color\"></div>");
		
		//sliders
		out.write("<div>");
		out.write("<div class=\"divrep_colorpicker_red\" id=\""+getNodeID()+"_red\"></div>");
		out.write("<div class=\"divrep_colorpicker_green\" id=\""+getNodeID()+"_green\"></div>");
		out.write("<div class=\"divrep_colorpicker_blue\" id=\""+getNodeID()+"_blue\"></div>");
		out.write("</div>");

		
		out.write("<script type=\"text/javascript\">");
		out.write("function change_"+getNodeID()+"() {");
		out.write("	var red = $(\"#"+getNodeID()+"_red\").slider('value');");
		out.write("	var green = $(\"#"+getNodeID()+"_green\").slider('value');");
		out.write("	var blue = $(\"#"+getNodeID()+"_blue\").slider('value');");
		out.write("	divrep('"+getNodeID()+"', null, red*256*256+green*256+blue);");
		out.write("}");
		out.write("function refresh_"+getNodeID()+"() {");
		out.write("	var red = $(\"#"+getNodeID()+"_red\").slider('value');");
		out.write("	var green = $(\"#"+getNodeID()+"_green\").slider('value');");
		out.write("	var blue = $(\"#"+getNodeID()+"_blue\").slider('value');");
		out.write("	$(\"#"+getNodeID()+" .divrep_colorpicker_color\").css(\"background-color\", \"#\" + colorpicker_hexFromRGB(red, green, blue));");
		out.write("}");
		out.write("$(\"#"+getNodeID()+"_red\").slider({change: change_"+getNodeID()+", slide: refresh_"+getNodeID()+", range: \"min\", max: 255, value: "+getValue().getRed()+"});");
		out.write("$(\"#"+getNodeID()+"_green\").slider({change: change_"+getNodeID()+", slide: refresh_"+getNodeID()+", range: \"min\", max: 255, value: "+getValue().getGreen()+"});");
		out.write("$(\"#"+getNodeID()+"_blue\").slider({change: change_"+getNodeID()+", slide: refresh_"+getNodeID()+", range: \"min\", max: 255, value: "+getValue().getBlue()+"});");
		out.write("refresh_"+getNodeID()+"();");
		out.write("</script>");
		
		out.write("</div>"); //main div
	}

}
