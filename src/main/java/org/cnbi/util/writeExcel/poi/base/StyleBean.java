package org.cnbi.util.writeExcel.poi.base;
/**
 * excel样式
 * @author 
 *
 */
public class StyleBean {
	/**
	 * 垂直对齐
	 */
	public static final String VERTICAL_ALIGNMENT = "verticalAlignment";
	private int verticalAlignment = 3;
   /**
    * 水平对齐
    */
	public static final String ALIGNMENT = "alignment";
	private int alignment =3;
	
	public static final String BORDER_TOP = "borderTop";
	private int borderTop;
	public static final String BORDER_BOTTOM = "borderBottom";
	private int borderBottom;
	public static final String BORDER_LEFT = "borderLeft";
	private int borderLeft;
	public static final String BORDER_RIGHT = "borderRight";
	private int borderRight;
	
	public static final String TOP_BORDER_COLOR = "topBorderColor";
	private String topBorderColor;
	public static final String BOTTOM_BORDER_COLOR = "bottomBorderColor";
	private String bottomBorderColor;
	public static final String LEFT_BORDER_COLOR = "leftBorderColor";
	private String leftBorderColor;
	public static final String RIGHT_BORDER_COLOR = "rightBorderColor";
	private String rightBorderColor;
   /**
    * 数据格式
    */
   public static final String DATA_FORMAT = "dataFormat";
   private String dataFormat;
	/**
	 * 填充图案类型
	 * FillPatternType
	 */
	public static final String FILL_PATTERN = "fillPattern";
	private int fillPattern = 0;
	/**
	 * 背景色
	 */
	public static final String FILL_BACKGROUND_COLOR = "fillBackgroundColor";
	private String fillBackgroundColor;
	/**
	 * 前景色
	 */
	public static final String FILL_FOREGROUND_COLOR = "fillForegroundColor";
	private String fillForegroundColor;
	
//	public static final String HIDDEN = "hidden";
//	public static final String INDENTION = "indention";
//	public static final String ROTATION = "rotation";
//	public static final String WRAP_TEXT = "wrapText";
//	public static final String LOCKED = "locked";
	
//	public static final String FONT = "font";
//	private Integer font;
	
	/**
	 * 字体颜色
	 */
	private String fontColor;
	/**
	 * 字体名称
	 */
	private String fontName;
	/**
	 * 字体大小
	 */
	private Integer fontSize;
	/**
	 * 字体粗细
	 */
	private Integer boldWeight;
	/**
	 * 是否斜体
	 */
	private Boolean isItalic;
	/**
	 * 下划线
	 */
	private Byte underline;
	/**
	 * 是否删除
	 */
	private Boolean isStrikeout;
	/**
	 * 字体类型
	 * 空/上标/下标 (none,super,sub)
	 */
	private Integer typeOffset;
	
	
	public StyleBean(){}
   
	public StyleBean(int verticalAlignment, int alignment, String dataFormat, String fillBackgroundColor, String fontColor,
		String fontName, Integer fontSize, Integer boldWeight, Boolean isItalic, Byte underline) {
		super();
		this.verticalAlignment = verticalAlignment;
		this.alignment = alignment;
		this.dataFormat = dataFormat;
		this.fillBackgroundColor = fillBackgroundColor;
		this.fontColor = fontColor;
		this.fontName = fontName;
		this.fontSize = fontSize;
		this.boldWeight = boldWeight;
		this.isItalic = isItalic;
		this.underline = underline;
		this.typeOffset=0;
		this.isStrikeout=false;
	}
	
	public StyleBean(int verticalAlignment, int alignment, int borderTop, int borderBottom, int borderLeft,
			int borderRight, String topBorderColor, String bottomBorderColor, String leftBorderColor,
			String rightBorderColor, String dataFormat, int fillPattern, String fillBackgroundColor,
			String fillForegroundColor, String fontColor, String fontName, Integer fontSize, Integer boldWeight,
			Boolean isItalic, Byte underline, Boolean isStrikeout, Integer typeOffset) {
		super();
		this.verticalAlignment = verticalAlignment;
		this.alignment = alignment;
		this.borderTop = borderTop;
		this.borderBottom = borderBottom;
		this.borderLeft = borderLeft;
		this.borderRight = borderRight;
		this.topBorderColor = topBorderColor;
		this.bottomBorderColor = bottomBorderColor;
		this.leftBorderColor = leftBorderColor;
		this.rightBorderColor = rightBorderColor;
		this.dataFormat = dataFormat;
		this.fillPattern = fillPattern;
		this.fillBackgroundColor = fillBackgroundColor;
		this.fillForegroundColor = fillForegroundColor;
		this.fontColor = fontColor;
		this.fontName = fontName;
		this.fontSize = fontSize;
		this.boldWeight = boldWeight;
		this.isItalic = isItalic;
		this.underline = underline;
		this.isStrikeout = isStrikeout;
		this.typeOffset = typeOffset;
	}

	public String getFillBackgroundColor() {
 		return fillBackgroundColor;
 	}
 	public void setFillBackgroundColor(String bgColor) {
 		this.fillBackgroundColor = bgColor;
 	}
 	public String getFillForegroundColor() {
		return fillForegroundColor;
	}
	public void setFillForegroundColor(String fColor) {
		this.fillForegroundColor = fColor;
	}
	public int getFillPattern() {
		return fillPattern;
	}
	public void setFillPattern(int fillPattern) {
		this.fillPattern = fillPattern;
	}
	public int getVerticalAlignment() {
 		return verticalAlignment;
 	}
 	public void setVerticalAlignment(int verticalAlignment) {
 		this.verticalAlignment = verticalAlignment;
 	}
 	public int getAlignment() {
		return alignment;
	}
	public void setAlignment(int alignment) {
		this.alignment = alignment;
	}
	public int getBorderTop() {
 		return borderTop;
 	}
 	public void setBorderTop(int borderTop) {
 		this.borderTop = borderTop;
 	}
 	public int getBorderBottom() {
 		return borderBottom;
 	}
 	public void setBorderBottom(int borderBottom) {
 		this.borderBottom = borderBottom;
 	}
 	public int getBorderLeft() {
 		return borderLeft;
 	}
 	public void setBorderLeft(int borderLeft) {
 		this.borderLeft = borderLeft;
 	}
 	public int getBorderRight() {
 		return borderRight;
 	}
 	public void setBorderRight(int borderRight) {
 		this.borderRight = borderRight;
 	}
 	public String getTopBorderColor() {
 		return topBorderColor;
 	}
 	public void setTopBorderColor(String topBorderColor) {
 		this.topBorderColor = topBorderColor;
 	}
 	public String getBottomBorderColor() {
 		return bottomBorderColor;
 	}
 	public void setBottomBorderColor(String bottomBorderColor) {
 		this.bottomBorderColor = bottomBorderColor;
 	}
 	public String getLeftBorderColor() {
 		return leftBorderColor;
 	}
 	public void setLeftBorderColor(String leftBorderColor) {
 		this.leftBorderColor = leftBorderColor;
 	}
 	public String getRightBorderColor() {
 		return rightBorderColor;
 	}
 	public void setRightBorderColor(String rightBorderColor) {
 		this.rightBorderColor = rightBorderColor;
 	}
 	
	public String getDataFormat() {
		return dataFormat;
	}
	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}
	public String getFontColor() {
		return fontColor;
	}
	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}
	public String getFontName() {
		return fontName;
	}
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
	public Integer getFontSize() {
		return fontSize;
	}
	public void setFontSize(Integer fontSize) {
		this.fontSize = fontSize;
	}
	public Integer getBoldWeight() {
		return boldWeight;
	}
	public void setBoldWeight(Integer boldWeight) {
		this.boldWeight = boldWeight;
	}
	public Boolean getIsItalic() {
		return isItalic;
	}
	public void setIsItalic(Boolean isItalic) {
		this.isItalic = isItalic;
	}
	public Byte getUnderline() {
		return underline;
	}
	public void setUnderline(Byte underline) {
		this.underline = underline;
	}
	
	public Boolean getIsStrikeout() {
		return isStrikeout;
	}
	public void setIsStrikeout(Boolean isStrikeout) {
		this.isStrikeout = isStrikeout;
	}
	public Integer getTypeOffset() {
		return typeOffset;
	}
	public void setTypeOffset(Integer typeOffset) {
		this.typeOffset = typeOffset;
	}
	public boolean hasFont(){
		if(fontColor != null || fontName != null || fontSize != null || boldWeight != null || isItalic != null || underline != null || isStrikeout != null || typeOffset != null){
			return true;
		}
		return false;
	}
	@Override
	public int hashCode() {
		final int prime = 11;
		int result = 1;
		result = prime * result + borderBottom;
		result = prime * result + ((bottomBorderColor == null) ? 0 : bottomBorderColor.hashCode());
		result = prime * result + borderLeft;
		result = prime * result + ((leftBorderColor == null) ? 0 : leftBorderColor.hashCode());
		result = prime * result + borderRight;
		result = prime * result + ((rightBorderColor == null) ? 0 : rightBorderColor.hashCode());
		result = prime * result + borderTop;
		result = prime * result + ((topBorderColor == null) ? 0 : topBorderColor.hashCode());
		
		result = prime * result + fillPattern;
		result = prime * result + ((fillBackgroundColor == null) ? 0 : fillBackgroundColor.hashCode());
		result = prime * result + ((fillForegroundColor == null) ? 0 : fillForegroundColor.hashCode());

		result = prime * result + alignment;
		result = prime * result + verticalAlignment;
		
		result = prime * result + ((dataFormat == null) ? 0 : dataFormat.hashCode());
		result = prime * result + ((fontColor == null) ? 0 : fontColor.hashCode());
		result = prime * result + ((fontName == null) ? 0 : fontName.hashCode());
		result = prime * result + ((fontSize == null) ? 0 : fontSize.hashCode());
		result = prime * result + ((boldWeight == null) ? 0 : boldWeight.hashCode());
		result = prime * result + ((isItalic == null) ? 0 : isItalic.hashCode());
		result = prime * result + ((underline == null) ? 0 : underline.hashCode());
		result = prime * result + ((isStrikeout == null) ? 0 : isStrikeout.hashCode());
		result = prime * result + ((typeOffset == null) ? 0 : typeOffset.hashCode());
		
		
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null){
			return false;
		}
		if (getClass() != obj.getClass()){
			return false;
		}
		if (this == obj){
			return true;
		}
		StyleBean other = (StyleBean) obj;
		if (fillPattern != other.fillPattern){
			return false;
		}
		if (fillBackgroundColor == null) {
			if (other.fillBackgroundColor != null){
				return false;
			}
		} else if (!fillBackgroundColor.equals(other.fillBackgroundColor)){
			return false;
		}
		if (fillForegroundColor == null) {
			if (other.fillForegroundColor != null){
				return false;
			}
		} else if (!fillForegroundColor.equals(other.fillForegroundColor)){
			return false;
		}
		if (borderBottom != other.borderBottom){
			return false;
		}
		if (bottomBorderColor == null) {
			if (other.bottomBorderColor != null){
				return false;
			}
		} else if (!bottomBorderColor.equals(other.bottomBorderColor)){
			return false;
		}
		if (borderLeft != other.borderLeft){
			return false;
		}
		if (leftBorderColor == null) {
			if (other.leftBorderColor != null){
				return false;
			}
		} else if (!leftBorderColor.equals(other.leftBorderColor)){
			return false;
		}
		if (borderRight != other.borderRight){
			return false;
		}
		if (rightBorderColor == null) {
			if (other.rightBorderColor != null){
				return false;
			}
		} else if (!rightBorderColor.equals(other.rightBorderColor)){
			return false;
		}
		if (borderTop != other.borderTop){
			return false;
		}
		if (topBorderColor == null) {
			if (other.topBorderColor != null){
				return false;
			}
		} else if (!topBorderColor.equals(other.topBorderColor)){
			return false;
		}
		if (dataFormat == null) {
			if (other.dataFormat != null){
				return false;
			}
		} else if (!dataFormat.equals(other.dataFormat)){
			return false;
		}
		if (alignment != other.alignment){
			return false;
		}
		if (verticalAlignment != other.verticalAlignment){
			return false;
		}
		if(!hasFont() &&  !other.hasFont()){
			return true;
		}else{
			if (fontColor == null) {
				if (other.fontColor != null){
					return false;
				}
			} else if (!fontColor.equals(other.fontColor)){
				return false;
			}
			if (fontName == null) {
				if (other.fontName != null){
					return false;
				}
			} else if (!fontName.equals(other.fontName)){
				return false;
			}
			if (fontSize == null) {
				if (other.fontSize != null){
					return false;
				}
			} else if (!fontSize.equals(other.fontSize)){
				return false;
			}
			if (boldWeight == null) {
				if (other.boldWeight != null){
					return false;
				}
			} else if (!boldWeight.equals(other.boldWeight)){
				return false;
			}
			if (isItalic == null) {
				if (other.isItalic != null){
					return false;
				}
			} else if (!isItalic.equals(other.isItalic)){
				return false;
			}
			if (underline == null) {
				if (other.underline != null){
					return false;
				}
			} else if (!underline.equals(other.underline)){
				return false;
			}
			if (isStrikeout == null) {
				if (other.isStrikeout != null){
					return false;
				}
			} else if (!isStrikeout.equals(other.isStrikeout)){
				return false;
			}
			if (typeOffset == null) {
				if (other.typeOffset != null){
					return false;
				}
			} else if (!typeOffset.equals(other.typeOffset)){
				return false;
			}
		}
		
		return true;
	}
}
