package com.p2psys.model.borrow;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.dao.AccountDao;
import com.p2psys.dao.UserAmountDao;
import com.p2psys.domain.Repayment;
import com.p2psys.domain.UserAmount;
import com.p2psys.util.NumberUtils;


/**
 * 信用标
 * 
 
 * @date 2012-9-5 下午5:18:52
 * @version
 *
 *  (c)</b> 2012-rongdu-<br/>
 *
 */
public class CreditBorrowModel extends BaseBorrowModel {

	private static final long serialVersionUID = 6478298326297026207L;

	private BorrowModel model;
	

	public CreditBorrowModel(BorrowModel model) {
		super(model);
		this.model=model;
		this.model.setBorrowType(Constant.TYPE_CREDIT);
		this.model.setType(Constant.TYPE_CREDIT);
		init();
	}
	
	@Override
	public double getTransactionFee() {
		return model.getAccount()*Global.getDouble("transaction_fee");
	}
	
	@Override
	public void borrowRepayHandleBorrow(Repayment repay, double lateInterest, double repayLateInterest) {
		super.borrowRepayHandleBorrow(repay, lateInterest, repayLateInterest);
		
		//如果是信用标，将本次的借款金额累加回借款人的信用额度，生成信用额度流水
		double capital = NumberUtils.getDouble(repay.getCapital());
		
		WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
		UserAmountDao userAmountDao = (UserAmountDao)ctx.getBean("userAmountDao");
		UserAmount amount = userAmountDao.getUserAmount(model.getUser_id());
		userAmountDao.updateCreditAmount(0, capital, -capital, amount);
	}
}
