package com.p2psys.service;

import java.util.List;

import com.p2psys.domain.Linkage;
import com.p2psys.domain.LinkageType;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;



public interface LinkageService {
	public List linkageList(int typeid);

	//v1.6.7.2 RDPROJECT-26 sj 2013-12-24 start
	/**
	 * 
	 * 查询dw_linkage_type数据
	 * 
	 * */
	public PageDataList getLinkageTypeListByPageNumber(int page,
			SearchParam searchParam);
	
	/**
	 * 
	 * 向dw_linkage_type插入数据
	 * 
	 * */
	public void addLinkageType(LinkageType linkageType);
	
	/**
	 * 
	 * 查询dw_linkage数据
	 * 
	 * */
	public PageDataList getLinkageListByPageNumber(int page,
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
	 * 通过id获得linkage对象
	 * 
	 * */
	public Linkage getLinkageById(long id);
	
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
	 * 更新linkagetype
	 * 
	 * */
	public void updateLinkageType(LinkageType linkageType);
	//v1.6.7.2 RDPROJECT-26 sj 2013-12-24 end
}
