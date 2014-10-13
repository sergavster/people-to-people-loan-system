package com.p2psys.domain;

import java.io.Serializable;

/**
 * 信用卡实体类
 
 *
 */
public class CreditCard implements Serializable {
	
	@Override
	public String toString() {
		return "CreditCard [card_id=" + card_id + ", name=" + name + ", issuing_bank="
				+ issuing_bank + ", issuing_nstitution=" + issuing_nstitution + ", issuing_status=" + issuing_status
				+ ", check_style=" + check_style + ", interest=" + interest + ", tel=" + tel
				+ ", currency=" + currency + ", grade=" + grade + ", borrowing_limit="
				+ borrowing_limit + ", interest_free=" + interest_free + ", integral_policy="
				+ integral_policy + ", integral_indate=" + integral_indate
				+ ", credit_rules=" + credit_rules + ", scoring_rules="
				+ scoring_rules + ", installment=" + installment + ", amount=" + amount
				+ ", fee_payment=" + fee_payment + ", applicable_fee=" + applicable_fee
				+ ", prepayment=" + prepayment + ", open_card="
				+ open_card + ", repea_card=" + repea_card
				+ ", app_condition=" + app_condition + ", app_way="
				+ app_way + ", submit_info=" + submit_info
				+ ", supplement_num=" + supplement_num
				+ ", supplement_app=" + supplement_app
				+ ", report_loss=" + report_loss
				+ ", loss_protection=" + loss_protection + ", loss_tel="
				+ loss_tel + ", lowest_refund=" + lowest_refund
				+ ", allopatry_back_fee=" + allopatry_back_fee + ", rmb_repayment="
				+ rmb_repayment + ", foreign_repayment=" + foreign_repayment + ", special_repayment=" + special_repayment + ", card_features=" + card_features
				+ ", add_service=" + add_service + ", joint_discount=" + joint_discount
				+ ", main_card_fee=" + main_card_fee + ", supplement_card_fee=" + supplement_card_fee
				+ ", year_cut_rules=" + year_cut_rules + ", fee_date=" + fee_date
				+ ", local_fee=" + local_fee + ", local_interbank_fee="
				+ local_interbank_fee + ", offsite_fee=" + offsite_fee + ", offsite_interbank_fee="
				+ offsite_interbank_fee + ", overseas_pay_fee=" + overseas_pay_fee + ", overseas_unpay_fee=" + overseas_unpay_fee
				+ ", overseas_meet_fee=" + overseas_meet_fee + ", enchashment_limit=" + enchashment_limit
				+ ", localback_fee=" + localback_fee + ", local_interbank_back_fee="
				+ local_interbank_back_fee + ", offsite_overflow_back_fee=" + offsite_overflow_back_fee + ", offsite_interbank_back_fee="
				+ offsite_interbank_back_fee + ", overseas_pay_back_fee=" + overseas_pay_back_fee + ", overseas_unpay_back_fee=" 
				+ overseas_unpay_back_fee + ", overflow_back_rules=" + overflow_back_rules + ", message_money=" 
				+ message_money + ", overseas_fee=" + overseas_fee + ", change_card=" + change_card + ", ahead_change_card=" 
				+ ahead_change_card + ", express_fee=" + express_fee + ", statement_fee=" + statement_fee + ", statement_free_clause="
				+ statement_free_clause + ", loss_fee=" + loss_fee + ", reset_password_fee="
				+ reset_password_fee + ", selfdom_card_fee=" + selfdom_card_fee + ", foreign_convert_fee="
				+ foreign_convert_fee + ", slip_fee=" + slip_fee + ", slip_fee_copy="
				+ slip_fee_copy + ", slip_fee_foreign=" + slip_fee_foreign
				+ ", slip_fee_copy_foreign=" + slip_fee_copy_foreign + ", overdue_fine=" + overdue_fine
				+ ", transfinite_fee=" + transfinite_fee + ", type_value=" + type_value + ", litpic=" + litpic + "]";
	}
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6784728727307847252L;
	
	// 信用卡ID
	private int card_id;
	// 信用卡名
	private String name;
	// 发卡银行
	private String issuing_bank;
	// 发卡组织
	private String issuing_nstitution;
	// 发行状态
	private String issuing_status;
	// 消费验证方式
	private String check_style;
	// 循环利息
	private String interest;
	// 服务热线
	private String tel;
	// 币种
	private String currency;
	// 信用卡等级
	private String grade;
	// 预借现金额度
	private String borrowing_limit;
	// 免息期
	private String interest_free;
	// 积分政策
	private String integral_policy;
	// 积分有效期
	private String integral_indate;
	// 信用额度占用规定
	private String credit_rules;
	// 分期付款积分规定
	private String scoring_rules;
	// 分期付款申请办法
	private String installment;
	// 最低消费额（单位：元）
	private String amount;
	// 手续费支付方式
	private String fee_payment;
	// 各期数手续费
	private String applicable_fee;
	// 提前还款规定
	private String prepayment;
	// 开卡方法
	private String open_card;
	// 销卡方法
	private String repea_card;
	// 申请条件
	private String app_condition;
	// 申请方式
	private String app_way;
	// 提交资料
	private String submit_info;
	// 附属卡最多数量
	private String supplement_num;
	// 附属卡申请条件
	private String supplement_app;
	// 挂失方法
	private String report_loss;
	// 挂失前失卡保护
	private String loss_protection;
	// 服务热线
	private String loss_tel;
	// 最低还款比例
	private String lowest_refund;
	// 异地本行存款手续费
	private String allopatry_back_fee;
	// 人民币还款方式
	private String rmb_repayment;
	// 外币还款方式
	private String foreign_repayment;
	// 特殊还款方式
	private String special_repayment;
	// 卡片特性
	private String card_features;
	// 增值服务
	private String add_service;
	// 联名优惠
	private String joint_discount;
	// 主卡年费
	private String main_card_fee;
	// 附属卡年费
	private String supplement_card_fee;
	// 年费减免规则
	private String year_cut_rules;
	// 年费收取时间
	private String fee_date;
	// 本地同行取现手续费
	private String local_fee;
	// 本地跨行取现手续费
	private String local_interbank_fee;
	// 异地同行取现手续费
	private String offsite_fee;
	// 异地跨行取现手续费
	private String offsite_interbank_fee;
	// 境外银联取现手续费
	private String overseas_pay_fee;
	// 境外非银联取现手续费
	private String overseas_unpay_fee;
	// 境外应急取现手续费
	private String overseas_meet_fee;
	// 单日取现限额
	private String enchashment_limit;
	// 本地同行溢缴款领回手续费
	private String localback_fee;
	// 本地跨行溢缴款领回手续费
	private String local_interbank_back_fee;
	// 异地同行溢缴款领回手续费
	private String offsite_overflow_back_fee;
	// 异地跨行溢缴款领回手续费
	private String offsite_interbank_back_fee;
	// 境外银联溢缴款领回手续费
	private String overseas_pay_back_fee;
	// 境外非银联溢缴款领回手续费
	private String overseas_unpay_back_fee;
	// 溢缴款领回方式的规定
	private String overflow_back_rules;
	// 短信通知费（元）
	private String message_money;
	// 境外应急补卡手续费
	private String overseas_fee;
	// 补发卡/损卡换卡
	private String change_card;
	// 提前换卡手续费
	private String ahead_change_card;
	// 快递费
	private String express_fee;
	// 补制对账单手续费
	private String statement_fee;
	// 补寄对账单手续费免费条款
	private String statement_free_clause;
	// 挂失费
	private String loss_fee;
	// 重置密码函手续费
	private String reset_password_fee;
	// 个性卡制作费
	private String selfdom_card_fee;
	// 外汇兑换手续费
	private String foreign_convert_fee;
	// 调阅国内签购单正本手续费
	private String slip_fee;
	// 调阅国内签购单副本手续费
	private String slip_fee_copy;
	// 调阅国外签购单正本手续费
	private String slip_fee_foreign;
	// 调阅国外签购单副本手续费
	private String slip_fee_copy_foreign;
	// 滞纳金
	private String overdue_fine;
	// 超限费
	private String transfinite_fee;
	// 类型 1.旅游卡；2.购物卡；3.女性卡；4.汽车卡
	private int type_value;
	// 图片路径
	private String litpic;
	
	public int getCard_id() {
		return card_id;
	}
	public void setCard_id(int card_id) {
		this.card_id = card_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIssuing_bank() {
		return issuing_bank;
	}
	public void setIssuing_bank(String issuing_bank) {
		this.issuing_bank = issuing_bank;
	}
	public String getIssuing_nstitution() {
		return issuing_nstitution;
	}
	public void setIssuing_nstitution(String issuing_nstitution) {
		this.issuing_nstitution = issuing_nstitution;
	}
	public String getIssuing_status() {
		return issuing_status;
	}
	public void setIssuing_status(String issuing_status) {
		this.issuing_status = issuing_status;
	}
	public String getCheck_style() {
		return check_style;
	}
	public void setCheck_style(String check_style) {
		this.check_style = check_style;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getBorrowing_limit() {
		return borrowing_limit;
	}
	public void setBorrowing_limit(String borrowing_limit) {
		this.borrowing_limit = borrowing_limit;
	}
	public String getInterest_free() {
		return interest_free;
	}
	public void setInterest_free(String interest_free) {
		this.interest_free = interest_free;
	}
	public String getIntegral_policy() {
		return integral_policy;
	}
	public void setIntegral_policy(String integral_policy) {
		this.integral_policy = integral_policy;
	}
	public String getIntegral_indate() {
		return integral_indate;
	}
	public void setIntegral_indate(String integral_indate) {
		this.integral_indate = integral_indate;
	}
	public String getCredit_rules() {
		return credit_rules;
	}
	public void setCredit_rules(String credit_rules) {
		this.credit_rules = credit_rules;
	}
	public String getScoring_rules() {
		return scoring_rules;
	}
	public void setScoring_rules(String scoring_rules) {
		this.scoring_rules = scoring_rules;
	}
	public String getInstallment() {
		return installment;
	}
	public void setInstallment(String installment) {
		this.installment = installment;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getFee_payment() {
		return fee_payment;
	}
	public void setFee_payment(String fee_payment) {
		this.fee_payment = fee_payment;
	}
	public String getApplicable_fee() {
		return applicable_fee;
	}
	public void setApplicable_fee(String applicable_fee) {
		this.applicable_fee = applicable_fee;
	}
	public String getPrepayment() {
		return prepayment;
	}
	public void setPrepayment(String prepayment) {
		this.prepayment = prepayment;
	}
	public String getOpen_card() {
		return open_card;
	}
	public void setOpen_card(String open_card) {
		this.open_card = open_card;
	}
	public String getRepea_card() {
		return repea_card;
	}
	public void setRepea_card(String repea_card) {
		this.repea_card = repea_card;
	}
	public String getApp_condition() {
		return app_condition;
	}
	public void setApp_condition(String app_condition) {
		this.app_condition = app_condition;
	}
	public String getApp_way() {
		return app_way;
	}
	public void setApp_way(String app_way) {
		this.app_way = app_way;
	}
	public String getSubmit_info() {
		return submit_info;
	}
	public void setSubmit_info(String submit_info) {
		this.submit_info = submit_info;
	}
	public String getSupplement_num() {
		return supplement_num;
	}
	public void setSupplement_num(String supplement_num) {
		this.supplement_num = supplement_num;
	}
	public String getSupplement_app() {
		return supplement_app;
	}
	public void setSupplement_app(String supplement_app) {
		this.supplement_app = supplement_app;
	}
	public String getReport_loss() {
		return report_loss;
	}
	public void setReport_loss(String report_loss) {
		this.report_loss = report_loss;
	}
	public String getLoss_protection() {
		return loss_protection;
	}
	public void setLoss_protection(String loss_protection) {
		this.loss_protection = loss_protection;
	}
	public String getLoss_tel() {
		return loss_tel;
	}
	public void setLoss_tel(String loss_tel) {
		this.loss_tel = loss_tel;
	}
	public String getLowest_refund() {
		return lowest_refund;
	}
	public void setLowest_refund(String lowest_refund) {
		this.lowest_refund = lowest_refund;
	}
	public String getAllopatry_back_fee() {
		return allopatry_back_fee;
	}
	public void setAllopatry_back_fee(String allopatry_back_fee) {
		this.allopatry_back_fee = allopatry_back_fee;
	}
	public String getRmb_repayment() {
		return rmb_repayment;
	}
	public void setRmb_repayment(String rmb_repayment) {
		this.rmb_repayment = rmb_repayment;
	}
	public String getForeign_repayment() {
		return foreign_repayment;
	}
	public void setForeign_repayment(String foreign_repayment) {
		this.foreign_repayment = foreign_repayment;
	}
	public String getSpecial_repayment() {
		return special_repayment;
	}
	public void setSpecial_repayment(String special_repayment) {
		this.special_repayment = special_repayment;
	}
	public String getCard_features() {
		return card_features;
	}
	public void setCard_features(String card_features) {
		this.card_features = card_features;
	}
	public String getAdd_service() {
		return add_service;
	}
	public void setAdd_service(String add_service) {
		this.add_service = add_service;
	}
	public String getJoint_discount() {
		return joint_discount;
	}
	public void setJoint_discount(String joint_discount) {
		this.joint_discount = joint_discount;
	}
	public String getMain_card_fee() {
		return main_card_fee;
	}
	public void setMain_card_fee(String main_card_fee) {
		this.main_card_fee = main_card_fee;
	}
	public String getSupplement_card_fee() {
		return supplement_card_fee;
	}
	public void setSupplement_card_fee(String supplement_card_fee) {
		this.supplement_card_fee = supplement_card_fee;
	}
	public String getYear_cut_rules() {
		return year_cut_rules;
	}
	public void setYear_cut_rules(String year_cut_rules) {
		this.year_cut_rules = year_cut_rules;
	}
	public String getFee_date() {
		return fee_date;
	}
	public void setFee_date(String fee_date) {
		this.fee_date = fee_date;
	}
	public String getLocal_fee() {
		return local_fee;
	}
	public void setLocal_fee(String local_fee) {
		this.local_fee = local_fee;
	}
	public String getLocal_interbank_fee() {
		return local_interbank_fee;
	}
	public void setLocal_interbank_fee(String local_interbank_fee) {
		this.local_interbank_fee = local_interbank_fee;
	}
	public String getOffsite_fee() {
		return offsite_fee;
	}
	public void setOffsite_fee(String offsite_fee) {
		this.offsite_fee = offsite_fee;
	}
	public String getOffsite_interbank_fee() {
		return offsite_interbank_fee;
	}
	public void setOffsite_interbank_fee(String offsite_interbank_fee) {
		this.offsite_interbank_fee = offsite_interbank_fee;
	}
	public String getOverseas_pay_fee() {
		return overseas_pay_fee;
	}
	public void setOverseas_pay_fee(String overseas_pay_fee) {
		this.overseas_pay_fee = overseas_pay_fee;
	}
	public String getOverseas_unpay_fee() {
		return overseas_unpay_fee;
	}
	public void setOverseas_unpay_fee(String overseas_unpay_fee) {
		this.overseas_unpay_fee = overseas_unpay_fee;
	}
	public String getOverseas_meet_fee() {
		return overseas_meet_fee;
	}
	public void setOverseas_meet_fee(String overseas_meet_fee) {
		this.overseas_meet_fee = overseas_meet_fee;
	}
	public String getEnchashment_limit() {
		return enchashment_limit;
	}
	public void setEnchashment_limit(String enchashment_limit) {
		this.enchashment_limit = enchashment_limit;
	}
	public String getLocalback_fee() {
		return localback_fee;
	}
	public void setLocalback_fee(String localback_fee) {
		this.localback_fee = localback_fee;
	}
	public String getLocal_interbank_back_fee() {
		return local_interbank_back_fee;
	}
	public void setLocal_interbank_back_fee(String local_interbank_back_fee) {
		this.local_interbank_back_fee = local_interbank_back_fee;
	}
	public String getOffsite_overflow_back_fee() {
		return offsite_overflow_back_fee;
	}
	public void setOffsite_overflow_back_fee(String offsite_overflow_back_fee) {
		this.offsite_overflow_back_fee = offsite_overflow_back_fee;
	}
	public String getOffsite_interbank_back_fee() {
		return offsite_interbank_back_fee;
	}
	public void setOffsite_interbank_back_fee(String offsite_interbank_back_fee) {
		this.offsite_interbank_back_fee = offsite_interbank_back_fee;
	}
	public String getOverseas_pay_back_fee() {
		return overseas_pay_back_fee;
	}
	public void setOverseas_pay_back_fee(String overseas_pay_back_fee) {
		this.overseas_pay_back_fee = overseas_pay_back_fee;
	}
	public String getOverseas_unpay_back_fee() {
		return overseas_unpay_back_fee;
	}
	public void setOverseas_unpay_back_fee(String overseas_unpay_back_fee) {
		this.overseas_unpay_back_fee = overseas_unpay_back_fee;
	}
	public String getOverflow_back_rules() {
		return overflow_back_rules;
	}
	public void setOverflow_back_rules(String overflow_back_rules) {
		this.overflow_back_rules = overflow_back_rules;
	}
	public String getMessage_money() {
		return message_money;
	}
	public void setMessage_money(String message_money) {
		this.message_money = message_money;
	}
	public String getOverseas_fee() {
		return overseas_fee;
	}
	public void setOverseas_fee(String overseas_fee) {
		this.overseas_fee = overseas_fee;
	}
	public String getChange_card() {
		return change_card;
	}
	public void setChange_card(String change_card) {
		this.change_card = change_card;
	}
	public String getAhead_change_card() {
		return ahead_change_card;
	}
	public void setAhead_change_card(String ahead_change_card) {
		this.ahead_change_card = ahead_change_card;
	}
	public String getExpress_fee() {
		return express_fee;
	}
	public void setExpress_fee(String express_fee) {
		this.express_fee = express_fee;
	}
	public String getStatement_fee() {
		return statement_fee;
	}
	public void setStatement_fee(String statement_fee) {
		this.statement_fee = statement_fee;
	}
	public String getStatement_free_clause() {
		return statement_free_clause;
	}
	public void setStatement_free_clause(String statement_free_clause) {
		this.statement_free_clause = statement_free_clause;
	}
	public String getLoss_fee() {
		return loss_fee;
	}
	public void setLoss_fee(String loss_fee) {
		this.loss_fee = loss_fee;
	}
	public String getReset_password_fee() {
		return reset_password_fee;
	}
	public void setReset_password_fee(String reset_password_fee) {
		this.reset_password_fee = reset_password_fee;
	}
	public String getSelfdom_card_fee() {
		return selfdom_card_fee;
	}
	public void setSelfdom_card_fee(String selfdom_card_fee) {
		this.selfdom_card_fee = selfdom_card_fee;
	}
	public String getForeign_convert_fee() {
		return foreign_convert_fee;
	}
	public void setForeign_convert_fee(String foreign_convert_fee) {
		this.foreign_convert_fee = foreign_convert_fee;
	}
	public String getSlip_fee() {
		return slip_fee;
	}
	public void setSlip_fee(String slip_fee) {
		this.slip_fee = slip_fee;
	}
	public String getSlip_fee_copy() {
		return slip_fee_copy;
	}
	public void setSlip_fee_copy(String slip_fee_copy) {
		this.slip_fee_copy = slip_fee_copy;
	}
	public String getSlip_fee_foreign() {
		return slip_fee_foreign;
	}
	public void setSlip_fee_foreign(String slip_fee_foreign) {
		this.slip_fee_foreign = slip_fee_foreign;
	}
	public String getSlip_fee_copy_foreign() {
		return slip_fee_copy_foreign;
	}
	public void setSlip_fee_copy_foreign(String slip_fee_copy_foreign) {
		this.slip_fee_copy_foreign = slip_fee_copy_foreign;
	}
	public String getOverdue_fine() {
		return overdue_fine;
	}
	public void setOverdue_fine(String overdue_fine) {
		this.overdue_fine = overdue_fine;
	}
	public String getTransfinite_fee() {
		return transfinite_fee;
	}
	public void setTransfinite_fee(String transfinite_fee) {
		this.transfinite_fee = transfinite_fee;
	}
	public int getType_value() {
		return type_value;
	}
	public void setType_value(int type_value) {
		this.type_value = type_value;
	}
	public String getLitpic() {
		return litpic;
	}
	public void setLitpic(String litpic) {
		this.litpic = litpic;
	}
	
}
