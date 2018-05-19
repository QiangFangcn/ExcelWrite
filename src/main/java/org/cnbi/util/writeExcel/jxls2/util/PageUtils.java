package org.cnbi.util.writeExcel.jxls2.util;

import org.cnbi.util.writeExcel.jxls2.bean.Page;

import java.util.List;

/**
 * Created by FangQiang on 2018/4/13
 * 产出分类信息的类，这里直接使用了List的分隔，因为哪些数据总归要全部查询出来
 */
public class PageUtils {
    private PageUtils() {}

    /**
     * 这个方法需要返回值，反会当前的查询页的数据！
     * @return
     */
    public static List getCurrentPageData(Page page) {
        List list = page.getList();
        int pageNum = page.getPageNum();
        int pageSize = page.getPageSize();
        int totalPage = page.getTotalPage();
        int totalRecord = page.getTotalRecord();
        // 如果没有数据就反回null
        if(pageNum > totalPage) {
            return null;
        }
        // 正常反回分页数据
        int from = (pageNum - 1) * pageSize;
        int to = pageNum * pageSize > totalRecord ? totalRecord : pageNum * pageSize;
        List subList = list.subList(from, to);
        //page.setPageNum(++pageNum);
        return subList;
    }
}
