package com.p2psys.web.action.member;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.domain.NoticeType;
import com.p2psys.domain.User;
import com.p2psys.domain.UserNoticeConfig;
import com.p2psys.model.UserCacheModel;
import com.p2psys.model.UserNoticeConfigModel;
import com.p2psys.service.AccountService;
import com.p2psys.service.NoticeTypeService;
import com.p2psys.service.UserNoticeConfigService;
import com.p2psys.service.UserService;
import com.p2psys.tool.coder.MD5;
import com.p2psys.util.DateUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

public class MemberNoticeConfigAction extends BaseAction {

	private static Logger logger = Logger
			.getLogger(MemberNoticeConfigAction.class);

	private UserService userService;
	private AccountService accountService;
	
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 start
	private NoticeTypeService noticeTypeService;
	
	private UserNoticeConfigService userNoticeConfigService;

	public NoticeTypeService getNoticeTypeService() {
		return noticeTypeService;
	}

	public void setNoticeTypeService(NoticeTypeService noticeTypeService) {
		this.noticeTypeService = noticeTypeService;
	}

	public UserNoticeConfigService getUserNoticeConfigService() {
		return userNoticeConfigService;
	}

	public void setUserNoticeConfigService(UserNoticeConfigService userNoticeConfigService) {
		this.userNoticeConfigService = userNoticeConfigService;
	}

	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 end
	
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public AccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}



	/**
	 * 扣取用户短信费用
	 * 
	 * @return
	 * @throws Exception
	 */
	public String smsfee() {
		String paysmsfeeType = request.getParameter("actionType");
		request.setAttribute("query_type", "smsfee");
		User user = (User) session.get(Constant.SESSION_USER);
		long userid = user.getUser_id();
		if (!StringUtils.isNull(paysmsfeeType).equals("")) {
			//v1.6.6.1 RDPROJECT-302 liukun 2013-10-16 begin
			String tokenMsg=checkToken("smsfee_token");
			if(!StringUtils.isBlank(tokenMsg)){
				message(tokenMsg);
				return MSG;
			}
			//检验支付密码
			String userpaypwd=StringUtils.isNull(user.getPaypassword());
			String paypwd = request.getParameter("paypassword");
			MD5 md5 = new MD5();
			if(!md5.getMD5ofStr(paypwd).equalsIgnoreCase(userpaypwd)){
				message("支付密码不正确！");
				return MSG;
			}
			//v1.6.6.1 RDPROJECT-302 liukun 2013-10-16 end
			// 根据期限获取短信费用
			int paymodel = Integer.parseInt(request.getParameter("paymodel"));
			if (paymodel == Constant.SMS_PAYFEE_MONTH
					|| paymodel == Constant.SMS_PAYFEE_YEAR) {

				Map resultHm = accountService.paySmsFee(userid, paymodel);

				if (Integer.parseInt(resultHm.get("result").toString()) == 0) {
					request.setAttribute("errormsg", resultHm.get("msg")
							.toString());
				} else {
					request.setAttribute("msg", resultHm.get("msg").toString());
				}
			}
		}
		//v1.6.6.1 RDPROJECT-302 liukun 2013-10-16 begin
		//因为扣费和显示都共用了一个页面，所以总是获取token值
		saveToken("smsfee_token");
		//v1.6.6.1 RDPROJECT-302 liukun 2013-10-16 end
		// 因为用户的配置全部
		UserCacheModel usercache = userService.getUserCacheByUserid(userid);
		String userSmspayEndtime = StringUtils.isNull(usercache
				.getSmspay_endtime());
		if (!userSmspayEndtime.equals("")) {
			String endtime = DateUtils.dateStr2(DateUtils
					.getDate(userSmspayEndtime));
			request.setAttribute("payendtime", endtime);
		}
		request.setAttribute("smsmonthfee", Global.getString("sms_month_fee"));
		request.setAttribute("smsyearfee", Global.getString("sms_year_fee"));
		return SUCCESS;
	}

	/**
	 * 设置用户
	 * 
	 * @return
	 * @throws Exception
	 */
	public String typeconf() {
		String actionType = StringUtils.isNull(request
				.getParameter("actionType"));
		request.setAttribute("query_type", "typeconf");
		User user = (User) session.get(Constant.SESSION_USER);
		long userid = user.getUser_id();

		// 获取用户设置为接收的所有类型
		List allSysNoticeTypes = noticeTypeService.getList();
		//将列形式的通知类型配置转换成行形式的
		String noticeTypeNid = "";
		UserNoticeConfigModel unc = null;
		ArrayList<UserNoticeConfigModel> uncList = new ArrayList<UserNoticeConfigModel>();
		for (Object object : allSysNoticeTypes) {
			NoticeType noticeType = (NoticeType)object;
			if (!noticeTypeNid.equals(noticeType.getNid())){
				unc = new UserNoticeConfigModel();
				uncList.add(unc);
				noticeTypeNid = noticeType.getNid();
			}
			
			unc.setNid(noticeType.getNid());
			unc.setNidName(noticeType.getName());
			if (Constant.NOTICE_SMS==noticeType.getNotice_type()){
				unc.setSmsSystem(noticeType.getType());
				unc.setSmsSysSend(noticeType.getSend());
			}
			if (Constant.NOTICE_EMAIL==noticeType.getNotice_type()){
				unc.setEmailSystem(noticeType.getType());
				unc.setEmailSysSend(noticeType.getSend());
			}
			if (Constant.NOTICE_MESSAGE==noticeType.getNotice_type()){
				unc.setMessageSystem(noticeType.getType());
				unc.setMessageSysSend(noticeType.getSend());
			}
		}
		

		if (!StringUtils.isBlank(actionType)) {

			for (Object object : uncList) {
				UserNoticeConfigModel uncsys = (UserNoticeConfigModel)object;
				
				if(request.getParameter(uncsys.getNid()+"_sms")!=null){
					uncsys.setSmsUserReceive(1);
				}
				if(request.getParameter(uncsys.getNid()+"_email")!=null){
					uncsys.setEmailUserReceive(1);
				}
				if(request.getParameter(uncsys.getNid()+"_message")!=null){
					uncsys.setMessageUserReceive(1);
				}
			}
			ArrayList<UserNoticeConfig> uncNewList = new ArrayList<UserNoticeConfig>();
			for (Object object : uncList) {
				UserNoticeConfigModel uncsys = (UserNoticeConfigModel)object;
				
				UserNoticeConfig uncNew = new UserNoticeConfig();
				uncNew.setUser_id(userid);
				uncNew.setNid(uncsys.getNid());
				uncNew.setSms(uncsys.getSmsUserReceive());
				uncNew.setEmail(uncsys.getEmailUserReceive());
				uncNew.setMessage(uncsys.getMessageUserReceive());
				uncNewList.add(uncNew);
			}
			
			userNoticeConfigService.updateUNConfigs(uncNewList, userid);
			
			/*String[] NoticeTypes = request.getParameterValues("NoticeType");
			if (null != NoticeTypes) {
				for (int i = 0; i < NoticeTypes.length; i++) {
					for (Iterator iter = allSysNoticeTypes.iterator(); iter
							.hasNext();) {
						NoticeType NoticeType = (NoticeType) (iter.next());

						if (NoticeTypes[i].equalsIgnoreCase(NoticeType.getNid())) {
							NoticeType.setSend(Constant.SMS_SEND);
						}
					}
				}
			}
			// 将用户拒绝接收的类型转换成JSON存入usercache
			Map<String, Byte> NoticeTypeMap = new HashMap<String, Byte>();
			for (Iterator iter = allSysSendNoticeTypes.iterator(); iter.hasNext();) {
				NoticeType NoticeType = (NoticeType) (iter.next());
				if (NoticeType.getSend() == Constant.SMS_NOT_SEND) {
					NoticeTypeMap.put(NoticeType.getNid(), Constant.SMS_NOT_SEND);
				}

			}
			JSONObject jsonObj = new JSONObject(NoticeTypeMap);
			String NoticeTypeConfig = jsonObj.toString();

			userService.updateNoticeTypeConfig(userid, NoticeTypeConfig);*/

			String msg = "接收通知类型配置成功！";
			request.setAttribute("msg", msg);
		} else {
			/*//获取用户的全部配置
			List userNoticeConfigList = userNoticeConfigService.getAllUNConfigs(userid); 
			// 因为用户的配置全部
			UserCacheModel usercache = userService.getUserCacheByUserid(userid);
			String userNoticeTypeConfig = usercache.getNoticeType_config();

			// 合并
			try {
				JSONObject jsonObject = new JSONObject(userNoticeTypeConfig);

				for (Iterator iter = allSysSendNoticeTypes.iterator(); iter
						.hasNext();) {
					NoticeType NoticeType = (NoticeType) (iter.next());
					try {
						byte smssend = Byte.parseByte(String.valueOf(jsonObject
								.getInt(NoticeType.getNid())));
						if (smssend == Constant.SMS_NOT_SEND) {
							NoticeType.setSend(Constant.SMS_NOT_SEND);
						}
					} catch (Exception e) {
					}
				}
			} catch (Exception e) {
				// nothing need do
			}*/
			List currUncList = userNoticeConfigService.getAllUNConfigs(userid);
			for (Object object : uncList) {
				UserNoticeConfigModel uncsys = (UserNoticeConfigModel)object;
				for (Object obj : currUncList) {
					UserNoticeConfig uncuser = (UserNoticeConfig)obj;
					if (uncsys.getNid().equals(uncuser.getNid())){
						uncsys.setSmsUserReceive(uncuser.getSms());
						uncsys.setEmailUserReceive(uncuser.getEmail());
						uncsys.setMessageUserReceive(uncuser.getMessage());
					}
				}
			}
		}

		request.setAttribute("NoticeTypeList", uncList);
		return SUCCESS;
	} 

}
