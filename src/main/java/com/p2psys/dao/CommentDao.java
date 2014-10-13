package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.Comments;
import com.p2psys.model.SearchParam;

public interface CommentDao extends BaseDao {
	/**
	 
	 * @param borrowid
	 *            传入-1 为返回所有评论 大于零时返回指定标的评论
	 * @return 评论List
	 */
	public List getCommentList(long borrowid);

	/**
	 
	 * @param borrowid
	 *            获取指定借贷单的评论总数 -1 为返回所有评论总数
	 * @return 返回-3为传入参数不合法或无对应情况
	 */
	public int getCommentCount(long borrowid);

	/**
	 
	 * @param Comments
	 */
	public int addComment(Comments c);

	/**
	 * 根据评论ID删除评论
	 * 
	 * @param id
	 * @return 大于0为sql执行成功（具体参考JdbcTemplate 的 update方法
	 */
	public int deleteCommentByCommentId(long id);

	/**
	 * 删除指定标下的所有评论
	 * 
	 * @param Borrowid
	 * @return
	 */
	public int deleteCommentByBorrowId(long Borrowid);
	
	public int deleteCommentByUserId(long userid);
	

	/**
	 * 根据id修改评论状态
	 * tanjiao
	 * 修改于2013-04-10
	 * @param c
	 */
	public void updateCommentByID(Comments c);
	
	/**
	 * 根据id删除评论
	 * tanjiao
	 * 修改于2013-04-10
	 * @param id
	 */
	public void deleteCommentID(long id);
	
	/**
	 * 根据id获得单条评论
	 * tanjiao
	 * 修改于2013-04-10
	 * @param id
	 * @return
	 */
	public Comments getCommentByid(long id);
	
	/**
	 * 获取评论条数
	 * tanjiao
	 * 修改于2013-04-10
	 * @param param
	 * @return
	 */
	public int getCommentCount(SearchParam param);
	
	/**
	 * 根据条件查询评论及分页
	 * tanjiao
	 * 修改于2013-04-10
	 * @param start
	 * @param end
	 * @param param
	 * @return
	 */
	public List getCommentCounts(int start,int end,SearchParam param);
}
