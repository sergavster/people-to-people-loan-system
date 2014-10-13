package com.p2psys.service;

import java.util.List;

import com.p2psys.domain.AccountLog;
import com.p2psys.domain.Areainfo;
import com.p2psys.domain.Attestation;
import com.p2psys.domain.AttestationType;
import com.p2psys.domain.User;
import com.p2psys.domain.UserCache;
import com.p2psys.domain.UserCredit;
import com.p2psys.domain.UserCreditLog;
import com.p2psys.domain.Userinfo;
import com.p2psys.model.AdminInfoModel;
import com.p2psys.model.AttestationModel;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.model.UserCreditLogModel;
import com.p2psys.model.UserCreditModel;
import com.p2psys.model.UserinfoModel;

public interface UserinfoService {

	public Userinfo getUserinfoByUserid(long user_id);

	public void updateUserinfo(UserinfoModel model);

	public List<Areainfo> getAreainfoByPid(String pid);

	public void updateBuilding(UserinfoModel model);

	public void updateCompany(UserinfoModel model);

	public void updateFirm(UserinfoModel model);

	public void updateFinance(UserinfoModel model);

	public void updateContact(UserinfoModel model);

	public void updateMate(UserinfoModel model);

	public void updateEducation(UserinfoModel model);

	public void updateOtherinfo(UserinfoModel model);

	public List<Attestation> getAttestationListByUserid(long user_id);

	public Attestation addAttestation(Attestation att);

	/**
	 * 获取有效证件列表
	 * 
	 * @param user_Id
	 *            status
	 * @Auther:lijie
	 */
	public List<Attestation> getAttestationListByUserid(long user_id, int status);

	/**
	 * 返回List[UserinfoModel]
	 * 
	 * @param page
	 * @return
	 */
	public PageDataList getUserInfoListByPageNumber(int page,
			SearchParam searchParam);

	/**
	 * 获取用户所有信息
	 */
	public UserinfoModel getUserALLInfoModelByUserid(long user_id);
	
	/**
	 * 获取客户证明材料
	 */
	public AttestationModel getUserAllCertifyByUserId(long user_id);

	/**
	 * 根据页数,返回条数Vip状态换回List[UserVipInfo]
	 * 
	 * @param page
	 * @param Max
	 * @param status
	 * @return
	 */
	public PageDataList getUserVipinfo(int page, int max, int status,
			SearchParam p);


	/**
	 * 根据用户名或者email 或 真实姓名搜索
	 * 返回自定义的[DetailUser][某些属性未set]
	 * @param page
	 * @param p
	 * @return
	 */
	public PageDataList getSearchUserInfo(int page,
			SearchParam p);

	/**
	 * 根据用户名或者真实姓名搜索
	 * 
	 * @param page
	 * @param p
	 * @return
	 */
	public PageDataList getSearchUserCertify(int page,
			SearchParam p);
	
	/**
	 * 客户信息管理-搜索用户
	 * 返回List[UserInfoModel]
	 */
	public PageDataList getSearchUserInfo(SearchParam p ,int page);
	
	
	/**
	 * 根据用户名或者真实姓名搜索
	 * 
	 * @param page
	 * @param p
	 * @return
	 */
	public PageDataList getUserCreditList(int page,SearchParam p);
	
	public User getUserinfoByUsername(String username);
	
	public void updateUserCredit(UserCredit userCredit);
	
	public UserCredit getUserCreditByUserId(long user_id);
	
	public void updateUserCache(UserCache userCache);
	
	public void VerifyVipSuccess(UserCache userCache);
	
	// v1.6.7.1 RDPROJECT-100 zza 2013-11-11 start
	/**
	 * vip免年费续期
	 * @param userCache 用户
	 * @param accountLog 自己日志
	 * @param verifyVip 审核状态
	 */
	void verifyVipSuccess(UserCache userCache, AccountLog accountLog, int verifyVip);
	// v1.6.7.1 RDPROJECT-100 zza 2013-11-11 end
	
	public void VerifyVipSuccess(UserCache userCache,AccountLog vipLog, AccountLog inviteLog);
	
	public void VerifyVipFail(UserCache userCache);
	
	public void VerifyVipFail(UserCache userCache,AccountLog accountLog);
	
	public AttestationType getAttestationTypeByTypeId(int typeId);
	
	public Attestation getAttestationById(String id);
	
	public Attestation modifyUserCertifyStatus(Attestation attestation);
	
	public void updateAdmininfo(AdminInfoModel model);
	
	public PageDataList getCreditLogList(int page,SearchParam p);
	
	//增加用户积分日志
	public void addUserCreditLog(UserCreditLog userCreditLog);
	
	public UserCreditLogModel getCreditLogByUserId(long user_id);
	
	public UserCreditModel getCreditModelById(long user_id);
	
	public AdminInfoModel getAllAdminInfoModelByUserid(long user_id);
	
	/**
	 * 更新投标积分
	 * @param userCredit
	 */
	public void updateCreditTenderValue(UserCredit userCredit);
	/**
	 * vip赠送
	 * @param page
	 * @param max
	 * @param status
	 * @param type
	 * @param p
	 * @return
	 */
	public PageDataList getUserVipinfo(int page, int max, int status,int type,
			SearchParam p);
	//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 start
	public List getUserVipinfo(SearchParam p,int type,int status,int ruleStatus);
	public List getUserVipinfo(SearchParam p,int status);
	//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 end

}
