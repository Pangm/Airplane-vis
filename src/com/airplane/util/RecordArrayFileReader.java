package com.airplane.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.airplane.entity.Flight;
import com.airplane.entity.Record;

import de.fhpotsdam.unfolding.geo.Location;

/**
 * Created by pangm on 2/25/16.
 * 读取飞机轨迹的信息数据
 * 默认基站属性是采用'|'作为分隔符
 * 文件可以有注释行，注释行开头第一个字符必须是'#'或'%'
 * 空行会自动被跳过
 *
 * @author Weizhu Xie (<a href="weizhushieh@gmail.com">email</a>,
 */
public class RecordArrayFileReader {
    private String filepath;
    private BufferedReader br;
    private boolean isOpen = false;
    private int columns = 0;
    private int rows = 0;

    private String delimiter = "\\|";

    /**
     * 创建 ArrayFileReader
     * @param filepath {@link String} 文件路径
     */
    public RecordArrayFileReader(String filepath, String delimiter, int columns) {
        this.filepath = filepath;
        this.delimiter = delimiter;
        this.columns = columns;
    }

    private void openArrayFile()
        throws FileNotFoundException, IOException {

        if (isOpen) {
            return;
        }

//        br = new BufferedReader(new FileReader(this.filepath));
        br = new BufferedReader(new InputStreamReader(new FileInputStream(this.filepath), "utf-8"));
        rows = 0;
        for (String oLine = br.readLine(); oLine != null; oLine = br.readLine()) {
            // 将行首的空格和指标符删掉
            String line = oLine.replaceFirst("^[ \t]*", "");
            if (line.startsWith("#") || line.startsWith("%")) {
                // 注释行
                continue;
            }

            String[] attributes = line.split(this.delimiter);
            if (attributes.length == 0) {
                // 空行
                continue;
            }

            int columnsHere = 0;
            for (int i = 0; i < attributes.length; i++) {
                if (attributes[i].equals("")) {
                    columnsHere++;
                }
            }

            if (columnsHere == 0) {
                continue;
            }

            if (columnsHere != columns) {
                throw new IOException(
                        String.format(
                                "(\"%s\") number of columns %d on row %d does not match number on lines ",
                                oLine,
                                columnsHere,
                                rows,
                                columns
                        )
                );
            }
            rows++;

        }
        br.close();

//        br = new BufferedReader(new FileReader(filepath));
        br = new BufferedReader(new InputStreamReader(new FileInputStream(this.filepath), "utf-8"));
        isOpen = true;
    }

    private  void closeArrayFile() throws IOException {
        br.close();
        isOpen = false;
    }

    public List<Record> getRecordList() throws Exception {
        List<Record> recordList = new ArrayList<Record>();
        openArrayFile();
//        String comment0 = new String("#".getBytes(), "UTF-8");
//        String comment1 = new String("%".getBytes(), "UTF-8");
        boolean isFirst = true;

        for (String oLine = br.readLine(); oLine != null; oLine = br.readLine()) {
            if (isFirst) {
//                oLine = oLine.substring(1); // skip utf-8 flag '\uFEFF'
                isFirst = false;
            }

            // 将行首的空格和指标符删掉
            String line = oLine.replaceFirst("^[ \t]*", "");

            if (line.startsWith("#") || line.startsWith("%")) {
//            if (line.length() > 0 && (line.charAt(0) == '#' || line.charAt(0) == '%')) {
                // 注释行
                continue;
            }

            String[] attributes = line.split(this.delimiter);
            if (attributes.length == 0) {
                // 空行
                continue;
            }
            try {
            	
            	String flightID = attributes[0];
            	Location loc = new Location(
            			Float.parseFloat(attributes[2]), 
            			Float.parseFloat(attributes[3])
            	);
            	
            	float height = Float.parseFloat(attributes[5]);
            	
            	Date time = new Date(Long.parseLong(attributes[11]) * 1000);
            	
            	String codeSrcAirport = attributes[12];
            	String codeDestAirport = attributes[13];
            	
            	String flightNo = attributes[14];
            	
            	Flight flight = new Flight();
            	flight.setID(flightID);
            	flight.setFlightNo(flightNo);
            	flight.setLoc(loc);
            	
            	Record record = new Record(flight, loc, time, height);
            	
            	recordList.add(record);
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception(
                        String.format(
                                "(\"%s\") on row %d does not match!",
                                oLine,
                                rows
                        )
                );
            }
        }

        closeArrayFile();
        return recordList;
    }
    
    
//    public static void main(String[] args) {
//    	RecordArrayFileReader fileReader = new RecordArrayFileReader(
//    			"./data/data/8d9f5a0.txt",
//    			",",
//    			19);
//    	
//    	try {
//			List<Record> recordList = fileReader.getRecordList();
//			
//			for (Record r : recordList) {
//				System.out.println(r);
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	
//    }
}
