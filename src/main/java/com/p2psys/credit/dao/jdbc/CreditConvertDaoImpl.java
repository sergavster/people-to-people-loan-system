package com.p2psys.credit.dao.jdbc;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.p2psys.credit.dao.CreditConvertDao;
import com.p2psys.credit.domain.CreditLog;
import com.p2psys.credit.domain.Goods;
import com.p2psys.credit.domain.GoodsCategory;
import com.p2psys.credit.domain.GoodsPic;
import com.p2psys.credit.model.CreditGoodsModel;
import com.p2psys.credit.model.CreditModel;
import com.p2psys.credit.model.GoodsCategoryModel;
import com.p2psys.dao.jdbc.BaseDaoImpl;
import com.p2psys.domain.CreditConvert;
import com.p2psys.domain.User;
import com.p2psys.model.CreditConvertModel;
import com.p2psys.model.SearchParam;
import com.p2psys.util.DateUtils;
import com.p2psys.util.StringUtils;


@Repository
public class CreditConvertDaoImpl extends BaseDaoImpl implements CreditConvertDao {

	private static Logger logger = Logger.getLogger(CreditConvertDaoImpl.class);

	// v1.6.7.2 RDPROJECT-560 zza 2013-12-13 start
	@Override
	public int getCreditConvertCount(SearchParam p) {
		StringBuffer sql = new StringBuffer(
				"select count(id)  from  dw_credit_convert as p1 left join dw_user as p2 on p1.user_id=p2.user_id  where 1 = 1 ");
		/*Map<String, Object> map = new HashMap<String, Object>();
		if (status != null) {
			map.put("status", status);
			sql.append(" and p1.status = :status ");
		}

		if (user_id != null && user_id > 0) {
			map.put("user_id", user_id);
			sql.append("and p1.user_id = :user_id ");
		}*/
		// v1.6.7.2 RDPROJECT-560 zza 2013-12-13 end
		sql.append(p.getSearchParamSql());
		try {
			return getNamedParameterJdbcTemplate().queryForInt(sql.toString(), new BeanPropertySqlParameterSource(Integer.class));
		} catch (DataAccessException e) {
			logger.error("查询错误。");
		}
		return 0;
	}

	// v1.6.7.2 RDPROJECT-560 zza 2013-12-13 start
	@Override
	@SuppressWarnings({ "unchecked" })
	public List<CreditConvertModel> getCreditConvertPage(int page, int max, SearchParam p) {
		StringBuffer sql = new StringBuffer(
				"select p1.* , p2.username from dw_credit_convert as p1 left join dw_user as p2 on p1.user_id=p2.user_id  where 1 = 1");
		Map<String, Object> map = new HashMap<String, Object>();
//		if (status != null) {
//			map.put("status", status);
//			sql.append(" and p1.status = :status");
//		}
//		if (user_id != null && user_id > 0) {
//			map.put("user_id", user_id);
//			sql.append(" and p1.user_id = :user_id");
//		}
//		map.put("status", status);
//		map.put("user_id", user_id);
		// v1.6.7.2 RDPROJECT-560 zza 2013-12-13 end
		sql.append(p.getSearchParamSql());
		sql.append(" order by p1.id desc LIMIT :page,:max");
		map.put("page", page);
		map.put("max", max);
		return getNamedParameterJdbcTemplate().query(sql.toString(), map, getBeanMapper(CreditConvertModel.class));
	}

	/**
	 * 新增积分兑换信息
	 * 
	 * @param creditConvert
	 * @return
	 */
	public void insertCreditConvert(CreditConvert creditConvert) {
		StringBuffer sql = new StringBuffer(
				"insert into dw_credit_convert(user_id,credit_value,convert_money,money,status,addtime,verify_time,verify_user,verify_user_id,remark,type,convert_num,goods_id)");
		sql.append("value (:user_id,:credit_value,:convert_money,:money,:status,:addtime,:verify_time,:verify_user,:verify_user_id,:remark,:type,:convert_num,:goods_id)");
		SqlParameterSource ps = new BeanPropertySqlParameterSource(creditConvert);
		this.getNamedParameterJdbcTemplate().update(sql.toString(), ps);
	}

	@SuppressWarnings({ "unchecked"})
	public CreditConvertModel getCreditConvertById(Long id) {
		StringBuffer sql = new StringBuffer(
				"select p1.* , p2.username  from  dw_credit_convert as p1 left join dw_user as p2 on p1.user_id=p2.user_id  where 1 = 1");
		Map<String, Object> map = new HashMap<String, Object>();
		if (id != null && id > 0) {
			map.put("id", id);
			sql.append(" and p1.id = :id");
		}
		return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), map,getBeanMapper(CreditConvertModel.class));
	}

	/**
	 * 积分兑换审核
	 * 
	 * @param creditConvert 积分兑换实体类
	 */
	public void auditCreditConvert(CreditConvert creditConvert) {
		StringBuffer sql = new StringBuffer("update dw_credit_convert set money = :money,status = :status,");
		sql.append("verify_time = :verify_time,verify_user = :verify_user,verify_user_id = :verify_user_id ,type = :type , convert_num = :convert_num where id = :id");
		SqlParameterSource ps = new BeanPropertySqlParameterSource(creditConvert);
		this.getNamedParameterJdbcTemplate().update(sql.toString(), ps);
	}
	
	/**
	 * 根据主键user_id和类型查询积分兑换信息
	 * @param user_id
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CreditConvertModel> getCreditConvert(long user_id , String type){
		StringBuffer sql = new StringBuffer( "select p1.* , p2.username  from  dw_credit_convert as p1 left join dw_user as p2 on p1.user_id=p2.user_id  where 1 = 1");
		Map<String, Object> map = new HashMap<String, Object>();
		if (type != null) {
			map.put("type", type);
			sql.append(" and p1.type = :type");
		}
		if (user_id > 0) {
			map.put("user_id", user_id);
			sql.append(" and p1.user_id = :user_id");
		}
		return getNamedParameterJdbcTemplate().query(sql.toString(), map, getBeanMapper(CreditConvertModel.class));
		
	}
	
	//v1.6.7.2 RDPROJECT-569 cx 2013-12-17 start
	
	/**
	 * 前端显示商品列表 上架时间<今天
	 */
	public int showGoodsCount(SearchParam p){
		String startDate=DateUtils.dateStr2(new Date());
		String startTime=Long.toString(DateUtils.valueOf(startDate+" 23:59:59").getTime()/1000);
		StringBuffer sql = new StringBuffer("select count(1) from dw_goods p1,dw_goods_pic p2 where p1.id=p2.goods_id and p2.ordering=1 and p1.store>0 and p1.shelves_time<"+startTime);
		sql.append(p.getSearchParamSql());
		try {
			logger.info("showGoodsCount:"+sql.toString());
			return getNamedParameterJdbcTemplate().queryForInt(sql.toString(), new BeanPropertySqlParameterSource(Integer.class));
		} catch (DataAccessException e) {
			logger.error("查询错误。");
		}
		return 0;
	}
	
	public List<CreditGoodsModel> showGoodsList(int page, int max, SearchParam p){
		String startDate=DateUtils.dateStr2(new Date());
		String startTime=Long.toString(DateUtils.valueOf(startDate+" 23:59:59").getTime()/1000);
		StringBuffer sql=new StringBuffer("select p1.*,p2.pic_url,p2.pic_name,p2.compress_pic_url from dw_goods p1,dw_goods_pic p2 where p1.id=p2.goods_id and p2.ordering=1 and p1.store>0 and  p1.shelves_time<"+startTime);
		sql.append(p.getSearchParamSql());
		Map<String, Object> map = new HashMap<String, Object>();
		sql.append(" order by p1.id desc limit :page,:max");
		map.put("page", page);
		map.put("max", max);
		logger.info("showGoodsList:"+sql.toString());
		return getNamedParameterJdbcTemplate().query(sql.toString(), map, getBeanMapper(CreditGoodsModel.class));
		
	}
	
	/**
	 * 积分兑换产品列表显示
	 * @param param
	 * @return
	*/
	public int getGoodsCount(SearchParam p,Integer compare) {
		StringBuffer sql = new StringBuffer("select count(1) from dw_goods p1,dw_goods_pic p2 where p1.id=p2.goods_id and p2.ordering=1");
		sql.append(p.getSearchParamSql());
		if(compare!=null && compare==0){
			sql.append(" and p1.store>0");
		}
		try {
			logger.info("积分商品列表count:"+sql.toString());
			return getNamedParameterJdbcTemplate().queryForInt(sql.toString(), new BeanPropertySqlParameterSource(Integer.class));
		} catch (DataAccessException e) {
			logger.error("查询错误。");
		}
		return 0;
	}
	
	/**
	 * 查询ording=1 第一个显示
	 */
	public List<CreditGoodsModel> getGoodsList(int page, int max, SearchParam p,Integer compare){
		StringBuffer sql=new StringBuffer("select p1.*,p2.pic_url,p2.pic_name,p2.compress_pic_url from dw_goods p1,dw_goods_pic p2 where p1.id=p2.goods_id and p2.ordering=1");
		sql.append(p.getSearchParamSql());
		if(compare!=null && compare==0){
			sql.append(" and p1.store>0");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		sql.append(" order by p1.id desc limit :page,:max");
		map.put("page", page);
		map.put("max", max);
		logger.info("积分商品列表list:"+sql.toString());
		return getNamedParameterJdbcTemplate().query(sql.toString(), map, getBeanMapper(CreditGoodsModel.class));
		
	}
	
	/**
	 * parent_id =0
	 * 得出二级商品菜单项
	 */
	public List<GoodsCategoryModel> getCategoryList(Integer parentId,String catetoryType){
		String sql="select * from dw_goods_category where 1=1";
		if(parentId!=null && parentId==0){
			sql+=" and parent_id <> 0";
		}
		if(!StringUtils.isBlank(catetoryType)&&(!("-1").equals(catetoryType))){
			sql+=" and parent_id ="+catetoryType;
		}
		sql+=" order by id desc";
		Map<String, Object> map = new HashMap<String, Object>();
		return getNamedParameterJdbcTemplate().query(sql.toString(), map, getBeanMapper(GoodsCategoryModel.class));
	}
	public long saveGoodsAndPic(CreditGoodsModel model){
		Goods goods=new Goods();
		goods.setName(model.getName());
		goods.setCredit_value(model.getCredit_value());
		goods.setStore(model.getStore());
		goods.setShelves_time(Integer.parseInt(model.getShelves_time()+""));
		goods.setAdd_time(Integer.parseInt(model.getAdd_time()+""));
		goods.setUpdate_time(Integer.parseInt(model.getUpdate_time()+""));
		goods.setCategory_id(model.getCategory_id());
		goods.setDescription(model.getDescription());
		goods.setCost(model.getCost());
		goods.setMarket_price(model.getMarket_price());
		goods.setAttribute(model.getAttribute());
		goods.setRemarks(model.getRemarks());
		return insertHoldKey(goods);
//		GoodsPic goodsPic=new GoodsPic();
//		goodsPic.setGoods_id(goodsId);
//		goodsPic.setPic_name(model.getPic_name());
//		goodsPic.setPic_url(model.getPic_url());
//		goodsPic.setAdd_time(Integer.parseInt(model.getAdd_time()+""));
//		goodsPic.setOrdering(model.getOrdering());
//		goodsPic.setCompress_pic_url(model.getCompress_pic_url());
//		insert(goodsPic);
	}
	
	public void delAllGoodsPicByGoodsId(int goodsId){
		//保存前先删除该商品ID的所有图片
		this.jdbcTemplate.update("delete from dw_goods_pic where goods_id="+goodsId);
	}
	public void saveGoodsPic(GoodsPic goodsPic){
		insert(goodsPic);
	}
	public GoodsCategory getGoodCategoryById(Integer id){
		if(id!=null){
			String sql="select name from dw_goods_category where id="+id;
			return (GoodsCategory) findById(GoodsCategory.class,id);
		}
		return null;
	}
	
	public CreditGoodsModel getGoodsById(Integer id){
		if(id!=null){
			String sql="select p1.*,p2.pic_url,p2.compress_pic_url from dw_goods p1,dw_goods_pic p2 where p1.id=p2.goods_id and p2.ordering=1 and p1.id="+id;
		//	return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), new HashMap(),getBeanMapper(CreditGoodsModel.class));
			List<CreditGoodsModel> list= getNamedParameterJdbcTemplate().query(sql, new HashMap(), getBeanMapper(CreditGoodsModel.class));
			if(list!=null && list.size()>0){
				return list.get(0);
			}
		}
		return null;
	}
	public void updateGoodsAndPic(CreditGoodsModel model){
		String sql="update dw_goods set name=?,credit_value=?,store=?,shelves_time=?,update_time=?,category_id=?,description=?,cost=?,market_price=?,attribute=?,remarks=? where id=?";
		getJdbcTemplate().update(sql,new Object[]{model.getName(),model.getCredit_value(),model.getStore(),model.getShelves_time(),model.getUpdate_time(),model.getCategory_id(),
												model.getDescription(),model.getCost(),model.getMarket_price(),model.getAttribute(),model.getRemarks(),model.getId()});
		
//		String picSql="update dw_goods_pic set pic_name=?,pic_url=?,compress_pic_url=? where goods_id=?";
//		getJdbcTemplate().update(picSql,new Object[]{model.getPic_name(),model.getPic_url(),model.getCompress_pic_url(),model.getId()});
			
	}
	
	/**
	 * 根据商品ID修改上传图片
	 */
	public void updateGoodsPicByGoodsId(GoodsPic goodsPic){
		String picSql="update dw_goods_pic set pic_name=?,pic_url=?,compress_pic_url=?,add_time=?,ordering=? where goods_id=?";
		getJdbcTemplate().update(picSql,new Object[]{goodsPic.getPic_name(),goodsPic.getPic_url(),goodsPic.getCompress_pic_url(),goodsPic.getAdd_time(),goodsPic.getAdd_time(),goodsPic.getOrdering(),goodsPic.getGoods_id()});
	}
	
	public void delGoodsAndPic(Integer id){
		if(id!=null){
			String picSql="delete from dw_goods_pic where goods_id="+id;
			this.jdbcTemplate.execute(picSql);
			String sql="delete from dw_goods where id="+id;
			this.jdbcTemplate.execute(sql);
		}
	}
	
	/**
	 * 用户有效积分
	 */
	public CreditModel getCreditByUserId(long userId){
		if(!StringUtils.isEmpty(userId+"")){
			String sql="select * from dw_credit where user_id="+userId;
			Map<String, Object> map = new HashMap<String, Object>();
			List<CreditModel> list=getNamedParameterJdbcTemplate().query(sql, map, getBeanMapper(CreditModel.class));
			if(list!=null && list.size()>0){
				return list.get(0);
			}
		}
		return null;
	}
	
	/**
	 * 修改商品审核状态
	 */
	public void updateCreditConvertByStatus(User user,byte status,Integer id){
		String sql="update dw_credit_convert set status="+status+",verify_time="+Long.toString(new Date().getTime()/1000)+",verify_user='"+user.getUsername()+"',verify_user_id="+user.getUser_id()+" where id="+id;
		this.jdbcTemplate.execute(sql);
	}
		
	/**
	 * 前台兑换提交后商品数量减少
	 */
	public synchronized int updateGoodsStore(CreditConvertModel convertModel){
		if(convertModel.getGoods_id()!=null){
			CreditGoodsModel goodsModel=this.getGoodsById(convertModel.getGoods_id());
			int total=goodsModel.getStore()-convertModel.getConvert_num();
			if(total>=0){
				String sql="update dw_goods set store="+total+" where id="+goodsModel.getId();
				this.jdbcTemplate.execute(sql);
				return 1;
			}else{
				logger.info("兑换商品数量不够！");
				return 0;
			}
		}
		return 0;
	}
	
	
	/**
	 * 积分明细展示
	 * @param start
	 * @param end
	 * @param param
	 * @return
	 */
	public List<CreditGoodsModel> getCreditLogList(int start, int limit, SearchParam param,long user_id){
		StringBuffer sql=new StringBuffer("select p1.*,p2.name as type_name from dw_credit_log p1,dw_credit_type p2 where p1.type_id=p2.id and p1.user_id="+user_id);
		sql.append(param.getSearchParamSql());
		sql.append(" order by p1.addtime desc,p1.id desc");
		if(limit!=0){
			sql.append(" limit "+start+","+limit);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		return getNamedParameterJdbcTemplate().query(sql.toString(), map, getBeanMapper(CreditGoodsModel.class));
	}
	public Integer getCreditLogCount(SearchParam param,long user_id){
		StringBuffer sql=new StringBuffer("select count(1) from dw_credit_log p1,dw_credit_type p2  where p1.type_id=p2.id and p1.user_id="+user_id);
		sql.append(param.getSearchParamSql());
		return this.jdbcTemplate.queryForInt(sql.toString());
	}
	
	/**
	 * dw_credit_log明细
	 */
	public void saveCreditLog(CreditLog creditLog){
//		CreditLog creditLog=new CreditLog();
//		creditLog.setUser_id(logModel.getUser_id());
//		creditLog.setType_id(logModel.getType_id());
//		creditLog.setOp(Long.parseLong((logModel.getOp()+"")));
//		creditLog.setValue(logModel.getValue());
//		creditLog.setRemark(logModel.getRemark());
//		creditLog.setOp_user(Integer.parseInt(logModel.getOp_user()));
//		creditLog.setAddtime(Integer.parseInt(logModel.getAddtime()));
//		creditLog.setAddip(logModel.getAddip());
		insert(creditLog);
//		String sql="insert into dw_credit_log(user_id,type_id,op,value,remark,op_user,addtime,addip) values(?,?,?,?,?,?,?,?)";
//		this.jdbcTemplate.update(sql,logModel.getUser_id(),logModel.getType_id(),logModel.getOp(),logModel.getValue(),logModel.getRemark(),logModel.getOp_user(),logModel.getAddtime(),logModel.getAddip());
	}
	
	/**
	 * 根据user_id拿到dw_credit_convert详细信息    --and a.type='expense_change'
	 */ 
	public List<CreditConvert> getCreditConvertList(int start, int limit, SearchParam param,long user_id){
		String sql="select a.*,b.name as goods_name,b.credit_value as goods_credit_value,c.pic_url from dw_credit_convert a,dw_goods b,dw_goods_pic c where a.goods_id=b.id and b.id=c.goods_id  and c.ordering=1 and a.user_id="+user_id+" order by a.addtime desc,a.id desc";
		sql+=param.getSearchParamSql();
		if(limit!=0){
			sql+=" limit "+start+","+limit;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		return getNamedParameterJdbcTemplate().query(sql.toString(), map, getBeanMapper(CreditConvert.class));
	}
	public Integer getCreditConvertCount(SearchParam param,long user_id){
		String sql="select count(1) from dw_credit_convert a,dw_goods b,dw_goods_pic c where a.goods_id=b.id and b.id=c.goods_id and c.ordering=1 and a.user_id="+user_id;
		sql+=param.getSearchParamSql();
		return this.jdbcTemplate.queryForInt(sql);
	}
	
	/**
	 * 最近用户兑换的商品显示 取近期10个
	 * @return
	 */
	public List<CreditConvertModel> getCreditConvertByStatus(){
		String sql="select c.username,b.name as goods_name,a.convert_num,a.credit_value from dw_credit_convert a,dw_goods b,dw_user c where a.goods_id=b.id and a.user_id=c.user_id and a.status=1 order by a.addtime desc,a.id desc limit 10";
		Map<String, Object> map = new HashMap<String, Object>();
		return getNamedParameterJdbcTemplate().query(sql.toString(), map, getBeanMapper(CreditConvertModel.class));
	}
	
	public void addGoodsStore(CreditConvertModel convertModel){
		String sql="select * from dw_goods where id="+convertModel.getGoods_id();
		Goods goods=getNamedParameterJdbcTemplate().queryForObject(sql, new HashMap(),getBeanMapper(Goods.class));
		if(goods!=null){
			goods.setStore(goods.getStore()+convertModel.getConvert_num());
			modify(Goods.class, goods);
		}
	}
	
	public List<GoodsPic> getGoodsPicByGoodIs(Integer goodsId){
		String sql="select * from dw_goods_pic where goods_id="+goodsId;
		return getNamedParameterJdbcTemplate().query(sql.toString(), new HashMap(), getBeanMapper(GoodsPic.class));
	}

	/**
	 * 兑换商品审核失败 还原用户积分
	 */
	public void updateCreditValieByCredit(long user_id,Integer credit_value){
		CreditModel creditModel=this.getCreditByUserId(user_id);
		creditModel.setValid_value(creditModel.getValid_value()+credit_value);
		creditModel.setExpense_value(creditModel.getExpense_value()-credit_value);
		String sql="update dw_credit set valid_value="+creditModel.getValid_value()+",expense_value="+creditModel.getExpense_value()+" where user_id="+user_id;
		this.jdbcTemplate.update(sql);
	}
	
	/**
	 * 用户撤回兑换
	 */
	public void updateCreditConvertByStatus(long id){
		String sql="update dw_credit_convert set status=3 where id="+id;
		this.jdbcTemplate.update(sql);
	}
	public void updateGoodsByStore(Goods goods){
		modify(Goods.class, goods);
	}
	
	/**
	 * 根据父类ID 找到对应商品子类
	 */
	public List<GoodsCategoryModel> getGoodsCategoryListByParentId(Integer parentId,Integer id){
		String sql="select * from dw_goods_category where 1=1";
		if(parentId!=null){
			sql+=" and parent_id="+parentId;
		}
		if(id!=null){
			sql+=" and id="+id;
		}
		List<GoodsCategoryModel> list=getNamedParameterJdbcTemplate().query(sql.toString(), new HashMap(), getBeanMapper(GoodsCategoryModel.class));
		return list;
	}
	
	public void saveGoodsCategory(GoodsCategory category){
		insert(category);
	}
	
	/**
	 * 删除商品类别 
	 * 1：子类直接删除 2：父类连带子类一起删除
	 * 2:对应分类产品连带删除(目前只有2级)
	 */
	public void delGoodsCategory(String cateId){
		List<GoodsCategory> list=getNamedParameterJdbcTemplate().query("select * from dw_goods_category where id="+Integer.parseInt(cateId)+" or parent_id="+Integer.parseInt(cateId), new HashMap(), getBeanMapper(GoodsCategoryModel.class));
		String str="";
		if(list!=null&&list.size()>0){
			for (GoodsCategory c : list) {
				str+=c.getId()+",";
			}
		}
		if(str.endsWith(",")){
			str=str.substring(0,str.length()-1);
		}
		this.jdbcTemplate.execute("delete from dw_goods where category_id in ("+str+")");
		this.jdbcTemplate.update("delete from dw_goods_category where parent_id="+Integer.parseInt(cateId));
		this.jdbcTemplate.update("delete from dw_goods_category where id="+Integer.parseInt(cateId));
		
	}
	
	/**
	 * 通过dw_credit_convert 得到商品ID 然后根据商品ID得到分类
	 */
	public String getIsVirtualByConvertGoodsId(int id){
		String sql="select c.is_virtual as is_virtual from dw_credit_convert a,dw_goods b,dw_goods_category c where a.goods_id=b.id and b.category_id=c.id and a.id="+id;
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList(sql);
		if(list!=null && list.size()>0){
			if(list.get(0).get("is_virtual")!=null){
				return list.get(0).get("is_virtual").toString();
			}
		}
		return null;
	}
	
	/**
	 * 通过dw_goods id
	 */
	public String getIsVirtualByGoodsId(int id){
		String sql="select b.is_virtual as is_virtual from dw_goods a,dw_goods_category b where a.category_id=b.id and a.id="+id;
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList(sql);
		if(list!=null && list.size()>0){
			if(list.get(0).get("is_virtual")!=null){
				return list.get(0).get("is_virtual").toString();
			}
		}
		return null;
	}
	
	/**
	 * 通过商品ID 得到所有图片
	 */
	public List<GoodsPic> getGoodsPicByGoodsId(int goodsId){
		String sql="select * from dw_goods_pic where goods_id="+goodsId;
		return getNamedParameterJdbcTemplate().query(sql, new HashMap(), getBeanMapper(GoodsPic.class));
	}
	//v1.6.7.2 RDPROJECT-569 cx 2013-12-17 end
}
