package com.p2psys.fund.web.action.admin;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ModelDriven;
import com.p2psys.fund.model.FundTenderModel;
import com.p2psys.fund.service.FundTenderService;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.tool.jxl.ExcelHelper;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

/**
 * 基金信托认购信息管理
 
 * @version 1.0
 * @since 2013-10-27
 */
public class ManageFundTenderAction extends BaseAction implements ModelDriven<FundTenderModel> {
	private static final Logger logger=Logger.getLogger(ManageFundTenderAction.class);
	
	@Resource
	private FundTenderService fundTenderService;
	
	FundTenderModel model = new FundTenderModel();
	@Override
	public FundTenderModel getModel() {
		return model;
	}
	
	/**
	 * 列表
	 * @return
	 * @throws Exception 
	 */
	public String index(){
		String keywords = paramString("keywords");
		String username = paramString("username");
		int type = paramInt("type");
		int status = paramInt("status");
		int page = paramInt("page");
		SearchParam param=new SearchParam();
		param.setKeywords(keywords);
		param.setUsername(username);
		param.setType(type+"");
		param.setStatus(status+"");
		setMsgUrl("/admin/fundTender/index.html");
		if("export".equals(actionType)){//导出报表
			String contextPath = ServletActionContext.getServletContext().getRealPath("/");
			String downloadFile="fund_tender_"+System.currentTimeMillis()+".xls";
			String infile=contextPath+"/data/export/"+downloadFile;
			String[] names=new String[]{"id","name","type","username","account","status","phone","remark"};
			String[] titles=new String[]{"ID","标题","类型(1:基金,2:信托)","认购人","借款金额(万)","状态(0:未电联,1:已电联)","手机号","备注"};
			List<FundTenderModel> allFundTenderList = fundTenderService.list(0,0,param);
			try {
				ExcelHelper.writeExcel(infile,allFundTenderList, FundTenderModel.class, Arrays.asList(names), Arrays.asList(titles));
				this.export(infile, downloadFile);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("基金信托认购信息导出报表异常："+e.getMessage());
			}
			return null;
		}else{
			PageDataList plist=fundTenderService.page(page, param);
			setPageAttribute(plist, param);
			return "index";
		}
	}
	
	/**
	 * 改
	 * @return
	 */
	public String modify(){
		if(!StringUtils.isBlank(actionType)){
			int r = fundTenderService.modify(model);
			if(r==1){
				message("修改基金信托认购信息成功。","/admin/fundTender/index.html?status=99");
			}else{
				message("修改基金信托认购信息失败!","/admin/fundTender/index.html?status=99");
			}
			return ADMINMSG;
		}else{
			long id = paramLong("id");
			model = fundTenderService.get(id);
		}
		return "modify";
	}
	
}
