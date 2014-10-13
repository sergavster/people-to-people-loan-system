package com.p2psys.model;

import java.util.Map;

import org.tuckey.web.filters.urlrewrite.utils.StringUtils;

public class NoticeTypeSearchParam extends SearchParam{

  	private String noticeTypeNid;
  	private String noticeTypeSms;
  	private String noticeTypeEmail;
  	private String noticeTypeMessage;
  	
    public String getNoticeTypeNid() {
		return noticeTypeNid;
	}

	public void setNoticeTypeNid(String noticeTypeNid) {
		this.noticeTypeNid = noticeTypeNid;
	}

	public String getNoticeTypeSms() {
		return noticeTypeSms;
	}

	public void setNoticeTypeSms(String noticeTypeSms) {
		this.noticeTypeSms = noticeTypeSms;
	}

	public String getNoticeTypeEmail() {
		return noticeTypeEmail;
	}

	public void setNoticeTypeEmail(String noticeTypeEmail) {
		this.noticeTypeEmail = noticeTypeEmail;
	}

	public String getNoticeTypeMessage() {
		return noticeTypeMessage;
	}

	public void setNoticeTypeMessage(String noticeTypeMessage) {
		this.noticeTypeMessage = noticeTypeMessage;
	}

	/**
	 * 将所有的参数信息封装成Map
	 * @return
	 */
	@Override
	public Map toMap(){
		
	    if(!StringUtils.isBlank(noticeTypeNid)){
			map.put("noticeTypeNid", noticeTypeNid);
		}
	    	
	    if(!StringUtils.isBlank(noticeTypeSms)){
	    	map.put("noticeTypeSms", noticeTypeSms);
	    }
	    if(!StringUtils.isBlank(noticeTypeEmail)){
	    	map.put("noticeTypeEmail", noticeTypeEmail);
	    }
	    if(!StringUtils.isBlank(noticeTypeMessage)){
	    	map.put("noticeTypeMessage", noticeTypeMessage);
	    }
	    
		return map;
		
	}

	@Override
	public String getSearchParamSql() {

		StringBuffer sb = new StringBuffer();
		
		if(!StringUtils.isBlank(noticeTypeNid )&&!"all".equalsIgnoreCase(noticeTypeNid)){
			sb.append(" and nid = '").append(noticeTypeNid).append("' ");
		}
		//因为这里的值是一个类型列里不同的值，而且可以同时查询，只能用or拼接SQL条件
	    if (!StringUtils.isBlank(noticeTypeSms) || !StringUtils.isBlank(noticeTypeEmail) || !StringUtils.isBlank(noticeTypeMessage)){
	    	sb.append(" and ( ");
			if(!StringUtils.isBlank(noticeTypeSms)&&"1".equalsIgnoreCase(noticeTypeSms)){
				sb.append("  notice_type = 1 or ");
			}
			
			if(!StringUtils.isBlank(noticeTypeEmail)&&"1".equalsIgnoreCase(noticeTypeEmail)){
				sb.append("  notice_type = 2 or ");
			}
			
			if(!StringUtils.isBlank(noticeTypeMessage)&&"1".equalsIgnoreCase(noticeTypeMessage)){
				sb.append("  notice_type = 3 or ");
			}
			sb.append(" 1=2)");
	    }
		return sb.toString();
	}

}
