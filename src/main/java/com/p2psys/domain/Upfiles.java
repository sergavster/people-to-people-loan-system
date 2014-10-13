package com.p2psys.domain;

import java.io.Serializable;

/**
 * 批量上传文件（.xls）
 * 
 
 * @version 1.0
 * @since 2013-8-20
 */
public class Upfiles implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1420854236398206190L;
	
	/**
	 * 主键ID
	 */
	private long id;
	
	/**
	 * 文件名
	 */
	private String file_name;
	
	/**
	 * 文件路径
	 */
	private String file_path;
	
	/**
	 * 文件备注
	 */
	private String file_remark;
	
	/**
	 * 上传者
	 */
	private long user_id;
	
	/**
	 * 上传时间
	 */
	private String addtime;
	
	/**
	 * 审核人
	 */
	private String verify_user;
	
	/**
	 * 审核人ID
	 */
	private long verify_user_id;
	
	/**
	 * 审核时间
	 */
	private String verify_time;
	
	/**
	 * 状态（0 未审核，1 审核通过， 2 审核失败）
	 */
	private int status;
	
	/**
	 * 类型
	 */
	private String type;

	/**
	 * 获取id
	 * 
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * 设置id
	 * 
	 * @param id 要设置的id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * 获取file_name
	 * 
	 * @return file_name
	 */
	public String getFile_name() {
		return file_name;
	}

	/**
	 * 设置file_name
	 * 
	 * @param file_name 要设置的file_name
	 */
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	/**
	 * 获取file_path
	 * 
	 * @return file_path
	 */
	public String getFile_path() {
		return file_path;
	}

	/**
	 * 设置file_path
	 * 
	 * @param file_path 要设置的file_path
	 */
	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}

	/**
	 * 获取file_remark
	 * 
	 * @return file_remark
	 */
	public String getFile_remark() {
		return file_remark;
	}

	/**
	 * 设置file_remark
	 * 
	 * @param file_remark 要设置的file_remark
	 */
	public void setFile_remark(String file_remark) {
		this.file_remark = file_remark;
	}

	/**
	 * 获取user_id
	 * 
	 * @return user_id
	 */
	public long getUser_id() {
		return user_id;
	}

	/**
	 * 设置user_id
	 * 
	 * @param user_id 要设置的user_id
	 */
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	/**
	 * 获取addtime
	 * 
	 * @return addtime
	 */
	public String getAddtime() {
		return addtime;
	}

	/**
	 * 设置addtime
	 * 
	 * @param addtime 要设置的addtime
	 */
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	/**
	 * 获取verify_user
	 * 
	 * @return verify_user
	 */
	public String getVerify_user() {
		return verify_user;
	}

	/**
	 * 设置verify_user
	 * 
	 * @param verify_user 要设置的verify_user
	 */
	public void setVerify_user(String verify_user) {
		this.verify_user = verify_user;
	}

	/**
	 * 获取verify_user_id
	 * 
	 * @return verify_user_id
	 */
	public long getVerify_user_id() {
		return verify_user_id;
	}

	/**
	 * 设置verify_user_id
	 * 
	 * @param verify_user_id 要设置的verify_user_id
	 */
	public void setVerify_user_id(long verify_user_id) {
		this.verify_user_id = verify_user_id;
	}

	/**
	 * 获取verify_time
	 * 
	 * @return verify_time
	 */
	public String getVerify_time() {
		return verify_time;
	}

	/**
	 * 设置verify_time
	 * 
	 * @param verify_time 要设置的verify_time
	 */
	public void setVerify_time(String verify_time) {
		this.verify_time = verify_time;
	}

	/**
	 * 获取status
	 * 
	 * @return status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * 设置status
	 * 
	 * @param status 要设置的status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 获取type
	 * 
	 * @return type
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置type
	 * 
	 * @param type 要设置的type
	 */
	public void setType(String type) {
		this.type = type;
	}

}
