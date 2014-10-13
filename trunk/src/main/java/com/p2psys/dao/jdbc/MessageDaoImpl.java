package com.p2psys.dao.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

import com.p2psys.dao.MessageDao;
import com.p2psys.domain.Message;
import com.p2psys.util.StringUtils;

public class MessageDaoImpl extends BaseDaoImpl implements MessageDao {

	private Logger logger = Logger.getLogger(MessageDaoImpl.class);
	
	public int getReceiveMessgeCount(long receive_user) {
		String sql="select count(t1.id) " +
				"from dw_message t1 " +
				"left join dw_user t2 on t2.user_id=t1.receive_user " +
				"left join dw_user t3 on t3.user_id=t1.sent_user " +
				"where t1.deltype=0 and t1.receive_user=? ";
		int total=0;
		total=count(sql, new Object[]{receive_user});
		return total;
	}
	
	@Override
	public List getReceiveMessgeByUserid(long receive_user,int start,int pernum) {
		String sql="select t1.*,t2.username as receive_username,t3.username as sent_username " +
				"from dw_message t1 " +
				"left join dw_user t2 on t2.user_id=t1.receive_user " +
				"left join dw_user t3 on t3.user_id=t1.sent_user " +
				"where t1.deltype=0 and t1.receive_user=? ";
		String orderSql=" order by t1.addtime desc";
		String limitSql=" limit ?,?";
		sql=sql+orderSql+limitSql;
		List list=new ArrayList();
		list=this.getJdbcTemplate().query(sql, new Object[]{receive_user,start,pernum}, getBeanMapper(Message.class));
		return list;
	}
	
	public int getSentMessgeCount(long sent_user) {
		String sql="select count(t1.id) " +
				"from dw_message t1 " +
				"left join dw_user t2 on t2.user_id=t1.receive_user " +
				"left join dw_user t3 on t3.user_id=t1.sent_user " +
				"where t1.sented=? and t1.sent_user=? ";
		int total=0;
		total=count(sql, new Object[]{"1",sent_user});
		return total;
	}
	
	@Override
	public List getSentMessgeByUserid(long sent_user,int start,int pernum) {
		String sql="select t1.*,t2.username as receive_username ,t3.username as sent_username " +
				"from dw_message t1 " +
				"left join dw_user t2 on t2.user_id=t1.receive_user " +
				"left join dw_user t3 on t3.user_id=t1.sent_user " +
				"where t1.sented=? and t1.sent_user=? ";
		String orderSql=" order by t1.addtime desc";
		String limitSql=" limit ?,?";
		sql=sql+orderSql+limitSql;
		List list=new ArrayList();
		list=this.getJdbcTemplate().query(sql, new Object[]{"1",sent_user,start,pernum}, getBeanMapper(Message.class));
		return list;
	}
	
	@Override
	public List getMessgeByUserid(long sent_user,long receive_user,int start,int pernum) {
		String sql="select t1.*,t2.username as sent_username,t3.username as receive_username " +
				"from dw_message t1 " +
				"left join dw_user t2 on t2.user_id=t1.receive_user " +
				"left join dw_user t3 on t3.user_id=t1.sent_user " +
				"where t1.sent_user=? and t1.receive_user=? "+
		        "order by t1.addtime desc";
		String orderSql=" order by t1.addtime desc";
		String limitSql=" limit ?,?";
		sql=sql+orderSql+limitSql;
		List list=new ArrayList();
		list=this.getJdbcTemplate().query(sql, new Object[]{sent_user,receive_user}, getBeanMapper(Message.class));
		return list;
	}

	@Override
	public Message getMessageById(long id) {
		String sql="select t1.*,t2.username as sent_username,t3.username as receive_username " +
				"from dw_message t1 " +
				"left join dw_user t2 on t2.user_id=t1.receive_user " +
				"left join dw_user t3 on t3.user_id=t1.sent_user " +
				"where id=?";
		Message msg=null;
		try {
			msg=this.getJdbcTemplate().queryForObject(sql, new Object[]{id}, getBeanMapper(Message.class));
		} catch (DataAccessException e) {
			logger.info("getMessageById"+e.getMessage());
		}
		return msg;
	}
	@Override
	public Message addMessage(Message msg) {
		String sql="";
		if(msg.getIs_Authenticate()!="1"){
		 sql="insert into dw_message(sent_user,receive_user,name,status,type,sented,deltype,content,addtime,addip) " +
				"values(?,?,?,?,?,?,?,?,?,?)";
		this.getJdbcTemplate().update(sql,msg.getSent_user(),msg.getReceive_user(),msg.getName()
				,msg.getStatus(),msg.getType(),msg.getSented(),msg.getDeltype(),msg.getContent(),msg.getAddtime(),msg.getAddip());
		}else{
			 sql="insert into dw_message(sent_user,receive_user,name,status,type,sented,deltype,content,addtime,addip,is_Authenticate) " +
						"values(?,?,?,?,?,?,?,?,?,?,?)";
				this.getJdbcTemplate().update(sql,msg.getSent_user(),msg.getReceive_user(),msg.getName()
						,msg.getStatus(),msg.getType(),msg.getSented(),msg.getDeltype(),msg.getContent(),msg.getAddtime(),msg.getAddip(),msg.getIs_Authenticate());
		}
		return msg;
	}

	@Override
	public Message modifyMessage(Message msg) {
		String sql="update dw_message set sent_user=?,receive_user=?,name=?,status=?,type=?,sented=?,deltype=?,content=?,addtime=?,addip=? where id=?";
		int ret=this.getJdbcTemplate().update(sql,msg.getSent_user(),msg.getReceive_user(),msg.getName()
				,msg.getStatus(),msg.getType(),msg.getSented(),msg.getDeltype(),msg.getContent(),msg.getAddtime(),msg.getAddip(),msg.getId());
		if(ret<1) return null;
		return msg;
	}
	
	@Override
	public void modifyBatchMessage(List<Message> list) {
		String sql="update dw_message set sent_user=?,receive_user=?,name=?,status=?,type=?,sented=?,deltype=?,content=?,addtime=?,addip=? where id=?";
		List msgList=new ArrayList();
		for(int i=0;i<list.size();i++){
			Message msg=list.get(i);
			Object[] args=new Object[]{msg.getSent_user(),msg.getReceive_user(),msg.getName()
					,msg.getStatus(),msg.getType(),msg.getSented(),msg.getDeltype(),msg.getContent(),msg.getAddtime(),msg.getAddip(),msg.getId()};
			msgList.add(args);
		}
		this.getJdbcTemplate().batchUpdate(sql, msgList);
	}

	@Override
	public List getMessageList(Long[] ids) {
		String sql="select t1.*,t2.username as sent_username,t3.username as receive_username " +
				"from dw_message t1 " +
				"left join dw_user t2 on t2.user_id=t1.receive_user " +
				"left join dw_user t3 on t3.user_id=t1.sent_user where t1.id in (";
		sql+=StringUtils.contact(ids)+")";
		List list=new ArrayList();
		list=this.getJdbcTemplate().query(sql, getBeanMapper(Message.class));
		return list;
	}

	@Override
	public int getMessageCount(long user_id, int status) {
		String sql="select count(1) " +
				"from dw_message t1 " +
				"left join dw_user t2 on t2.user_id=t1.receive_user " +
				"left join dw_user t3 on t3.user_id=t1.sent_user " +
				"where t1.deltype=0 and t1.receive_user=? and t1.status=? ";
		int total=0;
		total=count(sql, new Object[]{user_id,status});
		return total;
	}

	@Override
	public int getMessageByName(long user_id, String name) {
		String sql = "select count(*) from dw_message where receive_user=? and name=? ";
		int total=0;
		total=count(sql, new Object[]{user_id,name});
		logger.info("SQL:"+sql);
		return total;
	}
	
	// 方法没有地方调用，暂时注释掉
//	@Override
//	public void sendMessage(String status, String subject, String type,
//			String to, long user_id, String content, String addtime,
//			String extra) {
//		String sql = "insert into dw_message_tosend(status,subject,type,`to`,user_id,content,addtime,extra) " +
//					"values(?,?,?,?,?,?,?,?)";
//		logger.info("添加发送短信或邮件 SQL ： " + sql);
//		this.getJdbcTemplate().update(sql,status,subject,type,to,user_id,content, addtime,extra);
//		
//	}
//	
//	@Override
//	public int getSendMessageByContent(String content) {
//		String sql = "select count(*) from dw_message_tosend where content=? ";
//		int total=0;
//		total=count(sql, new Object[]{content});
//		return total;
//	}
	
	
}
