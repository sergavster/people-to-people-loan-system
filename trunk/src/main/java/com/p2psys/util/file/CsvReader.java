package com.p2psys.util.file;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class CsvReader {
	public static List read(String filePath){
		ArrayList list = new ArrayList();
		try {
//			File csv = new File(filePath); // CSV文件
			
			FileInputStream fis = new FileInputStream(filePath);
			  StringBuffer content = new StringBuffer();
			  DataInputStream in = new DataInputStream(fis);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in, "GBK"));// , "UTF-8"  

//			BufferedReader br = new BufferedReader(new FileReader(csv));

			// 读取直到最后一行
			String line = "";
			while ((line = br.readLine()) != null) {
				//跳过空行
				if("".equals(line)){
					continue;
				}
				// 把一行数据分割成多个字段
				StringTokenizer st = new StringTokenizer(line, ",");
				ArrayList lineList = new ArrayList();
				while (st.hasMoreTokens()) {
					lineList.add(st.nextToken());
				}
				list.add(lineList);
			}
			br.close();

		} catch (FileNotFoundException e) {
			// 捕获File对象生成时的异常
			e.printStackTrace();
		} catch (IOException e) {
			// 捕获BufferedReader对象关闭时的异常
			e.printStackTrace();
		}
		return list;
	}

}
