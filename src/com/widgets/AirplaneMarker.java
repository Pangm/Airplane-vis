package com.widgets;

import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PShape;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;

public class AirplaneMarker extends SimplePointMarker{
	protected String name;
	protected float size = 15;
	protected int space = 6;

	private PFont font;
	private float fontSize = 12;
	
	PImage img;
	
	static final String IMAGE_DIRECTORY = "./data/";
	
	public AirplaneMarker(Location location) {
		super(location);
//		airplaneIcon = loadShape(IMAGE_DIRECTORY + "white_Airplane_ok.svg");
	}

	public AirplaneMarker(Location location, String name, PImage img, PFont font, float size) {
		this(location);
		this.img = img;
		this.name = name;
		this.size = size;

		this.font = font;
		if (font != null) {
			this.fontSize = font.getSize();
		}
	}

	/**
	 * Displays this marker's name in a box.
	 */
	public void draw(PGraphics pg, float x, float y) {
		pg.pushStyle();
		pg.pushMatrix();
		if (selected) {
			pg.translate(0, 0, 1);
		}
		pg.strokeWeight(strokeWeight);
		if (selected) {
			pg.imageMode(PConstants.CORNER);
			// The image is drawn in object coordinates, i.e. the marker's origin (0,0) is at its geo-location.
			pg.image(img, x, y);
		} else {
			// TO-DO change the mode
			pg.imageMode(PConstants.CORNER);
			// The image is drawn in object coordinates, i.e. the marker's origin (0,0) is at its geo-location.
			pg.image(img, x, y);
		}

		// label
		if (selected && name != null) {
			if (font != null) {
				pg.textFont(font);
			}
			pg.fill(highlightColor);
			pg.stroke(highlightStrokeColor);
			pg.rect(x + strokeWeight / 2, y - fontSize + strokeWeight / 2 - space, pg.textWidth(name) + space * 1.5f,
					fontSize + space);
			pg.fill(255, 255, 255);
			pg.text(name, Math.round(x + space * 0.75f + strokeWeight / 2),
					Math.round(y + strokeWeight / 2 - space * 0.75f));
		}
		pg.popMatrix();
		pg.popStyle();
	}
	
	@Override
	public boolean isInside(float checkX, float checkY, float x, float y) {
		return checkX > x && checkX < x + img.width && checkY > y && checkY < y + img.height;
	}
	
	public String getName() {
		return name;
	}
}
