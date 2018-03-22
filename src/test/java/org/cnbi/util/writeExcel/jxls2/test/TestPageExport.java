package org.cnbi.util.writeExcel.jxls2.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.cnbi.tools.FileUtil;
import org.cnbi.tools.JsonUtil;
import org.cnbi.tools.TimeUtil;
import org.cnbi.util.writeExcel.jxls2.util.ExportXSLXUtils;

public class TestPageExport {

	public static void main(String[] args) {
		singerExcel();
	}
	
	public static void singerExcel(){
		String dataFileDir = TestPageExport.class.getResource("/").getPath();
		System.out.println(dataFileDir);
		
		
		
		String infoFilePath=dataFileDir+"jxls2/deom1/deom2_info.json"; 
		File infoFile = new File(infoFilePath);
		if(!infoFile.exists()){
			System.out.println("文件夹不存在："+infoFilePath);
			return ;
		}
		String infojson = FileUtil.getFileText(infoFile);
		Map infomap = JsonUtil.json2Map(infojson);
		
		String templatefilename = (String) infomap.get("tpl");
		String outFileName=(String) infomap.get("out");
		
		Map<String,Map<String, Object>> exportConfigSheetsMap =(Map<String, Map<String, Object>>) infomap.get("sheets");
		
		ExportDisposeImp exportDispose = new ExportDisposeImp(dataFileDir);
		exportDispose.setExportConfigMap(exportConfigSheetsMap);
		
		templatefilename = dataFileDir+templatefilename;
		outFileName = dataFileDir+outFileName;
		
		long s1 = System.currentTimeMillis();
		
		InputStream is = null;
		OutputStream os = null;
		File cachefile = null;
		try {

			is = new FileInputStream(templatefilename);
			os = new FileOutputStream(outFileName);
			
			ExportXSLXUtils.exportOfXSSF(is, os, exportDispose);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null){
				try{
					os.close();
				}catch(Exception ex){
				}
				os = null;
			}
				
			if (is != null){
				try{
					is.close();
				}catch(Exception ex){
				}
				is = null;
			}
			System.gc();
		}
		long s2 = System.currentTimeMillis();
		System.out.println("生成Excel：" + TimeUtil.getDefTime(s2 - s1));
		
	}
	
	

}
