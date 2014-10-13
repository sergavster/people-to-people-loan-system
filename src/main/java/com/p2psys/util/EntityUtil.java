package com.p2psys.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.p2psys.report.model.MonthTenderModel;

/**
 * 实体公用方法 
 
 * @version 1.0
 * @since 2013-11-13
 */
public class EntityUtil {

	/**
	 * 获取自定义注解的内容
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String[] getExcelTitle(Class clazz){
		
		Field[] fields = clazz.getDeclaredFields();
		// 把clazz这一类有利用到@ExcelTitle的全部方法保存到List中去
		List<Field> list = new ArrayList<Field>();
		for (int i = 0; i < fields.length; i++) {
			boolean otherFlag = fields[i].isAnnotationPresent(AnnotExcelTitle.class);
			if (otherFlag) {
				fields[i].setAccessible(true);
				list.add(fields[i]);
			}
		}
		String[] names=new String[fields.length];
		for (int i = 0 ; i < list.size() ; i++) {
			Field f = list.get(i);
			AnnotExcelTitle name = f.getAnnotation(AnnotExcelTitle.class);
			names[i]=name.value();
		}
		return names;
	}
	
	/**
	 * 根据class取出所有属性的string数组
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String[] findFields(Class clazz){
		Field[] fs=clazz.getDeclaredFields();
		List<String> list = new ArrayList<String>();
		for(int i=0;i<fs.length;i++){
			String name = fs[i].getName();
			if(name != null && !"serialVersionUID".equals(name)){
				list.add(name);
			}
		}
		if(list.size() > 0){
			String[] names=new String[list.size()];
			for(int i = 0; i < list.size(); i++) {
				names[i] = list.get(i);
			}
			return names;
		}
		return null;
	}
	
	public static void main(String[] args) {
		String[] aa =  findFields(MonthTenderModel.class);
		for(String a : aa){
			//System.out.println(a);
		}
		
	}
	
}
