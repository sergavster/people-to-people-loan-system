package com.p2psys.web.action.admin;

import com.p2psys.exception.BorrowException;
import com.p2psys.service.ErrorService;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

public class ErrorHandleAction extends BaseAction {
	private double money;
	private String username;
	private String type;
	private String remark;
	
	private ErrorService errorService;

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public ErrorService getErrorService() {
		return errorService;
	}

	public void setErrorService(ErrorService errorService) {
		this.errorService = errorService;
	}

	public String errorHandle(){
		if(paramString("actionType").isEmpty()){
			return "success";
		}else{
			if(money<=0){
				throw new BorrowException("money");
			}
			if(StringUtils.isBlank(type)){
				throw new BorrowException("type");
			}
			errorService.accountLogErrorHandle(username, type, money, remark);
			message("操作成功");
			return ADMINMSG;
		}
	}
	
}
