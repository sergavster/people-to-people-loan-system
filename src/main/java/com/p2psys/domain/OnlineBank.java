package com.p2psys.domain;

public class OnlineBank {
    private long id;
    private String bank_name;
    private String bank_logo;
    private String bank_value;
    private String payment_interface_id;
    private int is_enable;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getBank_name() {
		return bank_name;
	}
	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}
	public String getBank_logo() {
		return bank_logo;
	}
	public void setBank_logo(String bank_logo) {
		this.bank_logo = bank_logo;
	}
	public String getBank_value() {
		return bank_value;
	}
	public void setBank_value(String bank_value) {
		this.bank_value = bank_value;
	}
	public String getPayment_interface_id() {
		return payment_interface_id;
	}
	public void setPayment_interface_id(String payment_interface_id) {
		this.payment_interface_id = payment_interface_id;
	}
	public int getIs_enable() {
		return is_enable;
	}
	public void setIs_enable(int is_enable) {
		this.is_enable = is_enable;
	}
	
}
