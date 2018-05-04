package org.cnbi.util.writeExcel.jxls2.commandfordata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jxls.area.Area;
import org.jxls.command.AbstractCommand;
import org.jxls.command.Command;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.common.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 暂时用于处理逾期的Grid命令
 * @author FangQiang
 *
 */
public class ProcessDataForDataGrid extends AbstractCommand {
	
	private String headerItems;
	private String bodyItems;
	private Area area;
	 
	public static final String COMMAND_NAME = "processItemsForGrid";
	
	private static Logger logger = LoggerFactory.getLogger(ProcessDataForDataGrid.class);
	@Override
	public String getName() {
		return COMMAND_NAME;
	}

	@Override
	public Size applyAt(CellRef cellRef, Context context) {
		Object header = context.getVar(headerItems);
		Object body = context.getVar(bodyItems);
		if(!(header instanceof List) || !(body instanceof List)) {
			logger.error("processItemsForGrid不能处理不是List的headerItems");
			return area.applyAt(cellRef, context);
		}
		List<Map> headersList = (List) header;
		List<Map> bodysList = (List) body;
		List<String> newHeader = new ArrayList<>();
		newHeader.add("客户");
		for (Map map : headersList) {
			String value = (String) map.get("value");
			value = value.replaceAll("\\(", "").replaceAll("\\)", "");
			newHeader.add(value);
		}
		newHeader.add("总计");
		context.putVar("headers", newHeader);
		List<Map> CompanyList = new ArrayList();
		for (Map h : headersList) {
			Map map = new HashMap<>();
			map.put("companyScode", h.get("id"));
			map.put("companyName", h.get("value"));
			CompanyList.add(map);
		}
		List<Map> Cells = new ArrayList<>();
		for (Map bodyCell : bodysList) {
			Map cell = new HashMap<>();
			cell.put("客户", bodyCell.get("CUSTOMERNAME"));
			for (Map company : CompanyList) {
				String companyName = (String) company.get("companyName").toString().replaceAll("\\(", "").replaceAll("\\)", "");
				if(null == bodyCell.get(company.get("companyScode"))) {
					cell.put(companyName, 0.00);
				} else {
					cell.put(companyName, bodyCell.get(company.get("companyScode")));
				}
				
			}
			cell.put("总计", bodyCell.get("TOTAL"));
			Cells.add(cell);
		}
		context.putVar("data", Cells);
		return area.applyAt(cellRef, context);
	}

	public String getHeaderItems() {
		return headerItems;
	}

	public void setHeaderItems(String headerItems) {
		this.headerItems = headerItems;
	}

	public String getBodyItems() {
		return bodyItems;
	}

	public void setBodyItems(String bodyItems) {
		this.bodyItems = bodyItems;
	}
	
    @Override
    public Command addArea(Area area) {
        if (area == null) {
            return this;
        }
        if (this.getAreaList().size() >= 1) {
            throw new IllegalArgumentException("You can add only a single area to 'eachp' command");
        }
        this.area = area;
        return super.addArea(area);
    }
	
}
