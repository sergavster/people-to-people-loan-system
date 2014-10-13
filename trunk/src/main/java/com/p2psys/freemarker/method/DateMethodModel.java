package com.p2psys.freemarker.method;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

/**
 * 
 
 * @date 2012-7-13-上午11:09:22
 * @version
 * 
 *   (c)</b> 2012-51-<br/>
 * 
 */
public class DateMethodModel implements TemplateMethodModel {

	public Object exec(List args) throws TemplateModelException {
		String arg1="";
		String format = "yyyy-MM-dd HH:mm:ss";
		String s = "";
		if (null == args) {
			return "";
		} else {
			if (1 == args.size()) {
				if (args.get(0) == null || args.get(0).equals("")) {
					return "";
				}else if(args.get(0).equals("now")){
					return System.currentTimeMillis()/1000+"";
				}else{
					
				}
			}else if (2 == args.size()) {
				if (args.get(1) != null && !args.get(1).equals("")) {
					format = (String) args.get(1);
				}
			}else if (3 == args.size()) {
				if (args.get(2) != null && args.get(2).equals("vip")) {
					return vip((String) args.get(0), (String) args.get(1));
				}
			}
			if (format.equals("age")) {
				return getAge((String) args.get(0));
			}
			long times = 0;
			try {
				times = Long.parseLong((String) args.get(0));
			} catch (NumberFormatException e) {
				e.printStackTrace();
				times = 0;
			}
			Date d = new Date(times * 1000);
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			try {
				s = sdf.format(d);
			} catch (Exception e) {
				s = "ERROR！";
			}
			return s;
		}
	}

	private String getAge(String age) throws TemplateModelException {
		Map map= DateUtils.getApartTime(
				age,DateUtils.dateStr2(new Date()));
		if (map==null){
			return "-1";
		}
		return map.get("YEAR")+"";
	}

	private String vip(String begin, String end) {
		Map map = DateUtils.getApartTime(
				DateUtils.dateStr2(new  Date((NumberUtils.getLong(begin)*1000))),
				DateUtils.dateStr2(new Date(NumberUtils.getLong(end)*1000)));
		return "还有" + map.get("YEAR") +"年"+ map.get("MONTH")+"月" + map.get("DAY")+"天 到期";
	}
}
