package com.p2psys.domain;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.rd.util.DateUtils;

public class GetDomain {

	public static void main(String[] args) {

		String table_name = "dw_status_record";//表名  
		String authorName = "xx";//作者名字  
		String packageOutPath = "com.p2psys.domain";//指定实体生成所在包的路径 
		String folderPath = "E://";//生成文件路径
	    //数据库连接 
		String table_schema = "dev";
		String user = "root";
		String password = "root";
		String url = "jdbc:mysql://localhost/dev?useUnicode=true&characterEncoding=utf8";		
		
		Connection conn = null;
		Statement stmt = null;
		StringBuffer all = new StringBuffer();
		StringBuffer allAttrs = new StringBuffer();
		StringBuffer allMethod = new StringBuffer();
		String className = toUpper(table_name.substring(3)) ;

		try {
			Class.forName("com.mysql.jdbc.Driver"); // 加载mysq驱动
		} catch (ClassNotFoundException e) {
			e.printStackTrace();// 打印出错详细信息
		}

		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			stmt = conn.createStatement();
			String sql = "SELECT COLUMN_NAME, DATA_TYPE, COLUMN_COMMENT FROM INFORMATION_SCHEMA. `COLUMNS` WHERE table_name = '"
					+ table_name + "'  AND table_schema = '" + table_schema + "'";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String columnName = rs.getString("COLUMN_NAME");
				String dataType = rs.getString("DATA_TYPE");
				String columnComment = rs.getString("COLUMN_COMMENT");
				processAttrs(allAttrs, columnName, dataType, columnComment);
				processMethod(allMethod, columnName, dataType, columnComment);
			}
			
			all.append("package " + packageOutPath + ";\r\n");  
			all.append("\r\n");  
	        //注释部分  
			all.append("/**\r\n");  
			all.append(" * "+table_name+" 实体类\r\n");  
			all.append(" * \r\n"); 
			all.append(" 
			all.append(" * @version 1.0\r\n");
			all.append(" * @since "+DateUtils.dateStr2(new Date())+"\r\n");  
			all.append(" */ \r\n");  
	        //实体部分  
			all.append("public class " + className + " {\r\n");
			all.append(allAttrs.toString());
			all.append(allMethod.toString());
			all.append("}\r\n");
            try {  
                FileWriter fw = new FileWriter(folderPath +className+ ".java");  
                PrintWriter pw = new PrintWriter(fw);  
                pw.println(all.toString());  
                pw.flush();  
                pw.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * “_”+小写 转成大写字母
	 * 
	 * @param str
	 * @return
	 */
	private static String toUpper(String str) {
		char[] charArr = str.toCharArray();
		StringBuffer sb = new StringBuffer();
		sb.append(String.valueOf(charArr[0]).toUpperCase());
		for (int i = 1; i < charArr.length; i++) {
			if (charArr[i] == '_') {
				sb.append(String.valueOf(charArr[i + 1]).toUpperCase());
				i = i + 1;
			} else {
				sb.append(charArr[i]);
			}
		}
		return sb.toString();
	}

	/**
	 * 功能：生成属性
	 * 
	 * @param sb
	 * @param columnName
	 * @param dataType
	 * @param columnComment
	 */
	private static void processAttrs(StringBuffer sb, String columnName, String dataType, String columnComment) {
		sb.append("\t/** " + columnComment + " */\r\n");
		sb.append("\tprivate " + sqlType2JavaType(dataType) + " " + columnName + ";\r\n");

	}

	/**
	 * 功能：生成方法
	 * 
	 * @param sb
	 * @param columnName
	 * @param dataType
	 * @param columnComment
	 */
	private static void processMethod(StringBuffer sb, String columnName, String dataType, String columnComment) {

		// get方法
		sb.append("\r\n");
		sb.append("\t/**\r\n");
		sb.append("\t * 获取" + columnComment + "\r\n");
		sb.append("\t * \r\n");
		sb.append("\t * @return " + columnComment + "\r\n");
		sb.append("\t */\r\n");
		sb.append("\tpublic " + sqlType2JavaType(dataType) + " get" + initcap(columnName) + "(){\r\n");
		sb.append("\t\treturn " + columnName + ";\r\n");
		sb.append("\t}\r\n");

		// set方法
		sb.append("\r\n");
		sb.append("\t/**\r\n");
		sb.append("\t * 设置" + columnComment + "\r\n");
		sb.append("\t * \r\n");
		sb.append("\t * @param " + columnName + " 要设置的" + columnComment + "\r\n");
		sb.append("\t */\r\n");
		sb.append("\tpublic void set" + initcap(columnName) + "(" + sqlType2JavaType(dataType) + " " + columnName
				+ "){\r\n");
		sb.append("\t\tthis." + columnName + "=" + columnName + ";\r\n");
		sb.append("\t}\r\n");

	}

	/**
	 * 功能：将输入字符串的首字母改成大写
	 * 
	 * @param str
	 * @return
	 */
	private static String initcap(String str) {
		char[] ch = str.toCharArray();
		if (ch[0] >= 'a' && ch[0] <= 'z') {
			ch[0] = (char) (ch[0] - 32);
		}

		return new String(ch);
	}

	/**
	 * 功能：获得列的数据类型
	 * 
	 * @param sqlType
	 * @return
	 */
	private static String sqlType2JavaType(String sqlType) {

		if (sqlType.equalsIgnoreCase("bit")) {
			return "boolean";
		} else if (sqlType.equalsIgnoreCase("tinyint")) {
			return "byte";
		} else if (sqlType.equalsIgnoreCase("smallint")) {
			return "short";
		} else if (sqlType.equalsIgnoreCase("int")) {
			return "int";
		} else if (sqlType.equalsIgnoreCase("bigint")) {
			return "long";
		} else if (sqlType.equalsIgnoreCase("float")) {
			return "float";
		} else if (sqlType.equalsIgnoreCase("decimal") || sqlType.equalsIgnoreCase("numeric")
				|| sqlType.equalsIgnoreCase("real") || sqlType.equalsIgnoreCase("money")
				|| sqlType.equalsIgnoreCase("smallmoney") || sqlType.equalsIgnoreCase("double")) {
			return "double";
		} else if (sqlType.equalsIgnoreCase("varchar") || sqlType.equalsIgnoreCase("char")
				|| sqlType.equalsIgnoreCase("nvarchar") || sqlType.equalsIgnoreCase("nchar")
				|| sqlType.equalsIgnoreCase("text")) {
			return "String";
		} else if (sqlType.equalsIgnoreCase("datetime")) {
			return "Date";
		} else if (sqlType.equalsIgnoreCase("image")) {
			return "Blod";
		}

		return null;
	}

}
