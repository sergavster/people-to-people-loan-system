package com.p2psys.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.p2psys.domain.AccountLog;
import com.p2psys.domain.User;
import com.p2psys.domain.UserCache;
import com.p2psys.domain.UserCredit;
import com.p2psys.domain.UserTrack;
import com.p2psys.domain.UserType;
import com.p2psys.model.DetailUser;
import com.p2psys.model.PageDataList;
import com.p2psys.model.RuleModel;
import com.p2psys.model.SearchParam;
import com.p2psys.model.UserCacheModel;
import com.p2psys.model.UserinfoModel;

public interface UserService { 
	public User register(User user);
	/**
	 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部 
	 */
	public User login(String username, String password);

	public boolean checkUsername(String username);

	public boolean checkEmail(String email);

	public void addUserTrack(UserTrack ut);

	public UserTrack getLastUserTrack(long userid);
	/**
	 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部 
	 */
	public DetailUser getDetailUser(long userid);

	public UserCacheModel getUserCacheByUserid(long userid);
	public UserCacheModel getUserCacheByUserid(long userid,long vip_give_status,long type);
	/**
	 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部 
	 */
	public User getUserByName(String username);
	
	public Map<String,String> getUserInName(List<String> nameList);
	/**
	 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部 
	 */
	public User getUserById(long user_id);

	public void addUserCredit(UserCredit uc);

	public UserCacheModel saveUserCache(UserCache uc);
	
	public UserCacheModel applyVip(UserCache uc);

	public int realnameIdentify(UserinfoModel model);

	public User modifyEmail(User user);

	public User modifyPhone(User user);

	public User modifyEmail_status(User user);
	
	public User modifyPhone_status(User user);
	
	public User modifyReal_status(User user);
	
	public User modifyVideo_status(User user);
	
	public User modifyScene_status(User user);
	/**
	 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部 
	 */
	public User modifyUserPwd(User user);
	
	public User modifyPayPwd(User user);

	public User modifyAnswer(User user);
//type=1 前台调用   type=2 后台调用
	public List getKfList(int type);
	public List getKfListWithLimit(int start,int end);
	public List getVerifyUser();

	public List getInvitedUserByUserid(long user_id);

	//审核VIP是否到期
	public UserCacheModel validUserVip(long user_id);
	/**
	 * 
	 * 获取用户证件信息List;
	 * 用到模块 Vip审核-视频-手机-现场
	 
	 * @param page
	 * @return
	 */
	public PageDataList getUserList(int page,SearchParam param);

	/**
	 * 更新用户信息
	 * 
	 * @param user
	 */
	public void updateuser(User user);

	/**
	 * 模糊查询用户
	 * 
	 */
	//public PageDataList getSearchUserOrRealName(int page,SearchParam p);

	/**
	 * 获取用户类型:比如普通用户,客服等
	 * 
	 */
	public List getAllUserType();
	
	public UserType getUserTypeById(long id);
	/**
	 * 批量更新UserType
	 * @param list
	 */
	public void updateUserTypeByList(List list);
	
	/**
	 * 添加UserType
	 */
	public void addUserType(UserType usertype );
	
	/**
	 * 根据ID删除userType
	 */
	public void deleteUserTypeById(long id);
	
	public PageDataList getInviteUserList(int page ,SearchParam param);
	
	
	/**
	 * 根据Type得到不同类型的用户列表
	 */
	public PageDataList getUserList(int page,SearchParam param,int type);
	public List getUserList(SearchParam param);
	
	public DetailUser getDetailUser(long userid,int type);
	
	public UserCacheModel applyVip(UserCache uc ,AccountLog accountLog);
	
	public int userTrackCount(long user_id);
	
	public void updateUserLastInfo(User user);
	
	public User getUserByCardNO(String card);
	
	/**
	 * 新增方法：根据用户类型获得该类型所有的用户
	 * 修改日期：2013-3-21
	 */
	public List getAllUser(int type);
	
	public PageDataList getVipStatistic(int page,SearchParam param);
	
	public List getVipStatistic(SearchParam param);

	public int userCount();
	// v1.6.5.3 RDPROJECT-147 xx 2013.09.11 start
	/**
	 * 短信群发
	 * @param param
	 */
	public void sms(SearchParam param,String content);
	// v1.6.5.3 RDPROJECT-147 xx 2013.09.11 end
	
	public List getInviteUserList(SearchParam param);
	
	public int getLoginFailTimes(long user_id);

	public void cleanLoginFailTimes(long user_id);

	public void updateLoginFailTimes(long user_id);

	public void updateUserIsLock(long user_id);
	
	public  PageDataList getUserWithBirth(String start, String now,SearchParam param,int page);
	/**
	 * 每日新注册用户数
	 */
	public int getDayRegister();
	
	/**
	 * 最近一周未完成四项认证的用户
	 * @return
	 */
	public List getWeekUnAttestation(String addTime, String endTime);
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-13 start
	public void updateSmspayEndtime(long userid, int addtime);
	
	/*public void updateSmstypeConfig(long userid, String smstypeConfig);
	
	public boolean isSmssend(long userid, Smstype smstype);*/
	
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-13 start
	
	/**
	 * 根据user id修改临时会员用户名
	 * @param user_id
	 * @param username 会员名
	 * @param type_id 会员类型，必须为999才可以修改，999为临时账户、
	 * 注：临时会员有一次修改用户名的机会，修改之后，将会员类型转为普通用户
	 */
	public Boolean editUser(long user_id ,int type_id, String username);
	
	public void deleteOnlineKfById(long id);
	
	public void modifyOnlineKf(User user);
	
	public void addUser(User user);
	/**
	 * 一个用户只能使用唯一的手机号实现
	 * @param phone
	 * @return
	 */
	public List getOnlyPhone(String phone);
	// v1.6.6.2 RDPROJECT-271 lhm 2013-10-17 start
//	public List getOnlyPhone(User user);
	boolean isPhoneExist(User user);
	// v1.6.6.2 RDPROJECT-271 lhm 2013-10-17 end
	
	/**
	 * 获取vip到期的用户
	 * @param status status
	 * @param endTime endTime
	 */
	void doAutoUpdateExpireUser();
	public void updateusercache(UserCache userCache);
	
	// v1.6.6.2 RDPROJECT-235 zza 2013-10-17 start
	/**
	 * 更新邀请人对应的推广奖励规则Id
	 * @param invite_userid 邀请人id
	 * @param startTime 规则启用时间
	 */
	void updateRulePromoteId(String invite_userid, String startTime);
	// v1.6.6.2 RDPROJECT-235 zza 2013-10-17 end
	
	//v1.6.6.2 RDPROJECT-297 liukun 2013-10-21 begin
	/**
	 * @return 是否禁止提现
	 */
	public int isCashForbid(long userid);
	/**
	 * @return 更新有
	 */
	public boolean updateUserCashForbid(long userid, int cashForbid);
	/**
	 * 获取用户的状态说明
	 * @param userid 用户id
	 * @return dw_user_cache中的status_desc
	 */
	public String getUserStatusDesc(long userid);
	/**
	 * 更新用户的状态说明
	 * @param userid 用户id
	 * @param statusDesc 用户的状态说明
	 * @return 更新成功与否
	 */
	public boolean updateUserStatusDesc(long userid, String statusDesc);
	//v1.6.6.2 RDPROJECT-297 liukun 2013-10-21 end
	
	//v1.6.6.1 RDPROJECT-301 liukun 2013-10-28 begin
	/**
	 * 查询某种类型的用户列表，只返回用户名和用户ID
	 * @param type 类型 all全部用户
	 * @return list
	 */
	public List getUserList(String type);
	//v1.6.6.1 RDPROJECT-301 liukun 2013-10-28 begin

	// v1.6.7.1 RDPROJECT-104 xx 2013-11-11 start
	/**
	 * RSA解密
	 * @param str
	 * @param encrypt
	 * @return
	 * @throws Exception 
	 */
	public String getRSADecrypt(String str,int encrypt) throws Exception;
	// v1.6.7.1 RDPROJECT-104 xx 2013-11-11 end
	
	// v1.6.7.1 RDPROJECT-289 sj 2013.11.8 start
	/**
	 * 查询重复的证件号码
	 * @param type card_id 证件号码
	 * @return int
	 */
	public int getCountCard(String card_id);
	// v1.6.7.1 RDPROJECT-289 sj 2013.11.8 end
	
	// v1.6.7.1 sj 2013-11-13 start
	//v1.6.7.1 安全优化 sj 2013-11-18 start
	/**
	  * 修改密码
	  * 
	  * */
	//v1.6.7.1 安全优化 sj 2013-11-18 end
	public void modifypassword(User user);
	//v1.6.7.1 安全优化 sj 2013-11-18 start
	/**
	 * 查询修改时间
	 * 
	 * */
	//v1.6.7.1 安全优化 sj 2013-11-18 end
	public String selectModifyTime(long user_id);
	//v1.6.7.1 安全优化 sj 2013-11-18 start
	/**
	 * 新增用户插入到dw_user_cahce一条数据
	 * 
	 * */
	//v1.6.7.1 安全优化 sj 2013-11-18 end
	public void insertRegisterUserCache(UserCache userCache);
	//修改密码更新dw_user_cahce时间
	public void updateUserCacheTime(UserCache userCache);
	// v1.6.7.1 sj 2013-11-13 end
	
	//v1.6.7.1 安全优化 sj 2013-11-20 start
	/**
	 * 更新锁定时间
	 * 
	 * */
	public void updateLockTime(long user_id,String lock_time);
	/**
	 * 得到锁定时间
	 * 
	 * */
	public String getLockTime(long user_id);
	/**
	 *更新是否解锁 
	 * 
	 * */
	public void updateIsLock(long user_id, int number);
	
	/**
	 *更新登录失败次数为1
	 * 
	 * */
	public void updateFailTimes(long user_id);
	
	/**
	 * 后台登录锁定处理
	 * @param u
	 * @return
	 */
	public String adminLoginIsLock(boolean loginSuccess, User u, RuleModel safetyRule);
	//v1.6.7.1 安全优化 sj 2013-11-20 end
	
	//v1.6.7.1 安全优化 sj 2013-11-27 start
	/**
	 * 更新用户登录时间
	 * @param user_id
	 * @return
	 */
	public void updateLoginTime(long user_id);
	//v1.6.7.1 安全优化 sj 2013-11-27 end
	//v1.6.7.2  RDPROJECT-430 lx 2013-12-02 start
	/**
	* 根据传入用户的邮箱获取邮箱对应的登陆地址
	* @param email：用户邮箱
	* @return 邮箱对应的登陆地址
	*/
	public String getEmailUrl(String email);
	//v1.6.7.2  RDPROJECT-430 lx 2013-12-02 end
	
	//v1.6.7.2  安全优化  sj 2013-12-03 start
	/**
	* 用户一段时间（时间可配置）没有对平台进行操作，对用户进行冻结
	* @param safetyRule,u
	* @return msg
	*/
	public String fontFreezeUser(RuleModel safetyRule, User u);
	
	//v1.6.7.2  安全优化  sj 2013-12-03 end
	
	// v1.6.7.2 RDPROJECT-505 zza 2013-12-04 start
	/**
	 * 获取手机验证码
	 * @param phone 页面传过来的手机号码
	 * @param user 当前登录的用户
	 * @param session session 
	 * @param date date
	 * @return String
	 */
	public String phoneCode(String phone, User user, Map<String, Object> session, long date);
	// v1.6.7.2 RDPROJECT-505 zza 2013-12-04 end
	//v1.6.7.1RDPROJECT-510 cx 2013-12-09 start
	public Integer getInviteUserCount(SearchParam param);
	public String perferTotalMoney(String userId);
	//v1.6.7.1RDPROJECT-510 cx 2013-12-09 end
	// v1.6.7.2 RDPROJECT-545 lx 2013-12-10 start
	public void modifyUserType(User user);
	public PageDataList getKfList(SearchParam param, int page);
	// v1.6.7.2 RDPROJECT-545 lx 2013-12-10 end
}