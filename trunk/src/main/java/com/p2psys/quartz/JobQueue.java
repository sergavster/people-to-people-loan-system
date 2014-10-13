package com.p2psys.quartz;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.p2psys.domain.Repayment;
import com.p2psys.model.DetailCollection;
import com.p2psys.model.borrow.BorrowModel;

public class JobQueue<T> {
	private Queue<T> queue=new ConcurrentLinkedQueue();

	public synchronized void offer(T model){
		if(!queue.contains(model)){
			queue.offer(model);
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
	
	public static JobQueue<BorrowModel> FULL_SUCCESS=new JobQueue<BorrowModel>();
	public static JobQueue<BorrowModel> FULL_FAIL=new JobQueue<BorrowModel>();
	public static JobQueue<BorrowModel> CANCEL=new JobQueue<BorrowModel>();
	public static JobQueue<Repayment> REPAY=new JobQueue<Repayment>();
	public static JobQueue<DetailCollection> FLOW_REPAY=new JobQueue<DetailCollection>();
	public static JobQueue<BorrowModel> SECOND_REPAY=new JobQueue<BorrowModel>();
	public static JobQueue<BorrowModel> AUTO_TENDER=new JobQueue<BorrowModel>();
	
	// v1.6.7.2 RDPROJECT-504 sj 2013-12-06 start
	public static JobQueue<BorrowModel> AUTO_RECALL = new JobQueue<BorrowModel>();
	// v1.6.7.2 RDPROJECT-504 sj 2013-12-06 end
	
}
