package org.cnbi.util.writeExcel.jxls2.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.formula.functions.T;
import org.cnbi.tools.JsonUtil;

/**
 * Created by FangQiang on 2018/5/2
 */
public class JsonUtils {
    private static class JsonConverter {
        private static final ObjectMapper INSTANCE = new ObjectMapper();
    }
    private JsonUtils() {}
    public static final ObjectMapper getInstance() {
        return JsonConverter.INSTANCE;
    }

    /**
     * javaBean、列表数组转换为json字符串
     * @param obj
     * @return
     * @throws Exception
     */
    public static String obj2json(Object obj) throws Exception {
        return JsonConverter.INSTANCE.writeValueAsString(obj);
    }

    /**
     * javaBean、列表数组转换为json字符串,忽略空值
     * @param obj
     * @return
     * @throws Exception
     */
    public static String obj2jsonIgnoreNull(Object obj) throws Exception {
        JsonConverter.INSTANCE.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return JsonConverter.INSTANCE.writeValueAsString(obj);
    }
}
