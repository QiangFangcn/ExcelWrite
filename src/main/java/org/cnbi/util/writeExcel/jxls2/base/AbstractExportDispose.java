package org.cnbi.util.writeExcel.jxls2.base;

import java.util.Map;

/**
 * 导出处理
 * @author Administrator
 *
 */
public abstract class AbstractExportDispose implements ExportDispose {
	/**
	 * 导出配置集合
	 * <sheetname, config>
	 */
	private Map<String,ExportConfig> exportConfigMap ;
	/**
	 * 当前导出配置
	 */
	private ExportConfig currentExportConfig;
	
	/**
	 * 获取当前导出配置
	 * @return
	 */
	public ExportConfig getCurrentExportConfig(){
		return currentExportConfig;
	}
	
	@Override
	public boolean isNeedPage() {
		return currentExportConfig.isNeedPage();
	};
	
	/**
	 * 获取当前页 
	 * return 当前页索引 (从一开始的)
	 */
	public int getCurrentPage(){
		return currentExportConfig.getCurrentpage();
	};
	
	/**
	 *获取数据集键
	 */
	@Override
	public String getDatasKey(){
		return currentExportConfig.getDataskey();
	}
	
	/**
	 * 获取参数
	 */
	@Override
	public Map<String, Object> getParameters(){
		return currentExportConfig.getTplParameters();
	}
	
//	@Override
//	public List getDatas(String sheetName){
//		setNeedPage(false);
//		setCurrentPage(0);
//		return null;
//	};
	
	public ExportConfig getExportConfig(String sheetname){
		return exportConfigMap.get(sheetname);
	}
	
	@Override
	public Map<String,ExportConfig> getExportConfigMap() {
		return exportConfigMap;
	}
	
	@Override
	public void setExportConfigMap(Map<String,ExportConfig> exportConfigMap) {
		this.exportConfigMap = exportConfigMap;
	}

}
