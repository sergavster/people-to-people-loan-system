package com.p2psys.model;

import java.util.Map;

import com.p2psys.util.StringUtils;

/**
 * 
 * 新增类 RDPROJECT-26 sj 2013-12-24 
 * 
 * 
 * */
public class LinkageTypeSearchParam extends SearchParam {
	
	private String name;
	private int type_id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType_id() {
		return type_id;
	}

	public void setType_id(int type_id) {
		this.type_id = type_id;
	}

	public LinkageTypeSearchParam(){
		super();
	}
	
	/**
	 * 将所有的参数信息封装成Map
	 * @return
	 */
	@Override
	public Map toMap(){
		if(!StringUtils.isBlank(name)){
			map.put("name", name);
		}
		if(type_id != -1){
			map.put("type_id", type_id);
		}
		return map;
	}

	@Override
	public String getSearchParamSql() {

		StringBuffer sb = new StringBuffer();
		
		if(!StringUtils.isBlank(name)){
			sb.append(" and name like '%").append(name).append("%'");
		}
		if(type_id != -1){
			sb.append(" and type_id = ").append(type_id);
		}
		return sb.toString();
		
	}
	
}
