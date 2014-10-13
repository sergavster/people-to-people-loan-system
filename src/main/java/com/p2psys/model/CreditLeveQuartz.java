package com.p2psys.model;

import java.io.Serializable;

/**
 * 更新会员积分规则辅助类
 * @version 1.0
 * @since 2013-10-24
 */
public class CreditLeveQuartz implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	long operate_start;//预计执行时间
	
	int operate_time;//间隔多长时间执行一次
	
	int time_type;//日期类型：1代表日，2代表月

	public long getOperate_start() {
		return operate_start;
	}

	public void setOperate_start(long operate_start) {
		this.operate_start = operate_start;
	}

	public int getOperate_time() {
		return operate_time;
	}

	public void setOperate_time(int operate_time) {
		this.operate_time = operate_time;
	}

	public int getTime_type() {
		return time_type;
	}

	public void setTime_type(int time_type) {
		this.time_type = time_type;
	}
}
