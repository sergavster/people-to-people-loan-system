package com.p2psys.tool.javamail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;

import com.p2psys.context.Global;
import com.p2psys.domain.User;
import com.p2psys.model.SystemInfo;
import com.p2psys.tool.coder.BASE64Encoder;
import com.p2psys.tool.coder.MD5;
  

public class MailWithAttachment {
	
	private static Logger logger = Logger.getLogger(MailWithAttachment.class);
	
	String to = "";// 收件人  
    String from = "";// 发件人  
    String host = "";// smtp主机  
    String username = "";  
    String password = "";  
    String filename = "";// 附件文件名  
    String subject = "";// 邮件主题  
    String content = "";// 邮件正文  
    Vector file = new Vector();// 附件文件集合  
  
    /** 
     * 
     * 方法说明：默认构造器 
     * 输入参数： 
     * 返回类型： 
     */  
    public MailWithAttachment() {  
    }  
  
    /** 
     * 
     * 方法说明：构造器，提供直接的参数传入 
     * 输入参数：  
     * 返回类型： 
     */  
    public MailWithAttachment(String to, String from, String smtpServer,  
            String username, String password, String subject, String content) {  
        this.to = to;  
        this.from = from;  
        this.host = smtpServer;  
        this.username = username;  
        this.password = password;  
        this.subject = subject;  
        this.content = content;  
    }  
  
    /** 
     * 
     * 方法说明：设置邮件服务器地址  
     * 输入参数：String host 邮件服务器地址名称  
     * 返回类型： 
     */  
    public void setHost(String host) {  
        this.host = host;  
    }  
  
    public String getContent() {
		return content;
	}

	/** 
     * 
     * 方法说明：设置登录服务器校验密码 
     * 输入参数：  
     * 返回类型： 
     */  
    public void setPassWord(String pwd) {  
        this.password = pwd;  
    }  
  
    /** 
     * 
     * 方法说明：设置登录服务器校验用户  
     * 输入参数：  
     * 返回类型： 
     */  
    public void setUserName(String usn) {  
        this.username = usn;  
    }  
  
    /** 
     * 
     * 方法说明：设置邮件发送目的邮箱  
     * 输入参数： 
     * 返回类型： 
     */  
    public void setTo(String to) {  
        this.to = to;  
    }  
  
    /** 
     *  
     * 方法说明：设置邮件发送源邮箱
     * 输入参数：  
     * 返回类型： 
     */  
    public void setFrom(String from) {  
        this.from = from;  
    }  
  
    /** 
     * 
     * 方法说明：设置邮件主题 
     * 输入参数：  
     * 返回类型： 
     */  
    public void setSubject(String subject) {  
        this.subject = subject;  
    }  
  
    /** 
     * 
     * 方法说明：设置邮件内容 
     * 输入参数： 
     * 返回类型： 
     */  
    public void setContent(String content) {  
        this.content = content;  
    }  
  
    /** 
     * 
     * 方法说明：把主题转换为中文 
     * 输入参数：String strText  
     * 返回类型： 
     */  
    public String transferChinese(String strText) {  
        try {  
        	// v1.6.6.2 RDPROJECT-324 lhm 2013-10-18 start 
        	//  解决邮件标题乱码问题 
            strText = MimeUtility.encodeText( strText,"utf-8", "B");  
           // v1.6.6.2 RDPROJECT-324 lhm 2013-10-18 end
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return strText;  
    }  
  
    /** 
     * 
     * 方法说明：往附件组合中添加附件 
     * 输入参数： 
     * 返回类型： 
     */  
    public void attachfile(String fname) {  
        file.addElement(fname);  
    }  
  
    /** 
     * 
     * 方法说明：发送邮件  
     * 输入参数： 
     * 返回类型：boolean 成功为true，反之为false 
     */
    
	public static MailWithAttachment getInstance(){
		MailWithAttachment mail = new MailWithAttachment();
		String host = Global.getValue("email_host");
		String from = Global.getValue("email_from");
		String username = Global.getValue("email_email");
		String password = Global.getValue("email_pwd");
		String subject = Global.getValue("email_from_name");
		mail.setHost(host);
		mail.setFrom(from);
		mail.setUserName(username);
		mail.setPassWord(password);
		mail.setSubject(subject);
		return mail;
	}
	
	public void readProtocolMsg() {
	    this.readMsg("/res/protocol.msg");	
	}
	
	private void readMsg(String filename){
		StringBuffer sb=new StringBuffer();
		InputStream fis=Mail.class.getResourceAsStream(filename);
		InputStreamReader isr=null;
		try {
			isr=new InputStreamReader(fis,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			logger.error("读取文件遇见不正确的文件编码！");
		}
		BufferedReader br=new BufferedReader(isr);
		String line="";
		try {
			while((line=br.readLine())!=null){
				sb.append(line);
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		String msg=sb.toString();
		logger.info(msg);
		setContent(msg);
	}
	
	public String getdecodeIdStr(User user){
		String chars="0123456789qwertyuiopasdfghjklmnbvcxz-=~!@#$%^*+-._/*<>|";
		int length=chars.length();
		StringBuffer url=new StringBuffer();
		StringBuffer rancode=new StringBuffer();
		String timeStr=System.currentTimeMillis()/1000+"";
		String userIDAddtime = user.getUser_id()+user.getAddtime();
		MD5 md5=new MD5();
		userIDAddtime =  md5.getMD5ofStr(userIDAddtime);
		url.append(user.getUser_id()).append(",").append(userIDAddtime).append(",").append(timeStr).append(",");
		for(int i=0;i<10;i++){
			int num=(int)(Math.random()*(length-2))+1;
			rancode.append(chars.charAt(num));
		}
		url.append(rancode);
		String idurl=url.toString();
		BASE64Encoder encoder=new BASE64Encoder();
		String s=encoder.encode(idurl.getBytes());
		return s;
	}
	
	public void replace(String webname,String host,String username,String email,String url){
		String msg=this.getContent();
		msg=msg.replace("$webname$", webname).replace("$email$", email).replace("$host$", host).replace("$username$", username)
				.replace("$url$", host+url);
		this.setContent(msg);
	}
	
	public void replace(String username,String email,String url){
		SystemInfo info=Global.SYSTEMINFO;
		String weburl=info.getValue("weburl");
		String webname=info.getValue("webname");
		replace(webname,weburl,username,email,url);
	}
	
	public void sendMail(String to,String content) throws Exception {
		this.setTo(to);
		this.setContent(content);
		this.sendMail();
	}
	
    public boolean sendMail() {  
  
        // 构造mail session  
        Properties props = new Properties() ;  
        props.put("mail.smtp.host", host);  
        props.put("mail.smtp.auth", "true");  
        Session session = Session.getDefaultInstance(props,  
                new Authenticator() {  
                    public PasswordAuthentication getPasswordAuthentication() {  
                        return new PasswordAuthentication(username, password);  
                    }  
                });  
        //Session session = Session.getDefaultInstance(props);  
//      Session session = Session.getDefaultInstance(props, null);  
  
        try {  
            // 构造MimeMessage 并设定基本的值  
            MimeMessage msg = new MimeMessage(session);  
            //MimeMessage msg = new MimeMessage();  
            msg.setFrom(new InternetAddress(from));  
           
              
            //msg.addRecipients(Message.RecipientType.TO, address); //这个只能是给一个人发送email  
            msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(to)) ;  
            subject = transferChinese(subject);  
            msg.setSubject(subject);  
  
            // 构造Multipart  
            Multipart mp = new MimeMultipart();  
  
            // 向Multipart添加正文  
            MimeBodyPart mbpContent = new MimeBodyPart();  
            mbpContent.setContent(content, "text/html;charset=gb2312");  
              
            // 向MimeMessage添加（Multipart代表正文）  
            mp.addBodyPart(mbpContent);  
  
            // 向Multipart添加附件  
            Enumeration efile = file.elements();  
            while (efile.hasMoreElements()) {  
  
                MimeBodyPart mbpFile = new MimeBodyPart();  
                filename = efile.nextElement().toString();  
                FileDataSource fds = new FileDataSource(filename);  
                mbpFile.setDataHandler(new DataHandler(fds));  
                //<span style="color: #ff0000;">//这个方法可以解决附件乱码问题。</span>    
                String filename= new String(fds.getName().getBytes(),"ISO-8859-1");  
  
                mbpFile.setFileName(filename);  
                // 向MimeMessage添加（Multipart代表附件）  
                mp.addBodyPart(mbpFile);  
  
            }  
  
            file.removeAllElements();  
            // 向Multipart添加MimeMessage  
            msg.setContent(mp);  
            msg.setSentDate(new Date());  
            msg.saveChanges() ;  
            // 发送邮件  
              
            Transport transport = session.getTransport("smtp");  
            transport.connect(host, username, password);  
            transport.sendMessage(msg, msg.getAllRecipients());  
            transport.close();  
        } catch (Exception mex) {  
            mex.printStackTrace();  
//          Exception ex = null;  
//          if ((ex = mex.getNextException()) != null) {  
//              ex.printStackTrace();  
//          }  
            return false;  
        }  
        return true;  
    }  
  
  
      
    /** 
     * 
     * 方法说明：主方法，用于测试 
     * 输入参数： 
     * 返回类型： 
     */  
    public static void main(String[] args) {  
    	MailWithAttachment sendmail = new MailWithAttachment();  
          
        sendmail.setHost("smtp.qq.com");  
        sendmail.setUserName("1353216244@qq.com");  
        sendmail.setPassWord("kevin@407437254");  
        sendmail.setTo("407437254@qq.com");  
        sendmail.setFrom("1353216244@qq.com");  
        sendmail.setSubject("你好，这是测试！");  
        sendmail.setContent("你好这是一个带多附件的测试！");  
        // Mail sendmail = new  
        // Mail("dujiang@sricnet.com","du_jiang@sohu.com","smtp.sohu.com","du_jiang","31415926","你好","胃，你好吗？");  
        sendmail.attachfile("d:\\a.txt");  
        //sendmail.attachfile("d:\\bdcf.gif");  
          
        //System.out.println(sendmail.sendMail());  
  
    }  
}
