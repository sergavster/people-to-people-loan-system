package com.p2psys.util.sms;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.context.Global;
import com.p2psys.model.RuleModel;
import com.p2psys.util.HttpUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;

public class ErongduSmsPortalImpl implements SmsPortal {
	private Logger logger=Logger.getLogger(ErongduSmsPortalImpl.class);
	
	@Override
	public String send(String phone, String content) {
		try {
			content=URLEncoder.encode(content,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			//need do nothing
			e.printStackTrace();
			logger.warn("短信内容转换失败。");
		}
		
		//v1.6.7.2  RDPROJECT-556 liukun 2013-12-10 start
		
		if(null == Global.getRule(EnumRuleNid.ERONGDU_SMS.getValue())){
			return "没有通道信息，无法发送。";
		}
		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.ERONGDU_SMS.getValue()));
		
		String username = rule.getValueStrByKey("sms_username");
		String password = rule.getValueStrByKey("sms_password");
		String smsUrl = rule.getValueStrByKey("notice_sms");
		
//		String username=StringUtils.isNull(Global.getValue("sms_username"));
//		String password=StringUtils.isNull(Global.getValue("sms_password"));
//		String smsUrl=StringUtils.isNull(Global.getValue("notice_sms"));
		//v1.6.7.2  RDPROJECT-556 liukun 2013-12-10 end
		String url=smsUrl+"?username="+username+"&password="+password+"&mobile="+phone+"&content="+content;
		logger.debug("url:"+url);
		String s = HttpUtils.getHttpResponse(url);
		//logger.debug("发送短信结果:"+new String(s.getBytes("UTF-8")));
		//logger.debug("发送短信结果:"+new String(s.getBytes("GBK")));
		int ret=NumberUtils.getInt(StringUtils.isNull(s));
		String result = "ok";

		if (ret <= 0){
			switch (ret) {
			case -1:
				result = "短信条数已用完";
				break;
			case -2:
				result = "登陆失败";
				break;
			default:
				result = "其他异常错误";
				break;
			}
		}
		return result;
	}

	@Override
	public Map<String, Integer> getUseInfo() {
		//v1.6.7.2  RDPROJECT-556 liukun 2013-12-10 start
		if(null == Global.getRule(EnumRuleNid.ERONGDU_SMS.getValue())){
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			map.put("usenum", 0);
			map.put("usednum", 0);

			return map;
		}
		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.ERONGDU_SMS.getValue()));
		
		String username = rule.getValueStrByKey("sms_username");
		String password = rule.getValueStrByKey("sms_password");
		String smsuseUrl = rule.getValueStrByKey("use_sms_url");
		String smsusedUrl = rule.getValueStrByKey("used_sms_url");
		
		/*String username=StringUtils.isNull(Global.getValue("sms_username"));
		String password=StringUtils.isNull(Global.getValue("sms_password"));
		String smsuseUrl=StringUtils.isNull(Global.getValue("use_sms_url"));
		String smsusedUrl=StringUtils.isNull(Global.getValue("used_sms_url"));*/
		
		//v1.6.7.2  RDPROJECT-556 liukun 2013-12-10 end
		
		String useurl=smsuseUrl+"&username="+username+"&password="+password;
		String usedurl=smsusedUrl+"&username="+username+"&password="+password;
		
		logger.debug("url:"+useurl);
		logger.debug("url:"+usedurl);
		String usereturn =  HttpUtils.getHttpResponse(useurl);
		String usedreturn = HttpUtils.getHttpResponse(usedurl);
		
		int usenum = NumberUtils.getInt(StringUtils.isNull(usereturn));
		int usednum = NumberUtils.getInt(StringUtils.isNull(usedreturn));
	
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("usenum", usenum);
		map.put("usednum", usednum);

		return map;

	}

	//v1.6.7.2  RDPROJECT-556 liukun 2013-12-10 start
	@Override
	public String getSPName() {
		return "Erongdu";
	}
	//v1.6.7.2  RDPROJECT-556 liukun 2013-12-10 end

		
		


}
