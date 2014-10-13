package com.p2psys.model.borrow;

import com.p2psys.context.Constant;


/**
 * 文化艺术品专区
 * 
 
 * @date 2012-9-5 下午5:18:32
 * @version
 *
 *  (c)</b> 2012-rongdu-<br/>
 *
 */
public class CharityBorrowModel extends MortgageBorrowModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -965497211520156565L;
	private BorrowModel model;
	

	public CharityBorrowModel(BorrowModel model) {
		super(model);
		this.model=model;
		this.model.setBorrowType(Constant.TYPE_CHARITY);
//		this.model.setIs_charity(1);
		this.model.setType(Constant.TYPE_CHARITY);
		init();
	}
	

}
