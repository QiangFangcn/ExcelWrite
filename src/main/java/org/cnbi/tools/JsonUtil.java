package org.cnbi.tools;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

public class JsonUtil {
	
	public static Map<?,?> json2Map(String laststr) {
		return JSON.parseObject(laststr, Map.class);
	}
	
	public static JSONArray json2List(String text){
		return JSON.parseArray(text);
	}
	
}
