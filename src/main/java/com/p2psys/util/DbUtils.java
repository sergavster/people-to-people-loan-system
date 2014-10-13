package com.p2psys.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * db工具类，根据class通过反射拼接相应的Sql语句
 *
 * 未经授权不得进行修改、复制、出售及商业使用。
 *  (c)</b> 杭州科技有限公司-<br/>
 *
 * @ClassName: DbUtils
 * @Description: 
 
 * @date 2013-11-7 上午11:45:42
 */
public class DbUtils {

	public static List<String> exculdeFiledList=new ArrayList<String>();
	public static String prefix="dw_";
	
	static{
		exculdeFiledList.add("serialVersionUID");
	}
	/**
	 * 拼接insert语句
	 * @param clazz
	 * @param prefix
	 * @return
	 */
	public static String insertSql(Class clazz,String prefix){
		String[] fnames=findFields(clazz);
		StringBuffer names=new StringBuffer();
		StringBuffer params=new StringBuffer();
		for(int i=0;i<fnames.length;i++){
			if(exculdeFiledList.contains(fnames[i])) continue;
			names.append("`").append(StringUtils.toUnderline(fnames[i])).append("`").append(",");
			params.append(":").append(fnames[i]).append(",");
		}
		String nameStr=names.substring(0, names.length()-1);
		String paramStr=params.substring(0, params.length()-1);
		StringBuffer sql = new StringBuffer("INSERT INTO ")
		.append(prefix).append(StringUtils.toUnderline(clazz.getSimpleName()))
		.append("(").append(nameStr)
		.append(") values (")
		.append(paramStr).append(")");
		return sql.toString();
	}

	/**
	 * 根据class取出所有属性的string数组
	 * @param clazz
	 * @return
	 */
	public static String[] findFields(Class clazz){
		Field[] fs=clazz.getDeclaredFields();
		String[] names=new String[fs.length];
		for(int i=0;i<fs.length;i++){
			names[i]=fs[i].getName();
		}
		return names;
	}
	
	/**
	 * 根据排除部分字段，拼接查找的字段名语句,并指定别名
	 * @param clazz
	 * @param alias
	 * @param fields
	 * @return
	 */
	public static String findExcludeFieldSql(Class clazz,String alias,String...fields){
		String[] fnames=findFields(clazz);
		StringBuffer names=new StringBuffer();
		List fieldList=Arrays.asList(fields);
		for(int i=0;i<fnames.length;i++){
			if(exculdeFiledList.contains(fnames[i])) continue;
			if(fieldList.contains(fnames[i])) continue;
			if(!StringUtils.isBlank(alias)){
				names.append(alias).append(".");
			}	
			names.append(StringUtils.toUnderline(fnames[i])).append(",");
		}
		return names.substring(0, names.length()-1);
	}
	/**
	 * 根据排除部分字段，拼接查找的字段名语句
	 * @param clazz
	 * @param fields
	 * @return
	 */
	public static String findExcludeFieldSql(Class clazz,String...fields){
		return findExcludeFieldSql(clazz,null,fields);
	}
	
	/**
	 * 根据指定部分字段，拼接查找的字段名语句,并指定别名
	 * @param clazz
	 * @param alias
	 * @param fields
	 * @return
	 */
	public static String findFieldSql(Class clazz,String alias,String...fields){
		String[] fnames=findFields(clazz);
		StringBuffer names=new StringBuffer();
		List fieldList=Arrays.asList(fields);
		for(int i=0;i<fnames.length;i++){
			if(exculdeFiledList.contains(fnames[i])) continue;
			if(fields.length>0&&!fieldList.contains(fnames[i])) continue;
			if(!StringUtils.isBlank(alias)){
				names.append(alias).append(".");
			}
			names.append("`").append(StringUtils.toUnderline(fnames[i])).append("`").append(",");
		}
		return names.substring(0, names.length()-1);
	}
	
	/**
	 * 根据指定部分字段，拼接查找的字段名语句
	 * @param clazz
	 * @param fields
	 * @return
	 */
	public static String findFieldSql(Class clazz,String...fields){
		return findFieldSql(clazz,null,fields);
	}
	/**
	 * 根据指定部分字段，拼接查找的字段名语句,并指定别名
	 * @param clazz
	 * @param alias
	 * @return
	 */
	public static String findFieldSql(Class clazz,String alias){
		return findFieldSql(clazz,alias,new String[]{});
	}
	/**
	 * 查出所有的字段名语句
	 * @param clazz
	 * @return
	 */
	public static String findFieldSql(Class clazz){
		return findFieldSql(clazz,new String[]{});
	}
	
	public static String findWhereSql(Class clazz,String alias,String...fields){
		StringBuffer params=new StringBuffer();
		for(int i=0;i<fields.length;i++){
			params.append(":");
			if(StringUtils.isBlank(alias)){
				params.append(alias).append("_");
			}
			params.append(fields[i]).append(",");
		}
		String paramStr=params.substring(0, params.length()-1);
		return paramStr;
	}
	
	/**
	 * 拼接Select的sql
	 * @param clazz
	 * @return
	 */
	public static String findSql(Class clazz){
		StringBuffer sb=new StringBuffer("SELECT ");
		sb.append(findFieldSql(clazz,new String[]{}))
		.append(" FROM ")
		.append(prefix).append(StringUtils.toUnderline(clazz.getSimpleName()));
		return sb.toString();
	}
	/**
	 * 拼接Select的sql
	 * @param clazz
	 * @param alias
	 * @return
	 */
	public static String findSql(Class clazz,String alias){
		StringBuffer sb=new StringBuffer("SELECT ");
		sb.append(findFieldSql(clazz,alias,new String[]{}))
		.append(" FROM ")
		.append(prefix).append(StringUtils.toUnderline(clazz.getSimpleName()));
		if(StringUtils.isBlank(alias)) sb.append(" AS ").append(alias);
		return sb.toString();
	}
	
	
}
