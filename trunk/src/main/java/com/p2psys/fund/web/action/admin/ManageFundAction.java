package com.p2psys.fund.web.action.admin;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ModelDriven;
import com.p2psys.context.Constant;
import com.p2psys.domain.User;
import com.p2psys.fund.model.FundModel;
import com.p2psys.fund.service.FundService;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.tool.jxl.ExcelHelper;
import com.p2psys.util.DateUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

/**
 * 基金信托管理
 
 * @version 1.0
 * @since 2013-10-22
 */
public class ManageFundAction extends BaseAction implements ModelDriven<FundModel>{
	private static final Logger logger=Logger.getLogger(ManageFundAction.class);
	
	@Resource
	private FundService fundService;
	
	FundModel model = new FundModel();
	@Override
	public FundModel getModel() {
		return model;
	}
	
	private File files;
	private String filesFileName;
	
	/**
	 * 列表
	 * @return
	 * @throws Exception 
	 */
	public String index(){
		String keywords = paramString("keywords");
		String dotime1 = paramString("dotime1");
		String dotime2 = paramString("dotime2");
		int type = paramInt("type");
		int page = paramInt("page");
		SearchParam param=new SearchParam();
		param.setKeywords(keywords);
		param.setDotime1(dotime1);
		param.setDotime2(dotime2);
		param.setType(type+"");
		setMsgUrl("/admin/fund/index.html");
		if("export".equals(actionType)){//导出报表
			String contextPath = ServletActionContext.getServletContext().getRealPath("/");
			String downloadFile="fund_"+System.currentTimeMillis()+".xls";
			String infile=contextPath+"/data/export/"+downloadFile;
			String[] names=new String[]{"id","username","name","type","account","apr","timeLimit","isDay","addTime","status"};
			String[] titles=new String[]{"ID","用户名称","标题","类型(1:基金,2:信托)","借款金额(万)","利率(%)","借款期限","期限类型(0:月,1:天)","发布时间","状态(0:未发布,1:发布中,8:已结束)"};
			List<FundModel> allFundList = fundService.list(0,0,param);
			try {
				ExcelHelper.writeExcel(infile,allFundList, FundModel.class, Arrays.asList(names), Arrays.asList(titles));
				this.export(infile, downloadFile);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("基金信托导出报表异常："+e.getMessage());
			}
			return null;
		}else{
			PageDataList plist=fundService.page(page, param);
			setPageAttribute(plist, param);
			return "index";
		}
	}
	
	/**
	 * 增
	 * @return
	 */
	public String add(){
		if(!StringUtils.isBlank(actionType)){
			if(!StringUtils.isBlank(model.getContent()) && model.getContent().length()>2048){
				message("项目简介内容不能超过2048个字符!当前为"+model.getContent().length()+"个字符!","/admin/fund/add.html");
				return ADMINMSG;
			}
			if(model.getType()==2){//信托
				model.setLowestAccount(model.getTrustLowestAccount());
				model.setMostAccount(model.getTrustMostAccount());
			}
			if(model.getIsDay()==1){//产品期限为天
				model.setTimeLimit(model.getTimeLimitDay());
			}
			//处理附件
			try {
				model.setPic(upload(files, filesFileName, "/admin/fund/add.html"));
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("基金信托,上传文件出错："+e.getMessage());
			}
			User auth_user = (User) session.get(Constant.AUTH_USER);
			model.setAddTime(DateUtils.getNowTime());
			model.setAddIp(getRequestIp());
			model.setUserId(auth_user.getUser_id());
			int r = fundService.add(model);
			if(r>0){
				model=null;
				message("新增基金信托信息成功。","/admin/fund/index.html");
			}else{
				message("新增基金信托信息失败!","/admin/fund/index.html");
			}
			return ADMINMSG;
		}
		return "add";
	}

	/**
	 * 改
	 * @return
	 */
	public String modify(){
		if(!StringUtils.isBlank(actionType)){
			String oldPic = "";//老图片地址
			if(files!=null){//新图片
				try {
					if(!StringUtils.isBlank(model.getPic())){
						oldPic = model.getPic();
					}
					model.setPic(upload(files, filesFileName, "/admin/fund/add.html"));
					if(!StringUtils.isBlank(oldPic)){//删除老图片
						File oldPicFile = new File(ServletActionContext.getServletContext().getRealPath("/data/upload")+oldPic.replace("/data/upload", ""));
						oldPicFile.delete();
					}
				} catch (Exception e) {
					logger.error("基金信托,上传文件出错："+e.getMessage());
				}
			}
			if(model.getType()==2){//信托
				model.setLowestAccount(model.getTrustLowestAccount());
				model.setMostAccount(model.getTrustMostAccount());
			}
			if(model.getIsDay()==1){//产品期限为天
				model.setTimeLimit(model.getTimeLimitDay());
			}
			int r = fundService.modify(model);
			if(r==1){
				message("修改基金信托信息成功。","/admin/fund/index.html");
			}else{
				message("修改基金信托信息失败!","/admin/fund/index.html");
			}
			return ADMINMSG;
		}else{
			long id = paramLong("id");
			model = fundService.get(id);
		}
		return "modify";
	}
	
	/**
	 * 开启/关闭
	 * @return
	 */
	public String modifyStatus(){
		long id = paramLong("id");
		int status = paramInt("status");
		fundService.modify("status", status, id);
		return "redirectIndex";
	}
	
	public FundService getFundService() {
		return fundService;
	}

	public void setFundService(FundService fundService) {
		this.fundService = fundService;
	}

	public File getFiles() {
		return files;
	}

	public void setFiles(File files) {
		this.files = files;
	}

	public String getFilesFileName() {
		return filesFileName;
	}

	public void setFilesFileName(String filesFileName) {
		this.filesFileName = filesFileName;
	}

}
