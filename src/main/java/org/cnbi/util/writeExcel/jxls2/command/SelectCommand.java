package org.cnbi.util.writeExcel.jxls2.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jxls.area.Area;
import org.jxls.command.AbstractCommand;
import org.jxls.command.Command;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.common.Size;
import org.jxls.expression.EvaluationException;
import org.jxls.expression.ExpressionEvaluator;
import org.jxls.expression.JexlExpressionEvaluator;

/**
 * jx:select(items="datas" var="data" key="t1" select="data.DIM_ITEM==457010101"  lastCell="A3")
 * @author FangQiang
 * 筛选出需要的数据放入context中
 */
public class SelectCommand extends AbstractCommand{
	public final Log logger = LogFactory.getLog(SelectCommand.class); 
	
	public static final String COMMAND_NAME = "select";
	private String items;
	private String var;    //得到的分组名
	private String select;
	private String key;
	private Area area;
	
	public SelectCommand() {
	}
	
	public SelectCommand(String items, String var, String select, String key) {
		super();
		this.items = items;
		this.var = var;
		this.select = select;
		this.key = key;
	}
	
	public Command addArea(Area area) {
        if (super.getAreaList().size() >= 1) {
            throw new IllegalArgumentException("You can add only a single area to 'select' command");
        }
        this.area = area;
        return super.addArea(area);
    }

	@Override
	public String getName() {
		return SelectCommand.COMMAND_NAME;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Size applyAt(CellRef cellRef, Context context) {
		
		/*得到ExperssionEvaluator*/
		ExpressionEvaluator selectEvaluator = null;
		if (select != null) {
			///2.4.3
            //selectEvaluator = JxlsHelper.getInstance().createExpressionEvaluator(select);
			//2.4.0
			selectEvaluator = new JexlExpressionEvaluator(select);
        }
		
		/*最终被过滤过后的数据*/
		List filterList  = new ArrayList<>();
		Object object = context.getVar(items);
		
		if(object == null && !(object instanceof List)) {
			logger.error("items不是一个List集合，或者Item集合不存在！！！");
			context.putVar(key, filterList);
//			area.applyAt(cellRef, context);
			return area.applyAt(cellRef, context);
		}
		List dataList = (List) object;
		
		if(dataList.size() <= 0) {
			logger.error("items声明的List是空List！！！");
			context.putVar(key, new ArrayList<>());
//			area.applyAt(cellRef, context);
			return area.applyAt(cellRef, context);
		}
		
		for (Object obj : dataList) {
			if(obj != null && obj instanceof Map) {
				Map lineData = (Map) obj;
				/*放入var为键，作为判断条件和Each的select一样，用这个键作为select的判断值*/
				context.putVar(var, lineData);
				if (selectEvaluator != null && isConditionTrue(selectEvaluator, lineData)) {
					filterList.add(lineData);
		            continue;
		        }
			}
		}
		context.putVar(key, filterList);
		context.removeVar(var);
		logger.info("当前--《《"+key.toString()+"》》"+"的大小为"+filterList.size());
		//重复利用这个单元格
		//area.applyAt(cellRef, context);
		return area.applyAt(cellRef, context);
	}
	
	/**
	 * 判断当前行数据符不符合要求
	 * @param evaluator
	 * @param lineData
	 * @return true 数据符合要求, false数据不符合要求
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean isConditionTrue(ExpressionEvaluator evaluator, Map lineData) {
		Object conditionResult = evaluator.evaluate(select, lineData);
		if (!(conditionResult instanceof Boolean)) {
		      throw new EvaluationException(
		          "Condition result is not a boolean value at /cnbi-mvc/src/main/java/org/cnbi/util/exportXSL/command/SelectCommand.java");
		    }
		return (Boolean) conditionResult;
	}
	
	
	public String getItems() {
		return items;
	}

	public void setItems(String items) {
		this.items = items;
	}
	
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public String getSelect() {
		return select;
	}

	public void setSelect(String select) {
		this.select = select;
	}	
}
