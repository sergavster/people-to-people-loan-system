package com.p2psys.service;

import java.util.List;

import com.p2psys.domain.AccountBank;
import com.p2psys.domain.OnlineBank;
import com.p2psys.domain.PaymentInterface;
import com.p2psys.model.PageDataList;
/**
 * 充值银行service类
 
 * 2013-10-25
 *
 */
public interface ManageBankService {
	/**
	 * 
	 */

	public PageDataList getList(int page);

	public AccountBank getDownLineBank(int id);

	/**
	 * 第三方接口添加文件
	 */
	public void addPayInterface(PaymentInterface paymentInterface);

	public List getPayInterfaceList(int init);

	public PaymentInterface updatePayInterface(PaymentInterface p);

	public OnlineBank addOnlineBank(OnlineBank onlineBank);

	public PageDataList getOnlineBankList(int page);

	/**
	 * 线上银行
	 */
	public OnlineBank updateOnlineBank(OnlineBank p);

	/**
	 * 线上银行信息显示
	 */
	public OnlineBank getOnlineBank(int id);

	/**
	 * 线上银行添加修改操作
	 */
	public void OnLineBank(OnlineBank bank, String ids);

	/**
	 * 查看第三方接口信息
	 */
	public PaymentInterface getPayInterface(int id);

	public void deletePaymentInterface(long id);
	public List getPaymentList(int status);
	public void addRechargeDownLineBank(AccountBank bank);
	public void updateRechargeDownLineBank(AccountBank bank);
	public PageDataList getPayInterfaceList(int page,int status);
	public void deleteRechargeDownBank(long id);
	//v1.6.7.2 RDPROJECT-539 wcw 2013-12-06 start
	public List getDownRechargeBankList() ;
	//v1.6.7.2 RDPROJECT-539 wcw 2013-12-06 end
}
