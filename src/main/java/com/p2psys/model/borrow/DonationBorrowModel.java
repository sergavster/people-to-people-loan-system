package com.p2psys.model.borrow;

import com.p2psys.context.Constant;

public class DonationBorrowModel extends BaseBorrowModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8364474289482249293L;
	private BorrowModel model;

	public DonationBorrowModel(BorrowModel model) {
		super(model);
		this.model=model;
		this.model.setBorrowType(Constant.TYPE_DONATION);
//		this.model.setIs_donation(1);
		this.model.setType(Constant.TYPE_DONATION);
		init();
	}
	
}
