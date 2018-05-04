package org.cnbi.util.writeExcel.jxls2.commandfordata;

import java.util.List;
import java.util.Map;
import org.jxls.area.Area;
import org.jxls.command.AbstractCommand;
import org.jxls.command.Command;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.common.Size;
/**
 * 处理了现金流量分析表的数据的数据
 * 废弃，只用这一次
 * 想处理数据请用DataHandler2.0
 * @author FangQiang
 *
 */
@Deprecated
public class DataHandler extends AbstractCommand {
	
	public static final String COMMAND_NAME = "dataHandler";
	@Override
	public String getName() {
		return DataHandler.COMMAND_NAME;
	}
	
	@Override
	public Command addArea(Area area) {
		return super.addArea(area);
	}
	
	@Override
	public Size applyAt(CellRef cellRef, Context context) {
		if(context.getVar("data") != null){
			dataHandleListToMap(context);
		}
		if(context.getVar("datas") != null){
			removeNullValue(context);
		}
		return new Size(1,1);
	}
	
	/**
	 * 把分组中空的数据变为字符串
	 * @param context
	 */
	private void removeNullValue(Context context) {
		List<Map> datas = (List) context.getVar("datas");
		for (Map<String, Object> map : datas) {
			if(map.get("dim_customer")==null){
				map.put("dim_customer", " ");
			}
		}
		
	}

	/*
	 * 转换处理data
	 */
	private void dataHandleListToMap(Context context) {
		List<Map> list = (List<Map>) context.getVar("data");
		Map<String, Map> map = CommonUtils.castListToKeyMap(list, "SCODE");
		context.putVar("data", map);
	}
}



