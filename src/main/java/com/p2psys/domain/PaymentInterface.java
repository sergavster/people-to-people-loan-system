package com.p2psys.domain;
/**
 * 第三方支付接口实体类
 
 * 2013-10-25
 *
 */
public class PaymentInterface {

	private long id;
	private String name;
	private String merchant_id;
	private String key;
	private String recharge_fee;
	private String return_url;
	private String notice_url;
	private long is_enable_single;  //是否启用直连
	private String chartset;
	private String interface_Into_account;
	private String interface_value;
    private long is_enable_unsingle; //是否启用非直连
	//v1.6.7.1 RDPROJECT-417 wcw 2013-11-04 start
    private String signType; //加密方式
	//v1.6.7.1 RDPROJECT-417 wcw 2013-11-04 end
    private String pay_style;//支付方式
    private String seller_email;//卖家email
    private String transport;//访问模式
    private String order_description;//商品描述
    private long order; //排序
  //v1.6.7.1 RDPROJECT-417 wcw 2013-11-14 start
    private String gatewayUrl;
  //v1.6.7.1 RDPROJECT-417 wcw 2013-11-14 end
    //v1.6.7.2 RDPROJECT-417 wcw 2013-12-02 start
    private String orderInquireUrl;
  //v1.6.7.2 RDPROJECT-417 wcw 2013-12-02 end
  //v1.6.7.2 RDPROJECT-417 wcw 2013-12-11 start
    private String cert_position;
  //v1.6.7.2 RDPROJECT-417 wcw 2013-12-11 end
  //v1.6.7.2 RDPROJECT-417 wcw 2013-12-02 start
  public String getOrderInquireUrl() {
		return orderInquireUrl;
	}

	public void setOrderInquireUrl(String orderInquireUrl) {
		this.orderInquireUrl = orderInquireUrl;
	}

	//v1.6.7.2 RDPROJECT-417 wcw 2013-12-02 end
	//v1.6.7.1 RDPROJECT-417 wcw 2013-11-14 start
	public String getGatewayUrl() {
		return gatewayUrl;
	}

	public void setGatewayUrl(String gatewayUrl) {
		this.gatewayUrl = gatewayUrl;
	}

	//v1.6.7.1 RDPROJECT-417 wcw 2013-11-14 end
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

	public String getMerchant_id() {
		return merchant_id;
	}

	public void setMerchant_id(String merchant_id) {
		this.merchant_id = merchant_id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getRecharge_fee() {
		return recharge_fee;
	}

	public void setRecharge_fee(String recharge_fee) {
		this.recharge_fee = recharge_fee;
	}

	public String getReturn_url() {
		return return_url;
	}

	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}

	public String getNotice_url() {
		return notice_url;
	}

	public void setNotice_url(String notice_url) {
		this.notice_url = notice_url;
	}

	public long getIs_enable_single() {
		return is_enable_single;
	}

	public void setIs_enable_single(long is_enable_single) {
		this.is_enable_single = is_enable_single;
	}

	public long getIs_enable_unsingle() {
		return is_enable_unsingle;
	}

	public void setIs_enable_unsingle(long is_enable_unsingle) {
		this.is_enable_unsingle = is_enable_unsingle;
	}

	public String getChartset() {
		return chartset;
	}

	public void setChartset(String chartset) {
		this.chartset = chartset;
	}

	public String getInterface_Into_account() {
		return interface_Into_account;
	}

	public void setInterface_Into_account(String interface_Into_account) {
		this.interface_Into_account = interface_Into_account;
	}

	public String getInterface_value() {
		return interface_value;
	}

	public void setInterface_value(String interface_value) {
		this.interface_value = interface_value;
	}

	
	//v1.6.7.1 RDPROJECT-417 wcw 2013-11-04 start

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}
	//v1.6.7.1 RDPROJECT-417 wcw 2013-11-04 end

	public String getPay_style() {
		return pay_style;
	}

	public void setPay_style(String pay_style) {
		this.pay_style = pay_style;
	}

	public String getSeller_email() {
		return seller_email;
	}

	public void setSeller_email(String seller_email) {
		this.seller_email = seller_email;
	}

	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	public String getOrder_description() {
		return order_description;
	}

	public void setOrder_description(String order_description) {
		this.order_description = order_description;
	}

	public long getOrder() {
		return order;
	}

	public void setOrder(long order) {
		this.order = order;
	}
	  //v1.6.7.2    wcw 2013-12-11 start
    public String getCert_position() {
        return cert_position;
    }

    public void setCert_position(String cert_position) {
        this.cert_position = cert_position;
    }

    //v1.6.7.2 RDPROJECT-417 wcw 2013-12-11 end

}
