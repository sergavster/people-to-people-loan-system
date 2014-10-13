package com.p2psys.model.purview;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.p2psys.domain.Purview;
import com.p2psys.util.StringUtils;

public class PurviewSet {
	private List<Purview> purviewList;
	private Purview model;
	private Set childs;

	public PurviewSet(List purviewList) {
		super();
		this.purviewList = purviewList;
	}

	public PurviewSet(Purview model, Set childs) {
		super();
		this.model = model;
		this.childs = childs;
	}

	public PurviewSet() {
		super();
	};

	public Purview getModel() {
		return model;
	}

	public void setModel(Purview model) {
		this.model = model;
	}

	public Set getChilds() {
		return childs;
	}

	public void setChilds(Set childs) {
		this.childs = childs;
	}

	public boolean hasList(){
		if(purviewList==null||purviewList.size()<1){
			return false;
		}
		return true;
	}
	
	public boolean contains(String url){
		if(!hasList()||StringUtils.isBlank(url)){
			return false;
		}
		for(Purview p:purviewList){
			if(StringUtils.isNull(p.getUrl()).equals(url)){
				return true;
			}
		}
		return false;
	}
	
	public Set toSet(){
		childs=new TreeSet(new PurviewLevelCompare());
		List<Purview> firstList=new ArrayList<Purview>();
		List<Purview> secList=new ArrayList<Purview>();
		List<Purview> thirdList=new ArrayList<Purview>();
		
		if(purviewList==null) return childs;
		
		for(Purview p:purviewList){
			switch(p.getLevel()){
				case 1:firstList.add(p);break;
				case 2:secList.add(p);break;
				case 3:thirdList.add(p);break;
			}
		}
		
		PurviewSet ps=null;
		
		for(Purview p1:firstList){
			Set set1=new TreeSet(new PurviewLevelCompare());
			for(Purview p2:secList){
				if(p2.getPid()==p1.getId()){
					Set set2=new TreeSet(new PurviewLevelCompare());
					for(Purview p3:thirdList){
						if(p3.getPid()==p2.getId()){
							PurviewSet ps3=new PurviewSet(p3,null);
							set2.add(ps3);
						}
					}
					PurviewSet ps2=new PurviewSet(p2,set2);
					set1.add(ps2);
				}
			}
			ps=new PurviewSet(p1,set1);
			childs.add(ps);
		}
		
		return childs;
	}
	
}
