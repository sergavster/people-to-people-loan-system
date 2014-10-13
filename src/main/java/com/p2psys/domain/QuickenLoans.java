package com.p2psys.domain;

import java.io.Serializable;

/**
 * 快速贷款
 
 *
 */
public class QuickenLoans implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7020951864957549123L;
	//loansId
	private Integer loansId;
	// 姓名
	private String name;
	// 联系方式
	private String phone;
	// 地区
	private String area;
	// 备注
	private String remark;
	// 创建时间
	private String createTime;
	
	public Integer getLoansId() {
		return loansId;
	}
	public void setLoansId(Integer loansId) {
		this.loansId = loansId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
