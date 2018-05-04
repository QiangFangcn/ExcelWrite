package org.cnbi.util.writeExcel.jxls2.commandfordata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 常用转化工具类
 * @author FangQiang
 * @date 2018/1/29
 */
public class CommonUtils {
	
	
	
	private CommonUtils(){}
	
	/**
	 * 将list转化成带key的Map，提取list数据中Map的一列做为key
	 * @param list		要转换的list，数据库查询出的list[Map]格式
	 * @param scode		提取的list中的map的主键
	 * @return
	 */
	public static Map<String, Map> castListToKeyMap(List<Map> list, String scode) {
		Map<String, Map> resultMap = new HashMap<>();
		String str = "";
		for (Map oneMap : list) {
			str = (String) oneMap.get(scode);
			resultMap.put(str, oneMap);
		}
		return resultMap;
	}
}
