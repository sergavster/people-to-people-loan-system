package com.p2psys.model.borrow;

import com.p2psys.context.Constant;

/**
 * 实业标
 
 * @version 1.0
 * @since 2013-9-11
*/ 

public class IndustryBorrowModel extends BaseBorrowModel {

	private static final long serialVersionUID = 627215962542614794L;
	private BorrowModel model;

	public IndustryBorrowModel(BorrowModel model) {
		super(model);
		this.model=model;
		this.model.setBorrowType(Constant.TYPE_INDUSTRY);
//		this.model.setIs_industry(1);
		this.model.setType(Constant.TYPE_INDUSTRY);
		init();
	}

}
