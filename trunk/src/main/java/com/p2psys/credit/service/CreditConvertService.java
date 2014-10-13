package com.p2psys.credit.service;

import java.util.List;

import com.p2psys.credit.domain.CreditLog;
import com.p2psys.credit.domain.Goods;
import com.p2psys.credit.domain.GoodsCategory;
import com.p2psys.credit.domain.GoodsPic;
import com.p2psys.credit.model.CreditGoodsModel;
import com.p2psys.credit.model.CreditModel;
import com.p2psys.credit.model.GoodsCategoryModel;
import com.p2psys.domain.CreditConvert;
import com.p2psys.domain.User;
import com.p2psys.model.CreditConvertModel;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;

/**
 * 积分兑换Service接口
 
 * @version 1.0
 * @since 2013-10-15
 */
public interface CreditConvertService {

	// v1.6.7.2 RDPROJECT-560 zza 2013-12-13 start
	/**
	 * 积分兑换分页接口
	 * @param page分页开始页
	 * @param p查询参数
	 * @param status 查询信息状态
	 * @param user_id 会员ID
	 * @return
	 */
	public PageDataList getCreditConvertPage(int page,SearchParam p);
	// v1.6.7.2 RDPROJECT-560 zza 2013-12-13 end
	
	
	/**
	 * 新增积分兑换信息
	 * @param creditConvert
	 * @return
	 */
	public void insertCreditConvert(CreditConvert creditConvert);
	
	/**
	 * 根据主键ID查询积分兑换信息
	 * @param id
	 * @return
	 */
	public CreditConvertModel getCreditConvertById(Long id);
	
	
	/**
	 * 积分兑换审核
	 * @param creditConvert
	 * @return
	 */
	public boolean auditCreditConvert(CreditConvert creditConvert);
	
	/**
	 * 积分兑换VIP审核
	 * @param creditConvert
	 * @return
	 */
	public boolean auditCreditConvertVip(CreditConvert creditConvert);
	
	/**
	 * 根据主键user_id和类型查询积分兑换信息
	 * @param user_id
	 * @param type
	 * @return
	 */
	public List<CreditConvertModel> getCreditConvert(long user_id , String type);
	
	//v1.6.7.2 RDPROJECT-569 cx 2013-12-16 start
	/**
	 * 积分兑换产品列表显示
	 * @param param
	 * @return
	 */
	public int getGoodsCount(SearchParam p,Integer compare);
	public PageDataList getGoodsList(int page, SearchParam param,int pageNum,Integer compare);
	public List<GoodsCategoryModel> getCategoryList(Integer parentId,String catetoryType);
	public long saveGoodsAndPic(CreditGoodsModel model);
	public void saveGoodsPic(GoodsPic goodsPic);
	public GoodsCategory getGoodCategoryById(Integer id);
	public CreditGoodsModel getGoodsById(Integer id);
	public void updateGoodsAndPic(CreditGoodsModel model);
	public void delGoodsAndPic(Integer id);
	/**
	 * 用户有效积分
	 * @param userId
	 * @return
	 */
	public CreditModel getCreditByUserId(long userId);
	/**
	 * 积分兑换
	 */
	public void convertCreditValue(CreditConvert convert);
	public void updateCreditConvertByStatus(User user,byte status,Integer id);
	public void updateCreditAndGoods(CreditConvertModel convertModel);
	
	/**
	 * 积分明细列表
	 * @param page
	 * @param param
	 * @return
	 */
	public PageDataList getCreditLogList(int page,SearchParam param,long user_id);
	public void saveCreditLog(CreditLog creditLog);
	
	/**
	 * 积分消费情况
	 * @param page
	 * @param param
	 * @return
	 */
	public PageDataList getCreditConvertList(int page,SearchParam param,long user_id);
	
	/**
	 * 显示最近用户兑换的商品 取近期5个
	 * @return
	 */
	public List<CreditConvertModel> getCreditConvertByStatus();
	
	public int updateGoodsStore(CreditConvertModel convertModel);
	public void addGoodsStore(CreditConvertModel convertModel);
	public void updateCreditValieByCredit(long user_id,Integer credit_value);
	
	/**
	 * 用户撤回兑换
	 * @param id
	 */
	public void updateCreditConvertByStatus(long id);
	public void updateGoodsByStore(Goods goods);
	/**
	 * 根据父类ID 找到对应商品子类
	 */
	public List<GoodsCategoryModel> getGoodsCategoryListByParentId(Integer parentId,Integer id);
	
	public void saveGoodsCategory(GoodsCategory category);
	public void delGoodsCategory(String cateId);
	
	/**
	 * 通过dw_credit_convert 得到商品ID 然后根据商品ID得到分类
	 */
	public String getIsVirtualByConvertGoodsId(int id);
	
	/**
	 * 通过dw_goods id
	 */
	public String getIsVirtualByGoodsId(int id);
	
	/**
	 * 通过商品ID 得到所有图片
	 */
	public List<GoodsPic> getGoodsPicByGoodsId(int goodsId);
	/**
	 * 根据商品ID修改上传图片
	 */
	public void updateGoodsPicByGoodsId(GoodsPic goodsPic);
	
	public void delAllGoodsPicByGoodsId(int goodsId);
	public int showGoodsCount(SearchParam p);
	public PageDataList showGoodsList(int page, SearchParam param,int pageNum);
	//v1.6.7.2 RDPROJECT-569 cx 2013-12-16 end
}
