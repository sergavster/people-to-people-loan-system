package com.p2psys.web.action.admin;

import org.apache.log4j.Logger;

import com.p2psys.domain.Comments;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.service.CommentService;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;
/**
 * ManageCommentAction.java
 
 * 2013-4-10
 * 评论管理
 */
public class ManageCommentAction extends BaseAction{

	private Logger logger=Logger.getLogger(ManageBorrowAction.class);
	
	private CommentService commentService;
	
	public void setCommentService(CommentService commentService) {
		this.commentService = commentService;
	}


	/**
	 * 显示所有评论
	 * @return
	 * @throws Exception
	 */
	public String showAllComment() throws Exception{

		int page=NumberUtils.getInt(request.getParameter("page"));
		String username=StringUtils.isNull(request.getParameter("username"));
		SearchParam param = new SearchParam();
		param.setUsername(username);
		PageDataList plist=commentService.getComment(page, param);
		setPageAttribute(plist, param);
		setMsgUrl("/admin/comment/comment.html");
		return SUCCESS;
	}
	
	/**
	 * 审核评论
	 * @return
	 * @throws Exception
	 */
	public String updateComment()throws Exception{
		long status=NumberUtils.getLong(request.getParameter("status"));
		int id=NumberUtils.getInt(request.getParameter("id"));
		Comments c=commentService.getCommentById(id);
		request.setAttribute("comment", c);
		c.setId(id);
			if(status==1){
				c.setStatus(1);
				commentService.updateCommentByUser_Id(c);
				message("审核通过！", "/admin/comment/showAllComment.html");
				return ADMINMSG;
			}else if(status==-1){
				c.setStatus(-1);
				commentService.updateCommentByUser_Id(c);
				message("审核不通过","/admin/comment/showAllComment.html");
				return ADMINMSG;
			}
		return SUCCESS;
	}
	
	/**
	 * 删除评论
	 * @return
	 * @throws Exception
	 */
	public String deleteComment()throws Exception{
		long id=NumberUtils.getLong(request.getParameter("id"));
		commentService.deleteCommentByid(id);
		message("评论删除成功","/admin/comment/showAllComment.html");
		return ADMINMSG;
	}
	
}
