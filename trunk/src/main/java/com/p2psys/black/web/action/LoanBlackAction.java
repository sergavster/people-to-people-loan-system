package com.p2psys.black.web.action;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;





import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.p2psys.black.domain.LoanBlack;
import com.p2psys.black.service.LoanBlackService;
import com.p2psys.model.PageDataList;
import com.p2psys.model.RewardRecordModel;
import com.p2psys.model.SearchParam;
import com.p2psys.tool.jxl.ExcelHelper;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;
/**
 * 
 
 *
 */
@Controller("loanBlackAction")
public class LoanBlackAction extends BaseAction{
	@Autowired
	private LoanBlackService loanBlackService;
	/**
	 * 输出日志 
	 */
	private final static Logger logger = Logger.getLogger(LoanBlackAction.class);
	/**
	 * 获取符合条件黑名单list
	 * @return String
	 * @throws Exception Exception
	 */
	public String showLoanBlackList() {
		String choiceType = paramString("choiceType");
		int page = paramInt("page");
		String dotime1 = paramString("dotime1");
		String dotime2 = paramString("dotime2");
		String username = paramString("username");
		SearchParam param = new SearchParam();
		param.setDotime1(dotime1);
		param.setDotime2(dotime2);
		param.setUsername(username);
		if (StringUtils.isBlank(choiceType)) {
			PageDataList plist = loanBlackService.getLoanBlackList(param, page);
			setPageAttribute(plist, param);
			return SUCCESS;
		} else {
			try {
				String contextPath = ServletActionContext.getServletContext().getRealPath("/");
				String downloadFile = "loanblack_" + System.currentTimeMillis() + ".xls";
				String infile = contextPath + "/data/export/" + downloadFile;
				String[] names = new String[]{"id","type","fk_id","username", "passive_username", "reward_account", "addtime"};
				String[] titles = new String[]{"ID", "类型", "外键ID", "获得者", "奖励提供者", "奖励金额", "奖励时间"};
				List<LoanBlack> list = loanBlackService.getLoanBlackList(param);
				ExcelHelper.writeExcel(infile, list, RewardRecordModel.class,Arrays.asList(names), Arrays.asList(titles));
				this.export(infile, downloadFile);
				return null;
			} catch (Exception e) {
				logger.error(e);
			}
		}
		return null;
	}
	
	
	
	/**
	 * 跳转到黑名单查询页面
	 * @return
	 */
	public String loanblack() {
		return SUCCESS;
	}

	/**
	 * 黑名单查询
	 * @return
	 */
	public String getloanblack() {
		String username=paramString("username");
		String identity=paramString("identity");
		String mobile=paramString("mobile");
		String email=paramString("email");
		String qq=paramString("qq");
		if(!StringUtils.isBlank(username)||!StringUtils.isBlank(identity)||!StringUtils.isBlank(mobile)||!StringUtils.isBlank(email)||!StringUtils.isBlank(qq)){
			List<LoanBlack> loanBlackList=loanBlackService.getLoanBlack(username,identity,mobile,email,qq);
			if(loanBlackList.size()>0){
				request.setAttribute("list", loanBlackList);
				return SUCCESS;
			}else{
				/**
				 * 调用融360再查询一遍是否存在该黑名单信息，如果存在把融360的查询结果保存到本地服务器，如果不存在就提示
				 */
				message("该用户不在黑名单中");
				return ADMINMSG;
			}
		}else{
			message("请填写至少一个查询条件");
			return ADMINMSG;
		}
	}
	
	/**
	 * 设置为黑名单
	 * @return
	 */
	public String addloanblack() {
		String username=paramString("username");
		loanBlackService.addLoanBlack(username);
		message("设置成功");
		return ADMINMSG;
	}
	
	
}
