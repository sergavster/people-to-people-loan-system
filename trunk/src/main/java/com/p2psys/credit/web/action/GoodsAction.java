package com.p2psys.credit.web.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.p2psys.common.enums.EnumConvertStatus;
import com.p2psys.common.enums.EnumRuleNid;
import com.p2psys.common.enums.EnumRuleStatus;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.credit.domain.Goods;
import com.p2psys.credit.domain.GoodsCategory;
import com.p2psys.credit.domain.GoodsPic;
import com.p2psys.credit.model.CreditGoodsModel;
import com.p2psys.credit.model.CreditModel;
import com.p2psys.credit.model.GoodsCategoryModel;
import com.p2psys.credit.service.CreditConvertService;
import com.p2psys.domain.CreditConvert;
import com.p2psys.domain.User;
import com.p2psys.domain.UserCredit;
import com.p2psys.model.CreditConvertModel;
import com.p2psys.model.DetailUser;
import com.p2psys.model.PageDataList;
import com.p2psys.model.RuleModel;
import com.p2psys.model.SearchParam;
import com.p2psys.model.UserCacheModel;
import com.p2psys.service.UserCreditService;
import com.p2psys.service.UserService;
import com.p2psys.util.DateUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

/**
 * 商品兑换
 
 *  2013-12-19
 *  v1.6.7.2 RDPROJECT-569
 */

public class GoodsAction extends BaseAction {
	
	private final static Logger logger = Logger.getLogger(GoodsAction.class);
	
	@Resource
	private CreditConvertService creditConvertService; 
	
	@Resource
	private UserCreditService userCreditService;
	
	private UserService userService;
	public UserService getUserService() {
		return userService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	/**
	 * 积分商城
	 * @return
	 */
	public String shopIndex(){
		User user = (User) session.get(Constant.SESSION_USER);
		if (user == null) {
			return "login";
		}
		int page =paramInt("page");
		int pageNum=paramInt("pageNum");
		this.getLeftShowByUser(user);
		//用户有效积分
		CreditModel creditModel=this.creditConvertService.getCreditByUserId(user.getUser_id());
		request.setAttribute("creditModel", creditModel);
		
		SearchParam param=new SearchParam();
		String type=paramString("credit_type");
		param.setCredit_type(type);
		//积分查询
		if(("0").equals(type)){ //全部
			param.setCredit_value("");
		}
		//积分范围筛选
		String startCredit=paramString("startCredit");
		String endCredit=paramString("endCredit");
		if(!StringUtils.isBlank(startCredit)&&(!StringUtils.isBlank(endCredit))){ 
			param.setCredit_value(startCredit+","+endCredit);
		}else if(!StringUtils.isBlank(startCredit)&&(StringUtils.isBlank(endCredit))){
			param.setCredit_value(startCredit);
		}else if(StringUtils.isBlank(startCredit)&&(!StringUtils.isBlank(endCredit))){
			param.setCredit_value("0,"+endCredit);
		}
		request.setAttribute("startCredit", startCredit);
		request.setAttribute("endCredit", endCredit);
		//商品分类查询
		String category_Id=paramString("category_id");
		if(!StringUtils.isEmpty(category_Id)){
			param.setCategory_id(category_Id);
		}
		if(pageNum==0){  //默认显示12条数据
			pageNum=12;
		}
		PageDataList pList=this.creditConvertService.showGoodsList(page,param,pageNum);
		this.setPageAttribute(pList, param);
		return "success";
	}
	
	
	/**
	 * 兑换详情页面展示
	 * @return
	 */
	public String showGoods(){
		
		User user = (User) session.get(Constant.SESSION_USER);
		if (user == null) {
			return "login";
		}
		this.getLeftShowByUser(user);
		String goodsId=paramString("goodsId");
		if(!StringUtils.isBlank(goodsId)){
			CreditGoodsModel model=this.creditConvertService.getGoodsById(Integer.parseInt(goodsId));
			if(model!=null && model.getCategory_id()!=null){
				GoodsCategory cate=this.creditConvertService.getGoodCategoryById(model.getCategory_id());
				if(cate!=null){
					model.setCategoryName(cate.getName());
					model.setAddtime(DateUtils.dateStr2(DateUtils.getDate(model.getShelves_time()+"")));
					model.setIs_virtual(cate.getIs_virtual());
				}
			}
			//根据商品ID 查询所有图片
			List<GoodsPic> gpList=this.creditConvertService.getGoodsPicByGoodsId(Integer.parseInt(model.getId()+""));
			request.setAttribute("gpList", gpList);
			request.setAttribute("goodsModel", model);
			return "success";
		}
		return null;
	}
	
	/**
	 * 积分兑换
	 * @return
	 */
	public synchronized String convertGoods(){
		String goodsId=paramString("goodsId");
		String goodsNum=paramString("goodsNum");
		String address=paramString("address");
		if(!StringUtils.isBlank(goodsId)){
			CreditGoodsModel model=this.creditConvertService.getGoodsById(Integer.parseInt(goodsId));
			User user = (User) session.get(Constant.SESSION_USER);
			if (user == null) {
				return "login";
			}
			//提交
			if(model!=null){
				CreditConvertModel creditConvert=new CreditConvertModel();
				//判断该商品是否是vip is_virtual=1
				String isVirtual=this.creditConvertService.getIsVirtualByGoodsId(Integer.parseInt(model.getId()+""));
				if(("1").equals(isVirtual)){
					creditConvert.setType(EnumRuleNid.INTEGRAL_VIP.getValue());
					// 提取积分兑换规则信息
					RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.INTEGRAL_VIP.getValue()));
					boolean result = true; // 是否可以申请
					if(rule == null || rule.getStatus() != EnumRuleStatus.RULE_STATUS_YES.getValue()) result = false;
					int time_check = rule.getValueIntByKey("time_check");// 兑换vip间隔时间是否启用
					int convert_time = rule.getValueIntByKey("convert_time");// 兑换vip间隔多长时间（月）
					// 判断用户申请的间隔时间，是否可以再次申请兑换
					if(time_check > 0){
						List<CreditConvertModel> convertList = creditConvertService.getCreditConvert(user.getUser_id(), EnumRuleNid.INTEGRAL_VIP.getValue());
						for(CreditConvertModel cmodcel : convertList){
							if(cmodcel != null && cmodcel.getStatus() == EnumConvertStatus.WAIT_AUDIT.getValue() ){
								result = false;
								break;
							}else if(cmodcel != null && cmodcel.getStatus() == EnumConvertStatus.PASS_AUDIT.getValue()){
								String vipStartTime = DateUtils.getTimeStr(DateUtils.rollMon(DateUtils.getDate(DateUtils.getNowTimeStr()),-convert_time));
								long startTime = NumberUtils.getLong(vipStartTime);
								long verifyTime = NumberUtils.getLong(cmodcel.getVerify_time());
								if(verifyTime >= startTime) {
									result = false;
									break;
								}
							}
						}
					}
					UserCacheModel userCache = userService.getUserCacheByUserid(user.getUser_id());
					// 如果vip正在等待审核中，则不能申请,或者赠送vip正在审核中，则不能申请兑换vip
					if(userCache.getVip_status() == 2 || userCache.getVip_give_status() == 2 ){
						result = false;
					}
					if(!result){
						message("您当前不能兑换vip！","/credit/shopIndex.html");
						return "success";
					}
				}else{
					creditConvert.setType("expense_change");
				}
				creditConvert.setUser_id(user.getUser_id());
				creditConvert.setCredit_value(model.getCredit_value()*(Long.parseLong(goodsNum)));
				creditConvert.setConvert_money((model.getCredit_value()*(Long.parseLong(goodsNum)))/100);
				creditConvert.setMoney(model.getCredit_value()*(Long.parseLong(goodsNum))/100);
				creditConvert.setStatus(EnumConvertStatus.WAIT_AUDIT.getValue());//未审核
				creditConvert.setAddtime(DateUtils.getNowTimeStr());
				creditConvert.setConvert_num(Integer.parseInt(goodsNum));
				creditConvert.setGoods_id(Integer.parseInt(model.getId()+""));
				creditConvert.setRemark(address);
				//兑换提交对应dw_goods 数量减少
				int i=this.creditConvertService.updateGoodsStore(creditConvert);
				if(i>0){
					this.creditConvertService.convertCreditValue(creditConvert);
					//扣除用户积分
					creditConvertService.updateCreditAndGoods(creditConvert); //扣除用户积分
					message("兑换成功请等待管理人员审核！","/credit/shopIndex.html");
				}else{
					message("已被人兑换！","/credit/shopIndex.html");
				}
			}
		}
		return "success";
	}
	
	
	/**
	 * 积分明细
	 * @return
	 */
	public String creditDetail(){
		User user = (User) session.get(Constant.SESSION_USER);
		if (user == null) {
			return "login";
		}
		this.getLeftShowByUser(user);
		int page =paramInt("page");
		SearchParam param=new SearchParam();
		PageDataList pageDataList=this.creditConvertService.getCreditLogList(page, param,user.getUser_id());
		this.setPageAttribute(pageDataList, param);
		return "success";
	}
	
	
	/**
	 * 积分消费状况
	 * @return
	 */
	public String usedCreditDetail(){
		User user = (User) session.get(Constant.SESSION_USER);
		this.getLeftShowByUser(user);
		String user_id=paramString("user_id");
		try{
			if(!StringUtils.isBlank(user_id)){
				user_id=user_id.replace(",", "");
				int page = paramInt("page");
				SearchParam param=new SearchParam();
				param.setUser_id(Long.parseLong(user_id));
				PageDataList dataList=this.creditConvertService.getCreditConvertList(page, param, Long.parseLong(user_id));
				List<CreditConvert> list=dataList.getList();
				if(list!=null && list.size()>0){ //算得单个商品积分
					for (CreditConvert creditConvert : list) {
						creditConvert.setGoods_credit_value((creditConvert.getCredit_value()/creditConvert.getConvert_num())+"");
					}
					dataList.setList(list);
				}
				this.setPageAttribute(dataList, param);
				return "success";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "login";
	}
	
	/**
	 * 用户取消兑换
	 * @return
	 */
	public String canceltCreditConvert(){
		String convertId=paramString("id");
		if(!StringUtils.isBlank(convertId)){
			//撤销时 时候否被审核过
			CreditConvert cc=this.creditConvertService.getCreditConvertById(Long.parseLong(convertId));
			if(cc.getStatus()==0){
				this.creditConvertService.updateCreditConvertByStatus(Long.parseLong(convertId));
				//商品数量和用户积分返回
				CreditConvert convert=this.creditConvertService.getCreditConvertById(Long.parseLong(convertId));
				this.creditConvertService.updateCreditValieByCredit(convert.getUser_id(),Integer.parseInt(convert.getCredit_value()+""));
				if(!StringUtils.isBlank(convert.getGoods_id()+"")){
					Goods goods=this.creditConvertService.getGoodsById(convert.getGoods_id());
					if(goods!=null){
						goods.setStore(goods.getStore()+convert.getConvert_num());
						this.creditConvertService.updateGoodsByStore(goods);
					}
				}
			}else{
				message("已被管理员审核过，无法自行撤销！","/credit/shopIndex.html");
				return "goback";
			}
		}
		return "success";
	}
	
	/**
	 * 根据选择商品类别 加载子类
	 * @return
	 */
	public String childCreditCatagory(){
		String categoryId=paramString("id");
		try{
			if(!StringUtils.isBlank(categoryId)){
				List<GoodsCategoryModel> list=this.creditConvertService.getGoodsCategoryListByParentId(Integer.parseInt(categoryId),null);
				List categoryList=new ArrayList();
				if(list!=null && list.size()>0){
					for (GoodsCategoryModel category : list) {
						Map<String,Object> map=new HashMap<String, Object>(); 
						map.put("category_id", category.getId());
						map.put("name", category.getName());
						categoryList.add(map);
					}
				}
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("goryList", categoryList);
				response.getWriter().print(jsonObj.toString());
	
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	private void getLeftShowByUser(User user){
		UserCredit userCredit = userCreditService.getUserCreditByUserId(user.getUser_id());
		request.setAttribute("userCredit", userCredit);
		//商品分类  父类
		List<GoodsCategoryModel> cateList=this.creditConvertService.getGoodsCategoryListByParentId(0, null);
		for (GoodsCategoryModel goodsCategoryModel : cateList) {
			List<GoodsCategoryModel> childList=this.creditConvertService.getGoodsCategoryListByParentId(goodsCategoryModel.getId(), null);
			if(childList!=null && childList.size()>0){
				goodsCategoryModel.setChildList(childList);
			}
		}
		request.setAttribute("cateList", cateList);
		
		
		CreditModel creditModel=this.creditConvertService.getCreditByUserId(user.getUser_id());
		request.setAttribute("creditModel", creditModel);
		DetailUser detailUser = userService.getDetailUser(user.getUser_id());
		request.setAttribute("detailuser", detailUser);
		request.setAttribute("user", user);
		//用户最近兑换 10个
		List<CreditConvertModel> convertList=this.creditConvertService.getCreditConvertByStatus();
		request.setAttribute("convertList", convertList);
	}
	
}
