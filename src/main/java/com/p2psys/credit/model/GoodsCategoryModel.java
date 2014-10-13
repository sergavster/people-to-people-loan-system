package com.p2psys.credit.model;

import java.io.Serializable;
import java.util.List;

import com.p2psys.credit.domain.GoodsCategory;

public class GoodsCategoryModel extends GoodsCategory implements Serializable {

	private List<GoodsCategoryModel> childList;  
	
	private String parentName;

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public List<GoodsCategoryModel> getChildList() {
		return childList;
	}

	public void setChildList(List<GoodsCategoryModel> childList) {
		this.childList = childList;
	}
}
