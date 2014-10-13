package com.p2psys.model.tree;

import java.util.List;

import com.p2psys.domain.Site;

public class SiteTree implements Tree<Site> {

	private Site model;
	
	private Tree parent;
	
	private List<Tree> child;

	
	public SiteTree() {
		super();
	}

	public SiteTree(Site model, List<Tree> child) {
		super();
		this.model = model;
		this.child = child;
	}

	@Override
	public boolean hasChild() {
		if(child!=null&&child.size()>0){
			return true;
		}
		return false;
	}

	@Override
	public List<Tree>  getChild() {
		return child;
	}

	@Override
	public Tree getParent() {
		return parent;
	}

	@Override
	public Site getModel() {
		return model;
	}

	@Override
	public void addChild(Tree t) {
		child.add(t);
	}
	
}
