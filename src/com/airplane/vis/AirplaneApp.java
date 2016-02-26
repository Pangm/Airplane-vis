package com.airplane.vis;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PShape;
import processing.core.PVector;

import com.airplane.entity.Record;
import com.airplane.util.LocReformer;
import com.airplane.util.RecordArrayFileReader;
import com.csvreader.CsvReader;
import com.widgets.AirplaneMarker;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MarkerManager;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

public class AirplaneApp extends PApplet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 95402802576670615L;
	UnfoldingMap map;
	final String DATA_DIRECTORY = "./data/";
	final String IMAGE_DIRECTORY = DATA_DIRECTORY + "ui/";
	final String MAP_DIRECTORY = DATA_DIRECTORY + "tiles/";
	Location beijingLocation = new Location(39.9f, 116.3f);
	List<String> lineNums = new ArrayList<String>();
	List<Location> pathLocs = null;
	int initZoomLevel = 5;
	int oldZoomLevel = 5;
	float progress = 0;
	int loadCnt = 0;
	PShape busLogo;
	PShape airplaneIcon;
	Image icon;
	
	MarkerManager<Marker> markerManager;

	int displayFrameCnt = 80;
	
	PImage airplane;
	PImage locationIcon;
	
	List<Record> records;
	int counter = 0;

	public static void main(String[] agrs) {
		PApplet.main(new String[] { "com.airplane.vis.AirplaneApp", "--full-screen" });
	}
    public boolean sketchFullScreen() {
    	return true;
    }
	public void loadData() {
		RecordArrayFileReader fileReader = new RecordArrayFileReader(
    			"./data/data/8dc2eb6.txt",
    			",",
    			19);
    	
    	try {
    		records = fileReader.getRecordList();
			
			for (Record r : records) {
				System.out.println(r);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setup() {
//		size(1366, 768);
		size(displayWidth, displayHeight);
		if (frame != null) {
            frame.setResizable(true);
        }

		String mbTilesString = sketchPath(MAP_DIRECTORY + "China-City-6-16.mbtiles");

		map = new UnfoldingMap(this, new MBTilesMapProvider(mbTilesString));

		map.zoomToLevel(initZoomLevel);
		map.panTo(beijingLocation);
//		map.setZoomRange(6, 14); // prevent zooming too far out
//		map.setPanningRestriction(beijingLocation, 50);
		MapUtils.createDefaultEventDispatcher(this, map);
		
		frameRate(60);
		loadCnt = (int) (frameRate * 6);

		busLogo = loadShape(IMAGE_DIRECTORY + "bus_logo.svg");
		Toolkit tk = Toolkit.getDefaultToolkit();
		icon = tk.createImage(IMAGE_DIRECTORY + "bus-red-icon.png");
		
		airplaneIcon = loadShape(IMAGE_DIRECTORY + "white_Airplane_ok.svg");
		locationIcon = loadImage(IMAGE_DIRECTORY + "location_blue_10x14.png");
		counter = 0;
		
		markerManager = AirportsMarkerManager();
		map.addMarkerManager(markerManager);
	}

	public void draw() {
		background(0);
		
		this.frame.setTitle("Beijing Bus");
		this.frame.setIconImage(icon);
		map.draw();
//		ScreenPosition bjScreenPos = map.getScreenPosition(beijingLocation);
//		
//		image(locationIcon, bjScreenPos.x, bjScreenPos.y); 
		
		if (loadCnt > 0) {
			fill(255);
			rect(0, 0, width, height);
			shape(busLogo, width / 2 - width / 16f, height / 2 - 1.225f * width
					/ 16, width / 8f, 1.225f * width / 8);
			loadData();
			loadCnt--;
		} else {
			
			int zoomLevel = map.getZoomLevel();
			if (oldZoomLevel != zoomLevel) {
				if (zoomLevel < 5) {
					markerManager.disableDrawing();
				} else {
					markerManager.enableDrawing();
				}

				oldZoomLevel = zoomLevel;
			}
			
			int index = counter / 20 ;
			if (index < records.size()) {
				counter++;
				
				ScreenPosition airplaneCurScreenPos = map.getScreenPosition(records.get(index).getLoc());
				
				pushMatrix();
				translate(airplaneCurScreenPos.x, airplaneCurScreenPos.y);
				
				if (index + 1 < records.size()) {
					ScreenPosition headingPos = map.getScreenPosition(records.get(index+1).getLoc());
					PVector v = new PVector(
							headingPos.x - airplaneCurScreenPos.x,
							headingPos.y - airplaneCurScreenPos.y
					);
					float theta = PVector.angleBetween(v, new PVector(1, 0));
					rotate(-theta);
				}
				
				shape(airplaneIcon, 0, 0, 20, 20);
				popMatrix();
			}
			
			
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
	
	public void mouseMoved() {
		for (Marker marker : map.getMarkers()) {
			marker.setSelected(false);
		}
		Marker marker = map.getFirstHitMarker(mouseX, mouseY);
		if (marker != null)
			marker.setSelected(true);
	}
	
	private MarkerManager<Marker> AirportsMarkerManager() {
		MarkerManager<Marker> markerManager = new MarkerManager<Marker>();
		
		markerManager.addMarkers(getAirportsMarkers(false));

		return markerManager;
	}
	
	
	private List<Marker> getAirportsMarkers(boolean isReformed) {
		ArrayList<Marker> markers = new ArrayList<Marker>();
		
		
		File stationFile = new File(DATA_DIRECTORY + "data/domestic-airports.txt");
        InputStream airportsInputStream = null;
		try {
			airportsInputStream = new FileInputStream(stationFile);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        PFont font = loadFont(DATA_DIRECTORY + "ui/OpenSans-12.vlw");

        CsvReader airportsReader = new CsvReader(
                airportsInputStream,
                ',',
                Charset.forName("utf-8"));

        try {
            airportsReader.readHeaders();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
        try {
            while (airportsReader.readRecord()) {
                String name = airportsReader.get("name");
                String iata = airportsReader.get("iata");
                String icao = airportsReader.get("icao");
                String country = airportsReader.get("country");
                
                float longitude = Float.parseFloat(airportsReader.get("lon"));
                float latitude = Float.parseFloat(airportsReader.get("lat"));
                Location loc;

                if (!isReformed) {
                    loc = LocReformer.reform(new Location(latitude, longitude));
                    System.out.print("Before: "
                                    + longitude
                                    + "---"
                                    + latitude
                    );

                    System.out.println("---After: "
                                    + loc.getLon()
                                    + "---"
                                    + loc.getLat()
                    );
                } else {
                    loc = new Location(latitude, longitude);
                }
                
                
                AirplaneMarker marker = new AirplaneMarker(loc, name, locationIcon, font, 15);

                markers.add(marker);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		return markers;
	}
	
}
