package com.p2psys.util;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.p2psys.context.Global;
/**
 * 实体类
 
 * @version 1.0    
 * @since 2013-12-02 
 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部
 */
public class StringUtils {
	/**
	 * 如果str为null，返回“”,否则返回str
	 * 
	 * @param str
	 * @return
	 */
	public static String isNull(String str) {
		if (str == null) {
			return "";
		}
		// v1.6.7.1 去除参数前后空格 xx 2013-11-04 start
		return str.trim();
		// v1.6.7.1 去除参数前后空格 xx 2013-11-04 end
	}

	public static String isNull(Object o) {
		if (o == null) {
			return "";
		}
		String str="";
		if(o instanceof String){
			str=(String)o;
		}else{
			str=o.toString();
		}
		return str;
	}
	
	// v1.6.5.3 RDPROJECT-147 xx 2013.09.11 start
	/**
	 * 检验手机号
	 * @param phone
	 * @return
	 */
	public static boolean isPhone(String phone){
		phone = isNull(phone);
		Pattern regex = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher matcher = regex.matcher(phone);
		boolean isMatched = matcher.matches();
		return isMatched;
	}
	// v1.6.5.3 RDPROJECT-147 xx 2013.09.11 end

	// v1.6.6.2 注册时加强真实姓名验证 start
	/**
	 * 检查是否全中文，返回true 表示是 反之为否
	 * @param realname
	 * @return
	 */
	public static boolean isChinese(String realname){
		realname = isNull(realname);
		// v1.6.7.1 RDPROJECT-423 xx 2013-11-15 start
		Pattern regex = Pattern.compile("[\\u4e00-\\u9fa5]{2,25}");
		// v1.6.7.1 RDPROJECT-423 xx 2013-11-15 end
		Matcher matcher = regex.matcher(realname);
		boolean isMatched = matcher.matches();
		return isMatched;
	}
	// v1.6.6.2 注册时加强真实姓名验证 end
	
	/**
	 * 检查email是否是邮箱格式，返回true表示是，反之为否
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		email = isNull(email);
		Pattern regex = Pattern
				.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher matcher = regex.matcher(email);
		boolean isMatched = matcher.matches();
		return isMatched;
	}
	
	/**
	 * 检查身份证的格式，返回true表示是，反之为否
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isCard(String cardId) {
		cardId = isNull(cardId);
		//身份证正则表达式(15位) 
		Pattern isIDCard1=Pattern.compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$"); 
		//身份证正则表达式(18位) 
		Pattern isIDCard2=Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$"); 
		Matcher matcher1= isIDCard1.matcher(cardId);
		Matcher matcher2= isIDCard2.matcher(cardId);
		boolean isMatched = matcher1.matches()||matcher2.matches();
		return isMatched;
	}

	/**
	 * 判断字符串是否为整数
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str) {
		if (isEmpty(str)) {
			return false;
		}
		Pattern regex = Pattern.compile("\\d*");
		Matcher matcher = regex.matcher(str);
		boolean isMatched = matcher.matches();
		return isMatched;
	}

	/**
	 * 判断字符串是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		if (isEmpty(str)) {
			return false;
		}

		Pattern regex = Pattern.compile("\\d*(.\\d*)?");
		Matcher matcher = regex.matcher(str);
		boolean isMatched = matcher.matches();
		return isMatched;
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str)) {
			return true;
		}
		return false;
	}

	/**
	 * 首字母大写
	 * 
	 * @param s
	 * @return
	 */
	public static String firstCharUpperCase(String s) {
		StringBuffer sb = new StringBuffer(s.substring(0, 1).toUpperCase());
		sb.append(s.substring(1, s.length()));
		return sb.toString();
	}
	
	public static String hideChar(String str,int len){
		if(str==null) return null;
		char[] chars=str.toCharArray();
		for(int i=1;i>chars.length-1;i++){
			if(i<len){
				chars[i]='*';
			}
		}
		str=new String(chars);
		return str;
	}
	
	public static String hideFirstChar(String str,int len){
		if(str==null) return null;
		char[] chars=str.toCharArray();
		if(str.length()<=len){
			for(int i=0;i<chars.length;i++){
				chars[i]='*';
			}
		}else{
			for(int i=0;i<1;i++){
				chars[i]='*';
			}
		}
		str=new String(chars);
		return str;
	}
	public static String hideLastChar(String str,int len){
		if(str==null) return null;
		char[] chars=str.toCharArray();
		if(str.length()<=len){
			for(int i=0;i<chars.length;i++){
				chars[i]='*';
			}
		}else{
			for(int i=chars.length-1;i>chars.length-len-1;i--){
				chars[i]='*';
			}
		}
		str=new String(chars);
		return str;
	}
	
	/**
	 * 
	 * @return
	 */
	public static String format(String str,int len){
		if(str==null) return "-";
		if(str.length()<=len){
			int pushlen=len-str.length();
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<pushlen;i++){
				sb.append("0");
			}
			sb.append(str);
			str=sb.toString();
		}else{
			String newStr=str.substring(0, len);
			str=newStr;
		}
		return str;
	}
	
	public static String contact(Object[] args){
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<args.length;i++){
			sb.append(args[i]);
			if(i<args.length-1){
				sb.append(",");
			}
		}
		return sb.toString();
	}
	
	/**
	 * 是否包含在以“，”隔开字符串内
	 * @param s
	 * @param type
	 * @return
	 */
	public static boolean isInSplit(String s,String type){
		if(isNull(s).equals("")){
			return false;
		}
		List<String> list=Arrays.asList(s.split(","));
		if(list.contains(type)){
			return true;
		}
		return false;
	}
	
	public static boolean isBlank(String str){
		return StringUtils.isNull(str).equals("");
	}
	
	public synchronized static String generateTradeNO(long userid,String type){
		String s;
		s = type + userid + getFullTimeStr();
		return s;
	}
	
	public static String getFullTimeStr(){
		String s=DateUtils.dateStr3(Calendar.getInstance().getTime());
		return s;
	}

	public static String array2Str(Object[] arr){
		StringBuffer s=new StringBuffer();
		for(int i=0;i<arr.length;i++){
			s.append(arr[i]);
			if(i<arr.length-1){
				s.append(",");
			}
		}
		return s.toString();
	}
	
	public static String array2Str(int[] arr){
		StringBuffer s=new StringBuffer();
		for(int i=0;i<arr.length;i++){
			s.append(arr[i]);
			if(i<arr.length-1){
				s.append(",");
			}
		}
		return s.toString();
	}
	
	
	/**
	 * 指定起始位置字符串隐藏
	 * 
	 * @param str
	 * @param index1
	 * @param index2
	 * @return
	 */
	public static String hideStr(String str, int index1, int index2) {
		if (str == null)
			return null;
		String str1 = str.substring(index1, index2);
		String str2 = str.substring(index2);
		String str3 = "";
		if (index1 > 0) {
			str1 = str.substring(0, index1);
			str2 = str.substring(index1, index2);
			str3 = str.substring(index2);
		}
		char[] chars = str2.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			chars[i] = '*';
		}
		str2 = new String(chars);
		String str4 = str1 + str2 + str3;
		return str4;
	}

	// 四舍五入保留两位小数点
	public static String SetNumberFractionDigits(String str) {
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		return nf.format(Float.valueOf(str));
	}
//	public static String getMonday(String the_rq){
//	     int n=getXC_days(the_rq);
//	     //System.out.println("n="+n);
//	     n=n*-1;
//	     return Q_N_Day(n,the_rq);
//	 }

	 

//	//获取输入日期的星期天日期
//
//	 public static String getSunday(String the_rq){
//	      int n=getXC_days(the_rq);
//	      //System.out.println("n="+n);
//	      n=(6-(n*-1))*-1;
//	      return Q_N_Day(n,the_rq);
//	  }

//	// 获得输入日期与本周一相差的天数
//	 public static int getXC_days(String rq){
//	     SimpleDateFormat formatYMD=new SimpleDateFormat("yyyy-MM-dd");//formatYMD表示的是yyyy-MM-dd格式
//	     SimpleDateFormat formatD=new SimpleDateFormat("E");//"E"表示"day in week"
//	     Date d=null;
//	     String weekDay="";
//	     int xcrq=0;
//	     try{
//	         d=formatYMD.parse(rq);//将String 转换为符合格式的日期
//	         weekDay=formatD.format(d);
//	         if(weekDay.equals("星期一")){
//	             xcrq=0;
//	         }else{
//	             if(weekDay.equals("星期二")){
//	                 xcrq=-1;
//	             }else{
//	                 if(weekDay.equals("星期三")){
//	                     xcrq=-2;
//	                 }else{
//	                     if(weekDay.equals("星期四")){
//	                         xcrq=-3;
//	                     }else{
//	                         if(weekDay.equals("星期五")){
//	                             xcrq=-4;
//	                         }else{
//	                             if(weekDay.equals("星期六")){
//	                                 xcrq=-5;
//	                             }else{
//	                                 if(weekDay.equals("星期日")){
//	                                     xcrq=-6;
//	                                 }
//
//	                             }
//
//	                         }
//
//	                     }
//	                 }
//	             }
//	         }
//	     }catch (Exception e){
//	         e.printStackTrace();
//	     }
//	     return xcrq;
//	 }
//
//	 public static String Q_N_Day(int N,String d1){//
//	        String []d2=d1.split("-");
//	        int year=Integer.parseInt(d2[0]);
//	        int month=Integer.parseInt(d2[1]);
//	        int day=Integer.parseInt(d2[2]);
//	        if(day-N<=0){
//	            if(month==1){
//	               year=year-1;
//	               month=12;
//	               day = day + 31-N;
//	            }else{
//	                month=month-1;
//	                if (month == 2) {
//	                    if (year % 4 == 0) {
//	                        day = day + 29-N;
//	                    } else {
//	                        day = day + 28-N;
//	                    }
//	                }else{
//	                    if(month==4||month==6||month==9||month==11){
//	                        day=day+30-N;
//	                    }else{
//	                        day=day+31-N;
//	                    }
//	                }
//	            }
//	        }else{
//	            ///////////////////////////////////////////////////////////////////////////////////
//	            if(month==12){
//	                if((day-N)>31){
//	                    year=year+1;
//	                    month=1;
//	                    day=(day-N)-31;
//	                }else{
//	                    day=day-N;
//	                }
//	            }else{
//	                if (month == 2) {
//	                    if (year % 4 == 0) {
//	                        if((day-N)>29){
//	                            month++;
//	                            day=(day-N)-29;
//	                        }else{
//	                            day=day-N;
//	                        }
//	                    } else {
//	                        if((day-N)>28){
//	                            month++;
//	                            day=(day-N)-28;
//	                        }else{
//	                            day=day-N;
//	                        }
//	                    }
//	                }else{
//	                    if(month==4||month==6||month==9||month==11){
//	                        if((day-N)>30){
//	                            month++;
//	                            day=(day-N)-30;
//	                        }else{
//	                            day=day-N;
//	                        }
//	                    }else{
//	                        if((day-N)>31){
//	                            month++;
//	                            day=(day-N)-31;
//	                        }else{
//	                            day=day-N;
//	                        }
//	                    }
//	                }
//	            }
//
//	 
//
//	            //day=day-N;
//	        }
//	        String str=String.valueOf(year);
//	        if(month<10){
//	            str=str+"-0"+String.valueOf(month);
//	        }else{
//	            str=str+"-"+String.valueOf(month);
//	        }
//	        if(day<10){
//	            str=str+"-0"+String.valueOf(day);
//	        }else{
//	            str=str+"-"+String.valueOf(day);
//	        }
//
//	        return str;
//	    }

	/*public static void main(String[] args) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
		String mondayString=StringUtils.getMonday(df.format(new Date()))+ " 00:00:00";
		String sumdayString=StringUtils.getSunday(df.format(new Date()))+ " 23:59:59";
		String monday=DateUtils.getTime(mondayString)+"";
		String sumday=DateUtils.getTime(sumdayString)+"";
		//System.out.println(monday);
		//System.out.println(sumday);

	}*/
	
	public static String fillTemplet(String templet, String phstr, String[] paras){
		StringBuffer templetSB = new StringBuffer(templet);
		int i = 0;
		while(templetSB.indexOf(phstr) >= 0 && i < paras.length){
			templetSB.replace(templetSB.indexOf(phstr), templetSB.indexOf(phstr)+phstr.length(), paras[i]);
			i++;
		}
		return templetSB.toString();
	}
	//V1.6.6.1 RDPROJECT-226 liukun 2013-09-26 start
	public static String fillTemplet(String template){
		//V1.6.6.1 RDPROJECT-331 liukun 2013-10-12 start
		//模板中的'是非法字符，会导致无法提交，所以页面上用`代替
		template = template.replace('`', '\'');
		//V1.6.6.1 RDPROJECT-331 liukun 2013-10-12 end
		
		Map<String,Object> data=Global.getTransfer();
		try {
			return FreemarkerUtil.renderTemplate(template, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	//V1.6.6.1 RDPROJECT-226 liukun 2013-09-26 end
	
	//V1.6.5.3 RDPROJECT-142 liukun 2013-09-11 start
	public static int[] strarr2intarr(String[] strarr){
		int[] result = new int[strarr.length];
		for(int i=0;i<strarr.length;i++)
		{
		   result[i] = Integer.parseInt(strarr[i]);
		}
		return result;
	}
	//V1.6.5.3 RDPROJECT-142 liukun 2013-09-11 end
	
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-12 start
	public static String fillTemplet(String template, Map<String, Object> sendData){
		//V1.6.6.1 RDPROJECT-331 liukun 2013-10-12 start
		//模板中的'是非法字符，会导致无法提交，所以页面上用`代替
		template = template.replace('`', '\'');
		//V1.6.6.1 RDPROJECT-331 liukun 2013-10-12 end
		
		try {
			return FreemarkerUtil.renderTemplate(template, sendData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-12 end
	
	/**
	 * 大写字母转成“_”+小写
	 * @param str
	 * @return
	 */
	public static String toUnderline(String str){
		char[] charArr=str.toCharArray();
		StringBuffer sb=new StringBuffer();
		sb.append(charArr[0]);
		for(int i=1;i<charArr.length;i++){
			if(charArr[i]>='A'&&charArr[i]<='Z'){
				sb.append('_').append(charArr[i]);
			}else{
				sb.append(charArr[i]);
			}  
		}
		return sb.toString().toLowerCase();
	}
}
