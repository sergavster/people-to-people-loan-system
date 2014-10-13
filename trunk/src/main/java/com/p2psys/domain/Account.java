package com.p2psys.domain;

import java.io.Serializable;
/**
 
 * @date 2012-9-26 上午10:41:36
 * @version
 *
 *  (c)</b> 2012-rongdu-<br/>
 *   无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部
 */
public class Account implements Serializable {

	private static final long serialVersionUID = -2603972601624997167L;
	private	long	id;
	private	long	user_id;
	private	double	total;
	private	double	use_money;
	private	double	no_use_money;
	private	double	collection;
	private double hongbao;
	//山水聚宝累计投资奖励
	private double total_tender_award;
	
	
	public double getTotal_tender_award() {
		return total_tender_award;
	}
	public void setTotal_tender_award(double total_tender_award) {
		this.total_tender_award = total_tender_award;
	}
	public double getHongbao() {
		return hongbao;
	}
	public void setHongbao(double hongbao) {
		this.hongbao = hongbao;
	}
	@Override
	public String toString() {
		return "Account [id=" + id + ", user_id=" + user_id + ", total="
				+ total + ", use_money=" + use_money + ", no_use_money="
				+ no_use_money + ", collection=" + collection + "]";
	}
	public Account(long user_id, double total, double use_money,
			double no_use_money, double collection) {
		super();
		this.user_id = user_id;
		this.total = total;
		this.use_money = use_money;
		this.no_use_money = no_use_money;
		this.collection = collection;
	}
	public Account(long user_id) {
		super();
		this.user_id = user_id;
	}
	public Account() {
		super();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public double getUse_money() {
		return use_money;
	}
	public void setUse_money(double use_money) {
		this.use_money = use_money;
	}
	public double getNo_use_money() {
		return no_use_money;
	}
	public void setNo_use_money(double no_use_money) {
		this.no_use_money = no_use_money;
	}
	public double getCollection() {
		return collection;
	}
	public void setCollection(double collection) {
		this.collection = collection;
	}
	
}