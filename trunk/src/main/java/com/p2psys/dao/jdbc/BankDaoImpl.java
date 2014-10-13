package com.p2psys.dao.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.p2psys.common.enums.EnumTroubleFund;
import com.p2psys.dao.BankDao;
import com.p2psys.domain.AccountBank;
import com.p2psys.domain.OnlineBank;
import com.p2psys.domain.Payment;
import com.p2psys.domain.PaymentInterface;
import com.p2psys.model.AccountBankModel;
import com.p2psys.model.account.OnlineBankModel;
import com.p2psys.util.StringUtils;

public class BankDaoImpl extends BaseDaoImpl implements BankDao {

	private static Logger logger = Logger.getLogger(BankDaoImpl.class);  
	@Override
	public List getDownRechargeBankList(int start, int end) {
		String selectSql="select p1.id,p1.account,p1.branch,p1.bank_realname,p1.payment,p2.name as bank_name,p1.`order` from dw_account_bank as p1 left join dw_linkage as p2 on p2.id=p1.bank and p2.type_id=25 where p1.user_id=0 ";
		selectSql+=" order by p1.`order`";
		if(end<1){
			 logger.debug("getDownRechargeBankListsql():"+selectSql);
			 List list=getJdbcTemplate().query(selectSql, new Object[]{}, getBeanMapper(AccountBankModel.class));
				return list;
		}else{
			String limitSql=" limit ?,?";
			selectSql+=limitSql;
		}
		logger.debug("getDownRechargeBankListsql():"+selectSql);
		List list=getJdbcTemplate().query(selectSql, new Object[]{start,end}, getBeanMapper(AccountBankModel.class));
		return list;
	}
	//v1.6.7.2 RDPROJECT-539 wcw 2013-12-06 start
	@Override
	public List getDownRechargeBankList() {
		String selectSql="select p1.id,p1.account,p1.branch,p1.bank_realname,p1.payment,p2.name as bank_name,p1.`order` from dw_account_bank as p1 left join dw_linkage as p2 on p2.id=p1.bank and p2.type_id=25 where p1.user_id=0 group by p1.bank ";
		logger.debug("getDownRechargeBankListsql():"+selectSql);
		List list=getJdbcTemplate().query(selectSql, new Object[]{}, getBeanMapper(AccountBankModel.class));
		return list;
	}
	//v1.6.7.2 RDPROJECT-539 wcw 2013-12-06 end
	@Override
	public AccountBank getDownRechargeBank(int id) {
		String selectSql="select p1.id,p1.bank,p1.account,p1.branch,p1.bank_realname,p1.payment,p2.name as bank_name,p1.`order` from dw_account_bank as p1 left join dw_linkage as p2 on p2.id=p1.bank and p2.type_id=25 where p1.user_id=0 ";
		selectSql+=" and p1.id=? ";
		selectSql+=" order by p1.`order` ";
		logger.debug("getDownRechargeBanksql():"+selectSql);
		AccountBank accountBank=null;
		try {
			 accountBank=getJdbcTemplate().queryForObject(selectSql, new Object[]{id}, getBeanMapper(AccountBankModel.class));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return accountBank;
	}
	@Override
	public int getDownRechargeBankCount() {
		String selectSql="select count(1) from dw_account_bank where user_id=0 ";
		logger.debug("getDownRechargeBankCountsql():"+selectSql);
		int total=0;
		total=count(selectSql);
		return total;
	}
	@Override
	public PaymentInterface addPayInterface(PaymentInterface paymentInterface){
		//v1.6.7.2 RDPROJECT-417 wcw 2013-12-11 start
		final String sql = "insert into dw_payment_interface(name,merchant_id,`key`,recharge_fee,return_url,notice_url,is_enable_single,chartset,interface_Into_account,interface_value,is_enable_unsingle,pay_style,signType,seller_email,transport,order_description,`order`,gatewayUrl,orderInquireUrl,cert_position) "
				+ "values(:name,:merchant_id,:key,:recharge_fee,:return_url,:notice_url,:is_enable_single,:chartset,:interface_Into_account,:interface_value,:is_enable_unsingle,:pay_style,:signType,:seller_email,:transport,:order_description,:order,:gatewayUrl,:orderInquireUrl,:cert_position)";
		 //v1.6.7.2 RDPROJECT-417 wcw 2013-12-11 end
		SqlParameterSource ps = new BeanPropertySqlParameterSource(paymentInterface);
		 KeyHolder keyHolder = new GeneratedKeyHolder();
		 getNamedParameterJdbcTemplate().update(sql, ps, keyHolder);
		 int id = keyHolder.getKey().intValue();
		 paymentInterface.setId(id);
		return paymentInterface;
	}
	@Override
	public List getPayInterfaceUnsingleList(int is_enable_unsingle) {
		String selectSql="select * from dw_payment_interface  where 1=1 ";
		selectSql+="  and is_enable_unsingle=?";
		selectSql+=" order by `order` ";
		logger.debug("getDownRechargeBankListsql():"+selectSql);
		List  list=new ArrayList();
		try {
			list=getJdbcTemplate().query(selectSql, new Object[]{is_enable_unsingle}, getBeanMapper(PaymentInterface.class));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}
	@Override
	public int getPayInterfaceCount() {
		String selectSql="select count(1) from dw_payment_interface  ";
		logger.debug("getPayInterfaceCountsql():"+selectSql);
		int total=0;
		total=count(selectSql);
		return total;
	}
	@Override
	public PaymentInterface updatePayInterface(PaymentInterface p) {
		String sql="update dw_payment_interface set name=:name,merchant_id=:merchant_id,`key`=:key,recharge_fee=:recharge_fee,notice_url=:notice_url,return_url=:return_url," +
				"is_enable_single=:is_enable_single,is_enable_unsingle=:is_enable_unsingle,chartset=:chartset,interface_Into_account=:interface_Into_account,interface_value=:interface_value,signType=:signType,pay_style=:pay_style,seller_email=:seller_email," +
				//v1.6.7.2 RDPROJECT-417 wcw 2013-12-02 start
				"transport=:transport,order_description=:order_description,`order`=:order,gatewayUrl=:gatewayUrl,orderInquireUrl=:orderInquireUrl,cert_position=:cert_position where id=:id";
		//v1.6.7.2 RDPROJECT-417 wcw 2013-12-02 end
		SqlParameterSource ps = new BeanPropertySqlParameterSource(p);
		int ret=this.getNamedParameterJdbcTemplate().update(sql.toString(), ps);
		if(ret==1) return p;
		return p;
	}
	@Override
	public OnlineBank addOnlineBank(OnlineBank onlineBank){
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String sql="insert into dw_online_bank(bank_name,bank_logo,bank_value,payment_interface_id) values(:bank_name,:bank_logo,:bank_value,:payment_interface_id)";
		SqlParameterSource ps = new BeanPropertySqlParameterSource(onlineBank);
		this.getNamedParameterJdbcTemplate().update(sql.toString(), ps);
		int id = keyHolder.getKey().intValue();
		onlineBank.setId(id);
		return onlineBank;
	}
	@Override
	public List getOnlineBankList(String payment_interface_id) {
		String selectSql="select a.*,b.name as `name` from dw_online_bank as a left join dw_payment_interface as b on a.payment_interface_id=b.interface_value where 1=1 ";
		if(!StringUtils.isBlank(payment_interface_id)){
			 selectSql+=" and a.payment_interface_id=? and b.is_enable_single=? and a.is_enable=1";
				logger.debug("getOnlineBankListsql():"+selectSql);
				List list=getJdbcTemplate().query(selectSql, new Object[]{payment_interface_id,EnumTroubleFund.FIRST.getValue()}, getBeanMapper(OnlineBankModel.class));
				return list;
		}
		logger.debug("getOnlineBankListsql():"+selectSql);
		List list=new ArrayList();
		try {
			 list=getJdbcTemplate().query(selectSql, new Object[]{}, getBeanMapper(OnlineBankModel.class));

		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}
	@Override
	public int getOnlineBankCount() {
		String selectSql="select count(1) from dw_online_bank  ";
		logger.debug("getOnlineBankCountsql():"+selectSql);
		int total=0;
		total=count(selectSql);
		return total;
	}
	public OnlineBank updateOnlineBank(OnlineBank p){
		String sql="update dw_online_bank set bank_name=?,bank_logo=?,`bank_value`=?,payment_interface_id=? where id=?";
		int ret=this.getJdbcTemplate().update(sql, p.getBank_name(),p.getBank_logo(),p.getBank_value(),p.getPayment_interface_id(),p.getId());
		if(ret==1) return p;
		return p;
	}
	@Override
	public OnlineBank getOnlineBank(int id) {
		String selectSql="select a.*,b.name as name from dw_online_bank as a left join dw_payment_interface as b on a.payment_interface_id=b.id where 1=1 ";
		if(id!=EnumTroubleFund.ZERO.getValue()){
			selectSql+=" and a.id=? ";
		}
		logger.debug("getOnlineBanksql():"+selectSql);
		OnlineBankModel onlineBank=null;
		try {
			 onlineBank=getJdbcTemplate().queryForObject(selectSql, new Object[]{id}, getBeanMapper(OnlineBankModel.class));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return onlineBank;
	}
	@Override
	public PaymentInterface getPaymentInterface(int id) {
		String selectSql="select * from dw_payment_interface where 1=1 ";
		if(id!=EnumTroubleFund.ZERO.getValue()){
			selectSql+=" and id=? ";
			selectSql+=" order by `order` ";
			logger.debug("getPaymentInterfacesql():"+selectSql);
			PaymentInterface paymentInterface=null;
			try {
				 paymentInterface=getJdbcTemplate().queryForObject(selectSql, new Object[]{id}, getBeanMapper(PaymentInterface.class));

			} catch (Exception e) {
				// TODO: handle exception
			}
			return paymentInterface;
		}
		selectSql+=" order by `order` ";
		logger.debug("getPaymentInterfacesql():"+selectSql);
		PaymentInterface paymentInterface=null;
		try {
			 paymentInterface=getJdbcTemplate().queryForObject(selectSql, new Object[]{}, getBeanMapper(PaymentInterface.class));

		} catch (Exception e) {
			// TODO: handle exception
		}
		return paymentInterface;
	}
	@Override
	public void deletePaymentInterface(long id){
		String sql="delete from dw_payment_interface where id=?";
		this.getJdbcTemplate().update(sql, id);
	}
	@Override
	public void deleteRechargeDownBank(long id){
		String sql="delete from dw_account_bank where id=?";
		this.getJdbcTemplate().update(sql, id);
	}
	@Override
	public Payment getPayment(String nid) {
		Payment payment=null;
		String selectSql="select * from dw_payment where nid=?";
		logger.debug("getPaymentsql():"+selectSql);
		try {
			payment=getJdbcTemplate().queryForObject(selectSql, new Object[]{nid}, getBeanMapper(Payment.class));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return payment;
	}
	@Override
	public List getPaymentList(int status) {
		Payment payment=null;
		String selectSql="select * from dw_payment where status=?";
		logger.debug("getPaymentsql():"+selectSql);
		List list=new ArrayList();
		try {
			list=getJdbcTemplate().query(selectSql, new Object[]{status}, getBeanMapper(Payment.class));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}
	@Override
	public List getPaymentInterfaceSingleList(long status) {
		String selectSql="select * from dw_payment_interface where 1=1";
		if(status!=0){
			selectSql+=" and is_enable_single=? ";
		}
		selectSql+=" order by `order` ";
		logger.debug("getPaymentsql():"+selectSql);
		List list=new ArrayList();
		if(status!=0){
			try {
				list=getJdbcTemplate().query(selectSql, new Object[]{status}, getBeanMapper(PaymentInterface.class));
			} catch (Exception e) {
				// TODO: handle exception
			}
			return list;
		}
		try {
			list=getJdbcTemplate().query(selectSql, new Object[]{}, getBeanMapper(PaymentInterface.class));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}
	@Override
	public List getOnlyPaymentInterface(String nid) {
		String selectSql="select * from dw_payment_interface where interface_value=?";
		logger.debug("getPaymentsql():"+selectSql);
		List list=new ArrayList();
		try {
			list=getJdbcTemplate().query(selectSql, new Object[]{nid}, getBeanMapper(PaymentInterface.class));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}
	@Override
	public int getPaymentInterfaceCount(long status) {
		Payment payment=null;
		String selectSql="select count(1) as count from dw_payment_interface where 1=1";
		if(status!=0){
			selectSql+=" and is_enable_single=? ";
		}
		logger.debug("getPaymentsql():"+selectSql);
	    int count=this.getJdbcTemplate().queryForInt(selectSql, new Object[]{status});
		return count;
	}
}
