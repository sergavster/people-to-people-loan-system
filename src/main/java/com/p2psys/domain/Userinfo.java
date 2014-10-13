package com.p2psys.domain;

import java.io.Serializable;

import com.p2psys.util.ReflectUtils;
import com.p2psys.util.StringUtils;
/**
 * 实体类
 
 * @version 1.0  
 * @since 2013-12-02 
 * 无线部使用中，若做较大更改（1.实体里参数类型、2.方法名或参数），请告知无线部
 */
public class Userinfo implements Serializable {

	private static final long serialVersionUID = -332938148258561708L;
	private long id;
	private long site_id;
	private long user_id;
	private String name;
	private int status;
	private int order;
	private int hits;
	private String litpic;
	private String flag;
	private String source;
	private String publish;
	private String marry;
	private String child;
	private String education;
	private String income;
	private String shebao;
	private String shebaoid;
	private String housing;
	private String car;
	private String late;
	private String house_address;
	private String house_area;
	private String house_year;
	private String house_status;
	private String house_holder1;
	private String house_holder2;
	private String house_right1;
	private String house_right2;
	private String house_loanyear;
	private String house_loanprice;
	private String house_balance;
	private String house_bank;
	private String company_name;
	private String company_type;
	private String company_industry;
	private String company_office;
	private String company_jibie;
	private String company_worktime1;
	private String company_worktime2;
	private String company_workyear;
	private String company_tel;
	private String company_address;
	private String company_weburl;
	private String company_reamrk;
	private String private_type;
	private String private_date;
	private String private_place;
	private String private_rent;
	private String private_term;
	private String private_taxid;
	private String private_commerceid;
	private String private_income;
	private String private_employee;
	private String finance_repayment;
	private String finance_property;
	private String finance_amount;
	private String finance_car;
	private String finance_caramount;
	private String finance_creditcard;
	private String mate_name;
	private String mate_salary;
	private String mate_phone;
	private String mate_tel;
	private String mate_type;
	private String mate_office;
	private String mate_address;
	private String mate_income;
	private String education_record;
	private String education_school;
	private String education_study;
	private String education_time1;
	private String education_time2;
	private String tel;
	private String phone;
	private String post;
	private String address;
	private String province;
	private String city;
	private String area;
	private String linkman1;
	private String relation1;
	private String tel1;
	private String phone1;
	private String linkman2;
	private String relation2;
	private String tel2;
	private String phone2;
	private String linkman3;
	private String relation3;
	private String tel3;
	private String phone3;
	private String msn;
	private String qq;
	private String wangwang;
	private String ability;
	private String interest;
	private String others;
	private String experience;
	private String addtime;
	private String addip;
	private String updatetime;
	private String updateip;
	
	private String building[]= new String[]{"house_address","house_area","house_year"};
	private String company[] = new String[]{"company_name","company_type","company_industry","company_office","company_jibie","company_worktime1","company_worktime2","company_workyear","company_tel","company_address","company_weburl","company_reamrk"};
	private String firm[] = new String[]{"private_type","private_date","private_place","private_rent","private_term","private_taxid","private_commerceid","private_income","private_employee"};
	private String finance[] = new String[]{"finance_repayment","finance_property","finance_amount","finance_car","finance_caramount","finance_creditcard"};
	private String contact[] = new String[]{"tel","phone","post","address","province","city","area","linkman1","relation1","tel1","phone1","qq","wangwang"};
	private String mate[] = new String[]{"mate_name","mate_salary","mate_phone","mate_tel","mate_type","mate_office","mate_address","mate_income"};
	private String edu[]  = new String[]{"education_record","education_school","education_study","education_time1","education_time2"};
	private String job[]  = new String[]{"ability","interest","others","experience"};
	private String info[][]=new String[][]{building,company,firm,contact,mate,edu,job};
	
	private boolean building_status=false;
	private boolean company_status=false;
	private boolean firm_status=false;
	private boolean finance_status=false;
	private boolean contact_status=false;
	private boolean mate_status=false;
	private boolean edu_status=false;
	private boolean job_status=false;

	private double rate;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getSite_id() {
		return site_id;
	}
	public void setSite_id(long site_id) {
		this.site_id = site_id;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public int getHits() {
		return hits;
	}
	public void setHits(int hits) {
		this.hits = hits;
	}
	public String getLitpic() {
		return litpic;
	}
	public void setLitpic(String litpic) {
		this.litpic = litpic;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getPublish() {
		return publish;
	}
	public void setPublish(String publish) {
		this.publish = publish;
	}
	public String getMarry() {
		return marry;
	}
	public void setMarry(String marry) {
		this.marry = marry;
	}
	public String getChild() {
		return child;
	}
	public void setChild(String child) {
		this.child = child;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getIncome() {
		return income;
	}
	public void setIncome(String income) {
		this.income = income;
	}
	public String getShebao() {
		return shebao;
	}
	public void setShebao(String shebao) {
		this.shebao = shebao;
	}
	public String getShebaoid() {
		return shebaoid;
	}
	public void setShebaoid(String shebaoid) {
		this.shebaoid = shebaoid;
	}
	public String getHousing() {
		return housing;
	}
	public void setHousing(String housing) {
		this.housing = housing;
	}
	public String getCar() {
		return car;
	}
	public void setCar(String car) {
		this.car = car;
	}
	public String getLate() {
		return late;
	}
	public void setLate(String late) {
		this.late = late;
	}
	public String getHouse_address() {
		return house_address;
	}
	public void setHouse_address(String house_address) {
		this.house_address = house_address;
	}
	public String getHouse_area() {
		return house_area;
	}
	public void setHouse_area(String house_area) {
		this.house_area = house_area;
	}
	public String getHouse_year() {
		return house_year;
	}
	public void setHouse_year(String house_year) {
		this.house_year = house_year;
	}
	public String getHouse_status() {
		return house_status;
	}
	public void setHouse_status(String house_status) {
		this.house_status = house_status;
	}
	public String getHouse_holder1() {
		return house_holder1;
	}
	public void setHouse_holder1(String house_holder1) {
		this.house_holder1 = house_holder1;
	}
	public String getHouse_holder2() {
		return house_holder2;
	}
	public void setHouse_holder2(String house_holder2) {
		this.house_holder2 = house_holder2;
	}
	public String getHouse_right1() {
		return house_right1;
	}
	public void setHouse_right1(String house_right1) {
		this.house_right1 = house_right1;
	}
	public String getHouse_right2() {
		return house_right2;
	}
	public void setHouse_right2(String house_right2) {
		this.house_right2 = house_right2;
	}
	public String getHouse_loanyear() {
		return house_loanyear;
	}
	public void setHouse_loanyear(String house_loanyear) {
		this.house_loanyear = house_loanyear;
	}
	public String getHouse_loanprice() {
		return house_loanprice;
	}
	public void setHouse_loanprice(String house_loanprice) {
		this.house_loanprice = house_loanprice;
	}
	public String getHouse_balance() {
		return house_balance;
	}
	public void setHouse_balance(String house_balance) {
		this.house_balance = house_balance;
	}
	public String getHouse_bank() {
		return house_bank;
	}
	public void setHouse_bank(String house_bank) {
		this.house_bank = house_bank;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getCompany_type() {
		return company_type;
	}
	public void setCompany_type(String company_type) {
		this.company_type = company_type;
	}
	public String getCompany_industry() {
		return company_industry;
	}
	public void setCompany_industry(String company_industry) {
		this.company_industry = company_industry;
	}
	public String getCompany_office() {
		return company_office;
	}
	public void setCompany_office(String company_office) {
		this.company_office = company_office;
	}
	public String getCompany_jibie() {
		return company_jibie;
	}
	public void setCompany_jibie(String company_jibie) {
		this.company_jibie = company_jibie;
	}
	public String getCompany_worktime1() {
		return company_worktime1;
	}
	public void setCompany_worktime1(String company_worktime1) {
		this.company_worktime1 = company_worktime1;
	}
	public String getCompany_worktime2() {
		return company_worktime2;
	}
	public void setCompany_worktime2(String company_worktime2) {
		this.company_worktime2 = company_worktime2;
	}
	public String getCompany_workyear() {
		return company_workyear;
	}
	public void setCompany_workyear(String company_workyear) {
		this.company_workyear = company_workyear;
	}
	public String getCompany_tel() {
		return company_tel;
	}
	public void setCompany_tel(String company_tel) {
		this.company_tel = company_tel;
	}
	public String getCompany_address() {
		return company_address;
	}
	public void setCompany_address(String company_address) {
		this.company_address = company_address;
	}
	public String getCompany_weburl() {
		return company_weburl;
	}
	public void setCompany_weburl(String company_weburl) {
		this.company_weburl = company_weburl;
	}
	public String getCompany_reamrk() {
		return company_reamrk;
	}
	public void setCompany_reamrk(String company_reamrk) {
		this.company_reamrk = company_reamrk;
	}
	public String getPrivate_type() {
		return private_type;
	}
	public void setPrivate_type(String private_type) {
		this.private_type = private_type;
	}
	public String getPrivate_date() {
		return private_date;
	}
	public void setPrivate_date(String private_date) {
		this.private_date = private_date;
	}
	public String getPrivate_place() {
		return private_place;
	}
	public void setPrivate_place(String private_place) {
		this.private_place = private_place;
	}
	public String getPrivate_rent() {
		return private_rent;
	}
	public void setPrivate_rent(String private_rent) {
		this.private_rent = private_rent;
	}
	public String getPrivate_term() {
		return private_term;
	}
	public void setPrivate_term(String private_term) {
		this.private_term = private_term;
	}
	public String getPrivate_taxid() {
		return private_taxid;
	}
	public void setPrivate_taxid(String private_taxid) {
		this.private_taxid = private_taxid;
	}
	public String getPrivate_commerceid() {
		return private_commerceid;
	}
	public void setPrivate_commerceid(String private_commerceid) {
		this.private_commerceid = private_commerceid;
	}
	public String getPrivate_income() {
		return private_income;
	}
	public void setPrivate_income(String private_income) {
		this.private_income = private_income;
	}
	public String getPrivate_employee() {
		return private_employee;
	}
	public void setPrivate_employee(String private_employee) {
		this.private_employee = private_employee;
	}
	public String getFinance_repayment() {
		return finance_repayment;
	}
	public void setFinance_repayment(String finance_repayment) {
		this.finance_repayment = finance_repayment;
	}
	public String getFinance_property() {
		return finance_property;
	}
	public void setFinance_property(String finance_property) {
		this.finance_property = finance_property;
	}
	public String getFinance_amount() {
		return finance_amount;
	}
	public void setFinance_amount(String finance_amount) {
		this.finance_amount = finance_amount;
	}
	public String getFinance_car() {
		return finance_car;
	}
	public void setFinance_car(String finance_car) {
		this.finance_car = finance_car;
	}
	public String getFinance_caramount() {
		return finance_caramount;
	}
	public void setFinance_caramount(String finance_caramount) {
		this.finance_caramount = finance_caramount;
	}
	public String getFinance_creditcard() {
		return finance_creditcard;
	}
	public void setFinance_creditcard(String finance_creditcard) {
		this.finance_creditcard = finance_creditcard;
	}
	public String getMate_name() {
		return mate_name;
	}
	public void setMate_name(String mate_name) {
		this.mate_name = mate_name;
	}
	public String getMate_salary() {
		return mate_salary;
	}
	public void setMate_salary(String mate_salary) {
		this.mate_salary = mate_salary;
	}
	public String getMate_phone() {
		return mate_phone;
	}
	public void setMate_phone(String mate_phone) {
		this.mate_phone = mate_phone;
	}
	public String getMate_tel() {
		return mate_tel;
	}
	public void setMate_tel(String mate_tel) {
		this.mate_tel = mate_tel;
	}
	public String getMate_type() {
		return mate_type;
	}
	public void setMate_type(String mate_type) {
		this.mate_type = mate_type;
	}
	public String getMate_office() {
		return mate_office;
	}
	public void setMate_office(String mate_office) {
		this.mate_office = mate_office;
	}
	public String getMate_address() {
		return mate_address;
	}
	public void setMate_address(String mate_address) {
		this.mate_address = mate_address;
	}
	public String getMate_income() {
		return mate_income;
	}
	public void setMate_income(String mate_income) {
		this.mate_income = mate_income;
	}
	public String getEducation_record() {
		return education_record;
	}
	public void setEducation_record(String education_record) {
		this.education_record = education_record;
	}
	public String getEducation_school() {
		return education_school;
	}
	public void setEducation_school(String education_school) {
		this.education_school = education_school;
	}
	public String getEducation_study() {
		return education_study;
	}
	public void setEducation_study(String education_study) {
		this.education_study = education_study;
	}
	public String getEducation_time1() {
		return education_time1;
	}
	public void setEducation_time1(String education_time1) {
		this.education_time1 = education_time1;
	}
	public String getEducation_time2() {
		return education_time2;
	}
	public void setEducation_time2(String education_time2) {
		this.education_time2 = education_time2;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getLinkman1() {
		return linkman1;
	}
	public void setLinkman1(String linkman1) {
		this.linkman1 = linkman1;
	}
	public String getRelation1() {
		return relation1;
	}
	public void setRelation1(String relation1) {
		this.relation1 = relation1;
	}
	public String getTel1() {
		return tel1;
	}
	public void setTel1(String tel1) {
		this.tel1 = tel1;
	}
	public String getPhone1() {
		return phone1;
	}
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}
	public String getLinkman2() {
		return linkman2;
	}
	public void setLinkman2(String linkman2) {
		this.linkman2 = linkman2;
	}
	public String getRelation2() {
		return relation2;
	}
	public void setRelation2(String relation2) {
		this.relation2 = relation2;
	}
	public String getTel2() {
		return tel2;
	}
	public void setTel2(String tel2) {
		this.tel2 = tel2;
	}
	public String getPhone2() {
		return phone2;
	}
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
	public String getLinkman3() {
		return linkman3;
	}
	public void setLinkman3(String linkman3) {
		this.linkman3 = linkman3;
	}
	public String getRelation3() {
		return relation3;
	}
	public void setRelation3(String relation3) {
		this.relation3 = relation3;
	}
	public String getTel3() {
		return tel3;
	}
	public void setTel3(String tel3) {
		this.tel3 = tel3;
	}
	public String getPhone3() {
		return phone3;
	}
	public void setPhone3(String phone3) {
		this.phone3 = phone3;
	}
	public String getMsn() {
		return msn;
	}
	public void setMsn(String msn) {
		this.msn = msn;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getWangwang() {
		return wangwang;
	}
	public void setWangwang(String wangwang) {
		this.wangwang = wangwang;
	}
	public String getAbility() {
		return ability;
	}
	public void setAbility(String ability) {
		this.ability = ability;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public String getOthers() {
		return others;
	}
	public void setOthers(String others) {
		this.others = others;
	}
	public String getExperience() {
		return experience;
	}
	public void setExperience(String experience) {
		this.experience = experience;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public String getAddip() {
		return addip;
	}
	public void setAddip(String addip) {
		this.addip = addip;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public String getUpdateip() {
		return updateip;
	}
	public void setUpdateip(String updateip) {
		this.updateip = updateip;
	}
	public String[] getBuilding() {
		return building;
	}
	public void setBuilding(String[] building) {
		this.building = building;
	}
	public String[] getCompany() {
		return company;
	}
	public void setCompany(String[] company) {
		this.company = company;
	}
	public String[] getFirm() {
		return firm;
	}
	public void setFirm(String[] firm) {
		this.firm = firm;
	}
	public String[] getFinance() {
		return finance;
	}
	public void setFinance(String[] finance) {
		this.finance = finance;
	}
	public String[] getContact() {
		return contact;
	}
	public void setContact(String[] contact) {
		this.contact = contact;
	}
	public String[] getMate() {
		return mate;
	}
	public void setMate(String[] mate) {
		this.mate = mate;
	}
	public String[] getEdu() {
		return edu;
	}
	public void setEdu(String[] edu) {
		this.edu = edu;
	}
	public String[] getJob() {
		return job;
	}
	public void setJob(String[] job) {
		this.job = job;
	}
	public boolean isBuilding_status() {
		return building_status;
	}
	public void setBuilding_status(boolean building_status) {
		this.building_status = building_status;
	}
	public boolean isFirm_status() {
		return firm_status;
	}
	public void setFirm_status(boolean firm_status) {
		this.firm_status = firm_status;
	}
	public boolean isFinance_status() {
		return finance_status;
	}
	public void setFinance_status(boolean finance_status) {
		this.finance_status = finance_status;
	}
	public boolean isContact_status() {
		return contact_status;
	}
	public void setContact_status(boolean contact_status) {
		this.contact_status = contact_status;
	}
	public boolean isMate_status() {
		return mate_status;
	}
	public void setMate_status(boolean mate_status) {
		this.mate_status = mate_status;
	}
	public boolean isEdu_status() {
		return edu_status;
	}
	public void setEdu_status(boolean edu_status) {
		this.edu_status = edu_status;
	}
	public boolean isJob_status() {
		return job_status;
	}
	public void setJob_status(boolean job_status) {
		this.job_status = job_status;
	}
	public boolean isCompany_status() {
		return company_status;
	}
	public void setCompany_status(boolean company_status) {
		this.company_status = company_status;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public void getAllStatus(){
		building_status=getstatus(building);
		company_status=getstatus(company);
		firm_status=getstatus(firm);
		finance_status=getstatus(finance);
		contact_status=getstatus(contact);
		mate_status=getstatus(mate);
		edu_status=getstatus(edu);
		job_status=getstatus(job);
		calculateRate();
	}
	
	private boolean getstatus(String[] args){
		boolean status = true;
		for(int i=0;i<args.length;i++){
			Object ret=ReflectUtils.invokeGetMethod(this.getClass(), this, args[i]);
			if (StringUtils.isBlank(StringUtils.isNull(ret))){
				status=false;
				break;
			}
		}
		return status;
	}
	
	private double calculateRate(){
		int count=0,total=0;
		for(int i=0;i<info.length;i++){
			total+=info[i].length;
			for(int j=0;j<info[i].length;j++){
				Object ret=ReflectUtils.invokeGetMethod(this.getClass(), this, info[i][j]);
				if (ret!= null){
					count++;
				}
			}
		}
		if(count==0||total==0){
			rate=0;
		}
		rate=(count+0.0)/total;
		return rate;
	}
	
}
