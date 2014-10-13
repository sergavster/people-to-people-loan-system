package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.AccountBank;
import com.p2psys.domain.OnlineBank;
import com.p2psys.domain.Payment;
import com.p2psys.domain.PaymentInterface;


public interface BankDao extends BaseDao {
	/**
	 * 线下银行列表
	 * @param start
	 * @param end
	 * @return
	 */
	public List getDownRechargeBankList(int start, int end) ;
	/**
	 * 线下银行count
	 * @return
	 */
	public int getDownRechargeBankCount() ;
	/**
	 * 通过id获取线下银行信息
	 * @param id
	 * @return
	 */
	public AccountBank getDownRechargeBank(int id);
    /**
     * 添加第三方支付接口
     * @param paymentInterface
     * @return
     */
	public PaymentInterface addPayInterface(PaymentInterface paymentInterface);
	/**
	 * 第三方支付接口count
	 * @return
	 */
	public int getPayInterfaceCount();
	/**
	 * 第三方支付接口信息修改
	 * @param p
	 * @return
	 */
	public PaymentInterface updatePayInterface(PaymentInterface p);
	/**
	 * 线上银行添加
	 */
	public OnlineBank addOnlineBank(OnlineBank onlineBank);
	public List getOnlineBankList(String payment_interface_id);
	public int getOnlineBankCount();
	public OnlineBank updateOnlineBank(OnlineBank p);
	/**
	 * 查看线上银行卡信息
	 */
	public OnlineBank getOnlineBank(int id);
	/**
	 * 查看接口信息
	 */
	public PaymentInterface getPaymentInterface(int id) ;
	/**
	 * 通过id删除第三方接口信息
	 * @param id
	 */
	public void deletePaymentInterface(long id);
	/**
	 * 通过nid获取银行信息
	 * @param nid
	 * @return
	 */
	public Payment getPayment(String nid);
	/**
	 * 通过status获取list
	 * @param status  (payment表中第三方支付接口和线下银行的区分字段) status=0 代表第三方支付接口 status=1代表线下银行
	 * @return
	 */
	public List getPaymentList(int status);
	/**
	 * 查看启用非直连的第三方支付接口
	 * @param is_enable_unsingle
	 * @return
	 */
	public List getPayInterfaceUnsingleList(int is_enable_unsingle);
	/**
	 * 查看启用直连的第三方支付接口
	 * @param status
	 * @return
	 */
	public List getPaymentInterfaceSingleList(long status) ;
	/**
	 * 第三方支付接口count
	 * @param status
	 * @return
	 */
	public int getPaymentInterfaceCount(long status);
	/**
	 * 通过nid查询第三方支付接口
	 * @param nid
	 * @return
	 */
	public List getOnlyPaymentInterface(String nid);
	/**
	 * 删除线下银行
	 * @param id
	 */
	public void deleteRechargeDownBank(long id);
	//v1.6.7.2 RDPROJECT-539 wcw 2013-12-06 start
	public List getDownRechargeBankList();
	//v1.6.7.2 RDPROJECT-539 wcw 2013-12-06 end
	}
