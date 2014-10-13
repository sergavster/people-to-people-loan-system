package com.p2psys.context;

import com.p2psys.util.StringUtils;

public enum AuditType {
	realnameAudit,emailAudit,phoneAudit,videoAudit,sceneAudit,
	cancelRealnameAudit,cancelPhoneAudit,cancelVideoAudit,cancelSceneAudit;
	
	public static AuditType getAuditType(String name){
		name=StringUtils.isNull(name);
		if(name.equals("realname")){
			return realnameAudit;
		}else if(name.equals("email")){
			return emailAudit;
		}else if(name.equals("phone")){
			return phoneAudit;
		}else if(name.equals("video")){
			return videoAudit;
		}else if(name.equals("scene")){
			return sceneAudit;
		}else if(name.equals("cancelRealname")){
			return AuditType.cancelRealnameAudit;
		}else if(name.equals("cancelPhone")){
			return cancelPhoneAudit;
		}else if(name.equals("cancelVideo")){
			return cancelVideoAudit;
		}else if(name.equals("cancelScene")){
			return cancelSceneAudit;
		}else{
			return null;
		}
	}
	
}
