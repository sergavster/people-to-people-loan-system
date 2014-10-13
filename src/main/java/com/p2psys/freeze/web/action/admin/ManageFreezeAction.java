package com.p2psys.freeze.web.action.admin;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ModelDriven;
import com.p2psys.context.Constant;
import com.p2psys.domain.User;
import com.p2psys.freeze.model.FreezeModel;
import com.p2psys.freeze.service.FreezeService;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.service.UserService;
import com.p2psys.tool.jxl.ExcelHelper;
import com.p2psys.util.DateUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

/**
 * 用户冻结管理
 
 * @version 1.0
 * @since 2013-10-29
 */
public class ManageFreezeAction extends BaseAction implements ModelDriven<FreezeModel>{
	private static final Logger logger=Logger.getLogger(ManageFreezeAction.class);
	
	private FreezeService freezeService;
	private UserService userService;
	
	FreezeModel model = new FreezeModel();
	@Override
	public FreezeModel getModel() {
		return model;
	}
	
	/**
	 * 列表
	 * @return
	 * @throws Exception 
	 */
	public String index(){
		String username = paramString("username");
		int status = paramInt("status");
		int page = paramInt("page");
		SearchParam param=new SearchParam();
		param.setUsername(username);
		param.setStatus(status+"");
		setMsgUrl("/admin/freeze/index.html");
		if("export".equals(actionType)){//导出报表
			String contextPath = ServletActionContext.getServletContext().getRealPath("/");
			String downloadFile="userFreeze_"+System.currentTimeMillis()+".xls";
			String infile=contextPath+"/data/export/"+downloadFile;
			String[] names=new String[]{"id","username","status","mark","remark","addTime"};
			String[] titles=new String[]{"ID","用户名称","状态(0:未启用,1:启用)","冻结标示","备注","添加时间"};
			List<FreezeModel> allFundList = freezeService.list(0,0,param);
			try {
				ExcelHelper.writeExcel(infile,allFundList, FreezeModel.class, Arrays.asList(names), Arrays.asList(titles));
				this.export(infile, downloadFile);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("用户冻结导出报表异常："+e.getMessage());
			}
			return null;
		}else{
			PageDataList plist=freezeService.page(page, param);
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
			if(model.getUserId()==0 && !StringUtils.isBlank(model.getUsername())){
				model.setMark(model.getMark().replaceAll(" ", ""));
				User user = userService.getUserByName(model.getUsername());
				if(user==null){
					message(model.getUsername()+"该用户不存在,添加冻结信息失败！", "/admin/freeze/add.html");
					return ADMINMSG;
				}
				if(freezeService.isExistsByUserName(model.getUsername())){
					message(model.getUsername()+"的冻结信息已存在,请勿重复添加！", "/admin/freeze/add.html");
					return ADMINMSG;
				}
				model.setUserId(user.getUser_id());
			}
			
			User auth_user = (User) session.get(Constant.AUTH_USER);
			model.setVerifyUserId(auth_user.getUser_id());
			model.setAddTime(DateUtils.getNowTime());
			model.setAddIp(getRequestIp());
			
			int r = freezeService.add(model);
			if(r==1){
				model=null;
				message("新增冻结信息成功。","/admin/freeze/index.html?status=99");
			}else{
				message("新增冻结信息失败!","/admin/freeze/index.html?status=99");
			}
			return ADMINMSG;
		}
		return "add";
	}
	
	/**
	 * 根据用户名判断是否已存在冻结信息
	 */
	public String isExistsByUserName(){
		String username = paramString("username");
		Map<String, Object> map = new HashMap<String, Object>();
		boolean r = freezeService.isExistsByUserName(username);
		map.put("result", r+"");
		String json = JSON.toJSONString(map);
		try {
			printJson(json);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("根据用户名判断冻结信息是否存在异常："+e.getMessage());
		}
		return null;
	}
	
	/**
	 * 改
	 * @return
	 */
	public String modify(){
		if(!StringUtils.isBlank(actionType)){
			model.setMark(model.getMark().replaceAll(" ", ""));
			int r = freezeService.modify(model);
			if(r==1){
				message("修改冻结信息成功。","/admin/freeze/index.html?status=99");
			}else{
				message("修改冻结信息失败!","/admin/freeze/index.html?status=99");
			}
			return ADMINMSG;
		}else{
			long id = paramLong("id");
			model = freezeService.get(id);
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
		freezeService.modify("status", status, id);
		return "redirectIndex";
	}

	public FreezeService getFreezeService() {
		return freezeService;
	}

	public void setFreezeService(FreezeService freezeService) {
		this.freezeService = freezeService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
