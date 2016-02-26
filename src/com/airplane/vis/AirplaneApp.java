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

import com.airplane.entity.Flight;
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
import de.fhpotsdam.unfolding.utils.StyleConstants;

public class AirplaneApp extends PApplet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 95402802576670615L;
	UnfoldingMap map;
	final String DATA_DIRECTORY = "./data/";
	final String IMAGE_DIRECTORY = DATA_DIRECTORY + "ui/";
	final String MAP_DIRECTORY = DATA_DIRECTORY + "tiles/";
	Location beijingLocation = new Location(37.3505, 106.842);// new Location(39.9f, 116.3f);

	int initZoomLevel = 5;
	int oldZoomLevel = 0;
	float progress = 0;
	int loadCnt = 0;
	PShape airplaneLogo;
	PShape airplaneIcon;
	Image icon;
	
	MarkerManager<Marker> markerManager;

	int displayFrameCnt = 80;
	
	PImage airplane;
	PImage locationIcon;
	
	List<FlightTrack> tracks = new ArrayList<FlightTrack>();
	int counter = 0;

	public static void main(String[] agrs) {
		PApplet.main(new String[] { "com.airplane.vis.AirplaneApp", "--full-screen" });
	}
    public boolean sketchFullScreen() {
    	return true;
    }
    
    
	public FlightTrack loadData(String path) {
		List<Record> records;
		FlightTrack track = null;
		
		RecordArrayFileReader fileReader = new RecordArrayFileReader(
				path,
    			",",
    			19);
    	
    	try {
    		records = fileReader.getRecordList();
			
			for (Record r : records) {
				System.out.println(r);
			}
			track = new FlightTrack(records.get(0).getFlight(), records);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return track;
	}
	
	private void initFlightTracks(List<FlightTrack> tracks, String filePath) {
//		tracks = new ArrayList<FlightTrack>();
		try {
			File file = new File(filePath);
			if (file.isDirectory()) {
				System.out.println(file.getAbsolutePath());
				String[] filelist = file.list();
				for (int i = 0; i < filelist.length; i++) {
					File readfile = new File(filePath + "/" + filelist[i]);
					if (!readfile.isDirectory()) {
						String name = readfile.getName();
						System.out.println("path=" + readfile.getPath());
						System.out.println("absolutepath="
								+ readfile.getAbsolutePath());
						System.out.println("name=" + name);

						FlightTrack track = loadData(readfile.getPath());
						if (track != null) {
							tracks.add(track);
						}
					} else {
						// readfile(filePath + "/" + filelist[i]);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("readfile()   Exception:" + e.getMessage());
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
		MapUtils.createDefaultEventDispatcher(this, map);
		
		frameRate(60);
		loadCnt = (int) (frameRate * 6);

		airplaneLogo = loadShape(IMAGE_DIRECTORY + "airplane-logo.svg");
		Toolkit tk = Toolkit.getDefaultToolkit();
		icon = tk.createImage(IMAGE_DIRECTORY + "airplane-icon.png");
		
		airplaneIcon = loadShape(IMAGE_DIRECTORY + "white_Airplane_ok.svg");
		locationIcon = loadImage(IMAGE_DIRECTORY + "location_blue_10x14.png");
		counter = 0;
		
		markerManager = AirportsMarkerManager();
		map.addMarkerManager(markerManager);
	}

	public void draw() {
		background(0);
		
		this.frame.setTitle("Airplane Vis");
		this.frame.setIconImage(icon);
		map.draw();
		
		if (loadCnt > 0) {
			
			
			fill(255);
			rect(0, 0, width, height);
			pushStyle();
			shapeMode(CENTER);
			
			shape(airplaneLogo, width / 2, height / 2, 
					width / 5f, width / 5f);

			popStyle();
//			loadData();
			initFlightTracks(tracks, DATA_DIRECTORY + "records");
			loadCnt--;
		} else {
			
			int zoomLevel = map.getZoomLevel();
			if (oldZoomLevel != zoomLevel) {
				if (zoomLevel < 6) {
					markerManager.disableDrawing();
				} else {
					markerManager.enableDrawing();
				}

				oldZoomLevel = zoomLevel;
			}
			
			counter++;
			
			if (counter % 10 == 0) {
				for (FlightTrack track : tracks) {
					track.update();
				}
			}
			
			for (FlightTrack track : tracks) {
				track.render();
			}
			
			if (counter > 1000) {
				counter = 0;
			}
//			if (index < records.size()) {
//				counter++;
//				
//				ScreenPosition airplaneCurScreenPos = map.getScreenPosition(records.get(index).getLoc());
//				
//				pushMatrix();
//				translate(airplaneCurScreenPos.x, airplaneCurScreenPos.y);
//				
//				if (index + 1 < records.size()) {
//					ScreenPosition headingPos = map.getScreenPosition(records.get(index+1).getLoc());
//					PVector v = PVector.sub(
//							airplaneCurScreenPos,
//							headingPos
//					);
//					
//					float angle = PVector.angleBetween(v, new PVector(1, 0));
//					
//                    if (v.y < 0) {
//                        rotate(PI -angle);
//                    } else {
//                        rotate(PI + angle);
//                    }
////                    ctx.shape(busShapeRight, 0, 0, 75, 25);
//					
//					
////					rotate(-angle);
//				}
//				
//				shape(airplaneIcon, 0, 0, 20, 20);
//				popMatrix();
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
	
	class FlightTrack {
		Flight flight;
		List<Record> records;
		int index = 0;
		
		int imgWidth = 20;
		int imgHeight = 20;
		ScreenPosition airplaneCurScreenPos;
		PFont font = loadFont(DATA_DIRECTORY + "ui/OpenSans-12.vlw");
		int fontSize = 12;
		int space = 6;
		protected int strokeWeight = StyleConstants.DEFAULT_STROKE_WEIGHT;
		protected int highlightColor = StyleConstants.HIGHLIGHTED_FILL_COLOR;
		protected int highlightStrokeColor = StyleConstants.HIGHLIGHTED_STROKE_COLOR;
		
		public FlightTrack() {
			
		}
		
		public FlightTrack(Flight flight, List<Record> records) {
			this.flight = flight;
			this.records = records;
		}
		
		public void render() {
			airplaneCurScreenPos = map.getScreenPosition(records.get(index).getLoc());
			
			pushStyle();
			pushMatrix();
			translate(airplaneCurScreenPos.x, airplaneCurScreenPos.y);
			
			if (index + 1 < records.size()) {
				ScreenPosition headingPos = map.getScreenPosition(records.get(index+1).getLoc());
				PVector v = PVector.sub(
						airplaneCurScreenPos,
						headingPos
				);
				
				float angle = PVector.angleBetween(v, new PVector(1, 0));
				
                if (v.y < 0) {
                    rotate(PI -angle);
                } else {
                    rotate(PI + angle);
                }
//                ctx.shape(busShapeRight, 0, 0, 75, 25);
				
				
//				rotate(-angle);
			}
			
			shapeMode(CENTER);
			shape(airplaneIcon, 0, 0, imgWidth, imgHeight);
			popMatrix();
			popStyle();
			
			if (isInside(mouseX, mouseY, airplaneCurScreenPos.x, airplaneCurScreenPos.y)) {
				// label
				pushStyle();
				pushMatrix();
				Record r = records.get(index);
				if (flight != null) {
					String info = "[loc=" + r.getLoc()
							+ ", height=" + r.getHeight() 
							+ ", flight=" + r.getFlight();
					//flight.getID() + flight.getFlightNo() + flight.getLoc();
					if (font != null) {
						textFont(font);
					}
					fill(highlightColor);
					stroke(highlightStrokeColor);
					rect(airplaneCurScreenPos.x + strokeWeight / 2, airplaneCurScreenPos.y - fontSize + strokeWeight / 2 - space, textWidth(info) + space * 1.5f,
							fontSize + space);
					fill(255, 255, 255);
					text(info , Math.round(airplaneCurScreenPos.x + space * 0.75f + strokeWeight / 2),
							Math.round(airplaneCurScreenPos.y + strokeWeight / 2 - space * 0.75f));
				}
				popMatrix();
				popStyle();
			}
		}
		
		public void update() {
			if (index + 1 < records.size()) {
				index ++;
			}
		}
		
		public boolean isInside(float checkX, float checkY, float x, float y) {
			return checkX > x - imgWidth / 2 
					&& checkX < x + imgWidth / 2 
					&& checkY > y - imgHeight / 2
					&& checkY < y + imgHeight / 2;
		}
	}
}
