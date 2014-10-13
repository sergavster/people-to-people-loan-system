// v1.6.7.1 RDPROJECT-452 lhm 2013-11-20
package com.p2psys.util.sms;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.context.Global;
import com.p2psys.model.RuleModel;
import com.p2psys.tool.coder.MD5;
import com.p2psys.util.HttpUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;

/**
 * 中国短信网接口（登录地址：http://web.c123.cn）
 * 
 
 * @version 1.0
 * @since 2013-11-20
 */
public class ChinaSmsPortalImpl implements SmsPortal {

	/** 日志 */
	private Logger logger = Logger.getLogger(ChinaSmsPortalImpl.class);

	@Override
	public String send(String phone, String content) {

		// 短信内容
		String strcontent = "";
		try {
			strcontent = URLEncoder.encode(content, "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.warn("短信内容转换失败。");
		}
		//v1.6.7.2  RDPROJECT-556 liukun 2013-12-10 start
		
		if(null == Global.getRule(EnumRuleNid.CHINA_SMS.getValue())){
			return "没有通道信息，无法发送。";
		}
		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.CHINA_SMS.getValue()));
		
		String username = rule.getValueStrByKey("sms_username");
		String password = rule.getValueStrByKey("sms_password");
		String smsUrl = rule.getValueStrByKey("notice_sms");
		
		// 用户账号
		//String uid = StringUtils.isNull(Global.getValue("sms_username"));
		String uid = StringUtils.isNull(username);
		// 用户密码
		MD5 md5 = new MD5();
		//String pwd = md5.getMD5ofStr(StringUtils.isNull(Global.getValue("sms_password"))).toLowerCase();
		String pwd = md5.getMD5ofStr(StringUtils.isNull(password)).toLowerCase();
		// url
		//String smsUrl = StringUtils.isNull(Global.getValue("notice_sms"));
		smsUrl = StringUtils.isNull(smsUrl);
		//v1.6.7.2  RDPROJECT-556 liukun 2013-12-10 end
		StringBuffer url = new StringBuffer();
		url.append(smsUrl).append("?uid=").append(uid).append("&pwd=").append(pwd).append("&mobile=").append(phone)
				.append("&content=").append(strcontent);
		String resultCd = HttpUtils.getHttpResponse(url.toString());

		return getResultName(NumberUtils.getInt(resultCd));
	}

	@Override
	public Map<String, Integer> getUseInfo() {
		//v1.6.7.2  RDPROJECT-556 liukun 2013-12-10 start
		if(null == Global.getRule(EnumRuleNid.CHINA_SMS.getValue())){
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			map.put("usenum", 0);
			map.put("usednum", 0);

			return map;
		}
		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.CHINA_SMS.getValue()));
		
		String username = rule.getValueStrByKey("sms_username");
		String password = rule.getValueStrByKey("sms_password");
		String smsUrl = rule.getValueStrByKey("use_sms_url");
		
		// 剩余短信条数
		int remainNum = 0;
		// 已使用短信条数
		int usedNum = 0;
		// 用户账号
//		String uid = StringUtils.isNull(Global.getValue("sms_username"));
		String uid = StringUtils.isNull(username);
		// 用户密码
		MD5 md5 = new MD5();
//		String pwd = md5.getMD5ofStr(StringUtils.isNull(Global.getValue("sms_password"))).toLowerCase();
		String pwd = md5.getMD5ofStr(StringUtils.isNull(password)).toLowerCase();
		// url
//		String smsUrl = StringUtils.isNull(Global.getValue("use_sms_url"));
		smsUrl = StringUtils.isNull(smsUrl);
		//v1.6.7.2  RDPROJECT-556 liukun 2013-12-10 end
		
		StringBuffer remainUrl = new StringBuffer();
		// 取剩余短信条数
		remainUrl.append(smsUrl).append("?uid=").append(uid).append("&pwd=").append(pwd);
		String resultStrRemain = HttpUtils.getHttpResponse(remainUrl.toString());
		if (resultStrRemain.startsWith("100")) {
			remainNum = NumberUtils.getInt(resultStrRemain.substring(5));
		}
		
//   查看已使用短信条数有问题,暂时不能查看
//		// 取已使用短信条数
//		StringBuffer usedurl = new StringBuffer();
//		usedurl.append(smsUrl).append("?uid=").append(uid).append("&pwd=").append(pwd).append("&cmd=send");
//		String resultStrUsed = HttpUtils.getHttpResponse(remainUrl.toString());
//		if (resultStrUsed.startsWith("100")) {
//			usedNum = NumberUtils.getInt(resultStrUsed.substring(5));
//		}

		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("usenum", remainNum);
		map.put("usednum", usedNum);

		return map;
	}

	/**
	 * 取得状态码说明
	 * 
	 * @param cd 状态码
	 * @return 状态码说明
	 */
	private String getResultName(int cd) {
		String resultName = "";
		switch (cd) {
			case 100:
				resultName = "ok";
				break;
			case 101:
				resultName = "验证失败";
				break;
			case 102:
				resultName = "短信不足";
				break;
			case 103:
				resultName = "操作失败";
				break;
			case 104:
				resultName = "非法字符";
				break;
			case 105:
				resultName = "内容过多";
				break;
			case 106:
				resultName = "号码过多";
				break;
			case 107:
				resultName = "频率过快";
				break;
			case 108:
				resultName = "号码内容空";
				break;
			case 109:
				resultName = "账号冻结";
				break;
			case 110:
				resultName = "禁止频繁单条发送";
				break;
			case 111:
				resultName = "系统暂定发送";
				break;
			case 112:
				resultName = "号码错误";
				break;
			case 113:
				resultName = "定时时间格式不对";
				break;
			case 114:
				resultName = "账号被锁，10分钟后登录";
				break;
			case 115:
				resultName = "连接失败";
				break;
			case 116:
				resultName = "禁止接口发送";
				break;
			case 117:
				resultName = "绑定IP不正确";
				break;
			case 120:
				resultName = "系统升级";
				break;
			default:
				resultName = "其他异常错误";
				break;
		}
		return resultName;
	}
	
	//v1.6.7.2  RDPROJECT-556 liukun 2013-12-10 start
	@Override
	public String getSPName() {
		return "China";
	}
	//v1.6.7.2  RDPROJECT-556 liukun 2013-12-10 end
}
