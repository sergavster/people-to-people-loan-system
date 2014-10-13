package com.p2psys.util;

import java.util.HashMap;
import java.util.Map;
/**
 * 实体类
 
 * @version 1.0    
 * @since 2013-12-02 
 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部
 */
public class JsonMap{
    	Map map=new HashMap();

		public JsonMap() {
			super();
		}
    	
		public Map getMap() {
			return map;
		}

		public void add(String key,Object o){
			map.put(key, o);
		}
    }