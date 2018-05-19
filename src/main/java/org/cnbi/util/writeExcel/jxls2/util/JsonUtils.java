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
    
}
