package com.p2psys.util.sms;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.context.Global;
import com.p2psys.model.RuleModel;
import com.p2psys.util.HttpUtils;

public class YndSmsPortalImpl implements SmsPortal {
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
		
		if(null == Global.getRule(EnumRuleNid.YND_SMS.getValue())){
			return "没有通道信息，无法发送。";
		}
		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.YND_SMS.getValue()));
		
		String username = rule.getValueStrByKey("sms_username");
		String password = rule.getValueStrByKey("sms_password");
		String smsUrl = rule.getValueStrByKey("notice_sms");
		
//		String username=StringUtils.isNull(Global.getValue("sms_username"));
//		String password=StringUtils.isNull(Global.getValue("sms_password"));
//		String smsUrl=StringUtils.isNull(Global.getValue("notice_sms"));
		//v1.6.7.2  RDPROJECT-556 liukun 2013-12-10 end
		
		//action=send&userid=12&account=账号&password=密码&mobile=15023239810,13527576163&content=内容&sendTime=&extno=
				
				
		String url=smsUrl+"&account="+username+"&password="+password+"&mobile="+phone+"&content="+content+"&sendTime=&extno=";
		logger.debug("url:"+url);
		String s = HttpUtils.getHttpResponse(url);
		logger.debug("return:"+s);
		String result = "";
		SAXBuilder builder = new SAXBuilder();
		try {

			StringReader read = new StringReader(s);

			InputSource source = new InputSource(read);
			Document doc = builder.build(source);

			Element rootEl = doc.getRootElement();
			String returnstatus = rootEl.getChildText("returnstatus");
			String message = rootEl.getChildText("message");
			String remainpoint = rootEl.getChildText("remainpoint");
			String taskID = rootEl.getChildText("taskID");
			String successCounts = rootEl.getChildText("successCounts");

			if (returnstatus.equalsIgnoreCase("Success") && message.equalsIgnoreCase("ok")){
				result = "ok";
			}else{
				result = message;
			}
		//v1.6.7.2  RDPROJECT-593 liukun 2013-12-13 start
		/*} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		} catch (Exception e) {
			e.printStackTrace();
			result = "未知错误";
		}
		//v1.6.7.2  RDPROJECT-593 liukun 2013-12-13 end
		
		return result;
	}

	@Override
	public Map<String, Integer> getUseInfo() {
		
		//v1.6.7.2  RDPROJECT-556 liukun 2013-12-10 start
		if(null == Global.getRule(EnumRuleNid.YND_SMS.getValue())){
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			map.put("usenum", 0);
			map.put("usednum", 0);

			return map;
		}
		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.YND_SMS.getValue()));
		
		String username = rule.getValueStrByKey("sms_username");
		String password = rule.getValueStrByKey("sms_password");
		String smsuseUrl = rule.getValueStrByKey("use_sms_url");
		
		/*String username=StringUtils.isNull(Global.getValue("sms_username"));
		String password=StringUtils.isNull(Global.getValue("sms_password"));
		String smsuseUrl=StringUtils.isNull(Global.getValue("use_sms_url"));
		String smsusedUrl=StringUtils.isNull(Global.getValue("used_sms_url"));*/
		
		//v1.6.7.2  RDPROJECT-556 liukun 2013-12-10 end
		
		
		String useurl=smsuseUrl+"&account="+username+"&password="+password;
		
		logger.debug("url:"+useurl);
		String s =  HttpUtils.getHttpResponse(useurl);
		logger.debug("return:"+s);
		
		int usenum = 0;
		int usednum = 0;
		
		String result = "ok";
		SAXBuilder builder = new SAXBuilder();
		try {

			StringReader read = new StringReader(s);

			InputSource source = new InputSource(read);
			Document doc = builder.build(source);
			
			Element rootEl = doc.getRootElement();
			String returnstatus = rootEl.getChildText("returnstatus");
			String message = rootEl.getChildText("message");
			String payinfo = rootEl.getChildText("payinfo");
			String overage = rootEl.getChildText("overage");
			String sendTotal = rootEl.getChildText("sendTotal");

			if (returnstatus.equalsIgnoreCase("Sucess") && message.equalsIgnoreCase("")){
				usenum = Integer.valueOf(overage);
			}else{
				result = message;
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("usenum", usenum);
		map.put("usednum", usednum);
		

		return map;
	}
	
	//v1.6.7.2  RDPROJECT-556 liukun 2013-12-10 start
	@Override
	public String getSPName() { 
		return "Ynd";
	}
	//v1.6.7.2  RDPROJECT-556 liukun 2013-12-10 end

}
