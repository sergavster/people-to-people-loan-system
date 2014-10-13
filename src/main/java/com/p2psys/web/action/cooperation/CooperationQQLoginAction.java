package com.p2psys.web.action.cooperation;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import com.p2psys.common.enums.EnumCooperationLoginType;
import com.p2psys.domain.CooperationLogin;
import com.p2psys.domain.QqGetUserInfoParamBean;
import com.p2psys.domain.QqGetUserInfoResultBean;
import com.p2psys.domain.User;
import com.p2psys.service.CooperationLoginService;
import com.p2psys.service.UserService;
import com.p2psys.util.cooperation.OpenQqUtils;
import com.p2psys.web.action.BaseAction;

public class CooperationQQLoginAction extends BaseAction {

	private CooperationLoginService cooperationLoginService;
	
	private UserService userService;
	
	// 日志
    protected final Logger    log = Logger.getLogger(this.getClass());
	
	public void qqLogin(){
		// QQ互联工具类 
		OpenQqUtils oqu = new OpenQqUtils();
		try {
			request.setAttribute("qqLoginUrl", oqu.getQqLoginUrl());
			response.sendRedirect(oqu.getQqLoginUrl());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String loginSuccess()throws IOException {
		
		// QQ互联工具类 
		OpenQqUtils oqu = new OpenQqUtils();
		// 动态拼接换取accessToken的URL 
		String accessTokenUrl = oqu.getAccessTokenUrl(request.getParameter("code"));
		// AccessToken
		String accessToken = oqu.getAccessToken(accessTokenUrl);
		// OpenId
		String openId = oqu.getOpenId(accessToken);
		
		if(openId == null || openId.length() <= 0  || accessToken == null || accessToken.length() <= 0){
			return "loginError";
		}
		QqGetUserInfoParamBean paramBean = new QqGetUserInfoParamBean();
		paramBean.setAccessToken(accessToken);
		paramBean.setOpenId(openId);
		QqGetUserInfoResultBean userInfoResultBean = oqu.getUserInfo(paramBean);
		// 判断qq合作登录的接口 获取合作登录会员基本信息失败操作
		if (userInfoResultBean.getErrorFlg()) {
			this.log.error("QQ合作登录_QQ登录信息验证失败 ");
			return "loginError";
		}
		String getNickname = userInfoResultBean.getNickName();
		if(getNickname != null && getNickname.length() > 0){
			request.setAttribute("nickname", getNickname);
		}
		
		// 封装联合登陆信息
		CooperationLogin cooperation = new CooperationLogin();
		cooperation.setOpen_id(openId);
		cooperation.setOpen_key(accessToken);
		cooperation.setType(EnumCooperationLoginType.COOPERATION_QQ.getValue());
		cooperationLoginService.addCooperationLogin(cooperation);
		
		CooperationLogin qqLogin = cooperationLoginService.getCooperationLogin(openId, accessToken, EnumCooperationLoginType.COOPERATION_QQ.getValue());
		// 如果查询联合登陆信息不为空，并且user_id不为空，则说明QQ已被绑定，查询user信息，跳转至登陆的action
		
		if(qqLogin != null && qqLogin.getUser_id() > 0){
			User user = userService.getUserById(qqLogin.getUser_id());
			request.setAttribute("openUser", user);
			try {
				request.getRequestDispatcher("/user/login.html").forward(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			}
		}else if(qqLogin != null && qqLogin.getId() > 0 && qqLogin.getUser_id() <= 0){// 如果查询联合登陆信息不为空，并且user_id为空，则说明QQ未被绑定，跳转至登陆的注册页面
			request.setAttribute("openLoginId", qqLogin.getId());
			return "loginRegister";
		}
		return "loginError";
	}

	public CooperationLoginService getCooperationLoginService() {
		return cooperationLoginService;
	}

	public void setCooperationLoginService(
			CooperationLoginService cooperationLoginService) {
		this.cooperationLoginService = cooperationLoginService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
}
