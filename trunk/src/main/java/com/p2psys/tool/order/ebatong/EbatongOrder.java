package com.p2psys.tool.order.ebatong;

import java.io.Serializable;
/**
 * 汇潮支付订单查询          RDPROJECT-28
 
 *
 */
public class EbatongOrder implements Serializable{
	private static final long serialVersionUID = -5290637298310888840L;
	private String status;    //反馈结果的状态     SUCCESS :表示查询成功，指定交易已找到。 NOT_FOUND :表示指定交易未找到。ERROR:表示请求参数不合法或易八通验签异常，查询失败。
	private String charset;  //反馈结果的编码格式  UTF-8
	private EbatongResult ebatongResult=new EbatongResult();
	private String sign;//签名数据，商户网站收到查询结果后要验签
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public EbatongResult getEbatongResult() {
		return ebatongResult;
	}
	public void setEbatongResult(EbatongResult ebatongResult) {
		this.ebatongResult = ebatongResult;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
}
