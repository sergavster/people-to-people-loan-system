package com.p2psys.domain;

import java.io.Serializable;

/**
 * 文章实体类
 * 
 
 * @date 2012-7-5-下午12:46:16
 * @version
 * 
 *           (c)</b> 2012-51-<br/>
 * 
 */
public class Article implements Serializable {

	private static final long serialVersionUID = 7029710509016395903L;

	private long id;
	private long site_id;
	private String name;
	private String littitle;
	private int status;
	private String flag;
	private String source;
	private String publish;
	private String author;
	private String summary;
	private String content;
	private int order;
	// v1.6.5.3 RDPROJECT-159 xx 2013.09.13 start
	private int is_top;//是否置顶，0:否 1:是
	// v1.6.5.3 RDPROJECT-159 xx 2013.09.13 end
	private int hits;
	private String litpic;
	private long user_id;
	private String addtime;
	private String addip;
	
	//扩展字段
	private String sitename;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSite_id() {
		return site_id;
	}

	public void setSite_id(long siteId) {
		site_id = siteId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLittitle() {
		return littitle;
	}

	public void setLittitle(String littitle) {
		this.littitle = littitle;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getPublish() {
		return publish;
	}

	public void setPublish(String publish) {
		this.publish = publish;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}

	public String getLitpic() {
		return litpic;
	}

	public void setLitpic(String litpic) {
		this.litpic = litpic;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long userId) {
		user_id = userId;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getAddip() {
		return addip;
	}

	public void setAddip(String addip) {
		this.addip = addip;
	}

	public int getIs_top() {
		return is_top;
	}

	public void setIs_top(int is_top) {
		this.is_top = is_top;
	}

	public String getSitename() {
		return sitename;
	}

	public void setSitename(String sitename) {
		this.sitename = sitename;
	}
	
}
