package com.p2psys.model.borrow;

import com.p2psys.context.Constant;

/**
 * 联名担保标
 
 * @version 1.0
 * @since 2013-9-11
*/ 

public class JointGuaranteeBorrowModel extends BaseBorrowModel {

	private static final long serialVersionUID = 627215962542614794L;
	private BorrowModel model;

	public JointGuaranteeBorrowModel(BorrowModel model) {
		super(model);
		this.model=model;
		this.model.setBorrowType(Constant.TYPE_JOINTGUARANTEE);
		this.model.setType(Constant.TYPE_JOINTGUARANTEE);
//		this.model.setIs_jointguarantee(1);
		init();
	}

}
