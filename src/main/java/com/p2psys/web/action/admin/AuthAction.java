package com.p2psys.web.action.admin;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.common.enums.EnumRuleStatus;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.domain.User;
import com.p2psys.domain.UserTrack;
import com.p2psys.model.RuleModel;
import com.p2psys.model.purview.PurviewSet;
import com.p2psys.service.ManageAuthService;
import com.p2psys.service.UserService;
import com.p2psys.tool.uchon.UchonHelper;
import com.p2psys.util.DateUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

public class AuthAction extends BaseAction {
	private final  static Logger logger=Logger.getLogger(AuthAction.class);
	private UserService userService;
	private ManageAuthService manageAuthService;
	
	private String username;
	private String password;
	private String valicode;
	private String uchoncode;
	private StringBuffer sb=new StringBuffer();
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getValicode() {
		return valicode;
	}
	public void setValicode(String valicode) {
		this.valicode = valicode;
	}
	public ManageAuthService getManageAuthService() {
		return manageAuthService;
	}
	public void setManageAuthService(ManageAuthService manageAuthService) {
		this.manageAuthService = manageAuthService;
	}
	public UserService getUserService() {
		return userService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public String index() throws Exception{
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String auth() throws Exception{
		//v1.6.7.1 安全优化 sj 2013-11-20 start
		String source=StringUtils.isNull(request.getAttribute("source"));
		if(source.equals("fliter")){
			return ERROR;
		}
		String msg=checkAuth();
		if(!StringUtils.isBlank(msg)){
			request.setAttribute("msg", sb.toString());
			return ERROR;
		}
		User u=userService.login(username, password);
		//v1.6.7.1 安全优化 sj 2013-11-13 start
		//安全优化规则
		RuleModel safetyRule = new RuleModel(Global.getRule(EnumRuleNid.SAFETY.getValue()));
		if(u == null){//登录失败，用户名或密码错误
			User failUser = userService.getUserByName(username);
			if(failUser != null){//登录失败，用户名存在，但密码错误
				msg = userService.adminLoginIsLock(false, failUser, safetyRule);
				if(!StringUtils.isBlank(msg)){
					request.setAttribute("msg", msg);
					return ERROR;
				}else{
					request.setAttribute("msg", "用户名或密码错误！");
					return ERROR;
				}
			}
			request.setAttribute("msg", "用户名或密码错误！");
			return ERROR;
		}else{//登录成功
			msg = userService.adminLoginIsLock(true, u, safetyRule);
			if(!StringUtils.isBlank(msg)){
				request.setAttribute("msg", msg);
				return ERROR;
			}
		}
		//定期修改密码
		if(safetyRule != null && safetyRule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue() && safetyRule.getValueIntByKey("adminStatus") == 1){
			int regularModifyPwd = safetyRule.getValueIntByKey("adminRegularModifyPwd");
			String modifytime = userService.selectModifyTime(u.getUser_id());//上次修改密码时间
			if(!StringUtils.isBlank(modifytime)){
				if((Long.parseLong(this.getTimeStr()) - Long.parseLong(modifytime))/(24*3600) + 1 > regularModifyPwd){
					request.setAttribute("msg", "需定期("+regularModifyPwd+"天)对密码进行修改!");
					request.setAttribute("username", username);
					request.setAttribute("password", u.getPassword());
					request.setAttribute("user_id", u.getUser_id());
					return "modifypass";
				}
			}
		}
		String pwd = u.getPassword();
		boolean b1 = Pattern.compile("[0-9]").matcher(password).find();
		boolean b2 = Pattern.compile("(?i)[a-zA-Z]").matcher(password).find();
		boolean b3 = Pattern.compile("[~!@#$%^&*()]").matcher(password).find();
		if(b1 && b2 && b3 && password.length() >= 10){
			//v1.6.7.1 安全优化 sj 2013-11-19 end
			// v1.6.5.3 RDPROJECT-87 xx 2013.09.13 start
			RuleModel rule = new RuleModel(Global.getRule("dynamic_password"));
			if(rule!=null && rule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue() && rule.getValueIntByKey("is_use") == EnumRuleStatus.RULE_STATUS_YES.getValue()){
				if (!StringUtils.isBlank(u.getSerial_id())) {
					if (StringUtils.isBlank(uchoncode)) {
						sb.append("请输入动态口令!");
						request.setAttribute("msg", sb.toString());
						return "error";
					}
					String result = UchonHelper.checkDymicPassword(rule.getValueStrByKey("check_url"), u.getSerial_id(), uchoncode);
					Object obj = JSON.parse(result);
					if(obj==null){
						msg = "动态口令校验失败！";
						logger.error("动态口令校验失败！接口返回结果为空！");
					} else {
						Map<String, Object> map = (Map<String, Object>) obj;
						if(map.get("resCode")==null){
							msg = "动态口令校验失败！";
							logger.error("动态口令校验失败！接口返回resCode为空！");
						} else if(Integer.parseInt(map.get("resCode").toString())!=0 && Integer.parseInt(map.get("resCode").toString())!=1 ){
							msg = "动态口令校验失败！";
							logger.error("动态口令校验失败！"+map.get("msg").toString()+","+map.get("resCode").toString());
						} else if(Integer.parseInt(map.get("resCode").toString())==0){
							msg = "动态口令错误！";
						}
					}
					if (!StringUtils.isBlank(msg)) {
						sb.append(msg);
						request.setAttribute("msg", sb.toString());
						return "error";
					}
			    } 
			}
			// v1.6.5.3 RDPROJECT-87 xx 2013.09.13 end
			List list=manageAuthService.getPurviewByUserid(u.getUser_id());
			if(list==null||list.size()<1){
				msg="没有权限，非法访问！";
				sb.append(msg);
				request.setAttribute("msg", sb.toString());
				return ERROR;
			}
			PurviewSet ps=new PurviewSet(list);
			ps.toSet();
			session.put(Constant.AUTH_PURVIEW, ps);
			session.put(Constant.AUTH_USER, u);
			//V1.6.5.3 RDPROJECT-146 liukun 2013-09-11 start
			//前后台会话不要混淆
			//RDPROJECT-177 liukun 2013-09-17 begin
			//session.put(Constant.SESSION_USER, u);
			 //RDPROJECT-177 liukun 2013-09-17 end
			//V1.6.5.3 RDPROJECT-146 liukun 2013-09-11 end
			
			UserTrack newTrack = new UserTrack();
			newTrack.setUser_id(u.getUser_id() + "");
			newTrack.setLogin_ip(getRequestIp());
			newTrack.setLogin_time(DateUtils.getNowTimeStr());
			userService.addUserTrack(newTrack);

			return SUCCESS;
			//v1.6.7.1 安全优化 sj 2013-11-19 start
		}else{
			request.setAttribute("msg", "密码过于简单，请立即修改密码!");
			request.setAttribute("username", username);
			request.setAttribute("password", pwd);
			request.setAttribute("user_id", u.getUser_id());
			return "modifypass";
			
		}
		//v1.6.7.1 安全优化 sj 2013-11-13 end
		//v1.6.7.1 安全优化 sj 2013-11-19 end
		
		//v1.6.7.1 安全优化 sj 2013-11-20 end
	}
	
	private String checkAuth(){
		if(StringUtils.isBlank(valicode)){
			sb.append("验证码不能为空！");
			return ERROR;
		}else if(!checkValidImg(valicode)){
			sb.append("验证码不正确！");
			return ERROR;
		}else if(StringUtils.isBlank(username)){
			sb.append("用户名不能为空！");
			return ERROR;
		}else if(StringUtils.isBlank(password)){
			sb.append("密码不能为空！");
			return ERROR;
		}
		return "";
	}
	
	public String logout() throws Exception{
		session.put(Constant.AUTH_USER, null);
		session.put(Constant.SESSION_USER, null);
		session.put(Constant.AUTH_PURVIEW, null);
		session.put("adminUrlCheck", null);
		message("退出成功！",Global.getString("admin_url"));
		return ADMINMSG;
	}
	
	
	public String getUchoncode() {
		return uchoncode;
	}
	public void setUchoncode(String uchoncode) {
		this.uchoncode = uchoncode;
	}

}