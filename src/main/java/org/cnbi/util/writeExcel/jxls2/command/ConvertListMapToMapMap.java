package org.cnbi.util.writeExcel.jxls2.command;

import java.util.List;
import java.util.Map;
import org.cnbi.util.writeExcel.jxls2.util.CommonUtils;
import org.jxls.area.Area;
import org.jxls.command.AbstractCommand;
import org.jxls.command.Command;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.common.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 把对应的数据装成key map 的形式，用对应的主键去取
 * 主键的列不要有空值，否则不要对应行的值(可以用select命令筛选非空的值再来进行数据的更换)
 * 当前你如果想扩展空值也可以在下面的程序进行修改
 * jx:dataHandler2(lastCell="" items="" id="" key="")
 * @author FangQiang
 */
public class ConvertListMapToMapMap extends AbstractCommand{
	
	private final Logger logger = LoggerFactory.getLogger(ConvertListMapToMapMap.class);

	private static final String COMMAND_NAME = "dataHandler2";
	private static final String DEFAULT_ITEMS_NAME = "datas";
	
	//必须设置
	private String id;
	private String key;
	private Area area;
	private String items = DEFAULT_ITEMS_NAME;

	public ConvertListMapToMapMap() {
		super();
	}

	public ConvertListMapToMapMap(String id, String key, String items) {
		super();
		this.id = id;
		this.key = key;
		this.items = items;
	}
	
	@Override
	public String getName() {
		return ConvertListMapToMapMap.COMMAND_NAME;
	}
	
    @Override
    public Command addArea(Area area) {
        if (super.getAreaList().size() >= 1) {
            throw new IllegalArgumentException("You can add only a single area to 'dataHanler2' command");
        }
        this.area = area;
        return super.addArea(area);
    }
	
	@Override
	public Size applyAt(CellRef cellRef, Context context) {
		items = this.getItems();
		if (id == null || "".equals( id )) {
			logger.error(cellRef+"-->"+"你未输入{}的key的值，请在jxls2标签中键入对应的key值", COMMAND_NAME);
			return new Size(1,1);
		}
		Object obj = context.getVar(items);
		if(!(obj instanceof List)) {
			logger.error(cellRef+"-->"+"要转换的items不是一个List，无法处理，请确认!!!");
			return new Size(1,1);
		}
		//里面是不是装的map没有确认，我默认是装了map 这是一个bug点 未处理
		List<Map> list = (List<Map>) obj;
		Map<String, Map> map = CommonUtils.castListToKeyMap(list, id);
		context.putVar(key, map);
		return area.applyAt(cellRef, context);
	}

	public String getItems() {
		return items;
	}
	public void setItems(String items) {
		this.items = items;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}	
	
	
}
