package com.p2psys.creditassignment.web.action.admin;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.p2psys.context.Constant;
import com.p2psys.creditassignment.domain.CreditAssignment;
import com.p2psys.creditassignment.service.CreditAssignmentService;
import com.p2psys.domain.OperationLog;
import com.p2psys.domain.User;
import com.p2psys.model.CaSearchParam;
import com.p2psys.model.PageDataList;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

public class ManageCaAction extends BaseAction {
	
	private static Logger logger = Logger.getLogger(ManageCaAction.class);
	
	@Resource
	private CreditAssignmentService creditAssignmentService;
	
	private CreditAssignment creditAssignment;
	
	public CreditAssignmentService getCreditAssignmentService() {
		return creditAssignmentService;
	}

	public void setCreditAssignmentService(
			CreditAssignmentService creditAssignmentService) {
		this.creditAssignmentService = creditAssignmentService;
	}

	public CreditAssignment getCreditAssignment() {
		return creditAssignment;
	}

	public void setCreditAssignment(CreditAssignment creditAssignment) {
		this.creditAssignment = creditAssignment;
	}
	
	/**
	 * 债权转让发布数据有效性检查
	 * @return
	 */
	private Map<String , Object> checkVerifyCa(long caId){
		String message = "ok";
		
		CreditAssignment ca = creditAssignmentService.getOne(caId);
		//查不到指定债权
		if(null == ca){
			message = "只能初审状态为初始状态的债权转让";
		}
				
		//只能初审状态为初始状态的债权转让
		if(null != ca && Constant.CA_STATUS_INIT != ca.getStatus()){
			message = "只能初审状态为初始状态的债权转让";
		}
		
		Map<String , Object> resultHM = new HashMap<String , Object>();
		resultHM.put("message", message);
		if(!"ok".equalsIgnoreCase(message)){
			resultHM.put("result", false);
		}else{
			resultHM.put("result", true);
		}
		
		return resultHM;
	}

	//初审债权转让
	public String verifyCa(){
		User auth_user=(User) session.get(Constant.AUTH_USER);
		
		long caId = NumberUtils.getLong(request.getParameter("caId"));
		int verifyStatus = NumberUtils.getInt(request.getParameter("status"));
		String verifyRemark = StringUtils.isNull(request.getParameter("verifyRemark"));
		
		Map<String , Object> ckr = checkVerifyCa(caId);
		
		boolean ckResult = Boolean.valueOf(ckr.get("result").toString()).booleanValue();
		String message = ckr.get("message").toString();
		
		if (!ckResult){
			request.setAttribute("message", message);
			return FAIL;
		}
		
		CreditAssignment ca = creditAssignmentService.getOne(caId);
		
		if (Constant.CA_STATUS_VERIFY_SUCC == verifyStatus){
			//初审成功
			ca.setStatus(Constant.CA_STATUS_VERIFY_SUCC);
		}else{
			//初审失败
			ca.setStatus(Constant.CA_STATUS_VERIFY_FAIL);
		}
		ca.setVerify_user_id(Integer.parseInt(String.valueOf(auth_user.getUser_id())));
		ca.setVerify_time(DateUtils.getNowTime());
		ca.setVerify_remark(verifyRemark);
		
		//TODO 生成有意义的操作日志
		//操作日志
		OperationLog operationLog=new OperationLog();
		creditAssignmentService.verifyCa(ca, verifyStatus);
		
		return SUCCESS;
	}
	
	public String fullVerifyCa(){
		User auth_user=(User) session.get(Constant.AUTH_USER);
		
		long caId = NumberUtils.getLong(request.getParameter("caId"));
		int verifyStatus = NumberUtils.getInt(request.getParameter("status"));
		String verifyRemark = StringUtils.isNull(request.getParameter("verifyRemark"));
		
		CreditAssignment ca = creditAssignmentService.getOne(caId);
		
		if (Constant.CA_STATUS_FULL_SUCC == verifyStatus){
			//初审成功
			ca.setStatus(Constant.CA_STATUS_FULL_SUCC);
		}else{
			//初审失败
			ca.setStatus(Constant.CA_STATUS_FULL_FAIL);
		}
		ca.setVerify_user_id(Integer.parseInt(String.valueOf(auth_user.getUser_id())));
		ca.setVerify_time(DateUtils.getNowTime());
		ca.setVerify_remark(verifyRemark);
		
		//TODO 生成有意义的操作日志
		//操作日志
		OperationLog operationLog=new OperationLog();
		creditAssignmentService.fullCa(ca, verifyStatus);		
		return SUCCESS;
	}
	
	public String viewCa(){
		long caId = NumberUtils.getLong(request.getParameter("caId"));
		
		CreditAssignment creditAssignment = creditAssignmentService.getOne(caId);
		
		request.setAttribute("ca", creditAssignment);
		return SUCCESS;
	}
	
	public String listOutCas(){
		int page=NumberUtils.getInt(StringUtils.isNull(request.getParameter("page")));
		CaSearchParam param=new CaSearchParam();
		PageDataList pList=null;
		
		//pList=creditAssignmentService.listOutCas(page, param);
		
		setPageAttribute(pList, param);
		request.setAttribute("param", param.toMap());
		return SUCCESS;
	}
	
	
	public String listInCas(){
		
		int page=NumberUtils.getInt(StringUtils.isNull(request.getParameter("page")));
		CaSearchParam param=new CaSearchParam();
		PageDataList pList=null;
		
		pList=creditAssignmentService.listInCas(page, param);
		
		setPageAttribute(pList, param);
		request.setAttribute("param", param.toMap());
		return SUCCESS;
	}
	
	

}
