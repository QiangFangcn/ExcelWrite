package org.cnbi.util.writeExcel.poi.test;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.cnbi.util.writeExcel.poi.base.POIUtiles;
import org.cnbi.util.writeExcel.poi.base.StyleBean;

public class Test1 {
	
	public static void main(String[] args) {
		
		HashMap<StyleBean, CellStyle> cacheStyleMap = new HashMap<StyleBean, CellStyle>();
		
		SXSSFWorkbook wb = new SXSSFWorkbook(10);  
		// 新建sheet 页  
		SXSSFSheet sheet = null; 
		// 创建 row 第一行对象  
		Row row = null;   
		
		//开始时间  
		long  startTime = System.currentTimeMillis();  
		System.out.println("strat execute time: " + startTime);  
		
		
		try {
//		for(){
		sheet = (SXSSFSheet) wb.createSheet();
//		XSSFDrawing patriarch = (XSSFDrawing) sheet.createDrawingPatriarch();
		
		row = sheet.createRow(0);
		Cell cell = row.createCell(0);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		cell.setCellValue("A1");
		CellStyle style = POIUtiles.getStyle(cacheStyleMap, wb, getStyleBean());
		cell.setCellStyle(style);
		
		row = sheet.createRow(1);
		Cell cell1 = row.createCell(1);
		cell1.setCellType(Cell.CELL_TYPE_STRING);
		cell1.setCellValue("B2");
		CellStyle style1 = POIUtiles.getStyle(cacheStyleMap, wb, getStyleBean2());
		cell1.setCellStyle(style1);
		
//		}
		
		
		
			wb.write(new FileOutputStream("D:/testExcel.xlsx"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//开始时间  
		long  endTime = System.currentTimeMillis();  
		System.out.println("end execute time: " + endTime);  
		
	}
	
	
	public static StyleBean getStyleBean(){
		StyleBean styleBean= new StyleBean(XSSFCellStyle.VERTICAL_CENTER, XSSFCellStyle.ALIGN_CENTER, 
				null, POIUtiles.color2Hex(Color.white), 
				POIUtiles.color2Hex(Color.black), XSSFFont.DEFAULT_FONT_NAME, (int)XSSFFont.DEFAULT_FONT_SIZE, 
				1, false, XSSFFont.U_NONE);
		return styleBean;
	}
	
	public static StyleBean getStyleBean2(){
		
		String black16Str = POIUtiles.color2Hex(Color.black);
		String white16Str = POIUtiles.color2Hex(Color.white);
//		String red16Str = POIUtiles.color2Hex(Color.red);
		
		StyleBean styleBean = new StyleBean(XSSFCellStyle.VERTICAL_CENTER, XSSFCellStyle.ALIGN_CENTER, 
				1, 1, 1, 1, 
				black16Str, black16Str, black16Str, black16Str, 
				"@", XSSFCellStyle.NO_FILL, white16Str, black16Str, 
				black16Str, XSSFFont.DEFAULT_FONT_NAME, (int)XSSFFont.DEFAULT_FONT_SIZE, 
				1, false, XSSFFont.U_NONE, false, (int)XSSFFont.SS_NONE);
		
		return styleBean;
	}
	
	
	
}
