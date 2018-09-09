package org.cnbi.util.writeExcel.jxls2;

import org.cnbi.util.writeExcel.jxls2.util.CommonUtils;
import org.cnbi.util.writeExcel.jxls2.entity.ExcelExportOptions;
import org.cnbi.util.writeExcel.jxls2.util.ExportExcelUtil;
import org.cnbi.util.writeExcel.jxls2.util.JsonUtils;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Created by FangQiang on 2018/5/3
 * 这个类模仿了Controller层 从前端获取数据 ，去 数据库 查询数据 导出Excel，可以稍作修改获取数据方法在Web中使用
 * 就没有压缩，返回路径，下载而已
 */
public class ExcelExportExample {
    private static final String HEAD_FILE_NAME = "head.json";
    private static final String CONFIG_FILE_NAME = "config.json";
    private static final String SQL_DATA_FILE_NAME = "sqldata.json";
    private static final String RESOURCE_ROOT = "jxls";
    // 文件统一存放的目录
    private static final Path RES_DIR = Paths.get( System.getProperty("user.dir"), "src", "main", "resources", RESOURCE_ROOT );
    // 配置文件目录 （包括 head 前端配置 config 后端配置 sqldata 相当于sql查询出来的数据）
    private static final Path CONFIG_DIR = Paths.get( RES_DIR.toString(), "config" );
    // 导出目录
    private static final Path OUT_DIR = Paths.get( RES_DIR.toString(), "out" );
    // 模版文件目录
    private static final Path TEMPLATE_DIR = Paths.get( RES_DIR.toString(), "template" );

    /**
     * 导出模拟
     *
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @Test
    void demo1() throws IOException {
        // 模拟获取前端数据
        /**
         * 前端数据格式说明
         * excels: [{"id": "ryxlsjtjb", "name": "人员效率数据统计表", "modelid": "rlzy", "sheets": ["1"]}]
         * 一个数组，里面存放的每个要导出的Sheet页的信息，其中
         *  id对应后台excel的id部分
         *  name对应后台excel中的name的配置
         *  modelid是后台配置中excel所配置的model中
         *  sheets是后台excel总所对应sheet页的orderNumber，是决定导出哪几张的条件
         */
        String head = CommonUtils.readFileContentToString( Paths.get( CONFIG_DIR.toString(), HEAD_FILE_NAME ) );
        Map headMap = JsonUtils.getInstance().readValue( head, Map.class );

        // 模拟获取导出信息配置文件
        String content = CommonUtils.readFileContentToString( Paths.get( CONFIG_DIR.toString(), CONFIG_FILE_NAME ) );
        Map excelConfigMap = JsonUtils.getInstance().readValue( content, Map.class );

        // 前端请求的excels 的信息
        List excelsInfosOfRequest = (List) headMap.get( "excels" );
        // 所有导出表配置集合
        List<ExcelExportOptions> exportOptionsList = new ArrayList<ExcelExportOptions>();

        // 循环excels信息，将需要导出的excel表的配置得到，放入exportOptionsList中
        for (Object excelInfoOfRequest : excelsInfosOfRequest) {
            // 取出当前表信息
            Map excelInfoMapOfRequest = (Map) excelInfoOfRequest;
            String excelIdOfExcelsFromRequest = (String) excelInfoMapOfRequest.get( "id" );
            List sheetsOfExcelsFromRequest = (List) excelInfoMapOfRequest.get( "sheets" );
            String moduleIdOfExcelsFromRequest = (String) excelInfoMapOfRequest.get( "modelid" );

            // 取出配置信息
            Map moduleConfig = (Map) excelConfigMap.get( moduleIdOfExcelsFromRequest );
            Object obj = moduleConfig.get( excelIdOfExcelsFromRequest );
            // 将对应excelId的excel配置得到，并转换为excelExportOptions
            ExcelExportOptions excelExportOptions = JsonUtils.getInstance().convertValue( obj, ExcelExportOptions.class );

            // 得到excelExportOptions中需要导出的Sheet页
            Map<String, ExcelExportOptions.SheetInfo> sheetInfoMap = excelExportOptions.getSheets();
            // 需要导出的Sheet信息
            Map<String, ExcelExportOptions.SheetInfo> needToExportSheetInfoMap = new HashMap<>();
            for (Map.Entry<String, ExcelExportOptions.SheetInfo> sheetInfoEntry : sheetInfoMap.entrySet()) {
                // 这里的sheetInfoEntry.getKey是Sheet页的名字，sheetInfoEntry.getValue才是对应Sheet页的相信信息
                ExcelExportOptions.SheetInfo entryValue = sheetInfoEntry.getValue();
                Integer orderNumber = entryValue.getOrderNumber();
                if (sheetsOfExcelsFromRequest.size() == 0) { // 有些表只有一张没有加，所以这里加上
                    sheetsOfExcelsFromRequest.add( "1" );
                }
                if (sheetsOfExcelsFromRequest.contains( orderNumber + "" )) {
                    // 这里只是模拟了从数据库中查询数据，并没有进行参数替换什么的，取的数据放入对应sheetInfo的Context中
                    sqlHandler( entryValue.getSqls(), entryValue.getContext() );
                    dimHandler( entryValue.getDimensions(), headMap );
                    needToExportSheetInfoMap.put( sheetInfoEntry.getKey(), entryValue );
                }
            }
            excelExportOptions.setSheets( needToExportSheetInfoMap );
            String tplName = excelExportOptions.getTplPath();
            excelExportOptions.setTplPath( Paths.get( TEMPLATE_DIR.toString(), excelExportOptions.getTplPath() ).toString() );
            if (excelExportOptions.getOutPath() != null) {
                excelExportOptions.setOutPath( Paths.get( OUT_DIR.toString(), excelExportOptions.getOutPath() ).toString() );
            } else {
                excelExportOptions.setOutPath( Paths.get( OUT_DIR.toString(), tplName ).toString() );
            }
            exportOptionsList.add( excelExportOptions );
        }
        ExportExcelUtil.useOptionsToExportExcel( exportOptionsList );
    }

    /**
     * 放入维度参数
     * @param curSheetDim
     * @param headMap
     */
    private void dimHandler(Map<String, Object> curSheetDim, Map headMap) {
        curSheetDim.putAll( headMap );
    }

    /**
     * 放入SQL参数
     * @param sqls
     * @param context
     * @throws IOException
     */
    private void sqlHandler(ArrayList<String> sqls, ArrayList<List<Map>> context) throws IOException {
        String sqlData = CommonUtils.readFileContentToString( Paths.get( CONFIG_DIR.toString(), SQL_DATA_FILE_NAME ) );
        Map sqlDataMap = JsonUtils.getInstance().readValue( sqlData, Map.class );
        List<Map> data = new ArrayList<>();
        for (String sql : sqls) {
            data = (List<Map>) sqlDataMap.get( sql );
            context.add( data );
        }

    }


}
