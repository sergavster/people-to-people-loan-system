package com.p2psys.model.creditlog;

import java.util.HashMap;
import java.util.Map;

import com.p2psys.common.enums.EnumCreditTemplateNid;
import com.p2psys.common.enums.EnumIntegralTypeName;
import com.p2psys.common.enums.EnumLogTemplateType;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.domain.CreditType;
import com.p2psys.domain.User;
import com.p2psys.domain.UserCredit;
import com.p2psys.domain.UserCreditLog;
import com.p2psys.util.DateUtils;
import com.p2psys.util.FreemarkerUtil;



/**
 * 手动修改积分父类
 
 * @version 1.0
 * @since 2013-10-18
 */
public class BaseHandEditLog extends BaseCreditLog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -141858044155521465L;

	
	public BaseHandEditLog() {
		super();
	}
	
	public BaseHandEditLog(long op , long op_user , long credit_type , long user_id, long credit_value, long valid_value) {
		super(op, op_user, credit_type, user_id, credit_value, valid_value);
	}
	
	/**
	 * 手动修改总积分，有效积分和消费积分日志记录
	 */
	@Override
	public void addCreditLog() {
		
		Map<String , Object> map = Global.getTransfer();
		UserCredit newCredit = (UserCredit) map.get("newCredit");//修改后的会员积分信息
		UserCredit oldCredit = (UserCredit) map.get("oldCredit");//修改前的会员积分信息
		String editRemark =  (String) map.get("editRemark");//修改前手动填写备注
		
		long user_id = newCredit.getUser_id();
		int value = newCredit.getValue() - oldCredit.getValue();// 变动的总积分
		int valid_value = newCredit.getValid_value() - oldCredit.getValid_value();// 变动的有效积分
		int expense_value = newCredit.getExpense_value() - oldCredit.getExpense_value();// 变动的消费积分
		
		byte totalOp = 0;
		if(value > 0 ){// 如果变动值为大于零，则说明是加
			totalOp = new Byte(Constant.OP_ADD);
		}else if(value < 0){//如果变动值为小于零，则说明是减，并将值转化为正数
			totalOp = new Byte(Constant.OP_REDUCE);
			value = -value;
		}
		
	 	CreditType totalType = userCreditDao.getCreditTypeByNid(EnumIntegralTypeName.INTEGRAL_TOTAL.getValue());
		User user = userDao.getUserByUserid(newCredit.getOp_user());
		// 综合积分日志
		if(totalOp != 0){// op不等于零，则说明值有变动
			UserCreditLog totalLog = new UserCreditLog();
			totalLog.setAddtime(Long.parseLong(DateUtils.getNowTimeStr()));
			totalLog.setOp(totalOp);
			totalLog.setOp_user(newCredit.getOp_user());
			totalLog.setType_id(totalType.getId());
			totalLog.setUser_id(user_id);
			totalLog.setValue(value);
			// v1.6.7.2 RDPROJECT-503 zza 2013-12-13 start
			totalLog.setValid_value(newCredit.getValid_value()); // 有效积分
			// v1.6.7.2 RDPROJECT-503 zza 2013-12-13 end
			if(editRemark != null && editRemark.length() > 0 && editRemark.length() < 255){//如果手动填写了备注，就将备注填写日志表中
				totalLog.setRemark(editRemark);
			}else {// 如果没有手动输入备注，则从模板中读取备注。
				//积分日志模板
				Map<String , Object> totalMap = new HashMap<String, Object>();
				totalMap.put("total", oldCredit.getValue());
				totalMap.put("new_total", newCredit.getValue());
				totalMap.put("username", user.getUsername());
				String totalLogType = EnumCreditTemplateNid.INTEGRAL_TOTAL_EDIT.getValue();
				logRemarkTemplate = Global.getLogTempValue(EnumLogTemplateType.CREDIT_LOG.getValue(), totalLogType);
				totalLog.setRemark(this.getLogRemark(totalMap));
			}
			userCreditDao.addUserCreditLog(totalLog);
		}
		byte validOp = 0;
		if(valid_value > 0 ){// 如果变动值为大于零，则说明是加
			validOp = new Byte(Constant.OP_ADD);
		}else if(valid_value < 0){//如果变动值为小于零，则说明是减，并将值转化为正数
			validOp = new Byte(Constant.OP_REDUCE);
			valid_value = -valid_value;
		}
		
	 	CreditType validType = userCreditDao.getCreditTypeByNid(EnumIntegralTypeName.VALID_CHANGE.getValue());
		// 有效积分日志
		if(validOp != 0){// op不等于零，则说明值有变动
			
			String validLogType = EnumCreditTemplateNid.INTEGRAL_VALID_EDIT.getValue();
			
			UserCreditLog validLog = new UserCreditLog();
			validLog.setAddtime(Long.parseLong(DateUtils.getNowTimeStr()));
			validLog.setOp(validOp);
			validLog.setOp_user(newCredit.getOp_user());
			validLog.setType_id(validType.getId());
			validLog.setUser_id(user_id);
			validLog.setValue(valid_value);
			// v1.6.7.2 RDPROJECT-503 zza 2013-12-13 start
			validLog.setValid_value(newCredit.getValid_value());
			// v1.6.7.2 RDPROJECT-503 zza 2013-12-13 end
			if(editRemark != null && editRemark.length() > 0 && editRemark.length() < 255){//如果手动填写了备注，就将备注填写日志表中
				validLog.setRemark(editRemark);
			}else{ // 如果没有手动输入备注，则从模板中读取备注。
				//积分日志模板
				Map<String , Object> validMap = new HashMap<String, Object>();
				validMap.put("valid", oldCredit.getValid_value());
				validMap.put("new_valid", newCredit.getValid_value());
				validMap.put("username", user.getUsername());
				logRemarkTemplate = Global.getLogTempValue(EnumLogTemplateType.CREDIT_LOG.getValue(), validLogType);
				validLog.setRemark(this.getLogRemark(validMap));
			}
			userCreditDao.addUserCreditLog(validLog);
		}
		
		byte expenseOp = 0;
		if(expense_value > 0 ){// 如果变动值为大于零，则说明是加
			expenseOp = new Byte(Constant.OP_ADD);
		}else if(expense_value < 0){//如果变动值为小于零，则说明是减，并将值转化为正数
			expenseOp = new Byte(Constant.OP_REDUCE);
			expense_value = -expense_value;
		}
		
	 	CreditType expenseType = userCreditDao.getCreditTypeByNid(EnumIntegralTypeName.EXPENSE_CHANGE.getValue());
		// 有效积分日志
		if(expenseOp != 0){// op不等于零，则说明值有变动
			String expenseLogType = EnumCreditTemplateNid.INTEGRAL_EXPENSE_EDIT.getValue();
			UserCreditLog expenseLog = new UserCreditLog();
			expenseLog.setAddtime(Long.parseLong(DateUtils.getNowTimeStr()));
			expenseLog.setOp(expenseOp);
			expenseLog.setOp_user(newCredit.getOp_user());
			expenseLog.setType_id(expenseType.getId());
			expenseLog.setUser_id(user_id);
			expenseLog.setValue(expense_value);
			// v1.6.7.2 RDPROJECT-503 zza 2013-12-13 start
			expenseLog.setValid_value(newCredit.getValid_value());
			// v1.6.7.2 RDPROJECT-503 zza 2013-12-13 end
			if(editRemark != null && editRemark.length() > 0 && editRemark.length() < 255){//如果手动填写了备注，就将备注填写日志表中
				expenseLog.setRemark(editRemark);
			}else{ // 如果没有手动输入备注，则从模板中读取备注。
				//积分日志模板
				Map<String , Object> expenseMap = new HashMap<String, Object>();
				expenseMap.put("expense", oldCredit.getExpense_value());
				expenseMap.put("new_expense", newCredit.getExpense_value());
				expenseMap.put("username", user.getUsername());
				logRemarkTemplate = Global.getLogTempValue(EnumLogTemplateType.CREDIT_LOG.getValue(), expenseLogType);
				expenseLog.setRemark(this.getLogRemark(expenseMap));
			}
			userCreditDao.addUserCreditLog(expenseLog);
		}
		Global.getTransfer().remove("newCredit");
		Global.getTransfer().remove("oldCredit");
		Global.getTransfer().remove("editRemark");
	}
	
	public String getLogRemark(Map<String , Object> map){
		try {
			return FreemarkerUtil.renderTemplate(logRemarkTemplate, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
