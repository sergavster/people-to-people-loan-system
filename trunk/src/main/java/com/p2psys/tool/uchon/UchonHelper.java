package com.p2psys.tool.uchon;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import com.p2psys.util.StringUtils;

public class UchonHelper {
	
	private static Logger logger = Logger.getLogger(UchonHelper.class);
	
	/**
	 * 向 udp://42.120.11.17:3433发起验证请求，检查 动态口令 是否正确
	 * @throws IOException 
	 * @param uchonSnDb 序列号
	 * @param uchonOtp 6位动态口令
	 * @return 验证结果， 200 正确 ，421 动态口令错误 ， 0 发送请求错误
	 */
	public static int checkOtp(String uchonSnDb, String uchonOtp) throws IOException {
		int result = 0;
		DatagramSocket client = new DatagramSocket();
		String sendStr = "code=0&id=username&sn="+uchonSnDb+"&otp="+ uchonOtp +"&svrC=&";
        byte[] sendBuf;
        sendBuf = sendStr.getBytes();
        InetAddress addr = InetAddress.getByName("42.120.11.17");
        int port = 3344;
        DatagramPacket sendPacket 
        		= new DatagramPacket(sendBuf ,sendBuf.length , addr , port);
    	
        byte[] recvBuf = new byte[100];
        DatagramPacket recvPacket
            = new DatagramPacket(recvBuf , recvBuf.length);
    
    	int count = 0 ;
    	String recvStr = "";
    	
    	//重复请求10次，如果服务器传回验证结果，就break
    	while(count++ < 10){
	    	client.send(sendPacket);
	        client.receive(recvPacket);
	        recvStr = new String(recvPacket.getData() , 0 ,recvPacket.getLength());
	        if(recvStr != null){
	        	// recvStr 类似 "id=username&200 OK"
	        	result = Integer.valueOf(recvStr.split("&")[1].split(" ")[0]);
	        	break;
	        }
    	}
		return result;
	}
	
	// v1.6.5.3 RDPROJECT-87 xx 2013.09.13 start
	/**
	 * 动态口令验证接口-GET访问方式
	 * 2013-09-13
	 * @param url
	 * @param sn_id
	 * @param dym_password
	 */
	public static String checkDymicPassword(String url, String sn_id, String dym_password) {
		if(!StringUtils.isBlank(sn_id) && !StringUtils.isBlank(dym_password)){
			HttpClient httpClient = new HttpClient();
			GetMethod getMethod = new GetMethod(url+"/sn_id/"+sn_id+"/dym_password/"+dym_password+".html"); 
			getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
			try {
				//执行getMethod
				int statusCode = httpClient.executeMethod(getMethod);
				if (statusCode != HttpStatus.SC_OK) {
					logger.error("checkDymicPassword2 failed: " + getMethod.getStatusLine());
				}
				byte[] responseBody = getMethod.getResponseBody();
				return new String(responseBody);
			} catch (HttpException e) {
				logger.error(e);
			} catch (IOException e) {
				//发生网络异常
				logger.error(e);
			} finally {
				//释放连接
				getMethod.releaseConnection();
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		String url = "http://key.erongdu.com/checkDymicPassword2";
		String sn_id="798201003";
	    String dym_password = "375695";
	    //System.out.println(checkDymicPassword(url, sn_id, dym_password));;
	}
	// v1.6.5.3 RDPROJECT-87 xx 2013.09.13 end
}
