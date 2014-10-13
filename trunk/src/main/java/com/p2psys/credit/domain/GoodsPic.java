package com.p2psys.credit.domain;

import java.io.Serializable;

/**
 * 积分兑换商品存放图片实体类
 
 * 2013-12-19
 * v1.6.7.2 RDPROJECT-569
 */
public class GoodsPic implements Serializable{
	
	private long id;
	private long goods_id; //商品ID
	private String pic_name;  //图片名称
	private String pic_url;  //图片路径
	private Integer add_time;  //添加时间 
	private Integer ordering;  //显示图片顺序
	private String compress_pic_url;  //压缩后图片
	public String getCompress_pic_url() {
		return compress_pic_url;
	}
	public void setCompress_pic_url(String compress_pic_url) {
		this.compress_pic_url = compress_pic_url;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public long getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(long goods_id) {
		this.goods_id = goods_id;
	}
	public String getPic_name() {
		return pic_name;
	}
	public void setPic_name(String pic_name) {
		this.pic_name = pic_name;
	}
	public String getPic_url() {
		return pic_url;
	}
	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}
	public Integer getAdd_time() {
		return add_time;
	}
	public void setAdd_time(Integer add_time) {
		this.add_time = add_time;
	}
	public Integer getOrdering() {
		return ordering;
	}
	public void setOrdering(Integer ordering) {
		this.ordering = ordering;
	}
}
