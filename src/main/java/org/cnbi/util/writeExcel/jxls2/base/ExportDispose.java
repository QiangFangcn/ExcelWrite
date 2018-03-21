package org.cnbi.util.writeExcel.jxls2.base;

import java.util.List;
import java.util.Map;

public interface ExportDispose {
	
	Map<String, Map<String, Object>> getExportConfigMap();
	
	void setExportConfigMap(Map<String,Map<String, Object>> exportConfigMap);
	
	List getDatas(String sheetName);

	Map<String, Object> getCurrentExportConfig();
	
}
