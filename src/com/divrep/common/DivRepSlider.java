package com.divrep.common;

import java.awt.Color;
import java.io.PrintWriter;

import com.divrep.DivRep;
import com.divrep.DivRepEvent;
import com.divrep.common.DivRepFormElement;

//This DivRep requires jQuery-UI
public class DivRepSlider extends DivRepFormElement<Long> {
	
	Long max = 100L;
	Color color = null;
	
	//slide events will be generated whenever user slides across the nob
	private Boolean bListenSlideEvents = false;
	public Boolean isListenSlideEvents() { return bListenSlideEvents; }
	public void setListenSlideEvents(Boolean b) { bListenSlideEvents = b; }

	public DivRepSlider(DivRep parent) {
		super(parent);
		// TODO Auto-generated constructor stub
	}
	public void setMax(Long _max) { max = _max; }
	public void setColor(Color _color) { color = _color; }

	@Override
	protected void onEvent(DivRepEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void render(PrintWriter out) {
		out.write("<div id=\""+getNodeID()+"\">");
		
		out.write("<div id=\""+getNodeID()+"_slider\"></div>");
		out.write("<script type=\"text/javascript\">");
		out.write("$(\"#"+getNodeID()+"_slider\").slider({range: 'min', max: "+max+", ");
		if(bListenSlideEvents) {
			out.write("slide: function(event, ui) {divrep('"+getNodeID()+"', event, ui.value);}, ");
		}
		out.write("value: "+getValue()+", change: function(event,ui) {divrep('"+getNodeID()+"', event, ui.value); }});");
		//out.write("$('#"+getNodeID()+"_slider').unbind('click');");
		out.write("</script>");
		if(color != null) {
			out.write("<style type=\"text/css\">");
			out.write("#"+getNodeID()+"_slider .ui-slider-range {background: #"+String.format("%06x", (color.getRGB() & 0x00ffffff) )+"}");
			out.write("</style>");
		}
		out.write("</div>");
	}

}
