package com.p2psys.web.action.member;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.opensymphony.xwork2.ModelDriven;
import com.p2psys.context.Constant;
import com.p2psys.domain.Message;
import com.p2psys.domain.User;
import com.p2psys.model.DetailUser;
import com.p2psys.model.PageDataList;
import com.p2psys.service.MessageService;
import com.p2psys.service.UserService;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

public class MessageAction extends BaseAction implements ModelDriven<Message>{
	
	private Message message=new Message();
	private MessageService messageService;
	private UserService userService;

	@Override
	public Message getModel() {
		return message;
	}
	public MessageService getMessageService() {
		return messageService;
	}
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}
	public UserService getUserService() {
		return userService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	/**
	 * 读取收件箱Action
	 * @return
	 * @throws Exception
	 */
	/**
	 * @return
	 * @throws Exception
	 */
	public String box() throws Exception{
		User sessionUser=this.getSessionUser();
		long userid=sessionUser.getUser_id();
		int page=NumberUtils.getInt(request.getParameter("page"));
		PageDataList plist=messageService.getReceiveMessgeByUserid(userid, page);
		
		/**
		 * 新增：获得登录用户的类型，根据用户类型判断是否允许用户写信息
		 * 修改时间：2013-3-21
		 */
		int Utypeid = userService.getUserById(userid).getType_id();
		int displaySend = 0;
		if (Utypeid == 1 || Utypeid == 3 || Utypeid == 15) {
			displaySend = 1;
		}
		HttpSession session = request.getSession();
		session.setAttribute("displaySend", displaySend);
		
		request.setAttribute("msgList", plist.getList());
		request.setAttribute("page", plist.getPage());
		request.setAttribute("param", new HashMap());
		return SUCCESS;
	}
	
	public String sent() throws Exception{
		User sessionUser=this.getSessionUser();
		long userid=sessionUser.getUser_id();
		int page=NumberUtils.getInt(request.getParameter("page"));
		PageDataList plist=messageService.getSentMessgeByUserid(userid, page);
		request.setAttribute("msgList", plist.getList());
		request.setAttribute("page", plist.getPage());
		request.setAttribute("param", new HashMap());
		return SUCCESS;
	}
	/**
	 * 处理发消息Action
	 * @return
	 * @throws Exception
	 */
	/**
	 * 处理发消息Action
	 * 
	 * @return
	 * @throws Exception
	 */
	public String send() throws Exception {
		String type = StringUtils.isNull(request.getParameter("type"));
		String sendType = StringUtils.isNull(request.getParameter("sendType"));
		long id = NumberUtils.getLong(request.getParameter("id"));
		Message msg = messageService.getMessageById(id);
		User sent_user = this.getSessionUser();
		DetailUser detailUser=userService.getDetailUser(sent_user.getUser_id());
		if(detailUser!=null){
			int vip_status=detailUser.getVip_status();
			// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 start
			if(detailUser.getReal_status()!=1){
				request.setAttribute("errormsg", "请先进行实名认证");
				return FAIL;
			}
			// v1.6.7.2 RDPROJECT-514 xx 2013-12-03 end
			if(vip_status!=1){
				request.setAttribute("errormsg", "请先进行vip认证");
				return FAIL;
			}
		}
		String checked = request.getParameter("sendAll");
		if (type.equals("add")) {
			String errormsg = checkMessage();
			if (!errormsg.equals("")) {
				request.setAttribute("errormsg", errormsg);
				return FAIL;
			}
			User receive_user = userService.getUserByName(message
					.getReceive_username());
			if (receive_user == null && checked == null) {
				errormsg = "收件人不存在";
				request.setAttribute("errormsg", errormsg);
				return FAIL;
			}

			if (checked != null) {
				/**
				 * 群发邮件
				 */
				List<User> list = userService.getAllUser(2);
				for (int i = 0; i < list.size(); i++) {
					message.setReceive_user(list.get(i).getUser_id());
					message.setSent_user(sent_user.getUser_id());
					message.setStatus(0);
					message.setType(Constant.SYSTEM);
					message.setAddip(getRequestIp());
					message.setAddtime(getTimeStr());
					messageService.addMessage(message);
				}
				message("发送消息成功！", "/member/message/sent.html");
				return MSG;
			}

			message.setSent_user(sent_user.getUser_id());
			message.setReceive_user(receive_user.getUser_id());
			message.setStatus(0);
			message.setType(Constant.SYSTEM);
			message.setAddip(getRequestIp());
			message.setAddtime(getTimeStr());

			messageService.addMessage(message);

			message("发送消息成功！", "/member/message/sent.html");
			return MSG;
		} else if (type.equals("reply")) {
			message.setSent_user(msg.getReceive_user());
			message.setReceive_user(msg.getSent_user());
			message.setStatus(0);
			message.setType(Constant.SYSTEM);
			message.setAddip(getRequestIp());
			message.setAddtime(getTimeStr());
			message.setName("Re:" + msg.getName());
			message.setContent(message.getContent()
					+ "</br>------------------ 原始信息 ------------------</br>"
					+ msg.getContent());

			messageService.addMessage(message);

			message("回复消息成功！", "/member/message/sent.html");
			return MSG;
		} else {
			request.setAttribute("msg_type", "send");
		}
		request.setAttribute("sendType", sendType);
		request.setAttribute("msg", msg);
		return SUCCESS;
	}

	private String checkMessage() {
		String errormsg = "";
		String checked = request.getParameter("sendAll");
		String validcode = StringUtils.isNull(request.getParameter("valicode"));
		//System.out.println("name: " + message.getName());
		if (StringUtils.isNull(message.getReceive_username()).equals("")
				&& checked == null) {
			errormsg = "收件人不能为空！";
			return errormsg;
		} else if (StringUtils.isNull(message.getName().trim()).equals("")) {
			errormsg = "标题不能为空！";
			return errormsg;
		} else if (StringUtils.isNull(message.getContent()).equals("")) {
			errormsg = "内容不能为空！";
			return errormsg;
		} else if (!checkValidImg(validcode)) {
			errormsg = "验证码错误！";
			return errormsg;
		}
		return errormsg;
	}
	
	public String set() throws Exception{
		String type=StringUtils.isNull(request.getParameter("type"));
		Long[] ids=NumberUtils.getLongs(request.getParameterValues("id"));
		if(ids.length<1){
			message("您操作有误，请勿乱操作！", "/member/message/box.html");
			return MSG;
		}
		String tip="";
		if(type.equals(Constant.DEL_RECEIVE_MSG)){
			tip="删除信息成功！";
			messageService.deleteReceiveMessage(ids);
		}else if(type.equals(Constant.DEL_SENT_MSG)){
			tip="删除信息成功！";
			messageService.deleteSentMessage(ids);
		}else if(type.equals(Constant.SET_READ_MSG)){
			tip="已标记已读 ！";
			messageService.setReadMessage(ids);
		}else if(type.equals(Constant.SET_UNREAD__MSG)){
			tip="已标记未读 ！";
			messageService.setUnreadMessage(ids);
		}
		message(tip, "/member/message/box.html");
		return MSG;
	}
	
	public String view() throws Exception{
		long id=NumberUtils.getLong(request.getParameter("id"));
		String type=StringUtils.isNull(request.getParameter("type"));
		if(id<1){
			message("您操作有误，请勿乱操作！", "/member/message/box.html");
			return MSG;
		}

		Message msg=messageService.getMessageById(id);
		if(msg==null) {
			message("您操作有误，请勿乱操作！", "/member/message/box.html");
			return MSG;
		}
		User friend=userService.getUserById(msg.getSent_user());
		msg.setStatus(1);
		messageService.modifyMessge(msg);
		request.setAttribute("msg", msg);
		request.setAttribute("friend", friend);
		request.setAttribute("type", type);
		return SUCCESS;
	}
	
	
}
