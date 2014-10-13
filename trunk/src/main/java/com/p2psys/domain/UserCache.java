package com.p2psys.domain;
/**
 * 实体类
 
 * @version 1.0    
 * @since 2013-12-02 
 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部
 */
public class UserCache implements java.io.Serializable {

	private static final long serialVersionUID = -889562087705028166L;
	
	private long user_id;
	private long kefu_userid;
	private String	kefu_username;
	private String	kefu_addtime;
	private int	vip_status;
	private String vip_remark;
	private String vip_money;
	private String vip_verify_remark;
	private String vip_verify_time;
	private int	bbs_topics_num;
	private int	bbs_posts_num;
	private int	credit;
	private int	account;
	private int	account_use;
	private int	account_nouse;
	private int	account_waitin;
	private int	account_waitintrest;
	private int	account_intrest;
	private int	account_award;
	private int	account_payment;
	private int	account_expired;
	private int	account_waitvip;
	private int	borrow_amount;
	private int	vouch_amount;
	private int	borrow_loan;
	private int	borrow_success;
	private int	borrow_wait;
	private int	borrow_paymeng;
	private int	friends_apply;
	//登录失败次数
	private int login_fail_times;
	//vip失效时间
	private String vip_end_time;
	//vip类型
	private long type;
	//vip赠送状态
	private long vip_give_status;
	//vip赠送月份
	private long vip_give_month;
	
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-12 start
	//短信付费到期时间
//	private String smspay_endtime;
	//private String smstype_config;
	private long smspay_endtime;
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-12 end
	
	//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 start
	private String phone_verify_time;
	private String realname_verify_time;
	private String video_verify_time;
	private String scene_verify_time;
	//v1.6.7.1  RDPROJECT-354 wcw 2013-11-05 end
	
	//v1.6.7.1  安全优化  sj 2013-11-14 start
	private long pwd_modify_time;
	
	public long getPwd_modify_time() {
		return pwd_modify_time;
	}
	public void setPwd_modify_time(long pwd_modify_time) {
		this.pwd_modify_time = pwd_modify_time;
	}
	//v1.6.7.1  安全优化  sj 2013-11-14 end
	public String getPhone_verify_time() {
		return phone_verify_time;
	}
	public void setPhone_verify_time(String phone_verify_time) {
		this.phone_verify_time = phone_verify_time;
	}
	public String getRealname_verify_time() {
		return realname_verify_time;
	}
	public void setRealname_verify_time(String realname_verify_time) {
		this.realname_verify_time = realname_verify_time;
	}
	public String getVideo_verify_time() {
		return video_verify_time;
	}
	public void setVideo_verify_time(String video_verify_time) {
		this.video_verify_time = video_verify_time;
	}
	public String getScene_verify_time() {
		return scene_verify_time;
	}
	public void setScene_verify_time(String scene_verify_time) {
		this.scene_verify_time = scene_verify_time;
	}
	public long getVip_give_month() {
		return vip_give_month;
	}
	public void setVip_give_month(long vip_give_month) {
		this.vip_give_month = vip_give_month;
	}
	public long getVip_give_status() {
		return vip_give_status;
	}
	public void setVip_give_status(long vip_give_status) {
		this.vip_give_status = vip_give_status;
	}
	public long getType() {
		return type;
	}
	public void setType(long type) {
		this.type = type;
	}
	public String getVip_end_time() {
		return vip_end_time;
	}
	public void setVip_end_time(String vip_end_time) {
		this.vip_end_time = vip_end_time;
	}
	public int getLogin_fail_times() {
		return login_fail_times;
	}
	public void setLogin_fail_times(int login_fail_times) {
		this.login_fail_times = login_fail_times;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public long getKefu_userid() {
		return kefu_userid;
	}
	public void setKefu_userid(long kefu_userid) {
		this.kefu_userid = kefu_userid;
	}
	
	public String getKefu_username() {
		return kefu_username;
	}
	public void setKefu_username(String kefu_username) {
		this.kefu_username = kefu_username;
	}
	public String getKefu_addtime() {
		return kefu_addtime;
	}
	public void setKefu_addtime(String kefu_addtime) {
		this.kefu_addtime = kefu_addtime;
	}
	public int getVip_status() {
		return vip_status;
	}
	public void setVip_status(int vip_status) {
		this.vip_status = vip_status;
	}
	public String getVip_remark() {
		return vip_remark;
	}
	public void setVip_remark(String vip_remark) {
		this.vip_remark = vip_remark;
	}
	public String getVip_money() {
		return vip_money;
	}
	public void setVip_money(String vip_money) {
		this.vip_money = vip_money;
	}
	public String getVip_verify_remark() {
		return vip_verify_remark;
	}
	public void setVip_verify_remark(String vip_verify_remark) {
		this.vip_verify_remark = vip_verify_remark;
	}
	public String getVip_verify_time() {
		return vip_verify_time;
	}
	public void setVip_verify_time(String vip_verify_time) {
		this.vip_verify_time = vip_verify_time;
	}
	public int getBbs_topics_num() {
		return bbs_topics_num;
	}
	public void setBbs_topics_num(int bbs_topics_num) {
		this.bbs_topics_num = bbs_topics_num;
	}
	public int getBbs_posts_num() {
		return bbs_posts_num;
	}
	public void setBbs_posts_num(int bbs_posts_num) {
		this.bbs_posts_num = bbs_posts_num;
	}
	public int getCredit() {
		return credit;
	}
	public void setCredit(int credit) {
		this.credit = credit;
	}
	public int getAccount() {
		return account;
	}
	public void setAccount(int account) {
		this.account = account;
	}
	public int getAccount_use() {
		return account_use;
	}
	public void setAccount_use(int account_use) {
		this.account_use = account_use;
	}
	public int getAccount_nouse() {
		return account_nouse;
	}
	public void setAccount_nouse(int account_nouse) {
		this.account_nouse = account_nouse;
	}
	public int getAccount_waitin() {
		return account_waitin;
	}
	public void setAccount_waitin(int account_waitin) {
		this.account_waitin = account_waitin;
	}
	public int getAccount_waitintrest() {
		return account_waitintrest;
	}
	public void setAccount_waitintrest(int account_waitintrest) {
		this.account_waitintrest = account_waitintrest;
	}
	public int getAccount_intrest() {
		return account_intrest;
	}
	public void setAccount_intrest(int account_intrest) {
		this.account_intrest = account_intrest;
	}
	public int getAccount_award() {
		return account_award;
	}
	public void setAccount_award(int account_award) {
		this.account_award = account_award;
	}
	public int getAccount_payment() {
		return account_payment;
	}
	public void setAccount_payment(int account_payment) {
		this.account_payment = account_payment;
	}
	public int getAccount_expired() {
		return account_expired;
	}
	public void setAccount_expired(int account_expired) {
		this.account_expired = account_expired;
	}
	public int getAccount_waitvip() {
		return account_waitvip;
	}
	public void setAccount_waitvip(int account_waitvip) {
		this.account_waitvip = account_waitvip;
	}
	public int getBorrow_amount() {
		return borrow_amount;
	}
	public void setBorrow_amount(int borrow_amount) {
		this.borrow_amount = borrow_amount;
	}
	public int getVouch_amount() {
		return vouch_amount;
	}
	public void setVouch_amount(int vouch_amount) {
		this.vouch_amount = vouch_amount;
	}
	public int getBorrow_loan() {
		return borrow_loan;
	}
	public void setBorrow_loan(int borrow_loan) {
		this.borrow_loan = borrow_loan;
	}
	public int getBorrow_success() {
		return borrow_success;
	}
	public void setBorrow_success(int borrow_success) {
		this.borrow_success = borrow_success;
	}
	public int getBorrow_wait() {
		return borrow_wait;
	}
	public void setBorrow_wait(int borrow_wait) {
		this.borrow_wait = borrow_wait;
	}
	public int getBorrow_paymeng() {
		return borrow_paymeng;
	}
	public void setBorrow_paymeng(int borrow_paymeng) {
		this.borrow_paymeng = borrow_paymeng;
	}
	public int getFriends_apply() {
		return friends_apply;
	}
	public void setFriends_apply(int friends_apply) {
		this.friends_apply = friends_apply;
	}
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-13 start
	public long getSmspay_endtime() {
		return smspay_endtime;
	}
	public void setSmspay_endtime(long smspay_endtime) {
		this.smspay_endtime = smspay_endtime;
	}  
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-13 end
}
