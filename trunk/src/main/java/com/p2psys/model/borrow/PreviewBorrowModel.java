package com.p2psys.model.borrow;

import com.p2psys.context.Constant;
import com.p2psys.tool.interest.EndInterestCalculator;
import com.p2psys.tool.interest.InterestCalculator;
import com.p2psys.util.NumberUtils;


/**
 * 预定标
 * 
 
 * @date 2013-10-30 9:25:51
 * @version
 *
 *  (c)</b> 2013-rongdu-<br/>
 *
 */
public class PreviewBorrowModel extends BaseBorrowModel {
	
	private static final long serialVersionUID = 7375703874958748525L;

	private BorrowModel model;

	public PreviewBorrowModel(BorrowModel model) {
		super(model);
		this.model=model;
		//this.model.setIs_preview(1);
		this.model.setBorrowType(Constant.TYPE_PREVIEW);
		this.model.setNeedBorrowFee(false);
		init();
	}

	@Override
	public double calculateInterest() {
		InterestCalculator ic= interestCalculator();
		double interest=ic.getMoneyPerMonth()*ic.getPeriod()-getModel().getFlow_money()*getModel().getFlow_count();
		return interest;
	}

	@Override
	public InterestCalculator interestCalculator() {
		BorrowModel model=getModel();
		double account=model.getFlow_money()*model.getFlow_count();
		InterestCalculator ic=interestCalculator(account);
		return ic;
	}

	@Override
	public InterestCalculator interestCalculator(double validAccount) {
		BorrowModel model=getModel();
		InterestCalculator ic=null;
		double apr=model.getApr()/100;
		if(model.getIsday()==1){
			int time_limit_day=model.getTime_limit_day();
			ic =new EndInterestCalculator(validAccount,apr,time_limit_day);
		}else{
			int period=NumberUtils.getInt(model.getTime_limit());
			ic =new EndInterestCalculator(validAccount,apr,period,InterestCalculator.TYPE_MONTH_END);
		}
		ic.each();
		return ic;
	}
	@Override
	public boolean allowFullSuccess() {
		return false;
	}

	@Override
	public boolean checkModelData() {
		super.checkModelData();
		model.setApr(0);
		model.setIsday(1);
		model.setTime_limit_day(30);
		model.setValid_time("30");
		return true;
	}

	

	
}
