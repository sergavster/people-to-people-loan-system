package com.p2psys.service;

import com.p2psys.domain.Message;
import com.p2psys.model.PageDataList;

/**
 * 消息模块的Serice层JDBC实现类
 *
 
 * @date 2012-8-29 下午5:20:45
 * @version
 *
 *  (c)</b> 2012-rongdu-<br/>
 *
 */
public interface MessageService {
	
	/**
	 * 根据用户ID获取收件箱中的消息列表
	 * @param userid
	 * @return
	 */
	public PageDataList getReceiveMessgeByUserid(long userid,int startPage);
	
	/**
	 * 根据用户ID获已经发送的消息列表
	 * @param userid
	 * @return
	 */
	public PageDataList getSentMessgeByUserid(long userid,int startPage);
	
	/**
	 * 根据Id获取Message
	 * @param id
	 * @return
	 */
	public Message getMessageById(long id);	
	
	/**
	 * 保存消息到数据库
	 * @param msg
	 * @return
	 */
	public Message addMessage(Message msg);
	
	/**
	 * 更新Message
	 * @param msg
	 * @return
	 */
	public Message modifyMessge(Message msg);
	/**
	 * 批量删除收件箱信息
	 * @param ids
	 */
	public void deleteReceiveMessage(Long[] ids);
	/**
	 * 批量删除发件箱信息
	 * @param ids
	 */
	public void deleteSentMessage(Long[] ids);
	/**
	 * 批量设置已经信息
	 * @param ids
	 */
	public void setReadMessage(Long[] ids);
	/**
	 * 批量设置未读信息
	 * @param ids
	 */
	public void setUnreadMessage(Long[] ids);
	
	public int getUnreadMesage(long userid);
}
