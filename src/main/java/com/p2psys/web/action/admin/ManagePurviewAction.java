package com.p2psys.web.action.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ModelDriven;
import com.p2psys.domain.Purview;
import com.p2psys.domain.UserType;
import com.p2psys.exception.ManageAuthException;
import com.p2psys.exception.UserServiceException;
import com.p2psys.model.AdminInfoModel;
import com.p2psys.model.DetailUser;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.model.UserinfoModel;
import com.p2psys.model.purview.PurviewSet;
import com.p2psys.service.ManageAuthService;
import com.p2psys.service.UserService;
import com.p2psys.service.UserinfoService;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

/**
 * 权限管理的Action
 * 
 
 * @date 2012-9-26 下午3:32:30
 * @version
 * 
 *           (c)</b> 2012-rongdu-<br/>
 * 
 */
public class ManagePurviewAction extends BaseAction implements
		ModelDriven<Purview> {

	private static final Logger logger = Logger
			.getLogger(ManagePurviewAction.class);

	Purview model = new Purview();
	List<Integer> purviewid;
	ManageAuthService manageAuthService;
	UserService userService;
	UserinfoService userinfoService;
	private UserinfoModel UserinfoModel = new UserinfoModel();
	
	public UserinfoModel getUserinfoModel() {
		return UserinfoModel;
	}

	public void setUserinfoModel(UserinfoModel userinfoModel) {
		UserinfoModel = userinfoModel;
	}

	public UserinfoService getUserinfoService() {
		return userinfoService;
	}

	public void setUserinfoService(UserinfoService userinfoService) {
		this.userinfoService = userinfoService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public static Logger getLogger() {
		return logger;
	}
	public List<Integer> getPurviewid() {
		return purviewid;
	}
	public void setPurviewid(List<Integer> purviewid) {
		this.purviewid = purviewid;
	}
	public void setModel(Purview model) {
		this.model = model;
	}

	@Override
	public Purview getModel() {
		return model;
	}

	public ManageAuthService getManageAuthService() {
		return manageAuthService;
	}

	public void setManageAuthService(ManageAuthService manageAuthService) {
		this.manageAuthService = manageAuthService;
	}

	public String showAllRole() throws Exception {

		List userTypeList = userService.getAllUserType();
		request.setAttribute("list", userTypeList);
		return SUCCESS;
	}

	public String modifyRole() throws Exception {
		String actionType = StringUtils.isNull(request
				.getParameter("actionType"));
		int id = NumberUtils.getInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String purview = request.getParameter("purview");
		int type = NumberUtils.getInt(request.getParameter("type"));
		String order = request.getParameter("order");
		String remark = request.getParameter("remark");
		UserType userType = new UserType();
		userType.setType_id(id);
		userType.setName(name);
		userType.setPurview(purview);
		userType.setType(type);
		userType.setOrder(order);
		userType.setRemark(remark);
	
		List userTypeList = new ArrayList<UserType>();
		userTypeList.add(userType);
		String msg = "修改成功！";
		if (!StringUtils.isBlank(actionType)) {
			userService.updateUserTypeByList(userTypeList);
			message(msg, "/admin/purview/showAllRole.html");
			return ADMINMSG;
		}

		request.setAttribute("id", id);
		request.setAttribute("userType",userService.getUserTypeById(id));
		return SUCCESS;
	}

	public String delRole() throws Exception {
		long id = NumberUtils.getLong(request.getParameter("id"));
		String msg = "删除成功！";
		try {
			userService.deleteUserTypeById(id);
		} catch (UserServiceException e) {
			logger.debug("delRole:" + e.getMessage());
			msg = e.getMessage();
		}
		message(msg, "/admin/purview/showAllRole.html");
		return ADMINMSG;

	}

	public String addRole() throws Exception {
		String actionType = StringUtils.isNull(request
				.getParameter("actionType"));
		int id = NumberUtils.getInt(request.getParameter("id"));
		String msg = "增加成功！";
		String name = request.getParameter("name");
		String purview = request.getParameter("purview");
		int type = NumberUtils.getInt(request.getParameter("type"));
		String order = request.getParameter("order");
		String remark = request.getParameter("remark");
		UserType userType = new UserType();
		userType.setName(name);
		userType.setPurview(purview);
		userType.setType(type);
		userType.setOrder(order);
		userType.setRemark(remark);
		if (!StringUtils.isBlank(actionType)) {
			userService.addUserType(userType);
			message(msg, "/admin/purview/showAllRole.html");
			return ADMINMSG;
		}
		request.setAttribute("id", id);
		return SUCCESS;
	}

	public String setPurview() throws Exception {

		long user_typeid=NumberUtils.getLong(request.getParameter("user_typeid"));
		UserType userType=userService.getUserTypeById(user_typeid);
		if(userType==null){
			message("该角色不存在","/admin/purview/showAllRole.html");
			return ADMINMSG;
		}
		if(!getActionType().isEmpty()){
			logger.debug(this.purviewid);
			manageAuthService.addUserTypePurviews(purviewid, user_typeid);
		}
		UserType role=userService.getUserTypeById(user_typeid);
		List list = manageAuthService.getAllCheckedPurview(user_typeid);

		PurviewSet ps = new PurviewSet(list);
		Set set = ps.toSet();
		request.setAttribute("purviews", set);
		request.setAttribute("role", role);
		return SUCCESS;
	}

	public String allPurview() throws Exception {
		int pid = NumberUtils.getInt(request.getParameter("pid"));
		List list = manageAuthService.getPurviewByPid(pid);
		setMsgUrl("/admin/purview/allPurview.html");
		request.setAttribute("list", list);
		request.setAttribute("pid", pid);
		return SUCCESS;
	}

	public String addPurview() throws Exception {
		String actionType = StringUtils.isNull(request
				.getParameter("actionType"));
		int pid = NumberUtils.getInt(request.getParameter("pid"));
		if (!StringUtils.isBlank(actionType)) {
			Purview p = manageAuthService.getPurview(pid);
			if (p != null) {
				model.setLevel(p.getLevel() + 1);
			} else {
				model.setLevel(1);
			}
			manageAuthService.addPurview(model);
			message("新增权限成功！");
			return ADMINMSG;
		}
		request.setAttribute("pid", pid);
		return SUCCESS;
	}

	public String delPurview() throws Exception {
		long id = NumberUtils.getLong(request.getParameter("id"));
		String msg = "删除成功！";
		try {
			manageAuthService.delPurview(id);
		} catch (ManageAuthException e) {
			logger.debug("delPurview:" + e.getMessage());
			msg = e.getMessage();
		}
		message(msg);
		return ADMINMSG;
	}

	public String modifyPurview() throws Exception {
		String actionType = StringUtils.isNull(request.getParameter("actionType"));
		int id = NumberUtils.getInt(request.getParameter("id"));
		if (!StringUtils.isBlank(actionType)) {
			manageAuthService.modifyPurview(model);
			message("修改权限成功");
			return ADMINMSG;
		}
		Purview p=manageAuthService.getPurview(id);
		request.setAttribute("p", p);
		return SUCCESS;
	}
	
	public String allAdmin() throws Exception {
		
		int page = NumberUtils.getInt(request.getParameter("page"));
		int type = NumberUtils.getInt(request.getParameter("type"));
		SearchParam searchParam=new SearchParam();
		PageDataList pageDataList = userService.getUserList(page,searchParam,type);
		request.setAttribute("adminList", pageDataList.getList());
		request.setAttribute("page", pageDataList.getPage());
		request.setAttribute("params", searchParam.toMap());
		return SUCCESS;
	}
	
	public String editAdmin() throws Exception {
		
		long userid = NumberUtils.getLong(request.getParameter("user_id"));
		int type = NumberUtils.getInt(request.getParameter("type"));
		if (!StringUtils.isBlank(actionType)) {
			AdminInfoModel model = userinfoService.getAllAdminInfoModelByUserid(userid);
			userinfoService.updateAdmininfo(model);
			message("修改信息成功","/admin/purview/allAdmin.html?type=1");
			return ADMINMSG;
		}
		DetailUser detailUser = userService.getDetailUser(userid,type);
		request.setAttribute("user",detailUser);
		return SUCCESS;
	}
	
}
