package com.p2psys.model.borrow.protocol;

import java.io.IOException;
import java.net.MalformedURLException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.p2psys.context.Global;
import com.p2psys.domain.Borrow;
import com.p2psys.domain.Site;
import com.p2psys.domain.User;
import com.p2psys.tool.itext.PdfHelper;

/**
 * 未经授权不得进行修改、复制、出售及商业使用。
 *  (c)</b> 杭州科技有限公司-<br/>
 * @ClassName: XdcfBorrowProtocol
 * @Description: 信达财富协议生产类
 
 * @date 2013-4-3 上午11:05:12
 */
public class XdcfBorrowProtocol extends BorrowProtocol {

	public XdcfBorrowProtocol(User user, long borrow_id, long tender_id) {
		super(user, borrow_id, tender_id);
	}
	
	protected int addPdfContent(PdfHelper pdf,Borrow b) throws DocumentException{
		int size=0;
		Site site=getArticleService().getSiteById(32);
		String content=site.getContent();
		pdf.addText(content);
		try {
			Image image = Image.getInstance(""+Global.getValue("weburl")+Global.getValue("theme_dir")+"/images/zhang.jpg");
			pdf.addImage(image);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		size=pdf.getPageNumber();
		return size;
	}
	
}
