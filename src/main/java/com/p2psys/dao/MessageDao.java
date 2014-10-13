package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.Message;

public interface MessageDao extends BaseDao {
	
	public int getReceiveMessgeCount(long receive_user) ;
	
	public List getReceiveMessgeByUserid(long receive_user,int start,int pernum);
	
	public int getSentMessgeCount(long sent_user);
	
	public List getSentMessgeByUserid(long sent_user,int start,int pernum) ;
	
	public List getMessgeByUserid(long sent_user,long receive_user,int start,int pernum) ;
	
	public Message getMessageById(long id);
	
	public Message addMessage(Message msg);
	
	public Message modifyMessage(Message msg);
	
	public void modifyBatchMessage(List<Message> list);
	
	public List getMessageList(Long[] ids);
	
	public int getMessageCount(long user_id,int status);
	
	public int getMessageByName(long user_id,String name);
	
	// 方法没有地方调用，暂时注释掉
//	public void sendMessage(String status, String subject, String type,
//			String to, long user_id, String content, String addtime,
//			String extra);
//	public int getSendMessageByContent(String content);
	
}
