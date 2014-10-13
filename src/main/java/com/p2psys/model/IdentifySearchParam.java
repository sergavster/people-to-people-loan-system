package com.p2psys.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.tuckey.web.filters.urlrewrite.utils.StringUtils;

import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.common.enums.EnumRuleStatus;
import com.p2psys.context.Global;
import com.p2psys.util.DateUtils;

public class IdentifySearchParam extends SearchParam{

	// v1.6.6.2 RDPROJECT-373 lhm 2013-10-24 start
	//private int real_status = -1;
	private String real_status;
	// v1.6.6.2 RDPROJECT-373 lhm 2013-10-24 start
	private String phone_status;
	private String video_status;
	private String scene_status;
	private String vip_status;
	private String dotime1;
	private String dotime2;
	 //v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 start
  	private String verify_start_time;
  	private String verify_end_time;
  	private int verify_type;
  	//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 end
  //v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 start
  	public String getVerify_start_time() {
  		return verify_start_time;
  	}
  	public void setVerify_start_time(String verify_start_time) {
  		this.verify_start_time = verify_start_time;
  	}
  	public String getVerify_end_time() {
  		return verify_end_time;
  	}
  	public void setVerify_end_time(String verify_end_time) {
  		this.verify_end_time = verify_end_time;
  	}
  	public int getVerify_type() {
  		return verify_type;
  	}
  	public void setVerify_type(int verify_type) {
  		this.verify_type = verify_type;
  	}
  	//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 start
	private Map map = new HashMap();

	public String getDotime1() {
		return dotime1;
	}
	public void setDotime1(String dotime1) {
		this.dotime1 = dotime1;
	}
	public String getDotime2() {
		return dotime2;
	}
	public void setDotime2(String dotime2) {
		this.dotime2 = dotime2;
	}
	
	public String getReal_status() {
		return real_status;
	}
	public void setReal_status(String real_status) {
		this.real_status = real_status;
	}
	public String getPhone_status() {
		return phone_status;
	}
	public void setPhone_status(String phone_status) {
		this.phone_status = phone_status;
	}
	public String getVideo_status() {
		return video_status;
	}
	public void setVideo_status(String video_status) {
		this.video_status = video_status;
	}
	public String getScene_status() {
		return scene_status;
	}
	public void setScene_status(String scene_status) {
		this.scene_status = scene_status;
	}
	public String getVip_status() {
		return vip_status;
	}
	public void setVip_status(String vip_status) {
		this.vip_status = vip_status;
	}
	public IdentifySearchParam() {
		super();
	}

	/**
	 * 将所有的参数信息封装成Map
	 * @return
	 */
	@Override
	public Map toMap(){
		// v1.6.6.2 RDPROJECT-373 lhm 2013-10-24 start
		//if(real_status >= 0){
		if(!StringUtils.isBlank(real_status )){
			// v1.6.6.2 RDPROJECT-373 lhm 2013-10-24 end
			map.put("status", real_status+"");
		}
		if(!StringUtils.isBlank(phone_status)){
			map.put("status", phone_status+"");
		}
		if(!StringUtils.isBlank(scene_status)){
			map.put("status", scene_status+"");
		}
		if(!StringUtils.isBlank(vip_status)){
			map.put("status", vip_status+"");
		}
		if(!StringUtils.isBlank(video_status)){
			map.put("status", video_status+"");
		}
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 start
		if(verify_type!=0){
			map.put("verify_type", verify_type+"");
		}
		if(!StringUtils.isBlank(verify_start_time)){
			map.put("verify_start_time", verify_start_time);
		}
		if(!StringUtils.isBlank(verify_end_time)){
			map.put("verify_end_time", verify_end_time);
		}
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 end
		map.put("username", getUsername());
		map.put("realname", getRealname());
		map.put("email", getEmail());
		map.put("kefu_username", getKefu_username());
		map.put("dotime1", this.dotime1);
	    map.put("dotime2", this.dotime2);
	    map.put("qq",qq);
	    //v1.6.7.1 RDPROJECT-354 wcw 2013-11-21 start
	    if(!StringUtils.isBlank(getPhone())){
	    	map.put("phone", getPhone());
	    }
	  //v1.6.7.1 RDPROJECT-354 wcw 2013-11-21 end
		return map;
		
	}

	@Override
	public String getSearchParamSql() {
		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.TIME_HOUR_ENABLE.getValue()));
		int hour_enable = 0;
		if (rule != null && rule.getStatus() == EnumRuleStatus.RULE_STATUS_YES.getValue()) {
			hour_enable = rule.getValueIntByKey("enable");
		}
		StringBuffer sb = new StringBuffer();
		// v1.6.6.2 RDPROJECT-373 lhm 2013-10-24 start
		//if(real_status >= 0){
		if(!StringUtils.isBlank(real_status )&&!"0".equals(real_status)){
			sb.append(" and p1.real_status=").append(real_status);
		}
		// v1.6.6.2 RDPROJECT-373 lhm 2013-10-24 end
		if(!StringUtils.isBlank(phone_status)&&!"0".equals(phone_status)){
			sb.append(" and p1.phone_status=").append(phone_status);
		}
		if(!StringUtils.isBlank(scene_status)&&!"0".equals(scene_status)){
			sb.append(" and p1.scene_status=").append(scene_status);
		}
		if(!StringUtils.isBlank(video_status)&&!"0".equals(video_status)){
			sb.append(" and p1.video_status=").append(video_status);
		}
		if(!StringUtils.isBlank(vip_status)&&!"0".equals(vip_status)){
			sb.append(" and p4.vip_status=").append(vip_status);
		}
		if(!StringUtils.isBlank(getUsername())){
			sb.append(" and p1.username like '%").append(getUsername().trim()).append("%'");
		}
		if(!StringUtils.isBlank(getRealname())){
			sb.append(" and p1.realname like '%").append(getRealname().trim()).append("%'");
		}
		if(!StringUtils.isBlank(getEmail())){
			sb.append(" and p1.email like '%").append(getEmail().trim()).append("%'");
		}
		if (!StringUtils.isBlank(qq)) {
			sb.append(" and p1.qq like '%").append(getQq().trim()).append("%'");
		}		
		if(!StringUtils.isBlank(getKefu_username())){
			sb.append(" and p4.kefu_username like '%").append(getKefu_username().trim()).append("%'");
		}
		if(!StringUtils.isBlank(getTypename())){
			sb.append(" and p8.name like '%").append(getTypename().trim()).append("%'");
		}
		if(!StringUtils.isBlank(getCard_id())){
			sb.append(" and p1.card_id like '%").append(getCard_id().trim()).append("%'");
		}
		if(!StringUtils.isBlank(getUserPhone())){
			sb.append(" and p1.phone like '%").append(getUserPhone().trim()).append("%'");
		}
		if(!StringUtils.isBlank(getPhone())){
			sb.append(" and p1.phone like '%").append(getPhone().trim()).append("%'");
		}
		String dotimeStr1=null,dotimeStr2=null;
		try {
			dotimeStr1=Long.toString(DateUtils.valueOf(dotime1).getTime()/1000);
		} catch (Exception e) {
			dotimeStr1="";
		}
		try {
			Date d=DateUtils.valueOf(dotime2);
			if(hour_enable!=1){
			d=DateUtils.rollDay(d, 1);	
			}
			dotimeStr2=Long.toString(d.getTime()/1000);
		}catch (Exception e) {
			dotimeStr2="";
		}
		if(!StringUtils.isBlank(dotimeStr1)) {
			sb.append(" and p1.addtime>="+dotimeStr1+" ");
		}
		if(!StringUtils.isBlank(dotimeStr2)){
			sb.append(" and p1.addtime<="+dotimeStr2+" ");
		}
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 start
		String verifyStartTimeStr=null,verifyEndTimeStr=null;
		try {
			verifyStartTimeStr=Long.toString(DateUtils.valueOf(verify_start_time).getTime()/1000);
		} catch (Exception e) {
			verifyStartTimeStr="";
		}
		try {
			Date d=DateUtils.valueOf(verify_end_time);
			if(hour_enable!=1){
			d=DateUtils.rollDay(d, 1);	
			}
			verifyEndTimeStr=Long.toString(d.getTime()/1000);
		} catch (Exception e) {
			verifyEndTimeStr="";
		}
		if(!StringUtils.isBlank(verifyStartTimeStr)) {
			if(verify_type==1){
				sb.append(" and p4.phone_verify_time>"+verifyStartTimeStr+" ");

			}else if(verify_type==2){
				sb.append(" and p4.realname_verify_time>"+verifyStartTimeStr+" ");

			}else if(verify_type==3){
				sb.append(" and p4.video_verify_time>"+verifyStartTimeStr+" ");

			}else if(verify_type==4){
				sb.append(" and p4.scene_verify_time>"+verifyStartTimeStr+" ");
			}else if(verify_type==5){
				sb.append(" and p.vip_verify_time>"+verifyStartTimeStr+" ");
			}
			
		}
		if(!StringUtils.isBlank(verifyEndTimeStr)){
			if(verify_type==1){
				sb.append(" and p4.phone_verify_time<"+verifyEndTimeStr+" ");

			}else if(verify_type==2){
				sb.append(" and p4.realname_verify_time<"+verifyEndTimeStr+" ");

			}else if(verify_type==3){
				sb.append(" and p4.video_verify_time<"+verifyEndTimeStr+" ");

			}else if(verify_type==4){
				sb.append(" and p4.scene_verify_time<"+verifyEndTimeStr+" ");
			}else if(verify_type==5){
				sb.append(" and p.vip_verify_time<"+verifyEndTimeStr+" ");
			}
		}
		//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 end
		return sb.toString();
	}

}
