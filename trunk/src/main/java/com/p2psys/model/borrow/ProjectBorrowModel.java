package com.p2psys.model.borrow;

import com.p2psys.context.Constant;

/**
 * 项目标，有保障利息以及浮动分红
 * 
 
 * @date 2012-10-23 上午10:01:59
 * @version
 *
 *  (c)</b> 2012-rongdu-<br/>
 *
 */
public class ProjectBorrowModel extends BaseBorrowModel {
	
	private static final long serialVersionUID = -965497211520156565L;
	private BorrowModel model;
	

	public ProjectBorrowModel(BorrowModel model) {
		super(model);
		this.model=model;
		this.model.setBorrowType(Constant.TYPE_PROJECT);
		this.model.setType(Constant.TYPE_PROJECT);
//		this.model.setIs_project(1);
		init();
	}
	

}
