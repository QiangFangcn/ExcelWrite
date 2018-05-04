package org.cnbi.util.writeExcel.jxls2.bean;

/**
 * Created by FangQiang on 2018/4/12
 * 维度信息
 */
public class Dimensions {
    private String year;
    private String unit;            // 单位
    private Integer lastYear;
    private String month;
    private String company;         // 公司编码
    private String companyName;     // 公司名称
    private String excels;          // excels是前端传过来的配置信息, 解析这个得到需要导出哪些Excel，哪些Sheet页

    public Dimensions() {
    }

    public Dimensions(String year, String month, String company, String excels) {
        this.year = year;
        this.month = month;
        this.company = company;
        this.excels = excels;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getLastYear() {
        return lastYear;
    }

    public void setLastYear(Integer lastYear) {
        this.lastYear = lastYear;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getExcels() {
        return excels;
    }

    public void setExcels(String excels) {
        this.excels = excels;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
