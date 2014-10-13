package com.p2psys.model.borrow;

import com.p2psys.context.Constant;
import com.p2psys.context.Global;


/**
 * 抵押标或者给力标
 * 
 
 * @date 2012-9-5 下午5:18:32
 * @version
 *
 *  (c)</b> 2012-rongdu-<br/>
 *
 */
public class MortgageBorrowModel extends BaseBorrowModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -965497211520156565L;
	private BorrowModel model;
	

	public MortgageBorrowModel(BorrowModel model) {
		super(model);
		this.model=model;
		this.model.setBorrowType(Constant.TYPE_MORTGAGE);
		this.model.setType(Constant.TYPE_MORTGAGE);
//		this.model.setIs_fast(1);
		init();
	}


	@Override
	public double getTransactionFee() {
		return model.getAccount()*Global.getDouble("transaction_fee");
	}
	
	

}
