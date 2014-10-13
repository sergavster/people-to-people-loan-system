package com.p2psys.web.action.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.p2psys.context.Global;
import com.p2psys.domain.Smstype;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.service.SmstypeService;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.util.sms.SmsPortal;
import com.p2psys.web.action.BaseAction;

public class SmstypeAction extends BaseAction {
	private static Logger logger = Logger.getLogger(SmstypeAction.class);	
	private SmstypeService smstypeService;
	private Smstype smstype;
	private SmsPortal smsPortal;

	public SmstypeService getSmstypeService() {
		return smstypeService;
	}

	public void setSmstypeService(SmstypeService smstypeService) {
		this.smstypeService = smstypeService;
	}

	public Smstype getSmstype() {
		return smstype;
	}

	public void setSmstype(Smstype smstype) {
		this.smstype = smstype;
	}

	public String update() {
		
		String actionType=StringUtils.isNull(request.getParameter("actionType"));
		String nid = request.getParameter("nid");
		if(!StringUtils.isBlank(actionType)){
			smstype=new Smstype();
			String name = StringUtils.isNull(request.getParameter("name"));
			byte type = Byte.parseByte(StringUtils.isNull(request.getParameter("type")));
			byte send = Byte.parseByte(StringUtils.isNull(request.getParameter("send")));
			String templet = StringUtils.isNull(request.getParameter("templet"));
			String remark = StringUtils.isNull(request.getParameter("remark"));
			
			smstype.setNid(nid);
			smstype.setName(name);
			smstype.setType(type);
			smstype.setSend(send);
			smstype.setTemplet(templet);
			smstype.setRemark(remark);
			smstype.setUpdatetime(Long.parseLong(getTimeStr()));
			smstype.setUpdateip(getRequestIp());
			smstypeService.update(smstype);
			
			//V1.6.5.3 RDPROJECT-143 liukun 2013-09-11 start
			List smstypeList = smstypeService.getList();
	        Map smstypeMap=new HashMap();
			for(int i=0;i<smstypeList.size();i++){
				Smstype smstype=(Smstype)smstypeList.get(i);
				smstypeMap.put(smstype.getNid(), smstype);
			}
			Global.SMSTYPECONFIG=smstypeMap;
			//V1.6.5.3 RDPROJECT-143 liukun 2013-09-11 end

			message("更新短信类型成功！","/admin/smstype/smstypeList.html");
			return ADMINMSG;
		}
		Smstype p=smstypeService.getSmsTypeByNid(nid);
		request.setAttribute("p", p);
		return SUCCESS;
	}


	
	public String smstypeList(){
		int page=NumberUtils.getInt(StringUtils.isNull(request.getParameter("page")));
		//V1.6.5.3 RDPROJECT-143 liukun 2013-09-11 start
		//PageDataList pList=smstypeService.smstypeList(page);
		SearchParam param=new SearchParam();
		PageDataList pList=smstypeService.smstypeList(page, param);
		//V1.6.5.3 RDPROJECT-143 liukun 2013-09-11 end
		setPageAttribute(pList, new SearchParam());
		return SUCCESS;
	}
	
	public String add() {
		String actionType=StringUtils.isNull(request.getParameter("actionType"));
		if(!StringUtils.isBlank(actionType)){
			smstype=new Smstype();
			String nid = StringUtils.isNull(request.getParameter("nid"));
			String name = StringUtils.isNull(request.getParameter("name"));
			byte type = Byte.parseByte(StringUtils.isNull(request.getParameter("type")));
			byte canswitch = Byte.parseByte(StringUtils.isNull(request.getParameter("canswitch")));
			String templet = StringUtils.isNull(request.getParameter("templet"));
			String remark = StringUtils.isNull(request.getParameter("remark"));
			long addtime = NumberUtils.getLong(StringUtils.isNull(request.getParameter("addtime")));
			String addip = StringUtils.isNull(request.getParameter("addip"));
			
			smstype.setNid(nid);
			smstype.setName(name);
			smstype.setType(type);
			smstype.setCanswitch(canswitch);
			smstype.setTemplet(templet);
			smstype.setRemark(remark);
			smstype.setAddtime(Long.parseLong(getTimeStr()));
			smstype.setAddip(getRequestIp());
			smstypeService.add(smstype);

			message("新增短信类型成功！","/admin/sms/smstypeList.html");
			return ADMINMSG;
		}
		return SUCCESS;
	
	}
	
	public String query(){

		Map<String, Integer> useinfo = smsPortal.getUseInfo();
		
		request.setAttribute("usenum", useinfo.get("usenum"));
		request.setAttribute("usednum", useinfo.get("usednum"));

		
		return SUCCESS;
	}
	
	public SmsPortal getSmsPortal() {
		return smsPortal;
	}

	public void setSmsPortal(SmsPortal smsPortal) {
		this.smsPortal = smsPortal;
	} 


}
