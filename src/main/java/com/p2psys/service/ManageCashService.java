package com.p2psys.service;

import java.util.List;
import java.util.Map;

import com.p2psys.domain.AccountCash;
import com.p2psys.domain.AccountLog;
import com.p2psys.domain.OperationLog;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.model.account.AccountSumModel;

public interface ManageCashService {

	public PageDataList getUserAccount(int page,SearchParam param);
	
	public List getUserAccount(SearchParam param);
	
	public PageDataList getAllCash(int page,SearchParam param);
	
	public List getAllCash(SearchParam param);
	
	public AccountCash getAccountCash(long id);
	
	public void verifyCash(AccountCash cash,AccountLog log,OperationLog operationLog, Map verifyConf);


	public PageDataList getUserOneDayAcount(int page, SearchParam param);
	public List getUserOneDayAcount(SearchParam param);

	
	public PageDataList getTiChengAcount(int page, SearchParam param);
	
	public void bankLog(OperationLog operationLog);
	/**
	 * 好友提成
	 * @param page
	 * @param param
	 * @return
	 */
	public PageDataList getFriendTiChengAcount(int page, SearchParam param);
	public List getFriendTiChengAcount(SearchParam param) ;
	public List getTiChengAcount(SearchParam param);
	
	public double getSumTotal();
	public double getSumUseMoney();
	public double getSumNoUseMoney();
	public double getSumCollection();
	
    // v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 start
//	//垫付
//	public void getAdvanced_insert(Advanced advanced);
//	public void getAdvanced_update(Advanced advanced);
//	public List getAdvancedList();
    // v1.6.7.2 RDPROJECT-576 lhm 2013-12-12 start
	/**
	 * 
	 */
	
	//V1.6.5.3 RDPROJECT-142 liukun 2013-09-11 start
	/**
	 * 提现审核
	 * 
	 
	 * @version 1.0	
	 * @since 2013-9-11
	 */
	public void verifyCashStep(AccountCash cash,AccountLog log,OperationLog operationLog, String verifyStep, int verifyResult, Map verifyConf);
	//V1.6.5.3 RDPROJECT-142 liukun 2013-09-11 end
	
	// v1.6.7.1 RDPROJECT-434 zza 2013-11-13 start
	/**
	 * 后台资金合计（总余额、总可用资金、总冻结资金、总待收资金、总待还金额）
	 * @param param param
	 * @return AccountSumModel
	 */
	AccountSumModel getAccountSum(SearchParam param);
	// v1.6.7.1 RDPROJECT-434 zza 2013-11-13 end
	
}
