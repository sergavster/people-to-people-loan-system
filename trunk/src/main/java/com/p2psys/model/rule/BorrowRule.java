package com.p2psys.model.rule;
//RDPROJECT-282 fxx 2013-10-18 start
//新增类
//RDPROJECT-282 fxx 2013-10-18 end
/**
 * 
 
 * 
 */
public class BorrowRule {
	private int type;
	private int[] status;
	private int isDay;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int[] getStatus() {
		return status;
	}

	public void setStatus(int[] status) {
		this.status = status;
	}

	public int getIsDay() {
		return isDay;
	}

	public void setIsDay(int isDay) {
		this.isDay = isDay;
	}

}
