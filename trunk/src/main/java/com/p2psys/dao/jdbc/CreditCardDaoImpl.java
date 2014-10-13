package com.p2psys.dao.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

import com.p2psys.dao.CreditCardDao;
import com.p2psys.domain.CreditCard;
import com.p2psys.model.SearchParam;

/**
 * 信用卡相关的Dao操作类
 
 * 
 */
public class CreditCardDaoImpl extends BaseDaoImpl implements CreditCardDao {

	private static Logger logger = Logger.getLogger(CreditCardDaoImpl.class);
	
	/**
	 * 查找所有的信用卡
	 */
	public List getList(int page, int max, SearchParam p) {
		String sql = "select * from dw_credit_card";
		sql += p.getSearchParamSql();
		sql += "  LIMIT ?,?";
		List list = getJdbcTemplate().query(sql, new Object[] { page , max }, getBeanMapper(CreditCard.class));
		return list;
	}
	
	public int getSearchCard(SearchParam param) {
		String selectSql = "select count(p2.card_id) from dw_credit_card p2 where 1=1 ";
		String searchSql = param.getSearchParamSql();
		// v1.6.7.2 RDPROJECT-554 zza start
//		searchSql = searchSql.replace("p1", "p2");
		// v1.6.7.2 RDPROJECT-554 zza end
		StringBuffer sb = new StringBuffer(selectSql);
		String querySql = sb.append(searchSql).toString();
		logger.debug("getSearchCard():" + querySql);
		int total = 0;
		total = count(querySql);
		return total;
		
	}
	
	/**
	 * 新增信用卡
	 * @param creditCard
	 */
	public CreditCard addCreditCard(CreditCard creditCard) {
		String sql = "insert into dw_credit_card(name,issuing_bank,issuing_nstitution,issuing_status,check_style,interest," +
				"tel,currency,grade,borrowing_limit,interest_free,integral_policy,integral_indate,credit_rules,scoring_rules," +
				"installment,amount,fee_payment,applicable_fee,prepayment,open_card,repea_card,app_condition,app_way," +
				"submit_info,supplement_num,supplement_app,report_loss,loss_protection,loss_tel,lowest_refund," +
				"allopatry_back_fee,rmb_repayment,foreign_repayment,special_repayment,card_features,add_service,joint_discount," +
				"main_card_fee,supplement_card_fee,year_cut_rules,fee_date,local_fee,local_interbank_fee,offsite_fee," +
				"offsite_interbank_fee,overseas_pay_fee,overseas_unpay_fee,overseas_meet_fee,enchashment_limit,localback_fee," +
				"local_interbank_back_fee,offsite_overflow_back_fee,offsite_interbank_back_fee,overseas_pay_back_fee," +
				"overseas_unpay_back_fee,overflow_back_rules,message_money,overseas_fee,change_card,ahead_change_card," +
				"express_fee,statement_fee,statement_free_clause,loss_fee,reset_password_fee,selfdom_card_fee," +
				"foreign_convert_fee,slip_fee,slip_fee_copy,slip_fee_foreign,slip_fee_copy_foreign,overdue_fine," +
				"transfinite_fee,type_value,litpic) "
				+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
				"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		logger.debug("SQL:" + sql);
		this.getJdbcTemplate().update(sql, creditCard.getName(),creditCard.getIssuing_bank(),creditCard.getIssuing_nstitution(),
				creditCard.getIssuing_status(),creditCard.getCheck_style(),creditCard.getInterest(),creditCard.getTel(),
				creditCard.getCurrency(),creditCard.getGrade(),creditCard.getBorrowing_limit(),
				creditCard.getInterest_free(),creditCard.getIntegral_policy(),creditCard.getIntegral_indate(),
				creditCard.getCredit_rules(),creditCard.getScoring_rules(),creditCard.getInstallment(),creditCard.getAmount(),
				creditCard.getFee_payment(),creditCard.getApplicable_fee(),
				creditCard.getPrepayment(),creditCard.getOpen_card(),creditCard.getRepea_card(),creditCard.getApp_condition(),
				creditCard.getApp_way(),creditCard.getSubmit_info(),creditCard.getSupplement_num(),creditCard.getSupplement_app(),
				creditCard.getReport_loss(),creditCard.getLoss_protection(),creditCard.getLoss_tel(),creditCard.getLowest_refund(),
				creditCard.getAllopatry_back_fee(),creditCard.getRmb_repayment(),creditCard.getForeign_repayment(),
				creditCard.getSpecial_repayment(),creditCard.getCard_features(),creditCard.getAdd_service(),
				creditCard.getJoint_discount(),creditCard.getMain_card_fee(),creditCard.getSupplement_card_fee(),creditCard.getYear_cut_rules(),
				creditCard.getFee_date(),creditCard.getLocal_fee(),creditCard.getLocal_interbank_fee(),creditCard.getOffsite_fee(),
				creditCard.getOffsite_interbank_fee(),creditCard.getOverseas_pay_fee(),creditCard.getOverseas_unpay_fee(),
				creditCard.getOverseas_meet_fee(),creditCard.getEnchashment_limit(),creditCard.getLocalback_fee(),creditCard.getLocal_interbank_back_fee(),
				creditCard.getOffsite_overflow_back_fee(),creditCard.getOffsite_interbank_back_fee(),creditCard.getOverseas_pay_back_fee(),
				creditCard.getOverseas_unpay_back_fee(),creditCard.getOverflow_back_rules(),creditCard.getMessage_money(),
				creditCard.getOverseas_fee(),creditCard.getChange_card(),creditCard.getAhead_change_card(),creditCard.getExpress_fee(),
				creditCard.getStatement_fee(),creditCard.getStatement_free_clause(),creditCard.getLoss_fee(),creditCard.getReset_password_fee(),
				creditCard.getSelfdom_card_fee(),creditCard.getForeign_convert_fee(),creditCard.getSlip_fee(),creditCard.getSlip_fee_copy(),
				creditCard.getSlip_fee_foreign(),creditCard.getSlip_fee_copy_foreign(),creditCard.getOverdue_fine(),
				creditCard.getTransfinite_fee(),creditCard.getType_value(),creditCard.getLitpic());
		return creditCard;
	}
	
	/**
	 * 根据ID取得对应的信用卡信息
	 * @param cardId
	 * @return
	 */
	public CreditCard getCardById(int cardId) {
		String sql = "select * from dw_credit_card where card_id=?";
		logger.debug("SQL:" + sql);
		logger.debug("SQL:" + cardId);
		CreditCard creditCard = null;
		try {
			creditCard = this.getJdbcTemplate().queryForObject(sql,
					new Object[] { cardId }, getBeanMapper(CreditCard.class));
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
			e.printStackTrace();
		}
		return creditCard;
	}
	
	/**
	 * 修改
	 */
	public void updateCreditCard(CreditCard creditCard) {
		String sql = "update dw_credit_card set name=?,issuing_bank=?,issuing_nstitution=?,issuing_status=?,check_style=?," +
				"interest=?,tel=?,currency=?,grade=?,borrowing_limit=?,interest_free=?,integral_policy=?,integral_indate=?,credit_rules=?," +
				"scoring_rules=?,installment=?,amount=?,fee_payment=?,applicable_fee=?,prepayment=?,open_card=?,repea_card=?,app_condition=?," +
				"app_way=?,submit_info=?,supplement_num=?,supplement_app=?,report_loss=?,loss_protection=?,loss_tel=?,lowest_refund=?," +
				"allopatry_back_fee=?,rmb_repayment=?,foreign_repayment=?,special_repayment=?,card_features=?,add_service=?,joint_discount=?,main_card_fee=?," +
				"supplement_card_fee=?,year_cut_rules=?,fee_date=?,local_fee=?,local_interbank_fee=?,offsite_fee=?,offsite_interbank_fee=?," +
				"overseas_pay_fee=?,overseas_unpay_fee=?,overseas_meet_fee=?,enchashment_limit=?,localback_fee=?,local_interbank_back_fee=?," +
				"offsite_overflow_back_fee=?,offsite_interbank_back_fee=?,overseas_pay_back_fee=?,overseas_unpay_back_fee=?," +
				"overflow_back_rules=?,message_money=?,overseas_fee=?,change_card=?,ahead_change_card=?,express_fee=?,statement_fee=?," +
				"statement_free_clause=?,loss_fee=?,reset_password_fee=?,selfdom_card_fee=?,foreign_convert_fee=?,slip_fee=?,slip_fee_copy=?," +
				"slip_fee_foreign=?,slip_fee_copy_foreign=?,overdue_fine=?,transfinite_fee=?,type_value=?,litpic=? where card_id=?";
		logger.debug("SQL:" + sql);
		this.getJdbcTemplate().update(sql, creditCard.getName(),creditCard.getIssuing_bank(),creditCard.getIssuing_nstitution(),creditCard.getIssuing_status(),
				creditCard.getCheck_style(),creditCard.getInterest(),creditCard.getTel(),creditCard.getCurrency(),creditCard.getGrade(),
				creditCard.getBorrowing_limit(),creditCard.getInterest_free(),creditCard.getIntegral_policy(),creditCard.getIntegral_indate(),
				creditCard.getCredit_rules(),creditCard.getScoring_rules(),creditCard.getInstallment(),creditCard.getAmount(),
				creditCard.getFee_payment(),creditCard.getApplicable_fee(),creditCard.getPrepayment(),creditCard.getOpen_card(),
				creditCard.getRepea_card(),creditCard.getApp_condition(),creditCard.getApp_way(),creditCard.getSubmit_info(),
				creditCard.getSupplement_num(),creditCard.getSupplement_app(),creditCard.getReport_loss(),creditCard.getLoss_protection(),
				creditCard.getLoss_tel(),creditCard.getLowest_refund(),creditCard.getAllopatry_back_fee(),creditCard.getRmb_repayment(),
				creditCard.getForeign_repayment(),creditCard.getSpecial_repayment(),creditCard.getCard_features(),creditCard.getAdd_service(),
				creditCard.getJoint_discount(),creditCard.getMain_card_fee(),creditCard.getSupplement_card_fee(),creditCard.getYear_cut_rules(),
				creditCard.getFee_date(),creditCard.getLocal_fee(),creditCard.getLocal_interbank_fee(),creditCard.getOffsite_fee(),
				creditCard.getOffsite_interbank_fee(),creditCard.getOverseas_pay_fee(),creditCard.getOverseas_unpay_fee(),creditCard.getOverseas_meet_fee(),
				creditCard.getEnchashment_limit(),creditCard.getLocalback_fee(),creditCard.getLocal_interbank_back_fee(),
				creditCard.getOffsite_overflow_back_fee(),creditCard.getOffsite_interbank_back_fee(),creditCard.getOverseas_pay_back_fee(),
				creditCard.getOverseas_unpay_back_fee(),creditCard.getOverflow_back_rules(),creditCard.getMessage_money(),creditCard.getOverseas_fee(),
				creditCard.getChange_card(),creditCard.getAhead_change_card(),creditCard.getExpress_fee(),creditCard.getStatement_fee(),
				creditCard.getStatement_free_clause(),creditCard.getLoss_fee(),creditCard.getReset_password_fee(),creditCard.getSelfdom_card_fee(),
				creditCard.getForeign_convert_fee(),creditCard.getSlip_fee(),creditCard.getSlip_fee_copy(),creditCard.getSlip_fee_foreign(),
				creditCard.getSlip_fee_copy_foreign(),creditCard.getOverdue_fine(),creditCard.getTransfinite_fee(),
				creditCard.getType_value(),creditCard.getLitpic(),creditCard.getCard_id());
	}
	
	
	/**
	 * 修改
	 */
	public void updateLitpic(String litpic, int cardId) {
		String sql = "update dw_credit_card set litpic=? where card_id=?";
		logger.debug("SQL:" + sql);
		this.getJdbcTemplate().update(sql, litpic, cardId);
	}

	/**
	 * 根据类型查询信用卡
	 */
	public List getCardByType(int type) {
		String sql = "select * from dw_credit_card where type_value=? limit 0,4";
		logger.debug("SQL:" + sql);
		List list = new ArrayList();
		try {
			list = this.getJdbcTemplate().query(sql, new Object[]{type}, getBeanMapper(CreditCard.class));
		} catch (DataAccessException e) {
			logger.debug("数据库查询结果为空！");
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 删除
	 */
	public void delCreditCard(int cardId) {
		String sql="delete from dw_credit_card where card_id=?";
		getJdbcTemplate().update(sql,cardId);
	}
		
}
