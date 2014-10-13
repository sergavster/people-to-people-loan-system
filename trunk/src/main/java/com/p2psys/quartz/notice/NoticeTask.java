package com.p2psys.quartz.notice;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.p2psys.domain.Notice;
import com.p2psys.service.NoticeService;

public class NoticeTask extends AbstractLoanTask {
	private Logger logger=Logger.getLogger(NoticeTask.class);

	@Autowired
	private NoticeService noticeService;

	public NoticeService getNoticeService() {
		return noticeService;
	}

	public void setNoticeService(NoticeService noticeService) {
		this.noticeService = noticeService;
	}

	public NoticeTask(NoticeService noticeService) {
		super();
		task.setName("NoticeTask");
		this.noticeService=noticeService;
	}
	
	@Override
	public void doLoan(){
		logger.debug("NoticeTask start");
		while(NoticeJobQueue.NOTICE.size()>0){
			Notice notice=NoticeJobQueue.NOTICE.peek();
			if(notice!=null){
				try {
					noticeService.sendNotice(notice);
				} catch (Exception e) {
					logger.error(e.getMessage());
					e.printStackTrace();
				}finally{
					NoticeJobQueue.NOTICE.remove(notice);
				}
			}
		}
	}

	@Override
	public Object getLock() {
		return NoticeTask.MESSAGE_STATUS;
	}
	
}
