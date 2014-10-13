package com.p2psys.util;

import com.p2psys.context.Global;

public class MessageUtils {
	public static String MessageRemindRepay(String repayday, String borrowname,
			String order) {
		String mobileMessage = Global.getValue("webname") + "提醒您，您申请的借款:"
				+ borrowname + "第:" + order + "期的还款日为:" + repayday
				+ "，请及时还款。如有问题请及时联系" + Global.getValue("webname")
				+ Global.getValue("fuwutel") + "【" + Global.getValue("webname")
				+ "】";
		return mobileMessage;
	}
}
