package org.cnbi.util.writeExcel.jxls2.command;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jxls.area.Area;
import org.jxls.command.AbstractCommand;
import org.jxls.command.Command;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.common.Size;
import org.jxls.util.Util;

/**
 * 使用方法
 * conditions里面的条件用,进行分隔， areas 按照与conditions对应的写
 * jx:switch(conditions="month*1<=3,month*1<=6,month*1<=9,month*1<=12" lastCell="S10" areas=["A3:S4","A5:S6","A7:S8","A9:S10"])
 */
public class SwitchCommand extends AbstractCommand{
	public final Log logger = LogFactory.getLog(SwitchCommand.class);
	public static final String COMMAND_NAME = "switch";
	private String conditions;

	public SwitchCommand() { 
	}
	
	public SwitchCommand(String conditions) {
		this.conditions = conditions;
	}

	public String getConditions() {
		return conditions;
	}

	public void setConditions(String conditions) {
		this.conditions = conditions;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return COMMAND_NAME;
	}
	
	@Override
	public Size applyAt(CellRef cellRef, Context context) {
		String[] conds = conditions.split(",");
		if(super.getAreaList().size() == 0) {
			logger.warn("您的【select】忘记填写【areas】命令了");
		}
		if(conds.length == 0) {
			logger.error("你的【select】命令忘记填写【conditions】命令了");
		}
		
		if(conds.length != super.getAreaList().size()) {
			logger.warn("注意！！！您填写的【conditions】长度和你填写的【areas】长度不一致，可能导致与您的预期不一致。");
		}
		for (int i = 0; i < conds.length; i++) {
			Boolean conditionResult = Util.isConditionTrue(getTransformationConfig().getExpressionEvaluator(), conds[i], context);
			if(conditionResult) {
				logger.info("在"+cellRef.getCellName()+"上的【switch】命令中，第"+(i+1)+"个条件被选作了结果");
				return super.getAreaList().get(i).applyAt(cellRef, context);
			}
		}
		logger.error("您的areas并没有匹配的conditions,默认用了第一个area", new Throwable());
		return super.getAreaList().get(0).applyAt(cellRef, context);
	}
	

	@Override
    public Command addArea(Area area) {
        return super.addArea(area);
    }
	
}
