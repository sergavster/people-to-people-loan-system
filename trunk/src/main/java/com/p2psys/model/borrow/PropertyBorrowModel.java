package com.p2psys.model.borrow;

import com.p2psys.context.Constant;



/**
 * 资产标或者净值标
 * 
 
 * @date 2012-9-5 下午4:37:46
 * @version
 *
 *  (c)</b> 2012-rongdu-<br/>
 *
 */
public class PropertyBorrowModel extends BaseBorrowModel {
	
	private BorrowModel model;
	
	private static final long serialVersionUID = -1490035608742973452L;

	public PropertyBorrowModel(BorrowModel model) {
		super(model);
		this.model=model;
		this.model.borrowType=Constant.TYPE_PROPERTY;
		this.model.setType(Constant.TYPE_PROPERTY);
//		this.model.setIs_jin(1);
		init();
	}
	
	
}
