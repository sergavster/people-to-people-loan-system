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
public class StudentBorrowModel extends BaseBorrowModel {

	private static final long serialVersionUID = -965497211520156565L;
	private BorrowModel model;
	

	public StudentBorrowModel(BorrowModel model) {
		super(model);
		this.model=model;
		this.model.setBorrowType(Constant.TYPE_STUDENT);
		this.model.setType(Constant.TYPE_STUDENT);
//		this.model.setIs_student(1);
//		this.model.setIs_xin(1);
		init();
	}

}
