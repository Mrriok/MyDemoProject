package com.zony.common.filenet.util;

import com.zony.common.filenet.vo.Entry;

import java.util.*;


/**
 * @Function TODO
 * @Version 1.0.0.0
 * @Date 2016-4-7
 * @Author Michael
 * @Copyright (C) 2014, ShangHai ZonySoft Co .,Ltd  All Rights Reserved.
 */
public class CollectionUtil {

	
	public static List<Entry> mapToEntryList(Map<String, Object> map){
		List<Entry> list = new ArrayList<Entry>();
		if(map!=null){
			Set<String> set = map.keySet();
			Iterator<String> it = set.iterator();
			while(it.hasNext()){
				String key = it.next();
				String value = map.get(key)==null?"":map.get(key).toString();
				Entry entry = new Entry();
				entry.setKey(key);
				entry.setValue(value);
				list.add(entry);
			}
		}
		return list;
	}
	
	public static Map<String,String> entryListToMap(List<Entry> list){
		Map<String,String> map = new HashMap<String, String>();
		if(list!=null){
			for(Entry entry:list){
				String key = entry.getKey();
				String value = entry.getValue();
				map.put(key, value);
			}
		}
		return map;
	}

}
