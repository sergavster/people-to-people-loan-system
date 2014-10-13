package com.p2psys.tool.disruptor;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.lmax.disruptor.EventHandler;
public class CEventHandler implements EventHandler<ValueEvent>{
	Logger logger = Logger.getLogger(CEventHandler.class);
	@Override
	public void onEvent(ValueEvent event, long sequence, boolean endOfBatch) throws Exception {
		try {
			WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
			/*if("tender".equals(event.getOperate())){//投标
				BorrowService borrowService = (BorrowService) ctx.getBean("borrowService");  
				borrowService.addTender(event.getBorrowParam(), event.getUser());
			}*/
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
	}

}
