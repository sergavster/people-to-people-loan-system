package com.p2psys.web.action.admin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.p2psys.context.Global;
import com.p2psys.domain.NoticeType;
import com.p2psys.model.NoticeTypeSearchParam;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.service.NoticeTypeService;
import com.p2psys.service.UserService;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.util.sms.SmsPortal;
import com.p2psys.web.action.BaseAction;

public class ManageNoticeAction extends BaseAction {
	private static Logger logger = Logger.getLogger(ManageNoticeAction.class);	
	private SmsPortal smsPortal;
	private NoticeTypeService noticeTypeService;
	private NoticeType noticeType;
	// v1.6.5.3 RDPROJECT-147 xx 2013.09.11 start
	private UserService userService;
	
	private File txt;
	private String txtFileName;
	
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
	// v1.6.5.3 RDPROJECT-147 xx 2013.09.11 end
	
	
	private SmsPortal[] smsPortalBackups;
	


	public SmsPortal[] getSmsPortalBackups() {
		return smsPortalBackups;
	}

	public void setSmsPortalBackups(SmsPortal[] smsPortalBackups) {
		this.smsPortalBackups = smsPortalBackups;
	}

	public String querySmsAccount(){
		//v1.6.7.2  RDPROJECT-556 liukun 2013-12-11 start
		ArrayList<SmsPortal> sps = new ArrayList<SmsPortal>();
		sps.add(smsPortal);
		
		for (SmsPortal sp : smsPortalBackups) {
			sps.add(sp);
		}
		
		ArrayList<Map> useInfo = new ArrayList<Map>();
		
		for (SmsPortal sp : sps) {
			Map useinfo = sp.getUseInfo();
			useinfo.put("spName", sp.getSPName());
			useinfo.put("usenum", useinfo.get("usenum"));
			useinfo.put("usednum", useinfo.get("usednum"));
			useInfo.add(useinfo);
		}

		request.setAttribute("useInfo", useInfo);
		//v1.6.7.2  RDPROJECT-556 liukun 2013-12-11 end
		
		return SUCCESS;

		
	}
	
	public SmsPortal getSmsPortal() {
		return smsPortal;
	}

	public void setSmsPortal(SmsPortal smsPortal) {
		this.smsPortal = smsPortal;
	} 

	public String noticeTypeList(){

		int page=NumberUtils.getInt(StringUtils.isNull(request.getParameter("page")));
		NoticeTypeSearchParam param=new NoticeTypeSearchParam();
		PageDataList pList=null;
		//v1.6.7.2  RDPROJECT-535 liukun 2013-12-06 start
		String noticeTypeNid = request.getParameter("noticeTypeNid");
		String noticeTypeSms = request.getParameter("noticeTypeSms");
		String noticeTypeEmail = request.getParameter("noticeTypeEmail");
		String noticeTypeMessage = request.getParameter("noticeTypeMessage");
		
		param.setNoticeTypeNid(noticeTypeNid);
		param.setNoticeTypeSms(noticeTypeSms);
		param.setNoticeTypeEmail(noticeTypeEmail);
		param.setNoticeTypeMessage(noticeTypeMessage);
		
		pList=noticeTypeService.noticeTypeList(page, param);
		
		/*if (request.getParameter("noticeTypeNid")!=null && !"".equals(request.getParameter("noticeTypeNid"))){
			pList=noticeTypeService.getList(page, request.getParameter("noticeTypeNid"));
		}else{
			pList=noticeTypeService.noticeTypeList(page, param);
		}*/
		//v1.6.7.2  RDPROJECT-535 liukun 2013-12-06 end
		setPageAttribute(pList, param);
		request.setAttribute("param", param.toMap());
		return SUCCESS;
	}

	public NoticeTypeService getNoticeTypeService() {
		return noticeTypeService;
	}

	public void setNoticeTypeService(NoticeTypeService noticeTypeService) {
		this.noticeTypeService = noticeTypeService;
	}
	
public String updateNoticeType() {
		
		String actionType=StringUtils.isNull(request.getParameter("actionType"));
		String nid = request.getParameter("nid");
		byte notice_type = Byte.parseByte(request.getParameter("notice_type"));
		if(!StringUtils.isBlank(actionType)){
			noticeType=new NoticeType();
			String name = StringUtils.isNull(request.getParameter("name"));
			byte type = Byte.parseByte(StringUtils.isNull(request.getParameter("type")));
			byte send = Byte.parseByte(StringUtils.isNull(request.getParameter("send")));
			//v1.6.7.2  RDPROJECT-556 liukun 2013-12-10 start
			String send_route = StringUtils.isNull(request.getParameter("send_route"));
			//v1.6.7.2  RDPROJECT-556 liukun 2013-12-10 end
			String title_templet = StringUtils.isNull(request.getParameter("title_templet"));
			String templet = StringUtils.isNull(request.getParameter("content"));
			String remark = StringUtils.isNull(request.getParameter("remark"));
			
			noticeType.setNid(nid);
			noticeType.setNotice_type(notice_type);
			noticeType.setName(name);
			noticeType.setType(type);
			noticeType.setSend(send);
			//v1.6.7.2  RDPROJECT-556 liukun 2013-12-10 start
			noticeType.setSend_route(send_route);
			//v1.6.7.2  RDPROJECT-556 liukun 2013-12-10 end
			noticeType.setTitle_templet(title_templet);
			noticeType.setTemplet(templet);
			noticeType.setRemark(remark);
			noticeType.setUpdatetime(Long.parseLong(getTimeStr()));
			noticeType.setUpdateip(getRequestIp());
			noticeTypeService.update(noticeType);
			
			List noticeTypeList = noticeTypeService.getList();
	        Map noticeTypeMap=new HashMap();
			for(int i=0;i<noticeTypeList.size();i++){
				NoticeType noticeType=(NoticeType)noticeTypeList.get(i);
				noticeTypeMap.put(noticeType.getNid() + "_" + noticeType.getNotice_type(), noticeType);
			}
			Global.SMSTYPECONFIG=noticeTypeMap;

			message("更新通知类型成功！","/admin/notice/noticeTypeList.html");
			return ADMINMSG;
		}
		NoticeType p=noticeTypeService.getNoticeTypeByNid(nid, notice_type);
		request.setAttribute("p", p);
		return SUCCESS;
	}

	//v1.6.5.3 RDPROJECT-147 xx 2013.09.11 start
	/**
	 * 短信群发
	 * @return
	 */
	public String batchSms(){
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
				//v1.6.7.2 RDPROJECT-577 liukun 2013-12-11 start
				//message("短信群发结束！发送成功"+sc+"条,发送失败"+fc+"条&nbsp;&nbsp;<a href=\"javascript:alert('"+sr.toString()+"');\">点击查看处理结果</a>", "/admin/notice/sms.html");
				message("短信群发结束！发送成功"+sc+"条,发送失败"+fc+"条&nbsp;&nbsp;<a href=\"javascript:alert('"+sr.toString()+"');\">点击查看处理结果</a>", "/admin/notice/batchSms.html");
				//v1.6.7.2 RDPROJECT-577 liukun 2013-12-11 end
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
	
	public String queryUserNotice(){
		
		return SUCCESS;
	}
}
