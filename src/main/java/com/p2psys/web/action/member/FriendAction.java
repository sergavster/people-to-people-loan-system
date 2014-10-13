package com.p2psys.web.action.member;

import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ModelDriven;
import com.p2psys.context.Constant;
import com.p2psys.domain.Friend;
import com.p2psys.domain.User;
import com.p2psys.model.DetailUser;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.service.FriendService;
import com.p2psys.service.UserService;
import com.p2psys.tool.Page;
import com.p2psys.tool.coder.BASE64Encoder;
import com.p2psys.web.action.BaseAction;

public class FriendAction extends BaseAction implements ModelDriven<Friend>{

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(FriendAction.class);

	private FriendService friendService;
	private  UserService userService;
	private Friend friend = new Friend();

	public FriendService getFriendService() {
		return friendService;
	}
	public void setFriendService(FriendService friendService) {
		this.friendService = friendService;
	}
	public UserService getUserService() {
		return userService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	@Override
	public Friend getModel() {
		return friend;
	}
	/**
	 * 好友邀请页面
	 * @return
	 * @throws Exception
	 */
	public String invite() throws Exception {
		User user = this.getSessionUser();
		long user_id = user.getUser_id();
		List list = userService.getInvitedUserByUserid(user_id);
		BASE64Encoder encoder = new BASE64Encoder();
		String s=encoder.encode((user.getUser_id()+"").getBytes());
		request.setAttribute("userid", s);
		request.setAttribute("friendList", list);
		return "success";
	}

	/**
	 * 好友请求页面
	 * @return
	 * @throws Exception
	 */
	public String request() throws Exception {
		User user = (User) session.get(Constant.SESSION_USER);
		long user_id = user.getUser_id();
		List<Friend> list = friendService.getFriendsRequest(user_id);
		request.setAttribute("list", list);
		return SUCCESS;
	}

	/**
	 * 新增好友
	 * @return
	 * @throws Exception
	 */
	public String addfriend() throws Exception {
		Friend f=wrapFriend();
		f.setFriends_userid(friend.getFriends_userid());
		f.setUser_id(this.getSessionUser().getUser_id());
		friendService.addFriend(f);
		message("添加好友成功! ", "/member/friend/myfriend.html");
		return SUCCESS;
	}
	/**
	 * 新增好友请求
	 * @return string
	 * @throws Exception
	 */
	public String addfriendrequest() throws Exception {
		User user = getSessionUser();
		if (user != null) {
			DetailUser detailUser = userService.getDetailUser(user.getUser_id());
			if (detailUser != null) {
				int vip_status = detailUser.getVip_status();
				// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 start
				if (detailUser.getReal_status()!=1) {	
					message("请先进行实名认证", "/member/identify/realname.html", "返回上一页");
					return FAIL;
				}
				// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 end
				if (vip_status != 1) {
					message("请先进行vip认证", "/member/vip.html", "返回上一页");
					return FAIL;
				}
			}
		}
		Friend f = wrapFriend();
		List<Friend> friendList = friendService.getFriendList(user.getUser_id(), friend.getFriends_userid());
		if (user.getUser_id() == friend.getFriends_userid()) {
			message("自己不能添加自己为好友！", "/member/friend/myfriend.html");
			return FAIL;
		} else if (!friendList.isEmpty() && friendList != null) {
			message("已经是好友了，不能重复添加！", "/member/friend/myfriend.html");
			return FAIL;
		} else {
			List<Friend> unVerifyList = friendService.getUnVerifyList(user.getUser_id(), friend.getFriends_userid());
			if (!unVerifyList.isEmpty() && friendList != null) {
				message("好友正在审核中，不能重复添加！", "/member/friend/myfriend.html");
				return FAIL;
			}
			f.setFriends_userid(friend.getFriends_userid());
			f.setUser_id(this.getSessionUser().getUser_id());
			friendService.addFriendRequest(f);
			this.message("添加好友成功，请等待好友的审核 ", "/member/friend/myfriend.html");
			return SUCCESS;
		}
	}
	
	private Friend wrapFriend(){
		friend.setStatus(0);
		friend.setAddtime(this.getTimeStr());
		friend.setAddip(getRequestIp());
		return friend;
	}
	/** 
	 * 封装好友
	 * @param status
	 * @return Friend
	 */
	private Friend wrapFriend(int status){
		wrapFriend();
		friend.setStatus(status);
		return friend;
	}

	/**
	 * 我的好友页面
	 * 
	 * @return 跳转页面
	 * @throws Exception 异常
	 */
	public String myfriend() throws Exception {
		
		User user = (User) session.get(Constant.SESSION_USER);
		long user_id = user.getUser_id();
		// v1.6.6.2 RDPROJECT-288 lhm 2013-10-17 start 
		String username =paramString("username");
		int total = friendService.countFriendsByUserId(user_id, username);
		// v1.6.6.2 RDPROJECT-288 lhm 2013-10-17 end
		int page = 1;
		Page pageInfo = new Page(total, page, Page.ROWS);
		request.setAttribute("pageInfo", pageInfo);
		request.setAttribute("getPageStr", getPageStr(pageInfo));
		// v1.6.6.2 RDPROJECT-288 lhm 2013-10-17 start 
		List<Friend> list = friendService.getFriendsByUserId(user_id, username);
		request.setAttribute("username", username);
		// v1.6.6.2 RDPROJECT-288 lhm 2013-10-17 end
		request.setAttribute("list", list);
		return "success";
	}

	/**
	 * 黑名单页面
	 * @return
	 * @throws Exception
	 */
	public String black() throws Exception {
		User user = (User) session.get(Constant.SESSION_USER);
		long user_id = user.getUser_id();
		List<Friend> list = friendService.getBlackList(user_id);
		request.setAttribute("list", list);
		request.setAttribute("query_type", "black");
		return "success";
	}
	
	public String blackfriend() throws Exception {
		String username =paramString("username"); // 黑名单 用户名
		
		Friend f=wrapFriend(2);
		f.setUser_id(this.getSessionUser().getUser_id());
		f.setFriends_username(username);
		
		friendService.addBlackFriend(f);
		this.message("成功加入黑名单!", "/member/friend/black.html");
		return MSG;
	}
	
	public String delfriend() throws Exception {
		String username =paramString("username");
		friendService.delFriend(this.getSessionUser().getUser_id(), username);
		this.message("成功删除好友!", "/member/friend/myfriend.html");
		return MSG;
	}
	
	public String readdfriend() throws Exception {
		String username =paramString("username");
		friendService.readdFriend(this.getSessionUser().getUser_id(), username);
		this.message("重新加为好友!", "/member/friend/myfriend.html");
		return MSG;
	}
	
	/**
	 * 好友提成页面
	 * @return
	 * @throws Exception
	 */
	public String tc() throws Exception {
	   User user=	getSessionUser();
		String type=paramString("type");
		int page=paramInt("page");
		if(page==0){
			page=1;
		}
		String username=user.getUsername();
		SearchParam param=new SearchParam();
		param.setUsername(username);
		PageDataList plist=friendService.getFriendTiChengAcount(page, param);
		setPageAttribute(plist, param);
		setMsgUrl("/member/friend/tc.html");
		return SUCCESS;	
	}

	private String getPageStr(Page p) {
		StringBuffer sb = new StringBuffer();
		int currentPage = p.getCurrentPage();
		int[] dispayPage = new int[5];
		if (p.getPages() < 5) {
			dispayPage = new int[p.getPages()];
			for (int i = 0; i < dispayPage.length; i++) {
				dispayPage[i] = i + 1;
			}
		} else {
			if (currentPage < 3) {
				for (int i = 0; i < 5; i++) {
					dispayPage[i] = i + 1;
				}
			} else if (currentPage > p.getPages() - 2) {
				for (int i = 0; i < 5; i++) {
					dispayPage[i] = p.getPages() - 4 + i;
				}
			} else {
				for (int i = 0; i < 5; i++) {
					dispayPage[i] = currentPage - 2 + i;
				}
			}
		}
		String typestr = " ";
		// 输出分页信息
		for (int i = 0; i < dispayPage.length; i++) {
			if (dispayPage[i] == currentPage) {
				sb
						.append(" <span class='this_page'>" + currentPage
								+ "</span>");
			} else {
				sb.append(" <a href='myfriend.html?page=" + dispayPage[i]
						+ typestr + "'>" + dispayPage[i] + "</a>");
			}
		}
		return sb.toString();
	}

	


}
