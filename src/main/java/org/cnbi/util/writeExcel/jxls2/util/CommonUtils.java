package org.cnbi.util.writeExcel.jxls2.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工具类
 * @author FangQiang
 * @date 2018/1/29
 */
public class CommonUtils {
	private CommonUtils(){}
	
	/**
	 * 将List<Map>转化成带key的Map<String,Map>，提取List<Map>的一列做为key
	 * @param list		要转换的List<Map>
	 * @param sCode		提取的List<Map>中Map的key，要求那些Map都必须有相同key值
	 * @return
	 */
	public static Map<String, Map> castListToKeyMap(List<Map> list, String sCode) {
		Map<String, Map> resultMap = new HashMap<>();
		String keyName = "";
		for (Map map : list) {
			keyName = String.valueOf( map.get(sCode) );
			resultMap.put(keyName, map);
		}
		return resultMap;
	}


	/**
	 * 读取文件的内容返回字符串
	 *
	 * @param filePath
	 * @return
	 */
	public static String readFileContentToString(Path filePath) {
		String content = "";
		try (InputStreamReader is = new InputStreamReader( Files.newInputStream( filePath ), "UTF-8" );) {
			try (BufferedReader reader = new BufferedReader( is );) {
				String line;
				StringBuilder builder = new StringBuilder();
				while ((line = reader.readLine()) != null) {
					builder.append( line );
				}
				content = builder.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
}
