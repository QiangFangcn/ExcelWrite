package org.cnbi.util.writeExcel.jxls2.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportConfigUtil {
//	private Map<String, Object> configMap=new HashMap<String, Object>();

//	public ExportConfig() {
//	}
//	public ExportConfig(Map<String, Object> configMap) {
//		this.configMap =configMap;
//	}

//    public void setConfigMap(Map<String, Object> configMap){
//    	this.configMap =configMap;
//    }
//    
//    public Map<String, Object> getConfigMap(){
//    	return configMap;
//    }

	public static void put(Map<String, Object> configMap, String key, Object value){
		configMap.put(key, value);
	}

    public static Object get(Map<String, Object> configMap, String key) {
    	return configMap.get(key);
    }

    public static List getSqls(Map<String, Object> configMap){
    	Object obj = configMap.get("sqls");
    	return obj instanceof List ? (List)obj:null;
    };

    public static void setSqls(Map<String, Object> configMap,  List sqls){
    	put(configMap, "sqls",sqls);
    }

	public static String getSql(Map<String, Object> configMap) {
		String sql = null;
		List sqls = getSqls(configMap);
		if(sqls != null && sqls.size() > 0){
			Object obj = sqls.get(0);
			sql = obj instanceof String ? (String) obj : null;
		}
		return  sql ;
	}

	public static List getSqlskey(Map<String, Object> configMap) {
		Object obj = get(configMap, "sqlskey");
		return  obj instanceof List ? (List) obj : null;
	}

	public static void setSqlskey(Map<String, Object> configMap, List sqlskey) {
		put(configMap, "sqlskey",sqlskey);
	}

	public static String getSqlkey(Map<String, Object> configMap) {
		String sqlkey = null;
		List sqlskey = getSqlskey(configMap);
		if(sqlskey != null && sqlskey.size() > 0){
			Object obj = sqlskey.get(0);
			sqlkey = obj instanceof String ? (String) obj : null;
		}
		return  sqlkey;
	}

	public static List getSqlsParameters(Map<String, Object> configMap){
		Object sqlsparametersObj = get(configMap, "sqlsparameters");
		if(sqlsparametersObj instanceof List){
		}else{
			sqlsparametersObj = new ArrayList();
			setSqlsParameters(configMap, (List) sqlsparametersObj);
		}
		return (List)sqlsparametersObj;
	}

	public static void setSqlsParameters(Map<String, Object> configMap, List sqlsparameters){
		put(configMap, "sqlsparameters",sqlsparameters);
	}

	public static Map<String, Object> getSqlParameters(Map<String, Object> configMap){
		Object obj = null;
		Map<String, Object> sqlparameters = null;
		List sqlsparameters = getSqlsParameters(configMap);
		if(sqlsparameters.size() > 0){
			obj = sqlsparameters.get(0);
		}
		if(obj instanceof Map){
			sqlparameters = (Map<String, Object>) obj;
		}else{
			sqlparameters = new HashMap<String, Object>();
			sqlsparameters.set(0, sqlparameters);
		}
		return sqlparameters;
	}

	public static List getPerpages(Map<String, Object> configMap) {
		Object obj = get(configMap, "perpages");
		return obj instanceof List ? (List) obj : null;
	}

	public static void setPerpages(Map<String, Object> configMap, List perpages) {
		put(configMap, "perpages",perpages);
	}

	public static int getPerpage(Map<String, Object> configMap) {
		int perpage = 0;
		List perpages = getPerpages(configMap);
		if(perpages != null && perpages.size() > 0){
			Object obj = perpages.get(0);
			perpage = obj instanceof Integer ? (int) obj : null;
		}
		return perpage ;
	}

	public static void setPerpage(Map<String, Object> configMap, int perpage) {
		List perpages = getPerpages(configMap);
		if(perpages != null && perpages.size() > 0){
			perpages.set(0, perpage);
		}
	}

	public static List getDataskeys(Map<String, Object> configMap){
		Object dataskeys = get(configMap, "dataskeys");
		return dataskeys instanceof List ? (List)dataskeys : null ;
	}

	public static String getDataskey(Map<String, Object> configMap, int index){
		String dataskey = "datas";
		List dataskeys = getDataskeys(configMap);
		if(dataskeys != null && dataskeys.size() > 0 && dataskeys.size() > index){
			Object obj = dataskeys.get(index);
			dataskey = obj instanceof String ? (String)obj : "datas" ;
		}
		return dataskey;
	}

	public static Map<String, Object> getTplParameters(Map<String, Object> configMap){
		Object tplparametersObj = get(configMap, "tplparameters");
		if(tplparametersObj instanceof Map){
		}else{
			tplparametersObj = new HashMap<String, Object>();
			setTplParameters(configMap, (HashMap<String, Object>)tplparametersObj);
		}
		return (Map<String, Object>)tplparametersObj;
	}
	public static void setTplParameters(Map<String, Object> configMap, Map<String, Object> tplparameters){
		put(configMap, "tplparameters",tplparameters);
	}

	public static int getPagecount(Map<String, Object> configMap){
		Object pageCountObj = get(configMap, "pagecount");
		return pageCountObj instanceof Integer ? (int)pageCountObj : 0 ;
	}

	public static void setPagecount(Map<String, Object> configMap, int pagecount){
		put(configMap, "pagecount", pagecount);
	}

	public static int getCurrentpage(Map<String, Object> configMap){
		Object currentPageObj = get(configMap, "currentpage");
		return currentPageObj instanceof Integer ? (int)currentPageObj : 0 ;
	}

	public static void setCurrentpage(Map<String, Object> configMap, int currentpage) {
		put(configMap, "currentpage", currentpage);
	}

	/**
	 * 设置是否需要分页处理
	 * @param needPage
	 */
	public static void setNeedPage(Map<String, Object> configMap, boolean needPage){
		put(configMap, "needpage", needPage);
	}

	/**
	 * 是否需要分页处理
	 *
	 */
	public static boolean isNeedPage(Map<String, Object> configMap){
		Object obj = get(configMap, "needpage");
		return obj instanceof Boolean ? (boolean)obj:false;
	}

	/**
	 * 拷贝参数
	 * @param exportDispose
	 * @param context
	 */
	public static void copyParameters(Map<String, Object> configMap, Map<String, Object> context) {
		Map parametersMap = getTplParameters(configMap);
		if(parametersMap != null && !parametersMap.isEmpty()){
			for(Object key : parametersMap.keySet()){
				context.put((String) key, parametersMap.get(key));
			}
		}
		setTplParameters(configMap, context);
	}

}
