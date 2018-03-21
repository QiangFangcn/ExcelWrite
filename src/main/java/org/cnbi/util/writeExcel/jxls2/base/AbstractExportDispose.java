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
	private Map<String,Map<String, Object>> exportConfigMap ;
	/**
	 * 当前导出配置
	 */
	private Map<String, Object> currentExportConfig;
	
	/**
	 * 设置当前导出配置
	 * @param currentExportConfig
	 */
	protected void setCurrentExportConfig(Map<String, Object> currentExportConfig) {
		this.currentExportConfig=currentExportConfig;
	}
	
	/**
	 * 获取当前导出配置
	 * @return
	 */
	@Override
	public Map<String, Object> getCurrentExportConfig(){
		return currentExportConfig;
	}
	
//	@Override
//	public boolean isNeedPage() {
//		return ExportConfigUtil.isNeedPage();
//	};
//	
//	/**
//	 * 获取当前页 
//	 * return 当前页索引 (从一开始的)
//	 */
//	public int getCurrentPage(){
//		return ExportConfigUtil.getCurrentpage();
//	};
	
	public Map<String, Object> getExportConfig(String sheetname){
		return exportConfigMap.get(sheetname);
	}
	
//	/**
//	 *获取数据集键
//	 */
//	@Override
//	public String getDatasKey(){
//		return ExportConfigUtil.getDataskey();
//	}
//	
//	/**
//	 * 获取参数
//	 */
//	@Override
//	public Map<String, Object> getParameters(){
//		return ExportConfigUtil.getTplParameters();
//	}
	
//	@Override
//	public List getDatas(String sheetName){
//		setNeedPage(false);
//		setCurrentPage(0);
//		return null;
//	};
	
	@Override
	public Map<String,Map<String, Object>> getExportConfigMap() {
		return exportConfigMap;
	}
	
	@Override
	public void setExportConfigMap(Map<String,Map<String, Object>> exportConfigMap) {
		this.exportConfigMap = exportConfigMap;
	}

}
