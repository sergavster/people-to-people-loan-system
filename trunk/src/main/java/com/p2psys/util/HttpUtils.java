package com.p2psys.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {
	/**
	 * 获取远程页面值
	 * 
	 * @param pageURL
	 * @return
	 */
	public static String getHttpResponse(String pageURL) {
		String pageContent = "";
		BufferedReader in = null;
		InputStreamReader isr = null;
		InputStream is = null;
		HttpURLConnection huc = null;
		try {
			URL url = new URL(pageURL);
			huc = (HttpURLConnection) url.openConnection();
			is = huc.getInputStream();
			isr = new InputStreamReader(is);
			in = new BufferedReader(isr);
			String line = null;
			while (((line = in.readLine()) != null)) {
				if (line.length() == 0)
					continue;
				pageContent += line;
			}
		} catch (Exception e) {
		} finally {
			try {
				is.close();
				isr.close();
				in.close();
				huc.disconnect();
			} catch (Exception e) {
			}
		}
		return pageContent;
	}
}
