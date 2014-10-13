package com.p2psys.util;

import java.util.Random;

public class RandomUtil {

	/**
	 * 获取从a至z，长度为length随机数
	 * @return
	 */
	public static String getRandomStr(int length){
		String base="abcdefghijklmnopqrstuvwxyz";
		Random random = new Random();   
	    StringBuffer sb = new StringBuffer();   
	    for (int i = 0; i < length; i++) {   
	        int number = random.nextInt(base.length());   
	        sb.append(base.charAt(number));   
	    }   
	    return sb.toString();   
	}
	/**
	 * 获取范围内int值
	 * @param 获取范围内int值
	 * @return
	 */
	public static int getRandomRange(int max,int min){
		return (int)(Math.random()*(max-min)+min);
	}
	/**
	 * 获取随机长度随机字符
	 * @param length base
	 * @return
	 */
	public static String getRandomString(int length,String base) { //length表示生成字符串的长度
	    Random random = new Random();   
	    StringBuffer sb = new StringBuffer();   
	    for (int i = 0; i < length; i++) {   
	        int number = random.nextInt(base.length());   
	        sb.append(base.charAt(number));   
	    }   
	    return sb.toString();   
	 }  
	/**
	 * 获取随机长度随机字符
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length) { //length表示生成字符串的长度
	    String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";   
	    Random random = new Random();   
	    StringBuffer sb = new StringBuffer();   
	    for (int i = 0; i < length; i++) {   
	        int number = random.nextInt(base.length());   
	        sb.append(base.charAt(number));   
	    }   
	    return sb.toString();   
	 }  
	/**
	 * 获取随机长度随机数字
	 * @param length
	 * @return
	 */
	public static String getRandomNumString(int length) { //length表示生成字符串的长度
	    String base = "0123456789";   
	    Random random = new Random();   
	    StringBuffer sb = new StringBuffer();   
	    for (int i = 0; i < length; i++) {   
	        int number = random.nextInt(base.length());   
	        sb.append(base.charAt(number));   
	    }   
	    return sb.toString();   
	 }  
	/**
	 * 返回随机数组
	 * @param start 开始值
	 * @param end  结束值
	 * @return
	 */
	public static int[] getRangRandom(int start, int end) {
		return getRangRandom(start, end, end - start + 1);
	}
	
	/**
	 * 返回指定范围指定个数的不重复随机数。
	 * @param start
	 * @param end
	 * @param num
	 * @return
	 */
	public static int[] getRangRandom(int start, int end, int num) {
		
		int length = end - start + 1;
		//参数不合法
		if (length < 1 || num > length) {
			return null;
		}
		else {
			
			int[] numbers = new int[length];
			int[] result = new int[num];
			
			//循环赋初始值
			for (int i = 0; i < length; i ++) {
				numbers[i] = i + start;
			}
			
			Random random = new Random();
			
			//取randomMax次数
			for (int i = 0; i < num; i ++) {
				
				//随机获取取数的位置
				int m = random.nextInt(length - i) + i;
				
				result[i] = numbers[m];
				
				//交换位置
				int temp = numbers[m];
				numbers[m] = numbers[i];
				numbers[i] = temp;
			}
			
			return result;
		}
		
	}
	
	
	public static void main(String[] avgs) {
		
//		Long a = System.currentTimeMillis();
//		int[] aaa = getRangRandom(0,500000,500001);
//		
//		Long b = System.currentTimeMillis();
//		
//		System.out.print(b - a);
//		System.out.print(" ");
//		
//		int num = 0;
//		
//		for (int i = 0; i < 500001; i ++) {
//				if (aaa[i] == i)
//					num ++;
//		}
		
		
		System.out.print(getRandomStr(5));
		//int[] aaa = getRangRandomShuffle(100,200);
		//System.out.print(aaa);
	}
}
