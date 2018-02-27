package org.cnbi.util.writeExcel.test;

import java.awt.Color;
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
		
//		for(){
		sheet = (SXSSFSheet) wb.createSheet();
//		XSSFDrawing patriarch = (XSSFDrawing) sheet.createDrawingPatriarch();
		
		row = sheet.createRow(0);
		Cell cell = row.createCell(0);
		
		
		
		
		
		CellStyle style = POIUtiles.getStyle(cacheStyleMap, wb, getStyleBean());
		
		cell.setCellStyle(style);
		
//		}
		
	}
	
	
	public static StyleBean getStyleBean(){
		StyleBean styleBean= new StyleBean(XSSFCellStyle.VERTICAL_CENTER, XSSFCellStyle.ALIGN_CENTER, 
				null, POIUtiles.color2Hex(Color.white), 
				POIUtiles.color2Hex(Color.black), XSSFFont.DEFAULT_FONT_NAME, (int)XSSFFont.DEFAULT_FONT_SIZE, 1, false, XSSFFont.U_NONE);
//				IndexedColors.BLACK
		
				
		return styleBean;
		
	}
	
	
	
	
	
}
