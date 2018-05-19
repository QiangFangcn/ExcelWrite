package org.cnbi.util.writeExcel.jxls2.bean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 导出配置信息
 */
public class ExcelExportOptions implements Serializable{
    private static final long serialVersionUID = 123L;
    private String name;                                                // Excel名字
    private String tplPath;                                             // 模板文件路径
    private String outPath;                                             // 模板文件导出路径

    private Map<String, SheetInfo> sheets;                              // 当前Excel的Sheet页信息

    public ExcelExportOptions() {
    }
    public static class SheetInfo {
        private ArrayList<String> dataKeys;                             // Sheet页中获取数据的键
        private ArrayList<Integer> pageSizes;                           // 页面的大小
        private ArrayList<String> sqls;                                 // SQL语句的键，也可以是SQL语句，但处理方式就不一样
        private ArrayList<List<Map>> context = new ArrayList<>();       // SQL对应的查询出来的数据集，
        private int realPagingSize;                                     // 真正需要分页的数据的大小，没有设置set方法，根据pageSizes判断生成
        // 当前Sheet页的序号
        private Integer orderNumber;                                    // 当前Sheet页的序号
        // 维度数据
        private Map<String, Object> dimensions = new LinkedHashMap<>(); // 当前Sheet页面的维度数据
        private List<Map<String, String>> sqlParamList;                 // 当前Sheet页SQL的额外参数

        public SheetInfo() {
        }

        public ArrayList<String> getDataKeys() {
            return dataKeys;
        }

        public void setDataKeys(ArrayList<String> dataKeys) {
            this.dataKeys = dataKeys;
        }

        public ArrayList<Integer> getPageSizes() {
            return pageSizes;
        }

        public void setPageSizes(ArrayList<Integer> pageSizes) {
            this.pageSizes = pageSizes;
        }

        public ArrayList<String> getSqls() {
            return sqls;
        }

        public void setSqls(ArrayList<String> sqls) {
            this.sqls = sqls;
        }

        public ArrayList<List<Map>> getContext() {
            return context;
        }

        public void setContext(ArrayList<List<Map>> context) {
            this.context = context;
        }

        public int getRealPagingSize() {
            int size = 0;
            for (int i = 0; i < pageSizes.size(); i++) {
                if(pageSizes.get(i) == null || pageSizes.get(i) != 0) {
                    size++;
                }
            }
            return size;
        }

        public Integer getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(Integer orderNumber) {
            this.orderNumber = orderNumber;
        }

        public Map<String, Object> getDimensions() {
            return dimensions;
        }

        public void setDimensions(Map<String, Object> dimensions) {
            this.dimensions = dimensions;
        }

        public List<Map<String, String>> getSqlParamList() {
            return sqlParamList;
        }

        public void setSqlParamList(List<Map<String, String>> sqlParamList) {
            this.sqlParamList = sqlParamList;
        }

        @Override
        public String toString() {
            return "SheetInfo{" +
                    ", dataKeys=" + dataKeys +
                    ", pageSizes=" + pageSizes +
                    ", sqls=" + sqls +
                    ", context=" + context +
                    '}';
        }
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTplPath() {
        return tplPath;
    }

    public void setTplPath(String tplPath) {
        this.tplPath = tplPath;
    }

    public String getOutPath() {
        return outPath;
    }

    public void setOutPath(String outPath) {
        this.outPath = outPath;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Map<String, SheetInfo> getSheets() {
        return sheets;
    }

    public void setSheets(Map<String, SheetInfo> sheets) {
        this.sheets = sheets;
    }

    @Override
    public String toString() {
        return "ExcelExportOptions{" +
                "name='" + name + '\'' +
                ", tplPath='" + tplPath + '\'' +
                ", outPath='" + outPath + '\'' +
                ", sheets=" + sheets +
                '}';
    }
}

