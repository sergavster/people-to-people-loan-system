package com.p2psys.credit.model;

import java.io.Serializable;

import com.p2psys.credit.domain.Goods;

/**
 
 *  2013-12-19
 */
public class CreditGoodsModel extends Goods implements Serializable{

	//图片
	private Integer goods_id; //商品ID
	private String pic_name; //图片名称
	private String pic_url; //图片路径
	private String compress_pic_url; //压缩后路径
	private Integer ordering; //排放顺序
	
	//商品分类
	private Integer parent_id; //是否父类 0:父类
	private Integer is_virtual;
	private Integer add_time;
	private String categoryName;//分类名称
	
	/**
	 * 对应积分明细表详细
	 * @return
	 */
	private Integer user_id;
	private Integer type_id;//积分类型
	private String type_name; //类型名称
	private Integer op;  //1:增加  2：减少
	private Integer value; //积分
	private String remark;  //备注
	private String addtime;
	private String op_user;
	private String addip;
	private String valid_value; //有效积分
	public String getValid_value() {
		return valid_value;
	}
	public void setValid_value(String valid_value) {
		this.valid_value = valid_value;
	}
	public Integer getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(Integer goods_id) {
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
	public Integer getOrdering() {
		return ordering;
	}
	public void setOrdering(Integer ordering) {
		this.ordering = ordering;
	}
	public Integer getParent_id() {
		return parent_id;
	}
	public void setParent_id(Integer parent_id) {
		this.parent_id = parent_id;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public Integer getType_id() {
		return type_id;
	}
	public void setType_id(Integer type_id) {
		this.type_id = type_id;
	}
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
	public Integer getOp() {
		return op;
	}
	public void setOp(Integer op) {
		this.op = op;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public String getOp_user() {
		return op_user;
	}
	public void setOp_user(String op_user) {
		this.op_user = op_user;
	}
	public String getAddip() {
		return addip;
	}
	public void setAddip(String addip) {
		this.addip = addip;
	}
	public String getCompress_pic_url() {
		return compress_pic_url;
	}
	public void setCompress_pic_url(String compress_pic_url) {
		this.compress_pic_url = compress_pic_url;
	}
	public Integer getIs_virtual() {
		return is_virtual;
	}
	public void setIs_virtual(Integer is_virtual) {
		this.is_virtual = is_virtual;
	}
	public Integer getAdd_time() {
		return add_time;
	}
	public void setAdd_time(Integer add_time) {
		this.add_time = add_time;
	}
	
	
}
