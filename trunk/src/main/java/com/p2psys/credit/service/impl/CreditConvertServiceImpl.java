package com.p2psys.credit.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.p2psys.common.enums.EnumConvertStatus;
import com.p2psys.common.enums.EnumIntegralTypeName;
import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.credit.dao.CreditConvertDao;
import com.p2psys.credit.domain.CreditLog;
import com.p2psys.credit.domain.Goods;
import com.p2psys.credit.domain.GoodsCategory;
import com.p2psys.credit.domain.GoodsPic;
import com.p2psys.credit.model.CreditGoodsModel;
import com.p2psys.credit.model.CreditModel;
import com.p2psys.credit.model.GoodsCategoryModel;
import com.p2psys.credit.service.CreditConvertService;
import com.p2psys.dao.AccountDao;
import com.p2psys.dao.UserCacheDao;
import com.p2psys.dao.UserCreditDao;
import com.p2psys.domain.Account;
import com.p2psys.domain.CreditConvert;
import com.p2psys.domain.CreditType;
import com.p2psys.domain.User;
import com.p2psys.domain.UserCredit;
import com.p2psys.model.CreditConvertModel;
import com.p2psys.model.PageDataList;
import com.p2psys.model.RuleModel;
import com.p2psys.model.SearchParam;
import com.p2psys.model.UserCacheModel;
import com.p2psys.model.accountlog.BaseAccountLog;
import com.p2psys.model.accountlog.award.AwardIntegralConvertLog;
import com.p2psys.model.creditlog.BaseCreditLog;
import com.p2psys.model.creditlog.convert.CreditConvertFailLog;
import com.p2psys.model.creditlog.convert.CreditConvertSuccessLog;
import com.p2psys.model.creditlog.convert.CreditConvertVipFailLog;
import com.p2psys.model.creditlog.convert.CreditConvertVipSuccessLog;
import com.p2psys.tool.Page;
import com.p2psys.util.DateUtils;


@Service
public class CreditConvertServiceImpl implements CreditConvertService {

	private CreditConvertDao creditConvertDao; 
	
	private UserCreditDao userCreditDao;
	
	private AccountDao accountDao;
	
	private UserCacheDao userCacheDao;
	
	// v1.6.7.2 RDPROJECT-560 zza 2013-12-13 start
	@Override
	public PageDataList getCreditConvertPage(int page, SearchParam p) {
		PageDataList pageDataList = new PageDataList();
		Page pages = new Page(creditConvertDao.getCreditConvertCount(p) , page );
		pageDataList.setList(creditConvertDao.getCreditConvertPage(pages.getStart(), pages.getPernum() , p));
		pageDataList.setPage(pages);
		return pageDataList;
	}
	// v1.6.7.2 RDPROJECT-560 zza 2013-12-13 end

	@Override
	public void insertCreditConvert(CreditConvert creditConvert) {
		creditConvertDao.insertCreditConvert(creditConvert);
	}

	public CreditConvertModel getCreditConvertById(Long id){
		return creditConvertDao.getCreditConvertById(id);
	}
	
	/**
	 * 积分兑换审核
	 * @param id
	 * @param money
	 * @return
	 */
	public boolean auditCreditConvert(CreditConvert creditConvert){
		
		// 如果积分兑换信息为空或者会员ID小于等于零，则return
		if(creditConvert == null || creditConvert.getUser_id() <= 0 || creditConvert.getMoney() <= 0) return false;
		
		CreditType creditType = userCreditDao.getCreditTypeByNid(EnumIntegralTypeName.INTEGRAL_CONVERT.getValue());
		// 如果积分类型为空，或者积分类型的积分种类为空，则return
		if( creditType == null || creditType.getCredit_category() <= 0) return false;
		
		long credit_value = creditConvert.getCredit_value();
		double money = creditConvert.getMoney();
		long user_id = creditConvert.getUser_id();
		
		CreditConvertModel convertModel = creditConvertDao.getCreditConvertById(creditConvert.getId());
		// 如果兑换信息不等于未审核，则return
		if(convertModel.getStatus() != EnumConvertStatus.WAIT_AUDIT.getValue()) return false;
		
		Global.setTransfer("credit_value", credit_value);
		Global.setTransfer("money", money);
		Global.setTransfer("award", money);
		creditConvert.setVerify_time(DateUtils.getNowTimeStr());
		// 审核通过
		if(creditConvert.getStatus() == EnumConvertStatus.PASS_AUDIT.getValue() ){
			creditConvertDao.auditCreditConvert(creditConvert);
			// v1.6.7.2 RDPROJECT-503 zza 2013-12-13 start
			UserCredit credit = userCreditDao.getUserCreditByUserId(user_id);
			//会员积分日志记录信息
			BaseCreditLog bLog = new CreditConvertSuccessLog(new Byte(
					Constant.OP_NONE), user_id, creditType.getId(), user_id,
					credit_value, credit.getValid_value());
			// v1.6.7.2 RDPROJECT-503 zza 2013-12-13 end
			bLog.doEvent();
			//资金日志处理
			accountDao.updateAccount(money, money,0, user_id); 
			Account act = accountDao.getAccount(user_id);
			BaseAccountLog blog = new AwardIntegralConvertLog(money,act);
			blog.doEvent();
		}else{// 审核失败或非法操作，返回兑换积分：消费积分减，有效积分加
			creditConvertDao.auditCreditConvert(creditConvert);
			userCreditDao.editExpenseValue(user_id, (int)-credit_value);
			// v1.6.7.2 RDPROJECT-503 zza 2013-12-13 start
			UserCredit credit = userCreditDao.getUserCreditByUserId(user_id);
			int valid_value = credit.getValid_value();
			BaseCreditLog bLog = new CreditConvertFailLog(new Byte(
					Constant.OP_ADD), user_id, creditType.getId(), user_id,
					credit_value, valid_value);
			// v1.6.7.2 RDPROJECT-503 zza 2013-12-13 end
			bLog.doEvent();
		}
		return true;
	}
	
	/**
	 * 积分兑换VIP审核
	 * @param id
	 * @param money
	 * @return
	 */
	public boolean auditCreditConvertVip(CreditConvert creditConvert){
		
		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.INTEGRAL_VIP.getValue()));
		int vip_time = rule.getValueIntByKey("vip_time");//兑换VIP有效时间（月）
		
		int convert_num = creditConvert.getConvert_num();
		if(convert_num <= 0) return false;
		convert_num = convert_num > 0 ? convert_num : vip_time;
		
		// 如果积分兑换信息为空或者会员ID小于等于零，则return
		if(creditConvert == null || creditConvert.getUser_id() <= 0) return false;
		
		CreditType creditType = userCreditDao.getCreditTypeByNid(EnumIntegralTypeName.INTEGRAL_VIP.getValue());
		// 如果积分类型为空，或者积分类型的积分种类为空，则return
		if( creditType == null || creditType.getCredit_category() <= 0) return false;
		
		long credit_value = creditConvert.getCredit_value();
		long user_id = creditConvert.getUser_id();
		
		CreditConvertModel convertModel = creditConvertDao.getCreditConvertById(creditConvert.getId());
		// 如果兑换信息不等于未审核，则return
		if(convertModel.getStatus() != EnumConvertStatus.WAIT_AUDIT.getValue()) return false;
		
		Global.setTransfer("valid_value", credit_value);
		creditConvert.setVerify_time(DateUtils.getNowTimeStr());
		// 审核通过
		if (creditConvert.getStatus() == EnumConvertStatus.PASS_AUDIT.getValue()) {
			creditConvertDao.auditCreditConvert(creditConvert);
			UserCacheModel userCache = userCacheDao.getUserCacheByUserid(creditConvert.getUser_id());
			// 更新结束时间
			String vipEndTimeString = "";
			if (userCache.getVip_status() == 0 || userCache.getVip_status() == -1 
					|| userCache.getVip_status() == 3) { // 没有申请vip或申请vip失败或vip过期
				userCache.setVip_status(1);
				vipEndTimeString = DateUtils.getTimeStr(
			// v1.6.7.2 RDPROJECT-569 cx 2013-12-24 start			
						// convert_num 兑换个数     -转换  vip_time
						DateUtils.rollMon(DateUtils.getDate(DateUtils.getNowTimeStr()), vip_time));
			} else if (userCache.getVip_status() == 1) { // vip现在状态就是vip
				vipEndTimeString = DateUtils.getTimeStr(
						DateUtils.rollMon(DateUtils.getDate(userCache.getVip_end_time()), vip_time));
			// v1.6.7.2 RDPROJECT-569 cx 2013-12-24 end		
			} else { // 如果vip正在等待审核中，则不能申请
				return false;
			}
			userCache.setVip_end_time(vipEndTimeString);
			userCache.setVip_verify_time(DateUtils.getNowTimeStr());
			userCacheDao.updateUserCache(userCache);
			// v1.6.7.2 RDPROJECT-503 zza 2013-12-13 start
			UserCredit credit = userCreditDao.getUserCreditByUserId(user_id);
			//会员积分日志记录信息
			BaseCreditLog bLog = new CreditConvertVipSuccessLog(new Byte(
					Constant.OP_NONE), user_id, creditType.getId(), user_id,
					credit_value, credit.getValid_value());
			// v1.6.7.2 RDPROJECT-503 zza 2013-12-13 end
			bLog.doEvent();
		}else{// 审核失败或非法操作，返回兑换积分：消费积分减，有效积分加
			creditConvertDao.auditCreditConvert(creditConvert);
			userCreditDao.editExpenseValue(user_id, (int)-credit_value);
			// v1.6.7.2 RDPROJECT-503 zza 2013-12-13 start
			UserCredit credit = userCreditDao.getUserCreditByUserId(user_id);
			int valid_value = credit.getValid_value();
			BaseCreditLog bLog = new CreditConvertVipFailLog(new Byte(
					Constant.OP_ADD), user_id, creditType.getId(), user_id,
					credit_value, valid_value);
			// v1.6.7.2 RDPROJECT-503 zza 2013-12-13 end
			bLog.doEvent();
		}
		return true;
	}
	
	/**
	 * 根据主键user_id和类型查询积分兑换信息
	 * @param user_id
	 * @param type
	 * @return
	 */
	public List<CreditConvertModel> getCreditConvert(long user_id , String type){
		return creditConvertDao.getCreditConvert(user_id, type);
	}
	
	
	
	
	//v1.6.7.2 RDPROJECT-569 cx 2013-12-18 start
	
	/**
	 * 前端显示商品列表 上架时间<今天
	 */
	public int showGoodsCount(SearchParam p){
		return creditConvertDao.showGoodsCount(p);
	}
	public PageDataList showGoodsList(int page, SearchParam param,int pageNum){
		int total=this.showGoodsCount(param);
		Page p=new Page(total,page,pageNum);
		List<CreditGoodsModel> goodsList = creditConvertDao.showGoodsList(p.getStart(), p.getPernum(), param);
		PageDataList plist=new PageDataList(p,goodsList);
		return plist;
	}
	
	/**
	 * 积分兑换产品列表显示
	 * @param param
	 * @return
	 */
	public int getGoodsCount(SearchParam p,Integer compare){
		return creditConvertDao.getGoodsCount(p,compare);
	}
	public PageDataList getGoodsList(int page, SearchParam param,int pageNum,Integer compare){
		int total=this.getGoodsCount(param,compare);
		Page p=new Page(total,page,pageNum);
		List<CreditGoodsModel> goodsList = creditConvertDao.getGoodsList(p.getStart(), p.getPernum(), param,compare);
		PageDataList plist=new PageDataList(p,goodsList);
		return plist;
	}
	/**
	 * 查询parent_id<>0  
	 * 得出所有二级菜单
	 */
	public List<GoodsCategoryModel> getCategoryList(Integer parentId,String catetoryType){
		return creditConvertDao.getCategoryList(parentId,catetoryType);
	}
	/**
	 * 得出商品分类
	 */
	public long saveGoodsAndPic(CreditGoodsModel model){
		 return creditConvertDao.saveGoodsAndPic(model);
	}
	
	public void saveGoodsPic(GoodsPic goodsPic){
		creditConvertDao.saveGoodsPic(goodsPic);
	}
	
	/**
	 * 通过商品ID 得出名称
	 */
	public GoodsCategory getGoodCategoryById(Integer id){
		return creditConvertDao.getGoodCategoryById(id);
	}
	public CreditGoodsModel getGoodsById(Integer id){
		return creditConvertDao.getGoodsById(id);
	}
	public void updateGoodsAndPic(CreditGoodsModel model){
		 creditConvertDao.updateGoodsAndPic(model);
	}
	public void delGoodsAndPic(Integer id){
		creditConvertDao.delGoodsAndPic(id);
	}
	/**
	 * 用户有效积分
	 */
	public CreditModel getCreditByUserId(long userId){
		return creditConvertDao.getCreditByUserId(userId);
	}
	/**
	 * 兑换由后台后审核
	 */
	public void convertCreditValue(CreditConvert convert){
		creditConvertDao.insertCreditConvert(convert);
		//userCreditDao.editExpenseValue(convert.getUser_id(), (int)convert.getCredit_value());
	}
	
	/**
	 * 后台商品兑换审核
	 */
	public void updateCreditConvertByStatus(User user,byte status,Integer id){
		creditConvertDao.updateCreditConvertByStatus(user,status, id);
	}
	
	/**
	 * 审核通过后用户积分减少
	 */
	public synchronized void updateCreditAndGoods(CreditConvertModel convertModel){
	//	int i=creditConvertDao.updateGoodsStore(convertModel);
	//	if(i>0){
			userCreditDao.editExpenseValue(convertModel.getUser_id(),Integer.parseInt(convertModel.getCredit_value()+""));
	//		return 1;
	//	}
	//	return 0;
	}
	
	
	/**
	 * 积分明细列表
	 * @param page
	 * @param param
	 * @return
	 */
	public PageDataList getCreditLogList(int page,SearchParam param,long user_id){
		Integer count=this.creditConvertDao.getCreditLogCount(param,user_id);
		Page p = new Page(count, page);
		List<CreditGoodsModel> list = this.creditConvertDao.getCreditLogList(p.getStart(), p.getPernum(), param,user_id);
	    PageDataList plist = new PageDataList(p, list);
	    return plist;
	}
	
	/**
	 * 保持积分明细  dw_credit_log
	 */
	public void saveCreditLog(CreditLog creditLog){
		this.creditConvertDao.saveCreditLog(creditLog);
	}
	
	/**
	 * 积分消费情况
	 * @param page
	 * @param param
	 * @return
	 */
	public PageDataList getCreditConvertList(int page,SearchParam param,long user_id){
		Integer count=this.creditConvertDao.getCreditConvertCount(param,user_id);
		Page p = new Page(count, page);
		List<CreditConvert> list=this.creditConvertDao.getCreditConvertList(p.getStart(), p.getPernum(), param, user_id);
		PageDataList plist = new PageDataList(p, list);
		return plist;
	}
	
	/**
	 * 最近用户兑换的商品显示
	 * @return
	 */
	public List<CreditConvertModel> getCreditConvertByStatus(){
		return this.creditConvertDao.getCreditConvertByStatus();
	}
	
	public int updateGoodsStore(CreditConvertModel convertModel){
		return this.creditConvertDao.updateGoodsStore(convertModel);
	}
	public void addGoodsStore(CreditConvertModel convertModel){
		this.creditConvertDao.addGoodsStore(convertModel);
	}
	
	public List<GoodsPic> getGoodsPicByGoodIs(Integer goodsId){
		return this.creditConvertDao.getGoodsPicByGoodIs(goodsId);
	}
	public void updateCreditValieByCredit(long user_id,Integer credit_value){
		this.creditConvertDao.updateCreditValieByCredit(user_id, credit_value);
	}
	
	/**
	 * 用户撤回兑换
	 * @param id
	 */
	public void updateCreditConvertByStatus(long id){
		this.creditConvertDao.updateCreditConvertByStatus(id);
	}
	public void updateGoodsByStore(Goods goods){
		this.creditConvertDao.updateGoodsByStore(goods);
	}
	
	/**
	 * 根据父类ID 找到对应商品子类
	 */
	public List<GoodsCategoryModel> getGoodsCategoryListByParentId(Integer parentId,Integer id){
		return this.creditConvertDao.getGoodsCategoryListByParentId(parentId,id);
	}
	public void saveGoodsCategory(GoodsCategory category){
		this.creditConvertDao.saveGoodsCategory(category);
	}
	public void delGoodsCategory(String cateId){
		this.creditConvertDao.delGoodsCategory(cateId);
	}
	
	/**
	 * 通过dw_credit_convert 得到商品ID 然后根据商品ID得到分类
	 */
	public String getIsVirtualByConvertGoodsId(int id){
		return this.creditConvertDao.getIsVirtualByConvertGoodsId(id);
	}
	
	public String getIsVirtualByGoodsId(int id){
		return this.creditConvertDao.getIsVirtualByGoodsId(id);
	}
	
	
	/**
	 * 通过商品ID 得到所有图片
	 */
	public List<GoodsPic> getGoodsPicByGoodsId(int goodsId){
		return this.creditConvertDao.getGoodsPicByGoodIs(goodsId);
	}
	
	/**
	 * 根据商品ID修改上传图片
	 */
	public void updateGoodsPicByGoodsId(GoodsPic goodsPic){
		this.creditConvertDao.updateGoodsPicByGoodsId(goodsPic);
	}
	
	public void delAllGoodsPicByGoodsId(int goodsId){
		this.creditConvertDao.delAllGoodsPicByGoodsId(goodsId);
	}
	//v1.6.7.2 RDPROJECT-569 cx 2013-12-18 end	
	
	public void setCreditConvertDao(CreditConvertDao creditConvertDao) {
		this.creditConvertDao = creditConvertDao;
	}

	public void setUserCreditDao(UserCreditDao userCreditDao) {
		this.userCreditDao = userCreditDao;
	}

	public void setAccountDao(AccountDao accountDao) {
		this.accountDao = accountDao;
	}

	public void setUserCacheDao(UserCacheDao userCacheDao) {
		this.userCacheDao = userCacheDao;
	}
}
