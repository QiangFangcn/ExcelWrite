package org.cnbi.util.writeExcel.jxls2.command;


import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jxls.area.Area;
import org.jxls.command.AbstractCommand;
import org.jxls.command.Command;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.common.Size;
import org.jxls.transform.Transformer;
import org.jxls.transform.jexcel.JexcelTransformer;
import org.jxls.transform.poi.PoiCellData;
import org.jxls.transform.poi.PoiTransformer;

import jxl.write.WriteException;

/**
 * @author FangQiang
 * 合并单元格命令
 */
public class MergeCommand extends AbstractCommand {
    private String cols;    //合并的列数
    private String rows;    //合并的行数
    private Area area;
    private CellStyle cellStyle;    //第一个单元格的样式
 
    @Override
    public String getName() {
        return "merge";
    }
 
    @Override
    public Command addArea(Area area) {
        if (super.getAreaList().size() >= 1) {
            throw new IllegalArgumentException("You can add only a single area to 'merge' command");
        }
        this.area = area;
        return super.addArea(area);
    }
 
    @Override
    public Size applyAt(CellRef cellRef, Context context) {
        int rows = 1, cols = 1;
        // 利用Apache的JEXL引擎解析我们的Command Markup,返回需要合并的行数和列数
        if(StringUtils.isNotBlank(this.rows)){
            Object rowsObj = getTransformationConfig().getExpressionEvaluator().evaluate(this.rows, context.toMap());
            if(rowsObj != null && NumberUtils.isDigits(rowsObj.toString())){
                rows = NumberUtils.toInt(rowsObj.toString());
            }
        }
        if(StringUtils.isNotBlank(this.cols)){
            Object colsObj = getTransformationConfig().getExpressionEvaluator().evaluate(this.cols, context.toMap());
            if(colsObj != null && NumberUtils.isDigits(colsObj.toString())){
                cols = NumberUtils.toInt(colsObj.toString());
            }
        }
        //根据不同的Transformer用不同的引擎去解析
        if(rows > 1 || cols > 1){
            Transformer transformer = this.getTransformer();
            if(transformer instanceof PoiTransformer){
                return poiMerge(cellRef, context, (PoiTransformer)transformer, rows, cols);
            }else if(transformer instanceof JexcelTransformer){
                return jexcelMerge(cellRef, context, (JexcelTransformer)transformer, rows, cols);
            }
        }
        
        area.applyAt(cellRef, context);
        return new Size(1, 1);
    }
    
    /**
     * Poi合并单元格
     * @param cellRef
     * @param context
     * @param transformer
     * @param rows
     * @param cols
     * @return
     */
    protected Size poiMerge(CellRef cellRef, Context context, PoiTransformer transformer, int rows, int cols){
        Sheet sheet = transformer.getWorkbook().getSheet(cellRef.getSheetName());
        CellRangeAddress region = new CellRangeAddress(
                cellRef.getRow(), 
                cellRef.getRow() + rows - 1, 
                cellRef.getCol(), 
                cellRef.getCol() + cols - 1);
        sheet.addMergedRegion(region);
 
        //合并之后单元格样式会丢失，以下操作将合并后的单元格恢复成合并前第一个单元格的样式
        area.applyAt(cellRef, context);
        if(cellStyle == null){
            PoiCellData cellData = (PoiCellData)transformer.getCellData(cellRef);
            cellStyle = cellData.getCellStyle();
        }
        setRegionStyle(cellStyle, region, sheet);
        //row 和 cols 就是改变的区域
        return new Size(cols, rows);
    }
 
    protected Size jexcelMerge(CellRef cellRef, Context context, JexcelTransformer transformer, int rows, int cols){
        try {
            transformer.getWritableWorkbook().getSheet(cellRef.getSheetName())
                .mergeCells(
                        cellRef.getRow(), 
                        cellRef.getCol(), 
                        cellRef.getRow() + rows - 1 , 
                        cellRef.getCol() + cols - 1);
            area.applyAt(cellRef, context);
        } catch (WriteException e) {
            throw new IllegalArgumentException("合并单元格失败");
        }
        return new Size(cols, rows);
    }
 
    private static void setRegionStyle(CellStyle cs, CellRangeAddress region, Sheet sheet) {
        for (int i = region.getFirstRow(); i <= region.getLastRow(); i++) {
            Row row = sheet.getRow(i);
            if (row == null)
                row = sheet.createRow(i);
            for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++) {
                Cell cell = row.getCell(j);
                if (cell == null) {
                    cell = row.createCell(j);
                }
                cell.setCellStyle(cs);
            }
        }
    }
 
    public String getCols() {
        return cols;
    }
 
    public void setCols(String cols) {
        this.cols = cols;
    }
 
    public String getRows() {
        return rows;
    }
 
    public void setRows(String rows) {
        this.rows = rows;
    }

}
