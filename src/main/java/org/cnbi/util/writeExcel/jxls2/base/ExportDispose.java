package org.cnbi.util.writeExcel.jxls2.base;

import java.util.List;
import java.util.Map;

public interface ExportDispose {
	
	Map<String,ExportConfig> getExportConfigMap();
	
	void setExportConfigMap(Map<String,ExportConfig> exportConfigMap);


	boolean isNeedPage();

	int getCurrentPage();
	
	Map getParameters();
	
	List getDatas(String sheetName);

	String getDatasKey();

}
