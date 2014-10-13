package com.p2psys.context;

import com.p2psys.model.accountlog.BaseAccountLog;
import com.p2psys.model.accountlog.noac.RegisterUserEmailLog;
import com.p2psys.tool.javamail.Mail;


/**
 * 常用变量类
 
 * @date 2012-7-10-上午10:26:20
 * @version  
 *
 *  (c)</b> 2012-51-<br/>
 *
 */
public class Constant {
	//标的类型
	public static int TYPE_ALL=100;
	//秒标
	public static int TYPE_SECOND=101;  
	//信用标
	public static int TYPE_CREDIT=102;
	/*
	 *给力标，抵押标
	 */
	public static int TYPE_MORTGAGE=103;
	//净标
	public static int TYPE_PROPERTY=104;
	//担保标
	public static int TYPE_VOUCH=105;
	/*
	 * 艺术品
	 */
	public static int TYPE_ART=106;
	/*
	 * 慈善标
	 */
	public static int TYPE_CHARITY=107;
	/*
	 * 预定标
	 */
	public static int TYPE_PREVIEW=108;
	
	/*
	 * 项目标
	 */
	public static int TYPE_PROJECT=109;
	/*
	 * 流转标
	 */
	public static int TYPE_FLOW=110;
	
	/*
	 * 学信标
	 */
	public static int TYPE_STUDENT=111;
	
	/*
	 * 担保标
	 */
	public static int TYPE_OFFVOUCH=112;
	//质押标
	public static int TYPE_PLEDGE=113;
	//爱心捐助
	public static int TYPE_DONATION=114;
	
	// v1.6.5.3 RDPROJECT-127 xx 2013.09.11 start
	//实业标，贷深圳
	public static int TYPE_INDUSTRY=115;
	//联名担保标，贷深圳
	public static int TYPE_JOINTGUARANTEE=116;
	// v1.6.5.3 RDPROJECT-127 xx 2013.09.11 end
	
	//借款标的全部状态
	public static int STATUS_ALL=-1;
	//借款标的发布状态
	public static int STATUS_NEW=0;
	//借款标的初审状态
	public static int STATUS_TRIAL=1;
	//借款标的复审状态
	public static int STATUS_REVIEW=3;
	//借款标的取消状态
	public static int STATUS_NOREVIEWS=4;
	//借款标的取消状态
	public static int STATUS_CANCEL=5;
	//借款标的完成放款状态
	public static int STATUS_BORROWSUCCESS=6;
	//借款标的部分还款状态
	public static int STATUS_PARTREPAIED=7;
	//借款标的全部还款状态
	public static int STATUS_ALLREPAIED=8;
	//借款标的逾期状态
	public static int STATUS_EXPIRED=9;
	
	public static int STATUS_REPAYING=10;
	
	public static int STATUS_INDEX=11;
	public static int STATUS_COMPLETE=12;
	
	public static int STATUS_ZR_INDEX=13;
	//美贷首页显示
	public static int STATUS_MD_INDEX=14;
	//及时雨首页正在投标的显示
	public static int STATUS_TENDER_INDEX=23;
	
	//及时雨首页显示
	public static int STATUS_JSY_INDEX=24;
	// 在线金融首页显示推荐标
	public static int STATUS_INDEX_ZA=25;
	//查找标的排序
	public static int ORDER_NONE=0;
	//按照金额的排序
	public static int ORDER_ACCOUNT_UP=1;
	//按照利率的排序
	public static int ORDER_APR_UP=2;
	//按照进度的排序
	public static int ORDER_JINDU_UP=3;
	//按照信用度的排序
	public static int ORDER_CREDIT_UP=4;
	//按照金额的排序
	public static int ORDER_ACCOUNT_DOWN=-1;
	//按照利率的排序
	public static int ORDER_APR_DOWN=-2;
	//按照进度的排序
	public static int ORDER_JINDU_DOWN=-3;
	//按照信用度的排序
	public static int ORDER_CREDIT_DOWN=-4;
	//首页项目排序
	public static int ORDER_INDEX=11;
	
	public static int ORDER_TENDER_ADDTIME_UP=21;
	
	public static int ORDRE_TENDER_ADDTIME_DOWN=21;
	
	public static int ORDER_BORROW_ADDTIME_UP=5;
	
	public static int ORDER_BORROW_ADDTIME_DOWN=-5;
	
	public static int ORDER_BORROW_TYPE_UP=6;
	
	public static int ORDER_BORROW_VERIFY_TIME_UP=7;
	
	public static int ORDER_BORROW_VERIFY_TIME_DOWN=-7;
	//按照标种类以及发标时间排序
	public static int ORDER_BORROW_KINDS=8;

	public static String SESSION_USER="session_user";
	public static String LOGINTIME="logintime";
	public static String AUTH_USER="auth_user";
	public static String AUTH_PURVIEW="auth_purview";
	
	public static int BORROW_KIND=19;

	//交易类型
	public static String TENDER="tender";
	
	public static String RECHARGE="recharge";
	public static String RECHARGE_APR="recharge_apr";
	public static String SYSTEM="system";
	public static String INVEST="invest";
	public static String RECHARGE_SUCCESS="recharge_success";
	public static String FREEZE="freeze";
	public static String UNFREEZE="unfreeze";
	public static String UNFREEZE_NO_PASS="unfreeze_no_pass";
	public static String BORROW_SUCCESS="borrow_success";
	public static String BORROW_FEE="borrow_fee";
	public static String VIP_BORROW_FEE="vip_borrow_fee";
	public static String MANAGE_FEE="manage_fee";
	public static String REPAID="repaid";
	public static String REPAID_CAPITAL="repaid_capital";
	public static String REPAID_INTEREST="repaid_interest";
	public static String PREREPAID="prerepaid";
	public static String VOUCH_REPAID="vouch_repaid";
	public static String VOUCH_WARD="vouch_ward";
	public static String BORROW_FAIL="borrow_fail";
	public static String VIP_FEE="vip_fee";
	public static String INVITE_MONEY="invite_money";
	public static String ACCOUNT_BACK="account_back";
	public static String CASH_FROST="cash_frost";
	public static String CASH_SUCCESS="cash_success";
	// V1.6.6.1 liukun 2013-09-13 end
	//public static String CASH_FAIL="CASH_fail";
	public static String CASH_FAIL="cash_fail";
	// V1.6.6.1 liukun 2013-09-13 end
	public static String CASH_CANCEL="cash_cancel";
	public static String WAIT_CAPITAL="wait_capital";
	public static String WAIT_INTEREST="wait_interest";
	public static String AWARD_ADD="award_add";
	public static String AWARD_DEDUCT="award_deduct";
	public static String HUIKUAN_AWARD="huikuan_award";
	public static String RECHARGE_FEE="recharge_fee";
	public static String CAPITAL_COLLECT="capital_collect";
	public static String INTEREST_COLLECT="interest_collect";
	public static String FEE="fee";
	public static String LATE_DEDUCT="late_deduct";
	public static String LATE_REPAYMENT="late_repayment";
	public static String LATE_REPAYMENT_INCOME="late_repayment_income";
	
	public static String OFFRECHARGE_AWARD="offrecharge_award";
	public static String TRANSACTION_FEE="transaction_fee";
	public static String BANK_SUCCESS="bank_success";
	public static String CASH_FEE="cash_fee";
	// 中奖金额返现
	public static String LOTTERY_AWARD="lottery_award";
	// 是否显示登录时间（0：不显示，1：显示）
	public static String LOGIN_TIME = "login_time";
	// 正常率
	public static String NORMAL_RATE = "normal_rate";
	// 逾期率
	public static String OVERDUE_RAGE = "overdue_rate";
	// 坏账率
	public static String BAD_RATE = "bad_rate";
	// V1.6.6.1 liukun 2013-10-15 start
	public static String ACCOUNT_BACK_FREEZE="account_back_freeze";
	public static String ACCOUNT_BACK_UNFREEZE="account_back_unfreeze";
	public static String ACCOUNT_RECHARGE_FEE="account_recharge_fee";
	// V1.6.6.1 liukun 2013-10-15 end
	// V1.6.6.1 RDPROJECT-341 liukun 2013-10-17 start
	public static String NEW_BORROW_FEE_FREEZE="new_borrow_fee_freeze";
	public static String NEW_BORROW_FEE_UNFREEZE="new_borrow_fee_unfreeze";
	// V1.6.6.1 RDPROJECT-341 liukun 2013-10-17 end
	
	//v1.6.6.2 RDPROJECT-369 liukun 2013-10-22 start
	public static String BORROW_REPAY_EXT_INTEREST="borrow_repay_ext_interest";
	public static String TENDER_REPAY_EXT_INTEREST="tender_repay_ext_interest";
	//v1.6.6.2 RDPROJECT-369 liukun 2013-10-22 start
	
	//v1.6.6.2 RDPROJECT-347 liukun 2013-10-22 start
	public static String BACK_MANAGE_FEE="back_manage_fee";
	public static String BACK_MANAGE_FEE_VIP="back_manage_fee_vip";
	//v1.6.6.2 RDPROJECT-347 liukun 2013-10-22 end
	//v1.6.6.2 zhangyz 2013-10-22 start
	public static String BACK_MANAGE_FEE_LEVEL = "back_manage_fee_level";
	//v1.6.6.2 zhangyz 2013-10-22 end
	//v1.6.6.2 RDPROJECT-282 fxx 2013-10-18 start
	public static String BACK_MANAGE_FEE_LMTA="back_manage_fee_lmta";
	//v1.6.6.2 RDPROJECT-282 fxx 2013-10-18 end
	
	//v1.6.6.2 RDPROJECT-401 liukun 2013-10-30 start
	public final static String AL_ONLINE_RECHARGE = "online_recharge";
	public final static String AL_OFF_RECHARGE = "off_recharge";
	public final static String AL_BATCH_RECHARGE = "batch_recharge";
	//v1.6.6.2 RDPROJECT-401 liukun 2013-10-30 end
	//消息处理类型
	public final static String DEL_RECEIVE_MSG="1";
	public final static String DEL_SENT_MSG="2";
	public final static String SET_UNREAD__MSG="3";
	public final static String SET_READ_MSG="4";
	
	public final static String DB_PREFIX="dw_";
	
	public final static long ADMIN_ID=1;
	
	public final static String OP_ADD="1";
	public final static String OP_REDUCE="2";
	public final static String OP_NONE="3";
	
	public static String ZRZB="zrzb";
	public static String MDW="mdw";
	public static String HUIZHOUDAI="huidai";
	
	//红包添加
	public static String HONGBAO_ADD="hongbao_add";
	//红包减少
	public static String HONGBAO_LESS="hongbao_less";
	//红包减少冻结
	public static String HONGBAO_LESS_FREEZE="hongbao_less_freeze";
	//红包减少失败
	public static String HONGBAO_LESS_FAIL="hongbao_less_fail";
	
	//邀请充值奖励
	public static String Invite_RECHARGE_AWARD="invite_recharge_award";
	//累计投资奖励
	public static String TENDER_AWARD="tender_award";
	
	//后台扣款申请
	public static String BACKSTAGE_BACK_APPLY="backstage_back_apply";
	//后台扣款审核成功
	public static String BACKSTAGE_BACK_SUCCESS="backstage_back_success";
	
	//后台扣款审核失败
	public static String BACKSTAGE_BACK_FAIL="backstage_back_fail";
	//后台线下充值申请
	public static String BACKSTAGE_RECHARGE_APPLY="backstage_recharge_apply";
	
	//后台线下充值审核成功
	public static String BACKSTAGE_RECHARGE_SUCCESS="backstage_recharge_success";
	
	//后台线下充值审核失败
	public static String BACKSTAGE_RECHARGE_FAIL="backstage_recharge_fail";
	//线上充值审核成功
	public static String ONLINE_RECHARGE_SUCCESS="online_recharge_success";
	
	//线上充值审核失败
	public static String ONLINE_RECHARGE_FAIL="online_recharge_fail";
	//线下充值审核成功
	public static String LINE_RECHARGE_SUCCESS="line_recharge_success";
	
	//线下充值审核失败
	public static String LINE_RECHARGE_FAIL="line_recharge_fail";
	
	//vip审核成功
	public static String VIP_SUCCESS="vip_success";
	//vip赠送成功
	public static String GIVE_VIP_SUCCESS="give_vip_success";
	//vip审核失败
	public static String VIP_FAIL="vip_fail";
	//vip冻结金额
	public static String VIP_FREEZE="vip_freeze";
	// v1.6.7.1 RDPROJECT-100 zza 2013-11-11 start
	// vip免年费续期
	public static String VIP_UNFREEZE = "vip_unfreeze";
	// v1.6.7.1 RDPROJECT-100 zza 2013-11-11 end
	//实名认证审核成功
	public static String REALNAME_SUCCESS="realname_success";
	//实名认证审核失败
	public static String REALNAME_FAIL="realname_fail";
	// v1.6.6.2 RDPROJECT-195 yl 2013-10-24 start
	//实名认证审核撤消
	public static String REALNAME_CANCEL="realname_cancel";
	// v1.6.6.2 RDPROJECT-195 yl 2013-10-24 end
	//手机认证审核成功
	public static String PHONE_SUCCESS="phone_success";
	//手机认证审核失败
	public static String PHONE_FAIL="phone_fail";
	// v1.6.6.2 RDPROJECT-195 yl 2013-10-24 start
	//手机认证审核撤消
	public static String PHONE_CANCEL="phone_cancel";
	//现场认证审核撤消
	public static String SCENE_CANCEL="scene_cancel";
	//视频认证审核撤消
	// v1.6.6.2 RDPROJECT-195 yl 2013-10-24 end
	public static String VIDEO_CANCEL="video_cancel";
	//发标初审成功
	public static String BORROW_FIRST_VERIFY_SUCCESS="borrow_first_verify_success";
	//发标初审失败
	public static String BORROW_FIRST_VERIFY_FAIL="borrow_first_verify_fail";
	//满标审核成功
	public static String BORROW_FULL_VERIFY_SUCCESS="borrow_full_verify_success";
	
	//满标审核失败
	public static String BORROW_FULL_VERIFY_FAIL="borrow_full_verify_fail";
	// 邀请奖励
	public static String INVITE_AWARD = "invite_award";
	
	//及时雨委托协议 
	public static String RECHARGE_PROTOCOL="recharge_protocol";
	public static String CASH_PROTOCOL="cash_protocol";
	public static String TENDER_PROTOCOL="tender_protocol";
	public static String REPAYMENT_ACCOUNT_PROTOCOL="repayment_account_protocol";
	public static String AWARD_PROTOCOL="award_protocol";
	
	//急难基金奖励类型
	public static String TROUBLE_AWARD="trouble_award";
	//急难基金捐款金额类型 
	public static String TROUBLE_DONATE="trouble_donate";
	//还款奖励
	public static String REPAYMENT_AWARD="repayment_award";
	public static String DEFALUTE_INVEST_BORROW="1,2,3,4,5,6";
	// 第一次投标奖励
	public static String FIRST_TENDER_AWARD = "first_tender_award";
	// v1.6.6.1 RDPROJECT-254 zza 2013-09-29 start
	// 民生创投邀请好友首次投标奖励
	public static String INVITE_FIRST_TENDER_AWARD = "invite_first_tender_award";
	// v1.6.6.1 RDPROJECT-254 zza 2013-09-29 end
	//短信类型
	
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 start
	/*public static String SMS_BANK_VERIFY_CODE="bank_verify_code";//TODO
	public static String SMS_CASH_VERIFY_CODE="cash_verify_code";//TODO
	public static String SMS_BORROW_VERIFY_SUCC="borrow_verify_succ";
	public static String SMS_BORROW_VERIFY_FAIL="borrow_verify_fail";
	public static String SMS_BORROW_FULL_SUCC="borrow_full_succ";
	public static String SMS_BORROW_FULL_FAIL="borrow_full_fail";
	public static String SMS_INVEST_SUCC="invest_succ";
	public static String SMS_INVEST_FAIL="invest_fail";
	public static String SMS_CASH_VERIFY_SUCC="cash_verify_succ";
	public static String SMS_CASH_VERIFY_FAIL="cash_verify_fail";
	public static String SMS_RECEIVE_REPAY="receive_repay";
	public static String SMS_RECEIVE_AWARD="receive_award";//TODO 
	public static String SMS_ATTESTATION_SUCC="attestation_succ";//TODO 
	public static String SMS_ATTESTATION_FAIL="attestation_fail";//TODO 
	public static String SMS_NEW_BORROW="new_borrow";
	public static String SMS_REPAY_SUCC="repay_succ";
	public static String SMS_RECHARGE_SUCC="recharge_succ";
	public static String SMS_DOWN_RECHARGE_VERIFY_SUCC="down_recharge_verify_succ";
	public static String SMS_DOWN_RECHARGE_VERIFY_FAIL="down_recharge_verify_fail";
	public static String SMS_PASSWORD_UPDATE="password_update";
	public static String SMS_PAYPWD_UPDATE="paypwd_update";
	public static String SMS_PHONE_CODE="phone_code";
	public static String SMS_BORROW_CANCEL="borrow_cancel";
	//V1.6.6.1 RDPROJECT-267 liukun 2013-10-09 start
	public static String SMS_FLOW_BUY_SUCC="flow_buy_succ";
	public static String SMS_FLOW_REPAY_SUCC="flow_repay_succ";
	//V1.6.6.1 RDPROJECT-267 liukun 2013-10-09 end
	// V1.6.6.1 RDPROJECT-149 liukun 2013-10-16 start
	public static String SMS_VIP_EXPIRED="vip_expired";
	// V1.6.6.1 RDPROJECT-149 liukun 2013-10-16 end
	
	// V1.6.6.1 RDPROJECT-305 liukun 2013-10-24 start
	public static String SMS_BATCH_RECHARGE_VERIFY_SUCC="batch_recharge_verify_succ";
	// V1.6.6.1 RDPROJECT-305 liukun 2013-10-24 end
*/	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-14 end
	
	public static byte SYSTEM_SMS=1;
	public static byte USER_SMS=2;
	public static byte SMS_SEND=1;
	public static byte SMS_NOT_SEND=0;
	public static String SMS_TEMPLET_PHSTR="{}";
	public static byte SMS_PAYFEE_MONTH = 1;
	public static byte SMS_PAYFEE_YEAR = 2;
	public static String SMS_FEE = "sms_fee";
	
	//普通用户对应的用户类型表里的id
	public static int NORMAL_CUSTOMER=2;
	
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-16 start
	/*public static String SMS_BORROWER_REPAY_NOTICE="borrower_repay_notice";
	public static String SMS_LOANER_REPAY_NOTICE="loaner_repay_notice";
	public static String SMS_LATE_REPAY_NOTICE="late_repay_notice";*/
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-16 end
	
	//资金合计
	public static String HUIKUAN_INTEREST="huikuan_interest";
	public static String HUIKUAN_CAPITAL="huikuan_capital";
	public static String DEDUCT_FREEZE="deduct_freeze";
	public static String BACK_HUIKUAN_INTEREST="back_huikuan_interest";
	
	//V1.6.5.3 RDPROJECT-142 liukun 2013-09-11 start
	public static int CASH_VERIFY_STEP_SUCC=1;
	public static int CASH_VERIFY_STEP_FAIL=0;
	public static int CASH_VERIFY_FINAL_STATUS_INIT=0;
	public static int CASH_VERIFY_FINAL_STATUS_SUCC=1;
	public static int CASH_VERIFY_FINAL_STATUS_FAIL=3;
	public static int CASH_VERIFY_FINAL_STATUS_CANCEL=4;
	//V1.6.5.3 RDPROJECT-142 liukun 2013-09-11 end
	
	// v1.6.6.2 RDPROJECT-235 zza 2013-10-18 start
	/**
	 * 及时雨推广奖励
	 */
	public static String PROMOTE_AWARD = "promote_award";
	// v1.6.6.2 RDPROJECT-235 zza 2013-10-18 start
	//v1.6.6.2 RDPROJECT-294 wcw 2013-10-12 start  
	//展期
	public static String EXTENSION_INTEREST="extension_interest";
	//v1.6.6.2 RDPROJECT-294 wcw 2013-10-12 end  
	/**
	 * 积分兑换
	 */
	public static String CONVERT_SUCCESS = "convert_success";
	//v1.6.6.2 RDPROJECT-360 liukun 2013-10-25 start
	public final static String CA_BACK = "ca_back";
	//v1.6.6.2 RDPROJECT-360 liukun 2013-10-25 end
	
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-12 start
	public final static byte NOTICE_SMS = 1;
	public final static byte NOTICE_EMAIL = 2;
	public final static byte NOTICE_MESSAGE = 3;
	
	public static String NOTICE_BANK_VERIFY_CODE="bank_verify_code";//TODO
	public static String NOTICE_CASH_VERIFY_CODE="cash_verify_code";//TODO
	public static String NOTICE_BORROW_VERIFY_SUCC="borrow_verify_succ";
	public static String NOTICE_BORROW_VERIFY_FAIL="borrow_verify_fail";
	public static String NOTICE_BORROW_FULL_SUCC="borrow_full_succ";
	public static String NOTICE_BORROW_FULL_FAIL="borrow_full_fail";
	public static String NOTICE_INVEST_SUCC="invest_succ";
	public static String NOTICE_INVEST_FAIL="invest_fail";
	public static String NOTICE_CASH_VERIFY_SUCC="cash_verify_succ";
	public static String NOTICE_CASH_VERIFY_FAIL="cash_verify_fail";
	public static String NOTICE_RECEIVE_REPAY="receive_repay";
	public static String NOTICE_NEW_BORROW="new_borrow";
	public static String NOTICE_REPAY_SUCC="repay_succ";
	public static String NOTICE_RECHARGE_SUCC="recharge_succ";
	public static String NOTICE_DOWN_RECHARGE_VERIFY_SUCC="down_recharge_verify_succ";
	public static String NOTICE_DOWN_RECHARGE_VERIFY_FAIL="down_recharge_verify_fail";
	public static String NOTICE_PASSWORD_UPDATE="password_update";
	public static String NOTICE_PAYPWD_UPDATE="paypwd_update";
	public static String NOTICE_PHONE_CODE="phone_code";
	public static String NOTICE_BORROW_CANCEL="borrow_cancel";
	public static String NOTICE_FLOW_BUY_SUCC="flow_buy_succ";
	public static String NOTICE_FLOW_REPAY_SUCC="flow_repay_succ";
	public static String NOTICE_VIP_EXPIRED="vip_expired";
	public static String NOTICE_BATCH_RECHARGE_VERIFY_SUCC="batch_recharge_verify_succ";
	
	public static String NOTICE_RECEIVE_TENDER_AWARD="receive_tender_award";
	public static String NOTICE_DEDUCT_BORROWER_AWARD="deduct_borrower_award";
	public static String NOTICE_HOUTAI_DEDUCT_SUCC="houtai_deduct_succ";
	public static String NOTICE_RECEIVE_HUIKUAN_AWARD="receive_huikuan_award";
	public static String NOTICE_BORROWER_REPAY_NOTICE="borrower_repay_notice";
	public static String NOTICE_LOANER_REPAY_NOTICE="loaner_repay_notice";
	
	public static String NOTICE_VIP_BIRTH="vip_birth";
	public static String NOTICE_FLOW_RESTART="flow_restart";
	public static String NOTICE_CERTIFY_SUCC="certify_succ";
	public static String NOTICE_CERTIFY_FAIL="certify_fail";
	public static String NOTICE_CERTIFY_CANCEL="certify_cancel";
	public static String NOTICE_REACTIVE_USER="reactive_user";
	public static String NOTICE_GET_PAYPWD="get_paypwd";
	public static String NOTICE_GET_PWD="get_pwd";
	//v1.6.7.2 RDPROJECT-533 lx 2013-12-24 start
	public static String NOTICE_REGISTER_USER="register_user";
	//v1.6.7.2 RDPROJECT-533 lx 2013-12-24 end
	
	public final static String NOTICE_AUTO_TENDER="auto_tender";
	
	
	public static byte SYSTEM_NOTICE=1;
	public static byte USER_NOTICE=2;
	public static byte NOTICE_SEND=1;
	public static byte NOTICE_NOT_SEND=0;
	public static byte NOTICE_RECEIVE=1;
	public static byte NOTICE_NOT_RECEIVE=0;
	
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-12 end
	//v1.6.7.1 wcw 2013-11-25 start
	//视频认证审核成功
	public static String VIDEO_SUCCESS="video_success";
	//视频认证审核失败
	public static String VIDEO_FAIL="video_fail";

	//现场认证审核成功
	public static String SCENE_SUCCESS="scene_success";
	//现场认证审核失败
	public static String SCENE_FAIL="scene_fail";
	//v1.6.7.1 wcw 2013-11-25 end

	//理财宝解冻资金
	public static String TREASURE_UNFREEZE="treasure_unfreeze";
	//理财宝冻结资金
	public static String TREASURE_FREEZE="treasure_freeze";
	//理财宝利息管理费
	public static String TREASURE_MANAGE_FEE="treasure_manage_fee";
	//理财宝收回利息
	public static String TREASURE_INTEREST="treasure_interest";
	//理财宝还款扣除本息
	public static String TREASURE_REPAID="treasure_repaid";
	//理财宝借款入账
	public static String TREASURE_SUCCESS="treasure_success";
	
	//v1.6.7.2 RDPROJECT-86 liukun 2013-12-17 start
	//LT:log template
	//CA:creditAssignment
	public final static String LT_CA_BUY_FREEZE="ca_buy_freeze";
	public final static String LT_CA_BUY_UNFREEZE="ca_buy_unfreeze";
	public final static String LT_CA_BUY_SUCC="ca_buy_succ";
	public final static String LT_CA_SELL_SUCC="ca_sell_succ";
	public final static String LT_CA_BUY_FEE_DEDUCT="ca_buy_fee_deduct";
	public final static String LT_CA_BUY_FEE_FREEZE="ca_buy_fee_freeze";
	public final static String LT_CA_BUY_FEE_BACK="ca_buy_fee_back";
	public final static String LT_CA_SELL_FEE_DEDUCT="ca_sell_fee_deduct";
	
	/** 债权发布待审核 */
	public final static byte CA_STATUS_INIT = 0;
	/** 债权初审通过 */
	public final static byte CA_STATUS_VERIFY_SUCC = 1;
	/** 债权初审未通过 */
	public final static byte CA_STATUS_VERIFY_FAIL = 2;
	/** 债权复审通过 */
	public final static byte CA_STATUS_FULL_SUCC = 3;
	/** 债权复审未通过 */
	public final static byte CA_STATUS_FULL_FAIL = 4;
	/** 债权撤回 */
	public final static byte CA_STATUS_CANCEL = 5;
	
	/** 债权类型：1标级别转让*/
	public final static byte CA_TYPE_BORROW = 1;
	/** 债权类型：2tender级别转让  */
	public final static byte CA_TYPE_TENDER = 2;
	/** 债权类型: 3collection级别转出 */
	public final static byte CA_TYPE_COLLECTION = 3;
	
	//CATS:creditassignment trade serial
	public final static byte CATS_STATUS_INIT = 0;
	public final static byte CATS_STATUS_SUCC = 1;
	public final static byte CATS_STATUS_CANCEL = 2;
	
	public final static int  COLLECTION_STATUS_NORMAL = 0;//正常待收状态
	public final static int  COLLECTION_STATUS_PAYED = 1;//已还款
	public final static int  COLLECTION_STATUS_CA = 2;//已经转让
	
	public final static int REPAY_AWARD_STATUS_NORAML = 0;//还款奖励等待
	public final static int REPAY_AWARD_STATUS_PAYED = 1;//还款奖励已收
	
	public final static byte CA_BUY_FEE_STATUS_FREEZE = 0;//冻结中
	public final static byte CA_BUY_FEE_STATUS_PAYED = 1;//已收取
	public final static byte CA_BUY_FEE_STATUS_BACK = 2;//已退回
	public final static byte CA_SELL_FEE_STATUS_UNPAYED = 0;//
	public final static byte CA_SELL_FEE_STATUS_PAYED = 1;//
	//v1.6.7.2 RDPROJECT-86 liukun 2013-12-17 end
	
}
