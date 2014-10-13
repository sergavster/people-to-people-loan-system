package com.p2psys.web.action;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.util.ServletContextAware;
import org.apache.struts2.util.TokenHelper;

import com.alibaba.fastjson.JSON;
import com.octo.captcha.service.CaptchaServiceException;
import com.opensymphony.xwork2.ActionContext;
import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.domain.Borrow;
import com.p2psys.domain.Rule;
import com.p2psys.domain.User;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.tool.iphelper.IPSeeker;
import com.p2psys.tool.iphelper.IPUtils;
import com.p2psys.tool.javamail.MailWithAttachment;
import com.p2psys.tool.jcaptcha.CaptchaServiceSingleton;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.RSAUtil;
import com.p2psys.util.StringUtils;

public class BaseAction implements ServletRequestAware,ServletResponseAware,SessionAware,ServletContextAware {
	private final static Logger logger=Logger.getLogger(BaseAction.class);
	public final static String SUCCESS="success";
	public final static String ERROR="error";
	public final static String FAIL="fail";
	public final static String OK="ok";
	public final static String MSG="msg";
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public final static String ADMINMSG="adminmsg";
	public final static String NOTFOUND="notfound";
	
	protected Map<String, Object> session;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected ServletContext context;
	
	protected String actionType;
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session=session;
	}
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request=request;
	}
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response=response;
	}
	@Override
	public void setServletContext(ServletContext context) {
		this.context=context;
	}
	
	public String getActionType() {
		return StringUtils.isNull(actionType);
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	/**
	 * 封装获取Session中的用户对象
	 * @return
	 */
	protected User getSessionUser(){
		User user = (User) session.get(Constant.SESSION_USER);
		return user;
	}
	/**
	 * 封装获取Session中的用户对象
	 * @return
	 */
	protected User getAuthUser(){
		User user = (User) session.get(Constant.AUTH_USER);
		return user;
	}
	/**
	 * 封装获取Session中的用户对象的用户名
	 * @return
	 */
	protected String getAuthUserName(){
		User user = (User) session.get(Constant.AUTH_USER);
		return user.getUsername();
	}
	/**
	 * 封装获取Session中的用户对象的ID
	 * @return
	 */
	protected long getAuthUserId(){
		User user = (User) session.get(Constant.AUTH_USER);
		return user.getUser_id();
	}
	/**
	 * 获取http请求的实际IP
	 * @return
	 */
	protected String getRequestIp(){
		String realip=IPUtils.getRemortIP(request);
		return realip;
	}
	
	/**
	 * 获取IP所在地
	 * @return
	 */
	protected String getAreaByIp(){
		String realip=getRequestIp();
		return getAreaByIp(realip);
	}
	protected String getAreaByIp(String ip){
		IPSeeker ipSeeker = IPSeeker.getInstance();
		String nowarea=ipSeeker.getArea(ip);
		return nowarea;
	}
	/**
	 * 获取当前时间
	 * @return
	 */
	protected String getTimeStr(){
		String str=Long.toString(System.currentTimeMillis() / 1000);
		return str;
	}
	/**
	 * 生产校验码
	 * @throws IOException
	 */
	protected void genernateCaptchaImage() throws IOException {  
		response.setHeader("Cache-Control", "no-store");  
        response.setHeader("Pragma", "no-cache");  
        response.setDateHeader("Expires", 0);  
        response.setContentType("image/jpeg");  
        ServletOutputStream out = response.getOutputStream();  
        try {  
            String captchaId = request.getSession(true).getId();  
            BufferedImage challenge = (BufferedImage)  CaptchaServiceSingleton.getInstance().getChallengeForID(captchaId, request.getLocale());  
            ImageIO.write(challenge, "jpg", out);  
            out.flush();  
        } catch (CaptchaServiceException e) {  
        } finally {  
            out.close();  
        }  
    }  
	/**
	 * 校验校验码是否正确
	 * @param valid
	 * @return
	 */
	protected boolean checkValidImg(String valid){
		 boolean b=false;
		 try {
			b= CaptchaServiceSingleton.getInstance()
					    .validateResponseForID(request.getSession().getId(), valid.toLowerCase());
		} catch (CaptchaServiceException e) {
			b=false;
		}
		 return b;
	}
	/**
	 * 提示消息
	 * @param msg
	 * @param url
	 */
	protected void message(String msg,String url){
		String urltext="";
		if(!StringUtils.isBlank(url)){
			urltext="<a href="+request.getContextPath()+url+" >返回上一页</a>";
			request.setAttribute("backurl",urltext);
		}else{
			urltext="<a href='javascript:history.go(-1)'>返回上一页</a>";
		}
		message(msg, url, urltext);
	}
	
	protected void message(String msg){
		this.message(msg, getMsgUrl());
	}
	/**
	 * 提示消息
	 * @param msg
	 * @param url
	 * @param text
	 */
	protected void message(String msg,String url,String text){
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute("rsmsg",msg);
		String urltext="<a href="+request.getContextPath()+url+" >"+text+"></a>";
		request.setAttribute("backurl",urltext);
	}
	/**
	 * 空白方法，不处理业务逻辑
	 * @return
	 * @throws Exception
	 */
	public String blank() throws Exception {
		return SUCCESS;
	}
	/**
	 * 校验参数actionType是否null或者空，是返回true.
	 * @return
	 */
	public boolean isBlank(){
		return "".equals(this.getActionType());
	}
	
	public void saveParam(){
		request.setAttribute("param", new SearchParam().toMap());
	}
	
	
	public boolean isSession(){
		User sessionUser=this.getSessionUser();
		if(sessionUser==null) return false;
		return true;
	}
	
	protected void setMsgUrl(String url){
		request.setAttribute("currentUrl",url);
		String msgurl=(String)session.get("msgurl");
		String query=request.getQueryString();
		if(!StringUtils.isBlank(query)){
			url=url+"?"+query;
		}
		msgurl=url;
		session.put("msgurl", msgurl);
	}
	
	
	protected String getMsgUrl(){
		String msgurl="";
		Object o=null;
		if((o=session.get("msgurl"))!=null){
			msgurl=(String)o;
		}
		return msgurl;
	}
	
   protected void setPageAttribute(PageDataList plist,SearchParam param){	
	   request.setAttribute("page", plist.getPage());
	   request.setAttribute("list", plist.getList());
	   request.setAttribute("param", param.toMap());
   }
   
   protected String upload(File upload,String fileName,String destDir,String destFileName) throws Exception {
	   	if(upload==null) return "";
		logger.info("文件："+upload);
		logger.info("文件名："+fileName);
		String destFileUrl=destDir+"/"+destFileName;
		String destfilename=ServletActionContext.getServletContext().getRealPath(destDir)+"/"+destFileName;
		logger.info(destfilename);
		File imageFile=null;
		imageFile = new File(destfilename);
		FileUtils.copyFile(upload, imageFile);
		return destFileUrl;
  }
   	
	// v1.6.6.2 RDPROJECT-277 xx 2013-10-24 start
	/**
	 * 后台管理功能中的图片上传
	 * @param files
	 * @param filesFileName
	 * @param backUrl
	 * @return
	 * @throws Exception
	 */
	protected String upload(File files, String filesFileName, String backUrl) throws Exception {
		String newUrl = "";
		if (files != null) {
			boolean rs = checkUpload(filesFileName);
			if (rs) {
				message("上传附件格式不正确!", backUrl);
				return ADMINMSG;
			}
			String newFileName = generateUploadFilename(filesFileName);
			newUrl = upload(files, "", "/data/upload", newFileName);
		}
		return newUrl;
	}
	// v1.6.6.2 RDPROJECT-277 xx 2013-10-24 end
   
   protected String generateUploadFilename(){
	   User u=getSessionUser();
	   String timeStr=DateUtils.dateStr3(new Date());
	   if(u==null) return timeStr;
	   return u.getUser_id()+timeStr;
   }
   
   protected String generateUploadFilename(String fileName){
	  String suffix = null;
	  if (fileName != null) {
		  int last = fileName.lastIndexOf('.');
		  suffix = fileName.substring(last);
	  }
	  return generateUploadFilename()+suffix;
   }
   
   protected String getLogRemark(Borrow b){
		String s="对[<a href='"+request.getContextPath()+
				"/invest/detail.html?borrowid="+b.getId()+"' target=_blank>"+
				b.getName()+"</a>]";
		return s;
		
	}
   protected String getRef(){
	   String ref=StringUtils.isNull(request.getParameter("ref"));
	   return ref;
   }
   protected String getAndSaveRef(){
	   String ref=getRef();
	   request.setAttribute("ref", ref);
	   return ref;
   }
   
   protected void printJson(String json) throws IOException{
	   HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(json);
		out.flush();   
	    out.close();
   }
   
   protected void generateDownloadFile(String inFile,String downloadFile) throws IOException{
		InputStream ins = new BufferedInputStream(new FileInputStream(inFile));
		byte[] buffer = new byte[ins.available()];
		ins.read(buffer);
		ins.close();
		HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
		response.reset();
		response.addHeader("Content-Disposition", "attachment;filename="+ new String(downloadFile.getBytes()));
		response.addHeader("Content-Length", "" + new File(inFile).length());
		OutputStream ous = new BufferedOutputStream(response.getOutputStream());
		response.setContentType("application/octet-stream");
		ous.write(buffer);
		ous.flush();
		ous.close();
   }
   
   protected int paramInt(String str){
	   return NumberUtils.getInt(request.getParameter(str));
   }
   
   protected long paramLong(String str){
	   return NumberUtils.getLong(request.getParameter(str));
   }
   
   protected double paramDouble(String str){
	   return NumberUtils.getDouble(request.getParameter(str));
   }
   
   protected String paramString(String str){
	   return StringUtils.isNull(request.getParameter(str));
   }
   
   protected void export(String infile,String downloadFile) throws Exception{
		File inFile = new File(infile);
		InputStream ins = new BufferedInputStream(new FileInputStream(infile));
		byte[] buffer = new byte[ins.available()];
		ins.read(buffer);
		ins.close();
		HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
		response.reset();
		response.addHeader("Content-Disposition", "attachment;filename="+ new String(downloadFile.getBytes()));
		response.addHeader("Content-Length", "" + inFile.length());
		OutputStream ous = new BufferedOutputStream(response.getOutputStream());
		response.setContentType("application/octet-stream");
		ous.write(buffer);
		ous.flush();
		ous.close();
	}
   /**
    * 检查表单提交的
    * @param name
    * @return
    */
   protected String checkToken(String name){
	   String paramValue=paramString(name);
	   String tokenValue=StringUtils.isNull((String)session.get(name));
	   //参数、session中都没用token值提示错误
	   if(paramValue.isEmpty()&&tokenValue.isEmpty()){
//		   throw new BorrowException("会话Token未设定！");
		   return "会话Token未设定！";
	   }else if(paramValue.isEmpty()&&!tokenValue.isEmpty()){
//		   throw new BorrowException("表单Token未设定！");
		   return "表单Token未设定！";
	   }else if(paramValue.equals(tokenValue)&&!tokenValue.isEmpty()){ //session中有token,防止重复提交检查
		   session.remove(name);
		   return "";
	   }else{
//		   throw new BorrowException("请勿重复提交！");
		   return "请勿重复提交！";
	   }
   }
   
   protected void saveToken(String name){
	   String token=TokenHelper.generateGUID();
	   session.put(name, token);
   }
   
   /**
	 * 发送邮件(带附件)
	 * @param user
	 * @throws Exception
	 */
   protected void sendMailWithAttachment(User user,long borrow_id,long tender_id) throws Exception {
		String to = user.getEmail();
		String attachfile = ServletActionContext.getServletContext().getRealPath("/")
				+ "/data/protocol/" + borrow_id + "_" + tender_id + ".pdf";
		MailWithAttachment m = MailWithAttachment.getInstance();
		m.setTo(to);
		m.readProtocolMsg();
		m.replace(user.getUsername(), to, "/member/identify/active.html?id="
				+ m.getdecodeIdStr(user));
		m.attachfile(attachfile);
		m.sendMail();
	}
   
   /**
    * 判断上传文件约束
    * @param fileName
    * @return
    */
   protected boolean checkUpload(String fileName){
	   	//v1.6.7.1 安全优化 sj 2013-11-18 start
		Pattern p = Pattern.compile(".(jsp|jspx|php|asp|aspx|js|sh)");
		//v1.6.7.1 安全优化 sj 2013-11-18 end
		Matcher m = p.matcher(fileName);
		return m.find();
   }
   
   
	// v1.6.7.1 RDPROJECT-104 xx 2013-11-10 start
	/**
	 * RSA
	 * 公钥的Modulus与PublicExponent的hex编码形式
	 */
	protected void initRSAME() {
		Rule rsaFormEncrypt = Global.getRule(EnumRuleNid.RSA_FORM_ENCRYPT.getValue());
		if (rsaFormEncrypt != null && rsaFormEncrypt.getStatus() == 1) {
			// RSA
			RSAPublicKey rsap;
			try {
				rsap = (RSAPublicKey) RSAUtil.getKeyPair().getPublic();
				String module = rsap.getModulus().toString(16);
				String empoent = rsap.getPublicExponent().toString(16);
				request.setAttribute("m", module);
				request.setAttribute("e", empoent);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}
		request.setAttribute("rsaFormEncrypt", rsaFormEncrypt);
	}
	// v1.6.7.1 RDPROJECT-104 xx 2013-11-10 end
	
	public void json(String returnmessage){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("data", returnmessage);
		try {
			printJson(JSON.toJSONString(map));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
