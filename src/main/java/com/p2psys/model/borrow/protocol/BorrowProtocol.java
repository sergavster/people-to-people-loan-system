package com.p2psys.model.borrow.protocol;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.WritableDirectElement;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.tool.xml.ElementHandler;
import com.itextpdf.tool.xml.Writable;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.pipeline.WritableElement;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.dao.TenderDao;
import com.p2psys.domain.Borrow;
import com.p2psys.domain.Collection;
import com.p2psys.domain.Site;
import com.p2psys.domain.Tender;
import com.p2psys.domain.User;
import com.p2psys.domain.Userinfo;
import com.p2psys.exception.BorrowException;
import com.p2psys.model.BorrowTender;
import com.p2psys.model.ProtocolBorrowTender;
import com.p2psys.model.borrow.BorrowHelper;
import com.p2psys.model.borrow.BorrowModel;
import com.p2psys.service.ArticleService;
import com.p2psys.service.BorrowService;
import com.p2psys.service.UserService;
import com.p2psys.service.UserinfoService;
import com.p2psys.tool.itext.PdfHelper;
import com.p2psys.util.DateUtils;
import com.p2psys.util.FreemarkerUtil;
import com.p2psys.util.MoneyUtils;
import com.p2psys.util.NumberUtils;
import com.p2psys.web.action.BorrowAction;

import freemarker.template.TemplateException;
public class BorrowProtocol {
	private static Logger logger = Logger.getLogger(BorrowAction.class);
	private String siteName;
	private long borrow_id;
	private long tender_id;
	private BorrowService borrowService;
	private UserService userService;
	private ArticleService articleService;
	private String pdfName;
	private String inPdfName;
	private String outPdfName;
	private String downloadFileName;
	private String imageFileName;
	private ServletContext context;
	private BorrowModel borrow;
	private Tender tender;
	private User tenderUser;
	private User borrowUser;
	private PdfHelper pdf;
	private int borrowType;//对应标种的编码 默认为100，表示区分标种；
	private int templateType;//模板的类型：1表示读协议栏目，默认为1； 2表示文件模板
	private TemplateReader templateReader;
	private Map<String,Object> data=new HashMap<String,Object>();
	
	public BorrowProtocol(User user, long borrow_id, long tender_id,int borrowType,int templateType) {
		super();
		init(user, borrow_id, tender_id,borrowType,templateType);
	}
	
	public BorrowProtocol(User user, long borrow_id, long tender_id) {
		super();
		init(user, borrow_id, tender_id,100,1);
	}

	public String findServerPath(){
		String path=this.getClass().getResource("/").getPath();
		path=path.replaceAll("/WEB-INF/classes/", "/");
		return path;
	}
	
	private void init(User tenderUser, long borrow_id, long tender_id,int borrowType,int templateType) {
		this.borrow_id = borrow_id;
		this.tender_id = tender_id;
		this.tenderUser = tenderUser;
		String contextPath = findServerPath();
		WebApplicationContext ctx=ContextLoader.getCurrentWebApplicationContext();
		borrowService = (BorrowService) ctx.getBean("borrowService");
		userService = (UserService) ctx.getBean("userService");
		articleService = (ArticleService) ctx.getBean("articleService");
		TenderDao tenderDao=(TenderDao) ctx.getBean("tenderDao");
		borrow = borrowService.getBorrow(borrow_id);
		tender=tenderDao.getTenderById(tender_id);
		this.borrowUser=userService.getUserById(borrow.getUser_id());
		downloadFileName = borrow.getId() + ".pdf";
		String timeStr=DateUtils.dateStr(new Date(),"yyyyMMdd");
//		inPdfName = contextPath + "data/protocol/" + borrow_id + "_"+ tender_id+"_"+timeStr + ".pdf";
		// v1.6.6.1 RDPROJECT-279 zza 2013-10-09 start
		inPdfName = contextPath + "data/protocol/" + borrow_id + "_"+ tender_id + ".pdf";
		// v1.6.6.1 RDPROJECT-279 zza 2013-10-09 end
		imageFileName = "" + Global.getValue("weburl")+ Global.getValue("theme_dir") + "/images/zhang.jpg";
//		outPdfName = contextPath + "data/protocol/" + borrow_id + "_"+ tender_id +"_"+timeStr+ ".pdf";
		// v1.6.6.1 RDPROJECT-279 zza 2013-10-09 start
		outPdfName = contextPath + "data/protocol/" + borrow_id + "_"+ tender_id + ".pdf";
		// v1.6.6.1 RDPROJECT-279 zza 2013-10-09 end
		pdf = PdfHelper.instance(inPdfName);
		initBorrowType();
		this.templateType=2;
	}
	
	protected void initBorrowType(){
		borrowType=100;
	}
	
	protected Map<String,Object> fillProtoclData(){
		WebApplicationContext ctx=ContextLoader.getCurrentWebApplicationContext();
		UserinfoService userinfoService = (UserinfoService) ctx.getBean("userinfoService");
		Userinfo borrowUserinfo=userinfoService.getUserinfoByUserid(getBorrowUser().getUser_id());
		Userinfo tenderUserinfo=userinfoService.getUserinfoByUserid(getTenderUser().getUser_id());
		data.put("webname", Global.getString("webname"));
		data.put("borrowProtocolTime", DateUtils.dateStr2(BorrowHelper.getBorrowProtocolTime(borrow)));
		data.put("ZSTime", DateUtils.dateStr7(BorrowHelper.getBorrowProtocolTime(borrow)));
		data.put("borrow", borrow);
		borrowUser.hideChar();
		data.put("borrowUser", borrowUser);
		data.put("tenderUser", tenderUser);
		data.put("tender", tender);
		data.put("borrowUserinfo", borrowUserinfo);
		data.put("tenderUserinfo", tenderUserinfo);
		data.put("borrowNO", getBorrow().getId()+"");
		Calendar cal=Calendar.getInstance();
		Date date=BorrowHelper.getBorrowRepayTime(getBorrow(), getTender());
		cal.setTime(date);
		data.put("payment",cal.get(Calendar.DAY_OF_MONTH));
		data.put("verifyTime", DateUtils.dateStr6(BorrowHelper.getBorrowProtocolTime(borrow)));
		data.put("repay_time", DateUtils.dateStr6(date));
		data.put("verify_time", DateUtils.dateStr6(BorrowHelper.getBorrowVerifyTime(getBorrow(), getTender())));
		data.put("roundAccount", NumberUtils.format2(borrow.getAccount()));
		cal=null;
		cal=Calendar.getInstance();
		data.put("nowYear", cal.get(Calendar.YEAR));
		data.put("nowMonth", cal.get(Calendar.MONDAY)+1);
		data.put("nowDay", cal.get(Calendar.DAY_OF_MONTH));
		String stamp = findServerPath() + "data/images/stamp/stamp.jpg";
		data.put("stamp",stamp);
		List<BorrowTender> list  = borrowService.getTenderList(borrow.getId());
		data.put("list", list);
		//V1.6.7.1 RDPROJECT-394 yl 2013-11-11 start
		//投资人的收款计划
		List<Collection> cList = borrowService.getCollectionByTender(tender_id);
		data.put("cList", cList);
		BorrowModel wrapBorrrow =BorrowHelper.getHelper(borrow.getType(),borrow);
		//借款管理费
		double manage_fee = wrapBorrrow.getManageFee();
		data.put("manage_fee", manage_fee);
		//借款金额转换为大写
		data.put("tenderMoney", MoneyUtils.convert(tender.getAccount()));
		//V1.6.7.1 RDPROJECT-394 yl 2013-11-11 end
//		int is_flow = borrow.getIs_flow();
		// master合并develop v1.6.6.1 2013-09-22 xx start develop 
		/*if(is_flow == 1){
			List<Collection> repFlow = borrowService.getFlowCollection(getTender_id());
			b.append("到期需偿还本息数额").append("&nbsp;&nbsp;").append("￥");
			b.append("<span style='color:red'>");
			b.append(NumberUtils.format2(NumberUtils.getDouble(repFlow.get(0).getRepay_account())));
			b.append("</span>").append("元");
			data.put("text", b.toString());
		}else{
			List<Repayment> repList = borrowService.getRepaymentByBorrowId(borrow.getId());
			if("2".equals(style)){
				b.append("到期需偿还本息数额").append("&nbsp;&nbsp;").append("￥");
				b.append("<span style='color:red'>");
				b.append(NumberUtils.format2(NumberUtils.getDouble(repList.get(0).getRepayment_account())));
				b.append("</span>").append("元");
				data.put("text", b.toString());
			}else{
				b.append("每月偿还本息数额").append("&nbsp;&nbsp;").append("￥");
				b.append("<span style='color:red'>");
				b.append(NumberUtils.format2(NumberUtils.getDouble(repList.get(0).getRepayment_account())));
				b.append("</span>").append("元").append("<br/>");
				b.append("(因计算中存在四舍五入，最后一期还款金额与之前略有不同，为￥");
				b.append("<span style='color:red'>");
				b.append(NumberUtils.format2(NumberUtils.getDouble(repList.get(repList.size()-1).getRepayment_account())));
				b.append("</span>").append("元)");
				data.put("text", b.toString());
			}
		}*/
		// master合并develop v1.6.6.1 2013-09-22 xx end develop
		// master合并develop v1.6.6.1 2013-09-22 xx start master 
		//下面这3个参数没定义，页面报错
		/*data.put("borrowmanage", "xxx");
		data.put("lastborrowmanage", "xxx");
		data.put("tenderTable", "xxx");*/
		
		List<Collection> repList = borrowService.getFlowCollection(getTender_id());//borrow.getId()
		if(borrow.getType() == Constant.TYPE_FLOW ){
			data.put("fullVerify_time",DateUtils.dateStr6(DateUtils.getDate(tender.getAddtime())));
			//data.put("repayList", repList);
		}else{
			String full_verifytime = borrow.getFull_verifytime();
			if(full_verifytime == null){
				data.put("fullVerify_time",  DateUtils.dateStr6(DateUtils.getDate(borrow.getVerify_time())));
			}else{
				data.put("fullVerify_time", DateUtils.dateStr6(DateUtils.getDate(borrow.getFull_verifytime())));
			}
			//List<Repayment> repayList = borrowService.getRepaymentByBorrowId(borrow.getId());
			//data.put("repayList", repayList);
		}
		//V1.6.7.1 RDPROJECT-394 yl 2013-11-11 start
		data.put("repayList", repList);
		//V1.6.7.1 RDPROJECT-394 yl 2013-11-11 end
		data.put("firstMonthAccount", NumberUtils.format2(NumberUtils.getDouble(repList.get(0).getRepay_account())));
		data.put("lastMonthAccount", NumberUtils.format2(NumberUtils.getDouble(repList.get(repList.size()-1).getRepay_account())));
		
		// 爱贷协议自处理 协议只针对流转标   v1.6.6.2 RDPROJECT-82 2013-10-28 gc start
		List<ProtocolBorrowTender> pList = borrowService.getProtocolTenderListByBorrowid(tender.getId());
		// 爱贷协议自处理 协议只针对流转标   v1.6.6.2 RDPROJECT-82 2013-10-28 gc end
		
		data.put("pList", pList);
		// master合并develop v1.6.6.1 2013-09-22 xx end master 
		return data;
	}

	public static BorrowProtocol getInstance(User user, long borrow_id,
			long tender_id,int borrowType) {
		BorrowProtocol p = null;
		//String webid = Global.getString("webid");
		p = new BaseBorrowProtocol(user, borrow_id, tender_id,borrowType,1);
		return p;
	}

	public int createPdf() throws Exception {
		int size = 0;
		if (borrow == null) throw new BorrowException("该借款标不存在！");
		getTemplate();//获得模板并渲染数据
		pdf.exportPdf();
		return size;
	}

	protected void addPdfTable(PdfHelper pdf, Borrow b)
			throws DocumentException {
		List<BorrowTender> list = borrowService.getTenderList(borrow.getId());
		List cellList = null;
		List[] args = new List[list.size() + 1];
		// 出借人(id)
		cellList = new ArrayList();
		cellList.add("出借人(id)");
		cellList.add("借款金额");
		cellList.add("借款期限");
		cellList.add("年利率");
		cellList.add("借款开始日");
		cellList.add("借款到期日");
		cellList.add("截止还款日");
		cellList.add("还款本息");
		args[0] = cellList;
		for (int i = 1; i < list.size() + 1; i++) {
			BorrowTender t = list.get(i - 1);
			cellList = new ArrayList();
			cellList.add(t.getUsername());
			cellList.add(t.getAccount() + "");
			cellList.add(b.getTime_limit());
			cellList.add(b.getApr() + "");
			cellList.add(DateUtils.dateStr2(BorrowHelper.getBorrowVerifyTime(b,
					t)));
			Date d = BorrowHelper.getBorrowRepayTime(b, t);
			cellList.add(DateUtils.dateStr2(d));
			cellList.add("每月截止" + DateUtils.getDay(d) + "日");
			cellList.add(t.getRepayment_account());
			args[i] = cellList;
		}
		pdf.addTable(args, 80, 7);
	}

	
	public void getTemplate() throws IOException, TemplateException,
			DocumentException {
//		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.PROTOCOL_TEMPLATE_TYPE.getValue()));
//		if(rule!=null){
//			
//		}
		if (getTemplateType() == 1) {
			Site site = articleService.getSiteByNid("protocol");
			if (site == null || site.getContent() == null)
				throw new BorrowException("协议模板有误！");
			String out = FreemarkerUtil.renderTemplate(site.getContent(), data);
			templateHtml(out);
		} else {
			String file = this.getBorrowType() + ".html";
			String out = FreemarkerUtil.renderFileTemplate(file, this.fillProtoclData());
			templateHtml(out);
		}
	}
	@Deprecated
	protected void readTemplate() throws IOException{
		if(getTemplateType()==1){
			Site site=articleService.getSiteById(32);
			if(site==null||site.getContent()==null) throw new BorrowException("协议模板有误！");
			BufferedReader in;
			in = new BufferedReader(new StringReader(site.getContent()));
			templateReader=new TemplateReader(in);
		}else{
//			String file=context.getRealPath("/")+"/WEB-INF/classes/"+this.getBorrowType()+".html";
			String file=findServerPath()+"/WEB-INF/classes/"+this.getBorrowType()+".html";
			templateReader=new TemplateReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
			//templateReader=new TemplateReader(new FileReader (context.getRealPath("/")+"/WEB-INF/classes/"+this.getBorrowType()+".html"));
		}
	}
	@Deprecated
	protected void doTemplate() throws IOException, DocumentException{
		while (templateReader.read()!=-1){}
		templateReader.close();
		List<String> elements=templateReader.getLines();
		ProtocolValue pv=new ProtocolValue();
		pv.setData(fillProtoclData());
		StringBuffer sb=new StringBuffer();
		for(String e:elements){
			if(e!=null&&e.startsWith("${")&&e.endsWith("}")){
				e=e.substring(2, e.length()-1);
				if(e.equals("tenderTable")){
					templateHtml(sb.toString());
					sb.setLength(0);
					addPdfTable(pdf, borrow);
				}else{
					sb.append(pv.printProtocol(e));	
				}
			}else{
				sb.append(e);
			}
		}
		templateHtml(sb.toString());
	}
	
	protected String templateHtml(String str) throws IOException, DocumentException{
		final List<Element> pdfeleList = new ArrayList<Element>();
        ElementHandler elemH = new ElementHandler() {
            public void add(final Writable w) {
                if (w instanceof WritableElement) {
                    pdfeleList.addAll(((WritableElement) w).elements());
                }
            }
        };
        InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(str.getBytes("UTF-8")), "UTF-8");
        XMLWorkerHelper.getInstance().parseXHtml(elemH, isr);
        List<Element> list = new ArrayList<Element>();
        for (Element ele : pdfeleList) {
            if (ele instanceof LineSeparator
                    || ele instanceof WritableDirectElement) {
                continue;
            }
            list.add(ele);
        }
        pdf.addHtmlList(list);
		return "";
	}
	
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public long getBorrow_id() {
		return borrow_id;
	}
	public void setBorrow_id(long borrow_id) {
		this.borrow_id = borrow_id;
	}
	public long getTender_id() {
		return tender_id;
	}
	public void setTender_id(long tender_id) {
		this.tender_id = tender_id;
	}
	public BorrowService getBorrowService() {
		return borrowService;
	}
	public void setBorrowService(BorrowService borrowService) {
		this.borrowService = borrowService;
	}
	public UserService getUserService() {
		return userService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public ArticleService getArticleService() {
		return articleService;
	}
	public void setArticleService(ArticleService articleService) {
		this.articleService = articleService;
	}
	public String getPdfName() {
		return pdfName;
	}
	public void setPdfName(String pdfName) {
		this.pdfName = pdfName;
	}
	public String getInPdfName() {
		return inPdfName;
	}
	public void setInPdfName(String inPdfName) {
		this.inPdfName = inPdfName;
	}
	public String getOutPdfName() {
		return outPdfName;
	}
	public void setOutPdfName(String outPdfName) {
		this.outPdfName = outPdfName;
	}
	public String getDownloadFileName() {
		return downloadFileName;
	}
	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}
	public String getImageFileName() {
		return imageFileName;
	}
	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}
	public ServletContext getContext() {
		return context;
	}
	public void setContext(ServletContext context) {
		this.context = context;
	}
	public BorrowModel getBorrow() {
		return borrow;
	}
	public void setBorrow(BorrowModel borrow) {
		this.borrow = borrow;
	}
	public User getTenderUser() {
		return tenderUser;
	}
	public void setTenderUser(User tenderUser) {
		this.tenderUser = tenderUser;
	}
	public User getBorrowUser() {
		return borrowUser;
	}
	public void setBorrowUser(User borrowUser) {
		this.borrowUser = borrowUser;
	}
	public PdfHelper getPdf() {
		return pdf;
	}
	public void setPdf(PdfHelper pdf) {
		this.pdf = pdf;
	}
	public Tender getTender() {
		return tender;
	}
	public void setTender(Tender tender) {
		this.tender = tender;
	}
	public int getBorrowType() {
		return borrowType;
	}
	public void setBorrowType(int borrowType) {
		this.borrowType = borrowType;
	}
	public int getTemplateType() {
		return templateType;
	}
	public void setTemplateType(int templateType) {
		this.templateType = templateType;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
