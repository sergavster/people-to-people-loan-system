package com.p2psys.dao;

import com.p2psys.domain.EmailLog;
/**
 * 邮件日志DAO
 
 *
 */
public interface EmailLogDao extends BaseDao {
	/**
	 * 添加邮件日志
	 * @param emailLog
	 */
	public void addEmailLog(EmailLog emailLog);
	/**
	 * 根据链接和类型查找某条日志
	 * @param content
	 * @param type
	 * @return
	 */
	public EmailLog getEmailLog(String content,int type);
	/**
	 * 根据链接和类型修改状态
	 * @param emailLog
	 * @return
	 */
	public EmailLog updateEmailLog(EmailLog emailLog);

}
