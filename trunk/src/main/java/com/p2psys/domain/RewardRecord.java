package com.p2psys.domain;
// v1.6.7.2 RDPROJECT-547 lx 2013-12-6 start
// 新增类
// v1.6.7.2 RDPROJECT-547 lx 2013-12-6 end
import java.io.Serializable;
/**
 * 奖励记录
 
 * @version 1.0    
 * @since 2013-12-05 
 */
public class RewardRecord implements Serializable{
	private static final long serialVersionUID = 6474808587371152056L;
	/** 主键ID */
	private long id;
	/**
	 * 信息状态：  1充值奖励， 2邀请人奖励，  3被邀请人奖励， 4投标奖励， 5回款续投奖励
	 */
	private byte type;
	/** 外键ID */
	private long fk_id;
	/**
	 * 收到奖励的会员ID
	 */
	private long reward_user_id;
	/**
	 * 发放奖励的会员ID
	 */
	private long passive_user_id;
	/**
	 * 奖励金额
	 */
	private double reward_account;
	/**
	 * 创建时间
	 */
	private int addtime;
	
	public RewardRecord() {
		super();
	}
	public RewardRecord(long id) {
		super();
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public long getReward_user_id() {
		return reward_user_id;
	}

	public void setReward_user_id(long reward_user_id) {
		this.reward_user_id = reward_user_id;
	}

	public long getPassive_user_id() {
		return passive_user_id;
	}

	public void setPassive_user_id(long passive_user_id) {
		this.passive_user_id = passive_user_id;
	}

	public double getReward_account() {
		return reward_account;
	}

	public void setReward_account(double reward_account) {
		this.reward_account = reward_account;
	}
	public long getFk_id() {
		return fk_id;
	}
	public void setFk_id(long fk_id) {
		this.fk_id = fk_id;
	}
	public int getAddtime() {
		return addtime;
	}
	public void setAddtime(int addtime) {
		this.addtime = addtime;
	}
	public void setType(byte type) {
		this.type = type;
	}
	

	
}
