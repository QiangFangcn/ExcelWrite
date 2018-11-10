package cnbi.simple_export;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jxls.area.Area;
import org.jxls.area.XlsArea;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.command.GridCommand;
import org.jxls.common.AreaRef;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.common.JxlsException;
import org.jxls.transform.poi.PoiTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;


public class CNBISimpleExporter {
    public static final String GRID_TEMPLATE_XLS = "CNBI_grid_template.xls";
    static Logger logger = LoggerFactory.getLogger( CNBISimpleExporter.class );

    private byte[] templateBytes;

    public CNBISimpleExporter() {
        InputStream is = CNBISimpleExporter.class.getResourceAsStream( GRID_TEMPLATE_XLS );
        try {
            registerGridTemplate( is );
        } catch (IOException e) {
            String message = "Failed to read default template file " + GRID_TEMPLATE_XLS;
            logger.error( message );
            throw new JxlsException( message, e );
        }
    }

    public void registerGridTemplate(InputStream inputStream) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        int count;
        while ((count = inputStream.read( data )) != -1) {
            os.write( data, 0, count );
        }
        templateBytes = os.toByteArray();
    }

    public void gridExport(Iterable headers, Iterable dataObjects, String objectProps, OutputStream outputStream) throws IOException, InvalidFormatException {
        InputStream is = new ByteArrayInputStream( templateBytes );
        // Transformer transformer = TransformerFactory.createTransformer(is, outputStream);
        PoiTransformer poiTransformer = PoiTransformer.createTransformer( is, outputStream );
        AreaBuilder areaBuilder = new XlsCommentAreaBuilder( poiTransformer );

        Area xlsArea = new XlsArea( "Sheet1!A1:A2", poiTransformer );
        GridCommand gridCommand = new GridCommand( "headers", "data",
                new XlsArea( "Sheet1!A1:A1", poiTransformer ), new XlsArea( "Sheet1!A2:A2", poiTransformer ) );
        // gridCommand.setFormatCells( "String:A2,String:B2,Double:C2,Float:C2,BigDecimal:C2" );
        // List<Area> xlsAreaList = areaBuilder.build();
        // Area xlsArea = xlsAreaList.get(0);
        xlsArea.addCommand( new AreaRef( "Sheet1!A1:A2" ), gridCommand );
        Context context = new Context();
        context.putVar( "headers", headers );
        context.putVar( "data", dataObjects );
        // GridCommand gridCommand = (GridCommand) xlsArea.getCommandDataList().get(0).getCommand();
        gridCommand.setProps( objectProps );
        xlsArea.applyAt( new CellRef( "Sheet1!A1" ), context );
        poiTransformer.getWorkbook().setSheetName( 0, "xxx表" );
        try {
            poiTransformer.write();
        } catch (IOException e) {
            logger.error( "Failed to write to output stream", e );
            throw new JxlsException( "Failed to write to output stream", e );
        }

    }
}
