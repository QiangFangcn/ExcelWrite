package org.cnbi.util.writeExcel.poi.base;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;

public class POIUtiles {
	
	
	public static String color2Hex(Color color){
            return color2Hex(color.getRed(), color.getGreen(), color.getBlue());
	}
	public static String color2Hex(int red, int green, int blue){
		if((0<=red&&red<=255)&&(0<=green&&green<=255)&&(0<=blue&&blue<=255)){  
            String str1 = new String("#");  
            str1 = str1 +Integer.toHexString(red)+Integer.toHexString(green)+Integer.toHexString(blue);  
            return str1;
		}
		return null;
	}
	public static Color hex2Color(String colorHex){
		Color color = null;
		if(colorHex.startsWith("#") && colorHex.length() == 7){
			int r = Integer.parseInt(colorHex.substring(1,3),16);
			int g = Integer.parseInt(colorHex.substring(3,5),16);
			int b = Integer.parseInt(colorHex.substring(5,7),16);
			color = new Color(r, g, b);
		}
		return color;
	}
	
	/**
	 * 获取颜色对象
	 * @param ocolor
	 * @return
	 */
	public static Color getColor(Object ocolor){
		Color color = Color.BLACK;
		try{
			if(ocolor instanceof Color){
				color = (Color) ocolor;
			}else if(ocolor instanceof String){
				Color newcolor = hex2Color((String) ocolor);
				if(newcolor != null){
					color = newcolor;
				}
	//		}else if(){
			}
		}catch(IndexOutOfBoundsException ex){
			
		}catch(NumberFormatException ex){
			
		}
		return color;
	}
	
	public static XSSFColor getXSSFColor(SXSSFWorkbook wb, Object color){
		XSSFColor xssfColor = new XSSFColor(getColor(color));
		return xssfColor;
	}
	
	public static XSSFFont getXSSFFont(SXSSFWorkbook wb, StyleBean styleBean){
		String color = styleBean.getFontColor();
		XSSFColor xssfColor = getXSSFColor(wb, color);
		return getXSSFFont(wb, styleBean.getFontSize(), styleBean.getFontName(), styleBean.getBoldWeight(), styleBean.getIsItalic(), styleBean.getUnderline(), styleBean.getIsStrikeout(), styleBean.getTypeOffset(), xssfColor);
	}
	
	/**
	 * 获取字体
	 * @param wb
	 * @param fontSize 字体大小
	 * @param fontName 字体名称
	 * @param boldWeight 粗细
	 * @param isItalic 斜体
	 * @param underLine 下划线
	 * @param isStrikeout 删除线
	 * @param typeOffset 空/上标/下标 (none,super,sub)
	 * @param color 字体颜色 
	 * @return 
	 */
	public static XSSFFont getXSSFFont(SXSSFWorkbook wb, int fontSize, String fontName, int boldWeight, boolean isItalic, byte underLine, boolean isStrikeout, int typeOffset, XSSFColor color ){
		XSSFFont font = findFont(wb, (short)boldWeight, color, (short)fontSize, fontName, isItalic, isStrikeout, (short)typeOffset, underLine);
		if(font == null){
			font = creatXSSFFont(wb, fontSize, fontName, boldWeight, isItalic, underLine, isStrikeout, typeOffset, color);
		}
		return font;
	}
	
	/**
	 * 查询字体
	 * @param wb
	 * @param boldWeight
	 * @param color
	 * @param fontSize
	 * @param fontName
	 * @param isItalic
	 * @param isStrikeout
	 * @param typeOffset
	 * @param underLine
	 * @return 
	 */
	private static XSSFFont findFont(SXSSFWorkbook wb, short boldWeight, XSSFColor color, short fontSize, String fontName,
			boolean isItalic, boolean isStrikeout, short typeOffset, byte underLine) {
		
		List<XSSFFont> fonts = wb.getXSSFWorkbook().getStylesSource().getFonts();
		for (XSSFFont font : fonts) {
			if (	(font.getBoldweight() == boldWeight)
					&& font.getXSSFColor().equals(color)
					&& font.getFontHeight() == fontSize
					&& font.getFontName().equals(fontName)
					&& font.getItalic() == isItalic
					&& font.getStrikeout() == isStrikeout
					&& font.getTypeOffset() == typeOffset
					&& font.getUnderline() == underLine)
			{
				return font;
			}
		}
		return null;
	}

	/**
	 * 创建字体
	 * @param wb
	 * @param fontSize 字体大小
	 * @param fontName 字体名称
	 * @param boldWeight 粗细
	 * @param isItalic 斜体
	 * @param underLine 下划线
	 * @param isStrikeout 删除线
	 * @param typeOffset 空/上标/下标 (none,super,sub)
	 * @param color 字体颜色
	 * @return
	 */
	public static XSSFFont creatXSSFFont(SXSSFWorkbook wb,  int fontSize, String fontName, int boldWeight, boolean isItalic, byte underLine, boolean isStrikeout, int typeOffset, XSSFColor color){
		XSSFFont font = (XSSFFont) wb.createFont();
		font.setTypeOffset((short)typeOffset);
		font.setStrikeout(isStrikeout);
		font.setFontHeightInPoints((short)fontSize);
		font.setFontName(fontName);
		font.setBoldweight((short)boldWeight);
		font.setItalic(isItalic);
		font.setUnderline(underLine);
		font.setColor(color);
		return font;
	}
	
	
	/**
	 * 获取样式
	 * @param cacheStyleMap
	 * @param wb
	 * @param styleBean
	 * @return
	 */
	public static CellStyle getStyle(HashMap<StyleBean, CellStyle> cacheStyleMap, SXSSFWorkbook wb, StyleBean styleBean){
		
		CellStyle style = cacheStyleMap.get(styleBean);
		if(style == null){
			
			XSSFColor fgc = getXSSFColor(wb, styleBean.getFillForegroundColor());
			int fgcidx = fgc.getIndexed();
			
			XSSFColor bgc = getXSSFColor(wb, styleBean.getFillBackgroundColor());
			int bgcidx = bgc.getIndexed();
			
			XSSFColor topc = getXSSFColor(wb, styleBean.getTopBorderColor());
			
			XSSFColor bottomc = getXSSFColor(wb, styleBean.getBottomBorderColor());
			
			XSSFColor leftc = getXSSFColor(wb, styleBean.getLeftBorderColor());
			
			XSSFColor rightc = getXSSFColor(wb, styleBean.getRightBorderColor());
			
			Font font = getXSSFFont(wb, styleBean);
			int fontidx = font.getIndex();
			
			style = creatStyle(wb,  styleBean.getAlignment(), styleBean.getVerticalAlignment(), 
					bgcidx, fgcidx, styleBean.getFillPattern(), 
					styleBean.getBorderTop(), styleBean.getBorderBottom(), styleBean.getBorderLeft(), styleBean.getBorderRight(), 
					topc, bottomc, leftc, rightc, fontidx, styleBean.getDataFormat());
			cacheStyleMap.put(styleBean, style);
		}
		return style;
	}
	
	
	/**
	 * 创建样式
	 * @param wb
	 * @param align
	 * @param vertical
	 * @param bgColoridx
	 * @param fgColoridx
	 * @param fp
	 * @param borderTop
	 * @param borderBottom
	 * @param borderLeft
	 * @param borderRight
	 * @param topBorderColoridx
	 * @param bottomBorderColoridx
	 * @param leftBorderColoridx
	 * @param rightBorderColoridx
	 * @param fontidx
	 * @param formatText
	 * @return
	 */
	public static CellStyle  creatStyle(SXSSFWorkbook wb, int align, int vertical, 
			int bgColoridx, int fgColoridx, int fp, 
		int borderTop, int borderBottom, int borderLeft, int borderRight,
		XSSFColor topBorderColor, XSSFColor bottomBorderColor, XSSFColor leftBorderColor, XSSFColor rightBorderColor,
		int fontidx,String formatText){

		XSSFCellStyle cellstyle = (XSSFCellStyle) wb.createCellStyle();
		
		// 填充
		cellstyle.setFillPattern((short) fp);
		cellstyle.setFillForegroundColor((short)bgColoridx);
		//背景色
		cellstyle.setFillBackgroundColor((short)bgColoridx);
		
		// 设置边框
		cellstyle.setBorderTop((short)borderTop);
		cellstyle.setTopBorderColor(topBorderColor);
		
		cellstyle.setBorderBottom((short)borderBottom);
		cellstyle.setBottomBorderColor(bottomBorderColor);

		cellstyle.setBorderLeft((short)borderLeft);
		cellstyle.setLeftBorderColor(leftBorderColor);
		
		cellstyle.setBorderRight((short)borderRight);
		cellstyle.setRightBorderColor(rightBorderColor);
		
		// 垂直对齐
		cellstyle.setVerticalAlignment((short)vertical);
		// 水平对齐
		cellstyle.setAlignment((short)align);
		
		// 设置字体
		Font font = wb.getFontAt((short) fontidx);
		cellstyle.setFont(font);

		//单元格格式
		if(formatText != null){
			DataFormat dataFormat = wb.createDataFormat();
			cellstyle.setDataFormat(dataFormat.getFormat(formatText));
		}
		return cellstyle;
	}
	
	
}
