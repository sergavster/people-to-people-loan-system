package com.p2psys.dao.jdbc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.p2psys.dao.BaseDao;
import com.p2psys.model.borrow.BorrowModel;
import com.p2psys.util.DbUtils;
import com.p2psys.util.StringUtils;

public class BaseDaoImpl implements BaseDao {
	
	private static final Logger logger=Logger.getLogger(BaseDaoImpl.class); 
	
	// v1.6.7.1 注解方式进行对象依赖注入 xx 2013-11-28 start
	@Resource
	protected JdbcTemplate jdbcTemplate;
	@Resource
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
        //this.jdbcTemplate = new JdbcTemplate(dataSource);
    	//this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
    
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return namedParameterJdbcTemplate;
	}
	// v1.6.7.1 注解方式进行对象依赖注入 xx 2013-11-28 end

	public String connectSql(BorrowModel model){
		String typeSql=model.getTypeSql();
		String statusSql= model.getStatusSql();
		String searchSql = model.getSearchParamSql();
		String orderSql = model.getOrderSql();
		String LimiteSql = model.getLimitSql();
		return typeSql+statusSql+searchSql+orderSql+LimiteSql;
	}
	
	public String connectCountSql(BorrowModel model){
		String typeSql=model.getTypeSql();
		String statusSql= model.getStatusSql();
		String searchSql = model.getSearchParamSql();
		return typeSql+statusSql+searchSql;
	}

	public String connectSqlWithNoLimit(BorrowModel model){
		String typeSql=model.getTypeSql();
		String statusSql= model.getStatusSql();
		String searchSql = model.getSearchParamSql();
		String orderSql = model.getOrderSql();
		return typeSql+statusSql+searchSql+orderSql;
	}
    
	protected int  count(String sql,Object...args){
		int count=0;
		count=getJdbcTemplate().queryForInt(sql, args);
		return count;
	}
	
	protected int  count(String sql){
		int count=0;
		count=getJdbcTemplate().queryForInt(sql);
		return count;
	}
	
	protected double sum(String sql,Object...args){
		double sum=0;
		SqlRowSet rs=null;
		try {
			rs=getJdbcTemplate().queryForRowSet(sql,args);
			if(rs.next()){
				sum=rs.getDouble("num");
			}
		} catch (InvalidResultSetAccessException e) {
			e.printStackTrace();
		}
		return sum;
	}
	
	protected double sum(String sql,String name,Object...args){
		double sum=0;
		SqlRowSet rs=null;
		try {
			rs=getJdbcTemplate().queryForRowSet(sql,args);
			if(rs.next()){
				sum=rs.getDouble(name);
			}
		} catch (InvalidResultSetAccessException e) {
			e.printStackTrace();
		}
		return sum;
	}

	/**
	 * 返回BeanPropertyRowMapper封装，防止数据库字段为空抛出异常，默认设置null
	 * @param clazz
	 * @return
	 */
	public RowMapper getBeanMapper(Class clazz){
		BeanPropertyRowMapper m=new BeanPropertyRowMapper(clazz);
		m.setPrimitivesDefaultedForNullValue(true);
		return m;
	}

	/**
	 * 实体插入数据库
	 * @param bean 插入的实体类
	 * @param key 知道主键名,非默认的ID
	 * @return 返回插入数据库成功的实体，带主键
	 */
	 
	@Override
	public Object insert(Object bean,String key) {
		return insert(bean.getClass(),bean,key);
	}

	
	/**
	 * 实体插入数据库
	 * @param 插入的实体类
	 * @return 返回插入数据库成功的实体，带主键
	 */
	@Override
	public Object insert(Class clazz,Object bean,String key) {
		String sql=DbUtils.insertSql(bean.getClass(), DbUtils.prefix);
	    SqlParameterSource ps = new BeanPropertySqlParameterSource(bean);
	    KeyHolder keyHolder = new GeneratedKeyHolder();
	    namedParameterJdbcTemplate.update(sql, ps, keyHolder);
	    Number keyNumber=keyHolder.getKey();
	    if(keyNumber!=null){
	    	int id = keyNumber.intValue();
		    if(id>0){
		    	try {
					BeanUtils.getPropertyDescriptor(clazz, key).getWriteMethod().invoke(bean, id);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("设置主键异常！");
				}
		    }
	    }
		return bean;
	}


	/**
	 * 实体插入数据库
	 * @param 插入的实体类
	 * @return 返回插入数据库成功的实体，带主键
	 */
	@Override
	public Object insert(Object bean) {
		return insert(bean,"id");
	}
	
	/**
	 * 实体插入数据库
	 * @param 插入的实体类
	 * @return 返回插入数据库成功的主键
	 */
	@Override
	public long insertHoldKey(Object bean) {
		String key="id";
		Object obj= insert(bean,key);
		long idValue=0L;
		try {
			idValue=(Long)BeanUtils.getPropertyDescriptor(bean.getClass(), key).getReadMethod().invoke(obj, new Object[]{});
		} catch (Exception e) {
			throw new RuntimeException("返回主键异常！");
		} 
		return idValue;
	}

	/**
	 * 根据属性进行查找实体对象
	 * @param clazz class类型
	 * @param props 查询条件的属性
	 * @param values 查询条件的值
	 * @return
	 */
	@Override
	public List findByProperty(Class clazz,String[] props,Object[] values) {
		StringBuffer selectSql=new StringBuffer(DbUtils.findSql(clazz));
		StringBuffer wheres=new StringBuffer();
		Map params = new HashMap();
		for(int i=0;i<props.length;i++){
			wheres.append("`").append(StringUtils.toUnderline(props[i])).append("`").append("=")
			.append(":").append(props[i]).append(" AND ");
			params.put(props[i],values[i]);
		}
		String whereStr=wheres.length()<5?wheres.toString():wheres.substring(0, wheres.length()-5);
		if(props.length>0){
			selectSql.append(" WHERE ").append(whereStr);
		}
		List list = namedParameterJdbcTemplate.query(selectSql.toString(), params, getBeanMapper(clazz));
		return list;
	}
	
	/**
	 * 根据单一条件进行查询实体
	 * @param clazz class类型
	 * @param property 查询条件的属性
	 * @param value 查询条件的值
	 * @return
	 */
	@Override
	public List findByProperty(Class clazz,String property,Object value) {
		StringBuffer selectSql=new StringBuffer(DbUtils.findSql(clazz));
		selectSql.append(" WHERE ").append("`").append(property).append("`").append("=:").append(property);
		Map params = new HashMap();
		params.put(property,value);
		List list = null;
		try {
			list = namedParameterJdbcTemplate.query(selectSql.toString(), params, getBeanMapper(clazz));
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	@Override
	public Object findObjByProperty(Class clazz, String property, Object value) {
		List list = findByProperty(clazz, property, value);
		if(list!=null && list.size()==1){
			return list.get(0);
		}
		if(list!=null && list.size()>1){
			logger.error("查询结果大于1错误：根据"+property+"="+value+",查询"+clazz);
		}
		return null;
	}

	/**
	 * 根据主键进行查找实体对象
	 * @param clazz
	 * @param id
	 * @return
	 */
	@Override
	public Object findById(Class clazz,long id) {
		try {
			StringBuffer selectSql=new StringBuffer(DbUtils.findSql(clazz));
			selectSql.append(" WHERE id=:id");
			Map params = new HashMap();
			params.put("id",id);
			Object o = namedParameterJdbcTemplate.queryForObject(selectSql.toString(), params, getBeanMapper(clazz));
			return o;
		} catch (DataAccessException e) {
			return null;
		}
	}
	
	/**
	 * 封装简单的单表更新语句，省去拼写简单SQL的操作
	 * @param clazz class类型
	 * @param bean 实体类
	 * @param updateValues  需要更新的哪些字段，与java类的字段匹配
	 * @param whereValues   根据哪些字段更新，与java类的字段匹配，id=1;
	 * @return 返回更新后的实体
	 */
	@Override
	public int modify(Class clazz,Object bean,String[] updateValues,String[] whereValues) {
		StringBuffer sql=new StringBuffer("UPDATE ");
		sql.append(DbUtils.prefix).append(StringUtils.toUnderline(clazz.getSimpleName())).append(" SET ");
		
		String[] fnames=DbUtils.findFields(clazz);
		StringBuffer updates=new StringBuffer();
		Map nameMap = new HashMap();
		Object value=null;
		List<String> updateList=Arrays.asList(updateValues);
		
		for(int i=0;i<fnames.length;i++){
			if(DbUtils.exculdeFiledList.contains(fnames[i])) continue;
			if(!updateList.contains(fnames[i])) continue;
			updates.append("`").append(StringUtils.toUnderline(fnames[i])).append("`").append("=")
			.append(":").append(fnames[i]).append(",");
			try {
				value=BeanUtils.getPropertyDescriptor(clazz, fnames[i]).getReadMethod().invoke(bean, new Object[]{});
			} catch (Exception e) {
				throw new RuntimeException("获取Bean值异常！");
			} 
			nameMap.put(fnames[i], value);
		}
		String updateStr=updates.length()<1?updates.toString():updates.substring(0, updates.length()-1);
		
		StringBuffer wheres=new StringBuffer();
		for(int i=0;i<whereValues.length;i++){
			wheres.append("`").append(StringUtils.toUnderline(whereValues[i])).append("`").append("=")
			.append(":").append(whereValues[i]).append(" AND ");
			nameMap.put(whereValues[i], value);
		}
		String whereStr=wheres.length()<5?wheres.toString():wheres.substring(0, wheres.length()-5);
		
		sql.append(updateStr);
		if(whereValues.length>0){
			sql.append(" WHERE ").append(whereStr);
		}
		
		SqlParameterSource ps = new BeanPropertySqlParameterSource(bean);
		int o =0;
		try {
			o=namedParameterJdbcTemplate.update(sql.toString(), ps);
		} catch (DataAccessException e) {
			logger.error("ERROR SQL:"+sql.toString());
			throw e;
		}
		return o;
	}
	
	/**
	 * 默认根据ID进行更新
	 * @param clazz
	 * @param bean
	 * @return
	 */
	@Override
	public int modify(Class clazz,Object bean) {
		return modify(clazz,bean,"id");
	}
	/**
	 * 默认根据key进行更新
	 * @param clazz
	 * @param bean
	 * @param key
	 * @return
	 */
	@Override
	public int modify(Class clazz,Object bean,String key) {
		return modify(clazz,bean,DbUtils.findFields(clazz),new String[]{key});
	}
}
