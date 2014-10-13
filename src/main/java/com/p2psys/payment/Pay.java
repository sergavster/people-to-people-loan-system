package com.p2psys.payment;

import org.apache.log4j.Logger;

public class Pay {

	private static final Logger logger=Logger.getLogger(Pay.class);
	
	private TranGood good; //封装的商品信息
	
	private String frontMerUrl; //前台回调地址,暂时不用
	private String backgroundMerUrl; //后台回调地址,暂时不用
	
	public TranGood getGood() {
		return good;
	}
	public void setGood(TranGood good) {
		this.good = good;
	}
	public String getFrontMerUrl() {
		return frontMerUrl;
	}
	public void setFrontMerUrl(String frontMerUrl) {
		this.frontMerUrl = frontMerUrl;
	}
	public String getBackgroundMerUrl() {
		return backgroundMerUrl;
	}
	public void setBackgroundMerUrl(String backgroundMerUrl) {
		this.backgroundMerUrl = backgroundMerUrl;
	}
	
	
	
}
