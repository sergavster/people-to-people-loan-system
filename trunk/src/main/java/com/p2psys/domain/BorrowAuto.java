package com.p2psys.domain;

import java.io.Serializable;

public class BorrowAuto implements Serializable {
	
	@Override
	public String toString() {
		return "BorrowAuto [id=" + id + ", status=" + status + ", tender_type="
				+ tender_type + ", tender_account=" + tender_account
				+ ", tender_scale=" + tender_scale + ", video_status="
				+ video_status + ", scene_status=" + scene_status
				+ ", my_friend=" + my_friend + ", not_black=" + not_black
				+ ", late_status=" + late_status + ", dianfu_status="
				+ dianfu_status + ", black_status=" + black_status
				+ ", late_times=" + late_times + ", dianfu_times="
				+ dianfu_times + ", black_user=" + black_user
				+ ", black_times=" + black_times + ", not_late_black="
				+ not_late_black + ", borrow_credit_status="
				+ borrow_credit_status + ", borrow_credit_first="
				+ borrow_credit_first + ", borrow_credit_last="
				+ borrow_credit_last + ", borrow_style_status="
				+ borrow_style_status + ", borrow_style=" + borrow_style
				+ ", timelimit_status=" + timelimit_status
				+ ", timelimit_month_first=" + timelimit_month_first
				+ ", timelimit_month_last=" + timelimit_month_last
				+ ", apr_status=" + apr_status + ", apr_first=" + apr_first
				+ ", apr_last=" + apr_last + ", award_status=" + award_status
				+ ", award_first=" + award_first + ", award_last=" + award_last
				+ ", vouch_status=" + vouch_status + ", tuijian_status="
				+ tuijian_status + ", user_id=" + user_id + ", addtime="
				+ addtime + ", fast_status=" + fast_status + ", xin_status="
				+ xin_status + ", jin_status=" + jin_status + "]";
	}
	private static final long serialVersionUID = 354246913406733300L;
	
	private long id;
	private int status;
	private int tender_type;
	private int tender_account;
	private int tender_scale;
	private int video_status;
	private int scene_status;
	private int my_friend;
	private int not_black;
	private int late_status;
	private int dianfu_status;
	private int black_status;
	private int late_times;
	private int dianfu_times;
	private int black_user;
	private int black_times;
	private int not_late_black;
	private int borrow_credit_status;
	private int borrow_credit_first;
	private int borrow_credit_last;
	private int borrow_style_status;
	private int borrow_style;
	private int timelimit_status;
	private int timelimit_month_first;
	private int timelimit_month_last;
	private int timelimit_day_first;
	private int timelimit_day_last;
	public int getTimelimit_day_first() {
		return timelimit_day_first;
	}
	public void setTimelimit_day_first(int timelimit_day_first) {
		this.timelimit_day_first = timelimit_day_first;
	}
	public int getTimelimit_day_last() {
		return timelimit_day_last;
	}
	public void setTimelimit_day_last(int timelimit_day_last) {
		this.timelimit_day_last = timelimit_day_last;
	}
	private int apr_status;
	private int apr_first;
	private int apr_last;
	private int award_status;
	private double award_first;
	private double award_last;
	private int vouch_status;
	private int tuijian_status;
	private long user_id;
	private int addtime;
	private int fast_status;
	private int xin_status;
	private int jin_status;
	//V1.6.7.1 RDPROJECT-345 liukun 2013-11-07 start
	private int flow_status;
	//V1.6.7.1 RDPROJECT-345 liukun 2013-11-07 end
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getTender_type() {
		return tender_type;
	}
	public void setTender_type(int tender_type) {
		this.tender_type = tender_type;
	}
	public int getTender_account() {
		return tender_account;
	}
	public void setTender_account(int tender_account) {
		this.tender_account = tender_account;
	}
	public int getTender_scale() {
		return tender_scale;
	}
	public void setTender_scale(int tender_scale) {
		this.tender_scale = tender_scale;
	}
	public int getVideo_status() {
		return video_status;
	}
	public void setVideo_status(int video_status) {
		this.video_status = video_status;
	}
	public int getScene_status() {
		return scene_status;
	}
	public void setScene_status(int scene_status) {
		this.scene_status = scene_status;
	}
	public int getMy_friend() {
		return my_friend;
	}
	public void setMy_friend(int my_friend) {
		this.my_friend = my_friend;
	}
	public int getNot_black() {
		return not_black;
	}
	public void setNot_black(int not_black) {
		this.not_black = not_black;
	}
	public int getLate_status() {
		return late_status;
	}
	public void setLate_status(int late_status) {
		this.late_status = late_status;
	}
	public int getDianfu_status() {
		return dianfu_status;
	}
	public void setDianfu_status(int dianfu_status) {
		this.dianfu_status = dianfu_status;
	}
	public int getBlack_status() {
		return black_status;
	}
	public void setBlack_status(int black_status) {
		this.black_status = black_status;
	}
	public int getLate_times() {
		return late_times;
	}
	public void setLate_times(int late_times) {
		this.late_times = late_times;
	}
	public int getDianfu_times() {
		return dianfu_times;
	}
	public void setDianfu_times(int dianfu_times) {
		this.dianfu_times = dianfu_times;
	}
	public int getBlack_user() {
		return black_user;
	}
	public void setBlack_user(int black_user) {
		this.black_user = black_user;
	}
	public int getBlack_times() {
		return black_times;
	}
	public void setBlack_times(int black_times) {
		this.black_times = black_times;
	}
	public int getNot_late_black() {
		return not_late_black;
	}
	public void setNot_late_black(int not_late_black) {
		this.not_late_black = not_late_black;
	}
	public int getBorrow_credit_status() {
		return borrow_credit_status;
	}
	public void setBorrow_credit_status(int borrow_credit_status) {
		this.borrow_credit_status = borrow_credit_status;
	}
	public int getBorrow_credit_first() {
		return borrow_credit_first;
	}
	public void setBorrow_credit_first(int borrow_credit_first) {
		this.borrow_credit_first = borrow_credit_first;
	}
	public int getBorrow_credit_last() {
		return borrow_credit_last;
	}
	public void setBorrow_credit_last(int borrow_credit_last) {
		this.borrow_credit_last = borrow_credit_last;
	}
	public int getBorrow_style_status() {
		return borrow_style_status;
	}
	public void setBorrow_style_status(int borrow_style_status) {
		this.borrow_style_status = borrow_style_status;
	}
	public int getBorrow_style() {
		return borrow_style;
	}
	public void setBorrow_style(int borrow_style) {
		this.borrow_style = borrow_style;
	}
	public int getTimelimit_status() {
		return timelimit_status;
	}
	public void setTimelimit_status(int timelimit_status) {
		this.timelimit_status = timelimit_status;
	}
	public int getTimelimit_month_first() {
		return timelimit_month_first;
	}
	public void setTimelimit_month_first(int timelimit_month_first) {
		this.timelimit_month_first = timelimit_month_first;
	}
	public int getTimelimit_month_last() {
		return timelimit_month_last;
	}
	public void setTimelimit_month_last(int timelimit_month_last) {
		this.timelimit_month_last = timelimit_month_last;
	}
	public int getApr_status() {
		return apr_status;
	}
	public void setApr_status(int apr_status) {
		this.apr_status = apr_status;
	}
	public int getApr_first() {
		return apr_first;
	}
	public void setApr_first(int apr_first) {
		this.apr_first = apr_first;
	}
	public int getApr_last() {
		return apr_last;
	}
	public void setApr_last(int apr_last) {
		this.apr_last = apr_last;
	}
	public int getAward_status() {
		return award_status;
	}
	public void setAward_status(int award_status) {
		this.award_status = award_status;
	}
	public double getAward_first() {
		return award_first;
	}
	public void setAward_first(double award_first) {
		this.award_first = award_first;
	}
	public double getAward_last() {
		return award_last;
	}
	public void setAward_last(double award_last) {
		this.award_last = award_last;
	}
	public int getVouch_status() {
		return vouch_status;
	}
	public void setVouch_status(int vouch_status) {
		this.vouch_status = vouch_status;
	}
	public int getTuijian_status() {
		return tuijian_status;
	}
	public void setTuijian_status(int tuijian_status) {
		this.tuijian_status = tuijian_status;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public int getAddtime() {
		return addtime;
	}
	public void setAddtime(int addtime) {
		this.addtime = addtime;
	}
	public int getFast_status() {
		return fast_status;
	}
	public void setFast_status(int fast_status) {
		this.fast_status = fast_status;
	}
	public int getXin_status() {
		return xin_status;
	}
	public void setXin_status(int xin_status) {
		this.xin_status = xin_status;
	}
	public int getJin_status() {
		return jin_status;
	}
	public void setJin_status(int jin_status) {
		this.jin_status = jin_status;
	}
	//V1.6.7.1 RDPROJECT-345 liukun 2013-11-07 start
	public int getFlow_status() {
		return flow_status;
	}
	public void setFlow_status(int flow_status) {
		this.flow_status = flow_status;
	}
	//V1.6.7.1 RDPROJECT-345 liukun 2013-11-07 end
}
