package com.p2psys.model.accountlog.noac;

import com.p2psys.context.Constant;
import com.p2psys.model.accountlog.BaseSimpleNoticeLog;
/**
 * 
 
 *
 */
public class RegisterUserEmailLog extends BaseSimpleNoticeLog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1457984779684944253L;
	public RegisterUserEmailLog() {
		super();
	}

	public RegisterUserEmailLog(long user_id) {
		super();
		this.setUser_id(user_id);
	}
	
	private final static String noticeTypeNid = Constant.NOTICE_REGISTER_USER;
	@Override
	public String getNoticeTypeNid() {
		return noticeTypeNid;
	} 
}
