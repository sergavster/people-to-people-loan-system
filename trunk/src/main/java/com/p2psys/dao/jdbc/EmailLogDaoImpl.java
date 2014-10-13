package com.p2psys.dao.jdbc;

import org.apache.log4j.Logger;

import com.p2psys.dao.EmailLogDao;
import com.p2psys.domain.EmailLog;

public class EmailLogDaoImpl extends BaseDaoImpl implements EmailLogDao{
	
	private Logger logger = Logger.getLogger(EmailLogDaoImpl.class);
	
	@Override
	public void addEmailLog(EmailLog emailLog) {
		String sql = "insert into dw_email_log (user_id,type,status,content,addtime) values(?,?,?,?,?)";
		logger.debug("addEmailLog():" + sql);
		this.getJdbcTemplate().update(sql,emailLog.getUser_id(),emailLog.getType(),emailLog.getStatus(),emailLog.getContent(),emailLog.getAddtime());
	}

	@Override
	public EmailLog getEmailLog(String content,int type) {
		String sql = "select * from dw_email_log where content=? and type=?";
		logger.debug("getEmailLogByContent():" + sql);
		EmailLog emailLog = null;
		try{
//			emailLog = this.getJdbcTemplate().queryForObject(sql, new Object[]{content,type},new EmailLogMapper());
			emailLog = this.getJdbcTemplate().queryForObject(sql, new Object[]{content,type},
					getBeanMapper(EmailLog.class));
		}catch(Exception e){
			e.printStackTrace();
		}
		return emailLog;
	}

	@Override
	public EmailLog updateEmailLog(EmailLog emailLog) {
		String sql = "update dw_email_log set status=? where content=? and type=?";
		logger.debug("updateEmailLog():" + sql);
		int result = this.getJdbcTemplate().update(sql,emailLog.getStatus(),emailLog.getContent(),emailLog.getType());
		if(result < 1){
			emailLog = null;
		}
		return emailLog;
	}

}
