package cnbi.test;

import cnbi.simple_export.CNBISimpleExporter;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.cnbi.util.writeExcel.jxls2.util.JsonUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by FangQiang on 2018/9/23/023
 */
public class SimpleExportTest {

    static Logger logger = LoggerFactory.getLogger( SimpleExportTest.class );

    public static void simpleExport(List<Map> data, String titles, String headers, OutputStream os) throws IOException, InvalidFormatException {
        CNBISimpleExporter exporter = new CNBISimpleExporter();
        exporter.gridExport( Arrays.asList( titles.split( "," ) ), data, headers, os );
    }

    @Test
    public void demo1() throws IOException, InvalidFormatException {
        InputStream is = this.getClass().getResourceAsStream( "data.json" );
        BufferedReader br = new BufferedReader( new InputStreamReader( is, "UTF-8" ) );
        StringBuilder sb = new StringBuilder();
        char[] chars = new char[2048];
        int len = 0;
        while((len = br.read(chars)) != -1 ) {
            sb.append( new String( chars, 0 ,len ) );
        }
        br.close();
        System.out.println(sb.toString());
        List<Map> list = JsonUtils.getInstance().readValue( sb.toString(), new TypeReference<List<Map>>() {} );
        simpleExport( list, "SCODE,itemname,M1,M2,M3,M4,M5,M6,M7,M8,M9,M10,M11,M12,MM",
                "SCODE,itemname,M1,M2,M3,M4,M5,M6,M7,M8,M9,M10,M11,M12,MM", new FileOutputStream( "666.xls" ));

    }


}
