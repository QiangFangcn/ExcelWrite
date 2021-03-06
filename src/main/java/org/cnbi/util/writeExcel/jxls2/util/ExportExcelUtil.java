package org.cnbi.util.writeExcel.jxls2.util;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.cnbi.util.writeExcel.jxls2.command.*;
import org.cnbi.util.writeExcel.jxls2.entity.ExcelExportOptions;
import org.cnbi.util.writeExcel.jxls2.entity.Page;
import org.cnbi.util.writeExcel.jxls2.command.ConvertListMapToMapMap;
import org.cnbi.util.writeExcel.jxls2.command.CommonFunctions;
import org.cnbi.util.writeExcel.jxls2.exception.ObjectNotExistException;
import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.formula.FastFormulaProcessor;
import org.jxls.transform.poi.PoiContext;
import org.jxls.transform.poi.PoiTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by FangQiang on 2018/4/12
 * 这个是Excel导出的工具类，
 */
public class ExportExcelUtil {

    private static final Logger logger = LoggerFactory.getLogger( ExportExcelUtil.class );

    /*辅助模版Sheet页前缀*/
    private static final String TEMPLATE_PREFIX = "1_1";

    private ExportExcelUtil() {
    }

    /**
     * 用一个List的Excel配置信息去批量导出
     *
     * @param exportOptionsList
     */
    public static void useOptionsToExportExcel(List<ExcelExportOptions> exportOptionsList) throws IOException {
        long l1 = System.currentTimeMillis();
        for (ExcelExportOptions exportOptions : exportOptionsList) {
            String outPath = exportOptions.getOutPath();
            String tplPath = exportOptions.getTplPath();
            Path path = Paths.get( outPath );
            if (!Files.exists( path.getParent() )) {
                Files.createDirectories( path.getParent() );
            }
            Map<String, ExcelExportOptions.SheetInfo> sheetInfos = exportOptions.getSheets();
            try (FileInputStream is = new FileInputStream( tplPath )) {
                try (OutputStream os = new FileOutputStream( outPath )) {
                    ExportExcel( is, os, sheetInfos );
                } catch (InvalidFormatException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long l2 = System.currentTimeMillis();
        logger.info( "使用了{}毫秒用于Excel的模板导出计算部分", l2 - l1 );
    }

    /**
     * 导出一个Excel根据传入的sheetInfo
     *
     * @param is
     * @param os
     * @param sheetInfos
     */
    public static void ExportExcel(InputStream is, OutputStream os, Map<String, ExcelExportOptions.SheetInfo> sheetInfos) throws IOException, InvalidFormatException {
        if (is == null) {
            //is = Files.newInputStream( Paths.get( sheetInfos ) )
        }
        Workbook workbook = WorkbookFactory.create( is );
//        Transformer poiTransformer = PoiTransformer.createSxssfTransformer(workbook, 100000, false, true); // 似乎在这种模板中无法使用
        List<Area> xlsAreaList = null;
        PoiTransformer poiTransformer = null;
        try {
            poiTransformer = PoiTransformer.createTransformer( workbook );
            // 可以用于设置在单元格中填写的表达式可用的方法
            JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) poiTransformer.getTransformationConfig().getExpressionEvaluator();
            HashMap<String, Object> function = new HashMap<>();
            function.put( "utils", CommonFunctions.getSingleton() );
            evaluator.getJexlEngine().setFunctions( function );
            AreaBuilder areaBuilder = new XlsCommentAreaBuilder( poiTransformer );
            // 主要用到以下5个命令
            XlsCommentAreaBuilder.addCommandMapping( "eachp", EachPlusCommand.class );
            XlsCommentAreaBuilder.addCommandMapping( "mergeRow", MergeRowCommand.class );
            XlsCommentAreaBuilder.addCommandMapping( "select", SelectCommand.class );
            XlsCommentAreaBuilder.addCommandMapping( "switch", SwitchCommand.class );
            XlsCommentAreaBuilder.addCommandMapping( "dataHandler2", ConvertListMapToMapMap.class );
            xlsAreaList = areaBuilder.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 防止这些没有创造出来
        if (poiTransformer == null || xlsAreaList == null) {
            throw new ObjectNotExistException( "创建poiTransformer、xlsAreaList出现错误" );
        }
        Context context = new PoiContext();
        // 用set去除重复
        HashSet<String> templateSheetNameSet = new HashSet<>();
        for (Area xlsArea : xlsAreaList) {
            CellRef cellref = xlsArea.getStartCellRef();
            String templateName = cellref.getSheetName();
            String sheetName = templateName;
            if (templateName.contains( TEMPLATE_PREFIX )) {
                sheetName = templateName.replaceFirst( TEMPLATE_PREFIX, "" );
                templateSheetNameSet.add( templateName );
            }
            // 根据sheetName得到这个Area的sheet页的信息
            ExcelExportOptions.SheetInfo sheetInfo = sheetInfos.get( sheetName );
            // 如果没有sheetInfo得到说明不需要导出这个表    (你需要设置一个空的表样，为导不出的数据做一个表样)
            if (sheetInfo != null) {
                // 分页处理
                if (sheetInfo.getPageSizes() != null && sheetInfo.getPageSizes().size() > 0) {
                    // 一个area只有一个context里面的每种类型数据都有可能去分页
                    // 1. 求分组数据
                    int pageNum = 1;
                    ArrayList<Boolean> isEndList = new ArrayList<Boolean>();
                    while (true) {
                        isEndList.clear();
                        int dataKeySize = sheetInfo.getDataKeys().size();
                        // 获取分页的数据
                        for (int i = 0; i < dataKeySize; i++) {
                            if (sheetInfo.getPageSizes().size() > i && sheetInfo.getPageSizes().get( i ) != null && sheetInfo.getPageSizes().get( i ) != 0) {
                                // 找出分页数据
                                Integer size = sheetInfo.getPageSizes().get( i );
                                List<Map> contextTemp = sheetInfo.getContext().get( i );
                                Page page = new Page( sheetInfo.getContext().get( i ), contextTemp.size() / size + 1, contextTemp.size(), size, pageNum );
                                List data = PageUtils.getCurrentPageData( page );
                                if (null == data) {
                                    context.putVar( sheetInfo.getDataKeys().get( i ), new ArrayList<Map>() );
                                    isEndList.add( true );
                                } else {
                                    context.putVar( sheetInfo.getDataKeys().get( i ), data );
                                }
                            } else {// 2. 如果是固定数据一起放入当前页的数据
                                context.putVar( sheetInfo.getDataKeys().get( i ), sheetInfo.getContext().get( i ) );
                            }
                        }
                        // 放入维度信息
                        context.toMap().putAll( sheetInfo.getDimensions() );
                        // 跳出的原因是什么？？ 分页的数据都达到极限，各个分页数据都不可以再分页了
                        if (isEndList.size() == sheetInfo.getRealPagingSize()) break;
                        CellRef curCellRef = new CellRef( sheetName + pageNum, cellref.getRow(), cellref.getCol() );
                        ++pageNum;
                        try {
                            xlsArea.setFormulaProcessor( new FastFormulaProcessor() );
                            xlsArea.applyAt( curCellRef, context );
                            xlsArea.processFormulas();
                        } catch (Exception e) {
                            System.out.println( "在" + sheetName + "第" + (pageNum - 1) + "页上发现以下错误" );
                            e.printStackTrace();
                        }
//                        context.toMap().clear();
                    }
                } else {// 非分页
                    // 获取SheetInfo中保存的数据们
                    ArrayList<List<Map>> contextList = sheetInfo.getContext();
                    ArrayList<String> dataKeys = sheetInfo.getDataKeys();
                    context.toMap().putAll( sheetInfo.getDimensions() );
                    for (int i = 0; i < contextList.size(); i++) {
                        List<Map> mapList = contextList.get( i );
                        String dataKey = dataKeys.get( i );
                        context.toMap().put( dataKey, mapList );
                    }
                    CellRef curCellRef = new CellRef( sheetName, cellref.getRow(), cellref.getCol() );
                    // 设置公式处理器
                    xlsArea.setFormulaProcessor( new FastFormulaProcessor() );
                    // 渲染
                    xlsArea.applyAt( curCellRef, context );
                    // 处理公式
                    xlsArea.processFormulas();
                }
            }
            context.toMap().clear();
        }
        // 删除模板
        for (String tName : templateSheetNameSet) {
            poiTransformer.deleteSheet( tName );
        }
        try {
            poiTransformer.getWorkbook().write( os );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}