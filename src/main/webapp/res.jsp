<%@page import="java.util.ResourceBundle"%>
<%@ page contentType="text/html; charset=GBK"%>
<%
   request.setCharacterEncoding("GBK");
   String version = request.getParameter("version");
   String charset = request.getParameter("charset");
   String language = request.getParameter("language");
   String signType = request.getParameter("signType");
   String tranCode = request.getParameter("tranCode");
   String merchantID = request.getParameter("merchantID");
   String merOrderNum = request.getParameter("merOrderNum");
   String tranAmt = request.getParameter("tranAmt");
   String feeAmt = request.getParameter("feeAmt");
   String frontMerUrl = request.getParameter("frontMerUrl");
   String backgroundMerUrl = request.getParameter("backgroundMerUrl");
   String tranDateTime = request.getParameter("tranDateTime");
   String tranIP = request.getParameter("tranIP");
   String respCode = request.getParameter("respCode");
   String msgExt = request.getParameter("msgExt");
   String orderId = request.getParameter("orderId");
   String gopayOutOrderId = request.getParameter("gopayOutOrderId");
   String bankCode = request.getParameter("bankCode");
   String tranFinishTime = request.getParameter("tranFinishTime");
   String merRemark1 =  request.getParameter("merRemark1");
   String merRemark2 =  request.getParameter("merRemark2");
   String VerficationCode = "12345678";
   String signValueFromGopay = request.getParameter("signValue");

   
   String plain = "version=[" + version + "]tranCode=[" + tranCode + "]merchantID=[" + merchantID + "]merOrderNum=[" + merOrderNum + "]tranAmt=[" + tranAmt + "]feeAmt=[" + feeAmt+ "]tranDateTime=[" + tranDateTime + "]frontMerUrl=[" + frontMerUrl + "]backgroundMerUrl=[" + backgroundMerUrl + "]orderId=[" + orderId + "]gopayOutOrderId=[" + gopayOutOrderId + "]tranIP=[" + tranIP + "]respCode=[" + respCode + "]gopayServerTime=[]VerficationCode=[" + VerficationCode + "]";
   out.println("签名明文:"+plain);
   out.print("</br>");
   out.println("返回消息:"+msgExt);
   out.print("</br>");
   StringBuffer sb=new StringBuffer();
   if(msgExt!=null){
	   for(byte b:msgExt.getBytes()){
		   sb.append(b).append(" ");
	   }
   }
   out.print("返回消息字节:"+sb);
   out.print("</br>");
  
%>
