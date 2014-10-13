package com.p2psys.web.action.admin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.p2psys.domain.Linkage;
import com.p2psys.domain.NoticeConfig;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.service.LinkageService;
import com.p2psys.service.NoticeConfigService;
import com.p2psys.service.UserService;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.util.sms.SmsPortal;
import com.p2psys.web.action.BaseAction;

public class NoticeConfigAction extends BaseAction {
	private NoticeConfigService noticeConfigService;
	private NoticeConfig noticeConfig;
	private LinkageService linkageService;
	// v1.6.5.3 RDPROJECT-147 xx 2013.09.11 start
	private UserService userService;
	
	private File txt;
	private String txtFileName;
	// v1.6.5.3 RDPROJECT-147 xx 2013.09.11 end
	
	// v1.6.6.2 短信调用公共接口 lhm 2013-10-25 start
	/**
	 * 短信公共接口
	 */
	private SmsPortal smsPortal;

	/**
	 * 获取smsPortal
	 * 
	 * @return smsPortal
	 */
	public SmsPortal getSmsPortal() {
		return smsPortal;
	}

	/**
	 * 设置smsPortal
	 * 
	 * @param smsPortal 要设置的smsPortal
	 */
	public void setSmsPortal(SmsPortal smsPortal) {
		this.smsPortal = smsPortal;
	}
	// v1.6.6.2 短信调用公共接口 lhm 2013-10-25 end
	
	public LinkageService getLinkageService() {
		return linkageService;
	}

	public void setLinkageService(LinkageService linkageService) {
		this.linkageService = linkageService;
	}

	public NoticeConfig getNoticeConfig() {
		return noticeConfig;
	}

	public void setNoticeConfig(NoticeConfig noticeConfig) {
		this.noticeConfig = noticeConfig;
	}

	public NoticeConfigService getNoticeConfigService() {
		return noticeConfigService;
	}

	public void setNoticeConfigService(NoticeConfigService noticeConfigService) {
		this.noticeConfigService = noticeConfigService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public File getTxt() {
		return txt;
	}

	public void setTxt(File txt) {
		this.txt = txt;
	}

	public String getTxtFileName() {
		return txtFileName;
	}

	public void setTxtFileName(String txtFileName) {
		this.txtFileName = txtFileName;
	}

	public String index() {
		return SUCCESS;
	}

	public String update() {
		
		return SUCCESS;
	}

	public String clean() {
		System.out.println(context.getRealPath(""));
		return SUCCESS;
	}
	
	public String noticeConfigList(){
		int page=NumberUtils.getInt(StringUtils.isNull(request.getParameter("page")));
		PageDataList pList=noticeConfigService.noticeConfigList(page);
		setPageAttribute(pList, new SearchParam());
		return SUCCESS;
	}
	
	public String addNoticeConfig() {
		List<Linkage> linkageList=linkageService.linkageList(45);
		request.setAttribute("linkageList", linkageList);
		String actionType=StringUtils.isNull(request.getParameter("actionType"));
		if(!StringUtils.isBlank(actionType)){
			noticeConfig=new NoticeConfig();
			String type=StringUtils.isNull(request.getParameter("type"));
			long sms=NumberUtils.getLong(StringUtils.isNull(request.getParameter("sms")));
			long email=NumberUtils.getLong(StringUtils.isNull(request.getParameter("email")));
			long letters=NumberUtils.getLong(StringUtils.isNull(request.getParameter("letters")));
			noticeConfig.setEmail(email);
			noticeConfig.setLetters(letters);
			noticeConfig.setSms(sms);
			noticeConfig.setType(type);
			noticeConfigService.add(noticeConfig);
			message("新增通知配置成功！","/admin/notice/noticeConfigList.html");
			return ADMINMSG;
		}
		return SUCCESS;
	
	}

	
	// v1.6.5.3 RDPROJECT-147 xx 2013.09.11 start
	/**
	 * 短信群发
	 * @return
	 */
	public String sms(){
		if(!StringUtils.isBlank(paramString("actionType"))){
			StringBuffer sr = new StringBuffer();
			int sc = 0;
			int fc = 0;
			BufferedReader fin = null;
			try {
				int scope = paramInt("scope");
				String content = paramString("content");
				if(scope==1 && !StringUtils.isBlank(content)){//全站用户
					SearchParam param = new SearchParam();
					param.setPhone_status("1");//手机认证
					param.setType("2");//平台用户
					userService.sms(param,content);
				}else if(scope==2 && !StringUtils.isBlank(content)){//指定用户
					String phones = paramString("phones");
					if(!StringUtils.isBlank(phones)){
						String[] phoneArr = phones.split(",");
						if(phoneArr!=null && phoneArr.length>0){
							for (String phone : phoneArr) {
								if(StringUtils.isPhone(phone)){
									sr.append(phone);
									sr.append(",");
									// v1.6.6.2 短信调用公共类 lhm 2013-10-25 start
									//String result = SmsOpenUtils.sendSms(phone, content);
									String result = smsPortal.send(phone, content);
									// v1.6.6.2 短信调用公共类 lhm 2013-10-25 end
									if("ok".equals(result)){
										sc++ ;
										sr.append("发送成功");
									}else{
										fc++ ;
										sr.append(result);
									}
									sr.append(";");
								}else{
									fc++ ;
									sr.append(phone);
									sr.append(",发送失败,手机号码格式错误");
									sr.append(";");
								}
							}
						}
					}else if(txt!=null){
						if(!StringUtils.isBlank(txtFileName) && !txtFileName.contains(".txt")){
							message("请上传正确的txt文件！", "/admin/notice/sms.html");
							return ADMINMSG; 
						}
						fin=new BufferedReader(new FileReader(txt));
						int l = 0;
						String line = fin.readLine();
						while(!StringUtils.isBlank(line) && l<5000){
							/*if(StringUtils.isPhone(line)){
								SmsOpenUtils.sendSms(line, content);
							}*/
							if(StringUtils.isPhone(line)){
								sr.append(line);
								sr.append(",");
								// v1.6.6.2 短信调用公共类 lhm 2013-10-25 start
								//String result = SmsOpenUtils.sendSms(line, content);
								String result = smsPortal.send(line, content);
								// v1.6.6.2 短信调用公共类 lhm 2013-10-25 end
								if("ok".equals(result)){
									sc++ ;
									sr.append("发送成功");
								}else{
									fc++ ;
									sr.append(result);
								}
								sr.append(";");
							}else{
								fc++ ;
								sr.append(line);
								sr.append(",发送失败,手机号码格式错误");
								sr.append(";");
							}
							
							line = fin.readLine();
							l++;
						}
						
					}
				}
				message("短信群发结束！发送成功"+sc+"条,发送失败"+fc+"条&nbsp;&nbsp;<a href=\"javascript:alert('"+sr.toString()+"');\">点击查看处理结果</a>", "/admin/notice/sms.html");
				return ADMINMSG;
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				if(fin != null){
					try {
						fin.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return SUCCESS;
	}
	// v1.6.5.3 RDPROJECT-147 xx 2013.09.11 start
}
