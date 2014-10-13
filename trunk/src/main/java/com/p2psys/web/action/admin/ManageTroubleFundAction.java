package com.p2psys.web.action.admin;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ModelDriven;
import com.p2psys.common.enums.EnumTroubleFund;
import com.p2psys.domain.Article;
import com.p2psys.domain.ScrollPic;
import com.p2psys.domain.TroubleDonateRecord;
import com.p2psys.model.PageDataList;
import com.p2psys.model.SearchParam;
import com.p2psys.service.ArticleService;
import com.p2psys.service.TroubleFundService;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;
import com.p2psys.web.action.BaseAction;

/**
 * 后台急难基金模块
 * 
 
 */
public class ManageTroubleFundAction extends BaseAction implements ModelDriven<Article> {
	/** logger */
	private static final Logger logger = Logger.getLogger(ManageTroubleFundAction.class);
	/** 急难基金捐赠service */
	private TroubleFundService troubleFundService;

	public TroubleFundService getTroubleFundService() {
		return troubleFundService;
	}

	public void setTroubleFundService(TroubleFundService troubleFundService) {
		this.troubleFundService = troubleFundService;
	}

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

	/**
	 * 急难基金文章添加
	 * 
	 * @return 结果集
	 * @throws Exception 异常
	 */
	public String addTroubleFundArticle() throws Exception {
		String actionType = StringUtils.isNull(request.getParameter("actionType"));
		if (!StringUtils.isBlank(actionType)) {
			if (model.getSite_id() < EnumTroubleFund.FIRST.getValue()) {
				message("请选择栏目!", "/admin/article/addArticle.html");
				return ADMINMSG;
			}
			fillArticle(model);
			// 处理附件
			String newUrl = "";
			if (files != null) {
				String newFileName = generateUploadFilename(filesFileName);
				try {
					newUrl = upload(files, "", "/data/upload", newFileName);
				} catch (Exception e) {
					logger.error("上传文件出错：" + e.getMessage());
				}
			}
			model.setLitpic(newUrl);
			articleService.addArticle(model, newUrl);
			message("新增文章成功！", "/admin/article/showArticle.html");
			return ADMINMSG;
		}
		return SUCCESS;
	}

	/**
	 * 急难基金基金收支内容添加以及列表
	 * 
	 * @return 结果集
	 */
	public String troubleFund() {
		int page = NumberUtils.getInt(request.getParameter("page"));
		String actionType = StringUtils.isNull(request.getParameter("type"));
		int id = NumberUtils.getInt(StringUtils.isNull(request.getParameter("id")));
		request.setAttribute("actionType", actionType);
		if (StringUtils.isBlank(actionType)) {//
			if (id == EnumTroubleFund.ZERO.getValue()) {
				return "success";
			}
			TroubleDonateRecord troubleDonateRecord = troubleFundService.getTroubleDonateById(id);
			request.setAttribute("troubleDonateRecord", troubleDonateRecord);
			return "success";
		}
		
		if ("add".equals(actionType)) {
			//基金收支记录添加操作
			TroubleDonateRecord t = getParater();
			if (StringUtils.isBlank(t.getBorrow_use())) {//
				message("用途不能为空", "/admin/troublefund/troubleFund.html?type=add");
				return ADMINMSG;
			}
			if (t.getMoney() == 0.0) {//
				message("金额不能为空", "/admin/troublefund/troubleFund.html?type=add");
				return ADMINMSG;
			}
			troubleFundService.addTroubleDonate(t);
			message("操作成功!", "/admin/troublefund/troubleFund.html?type=list");
			return ADMINMSG;
		}
		if ("update".equals(actionType)) {
			//基金收支记录修改操作
			TroubleDonateRecord t = getParater();
			if (StringUtils.isBlank(t.getBorrow_use())) {//
				message("用途不能为空", "/admin/troublefund/troubleFund.html?type=add");
				return ADMINMSG;
			}
			if (t.getMoney() == 0.0) {//
				message("金额不能为空", "/admin/troublefund/troubleFund.html?type=add");
				return ADMINMSG;
			}
			troubleFundService.updateTroubleDonate(t);
			message("操作成功!", "/admin/troublefund/troubleFund.html?type=list");
			return ADMINMSG;
		}
		if ("list".equals(actionType)) {//
			// 后台手工添加基金收入和支出
			PageDataList plist = troubleFundService.getTroubleDonateList(EnumTroubleFund.SECOND.getValue(), page);// 写成枚举类，不能直接写死
			request.setAttribute("page", plist.getPage());
			request.setAttribute("troubleDonateList", plist.getList());
			request.setAttribute("param", new SearchParam().toMap());

			return "success";
		}
		return "success";

		/*
		 * String actionType=StringUtils.isNull(request.getParameter("type")); String
		 * idString=StringUtils.isNull(request.getParameter("id")); PaymentInterface paymentInterface=new
		 * PaymentInterface(); int id=0; if(!StringUtils.isBlank(idString)){ id=Integer.parseInt(idString); }
		 * if(StringUtils.isBlank(actionType)){ if(id==0){ return "success"; }
		 * paymentInterface=manageCashService.getPayInterface(id); request.setAttribute("paymentInterface",
		 * paymentInterface); return "success"; } if("list".equals(actionType)){ int
		 * page=NumberUtils.getInt(request.getParameter("page")); List
		 * list=manageCashService.getPayInterfaceList(EnumTroubleFund.ZERO.getValue()); request.setAttribute("list",
		 * list); }else{ paymentInterface=getParameter(paymentInterface);
		 * manageCashService.PaymemtInterface(paymentInterface, idString);
		 * message("操作成功！","/admin/cash/payInterface.html?type=list"); return ADMINMSG; }
		 * request.setAttribute("actiontype", actionType); return "success";
		 */

	}
	/**
	 * 急难基金 基金收支实体封装
	 * 
	 * @return TroubleDonateRecord实体对象
	 */
	private TroubleDonateRecord getParater() {
		TroubleDonateRecord t = new TroubleDonateRecord();
		String use = StringUtils.isNull(request.getParameter("borrow_use"));
		String borrow_content = StringUtils.isNull(request.getParameter("borrow_content"));
		String repayment_time = StringUtils.isNull(request.getParameter("repayment_time"));
		String remark = StringUtils.isNull(request.getParameter("remark"));
		double money = NumberUtils.getDouble(StringUtils.isNull(request.getParameter("money")));
		long status = NumberUtils.getLong(StringUtils.isNull(request.getParameter("status")));
		int id = NumberUtils.getInt(StringUtils.isNull(request.getParameter("id")));
		t.setBorrow_use(use);
		t.setMoney(money);
		t.setStatus(status);
		t.setUser_id(EnumTroubleFund.FIRST.getValue());
		t.setBorrow_content(borrow_content);
		t.setRepayment_time(repayment_time);
		t.setRemark(remark);
		t.setId(id);
		return t;
	}

	/**
	 * 文章实体类信息封装
	 * 
	 * @param model
	 * @return 文章实体
	 */
	private Article fillArticle(Article model) {
		model.setAddtime(getTimeStr());
		model.setAddip(getRequestIp());
		model.setUser_id(EnumTroubleFund.FIRST.getValue());
		model.setAddtime(getTimeStr());
		model.setAddip(getRequestIp());
		return model;
	}
}
