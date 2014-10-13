package com.p2psys.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ModelDriven;
import com.p2psys.domain.PasswordToken;
import com.p2psys.domain.User;
import com.p2psys.service.PasswordTokenService;
import com.p2psys.util.StringUtils;

public class PasswordTokenAction extends BaseAction implements
		ModelDriven<PasswordToken> {
	private PasswordTokenService passwordTokenService;
	private List<PasswordToken> tokenList;
	private PasswordToken passwordToken = new PasswordToken();
	
	public String tokenListByUserId() throws IOException{
		User user = getSessionUser();
		List<PasswordToken> tokenList = passwordTokenService.getPasswordTokenByUserId(user.getUser_id());
		request.setAttribute("tokenList", tokenList);
		String actionType = request.getParameter("actionType");
		request.setAttribute("query_type", "tokenList");
		String errormsg = "";
		if (!StringUtils.isBlank(actionType)) {
			int size = tokenList.size();
			PasswordToken passwordToken = null;
			List<String> answerList = new ArrayList<String>();
			for (int i = 0; i < size; i++) {
				String answer = request.getParameter("answer" + (i+1) + "");
				if (StringUtils.isBlank(StringUtils.isNull(answer))) {
					errormsg = "密保答案不能为空！";
					request.setAttribute("errormsg", errormsg);
				    return SUCCESS;
				} else {
					answerList.add(answer);
				}
			}
			boolean isOk = false;
			List<String> ansList = new ArrayList<String>();
			for (int j = 0; j < size; j++) {
				passwordToken = tokenList.get(j);
				ansList.add(passwordToken.getAnswer());
				String answer = answerList.get(j);
				if (!StringUtils.isBlank(answer) && answer.equals(passwordToken.getAnswer())) {
					isOk = true;
				} else {
					isOk = false;
					errormsg = "密保问题不对，请重试！";
					request.setAttribute("errormsg", errormsg);
					return SUCCESS;
				}
			}
			if (isOk) {
				response.sendRedirect("/memberPasswordToken/modifyById.html");
			}
		}
		return SUCCESS;
	}
	
	// v1.6.7.2 RDPROJECT-505 zza 2013-12-06 start
	public String modifyById() throws IOException{
		User user = getSessionUser();
		List<PasswordToken> tokenList = passwordTokenService.getPasswordTokenByUserId(user.getUser_id());
		request.setAttribute("tokenList", tokenList);
		String commitType = request.getParameter("commitType");
		request.setAttribute("query_type", "modifyToken");
		String errormsg = "";
		if (!StringUtils.isBlank(commitType)) {
			int size = tokenList.size();
			List<String> answerList = new ArrayList<String>();
			List<String> questionList = new ArrayList<String>();
			List<String> idList = new ArrayList<String>();
			for (int i = 0; i < size; i++) {
				String id = request.getParameter("id" + (i+1) + "");
				String answer = request.getParameter("answer" + (i+1) + "");
				String question = request.getParameter("passwordToken" + (i+1) + "");
				if (StringUtils.isBlank(StringUtils.isNull(answer))) {
					errormsg = "密保答案不能为空！";
					request.setAttribute("errormsg", errormsg);
				    return SUCCESS;
				} else {
					answerList.add(answer);
				}
				questionList.add(question);
				idList.add(id);
			}
			List<PasswordToken> list = new ArrayList<PasswordToken>();
			for (int i = 0; i < idList.size(); i++) {
				long id = Long.valueOf(idList.get(i));
				PasswordToken pwdToken = passwordTokenService.getPasswordTokenById(id);
				String answer = answerList.get(i);
				String question = questionList.get(i);
				if (id == pwdToken.getId()) {
					pwdToken.setAnswer(answer);
					pwdToken.setQuestion(question);
					list.add(pwdToken);
				}
			}
			passwordTokenService.updatePasswordTokenByList(list);
			message("您已成功重新设置密保","/memberPasswordToken/tokenListByUserId.html","点击返回");
			return MSG;
		}
		return SUCCESS;
	}
	// v1.6.7.2 RDPROJECT-505 zza 2013-12-06 end
	
	public String modifyPasswordToken() throws IOException {
		String type = request.getParameter("type");
		request.setAttribute("query_type", "modifyToken");
		User user = getSessionUser();
		String errormsg = "";
		if ("add".equals(type)) {
			boolean isOk = true;
			try {
				List<PasswordToken> list = new ArrayList<PasswordToken>();
				PasswordToken pt1 = new PasswordToken();
				// 密保问题一
				String passwordToken1 = StringUtils.isNull(request.getParameter("passwordToken1"));
				pt1.setQuestion(passwordToken1);
				String answer1 = StringUtils.isNull(request.getParameter("answer1"));
				pt1.setAnswer(StringUtils.isNull(answer1));
				// 密保问题二
				PasswordToken pt2 = new PasswordToken();
				String passwordToken2 = StringUtils.isNull(request.getParameter("passwordToken2"));
				pt2.setQuestion(passwordToken2);
				String answer2 = request.getParameter("answer2");
				pt2.setAnswer(StringUtils.isNull(answer2));
				// 密保问题三
				PasswordToken pt3 = new PasswordToken();
				String passwordToken3 = StringUtils.isNull(request.getParameter("passwordToken3"));
				String answer3 = request.getParameter("answer3");
				pt3.setQuestion(passwordToken3);
				pt3.setAnswer(StringUtils.isNull(answer3));
				if (passwordToken1.equals(passwordToken2) && answer2.equals("") 
						|| passwordToken2.equals(passwordToken3) && answer3.equals("")
						|| passwordToken1.equals(passwordToken3)) {
					errormsg = "密保问题重复，请重新设置!";
					request.setAttribute("errormsg", errormsg);
				    return SUCCESS;
				} else if (StringUtils.isBlank(answer1) || StringUtils.isBlank(answer2) || StringUtils.isBlank(answer3)) {
					errormsg = "密保答案不能为空!";
					request.setAttribute("errormsg", errormsg);
				    return SUCCESS;
				} else {
					if (!StringUtils.isBlank(pt1.getAnswer())) {
						list.add(pt1);
					}
					if (!StringUtils.isBlank(pt2.getAnswer())) {
						list.add(pt2);
					}
					if (!StringUtils.isBlank(pt3.getAnswer())) {
						list.add(pt3);
					}
//					errormsg = "密保设置成功！";
//					request.setAttribute("errormsg", errormsg);
					passwordTokenService.addPasswordToken(list, user.getUser_id());
				}
			} catch (Exception e) {
				isOk = false;
				e.printStackTrace();
			}
			// v1.6.7.2 RDPROJECT-505 zza 2013-12-06 start
			if (isOk) {
				message("密保设置成功！","/memberPasswordToken/tokenListByUserId.html","点击返回");
				return MSG;
			}
			// v1.6.7.2 RDPROJECT-505 zza 2013-12-06 end
		}
		return SUCCESS;
	}

	public PasswordTokenService getPasswordTokenService() {
		return passwordTokenService;
	}

	public void setPasswordTokenService(PasswordTokenService passwordTokenService) {
		this.passwordTokenService = passwordTokenService;
	}

	public List<PasswordToken> getTokenList() {
		return tokenList;
	}

	public void setTokenList(List<PasswordToken> tokenList) {
		this.tokenList = tokenList;
	}

	@Override
	public PasswordToken getModel() {
		return passwordToken;
	}
	
}
