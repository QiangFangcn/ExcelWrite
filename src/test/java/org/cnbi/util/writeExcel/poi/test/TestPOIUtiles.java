package org.cnbi.util.writeExcel.poi.test;


import java.awt.Color;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.cnbi.util.writeExcel.poi.base.POIUtiles;
import org.cnbi.util.writeExcel.poi.base.StyleBean;

import com.alibaba.fastjson.JSONObject;

public class TestPOIUtiles {
	
	public static void main(String[] args) {
//		testGetColor();
//		testGetXSSFColor();
		testFont();
		
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
	
	public static void testFont(){
//		String colorStr = "#FF0000";
		System.out.println("testFont:");
//		StyleBean styleBean = new StyleBean(XSSFCellStyle.VERTICAL_CENTER, XSSFCellStyle.ALIGN_CENTER, 
//				null, 
//				POIUtiles.color2Hex(Color.white), POIUtiles.color2Hex(Color.black), XSSFFont.DEFAULT_FONT_NAME, (int)XSSFFont.DEFAULT_FONT_SIZE, 1, false, XSSFFont.U_NONE);
		String black16Str = POIUtiles.color2Hex(Color.black);
		String white16Str = POIUtiles.color2Hex(Color.white);
		
		StyleBean styleBean = new StyleBean(XSSFCellStyle.VERTICAL_CENTER, XSSFCellStyle.ALIGN_CENTER, 
				1, 1, 1, 1, 
				black16Str, black16Str, black16Str, black16Str, 
				"@", XSSFCellStyle.NO_FILL, white16Str, null, 
				black16Str, XSSFFont.DEFAULT_FONT_NAME, (int)XSSFFont.DEFAULT_FONT_SIZE, 
				1, false, XSSFFont.U_NONE, false, (int)XSSFFont.SS_NONE);
		String styleBeanStr = JSONObject.toJSONString(styleBean);
		
		System.out.println(styleBeanStr);
		
		SXSSFWorkbook wb = new SXSSFWorkbook(10);  
		
		XSSFFont font = POIUtiles.getXSSFFont(wb,styleBean);
		
		
		
	}
	
	
	
	
}
