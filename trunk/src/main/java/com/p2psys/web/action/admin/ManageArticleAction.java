package com.p2psys.web.action.admin;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ModelDriven;
import com.p2psys.common.enums.EnumPic;
import com.p2psys.context.Constant;
import com.p2psys.domain.Article;
import com.p2psys.domain.ScrollPic;
import com.p2psys.domain.User;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.service.ArticleService;
import com.p2psys.util.DateUtils;
import com.p2psys.util.ImageUtil;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

public class ManageArticleAction extends BaseAction implements  ModelDriven<Article> {
	
	private static final Logger logger=Logger.getLogger(ManageArticleAction.class);
	
	ArticleService articleService;
	Article model = new Article();
	List<Integer> bid;
	List<Integer> hid;
	List<Integer> orders;
	int type;
	File files;
	String filesFileName;
	ScrollPic sp = new ScrollPic();
	File pic;
	private String filePath;
	private String sep = File.separator;

	public String getSep() {
		return sep;
	}
	public void setSep(String sep) {
		this.sep = sep;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public File getPic() {
		return pic;
	}
	public void setPic(File pic) {
		this.pic = pic;
	}
	public ScrollPic getSp() {
		return sp;
	}
	public void setSp(ScrollPic sp) {
		this.sp = sp;
	}
	@Override
	public Article getModel() {
		return model;
	}
	public List<Integer> getBid() {
		return bid;
	}
	public void setBid(List<Integer> bid) {
		this.bid = bid;
	}
	public ArticleService getArticleService() {
		return articleService;
	}
	public List<Integer> getOrders() {
		return orders;
	}
	public void setOrders(List<Integer> orders) {
		this.orders = orders;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public File getFiles() {
		return files;
	}
	public void setFiles(File files) {
		this.files = files;
	}
	public String getFilesFileName() {
		return filesFileName;
	}
	public void setFilesFileName(String filesFileName) {
		this.filesFileName = filesFileName;
	}
	public void setArticleService(ArticleService articleService) {
		this.articleService = articleService;
	}
	
	public String addArticle() throws Exception {
		String actionType=StringUtils.isNull(request.getParameter("actionType"));
		if(!StringUtils.isBlank(actionType)){
			if(model.getSite_id()<1){
				message("请选择栏目!","/admin/article/addArticle.html");
				return ADMINMSG;
			}
			fillArticle(model);
			//处理附件
			String newUrl="";
			if(files!=null){
				boolean rs = checkUpload(filesFileName);
				if(rs){
					message("上传附件格式不正确!","/admin/article/addArticle.html");
					return ADMINMSG;
				}
				String newFileName=generateUploadFilename(filesFileName);
				try {
					newUrl=upload(files, "", "/data/upload", newFileName);
				} catch (Exception e) {
					logger.error("上传文件出错："+e.getMessage());
				}
			}
			model.setLitpic(newUrl);
			articleService.addArticle(model,newUrl);
			message("新增文章成功！","/admin/article/showArticle.html");
			return ADMINMSG;
		}
		return SUCCESS;
	}
	
	public String addRiskReportArticle() throws Exception {
		String actionType=StringUtils.isNull(request.getParameter("actionType"));
		if(!StringUtils.isBlank(actionType)){
			if(model.getSite_id()<1){
				message("请选择栏目!","/admin/article/addArticle.html");
				return ADMINMSG;
			}
			fillArticle(model);
			//处理附件
			String newUrl="";
			if(files!=null){
				boolean rs = checkUpload(filesFileName);
				if(rs){
					message("上传附件格式不正确!","/admin/article/addArticle.html");
					return ADMINMSG;
				}
				String newFileName=generateUploadFilename(filesFileName);
				try {
					newUrl=upload(files, "", "/data/upload", newFileName);
				} catch (Exception e) {
					logger.error("上传文件出错："+e.getMessage());
				}
			}
			model.setLitpic(newUrl);
			articleService.addArticle(model,newUrl);
			message("新增文章成功！","/admin/article/showArticle.html");
			return ADMINMSG;
		}
		return SUCCESS;
	}
	public String addTroubleFundArticle() throws Exception {
		String actionType=StringUtils.isNull(request.getParameter("actionType"));
		if(!StringUtils.isBlank(actionType)){
			if(model.getSite_id()<1){
				message("请选择栏目!","/admin/article/addArticle.html");
				return ADMINMSG;
			}
			fillArticle(model);
			//处理附件
			String newUrl="";
			if(files!=null){
				boolean rs = checkUpload(filesFileName);
				if(rs){
					message("上传附件格式不正确!","/admin/article/addArticle.html");
					return ADMINMSG;
				}
				String newFileName=generateUploadFilename(filesFileName);
				try {
					newUrl=upload(files, "", "/data/upload", newFileName);
				} catch (Exception e) {
					logger.error("上传文件出错："+e.getMessage());
				}
			}
			model.setLitpic(newUrl);
			articleService.addArticle(model,newUrl);
			message("新增文章成功！","/admin/article/showArticle.html");
			return ADMINMSG;
		}
		return SUCCESS;
	}
	private Article fillArticle(Article model){
		// v1.6.5.3 RDPROJECT-159 xx 2013.09.12 start
		User auth_user = (User) session.get(Constant.AUTH_USER);
		model.setAddtime(getTimeStr());
		model.setAddip(getRequestIp());
		model.setUser_id(auth_user.getUser_id());
		// v1.6.5.3 RDPROJECT-159 xx 2013.09.12 end
		return model;
	}
	
	/**
	 *批量处理文章
	 * @return
	 * @throws Exception
	 */
	public String batchOperateArticle() throws Exception {
		logger.debug("Param:"+bid);
		if(bid==null||bid.size()<1){
			message("请选择需要操作的文章！","/admin/article/showArticle.html");
			return ADMINMSG;
		}
		getBatchParams();
		articleService.batchOperateArticle(type, bid, orders);
		message(returnMsg(type),"/admin/article/showArticle.html");
		return ADMINMSG;
	}
	
	private void getBatchParams(){
		List newOrders=new ArrayList();
		for(int i=0;i<bid.size();i++){
			for(int j=0;j<bid.size();j++){
				if(bid.get(j)==bid.get(i)){
					newOrders.add(orders.get(j));
				}
			}
		}
		orders=newOrders;
	}
	
	
	public String showArticle() throws Exception {
		int page=NumberUtils.getInt(request.getParameter("page"));
		String articlename = StringUtils.isNull(request.getParameter("articlename"));
		String site_id = StringUtils.isNull(request.getParameter("site_id"));
		SearchParam param = new SearchParam();
		if(!site_id.isEmpty()&&!"0".equals(site_id)){
			param.setSite_id(site_id);
		}
		param.setArticlename(articlename);
		PageDataList plist = articleService.getArticleList(page,param);
		List<Article> siteTypeList = articleService.getAllSiteType();
		request.setAttribute("siteTypeList", siteTypeList);
		request.setAttribute("site_id", site_id);
		setPageAttribute(plist, param);
		return SUCCESS;
	}
	
	
	// v1.6.5.3 RDPROJECT-159 xx 2013.09.12 start
	public String modifyArticle() throws Exception{
		String actionType = paramString("actionType");
		long id = paramInt("id");
		if(!StringUtils.isBlank(actionType)){
			fillArticle(model);
			//处理附件
			String newFileName=generateUploadFilename(filesFileName);
			// v1.6.7.2 RDPROJECT-532 zza 2013-12-19 start 
			String newUrl="";
			if (!StringUtils.isBlank(filesFileName)) {
				try {
					newUrl=upload(files, "", "/data/upload", newFileName);
				} catch (Exception e) {
					logger.error("上传文件出错："+e.getMessage());
				}
			} else {
				Article article = articleService.getArticle(id);
				newUrl = article.getLitpic();
			}
			// v1.6.7.2 RDPROJECT-532 zza 2013-12-19 end
			model.setLitpic(newUrl);
			articleService.modifyArticle(model,newUrl);
			message("修改文章成功！","/admin/article/showArticle.html");
			return ADMINMSG;
		}
		Article a=articleService.getArticle(id);
		List files=articleService.getArticleFileds(a.getId());
		request.setAttribute("a", a);
		request.setAttribute("files", files);
		return SUCCESS;
	}
	// v1.6.5.3 RDPROJECT-159 xx 2013.09.12 end
	
	public String delArticle() throws Exception {
		long id=NumberUtils.getLong(request.getParameter("id"));
		articleService.delArticle(id);
		message("删除文章成功！","/admin/article/showArticle.html");
		return ADMINMSG;
	}
	
	/**
	 * 查看滚动图片
	 * 
	 * @return 
	 * @throws Exception 异常
	 */
	public String showScrollPic() throws Exception {
		int page = NumberUtils.getInt(StringUtils.isNull(request.getParameter("page")));
		PageDataList plist = articleService.getScrollPicList(EnumPic.ZERO.getValue(), page, new SearchParam());
		setPageAttribute(plist, new SearchParam());
		return SUCCESS;
	}
	
	// v1.6.7.1 RDPROJECT-449 lhm 2013-11-13 start
	/**
	 * 后台添加，删除，更新图片时，静态数据重新加载
	 */
	private void initPic () {
		// 首页滚动图片
		List scrollPic = articleService.getScrollPicList(1, 0, 10);
		context.setAttribute("scrollPic", scrollPic);

		// 合作伙伴
		List cooperativePartnerPic = articleService.getScrollPicList(2, 0, 10);
		context.setAttribute("cooperativePartnerPic", cooperativePartnerPic);

		// 友情链接
		List linksPic = articleService.getScrollPicList(3, 0, 10);
		context.setAttribute("linksPic", linksPic);
	}
	// v1.6.7.1 RDPROJECT-449 lhm 2013-11-13 end

	/**
	 * 滚动图片修改
	 * 
	 * @return forward页面
	 * @throws Exception 异常
	 */
	public String modifyScrollPic() throws Exception {
		String actionType = StringUtils.isNull(request.getParameter("actionType"));
		long id = NumberUtils.getLong(request.getParameter("id"));
		String delPic = StringUtils.isNull(request.getParameter("delPic"));
		if (!StringUtils.isBlank(actionType)) {
			fillScrollPic(sp);
			Boolean isImage = false;
			if (pic != null) {
				isImage = ImageUtil.fileIsImage(pic);
			}
			// v1.6.5.3 RDPROJECT-39  zza 2013-09-12 start
			ScrollPic scrollPic = articleService.getScrollPicListById(id);
			if ("1".equals(delPic)) {
				sp.setPic(null);
			} else if (this.pic == null) {
				sp.setPic(scrollPic.getPic());
			}
			if (sp.getType_id() == 1) {
				if (this.pic != null && !isImage) {
					message("你上传的图片格式不符合要求，请重新上传", "/admin/article/showScrollPic.html");
					return ADMINMSG;
				} else if (this.pic != null) {
					moveFile(sp);
					sp.setPic(filePath);
				}
			} else if (sp.getType_id() == 2 || sp.getType_id() == 3) {
				if (sp.getPic() == null && this.pic == null && "".equals(sp.getName())) {
					message("文字描述或者图片上传必须填写一项！", "/admin/article/showScrollPic.html");
					return ADMINMSG;
				} else if (this.pic != null && !isImage) {
					message("你上传的图片格式不符合要求，请重新上传", "/admin/article/showScrollPic.html");
					return ADMINMSG;
				} else if (this.pic != null) {
					moveFile(sp);
					sp.setPic(filePath);
				}
			}
			// v1.6.5.3 RDPROJECT-39  zza 2013-09-12 end
			articleService.modifyScrollPic(sp);
			
			// v1.6.7.1 RDPROJECT-449 lhm 2013-11-13 start
			initPic();
			// v1.6.7.1 RDPROJECT-449 lhm 2013-11-13 end
			message("修改图片成功！", "/admin/article/showScrollPic.html");
			return ADMINMSG;
		}
		ScrollPic scrollPic = articleService.getScrollPicListById(id);
		request.setAttribute("scrollPic", scrollPic);
		return SUCCESS;
	}

	/**
	 * 滚动图片删除
	 * 
	 * @return forward页面
	 * @throws Exception
	 */
	public String deleteScrollPic() throws Exception {
		long id = NumberUtils.getLong(request.getParameter("id"));
		articleService.delScrollPic(id);
		// v1.6.7.1 RDPROJECT-449 lhm 2013-11-13 start
		initPic();
		// v1.6.7.1 RDPROJECT-449 lhm 2013-11-13 end
		message("删除图片成功！", "/admin/article/showScrollPic.html");
		return ADMINMSG;
	}
	
	/**
	 * 滚动图片添加
	 * 
	 * @return forward页面
	 * @throws Exception 异常
	 */
	public String addScrollPic() throws Exception {
		String actionType = StringUtils.isNull(request.getParameter("actionType"));
		if (!StringUtils.isBlank(actionType)) {
			fillScrollPic(sp);
			logger.info(pic);
			Boolean isImage = ImageUtil.fileIsImage(pic);
			logger.info("isImage:" + isImage);
			// v1.6.5.3 RDPROJECT-39  zza 2013-09-12 start
			if (sp.getType_id() ==1) {
				if (this.pic == null) {
					message("你上传的图片为空", "/admin/article/addScrollPic.html");
					return MSG;
				} else if (isImage == false) {
					message("你上传的图片格式不符合要求，请重新上传", "/admin/article/addScrollPic.html");
					return ADMINMSG;
				} 
				moveFile(sp);
				sp.setPic(filePath);
			} else if (sp.getType_id() ==2 || sp.getType_id() ==3) {
				if (this.pic == null && sp.getName().equals("")) {
					message("你上传的图片为空", "/admin/article/addScrollPic.html");
					return MSG;
				} else if (this.pic != null && isImage == false) {
					message("你上传的图片格式不符合要求，请重新上传", "/admin/article/addScrollPic.html");
					return ADMINMSG;
				} else if (this.pic != null) {
					moveFile(sp);
					sp.setPic(filePath);
				}
			}
			// v1.6.5.3 RDPROJECT-39  zza 2013-09-12 end
			ScrollPic scrollPic = articleService.addScrollPic(sp);
			// v1.6.7.1 RDPROJECT-449 lhm 2013-11-13 start
			initPic();
			// v1.6.7.1 RDPROJECT-449 lhm 2013-11-13 end
			request.setAttribute("scrollPic", scrollPic);
			message("添加图片成功！", "/admin/article/showScrollPic.html");
			return ADMINMSG;
		}
		return SUCCESS;
	}

	private ScrollPic fillScrollPic(ScrollPic sp) {
		sp.setId(NumberUtils.getLong(request.getParameter("id")));
		sp.setSort(NumberUtils.getInt(request.getParameter("sort")));
		sp.setType_id(NumberUtils.getInt(request.getParameter("type_id")));
		sp.setUrl(request.getParameter("url"));
		sp.setPic(request.getParameter("pic"));
		sp.setName(StringUtils.isNull(request.getParameter("name")));
		return sp;
	}

	/**
	 * 截取url
	 * @param old
	 * @param truncat
	 * @return
	 */
	private String truncatUrl(String old, String truncat) {
		String url = "";
		url = old.replace(truncat, "");
		url = url.replace(sep, "/");
		return url;
	}

	/**
	 * 存储图片
	 * @param scrollPic
	 */
	private void moveFile(ScrollPic scrollPic) {
		String dataPath = ServletActionContext.getServletContext().getRealPath("/data");
		String contextPath = ServletActionContext.getServletContext().getRealPath("/");
		Date d1 = new Date();
		String upfiesDir = dataPath + sep + "upfiles" + sep + "images" + sep;
		String destfilename1 = upfiesDir + DateUtils.dateStr2(d1) + sep + scrollPic.getId() + "_scrollPic" + "_"
				+ d1.getTime() + ".jpg";
		filePath = destfilename1;
		filePath = this.truncatUrl(filePath, contextPath);
		logger.info(destfilename1);
		File imageFile1 = null;
		try {
			imageFile1 = new File(destfilename1);
			FileUtils.copyFile(pic, imageFile1);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}

	}
	
	private String returnMsg(int type){
		String msg = "";
		switch (type) {
		case 1:
			msg = "成功执行批量排序";
			break;
		case 2:
			msg = "成功执行批量显示";
			break;
		case 3:
			msg = "成功执行批量隐藏";
			break;
		case 6:
			msg = "成功执行批量删除";
			break;
		default:
			break;
		}
		return msg;
	}
	
	/**
	 *修改文章显示状态
	 * @return
	 * @throws Exception
	 */
	public String modifyArticleStatus(){
		
		long id = NumberUtils.getLong(request.getParameter("id"));
		int status = NumberUtils.getInt(request.getParameter("status"));
		Article article = new Article();
		article.setId(id);
		if (status == 1) {
			article.setStatus(0);
		} 
		if (status == 0) {
			article.setStatus(1);
		}
		articleService.modifyArticleStatus(article);
		this.message("修改状态成功！","/admin/article/showArticle.html");
		return ADMINMSG;
	}
	public List<Integer> getHid() {
		return hid;
	}
	public void setHid(List<Integer> hid) {
		this.hid = hid;
	}
	
	
	
}
