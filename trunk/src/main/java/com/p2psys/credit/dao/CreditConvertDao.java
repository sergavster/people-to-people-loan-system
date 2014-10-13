package com.p2psys.credit.dao;

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
import com.p2psys.model.SearchParam;

/**
 * 积分兑换DAO接口
 
 * @version 1.0
 * @since 2013-10-15
 */
public interface CreditConvertDao {

	// v1.6.7.2 RDPROJECT-560 zza 2013-12-13 start
	/**
	 * 根基条件查询积分兑换信息
	 * @param p
	 * @param status 积分兑换审核类型
	 * @param user_id 会员ID
	 * @return
	 */
	public int getCreditConvertCount(SearchParam p);
	
	/**
	 * 积分兑换分页接口
	 * @param page分页开始页
	 * @param max分页数量
	 * @param p查询参数
	 * @param status 查询信息状态
	 * @param user_id 会员ID
	 * @return
	 */
	public List<CreditConvertModel> getCreditConvertPage(int page, int max,SearchParam p);
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
	 */
	public void auditCreditConvert(CreditConvert creditConvert);
	
	/**
	 * 根据主键user_id和类型查询积分兑换信息
	 * @param user_id
	 * @param type
	 * @return
	 */
	public List<CreditConvertModel> getCreditConvert(long user_id , String type);
	
	
	//v1.6.7.2 RDPROJECT-569 cx 2013-12-17 start
	/**
	 * 积分兑换产品列表显示
	 * @param param
	 * @return
	 */
	public int getGoodsCount(SearchParam p,Integer compare);
	public List<CreditGoodsModel> getGoodsList(int page, int max, SearchParam p,Integer compare);
	public List<GoodsCategoryModel> getCategoryList(Integer parentId,String catetoryType);
	public long saveGoodsAndPic(CreditGoodsModel model);
	public void saveGoodsPic(GoodsPic goodsPic);
	public GoodsCategory getGoodCategoryById(Integer id);
	public CreditGoodsModel getGoodsById(Integer id);
	public void updateGoodsAndPic(CreditGoodsModel model);
	public void delGoodsAndPic(Integer id);
	/**
	 * 得出用户有效积分
	 * @param userId
	 * @return
	 */
	public CreditModel getCreditByUserId(long userId);
	public void updateCreditConvertByStatus(User user,byte status,Integer id);
	public int updateGoodsStore(CreditConvertModel convertModel);
	/**
	 * 积分明细展示
	 * @param start
	 * @param end
	 * @param param
	 * @return
	 */
	public List<CreditGoodsModel> getCreditLogList(int start, int limit, SearchParam param,long user_id);
	public Integer getCreditLogCount(SearchParam param,long user_id);
	public void saveCreditLog(CreditLog creditLog);
	public List<CreditConvert> getCreditConvertList(int start, int limit, SearchParam param,long user_id);
	public Integer getCreditConvertCount(SearchParam param,long user_id);
	
	/**
	 * 最近用户兑换的商品显示
	 * @return
	 */
	public List<CreditConvertModel> getCreditConvertByStatus();
	public void addGoodsStore(CreditConvertModel convertModel);
	public List<GoodsPic> getGoodsPicByGoodIs(Integer goodsId);
	/**
	 * 增加用户积分（还原）
	 * @param user_id
	 * @param credit_value
	 */
	public void updateCreditValieByCredit(long user_id,Integer credit_value);
	
	/**
	 * 撤回
	 */
	public void updateCreditConvertByStatus(long id);
	public void updateGoodsByStore(Goods goods);
	/**
	 * 根据父类ID 找到对应商品子类
	 */
	public List<GoodsCategoryModel> getGoodsCategoryListByParentId(Integer parentId,Integer id);
	
	public void saveGoodsCategory(GoodsCategory category);
	public void delGoodsCategory(String cateId);
	
	public String getIsVirtualByConvertGoodsId(int id);
	public String getIsVirtualByGoodsId(int id);
	public List<GoodsPic> getGoodsPicByGoodsId(int goodsId);
	public void updateGoodsPicByGoodsId(GoodsPic goodsPic);
	public void delAllGoodsPicByGoodsId(int goodsId);
	public int showGoodsCount(SearchParam p);
	public List<CreditGoodsModel> showGoodsList(int page, int max, SearchParam p);
	//v1.6.7.2 RDPROJECT-569 cx 2013-12-17 end
	
}
