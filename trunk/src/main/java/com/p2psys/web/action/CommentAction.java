package com.p2psys.web.action;

import java.util.Map;

import com.opensymphony.xwork2.ModelDriven;
import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.context.Global;
import com.p2psys.domain.User;
import com.p2psys.model.BorrowComments;
import com.p2psys.model.DetailUser;
import com.p2psys.model.RuleModel;
import com.p2psys.service.CommentService;
import com.p2psys.service.UserService;

public class CommentAction extends BaseAction implements ModelDriven<BorrowComments> {

	private String valicode;
	private String comments;
	private long borrowid;
	private CommentService commentservice;
	private UserService userService;

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	private BorrowComments commentinfo = new BorrowComments();
	private String rsmsg;
	private String backurl;

	public String add() {
		User user = getSessionUser();
		if (user != null) {
			DetailUser detailUser = userService.getDetailUser(user.getUser_id());
			if (detailUser != null) {
				RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.COMMENT.getValue()));
				int vip_status = detailUser.getVip_status();
				if (rule.getValueIntByKey("need_vip") == 1 && vip_status != 1) {
					message("请先进行vip认证", "/member/vip.html", "返回上一页");
					return FAIL;
				}
				// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 start
				if (detailUser.getReal_status() != 1) {
					message("请先进行实名认证", "/member/identify/realname.html", "返回上一页");
					return FAIL;
				}
				// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 end
			}
		}
		if (checkValidImg(valicode)) {
			//System.out.println(commentinfo);
			commentinfo.setAddip(getRequestIp());
			commentinfo.setAddtime(getTimeStr());
			commentinfo.setUser_id(getSessionUser().getUser_id());
			commentinfo.setArticle_id(borrowid);
			commentinfo.setComment(comments);
			if (commentservice.addComment(commentinfo)>0)
				rsmsg="评论添加成功";
		} else {
			rsmsg = "验证码不正确";
		}

		backurl = "<a href='" + request.getHeader("Referer") + "'>点击返回</a>";
		return "success";
	}
	
	public String list(){
		Map map=commentservice.getCommentListByBorrowid(borrowid);
		request.setAttribute("commentList", map.get("List"));
		request.setAttribute("count", map.get("count"));
		return "success";
	}

	public String getValicode() {
		return valicode;
	}

	public void setValicode(String valicode) {
		this.valicode = valicode;
	}

	public CommentService getCommentservice() {
		return commentservice;
	}

	public void setCommentservice(CommentService commentservice) {
		this.commentservice = commentservice;
	}

	public String getRsmsg() {
		return rsmsg;
	}

	public void setRsmsg(String rsmsg) {
		this.rsmsg = rsmsg;
	}

	public String getBackurl() {
		return backurl;
	}

	public void setBackurl(String backurl) {
		this.backurl = backurl;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Override
	public BorrowComments getModel() {
		return commentinfo;
	}

	public long getBorrowid() {
		return borrowid;
	}

	public void setBorrowid(long borrowid) {
		this.borrowid = borrowid;
	}
	

}
