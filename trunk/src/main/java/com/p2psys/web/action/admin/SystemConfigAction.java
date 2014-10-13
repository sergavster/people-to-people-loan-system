package com.p2psys.web.action.admin;

import java.util.List;
import java.util.Properties;

import com.p2psys.context.Global;
import com.p2psys.domain.SystemConfig;
import com.p2psys.model.SystemInfo;
import com.p2psys.service.SystemService;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

public class SystemConfigAction extends BaseAction {
	private SystemService systemService;
	private List<SystemConfig> sysInfo;

	public List<SystemConfig> getSysInfo() {
		return sysInfo;
	}

	public void setSysInfo(List<SystemConfig> sysInfo) {
		this.sysInfo = sysInfo;
	}

	public SystemService getSystemService() {
		return systemService;
	}

	public void setSystemService(SystemService systemService) {
		this.systemService = systemService;
	}

	public String index() {
		sysInfo = systemService.getSystemInfoForListBysytle(1);
		return SUCCESS;
	}

	public String update() {
		if (sysInfo != null)
			systemService.updateSystemInfo(sysInfo, context);
		sysInfo = systemService.getSystemInfoForListBysytle(1);
		SystemInfo info=systemService.getSystemInfo();
		String[] webinfo=Global.SYSTEMNAME;
		for(String s:webinfo){
			context.setAttribute(s, info.getValue(s));
		}
		return SUCCESS;
	}

	public String clean() {
		//System.out.println(context.getRealPath(""));
		return SUCCESS;
	}
	
	public String welcome(){
		Properties props = System.getProperties();
		Runtime runtime = Runtime.getRuntime();
		long freeMemoery = runtime.freeMemory();
		long totalMemory = runtime.totalMemory();
		long usedMemory = totalMemory - freeMemoery;
		long maxMemory = runtime.maxMemory();
		long useableMemory = maxMemory - totalMemory + freeMemoery;
		request.setAttribute("props", props);
		request.setAttribute("freeMemoery", freeMemoery);
		request.setAttribute("totalMemory", totalMemory);
		request.setAttribute("usedMemory", usedMemory);
		request.setAttribute("maxMemory", maxMemory);
		request.setAttribute("useableMemory", useableMemory);
		return SUCCESS;
	}
	
	public String add() {
		String actionType = request.getParameter("actionType");
		String name = request.getParameter("name");
		String nid = request.getParameter("nid");
		String value = request.getParameter("value");
		String msg = "增加成功";
		SystemConfig sysconfig = new SystemConfig();
		sysconfig.setName(name);
		sysconfig.setNid(nid);
		sysconfig.setValue(value);
		sysconfig.setStatus("1");
		
		if (!StringUtils.isBlank(actionType)) {
			if (name==null) {
				
				message("参数名不能为空", "/admin/system/index.html");
				return ADMINMSG;
			}
			if (value==null) {
				
				message("参数值不能为空", "/admin/system/index.html");
				return ADMINMSG;
			}
			if (nid==null) {
				
				message("变量名不能为空", "/admin/system/index.html");
				return ADMINMSG;
			}
			systemService.addSystemConfig(sysconfig);
			message(msg, "/admin/system/index.html");
			return ADMINMSG;
		}
		
		return SUCCESS;
	}


}
