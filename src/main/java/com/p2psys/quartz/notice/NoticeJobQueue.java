package com.p2psys.quartz.notice;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.annotation.Autowired;

import com.p2psys.domain.Notice;
import com.p2psys.service.NoticeService;

public class NoticeJobQueue<T> {
	private Queue<T> queue=new ConcurrentLinkedQueue();
	private LoanTask task;
	@Autowired
	private static NoticeService noticeService;
	
	public NoticeJobQueue(LoanTask task) {
		super();
		this.task = task;
	}

	public synchronized void offer(T model){
		if(!queue.contains(model)){
			queue.offer(model);
			synchronized(task.getLock()){
				task.execute();
			}
		}
	}
	
	public synchronized void offer(List<T> ts){
		for(int i=0;i<ts.size();i++){
			T t=ts.get(i);
			if(!queue.contains(t)){
				queue.offer(t);
			}
		}
		synchronized(task.getLock()){
			task.execute();
		}
	}
	
	public synchronized T poll(){
		return queue.poll();
	}
	public synchronized T peek(){
		return queue.peek();
	}
	public synchronized void remove(T model){
		queue.remove(model);
	}
	public int size(){
		return queue.size();
	}
	
	public static NoticeJobQueue<Notice> NOTICE=null;
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-12 start
	//public static NoticeJobQueue<Notice> MESSAGE=null;
	//v1.6.7.1 RDPROJECT-314 liukun 2013-11-12 end
	
	public static void init(NoticeService noticeService){
		//v1.6.7.1 RDPROJECT-314 liukun 2013-11-12 start
		//NOTICE=new NoticeJobQueue<Notice>(new SmsTask(noticeService));
		//MESSAGE=new NoticeJobQueue<Notice>(new MessageTask(noticeService));
		NOTICE=new NoticeJobQueue<Notice>(new NoticeTask(noticeService));
		//v1.6.7.1 RDPROJECT-314 liukun 2013-11-12 end
	}
	
	public static void stop(){
		NOTICE.task.stop();
	}
	
	public void doAutoQueue(){
	}
	
}
