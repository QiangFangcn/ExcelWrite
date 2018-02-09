package org.cnbi.util.writeExcel.test;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFDrawing;

public class Test1 {
	
	public static void main(String[] args) {
		
		SXSSFWorkbook wb = new SXSSFWorkbook(10000);  
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
//		
//		}
		
	}
	
}
