package com.p2psys.model.borrow;

import com.p2psys.context.Constant;


/**
 * 助学贷款
 * 
 
 * @date 2012-11-6 下午3:17:07
 * @version
 *
 *  (c)</b> 2012-rongdu-<br/>
 *
 */
public class OffVouchBorrowModel extends BaseBorrowModel {

	private static final long serialVersionUID = -965497211520156565L;
	private BorrowModel model;
	

	public OffVouchBorrowModel(BorrowModel model) {
		super(model);
		this.model=model;
		this.model.setBorrowType(Constant.TYPE_OFFVOUCH);
		this.model.setType(Constant.TYPE_OFFVOUCH);
//		this.model.setIs_offvouch(1);
		init();
	}
	

}
