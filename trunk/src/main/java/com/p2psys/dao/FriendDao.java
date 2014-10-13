package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.Friend;

public interface FriendDao extends BaseDao {
	public List<Friend> getFriendsRequest(long user_id);

	public int countFriendsRequest(long user_id);

	// v1.6.6.2 RDPROJECT-288 lhm 2013-10-17 start
	/**
	 * 根据用户ID,状态,好友名称获取好友列表
	 * 
	 * @param user_id 用户ID
	 * @param status 状态
	 * @param username 好友名称
	 * @return 好友列表
	 */
	List<Friend> getFriendsByUserId(long user_id, int status, String username);
	// v1.6.6.2 RDPROJECT-288 lhm 2013-10-17 end 
	List<Friend> getUnVerifyList(long userid, long friendId, int status);

	// v1.6.6.2 RDPROJECT-288 lhm 2013-10-17 start
	/**
	 * 获取用户的好友的个数
	 * 
	 * @param user_id 用户ID
	 * @param username 好友名称
	 * @return 用户的好友的个数
	 */
	int countFriendsByUserId(long user_id, String username);

	// v1.6.6.2 RDPROJECT-288 lhm 2013-10-17 end
	public void addFriend(Friend friend);
	
	public void addFriendRequest(Friend friend);
	
	public void modifyFriendRequest(Friend friend);
	
	public void modifyFriend(Friend friend);
	
	public void modifyFriendStatus(int status,long user_id,long frienduser_id);
	
	public void deleteFriend(long user_id,long frienduser_id);
	
	public void deleteFriendRequest(long user_id,long frienduser_id);
	
}
