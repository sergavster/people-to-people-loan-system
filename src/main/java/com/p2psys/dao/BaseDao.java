package com.p2psys.dao;

import java.util.List;


public interface BaseDao {

	public abstract Object insert(Object bean, String key);

	public abstract Object insert(Class clazz, Object bean, String key);

	public abstract Object insert(Object bean);

	public abstract long insertHoldKey(Object bean);

	List findByProperty(Class clazz, String[] props, Object[] values);

	List findByProperty(Class clazz, String property, Object value);
	
	Object findObjByProperty(Class clazz, String property, Object value);

	Object findById(Class clazz, long id);
	
	int modify(Class clazz, Object bean);

	int modify(Class clazz, Object bean, String key);

	int modify(Class clazz, Object bean, String[] updateValues,
			String[] whereValues);
	
	
}