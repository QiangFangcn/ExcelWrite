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
 * 判断是否是合并公司然后写对应的
 * 只使用一次
 * 废弃
 * @author FangQiang
 */
public class IsComboCompanySignHandler extends AbstractCommand{
	
	public static final String COMMAND_NAME = "isComboCompanySignHandler";
	@Override
	public String getName() {
		return IsComboCompanySignHandler.COMMAND_NAME;
	}
	
	@Override
	public Command addArea(Area area) {
		// TODO Auto-generated method stub
		return super.addArea(area);
	}

	@Override
	public Size applyAt(CellRef cellRef, Context context) {
		isComboCompanySignHandler(context);
		return new Size(1,1);
	}

	@SuppressWarnings("unchecked")
	private void isComboCompanySignHandler(Context context) {
		List<Map> data = (List<Map>) context.getVar("data");
		boolean isComboCompanySign = true;
		if(data.size() == 1) {
			isComboCompanySign = false;
		}
		context.putVar("isComboCompanySign", isComboCompanySign);
	}

}
