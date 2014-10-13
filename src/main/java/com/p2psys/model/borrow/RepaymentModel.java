package com.p2psys.model.borrow;

import com.p2psys.domain.Repayment;

public class RepaymentModel extends Repayment{

	public RepaymentModel() {
		// TODO Auto-generated constructor stub
	}
    private String repay_time;
    private String repay_account;
    private String repay_yestime;
    private long flow_borrow_id;
	public long getFlow_borrow_id() {
		return flow_borrow_id;
	}
	public void setFlow_borrow_id(long flow_borrow_id) {
		this.flow_borrow_id = flow_borrow_id;
	}
	public String getRepay_time() {
		return repay_time;
	}
	public void setRepay_time(String repay_time) {
		this.repay_time = repay_time;
	}
	public String getRepay_account() {
		return repay_account;
	}
	public void setRepay_account(String repay_account) {
		this.repay_account = repay_account;
	}
	public String getRepay_yestime() {
		return repay_yestime;
	}
	public void setRepay_yestime(String repay_yestime) {
		this.repay_yestime = repay_yestime;
	}
    
}
