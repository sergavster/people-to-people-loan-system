package com.p2psys.service;

import java.util.List;

import com.p2psys.domain.Friend;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;

public interface FriendService {
	/**
	 * 获取好友请求的列表
	 * @param user_id
	 * @return
	 */
	public List<Friend> getFriendsRequest(long user_id);
	/**
	 * 统计好友请求个数
	 * @param user_id
	 * @return
	 * 
	 */
	public int countFriendsRequest(long user_id);
	/**
	 * 根据user_id获取好友列表
	 * @param user_id
	 * @param username
	 * @return 好友列表
	 */
	// v1.6.6.2 RDPROJECT-288 lhm 2013-10-17 start 
	public List<Friend> getFriendsByUserId(long user_id, String username);
	/**
	 * 获取用户的好友的个数
	 * @param user_id
	 * @param username
	 * @return 用户的好友的个数
	 */
	public int countFriendsByUserId(long user_id, String username);
	// v1.6.6.2 RDPROJECT-288 lhm 2013-10-17 end
	/**
	 * 新增好友
	 * @param friend
	 */
	public void addFriend(Friend friend);
	/**
	 * 新增用户请求
	 * @param friend
	 */
	public void addFriendRequest(Friend friend);
	
	/**
	 * 新增好友黑名单
	 * @param user_id
	 * @param username
	 */
	public void addBlackFriend(long user_id,String username) ;
	
	/**
	 * 新增好友黑名单
	 * @param friend
	 */
	public void addBlackFriend(Friend friend) ;
	
	/**
	 * 删除好友
	 * @param user_id
	 * @param username
	 */
	public void delFriend(long user_id,String username) ;
	
	/**
	 * 重新新增好友
	 * @param user_id
	 * @param username
	 */
	public void readdFriend(long user_id,String username) ;
	
	public List getBlackList(long userid);
	/**
	 * 好友提成
	 * @param page
	 * @param param
	 * @return
	 */
	public PageDataList getTiChengAcount(int page, SearchParam param);
	public PageDataList getFriendTiChengAcount(int page, SearchParam param);
	List<Friend> getUnVerifyList(long userid, long friendId);
	List<Friend> getFriendList(long userid, long friendId);
}
