package com.p2psys.domain;

import com.p2psys.common.constant.ConsStatusRecord;
import com.p2psys.util.DateUtils;

//v1.6.7.2 RDPROJECT-579 xx 2013-12-10 start
//新增
//v1.6.7.2 RDPROJECT-579 xx 2013-12-10 end
/**
 * dw_status_record 实体类
 * 
 
 * @version 1.0
 * @since 2013-12-10
 */ 
public class StatusRecord {
	/** 主键 */
	private long id;
	/** 操作类型（标审核、还款等） */
	private byte type;
	/** 外键ID */
	private long fk_id;
	/** 当前状态/处理前状态 */
	private byte current_status;
	/** 目标状态 */
	private byte target_status;
	/** 处理结果 */
	private byte result;
	/** 备注 */
	private String remark;
	/** 添加时间 */
	private int addtime;
	/** 状态（0：作废，1：起效） */
	private byte status;
	
	public StatusRecord(byte type, long fk_id, byte current_status,
			byte target_status, byte result, String remark) {
		super();
		this.type = type;
		this.fk_id = fk_id;
		this.current_status = current_status;
		this.target_status = target_status;
		this.result = result;
		this.remark = remark;
		this.addtime = DateUtils.getNowTime();
		this.status = ConsStatusRecord.SR_STATUS_VALID;
	}
	
	/**
	 * 获取主键
	 * 
	 * @return 主键
	 */
	public long getId(){
		return id;
	}

	/**
	 * 设置主键
	 * 
	 * @param id 要设置的主键
	 */
	public void setId(long id){
		this.id=id;
	}

	/**
	 * 获取操作类型（标审核、还款等）
	 * 
	 * @return 操作类型（标审核、还款等）
	 */
	public byte getType(){
		return type;
	}

	/**
	 * 设置操作类型（标审核、还款等）
	 * 
	 * @param type 要设置的操作类型（标审核、还款等）
	 */
	public void setType(byte type){
		this.type=type;
	}

	/**
	 * 获取外键ID
	 * 
	 * @return 外键ID
	 */
	public long getFk_id(){
		return fk_id;
	}

	/**
	 * 设置外键ID
	 * 
	 * @param fk_id 要设置的外键ID
	 */
	public void setFk_id(long fk_id){
		this.fk_id=fk_id;
	}

	/**
	 * 获取当前状态/处理前状态
	 * 
	 * @return 当前状态/处理前状态
	 */
	public byte getCurrent_status(){
		return current_status;
	}

	/**
	 * 设置当前状态/处理前状态
	 * 
	 * @param current_status 要设置的当前状态/处理前状态
	 */
	public void setCurrent_status(byte current_status){
		this.current_status=current_status;
	}

	/**
	 * 获取目标状态
	 * 
	 * @return 目标状态
	 */
	public byte getTarget_status(){
		return target_status;
	}

	/**
	 * 设置目标状态
	 * 
	 * @param target_status 要设置的目标状态
	 */
	public void setTarget_status(byte target_status){
		this.target_status=target_status;
	}

	/**
	 * 获取处理结果
	 * 
	 * @return 处理结果
	 */
	public byte getResult(){
		return result;
	}

	/**
	 * 设置处理结果
	 * 
	 * @param result 要设置的处理结果
	 */
	public void setResult(byte result){
		this.result=result;
	}

	/**
	 * 获取备注
	 * 
	 * @return 备注
	 */
	public String getRemark(){
		return remark;
	}

	/**
	 * 设置备注
	 * 
	 * @param remark 要设置的备注
	 */
	public void setRemark(String remark){
		this.remark=remark;
	}

	/**
	 * 获取添加时间
	 * 
	 * @return 添加时间
	 */
	public int getAddtime(){
		return addtime;
	}

	/**
	 * 设置添加时间
	 * 
	 * @param addtime 要设置的添加时间
	 */
	public void setAddtime(int addtime){
		this.addtime=addtime;
	}

	/**
	 * 获取状态（0：作废，1：起效）
	 * 
	 * @return 状态（0：作废，1：起效）
	 */
	public byte getStatus(){
		return status;
	}

	/**
	 * 设置状态（0：作废，1：起效）
	 * 
	 * @param status 要设置的状态（0：作废，1：起效）
	 */
	public void setStatus(byte status){
		this.status=status;
	}
}
