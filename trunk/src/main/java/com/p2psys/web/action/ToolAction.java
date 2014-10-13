package com.p2psys.web.action;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import ueditor.Uploader;

import com.alibaba.fastjson.JSON;
import com.p2psys.context.Constant;
import com.p2psys.domain.User;
import com.p2psys.tool.interest.EndInterestCalculator;
import com.p2psys.tool.interest.InterestCalculator;
import com.p2psys.tool.interest.MonthEqualCalculator;
import com.p2psys.tool.interest.MonthInterest;
import com.p2psys.tool.interest.MonthInterestCalculator;
import com.p2psys.util.ImageUtil;
import com.p2psys.util.NumberUtils;
import com.p2psys.util.StringUtils;

/**
 * 工具类Action,验证码、生产图片
 
 *
 */
public class ToolAction extends  BaseAction {
	private static Logger logger = Logger.getLogger(ToolAction.class);  
	
	//imgurl action params
	private String userid;
	private String size;
	
	//interest action params
	private double account;
	private double lilv;
	private int times;
	private int time_limit_day;
	private String type;
	
	private File upload;
	private File localUrl;
	private String uploadFileName;
	private String sep=File.separator;
	
	//裁剪后的图像大小
	private double cropX;
	private double cropY;
	private double cropW;
	private double cropH;
	
	private String plugintype;
	//投标奖励比例
	private double tender_award_percentage;
	//续投/线下奖励比例
	private double offline_award_percentage;
	//利息管理费比例
	private double manage_fee_percentage;
	//是否月标
	private int is_month;
	//投标时间
	private String tender_time;
	//是否年利率
	private int is_APR;
	//
	/**
	 * 动态输出图像Action
	 * @return
	 * @throws Exception
	 */
	public String imgurl() throws Exception {
		 if(NumberUtils.getInt(userid)<1){
			 userid=NumberUtils.getInt(userid)+"";
		 }
		 userid=(userid==null||userid.equals(""))?"":userid;
		 size=(size==null||size.equals(""))?"":size;
		 String[] sizes={"big","middle","small"};
		 List<String> sizelist= Arrays.asList(sizes);
		 size=sizelist.contains(size)?size:"big";
		 
		 String url="data"+sep+"avatar"+sep+userid+"_avatar_"+size+".jpg";
		 String contextPath=ServletActionContext.getServletContext().getRealPath("/");
		 url=contextPath+url;
		 File avatarFile=new File(url); 
		 if(!avatarFile.exists()){
			 url=contextPath+"data"+sep+"images"+sep+"avatar"+sep+"noavatar_"+size+".gif";
		 }
		 logger.debug("IMG_URL:"+url);
		 cteateImg(url);
		return null;
    }
	
	public String editorUploadImg() throws Exception {
		String newFileName=generateUploadFilename();
		String retMsg="";
		if(upload==null){
			retMsg=getRetMsg(1, "上传图片失败！","");
		}
		if(retMsg.isEmpty()){
			String imgUrl=upload(upload, uploadFileName, "/data/upload", newFileName+".jpg");
			retMsg=getRetMsg(0, "上传图片成功！",imgUrl);
		}
		
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();  
		out.print(retMsg);
        out.flush();   
        out.close();
		return null;
	}
	
	public String ueditorUploadImg() throws Exception {
		Uploader up = new Uploader(request);
	    up.setSavePath("/data/upfiles");
	    String[] fileType = {".gif" , ".png" , ".jpg" , ".jpeg" , ".bmp"};
	    up.setAllowFiles(fileType);
	    up.setMaxSize(10000); //单位KB
	    up.upload();
	    String ret="{'original':'"+up.getOriginalName()+"','url':'"+up.getUrl()+"','title':'"+up.getTitle()+"','state':'"+up.getState()+"'}";
	    logger.info(ret);
	    response.getWriter().print(ret);
		return null;
	}
	
	private String getRetMsg(int flag,String msg,String imgUrl){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("error", flag);
		map.put("message", msg);
		map.put("url", request.getContextPath()+imgUrl);
		return JSON.toJSONString(map);
	}
	
	public String upload() throws Exception {
		Map map=ServletActionContext.getContext().getSession();
		User user=(User)map.get(Constant.SESSION_USER);
		logger.info("文件："+this.upload);
		logger.info("文件名："+uploadFileName);
		Date d=new Date();
		String destfilename2=ServletActionContext.getServletContext().getRealPath( "/data" ) +sep+"temp"+sep+user.getUser_id()+".jpg";
		logger.info(destfilename2);
		File imageFile2=null;
		try {
			imageFile2 = new File(destfilename2);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		// v1.6.7.1 RDPROJECT-441 xx 2013-11-11 start
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();  
		String retMsg="";
		if (!ImageUtil.fileIsImage(upload)) {
			retMsg=getRetMsg(1, "您上传的图片无效，请重新上传！","");
			out.print(retMsg);
			out.flush();
		    out.close();
			return null;
		}
		FileUtils.copyFile(upload, imageFile2);
		// v1.6.7.1 RDPROJECT-441 xx 2013-11-11 end
	
		//保存上传的临时图片名称
		String src=request.getContextPath()+"/data/temp/"+user.getUser_id()+".jpg";
		BufferedImage srcImage =null;
		BufferedImage destImage =null;
		File file=new File(destfilename2);
		int newWi=0;
		int newHi=0;
		try {
			srcImage=ImageIO.read(file);
			int w=srcImage.getWidth();
			int h=srcImage.getHeight();
			int minW=(w>h)?w:h;
			double newWd=(300.0/minW)*w;
			double newHd=(300.0/minW)*h;
			newWi=(int)newWd; newHi=(int)newHd;
			destImage = new BufferedImage(newWi,newHi, BufferedImage.TYPE_3BYTE_BGR);   
			destImage.getGraphics().drawImage(srcImage.getScaledInstance(newWi, newHi, Image.SCALE_SMOOTH), 0, 0, null);
			ImageIO.write(destImage, "jpg", new File(destfilename2));
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("Print img:"+src);
		out.print("{");
		out.print("error: '',\n");
		out.print("msg: '"+src+ "',\n");
		out.print("width: "+newWi+ ",\n");
		out.print("height: "+newHi+ "\n");
		out.print("}");
        out.flush();   
        out.close();
		return null;
   }

	//生成需要裁剪的照片
	public String cropimg() throws Exception {
		try {
			Map map=ServletActionContext.getContext().getSession();
			User user=(User)map.get(Constant.SESSION_USER);
			String destfilename=ServletActionContext.getServletContext().getRealPath( "/data" ) +sep+"temp"+sep+user.getUser_id()+".jpg";
			logger.info(destfilename);
			this.cteateImg(destfilename);
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	//保存头像
	public String saveAvatar() throws Exception {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("application/json;charset=UTF-8");
			int x=0,y=0,w=0,h=0;
			if(cropX>=0){x=(int)cropX;}
			if(cropY>=0){y=(int)cropY;}
			if(cropW>=0){w=(int)cropW;}
			if(cropH>=0){h=(int)cropH;}
			
			logger.debug("X="+x+",Y="+y+",W="+w+",H="+h);
			@SuppressWarnings("unused")
			boolean re=operateImg(x,y,w,h);
			PrintWriter out = response.getWriter();  
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("flag", 1);
			map.put("msg", "success");
			map.put("useravatar", "");
			out.print(JSON.toJSON(map));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  null;
	}
	
	private boolean operateImg(int x,int y,int w,int h) throws IOException{
		Map map=ServletActionContext.getContext().getSession();
		User user=(User)map.get(Constant.SESSION_USER);
		String dataPath=ServletActionContext.getServletContext().getRealPath("/data");
		String avatarDir=dataPath+sep+"avatar";
		String dest=avatarDir+sep+user.getUser_id()+"_avatar_middle.jpg";
		String smalldest=avatarDir+sep+user.getUser_id()+"_avatar_small.jpg";
		String src=ServletActionContext.getServletContext().getRealPath( "/data" ) +sep+"temp"+sep+user.getUser_id()+".jpg";
		BufferedImage srcImage =null;
		try {
			srcImage=ImageIO.read(new File(src));
			BufferedImage destImage=srcImage.getSubimage(x,y,w,h);
			BufferedImage lastImage=new BufferedImage(w, h,
	                BufferedImage.TYPE_3BYTE_BGR);
			lastImage.getGraphics().drawImage(destImage, 0, 0, null);  
			BufferedImage compressImage = new BufferedImage(98, 98, BufferedImage.TYPE_3BYTE_BGR);  
			BufferedImage compressImage48 = new BufferedImage(48, 48, BufferedImage.TYPE_3BYTE_BGR);   
			compressImage.getGraphics().drawImage(lastImage.getScaledInstance(98, 98, Image.SCALE_SMOOTH), 0, 0, null); 
			compressImage48.getGraphics().drawImage(lastImage.getScaledInstance(48, 48, Image.SCALE_SMOOTH), 0, 0, null);
			File avatarDirFile=new File(avatarDir);
			if(!avatarDirFile.exists()){
				avatarDirFile.mkdir();
			}
			File newFile=new File(dest);
			File smallNewFile=new File(smalldest);
			logger.info("Avatar dest:"+dest);
			ImageIO.write(compressImage, "jpg", newFile);
			ImageIO.write(compressImage48, "jpg", smallNewFile);
			FileUtils.forceDelete(new File(src));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		} 
		 return true;
	}
	
	/**
	 * 以图片流形式输出
	 * @param url
	 * @throws IOException
	 */
	private void cteateImg(String url) throws IOException{
		HttpServletResponse res=ServletActionContext.getResponse();
		res.setHeader("Pragma","No-cache");  
		res.setHeader("Cache-Control","no-cache");  
		res.setDateHeader("Expires", 0);  
		OutputStream output = res.getOutputStream();// 得到输出流   
		if(url.toLowerCase().endsWith(".jpg")){
			 //表明生成的响应是图片  
			res.setContentType("image/jpeg");  
		}else if(url.toLowerCase().endsWith(".gif")){
			res.setContentType("image/gif");  
		}
		InputStream imageIn = new FileInputStream(new File(url));  
        BufferedInputStream bis = new BufferedInputStream(imageIn);// 输入缓冲流  
        BufferedOutputStream bos = new BufferedOutputStream(output);// 输出缓冲流  
        byte data[] = new byte[4096];// 缓冲字节数  
        int size = 0;  
        size = bis.read(data);  
        while (size != -1) {  
            bos.write(data, 0, size);  
            size = bis.read(data);  
        }  
        bis.close();
        bos.flush();// 清空输出缓冲流  
        bos.close();  
        output.flush();
        output.close();
	}
	
	/**
	 * 计算利息的Action
	 * @return
	 * @throws Exception
	 */
	public String interest() throws Exception {
		String toolType=StringUtils.isNull(type);
		MonthInterest monthInterest=new MonthInterest();
		monthInterest.setManage_fee_percentage(manage_fee_percentage);
		monthInterest.setTender_time(tender_time);
		monthInterest.setTender_award_percentage(tender_award_percentage);
		monthInterest.setOffline_award_percentage(offline_award_percentage);
		monthInterest.setIs_APR(is_APR);
		monthInterest.setIs_month(is_month);
		if(toolType.equals("monthEqual")&&is_month==1){
			InterestCalculator ic =new MonthEqualCalculator(account,lilv/100,times,monthInterest);
			//循环每期的利息
			ic.invest_each_month();
		    request.setAttribute("ic", ic);
		}else if(toolType.equals("monthEqual")&&is_month==0){
	        InterestCalculator ic =new MonthEqualCalculator(account,lilv/100,times,monthInterest);
			ic.invest_each_day();
			request.setAttribute("ic", ic);
			
		}else if(toolType.equals("monthInterest")&&is_month==1){
			InterestCalculator ic =new MonthInterestCalculator(account,lilv/100,times,monthInterest);
			//循环每期的利息
			ic.invest_each_month();
		    request.setAttribute("ic", ic);
		}else if(toolType.equals("monthInterest")&&is_month==0){
		    InterestCalculator ic =new MonthInterestCalculator(account,lilv/100,times,monthInterest);
            //循环每期的利息
            ic.invest_each_day();
            request.setAttribute("ic", ic);
		}
		else if (toolType.equals("monthEndInterest")&&is_month==1) {
			InterestCalculator ic =new EndInterestCalculator(account,lilv/100,times,monthInterest);
			//循环每期的利息
			ic.invest_each_month();
			request.setAttribute("ic", ic);
		}else if (toolType.equals("monthEndInterest")&&is_month==0) {
			InterestCalculator ic =new EndInterestCalculator(account,lilv/100,times,monthInterest);
			//循环每期的利息
			ic.invest_each_day();
			request.setAttribute("ic", ic);
		}else if(toolType.equals("phone")){
		}else if(toolType.equals("ip")){
			String area=getAreaByIp(paramString("ip"));
			request.setAttribute("ip", area);
		}
		//投标奖励
        double tender_award=tender_award_percentage*account;
        request.setAttribute("tender_award", tender_award);
        //续投/线下奖励
        double offline_award=account*offline_award_percentage;
        request.setAttribute("offline_award", offline_award);
		request.setAttribute("type", toolType);
		return SUCCESS;
	}
	/**
	 * 显示插件Action
	 * @return
	 * @throws Exception
	 */
	public String plugin() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		return null;
	}
	/**
	 * 输出验证码
	 * @return
	 * @throws Exception
	 */
	public String validimg() throws Exception {
		genernateCaptchaImage();
		return null;
	}
	
	public String actionNotFound() throws Exception{
		return SUCCESS;
	}
	
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public double getAccount() {
		return account;
	}
	public void setAccount(double account) {
		this.account = account;
	}
	public double getLilv() {
		return lilv;
	}
	public void setLilv(double lilv) {
		this.lilv = lilv;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	
	public int getTime_limit_day() {
		return time_limit_day;
	}
	public void setTime_limit_day(int time_limit_day) {
		this.time_limit_day = time_limit_day;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public File getUpload() {
		return upload;
	}
	public void setUpload(File upload) {
		this.upload = upload;
	}
	public String getUploadFileName() {
		return uploadFileName;
	}
	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
	public double getCropX() {
		return cropX;
	}
	public void setCropX(double cropX) {
		this.cropX = cropX;
	}
	public double getCropY() {
		return cropY;
	}
	public void setCropY(double cropY) {
		this.cropY = cropY;
	}
	public double getCropW() {
		return cropW;
	}
	public void setCropW(double cropW) {
		this.cropW = cropW;
	}
	public double getCropH() {
		return cropH;
	}
	public void setCropH(double cropH) {
		this.cropH = cropH;
	}
	public String getPlugintype() {
		return plugintype;
	}
	public void setPlugintype(String plugintype) {
		this.plugintype = plugintype;
	}

	public File getLocalUrl() {
		return localUrl;
	}

	public void setLocalUrl(File localUrl) {
		this.localUrl = localUrl;
	}


	public int getIs_month() {
        return is_month;
    }

    public void setIs_month(int is_month) {
        this.is_month = is_month;
    }

    public double getTender_award_percentage() {
		return tender_award_percentage;
	}

	public void setTender_award_percentage(double tender_award_percentage) {
		this.tender_award_percentage = tender_award_percentage;
	}

	public double getOffline_award_percentage() {
		return offline_award_percentage;
	}

	public void setOffline_award_percentage(double offline_award_percentage) {
		this.offline_award_percentage = offline_award_percentage;
	}

	public double getManage_fee_percentage() {
		return manage_fee_percentage;
	}

	public void setManage_fee_percentage(double manage_fee_percentage) {
		this.manage_fee_percentage = manage_fee_percentage;
	}

	public String getTender_time() {
		return tender_time;
	}

	public void setTender_time(String tender_time) {
		this.tender_time = tender_time;
	}

    public int getIs_APR() {
        return is_APR;
    }

    public void setIs_APR(int is_APR) {
        this.is_APR = is_APR;
    }




	
}