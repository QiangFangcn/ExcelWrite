package org.cnbi.util.writeExcel.jxls2.test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.SpreadsheetVersion;
import org.cnbi.tools.FileUtil;
import org.cnbi.tools.JsonUtil;
import org.cnbi.util.writeExcel.jxls2.base.AbstractExportDispose;
import org.cnbi.util.writeExcel.jxls2.base.ExportConfigUtil;


public class ExportDisposeImp extends AbstractExportDispose {
	
	Pattern p = Pattern.compile(":[\\S]*");
	
	Map<String, Object> cachedb = new HashMap<String, Object>();
	private String dataFileDir;
	
	
	public ExportDisposeImp(){};
	
	public ExportDisposeImp(String dataFileDir){
		this.dataFileDir = dataFileDir;
	};
	
	@Override
	public List getDatas(String sheetName){
		//获取当前sheet配置
		Map<String, Object> currentExportConfig = getCurrentExportConfig();
		//判断是否存在配置或是否是当前sheet页的配置
		if(currentExportConfig == null || !sheetName.equals(currentExportConfig.get("sheetname"))){
			currentExportConfig = getExportConfig(sheetName);
			setCurrentExportConfig(currentExportConfig);
		}
		if(currentExportConfig == null){
			throw new RuntimeException("不存在《"+sheetName+"》sheet页配置！");
		}
		
		int currentpage = ExportConfigUtil.getCurrentpage(currentExportConfig);
		int perpage = ExportConfigUtil.getPerpage(currentExportConfig);
		boolean isNeedPage = ExportConfigUtil.isNeedPage(currentExportConfig);
		List datas = null;
		if(perpage == 0){//无分页  单sheet页涉及多区域的(多sql的)
			
			List sqls = ExportConfigUtil.getSqls(currentExportConfig);
			List sqlskey = ExportConfigUtil.getSqlskey(currentExportConfig);
			
			for(int si=0,sqlsize=sqls.size(); si < sqlsize; si++){
				String sql = (String) sqls.get(si);
				if(sql == null){
					String sqlkey = (String) sqlskey.get(si);
					//获取sql
					sql = getSql(sqlkey);
					if(sql != null){
////						throw new RuntimeException("《"+sqlkey+"》未配置sql！");
//						continue;
						sql = disposeSql(currentExportConfig, sql);
					}
				}
				
				String dataskey = ExportConfigUtil.getDataskey(currentExportConfig, si);
				if(sql != null){
					datas = queryDatas(sql);
				}
				datas = datas==null? new ArrayList<Object>():datas;
				//设置模版数据集
				ExportConfigUtil.put(ExportConfigUtil.getTplParameters(currentExportConfig), dataskey, datas);
			}
		}else{//有分页功能
			
			String sql = ExportConfigUtil.getSql(currentExportConfig);
			if(sql == null){
				String sqlkey = ExportConfigUtil.getSqlkey(currentExportConfig);
				//获取sql
				sql = getSql(sqlkey);
				if(sql == null){
					throw new RuntimeException("《"+sheetName+"》未配置sql！");
				}
				sql = disposeSql(currentExportConfig, sql);
			}
			
			if(currentpage > 0){
				currentpage+=1;
			}else{
				int pagecount = 0;
				currentpage = 1;
				//查询数据数量
				int datacount = getDataCount(sql);
				//2007最大行数
				int maxrow = SpreadsheetVersion.EXCEL2007.getMaxRows();
				//判断预设每页记录数是否大于excel最大行数  如 大于  就重新设置成Excel最大数
				if(perpage > maxrow){
					perpage=maxrow;
					ExportConfigUtil.setPerpage(currentExportConfig, maxrow);
				}
				//是否需要分页
				if(datacount > perpage){
					pagecount = datacount/perpage;
					pagecount += datacount%perpage>0?1:0;
					isNeedPage = true;
					ExportConfigUtil.setNeedPage(currentExportConfig, isNeedPage);
					ExportConfigUtil.setPagecount(currentExportConfig, pagecount);
				}
			}
			//设置当前查询的页
			ExportConfigUtil.setCurrentpage(currentExportConfig, currentpage);
			
			String dataskey = ExportConfigUtil.getDataskey(currentExportConfig, 0);
			
			if(isNeedPage){
				//分页查询 
				datas = queryPageDatas(currentpage, perpage, sql);
				if(datas == null){
					currentpage = 0;
					ExportConfigUtil.setCurrentpage(currentExportConfig, currentpage);
				}
			}else{
				datas = queryDatas(sql);
			}
			datas = datas==null? new ArrayList<Object>():datas;
			//设置模版数据集
			ExportConfigUtil.put(ExportConfigUtil.getTplParameters(currentExportConfig), dataskey, datas);
		}
		
		return datas;
	}
	/**
	 * 查询数据
	 * @param sql
	 * @return
	 */
	private List queryDatas(String sql) {
		List datalist = null;
		String filePath = dataFileDir+sql;
		datalist = (List) cachedb.get(filePath);
		if(datalist == null){
			File f = new File(filePath);
			if(f.exists() && f.isFile()){
				String text =FileUtil.getFileText(f);
				datalist = JsonUtil.json2List(text);
				if(datalist != null){
					cachedb.put(filePath, datalist);
				}
			}
		}
		return datalist;
	}
	/**
	 * 查询分页数据
	 * @param currentpage
	 * @param perpage
	 * @param sql
	 * @return
	 */
	private List queryPageDatas(int currentpage, int perpage, String sql) {
		List datas = null;//new ArrayList();
		int startindex = perpage*(currentpage-1);
		
		List datalist = queryDatas(sql);
		int datasize = datalist.size();
		
		if(datasize > startindex){
			int endindex =startindex+perpage;
			if(datasize < endindex ){
				endindex = datasize;
			}
			datas = new ArrayList(datalist.subList(startindex, endindex));
		}
		
		return datas;
	}
	
	/**
	 * 查询数据记录
	 * @param sql
	 * @return
	 */
	private int getDataCount(String sql) {
		List datalist = queryDatas(sql);
		int coutn = datalist == null ? 0 : datalist.size();
		return coutn;
	}
	
	/**
	 * 获取sql
	 * @param sqlkey
	 * @return
	 */
	private String getSql(String sqlkey) {
		String sql = "";
		return sql;
	}
	
	/**
	 * 处理sql
	 * @param currentExportConfig
	 * @param sql
	 * @return
	 */
	private String disposeSql(Map<String, Object> currentExportConfig, String sql) {
		Matcher m = p.matcher(sql);
		if(m.find()){
			Map<String, Object> map = ExportConfigUtil.getSqlParameters(currentExportConfig);
			for(String key :  map.keySet()){
				sql = sql.replaceAll((key.startsWith(":")?":":"")+key, (map.get(key) == null ?"":map.get(key).toString()));
			}
		}
		return sql;
	}
}