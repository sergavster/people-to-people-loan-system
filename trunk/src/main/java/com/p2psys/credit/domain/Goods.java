package com.p2psys.credit.domain;

import java.io.Serializable;

/**
 * 积商品兑换实体类
 
 * 2013-12-19
 * v1.6.7.2 RDPROJECT-569
 */
public class Goods implements Serializable{
	
	private long id;
	private String name;  //商品名
	private Integer credit_value;  //所需兑换积分
	private Integer store;   //商品数量 
	private Integer shelves_time;  // 上架时间
	private Integer add_time;   //添加时间 
	private Integer update_time;  //修改时间
	private Integer category_id;  //商品类别
	private String description;  //商品详情 
	private Double cost;		//商城售价
	private Double market_price;  //市场价 
	private String attribute;	//商品属性信息
	private Integer create_user_id;  //创建者ID 
	private String remarks;  //备注

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getCredit_value() {
		return credit_value;
	}
	public void setCredit_value(Integer credit_value) {
		this.credit_value = credit_value;
	}
	public Integer getStore() {
		return store;
	}
	public void setStore(Integer store) {
		this.store = store;
	}
	public Integer getShelves_time() {
		return shelves_time;
	}
	public void setShelves_time(Integer shelves_time) {
		this.shelves_time = shelves_time;
	}
	public Integer getAdd_time() {
		return add_time;
	}
	public void setAdd_time(Integer add_time) {
		this.add_time = add_time;
	}
	public Integer getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Integer update_time) {
		this.update_time = update_time;
	}
	public Integer getCategory_id() {
		return category_id;
	}
	public void setCategory_id(Integer category_id) {
		this.category_id = category_id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}
	public Double getMarket_price() {
		return market_price;
	}
	public void setMarket_price(Double market_price) {
		this.market_price = market_price;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public Integer getCreate_user_id() {
		return create_user_id;
	}
	public void setCreate_user_id(Integer create_user_id) {
		this.create_user_id = create_user_id;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
}
