package com.airplane.vis;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PShape;

import com.airplane.entity.Bus;
import com.widgets.Button;
import com.widgets.Circle;
import com.widgets.CircleButton;
import com.widgets.Control;
import com.widgets.Panel;
import com.widgets.Scrollbar;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

public class AirplaneApp extends PApplet {
	UnfoldingMap map;
	final String DATA_DIRECTORY = "./data/";
	Location beijingLocation = new Location(39.9f, 116.3f);
	List<Bus> buses = new ArrayList<Bus>();
	List<String> lineNums = new ArrayList<String>();
	List<Location> pathLocs = null;
	Scrollbar s = null; // the scrollbar
	Button button = null;
	Panel panel = null;
	int initZoomLevel = 6;
	float progress = 0;
	int loadCnt = 0;
	PShape busLogo;
	Image icon;

	int displayFrameCnt = 80;

	public static void main(String[] agrs) {
		PApplet.main(new String[] { "com.airplane.vis.AirpalneApp" });
	}
	
	public void setup() {
		size(1366, 768);

		String mbTilesString = sketchPath(DATA_DIRECTORY + "tiles/China-City-6-16.mbtiles");

		map = new UnfoldingMap(this, new MBTilesMapProvider(mbTilesString));

		map.zoomToLevel(initZoomLevel);
		map.panTo(beijingLocation);
//		map.setZoomRange(6, 14); // prevent zooming too far out
//		map.setPanningRestriction(beijingLocation, 50);
		MapUtils.createDefaultEventDispatcher(this, map);

//		initBuses(buses, DATA_DIRECTORY);
//
//		s = new Scrollbar(this, width / 2, 7 * height / 8, width / 3, 20, 20,
//				buses);
//
//		button = new Button(this, 6 * width / 7, 7 * height / 8 - 10, 20, 20,
//				buses);
//		panel = new Panel(this, 1 * width / 20, 4 * height / 5, 9 * width / 10,
//				3 * height / 20, buses);
//
//		controls.add(panel);
//		controls.add(s);
//		controls.add(button);
//
//		Circle grayCircle = new Circle(this, 1 * width / 20 + 60,
//				4 * height / 5 + 40, 30, 30, 1);
//		Circle yellowCircle = new Circle(this, 1 * width / 20 + 100,
//				4 * height / 5 + 40, 30, 30, 0);
//		controls.add(grayCircle);
//		controls.add(yellowCircle);
//
//		int half = (lineNums.size() + 1) / 2;
//
//		for (int i = 0; i < half; i++) {
//			CircleButton cButton = new CircleButton(this, 1 * width / 20 + 150
//					+ i * 35, 4 * height / 5 + 20, 25, 25, buses,
//					lineNums.get(i));
//			controls.add(cButton);
//		}
//
//		for (int i = half; i < lineNums.size(); i++) {
//			CircleButton cButton = new CircleButton(this, 1 * width / 20 + 150
//					+ (i - half) * 35, 19 * height / 20 - 30, 25, 25, buses,
//					lineNums.get(i));
//			controls.add(cButton);
//		}
//		this.registerMethod("mouseEvent", this);
//		frameRate(60);
//		loadCnt = (int) (frameRate * 6);
//
//		busLogo = loadShape("./data/bus_logo.svg");
//		Toolkit tk = Toolkit.getDefaultToolkit();
//		icon = tk.createImage("./data/bus-red-icon.png");
	}

	public void draw() {
		background(0);
		
		this.frame.setTitle("Beijing Bus");
		this.frame.setIconImage(icon);
		map.draw();

		if (loadCnt > 0) {
			fill(255);
			rect(0, 0, width, height);
			shape(busLogo, width / 2 - width / 16f, height / 2 - 1.225f * width
					/ 16, width / 8f, 1.225f * width / 8);
			loadCnt--;
		} else {
//			for (Control control : controls) {
//				control.display();
//			}
//
//			for (Bus bus : buses) {
//				bus.display();
//			}
//
//			if (mousePressed) {
//				for (Bus bus : buses) {
//					bus.clearPreviosPos();
//				}
//
//				for (Control control : controls) {
//					control.setIsDisplay(true);
//				}
//				button.setIsDisplay(true);
//				displayFrameCnt = 100;
//			}
//
//			if (displayFrameCnt > 0) {
//				displayFrameCnt--;
//			} else {
//				for (Control control : controls) {
//					control.setIsDisplay(false);
//				}
//			}
		}
	}
}
