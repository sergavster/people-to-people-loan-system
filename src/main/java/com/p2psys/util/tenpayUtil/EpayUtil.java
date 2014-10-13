package com.p2psys.util.tenpayUtil;

import java.util.Map;
import java.util.TreeMap;

import com.p2psys.tool.coder.MD5;
//v1.6.6.2 RDPROJECT-283 lhm 2013-10-21 start
//新增类
//v1.6.6.2 RDPROJECT-283 lhm 2013-10-21 end
/**
 * 双乾支付MD5加密方法
 * 
 
 * @version 1.0
 * @since 2013-10-21
 */
public class EpayUtil {
	/**
	 * signMap
	 * 
	 * @param md5Map md5Map
	 * @param securityKey securityKey
	 * @param type 类型（REQ：，RES：）
	 * @return 字符串
	 */
	public String signMap(String[] md5Map, String securityKey, String type) {
		String[] md5ReqMap = new String[] { "MerNo", "BillNo", "Amount", "ReturnURL" };
		String[] md5ResMap = new String[] { "MerNo", "BillNo", "Amount", "Succeed" };
		Map<String, String> map = new TreeMap<String, String>();
		if ("REQ".equals(type)) {
			for (int i = 0; i < md5ReqMap.length; i++) {
				map.put(md5ReqMap[i], md5Map[i]);
			}
		} else if ("RES".equals(type)) {
			for (int i = 0; i < md5ResMap.length; i++) {
				map.put(md5ResMap[i], md5Map[i]);
			}
		}

		MD5 md5 = new MD5();

		String strBeforeMd5 = joinMapValue(map, '&') + md5.getMD5ofStr(securityKey);

		return md5.getMD5ofStr(strBeforeMd5);

	}

	/**
	 * joinMapValue
	 * 
	 * @param map map
	 * @param connector connector
	 * @return 字符串
	 */
	public String joinMapValue(Map<String, String> map, char connector) {
		StringBuffer b = new StringBuffer();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			b.append(entry.getKey());
			b.append('=');
			if (entry.getValue() != null) {
				b.append(entry.getValue());
			}
			b.append(connector);
		}
		return b.toString();
	}
}
