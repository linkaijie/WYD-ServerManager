package com.zhwyd.server.common.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.util.StringUtils;

public class PageUtil {
	
	/**
	 * 分页size
	 * @param length
	 * @return
	 */
	public static List<String> getSelectValue(Integer length){
		List<String> list=new ArrayList<String>();
		for(int i=2;i<length;i++){
			list.add(i+"0");
		}		
		return list;
	}
	
	/**
	 * 把页面传递的卡号拆粉---中间（,）分割
	 * @return
	 */
	public static List<String> parseParamsToList(String params){
		List<String> list =new ArrayList<String>();
		String[] values=params.split(",");
		for(int i=0;i<values.length;i++){
			list.add(values[i]);
		}
		return list;
	}
	
	/**
	 * 拆分checkbox选中的id值
	 * @param ids
	 * @return
	 */
	public static List<Long> getCheckedId(String ids){
		String[] values=ids.split(",");
		List<Long> list=new ArrayList<Long>();
		for(String value:values){
			list.add(Long.valueOf(value).longValue());
		}
		return list;
	}
	

    /**
     * 转换时间
     * 
     * @param time
     * @return
     */
    public static Date getTime(String time) {
        if (StringUtils.hasText(time)) {
            return new Date(Long.valueOf(time));
        }
        return null;
    }
}
