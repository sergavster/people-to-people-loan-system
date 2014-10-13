package com.p2psys.service;

import java.util.Map;

import com.p2psys.domain.Comments;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;

public interface CommentService {

	
	/**
	 
	 * @param id
	 * 表号 传入-1为例出所有单号
	 * @return
	 * 返回Map类型   List为评论列表 count为评论总页数
	 */
	public Map getCommentListByBorrowid(long id);
	
	
	public int addComment(Comments c);
	
	//---新加内容  tanjiao 修改于2013-04-10
	public void updateCommentByUser_Id(Comments c);
	
	public void deleteCommentByid(long id);
	
	public Comments getCommentById(long id);
	
	public PageDataList getComment(int page, SearchParam param);
	//--新加结束
}
