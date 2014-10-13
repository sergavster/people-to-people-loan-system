package com.p2psys.dao;

import com.p2psys.domain.UserTrack;

public interface UserTrackDao extends BaseDao {
	public void addUserTrack(UserTrack t);
	public UserTrack getLastUserTrack(long userid);
	public int getUserTrackCount(long userid);
}
