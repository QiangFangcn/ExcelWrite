package org.cnbi.util.writeExcel.jxls2.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jxls.area.Area;
import org.jxls.command.AbstractCommand;
import org.jxls.command.Command;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.common.Size;
import org.jxls.transform.Transformer;
import org.jxls.transform.poi.PoiTransformer;

/**
 * 使用注意，放在主要area的下面的无用单元格中即可
 * 合并单元格：这一列单元格中相同的都按照第一个的样式进行合并
 * 确定合并范围
 * 			往下循环，记录开始行，
 * 			如果当前单元格的CellValue和下一行的CellValue不一样那么他就是结束行
 * 			结束行减去开始行是大于1的，那么就合并
 * 使用方式
 * jx:area
 * jx:mergeRow(start="3" lastCell="A3" endSign="end" mergeBlankCell="1" passBlakCell="1")
 * @author FangQiang
 */

public class MergeRowCommand extends AbstractCommand {
	
	public final Log logger = LogFactory.getLog(MergeRowCommand.class); 
	
	public static final String COMMAND_NAME = "mergeRow";
	public final static String YES = "1";
	public final static String NO = "0";
	
	private String start;				//行的开始行数从 1 开始
	private String endSign;				//结束标志，标志着到自己设置的CellValue就结束
	private String mergeBlankCell = NO;	//是否合并中间的空白
	private String passBlakCell = NO;	//是否跳过中间空白
	private Area area;
	private CellStyle cellStyle;		//备用，后面可能会设计到样式问题
	
	public MergeRowCommand() {
		super();
	}
	
	public MergeRowCommand(String start, Area area) {
		super();
		this.start = start;
		this.area = area;
	}

	public MergeRowCommand(String start, String endSign, Area area, CellStyle cellStyle) {
		super();
		this.start = start;
		this.area = area;
		this.endSign = endSign;
	}
	
	public MergeRowCommand(String start, String endSign, String mergeBlankCell, Area area, CellStyle cellStyle) {
		super();
		this.start = start;
		this.endSign = endSign;
		this.mergeBlankCell = mergeBlankCell;
		this.area = area;
		this.cellStyle = cellStyle;
	}
	
	public MergeRowCommand(String start, String endSign, String mergeBlankCell, String passBlakCell, Area area,
			CellStyle cellStyle) {
		super();
		this.start = start;
		this.endSign = endSign;
		this.mergeBlankCell = mergeBlankCell;
		this.passBlakCell = passBlakCell;
		this.area = area;
		this.cellStyle = cellStyle;
	}

	@Override
	public Command addArea(Area area) {
		if (super.getAreaList().size() >= 1) {
			throw new IllegalArgumentException("You can add only a single area to 'mergeRow' command");
		}
		this.area = area;
		return super.addArea(area);
	}

	@Override
	public String getName() {
		return MergeRowCommand.COMMAND_NAME;
	}
	
	/**
	 * 合并命令，有点耦合度过高，后面改写， 还需要把列一次配置, 这里还需要多次配置
	 */
	@Override
	public Size applyAt(CellRef cellRef, Context context) {
		Transformer transformer = this.getTransformer();
		if (!(transformer instanceof PoiTransformer)) {
			logger.error("不是Poi的Transfomer无法解析合并");
			return area.applyAt(cellRef, context);
		}
		
		PoiTransformer poiTransformer = ((PoiTransformer) transformer);
		Workbook workbook = poiTransformer.getWorkbook();
		Sheet sheet = workbook.getSheet(cellRef.getSheetName());
		
		// 得到当前的列
		int col = cellRef.getCol();
		
		if (!isNumeric(start)) {
			logger.error("输入的start不是一个数字");
			return area.applyAt(cellRef, context);
		}
		
		// 得到开始的行
		int curRowumn = getStartColumn();
		Row curRow = sheet.getRow(curRowumn);
		// 得到下一行数据
		Row nextRow = sheet.getRow(curRowumn+1);
		// 结束行数
		int endNum = 0;
		// 当前行的值
		String curCellValue = null;
		Cell cell = null;
		Cell nextCell = null;
		//第一次循环找到结束的地方
		while(true) {
			curRow = sheet.getRow(curRowumn);
			//如果获取不到当前行了，那么说明Excel到底了,上行为结束行
			if(curRow == null) {
				endNum = curRowumn;
				break;
			}
			//得到当前列的这个单元格
			cell = curRow.getCell(col);
			curCellValue = getCellValue(cell);
			//如果设置了endSign则会碰到endSign为结束位
			if(endSign != null && curCellValue.equals(endSign)) {//这样写不好每次就算没有endSign都要进行判断后面需要改写
				endNum = curRowumn;
				break;
			}
			//如果不跳过空值，那么就需要碰到空值就结束，或者在上面的循环到Excel结束就结束
			if(NO.equals(passBlakCell)) {
				//如果获取到的当前行的CellValue是空的，那么说明合并区域结束了，说明上行为结束行
				if(curCellValue.equals("")) {
					endNum = curRowumn;
					break;
				}
			}
			//每次结束再递增行
			curRowumn++;
		}
		
		logger.info(cellRef.getCellName()+"的"+"当前结束行为"+"-->"+endNum);
		//重置curRowmn到开始
		curRowumn = getStartColumn();
		curRow = sheet.getRow(curRowumn);
		//为合并做准备
		int regionStartRow = curRowumn;
		int regionEndRow = 0;
		String startRowValue = getCellValue(curRow.getCell(col));
		String nextRowValue = getCellValue(nextRow.getCell(col));	
		curRow = sheet.getRow(curRowumn);	
		
		//第二次循环这一列的所有行根据条件进行合并
		for (int i = curRowumn; i <= endNum; i++) {
			curRow = sheet.getRow(i);
			nextRow = sheet.getRow(i+1);
			
			if(curRow != null) {
				cell = curRow.getCell(col);
				curCellValue = getCellValue(cell);
			}
			
			if(nextRow != null) {
				nextCell = nextRow.getCell(col);
				nextRowValue = getCellValue(nextCell);
			}
			
			//Excel走到头了, 进行最后一次合并
			if(nextRow == null) {
				nextRowValue = null;
			}
			
			/*
			 * 如果当前循环到行的value和合并区域开始行的value不相同
			 * 那么现在行变为新的开始行
			 * */
			if(!(startRowValue.equals(curCellValue))) {
				regionStartRow = i;
			}
			/*
			 * 如果当前循环到行的value和开始行的value相同并且下一行的value和这一行的不同
			 * 那么可能需要合并当前行和开始行的区域
			 * */
			if(startRowValue.equals(curCellValue) && !(startRowValue.equals(nextRowValue))) {
				regionEndRow = i;
				//如果区域只差大于0，也就是是说至少要两格才可以合并
				if(regionEndRow - regionStartRow > 0) {
					//如果要合并空值,那么直接合并即可
					if(YES.equals(mergeBlankCell)) {
						mergeRegion(regionStartRow, regionEndRow, col, col, sheet);
					}
					//如果是不合并空值 而 当前合并的值不是空值	那么就合并
					if(NO.equals(mergeBlankCell) && !"".equals(startRowValue)) {	
						mergeRegion(regionStartRow, regionEndRow, col, col, sheet);
					}
				}
				//不论合并没合并，startRow都要往下走
				regionStartRow = regionEndRow+1;
				if(nextRow != null) {
					startRowValue = getCellValue(nextRow.getCell(col));
				} else {	//下一行为null说明结束了
					break;
				}
			}
			
		}
		return area.applyAt(cellRef, context);
	}
	
	/**
	 * 得到初始行数
	 * @return
	 */
	private int getStartColumn() {
		int curRowumn = Integer.parseInt(start);
		curRowumn = curRowumn - 1;
		return curRowumn;
	}
	
	/**
	 * 合并单元格命令
	 * @param regionStartRow	开始行
	 * @param regionEndRow		结束行
	 * @param regionStartCol	开始列
	 * @param regionEndCol		结束列
	 * @param sheet
	 */
	private void mergeRegion(int regionStartRow, int regionEndRow, int regionStartCol, int regionEndCol, Sheet sheet) {
		CellRangeAddress region = new CellRangeAddress(regionStartRow, regionEndRow, regionStartCol, regionEndCol);
		sheet.addMergedRegion(region);
	}
	
	/**
	 * 获取Cell的值
	 * @param cell	从row获取到的cell
	 * @return
	 */
	public static String getCellValue(Cell cell) {
		if(cell == null) {
			return "";
		}
		int cellType = cell.getCellType();
		if(cellType == 0) {
			double d = cell.getNumericCellValue();
			String str = String.valueOf(d);
			return str;
		}
		if(cellType == 1) {
			return cell.getStringCellValue();
		}
		if(cellType == 3) {
			return "";
		}
		if(cellType == 2) {
			String formula = cell.getCellFormula();
			return formula;
		}
		return null;
	}

	/**
	 * 利用正则表达式判断字符串是否是数字
	 * @param str
	 * @return
	 */
	public boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
	
	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}
	
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public CellStyle getCellStyle() {
		return cellStyle;
	}

	public void setCellStyle(CellStyle cellStyle) {
		this.cellStyle = cellStyle;
	}

	public MergeRowCommand(String start) {
		super();
		this.start = start;
	}

	public String getEndSign() {
		return endSign;
	}

	public void setEndSign(String endSign) {
		this.endSign = endSign;
	}

	public String getMergeBlankCell() {
		return mergeBlankCell;
	}

	public void setMergeBlankCell(String mergeBlankCell) {
		this.mergeBlankCell = mergeBlankCell;
	}

	public String getPassBlakCell() {
		return passBlakCell;
	}

	public void setPassBlakCell(String passBlakCell) {
		this.passBlakCell = passBlakCell;
	}


}
