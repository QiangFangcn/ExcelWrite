package org.cnbi.util.writeExcel.jxls2.base;

import java.util.HashMap;
import java.util.Map;

public class ExportConfig {
	private HashMap<String, Object> configMap=new HashMap<String, Object>();
	
	public ExportConfig() {
	}
	
	public void put(String key, Object value){
		configMap.put(key, value);
	}
	
    public Object get(String key) {
    	return configMap.get(key);
    }
	
	public String getSql() {
		return get("sql") instanceof String ? (String) get("sql") : null;
	}

	public void setSql(String sql) {
		put("sql",sql);
	}

	public String getSqlkey() {
		return get("sqlkey") instanceof String ? (String) get("sqlkey") : null;
	}

	public void setSqlkey(String sqlkey) {
		put("sqlkey",sqlkey);
	}

	public int getPerpage() {
		Object perPageObj = get("perpage");
		return perPageObj instanceof Integer ? (int)perPageObj : 0 ;
	}

	public void setPerpage(int perpage) {
		put("perpage",perpage);
	}
	
	public int getPagecount(){
		Object pageCountObj = get(ExportConfigKey.PageCount);
		return pageCountObj instanceof Integer ? (int)pageCountObj : 0 ;
	}
	
	public void setPagecount(int pagecount){
		put(ExportConfigKey.PageCount, pagecount);
	}
	
	public int getCurrentpage(){
		Object currentPageObj = get(ExportConfigKey.CurrentPage);
		return currentPageObj instanceof Integer ? (int)currentPageObj : 0 ;
	}
	
	public void setCurrentpage(int currentpage) {
		put(ExportConfigKey.CurrentPage, currentpage);
	}
	
	/**
	 * 设置是否需要分页处理
	 * @param needPage
	 */
	public void setNeedPage(boolean needPage){
		put("needpage", needPage);
	}
	
	/**
	 * 是否需要分页处理
	 * 
	 */
	public boolean isNeedPage(){
		Object obj = get("needpage");
		return obj instanceof Boolean ? (boolean)obj:false;
	}
	
	public String getDataskey(){
		Object dataskey = get("dataskey");
		return dataskey instanceof String ? (String)dataskey : "datas" ;
	}
	
	public void setDataskey(String dataskey){
		put("dataskey",dataskey);
	}
	
	public Map<String, Object> getTplParameters(){
		Object tplparametersObj = get("tplparameters");
		if(tplparametersObj instanceof Map){
		}else{
			tplparametersObj = new HashMap<String, Object>();
			setTplParameters((HashMap<String, Object>)tplparametersObj);
		}
		return (Map<String, Object>)tplparametersObj;
	}
	public void setTplParameters(HashMap<String, Object> tplparameters){
		put("tplparameters",tplparameters);
	}


}
