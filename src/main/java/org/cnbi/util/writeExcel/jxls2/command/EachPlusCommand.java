package org.cnbi.util.writeExcel.jxls2.command;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.cnbi.util.writeExcel.jxls2.command.help.UtilWrapper;
import org.jxls.area.Area;
import org.jxls.command.AbstractCommand;
import org.jxls.command.CellRefGenerator;
import org.jxls.command.Command;
import org.jxls.command.EachCommand;
import org.jxls.command.SheetNameGenerator;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.common.GroupData;
import org.jxls.common.JxlsException;
import org.jxls.common.Size;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 可以实现从list的开始到list指定位置结束的遍历 使用示例 ： jx:eachp(items="list" var="data"
 * lastCell="D3" start="1" end="4" select="data.scode > 1001"
 * shiftMode="adjacent" retainEmpty="true")
 * 循环增加下标变量“var_index”。如var="item"，获取下标方法：${item_index}
 * 新增faxtIndex是循环中被select到的那些排序的下标，过滤了没被选中的数据 使用${item_factIndex}
 * 
 * @author Administrator
 *
 */
public class EachPlusCommand extends AbstractCommand {

	public static final String COMMAND_NAME = "eachp";

	public enum Direction {
		RIGHT, DOWN
	}

	static final String GROUP_DATA_KEY = "_group";

	private String var;
	private String items;
	private String start;
	private String end;
	private String select;
	private Area area;
	private Direction direction = Direction.DOWN;
	private CellRefGenerator cellRefGenerator;
	private String multisheet;
	private String groupBy;
	private String primaryKey;
	private String groupOrder;
	private UtilWrapper util = new UtilWrapper();
	private String retainEmpty; // 当集合大小为0时，是否最少保留一行空行数据

	private static Logger logger = LoggerFactory.getLogger(EachCommand.class);

	public EachPlusCommand() {
	}

	public EachPlusCommand(String var, String items, Direction direction) {
		this.var = var;
		this.items = items;
		this.direction = direction == null ? Direction.DOWN : direction;
	}

	public EachPlusCommand(String items, Area area) {
		this(null, items, area);
	}

	public EachPlusCommand(String var, String items, Area area) {
		this(var, items, area, Direction.DOWN);
	}

	public EachPlusCommand(String var, String items, Area area, Direction direction) {
		this(var, items, direction);
		if (area != null) {
			this.area = area;
			addArea(this.area);
		}
	}

	public EachPlusCommand(String var, String items, String start, String end, Area area) {
		this.var = var;
		this.items = items;
		this.start = start;
		this.end = end;
		this.area = area;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	/**
	 * @param var
	 * @param items
	 * @param area
	 * @param cellRefGenerator
	 */
	public EachPlusCommand(String var, String items, Area area, CellRefGenerator cellRefGenerator) {
		this(var, items, area, (Direction) null);
		this.cellRefGenerator = cellRefGenerator;
	}

	UtilWrapper getUtil() {
		return util;
	}

	void setUtil(UtilWrapper util) {
		this.util = util;
	}

	/**
	 * Gets iteration directino
	 *
	 * @return current direction for iteration
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * Sets iteration direction
	 *
	 * @param direction
	 */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public void setDirection(String direction) {
		this.direction = Direction.valueOf(direction);
	}

	/**
	 * Gets defined cell ref generator
	 *
	 * @return current {@link CellRefGenerator} instance or null
	 */
	public CellRefGenerator getCellRefGenerator() {
		return cellRefGenerator;
	}

	public void setCellRefGenerator(CellRefGenerator cellRefGenerator) {
		this.cellRefGenerator = cellRefGenerator;
	}

	public String getName() {
		return COMMAND_NAME;
	}

	/**
	 * Gets current variable name for collection item in the context during
	 * iteration
	 *
	 * @return collection item key name in the context
	 */
	public String getVar() {
		return var;
	}

	/**
	 * Sets current variable name for collection item in the context during
	 * iteration
	 *
	 * @param var
	 */
	public void setVar(String var) {
		this.var = var;
	}

	/**
	 * Gets collection entity name
	 *
	 * @return collection entity name in the context
	 */
	public String getItems() {
		return items;
	}

	/**
	 * Sets collection entity name
	 *
	 * @param items
	 *            collection entity name in the context
	 */
	public void setItems(String items) {
		this.items = items;
	}

	/**
	 * Gets current 'select' expression for filtering out collection items
	 *
	 * @return current 'select' expression or null if undefined
	 */
	public String getSelect() {
		return select;
	}

	/**
	 * Sets current 'select' expression for filtering collection
	 *
	 * @param select
	 *            filtering expression
	 */
	public void setSelect(String select) {
		this.select = select;
	}

	/**
	 * @return Context variable name holding a list of Excel sheet names to
	 *         output the collection to
	 */
	public String getMultisheet() {
		return multisheet;
	}

	/**
	 * Sets name of context variable holding a list of Excel sheet names to
	 * output the collection to
	 *
	 * @param multisheet
	 */
	public void setMultisheet(String multisheet) {
		this.multisheet = multisheet;
	}

	/**
	 * @return property name for grouping the collection
	 */
	public String getGroupBy() {
		return groupBy;
	}

	/**
	 * @param groupBy
	 *            property name for grouping the collection
	 */
	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	/**
	 * @return group order
	 */
	public String getGroupOrder() {
		return groupOrder;
	}

	/**
	 * @param groupOrder
	 *            group ordering
	 */
	public void setGroupOrder(String groupOrder) {
		this.groupOrder = groupOrder;
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

	public Size applyAt(CellRef cellRef, Context context) {
		if (primaryKey != null && primaryKey.length() > 0) {
			try {
				generatePrimary(context, primaryKey);
			} catch (Exception e) {
				logger.error("{} ==> 注意配置主键的非空，否则这里报错");
				e.printStackTrace();
				return null;
			}
		}
		Iterable itemsCollection = null;
		try {
			itemsCollection = util.transformToIterableObject(getTransformationConfig().getExpressionEvaluator(), items,
					context);
		} catch (Exception e) {
			logger.warn("Failed to evaluate collection expression {}", items, e);
			itemsCollection = Collections.emptyList();
		}
		if (groupBy == null || groupBy.length() == 0) {
			return processCollectionByCell(context, itemsCollection, cellRef, var);
		} else {
			Collection<GroupData> groupedData = util.groupIterable(itemsCollection, groupBy, groupOrder);
			String groupVar = var != null ? var : GROUP_DATA_KEY;
			return processCollectionByCell(context, groupedData, cellRef, groupVar);
		}
	}

	// 当前使用
	private Size processCollectionByCell(Context context, Iterable itemsCollection, CellRef cellRef, String varName) {
		int index = 0;
		int newWidth = 0;
		int newHeight = 0;
		// 实际index的大小
		int factIndex = 0;

		if (start == null) {
			start = "1";
		}

		int begins = Integer.parseInt(start);
		int ends = 0;

		/* 转化一下开始和结束的字符 */
		if (StringUtils.isNotBlank(this.start)) {
			Object rowsObj = getTransformationConfig().getExpressionEvaluator().evaluate(this.start, context.toMap());
			if (rowsObj != null && NumberUtils.isDigits(rowsObj.toString())) {
				begins = NumberUtils.toInt(rowsObj.toString());
			}
		}
		if (StringUtils.isNotBlank(this.end)) {
			Object colsObj = getTransformationConfig().getExpressionEvaluator().evaluate(this.end, context.toMap());
			if (colsObj != null && NumberUtils.isDigits(colsObj.toString())) {
				ends = NumberUtils.toInt(colsObj.toString());
			}
		}

		int itemSize = ((List<Area>) itemsCollection).size();
		if (ends == 0) {
			ends = itemSize;
		}
		// 开始不能比list的size还大，
		if (begins > itemSize + 1) {
			logger.debug("{} ==> 输入的start大于Connection的大小了", cellRef);
			return area.applyAt(cellRef, context);
		}

		CellRefGenerator cellRefGenerator = this.cellRefGenerator;
		if (cellRefGenerator == null && multisheet != null) {
			List<String> sheetNameList = extractSheetNameList(context);
			cellRefGenerator = new SheetNameGenerator(sheetNameList, cellRef);
		}
		CellRef currentCell = cellRefGenerator != null ? cellRefGenerator.generateCellRef(index, context) : cellRef;
		JexlExpressionEvaluator selectEvaluator = null;
		if (select != null) {
			selectEvaluator = new JexlExpressionEvaluator(select);
		}
		Object currentVarObject = context.getVar(varName);
		for (Iterator iterator = itemsCollection.iterator(); iterator.hasNext();) {
			if (index >= ends) {
				break;
			}
			// 更换当前单元格数据
			index++;
			Object obj = iterator.next();
			context.putVar(varName, obj);
			context.putVar(varName + "_index", index);
			// 什么情况开始循环，当开始的行数和循环到的行数相等
			if (index < begins) {
				continue;
			}
			if (selectEvaluator != null && !Util.isConditionTrue(selectEvaluator, context)) {
				context.removeVar(varName);
				continue;
			}
			factIndex++;
			context.putVar(varName + "_factIndex", factIndex);
			Size size = area.applyAt(currentCell, context);
			// index++;
			if (cellRefGenerator != null) {
				newWidth = Math.max(newWidth, size.getWidth());
				newHeight = Math.max(newHeight, size.getHeight());
				if (iterator.hasNext()) {
					currentCell = cellRefGenerator.generateCellRef(index, context);
				}
			} else if (direction == Direction.DOWN) {
				// 通过currentCell.getRow() + size.getHeight()使行数增加
				currentCell = new CellRef(currentCell.getSheetName(), currentCell.getRow() + size.getHeight(),
						currentCell.getCol());
				newWidth = Math.max(newWidth, size.getWidth());
				newHeight += size.getHeight();
			} else {
				currentCell = new CellRef(currentCell.getSheetName(), currentCell.getRow(),
						currentCell.getCol() + size.getWidth());
				newWidth += size.getWidth();
				newHeight = Math.max(newHeight, size.getHeight());
			}
		}

		if ("true".equalsIgnoreCase(retainEmpty) && newWidth == 0 && newHeight == 0) {
			return area.applyAt(currentCell, context);
		}

		if (currentVarObject != null) {
			context.putVar(varName, currentVarObject);
		} else {
			context.removeVar(varName);
		}
		return new Size(newWidth, newHeight);
	}

	/**
	 * 用于在指定数据中产生一个唯一分辨的主键
	 * 
	 * @param context
	 * @param primaryKey
	 * @throws Exception 
	 */
	private void generatePrimary(Context context, String primaryKey) throws Exception {
		List<Map> list = (List<Map>) context.getVar(items);
		String[] primaryArr = primaryKey.split(",");
		for (Map map : list) {
			String primaryValue = "";
			for (String primary : primaryArr) {
				Object obj = map.get(primary);
				if(obj == null) throw new Exception("主键中有不存在的属性,请检查配置的正确性，分组的列需要先筛选非空，可以先用select命令！");
				primaryValue += obj.toString();
			}
			map.put("primaryKey", primaryValue);
		}
	}

	private void evaluateStartAndEnd(Context context, int begins, int ends) {
		/**
		 * 后面需要更佳简单的写
		 */
	}

	private List<String> extractSheetNameList(Context context) {
		try {
			return (List<String>) context.getVar(multisheet);
		} catch (Exception e) {
			throw new JxlsException("Failed to get sheet names from " + multisheet, e);
		}
	}

	public String getRetainEmpty() {
		return retainEmpty;
	}

	public void setRetainEmpty(String retainEmpty) {
		this.retainEmpty = retainEmpty;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

}
