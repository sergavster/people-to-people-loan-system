package com.p2psys.domain;

import java.io.Serializable;

/**
 * 实体类
 
 * @version 1.0    
 * @since 2013-12-02 
 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部
 */
public class CreditRank implements Serializable {
	
	private int point1;
	private int point2;
	
	private int rank;
	
	private String pic;

	public int getPoint1() {
		return point1;
	}

	public void setPoint1(int point1) {
		this.point1 = point1;
	}

	public int getPoint2() {
		return point2;
	}

	public void setPoint2(int point2) {
		this.point2 = point2;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}
	
}
