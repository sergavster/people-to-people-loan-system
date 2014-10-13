package com.p2psys.model.borrow.protocol;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.itextpdf.text.DocumentException;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.domain.Borrow;
import com.p2psys.domain.User;
import com.p2psys.model.BorrowTender;
import com.p2psys.model.borrow.BorrowHelper;
import com.p2psys.tool.itext.PdfHelper;
import com.p2psys.util.DateUtils;

public class BaseBorrowProtocol extends BorrowProtocol {
	public BaseBorrowProtocol(User user, long borrow_id, long tender_id,
			int borrowType, int templateType) {
		super(user, borrow_id, tender_id, borrowType, templateType);
	}

	public BaseBorrowProtocol(User user, long borrow_id, long tender_id) {
		super(user, borrow_id, tender_id);
	}

	@Override
	protected void addPdfTable(PdfHelper pdf, Borrow b)
			throws DocumentException {
		List<BorrowTender> list = new ArrayList(); 
//		if(b.getIs_flow()==1&&(Global.getWebid().equals("zbzr")||Global.getWebid().equals("mdw"))){
		if(b.getType() == Constant.TYPE_FLOW&&(Global.getWebid().equals("zbzr")||Global.getWebid().equals("mdw"))){
			User user=getTenderUser();
			list=getBorrowService().getTenderList(getBorrow().getId(),user==null?0:user.getUser_id());
		}else{
			list=getBorrowService().getTenderList(getBorrow().getId());
		}
		List cellList = null;
		List[] args = new List[list.size() + 1];
		// 出借人(id)
		cellList = new ArrayList();
		cellList.add("出借人(id)");
		cellList.add("借款金额");
		cellList.add("借款期限");
		cellList.add("年利率");
		cellList.add("借款开始日");
		cellList.add("借款到期日");
		cellList.add("截止还款日");
		cellList.add("还款本息");
		args[0] = cellList;
		for (int i = 1; i < list.size() + 1; i++) {
			BorrowTender t = list.get(i - 1);
			cellList = new ArrayList();
			cellList.add(t.getUsername());
			cellList.add(t.getAccount() + "");
			cellList.add(b.getTime_limit());
			cellList.add(b.getApr() + "");
			cellList.add(DateUtils.dateStr2(BorrowHelper.getBorrowVerifyTime(b,
					t)));
			Date d = BorrowHelper.getBorrowRepayTime(b, t);
			cellList.add(DateUtils.dateStr2(d));
			cellList.add("每月截止" + DateUtils.getDay(d) + "日");
			cellList.add(t.getRepayment_account());
			args[i] = cellList;
		}
		pdf.addTable(args, 80, 7);
	}
	
	
	
}
