package com.divrep.common;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Random;

import org.apache.commons.lang.StringEscapeUtils;

import com.divrep.DivRep;
import com.divrep.DivRepEvent;
import com.divrep.DivRepEventListener;
import com.divrep.common.DivRepFormElement;
import com.divrep.i18n.Labels;

//In order to use this location selector, you need to add following to your header
//<script src="http://maps.google.com/maps?file=api&v=2&key=_______MY_GMAP_KEY_________" type="text/javascript"></script>
public class DivRepLocationSelector extends DivRepFormElement<DivRepLocationSelector.LatLng> {
	Labels lab = Labels.getInstance();

	String target_image_url;
	DivRepTextBox latitude_text;
	DivRepTextBox longitude_text;
	int zoom;
	String apikey;
	
	boolean use_sensor = false;
	public void setUseSensor(boolean use_sensor) {
		this.use_sensor = use_sensor;
	}
	
	/*
	boolean https = false;
	public void setHttps(boolean https) {
		this.https = https;
	}
	*/
	
	public class LatLng implements Serializable
	{
		public LatLng(Double lat, Double lng, int _zoom) {
			latitude = lat;
			longitude = lng;
			zoom = _zoom;
		}
		public LatLng(Double lat, Double lng) {
			this(lat,lng, 10);
		}
		public Double latitude;
		public Double longitude;
		public int zoom;
	}
	
	@Override
	public void setDisabled(Boolean b)  {
		super.setDisabled(b);
		latitude_text.setDisabled(b);
		longitude_text.setDisabled(b);
	}
	
	//don't pass null values..
	public void setValue(DivRepLocationSelector.LatLng value) {
		super.setValue(value);
		latitude_text.setValue(value.latitude.toString());
		longitude_text.setValue(value.longitude.toString());
		zoom = value.zoom;
	}

	public DivRepLocationSelector(DivRep parent, String target_image_url, String apikey) {
		super(parent);
		this.target_image_url = target_image_url;
		this.apikey = apikey;
		this.use_sensor = use_sensor;
		//this.https = https;
		
		addClass("divrep_locationselector");
		
		latitude_text = new DivRepTextBox(this);
		latitude_text.setLabel("Latitude");
		latitude_text.addEventListener(new DivRepEventListener() {
			public void handleEvent(DivRepEvent e) {
				try {
					getValue().latitude = Double.parseDouble((String)e.value);
				} catch (NumberFormatException n) {
					alert("Failed to parse the coordinate..");
				}
		    	js("map.setCenter(new google.maps.LatLng("+getValue().latitude.toString()+", "+getValue().longitude.toString()+"));");
		    	js("map.setZoom("+getValue().zoom+");");
			}			
		});
		longitude_text = new DivRepTextBox(this);
		longitude_text.setLabel("Longitude");
		longitude_text.addEventListener(new DivRepEventListener() {
			public void handleEvent(DivRepEvent e) {
				try {
					getValue().longitude = Double.parseDouble((String)e.value);
				} catch (NumberFormatException n) {
					alert("Failed to parse the coordinate..");
				}
		    	js("map.setCenter(new google.maps.LatLng("+getValue().latitude.toString()+", "+getValue().longitude.toString()+"));");
		    	js("map.setZoom("+getValue().zoom+");");
			}			
		});
		
		setValue(new LatLng(0.0,0.0,10));
	}

	protected void onEvent(DivRepEvent e) {
		if(isDisabled()) return;
		//if(isHidden()) return;
		
		String newval = (String)e.value;
		String coords[] = newval.split(",");
		getValue().latitude = Double.parseDouble(coords[0]);
		if(!coords[0].equals(latitude_text.getValue())) {
			latitude_text.setValue(coords[0]);
			latitude_text.redraw();
		}
		getValue().longitude = Double.parseDouble(coords[1]);
		if(!coords[1].equals(longitude_text.getValue())) {
			longitude_text.setValue(coords[1]);
			longitude_text.redraw();
		}
		setFormModified();
	}

	public void render(PrintWriter out) {		
		out.write("<div ");
		renderClass(out);
		out.write("id=\""+getNodeID()+"\">");
		if(!isHidden()) {
			if(getLabel() != null) {
				out.print("<label>"+StringEscapeUtils.escapeHtml(getLabel())+"</label><br/>");
			}
			String random = Integer.toString(Math.abs(new Random().nextInt()));
			String canvasid = "map_canvas_"+getNodeID()+random;
			out.write("<div class=\"map-container\" style=\"width: 500px; height: 300px\">");
			out.write("<div id=\""+canvasid+"\" style=\"width: 100%; height: 100%;\"></div>");
			out.write("<div class=\"map-cross\" style=\"background-image: url("+target_image_url+");\"></div>");
			out.write("</div>");
			latitude_text.render(out);
			longitude_text.render(out);
			
			out.write("<script type=\"text/javascript\">");
			
			//gmap initializer
			out.write("var map;\n");
			out.write("function gmap_init() {\n");
			//out.write("console.log('init');");
			out.write(" var mapOptions = {\n");
			out.write(" zoom: "+zoom+",\n");
			out.write(" scrollwheel: false,\n");
			if(isDisabled()) {
				out.write(" draggable: false,\n");
				out.write(" disableDoubleClickZoom: true,\n");
			}
			out.write(" center: new google.maps.LatLng("+latitude_text.getValue()+", "+longitude_text.getValue()+"),\n");
			out.write(" mapTypeId: google.maps.MapTypeId.ROADMAP\n");
			out.write(" }\n");
			out.write("map = new google.maps.Map(document.getElementById(\""+canvasid+"\"), mapOptions);\n");
		    out.write("google.maps.event.addListener(map, 'dragend', function() {divrep('"+getNodeID()+"', null, map.getCenter().toUrlValue());});");		    
		    out.write("}\n");

			//gmap script loader
			out.write("function gmap_load() {\n");
			out.write(" var script = document.createElement(\"script\");\n");
			out.write(" script.type = \"text/javascript\";\n");
			/*
			if(https) {
				out.write(" script.src = \"https://maps.googleapis.com/maps/api/js?key="+apikey+"&sensor="+use_sensor+"&callback=gmap_init\";\n");			
			} else {
				out.write(" script.src = \"http://maps.googleapis.com/maps/api/js?key="+apikey+"&sensor="+use_sensor+"&callback=gmap_init\";\n");
			}
			*/
			out.write(" script.src = \"//maps.googleapis.com/maps/api/js?key="+apikey+"&sensor="+use_sensor+"&callback=gmap_init\";\n");			

			out.write("document.body.appendChild(script);\n");
			out.write("}\n");
			
			//load gmap script only once
			out.write("if(typeof(google) == 'undefined') {\n");
			out.write("window.onload = gmap_load;\n");
			out.write("} else {\n");
			out.write("gmap_init();\n");
			out.write("}\n");
				
			
			/*		   
		    //out.write("    map.setUIToDefault();");
		    //out.write("    map.setMapType(G_HYBRID_MAP);");
		    out.write("    var prompt = new GScreenOverlay(\""+target_image_url+"\",new GScreenPoint(0.5, 0.5, 'fraction', 'fraction'), new GScreenPoint(-50, -50), new GScreenSize(100, 100, 'pixel', 'pixel'));");
		    out.write("    map.addOverlay(prompt);");
		    out.write("    GEvent.addListener(map, 'moveend', function() {divrep('"+getNodeID()+"', null, map.getCenter().toUrlValue());});");
		    out.write("}");
		    */
			
			
		    out.write("</script>");
		
			if(isRequired()) {
				out.print(lab.RequiredFieldNote());
			}
			error.render(out);	
		}
		out.write("</div>");
	}
	
	//since the value is composite, I need to do some custom validation
	public boolean validate()
	{
		boolean previous_valid = super.validate();
		boolean valid = previous_valid;
		
		//validate if super class validates
		if(valid) {
			if(isRequired()) {
				if(getValue().latitude == null || getValue().longitude == null) {
					error.set(lab.RequiredFieldMessage());
					valid = false;
				}
			}
		}
		
		//why valid == false? because sometime error message can change to something else while it's set to true
		if(previous_valid != valid || valid == false) {
			error.redraw();
		}
		
		setValid(valid);
		return valid;
	}

}
