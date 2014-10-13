package com.p2psys.credit.domain;

import java.io.Serializable;


/**
 * 商品类别 dw_goods_category
 
 * 2013-12-20 
 * v1.6.7.2 RDPROJECT-569
 */
public class GoodsCategory implements Serializable {

	private Integer id;
	private String name;  //商品名称
	private Integer parent_id; //父类
	private Integer add_time; //添加时间
	private Integer is_virtual; //0：实物  3:父类型 1：vip 2：现金
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getParent_id() {
		return parent_id;
	}
	public void setParent_id(Integer parent_id) {
		this.parent_id = parent_id;
	}
	public Integer getAdd_time() {
		return add_time;
	}
	public void setAdd_time(Integer add_time) {
		this.add_time = add_time;
	}
	public Integer getIs_virtual() {
		return is_virtual;
	}
	public void setIs_virtual(Integer is_virtual) {
		this.is_virtual = is_virtual;
	}
	
}
