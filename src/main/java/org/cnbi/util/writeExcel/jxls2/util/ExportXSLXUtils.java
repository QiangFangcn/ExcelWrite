package org.cnbi.util.writeExcel.jxls2.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.cnbi.util.writeExcel.jxls2.base.ExportDispose;
import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.formula.FastFormulaProcessor;
import org.jxls.transform.Transformer;
import org.jxls.transform.poi.PoiContext;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.util.JxlsHelper;
import org.jxls.util.TransformerFactory;
/**
 * Excel导出工具
 * @author xjw
 *
 */

	


	public class ExportXSLXUtils {
		private ExportXSLXUtils() {}
		private static JxlsHelper jxlshelper = null;
		public static JxlsHelper getInstanceJxlsHelper(){
			if(jxlshelper == null){
				jxlshelper = JxlsHelper.getInstance();
	//			XlsCommentAreaBuilder.addCommandMapping("commandname", myCommand.class);
				
			}
			return jxlshelper;
		}
	
		/**
		 * 多sheet页导出
		 * @param is
		 * @param os
		 * @param sheetmap sheet页数据
		 * 	{
		 * 		sheet页名称:{ 数据集名称：数据集(list)[行数据(map)] }
		 * 		sheet1 : { datas : [{}] },
		 * 		sheet2 : { datas : [{}],infos:[{}] }
		 * 	}
		 * @param delsheetnamelist 
		 * 		
		 * @throws IOException
		 */
		public static void exportMoreSheet(InputStream is, OutputStream os, Map<String,Map<String, 
				Object>> sheetmap, List<String> delsheetnamelist) throws IOException, org.apache.poi.openxml4j.exceptions.InvalidFormatException{
			
			String perfix = "1_1"; 
			Transformer transformer = TransformerFactory.createTransformer(is, os);
			AreaBuilder areaBuilder = new XlsCommentAreaBuilder(transformer);

			areaBuilder.setTransformer(transformer);
			Context context = new Context();
			List<Area> xlsAreaList = areaBuilder.build();
			for (Area xlsArea : xlsAreaList) {
				CellRef cellref = xlsArea.getStartCellRef();
				String sheetname = cellref.getSheetName();
				String s = sheetname;
				boolean isperfix = false;
				if(sheetname.startsWith(perfix)){
					s = sheetname.replaceFirst(perfix, "");
					isperfix = true;
				}
				Map<String, Object> datamap = sheetmap.get(s);
				if(datamap != null){
					Object[] sets =  datamap.keySet().toArray();
					for(int i = 0, size = sets.length; i < size ; i++){
						String key = (String) sets[i];
						Object o = datamap.get(key);
						context.putVar(key, o);
	//					handleKey(key, context);
					} 
	//				System.out.println(cellref.getCellName());
					String cellName = cellref.getCellName();
					if(isperfix){
						cellName = cellName.replaceFirst(perfix, "");
					}
					xlsArea.applyAt(new CellRef(cellName), context);
					/// 多页的时候用
	//				xlsArea.setFormulaProcessor(new StandardFormulaProcessor());
	                xlsArea.setFormulaProcessor(new FastFormulaProcessor());
	                xlsArea.processFormulas();
					context.toMap().clear();
				}
//				System.out.println(sheetname);
				if(isperfix){
					transformer.deleteSheet(sheetname);
				}
			}
//			if(delsheetnamelist != null && delsheetnamelist.size() > 0 ) {
//				for (String delSheetName : delsheetnamelist) {
//					transformer.deleteSheet(delSheetName);
//				}
//			}
			
			transformer.write();
		}
		
	/**
	 * 按模版导出07版Excel
	 * 	支持：多sheet页 、大数据分页
	 * @param is
	 * @param os
	 * @param exportDispose
	 * @throws IOException
	 * @throws EncryptedDocumentException
	 * @throws org.apache.poi.openxml4j.exceptions.InvalidFormatException
	 */
	public static void exportOfXSSF(InputStream is, OutputStream os, ExportDispose exportDispose)
			throws IOException, EncryptedDocumentException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
		String perfix = "1_1";
		Workbook workbook = WorkbookFactory.create(is);

		Transformer transformer = PoiTransformer.createSxssfTransformer(workbook, 100000, false);

		AreaBuilder areaBuilder = new XlsCommentAreaBuilder(transformer, true);
		List<Area> xlsAreaList = areaBuilder.build();
		Context context = new PoiContext();
		String datasKey = "datas";
		for (Area xlsArea : xlsAreaList) {
			CellRef cellref = xlsArea.getStartCellRef();
			String sheetname = cellref.getSheetName();
			String s = sheetname;
			boolean isperfix = false;
			if (sheetname.contains(perfix)) {
				s = sheetname.replaceFirst(perfix, "");
				isperfix = true;
			}

			try {
				List datas = exportDispose.getDatas(s);
				boolean needpage = exportDispose.isNeedPage();
				int currentPage = exportDispose.getCurrentPage();
				String currsheet=s;
				
				copyParameters(exportDispose, context);
				
				datasKey = exportDispose.getDatasKey();
				
				//需要分页处理
				if(needpage){
					context.putVar(datasKey, datas);
					while (currentPage > 0) {
						currsheet = s+currentPage;
						CellRef currentCellRef = new CellRef(currsheet,cellref.getRow(),cellref.getCol());
						xlsArea.applyAt(currentCellRef, context);
						xlsArea.setFormulaProcessor(new FastFormulaProcessor());
						xlsArea.processFormulas();
						
						datas = exportDispose.getDatas(s);
						currentPage = exportDispose.getCurrentPage();
					}
				}else if(currentPage > 0){//单页显示
					context.putVar(datasKey, datas);
					CellRef currentCellRef = new CellRef(currsheet,cellref.getRow(),cellref.getCol());
					xlsArea.applyAt(currentCellRef, context);
					xlsArea.setFormulaProcessor(new FastFormulaProcessor());
					xlsArea.processFormulas();
				}
				
				context.toMap().clear();
				
				if (isperfix) {
					transformer.deleteSheet(sheetname);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		((PoiTransformer) transformer).getWorkbook().write(os);
	}
	
	/**
	 * 拷贝参数
	 * @param exportDispose
	 * @param context
	 */
	private static void copyParameters(ExportDispose exportDispose, Context context) {
		Map parametersMap = exportDispose.getParameters();
		if(parametersMap != null && !parametersMap.isEmpty()){
			for(Object key : parametersMap.keySet()){
				context.putVar((String) key, parametersMap.get(key));
			}
		}
	}

	
}

