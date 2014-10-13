package com.p2psys.dao;

import java.util.List;
import java.util.Map;

import com.p2psys.domain.User;
import com.p2psys.domain.UserCache;
import com.p2psys.domain.UserType;
import com.p2psys.model.DetailUser;
import com.p2psys.model.SearchParam;

public interface UserDao extends BaseDao {

	public void addUser(User u);

	public User getUserByUsername(String username);
	
	// v1.6.7.1 注册校验，防止表中有多个相同的用户，把对象改成count zza 2013-11-26 start
	/**
	 * 根据用户名查询数据库里是否已经存在
	 * @param username username
	 * @return int
	 */
	int getCountByUsername(String username);
	// v1.6.7.1 注册校验，防止表中有多个相同的用户，把对象改成count zza 2013-11-26 end
	
	public List<Map<String, Object>> getUserInUsername(List<String> nameList);

	public User getUserById(long user_id);

	public User getUserByEmail(String email);
	
	/**
	 * 通过邮箱计算用户数（用于校验邮箱是否已存在）
	 * @param email
	 * @return
	 */
	public int countByEmail(String email);

	public User getUserByUsernameAndPassword(String username, String password);

	public User getUserByUserid(long userid);

	public DetailUser getDetailUserByUserid(long userid);

	public void updateUser(User user);

	public int realnameIdentify(User user);

	public User modifyEmail(User user);

	public User modifyEmail_status(User user);

	public User modifyPhone(User user);

	public User modifyPhone_status(User user);

	public User modifyVideo_status(User user);

	public User modifyScene_status(User user);

	public User modifyRealname(User user);

	public User modifyReal_status(User user);

	public User modifyUserPwd(User user);
	
	public User modifyAnswer(User user);

	public User modifyPayPwd(User user);

	public List<User> getAllKfList(int type);
	
	public List getKfListWithLimit(int start,int end);
	
	public List<User> getAllVerifyUser();
	
	/**
	 * 根据userid获取受邀请的好友
	 * 
	 * @param id
	 * @return
	 */
	public List getInvitedUserByUserid(long user_id);

	/**
	 
	 * @param page
	 * @param Max
	 * @return 获取用户信息 List[DetailUser][自定义]
	 */
	public List getUserList(int page, int pernum, SearchParam p);

	public List getUserList(SearchParam p);

	public int getUserCount(SearchParam p);
    	// v1.6.5.3 RDPROJECT-147 xx 2013.09.11 start
	public int getUserCount(int phoneStatus);
	public List getUserList(int phoneStatus, int start, int pernum);
	// v1.6.5.3 RDPROJECT-147 xx 2013.09.11 end
	/**
	 * 获取所有会员类型
	 * 
	 * @return
	 */
	public List getAllUserType();

	/**
	 * 后台-更新用户信息
	 * 
	 * @param user
	 * @param modify
	 *            modify为是否修改密码 true 为修改
	 */
	public void updateUser(User user, boolean modify);

	/**
	 * 根据用户名查找用户(模糊查询)
	 * 
	 * @param name
	 * @param Isrealname
	 * @return
	 */
	public List getSearchUserOrRealName(int page, SearchParam p);

	/**
	 * 查找用户结果的总数
	 * 
	 * @return
	 */
	public int getSearchUserOrRealName(SearchParam p);

	/**
	 * 根据type_ID获取用户类型
	 * 
	 * 
	 */
	public String getUserTypeByid(String s);

	public UserType getUserTypeById(long id);

	/**
	 * 批量更新userType
	 */
	public void updateUserType(List<UserType> lsit);

	/**
	 * 添加Usertype
	 */
	public void addUserType(UserType userType);

	public void deleteUserTypeById(long id);

	public boolean isRoleHasPurview(long id);

	public int getInviteUserCount(SearchParam param);

	public List getInviteUserList(int start, int pernum, SearchParam param);

	public int getUserCount(SearchParam p, int type);

	public List getUserList(int page, int pernum, SearchParam p, int type);

	/**
	 * 根据用户Id和类型得到相应的信息
	 * 
	 * @param userid
	 * @param type
	 * @return
	 */
	public DetailUser getDetailUser(long userid, int type);

	public void updateUserLastInfo(User user);

	public User getUserByCard(String card);

	/**
	 * 新增方法：根据类型获得所有该类型的的用户 
	 * 修改日期：2013-3-21
	 * 
	 * @return
	 */
	public List getAllUser(int type);
	
	public int getUserCount();
	
	public List getInviteUserList(SearchParam param);
	
	public void updateUserIsLock(long userid);
	
	/**
	 * 获取vip用户列表
	 * @return
	 */
	public List<User> getVipUser();
	/**
	 * 查找生日在两个时间段中的用户列表的个数
	 * @param startDate
	 * @param nowDate
	 * @return
	 */
	public int getUserCountWithBirth(SearchParam param,String startDate,String nowDate);
	/**
	 * 查找生日在两个时间段中的用户列表
	 * @param startDate
	 * @param nowDate
	 * @return
	 */
	public List getUserListWithBirth(String startDate,String nowDate,SearchParam param, int start, int pernum);
	
	/**
	 * 每日新注册用户数
	 */
	public int getDayRegister();
	
	/**
	 * 最近一周未完成四项认证的用户
	 * @return
	 */
	public List getWeekUnAttestation(String addTime, String endTime);
	
	/**
	 * 根据user id修改临时会员用户名
	 * @param user_id
	 * @param username 会员名
	 * @param type_id 会员类型，必须为999才可以修改，999为临时账户、
	 * 注：临时会员有一次修改用户名的机会，修改之后，将会员类型转为普通用户
	 */
	public void editUser(long user_id ,int type_id, String username);
	
	public void deleteOnlineKfById(long id);
	
	public void modifyOnlineKf(User user);
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
	
	// v1.6.6.2 RDPROJECT-235 zza 2013-10-17 start
	/**
	 * 
	 * @param invite_userid 邀请人Id
	 * @param addtime 注册时间
	 * @return int
	 */
	int getInviteCount(String invite_userid, String addtime);
	
	/**
	 * 更新用户的推广奖励规则Id
	 * @param rule_promote_id 推广奖励规则Id
	 * @param user_id 用户Id
	 */
	void updateRulePromoteId(long rule_promote_id, long user_id);
	// v1.6.6.2 RDPROJECT-235 zza 2013-10-17 end
	
	//v1.6.6.1 RDPROJECT-301 liukun 2013-10-28 begin
	/**
	 * 查询某种类型的用户列表，只返回用户名和用户ID
	 * @param type 类型 all全部用户
	 * @return list
	 */
	public List getUserList(String type);
	//v1.6.6.1 RDPROJECT-301 liukun 2013-10-28 begin 

	// v1.6.7.1 RDPROJECT-289 sj 2013.11.8 start
	/**
	 * 查询重复的证件号码
	 * @param type card_id 证件号码
	 * @return int
	 */
	public int getCountCard(String card_id);
	// v1.6.7.1 RDPROJECT-289 sj 2013.11.8 end
	
	// v1.6.7.1 安全优化 sj 2013-11-13 start
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
	//v1.6.7.1 安全优化 sj 2013-11-18 start
	//public void updateUserCacheTime(UserCache userCache);
	// v1.6.7.1 安全优化  sj 2013-11-13 end

	//v1.6.7.1 安全优化 sj 2013-11-20 start
	/**
	 *更新是否解锁 
	 * 
	 * */
	public void updateIsLock(long user_id, int islock);
	//v1.6.7.1 安全优化 sj 2013-11-20 end
	
	//v1.6.7.1RDPROJECT-510 cx 2013-12-09 start
	/**
	 * 根据user_id 得出 dw_account_log 中money 根据type=back_manage_fee
	 * @param userId
	 * @return
	 */
	public String perferTotalMoney(String userId);
	//v1.6.7.1RDPROJECT-510 cx 2013-12-09 end
	
		//v1.6.7.1 安全优化 sj 2013-11-27 start
	/**
	 * 更新用户登录时间
	 * @param user_id
	 * @return
	 */
	public void updateLoginTime(long user_id);
	//v1.6.7.1 安全优化 sj 2013-11-27 end	
	
	//v1.6.7.2 安全优化 sj 2013-12-03 start
	/**
	 * 自动投标最后的时间
	 * @param user_id
	 * @return
	 */
	public String getAutoAenderAddTime(long user_id);
	//v1.6.7.2 安全优化 sj 2013-12-03 start
	// v1.6.7.2 RDPROJECT-545 lx 2013-12-10 start
	/**
	 * 修改用户类型
	 * @param user
	 * @return
	 */
	public void modifyUserType(User user);
	/**
	 * 获取筛选条件下客服数量
	 * @param param
	 * @return 数量
	 */
	public int getCountKf(SearchParam param);
	/**
	 * 获取筛选条件下客服集合
	 * @param param
	 * @param start
	 * @param pernum
	 * @return List<User>
	 */
	public List<User> getKfList(SearchParam param, int start, int pernum);
	// v1.6.7.2 RDPROJECT-545 lx 2013-12-10 end

}
