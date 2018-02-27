package org.cnbi.util.writeExcel.test;


import org.apache.poi.xssf.usermodel.XSSFColor;
import org.cnbi.util.writeExcel.poi.base.POIUtiles;

public class TestPOIUtiles {
	
	public static void main(String[] args) {
//		testGetColor();
		testGetXSSFColor();
		
		
	}
	
	public static void testGetColor(){
		String colorStr = "#FF0000";
		System.out.println("testGetColor:"+colorStr);
		java.awt.Color color = POIUtiles.getColor(colorStr);
		
		int rgb = color.getRGB();
		System.out.println(rgb);
		
		int red = color.getRed();
		int blue = color.getBlue();
		int green = color.getGreen();
		
		System.out.println(red+"_"+blue+"_"+green);
		
	}
	
	public static void testGetXSSFColor(){
		String colorStr = "#FF0000";
		System.out.println("testGetXSSFColor:"+colorStr);
		XSSFColor xssfColor = POIUtiles.getXSSFColor(null, colorStr);
		
		
		
	}
	
	
	
}
