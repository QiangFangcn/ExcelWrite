package org.cnbi.util.writeExcel.jxls2.test;

import java.util.List;

import org.apache.poi.ss.SpreadsheetVersion;
import org.cnbi.util.writeExcel.jxls2.base.AbstractExportDispose;
import org.cnbi.util.writeExcel.jxls2.base.ExportConfig;

public class ExportDisposeImp extends AbstractExportDispose {

	public ExportDisposeImp() {
	}

	@Override
	public List getDatas(String sheetName){
		//获取当前sheet配置
		ExportConfig currentExportConfig = getCurrentExportConfig();
		//判断是否存在配置或是否是当前sheet页的配置
		if(currentExportConfig == null || !sheetName.equals(currentExportConfig.get("sheetname"))){
			currentExportConfig = getExportConfig(sheetName);
		}
		
		String sql = currentExportConfig.getSql();
		if(sql == null){
			String sqlkey = currentExportConfig.getSqlkey();
			//获取sql
			
			// 处理sql
			
		}
		int currentpage = currentExportConfig.getCurrentpage();
		int perpage = currentExportConfig.getPerpage();
		boolean isNeedPage = currentExportConfig.isNeedPage();
		if(currentpage > 0){
			currentpage+=1;
		}else{
			int pagecount = 0;
			currentpage = 1;
			//查询数据数量
			int datacount = 1000;
			//2007最大行数
			int maxrow = SpreadsheetVersion.EXCEL2007.getMaxRows();
			//判断预设每页记录数是否大于excel最大行数  如 大于  就重新设置成Excel最大数
			if(perpage > maxrow){
				perpage=maxrow;
				currentExportConfig.setPerpage(maxrow);
			}
			//是否需要分页
			if(datacount > perpage){
				pagecount = datacount/perpage;
				pagecount += datacount%perpage>0?1:0;
				isNeedPage = true;
				currentExportConfig.setNeedPage(isNeedPage);
				currentExportConfig.setPagecount(pagecount);
			}
		}
		//设置当前查询的页
		currentExportConfig.setCurrentpage(currentpage);
		
		
		List datas = null;
		if(isNeedPage){
			//分页查询
			
			
			
		}else{
			
			
		}
		return datas;
	}

}
