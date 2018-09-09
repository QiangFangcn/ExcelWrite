package org.cnbi.util.writeExcel.jxls2.entity;

import java.util.List;

/**
 * Created by FangQiang on 2018/4/13
 * 分页信息类
 */
public class Page {
    private List list;          // 存放数据

    private int totalPage;      // 记住总页数

    private int totalRecord;    // 总记录数

    private int pageSize ;      // 每页的大小

    private int pageNum;        //代表用户想看的页码(当前页)

    private int startPage;      //开始页

    private int endPage;        //结束页

    public Page() {
    }

    public Page(List list, int totalPage, int totalRecord, int pageSize, int pageNum) {
        this.list = list;
        this.totalPage = totalPage;
        this.totalRecord = totalRecord;
        this.pageSize = pageSize;
        this.pageNum = pageNum;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }
}
