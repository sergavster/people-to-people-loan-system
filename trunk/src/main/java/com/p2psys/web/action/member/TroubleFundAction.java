package com.p2psys.web.action.member;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ModelDriven;
import com.p2psys.common.enums.EnumAwardType;
import com.p2psys.common.enums.EnumTroubleFund;
import com.p2psys.context.Global;
import com.p2psys.domain.TroubleAwardRecord;
import com.p2psys.domain.TroubleDonateRecord;
import com.p2psys.domain.TroubleFundDonateRecord;
import com.p2psys.domain.User;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.model.award.AwardResult;
import com.p2psys.service.AwardService;
import com.p2psys.service.TroubleFundService;
import com.p2psys.service.UserService;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;
/**
 * 急难基金action
 * TODO 类说明
 * 
 
 * @version 1.0
 * @since 2013-7-26
 */
public class TroubleFundAction extends BaseAction implements ModelDriven<TroubleFundDonateRecord>{

	private final static Logger logger = Logger.getLogger(TroubleFundAction.class);
	/**用户service */
    private UserService userService;
    /**急难基金service */
    private TroubleFundService troubleFundService;
    /**奖励service */
    private AwardService awardService;
    /**急难基金捐赠对象 */
    private TroubleFundDonateRecord t=new TroubleFundDonateRecord();
    /**验证码*/
    private String valicode;
    
    public AwardService getAwardService() {
		return awardService;
	}
	public void setAwardService(AwardService awardService) {
		this.awardService = awardService;
	}
	public String getValicode() {
		return valicode;
	}
	public void setValicode(String valicode) {
		this.valicode = valicode;
	}
	/**
	 * 急难基金捐赠记录添加
	 * @return  结果集
	 */
	public String addTroubleFund(){
    	
    	String actionType=paramString("type");
    	//获取捐赠基金比例
    	double trouble_apr=getTroubleApr();
    	//获取业主添加奖金池金额
    	double trouble_admin_award=getTroubleAdminAward();
    	double troubleAwardExtra=troubleFundService.getTroubleSum(EnumTroubleFund.FIRST.getValue());
    	if(StringUtils.isBlank(actionType)){
    		 //奖金池结余
    	    request.setAttribute("troubleAwardExtra", troubleAwardExtra+trouble_admin_award);
    		return "success";
    	}
    	String paypassword=paramString("paypassword");
    	User user=getSessionUser();
    	t.setUser_id(user.getUser_id());
    	//验证码校验
    	if(!checkValidImg(StringUtils.isNull(valicode))){
			message("验证码不正确！","");
			return MSG;
		}
    	//抽奖规则id
    	long ruleId=awardService.getRuleIdByAwardType(EnumAwardType.AWARD_TYPE_RATIO.getValue());
    	//奖励金额不超过奖金池的最大百分比
    	double award_apr=getTroubleAwardApr();
    	//业主设置奖金池押金
    	double troubleAdminAward=getTroubleAdminAward();
    	//修改最大奖励额度
    	awardService.updateTotalMoney(ruleId, (troubleAwardExtra+troubleAdminAward)*award_apr);
    	//抽奖核心方法  
    	AwardResult awardResult=awardService.award(ruleId, user, t.getMoney());
    	logger.info("awardType=============="+awardResult.getError());
    	logger.info("Level_no=============="+awardResult.getLevel_no());
    	logger.info("money=============="+t.getMoney());
    	//获取奖励值
    	double award_money=NumberUtils.getDouble(awardResult.getMoney());
    	logger.info("award_money=============="+award_money);
    	t.setAward_money(award_money);
    	//添加捐款记录 并捐款金额分配 且更新账户资金
    	TroubleFundDonateRecord troubleFundDonateRecord=troubleFundService.troubleFund(t,paypassword,trouble_apr);
		//添加奖励支出（奖金池奖励支出，会员用户添加奖励）
		TroubleAwardRecord troubleAwardRecord=troubleFundService.troubleAward(troubleFundDonateRecord, trouble_apr, EnumTroubleFund.FIRST.getValue(), award_money);
    	//若为无偿捐赠 显示无偿功德榜列表
		if(troubleFundDonateRecord.getGiving_way()==EnumTroubleFund.FIRST.getValue()){
			int page=paramInt("page2");
    		PageDataList plist=troubleFundService.getTroubleFundList(t,trouble_apr,page);
    		request.setAttribute("page", plist.getPage());
        	request.setAttribute("list", plist.getList());
        	request.setAttribute("param", new SearchParam().toMap());
	    	return "success";
    	}
		//若为有偿捐赠 显示本次活动捐赠获奖情况
    	if(troubleFundDonateRecord.getGiving_way()==EnumTroubleFund.ZERO.getValue()){
    		//捐赠记录对象
    		request.setAttribute("troubleFund", troubleFundDonateRecord);
    		//TroubleAwardRecord award=troubleFundService.troubleAward(troubleFundDonateRecord,trouble_apr);
    		//转入基金(捐赠到的钱一部分分给急难基金)
    	    TroubleDonateRecord donate=troubleFundService.troubleDonate(troubleFundDonateRecord, trouble_apr);
    	    //奖金池结余
    	     troubleAwardExtra=troubleFundService.getTroubleSum(EnumTroubleFund.FIRST.getValue());
    	    //基金结余
    	    double troubleDonateExtra=troubleFundService.getTroubleSum(EnumTroubleFund.ZERO.getValue());
    	    request.setAttribute("troubleAwardExtra", troubleAwardExtra+trouble_admin_award);
    	    request.setAttribute("troubleDonateExtra", troubleDonateExtra);
    	    request.setAttribute("donate", donate);
    	    
    	    return "success";
    	}
    	return "success";
    }
	/**
	 * 捐赠金额 分配给基金最大百分比  后台dw_system表中设置
	 * @return 捐赠金额 分配给基金最大百分比
	 */
    private double getTroubleApr(){
    	String troubleAprString=StringUtils.isNull(Global.getValue("trouble_apr"));
		troubleAprString=troubleAprString==""?"0.5":troubleAprString;
		double trouble_apr=NumberUtils.getDouble(troubleAprString);
		return trouble_apr;
    }
    /**
     * 抽奖获奖总额占奖金池的最大百分比  后台dw_system表中设置
     * @return 抽奖获奖总额占奖金池的最大百分比
     */
    private double getTroubleAwardApr(){
    	String awardAprString=StringUtils.isNull(Global.getValue("award_apr"));
    	awardAprString=awardAprString==""?"0.8":awardAprString;
		double award_apr=NumberUtils.getDouble(awardAprString);
		return award_apr;
    }
    /**
     * 业主添加奖金池金额设置  后台dw_system表中设置
     * @return 业主添加奖金池金额
     */
    private double getTroubleAdminAward(){
    	String troubleAdminAwardString=StringUtils.isNull(Global.getValue("trouble_admin_add_money"));
    	troubleAdminAwardString=troubleAdminAwardString==""?"200000":troubleAdminAwardString;
		double trouble_admin_award=NumberUtils.getDouble(troubleAdminAwardString);
		return trouble_admin_award;
    }
    /**
     * 无偿、有偿、捐赠列表  以及基金无息借款明细列表
     * @return  success结果
     */
    public String troubleFund(){
    	double trouble_apr=getTroubleApr();
		int page=paramInt("page");
    	String giving_way=paramString("giving_way");
    	String donateOutStatus=paramString("donateOutStatus");
    	request.setAttribute("donateOutStatus", donateOutStatus);
    	request.setAttribute("givingWay", giving_way);
    	String zero=String.valueOf(EnumTroubleFund.ZERO.getValue());
    	if(zero.equals(giving_way)){
          		 //有偿功德榜
          	    t.setGiving_way(EnumTroubleFund.ZERO.getValue());
          	    PageDataList plist=troubleFundService.getTroubleFundList(t,trouble_apr,page);
          	    request.setAttribute("page3", plist.getPage());
        	    request.setAttribute("list", plist.getList());
            	request.setAttribute("param3", new SearchParam().toMap());
      	    	return "success";
    	}
    	String first=String.valueOf(EnumTroubleFund.FIRST.getValue());
    	if(first.equals(giving_way)){
    		 //无偿功德榜
    		PageDataList plist=troubleFundService.getTroubleFundList(t,trouble_apr,page);
    		request.setAttribute("page2", plist.getPage());
        	request.setAttribute("list", plist.getList());
        	request.setAttribute("param2", new SearchParam().toMap());
	    	return "success";
    	}
    	//基金无息借款明细列表
    	if(first.equals(donateOutStatus)){
    		PageDataList plist=troubleFundService.getTroubleDonateList(EnumTroubleFund.FIRST.getValue(),page);
    		request.setAttribute("page", plist.getPage());
        	request.setAttribute("donateOutlist", plist.getList());
        	request.setAttribute("param", new SearchParam().toMap());
    		return "success";
    	}
    	return "success";
    }
	public UserService getUserService() {
		return userService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public TroubleFundService getTroubleFundService() {
		return troubleFundService;
	}
	public void setTroubleFundService(TroubleFundService troubleFundService) {
		this.troubleFundService = troubleFundService;
	}
	@Override
	public TroubleFundDonateRecord getModel() {
		return t;
	}
}
