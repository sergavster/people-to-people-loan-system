package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.Attestation;
import com.p2psys.domain.AttestationType;
import com.p2psys.domain.User;
import com.p2psys.domain.Userinfo;
import com.p2psys.model.AdminInfoModel;
import com.p2psys.model.AttestationModel;
import com.p2psys.model.SearchParam;
import com.p2psys.model.UserinfoModel;

public interface UserinfoDao extends BaseDao {

	public Userinfo getUserinfoByUserid(long userid);

	public void updateUserinfo(Userinfo info);

	public void addUserinfo(Userinfo info);

	public void updateBuilding(Userinfo i);

	public void addBuilding(Userinfo i);

	public void updateCompany(Userinfo i);

	public void addCompany(Userinfo i);

	public void updateFirm(Userinfo i);

	public void addFirm(Userinfo i);

	public void updateFinance(Userinfo i);

	public void addFinance(Userinfo i);

	public void updateContact(Userinfo i);

	public void addContact(Userinfo i);

	public void updateMate(Userinfo i);

	public void addMate(Userinfo i);

	public void updateEducation(Userinfo i);

	public void addEducation(Userinfo i);

	public void updateOtherinfo(Userinfo info);

	public void addOtherinfo(Userinfo info);
	
	
	/**
	 * 客户证明材料-获取用户信息  返回list<UserInfoModel>
	 * @param i
	 * @param Max
	 * @return  
	 * 
	 */
	public List getUserInfoBypageNumber(int i, int Max);

	/**
	 * 获取用户所有信息包括证件等
	 * 
	 * @param id
	 * @return
	 */
	public UserinfoModel getUserAllinfoByUserId(long id);

	/**
	 * 获取用户信息总数
	 */
	public int getUserInfocount();
	
	public int getUserInfocount(SearchParam param);
	/**
	 * 客户信息管理-搜索用户
	 * 返回List<UserInfoModel>
	 */
	public List getUserInfoBySearch(int page, int max, SearchParam p);

	/**
	 * 用户管理-根据用户名或者姓名 Email进行搜索
	 * 
	 * @return
	 */
	public List getSearchUserInfo(int page, int max, SearchParam p);
	
	/**
	 * 获取指定条件查询结果的总数
	 * @param p
	 * @return
	 */
	public int getSearchUserInfo(SearchParam p);
	
	public User getUserinfoByUsername(String username);
	
	public AdminInfoModel getAdminAllinfoByUserId(long id);
	
	public Attestation modifyUserCertifyStatus(Attestation attestation);
	
	public AttestationType getAttestationTypeByTypeId(int type_id);
	
	public Attestation getAttestationById(String id);
	/**
	 * 获取用户证明材料
	 * 
	 * @param id
	 * @return
	 */
	public AttestationModel getUserAllCertifyByUserId(long id);
	
	public int getSearchUserCertify(SearchParam p);
	
	public List getSearchUserCertify(int page, int max, SearchParam p);
	

}
