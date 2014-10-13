package com.p2psys.dao;

import java.util.List;

import com.p2psys.domain.Areainfo;
import com.p2psys.domain.Linkage;
import com.p2psys.domain.LinkageType;
import com.p2psys.model.SearchParam;

public interface LinkageDao extends BaseDao {
	/**
	 * 
	 * @param typeid
	 * @param type
	 * @return
	 */
	public List<Linkage> getLinkageByTypeid(int typeid,String type);

	
	public List<Areainfo> getAreainfoByPid(String pid) ;


	public List<Linkage> getLinkageByNid(String nid,String type);
	
	public Linkage getLinkageById(long id);
	
	public Linkage getLinkageByValue(String nid,String value);
	
	
	public String getLinkageNameByid(long id);
	/**
	 * 根据ID获取地区name
	 */
	public String getAreaByPid(String id);

	//v1.6.7.2 RDPROJECT-26 sj 2013-12-24 start
	/**
	 * 
	 * 获得查询的总条数
	 * 
	 * */
	public int getLinkageTypeCount(SearchParam searchParam);

	/**
	 * 
	 * 获得dw_linkage_type查询数据的集合
	 * 
	 * */
	public List getLinkageTypeBySearch(int start, int pernum,
			SearchParam searchParam);
	
	/**
	 * 
	 * 向dw_linkage_type插入数据
	 * 
	 * */
	public void addLinkageType(LinkageType linkageType);
	

	/**
	 * 
	 * 获得dw_linkage查询的总条数
	 * 
	 * */
	public int getLinkageCount(SearchParam searchParam);

	/**
	 * 
	 * 获得dw_linkage查询数据的集合
	 * 
	 * */
	public List getLinkageBySearch(int start, int pernum,
			SearchParam searchParam);

	/**
	 * 
	 * 获得dw_linkage_type的id和name
	 * 
	 * */
	public List<LinkageType> getTypeList();

	/**
	 * 
	 * 向dw_linkage_type插入数据
	 * 
	 * */
	public void addLinkage(Linkage linkage);
	
	/**
	 * 
	 * 更新linkage
	 * 
	 * */
	public void updateLinkage(Linkage linkage);

	/**
	 * 
	 * 修改linkagetype
	 * 
	 * */
	public LinkageType getLinkageTypeById(long id);

	/**
	 * 
	 * 更新linkage
	 * 
	 * */
	public void updateLinkageType(LinkageType linkageType);
	//v1.6.7.2 RDPROJECT-26 sj 2013-12-24 end
}
