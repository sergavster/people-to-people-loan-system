package com.p2psys.tool.disruptor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.SingleThreadedClaimStrategy;
import com.p2psys.context.Global;

public class DisruptorUtils {
	
	private static DisruptorUtils dis = null;
	private static final int BUFFER_SIZE = Integer.parseInt(Global.getValue("disruptor_ringbuffer_size"));

	private final RingBuffer<ValueEvent> ringBuffer = new RingBuffer<ValueEvent>(
			ValueEvent.EVENT_FACTORY, new SingleThreadedClaimStrategy(BUFFER_SIZE),
			new BlockingWaitStrategy());
	
	private final SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();
	private final CEventHandler handler = new CEventHandler();

	private final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

	private final BatchEventProcessor<ValueEvent> batchEventProcessor = new BatchEventProcessor<ValueEvent>(
			ringBuffer, sequenceBarrier, handler);

	public DisruptorUtils() {
		ringBuffer.setGatingSequences(batchEventProcessor.getSequence());
	}

	public void consume() {
		EXECUTOR.submit(batchEventProcessor);
	}

	public void produce(ValueEvent event) {
		new Thread(new Producer(event , ringBuffer)).start();
	}

	static{
		dis = new DisruptorUtils();
	}
	
	//投标
	/*public static void tender(BorrowParam borrowParam, User user) throws Exception {
		ValueEvent event = new ValueEvent();
    	event.setOperate("tender");
    	event.setBorrowParam(borrowParam);
    	event.setUser(user);
    	dis.produce(event);
    	dis.consume();
	}*/
	
}
