package com.p2psys.model.borrow;

import java.util.Date;

import com.p2psys.context.Constant;
import com.p2psys.domain.Borrow;
import com.p2psys.domain.Tender;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;


public class BorrowHelper {
	/**
	 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部 
	 */
	public static BorrowModel getHelper(int type,BorrowModel model){
		switch(type){
		case 100:
			return new BaseBorrowModel(model);
		case 101:
			return new SecondBorrowModel(model);
		case 102:
			return new CreditBorrowModel(model);
		case 103:
			return new MortgageBorrowModel(model);
		case 104:
			return new PropertyBorrowModel(model);
		case 105:
			return new MortgageBorrowModel(model);
		case 106:
			return new ArtBorrowModel(model);
		case 107:
			return new CharityBorrowModel(model);
		case 108:
			//v1.6.7.1 RDPROJECT-461 wcw 2013-11-21 start
			return new PreviewBorrowModel(model);
			//v1.6.7.1 RDPROJECT-461 wcw 2013-11-21 end
		case 109:
			return new ProjectBorrowModel(model);
		case 110:
			return new FlowBorrowModel(model);
		case 111:
			return new StudentBorrowModel(model);
		case 112:
			return new OffVouchBorrowModel(model);
		case 113:
			return new PledgeBorrowModel(model);
		case 114:
			return new DonationBorrowModel(model);
		// v1.6.5.3 RDPROJECT-127 xx 2013.09.11 start
		case 115:
			return new IndustryBorrowModel(model);
		case 116:
			return new JointGuaranteeBorrowModel(model);
		// v1.6.5.3 RDPROJECT-127 xx 2013.09.11 end
		default:
			throw new RuntimeException("不正确的Borrow类型:"+type);
		}
	}
	
//	public static BorrowModel getHelper(BorrowModel model){
//		if(model.getIs_mb()==1){
//			return new SecondBorrowModel(model);
//		}else if(model.getIs_jin()==1){
//			return new PropertyBorrowModel(model);
//		}else if(model.getIs_fast()==1){
//			return new MortgageBorrowModel(model);
//		}else if(model.getIs_vouch()==1){
//			return new BaseBorrowModel(model);
//		}else if(model.getIs_xin()==1){
//			return new CreditBorrowModel(model);
//		}else if(model.getIs_art()==1){
//			return new ArtBorrowModel(model);
//		}else if(model.getIs_charity()==1){
//			return new CharityBorrowModel(model);
//		}else if(model.getIs_project()==1){
//			return new ProjectBorrowModel(model);
//		}else if(model.getIs_flow()==1){
//			return new FlowBorrowModel(model);
//		}else if(model.getIs_student()==1){
//			return new StudentBorrowModel(model);
//		}else if(model.getIs_offvouch()==1){
//			return new OffVouchBorrowModel(model);
//		}else if(model.getIs_donation()==1){
//			return new DonationBorrowModel(model);
//		}
//		// v1.6.5.3 RDPROJECT-127 xx 2013.09.11 start
//		else if(model.getIs_industry()==1){
//			return new IndustryBorrowModel(model);
//		}
//		else if(model.getIs_jointguarantee()==1){
//			return new JointGuaranteeBorrowModel(model);
//		}
//		// v1.6.5.3 RDPROJECT-127 xx 2013.09.11 end
//		else{
//			return new BaseBorrowModel(model);
//		}
//		
//	}
	 
	public static Date getBorrowProtocolTime(Borrow model){
		String verifyTime="0";
//		if(model.getIs_flow()!=1){
		if(model.getType()!=Constant.TYPE_FLOW){
			if(model.getStatus()>=3){
				verifyTime=model.getFull_verifytime();
			}
		}else{
			verifyTime=model.getAddtime();
		}
		Date d=DateUtils.getDate((verifyTime==null?"0":verifyTime));
		return d;
	}
	
	public static Date getBorrowVerifyTime(Borrow model,Tender t){
		String verifyTime="0";
//		if(model.getIs_flow()!=1){
		if(model.getType()!=Constant.TYPE_FLOW){
			if(model.getStatus()>=3){
				verifyTime=model.getFull_verifytime();
			}
		}else{
			verifyTime=t.getAddtime();
		}
		Date d=DateUtils.getDate((verifyTime==null?"0":verifyTime));
		return d;
	}
	
	public static Date getBorrowRepayTime(Borrow model,Tender t){
		Date d=getBorrowVerifyTime(model,t);
//		if(model.getIs_flow()!=1){
		if(model.getType()!=Constant.TYPE_FLOW){
			if(model.getStatus()<3){
				return DateUtils.getDate("0");
			}
			if(model.getIsday()==1){
				return DateUtils.rollDay(d, model.getTime_limit_day());
//			}else if(model.getIs_mb()==1){
			}else if(model.getType()==Constant.TYPE_SECOND){
				return  d;
			}else {
				return DateUtils.rollMon(d, NumberUtils.getInt(model.getTime_limit()));
			}
		}else{
			if(model.getIsday()==1){
				return DateUtils.rollDay(d, model.getTime_limit_day());
			}else{
				return DateUtils.rollMon(d, NumberUtils.getInt(model.getTime_limit()));
			}
			
		}
		
	}
	
}
