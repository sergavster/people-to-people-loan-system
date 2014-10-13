package com.p2psys.tool.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.p2psys.context.Global;

public class Producer implements Runnable {

	private ValueEvent event = null;
	private RingBuffer<ValueEvent> ringBuffer = null;

	public Producer(ValueEvent e , RingBuffer<ValueEvent> rb) {
		this.event = e;
		this.ringBuffer = rb;
	}

	@Override
	public void run() {
		// Publishers claim events in sequence
		if(this.event!=null){
			if(ringBuffer.remainingCapacity() < Integer.parseInt(Global.getValue("disruptor_ringbuffer_size")) * 0.1) {
				//SmsOpenUtils.sendSms("http://smsopen.erongdu.com/sendsms.php", "ywdjava", "3gpOtlJ3", "15968892760", "宜城贷Disruptor BUFFER_SIZE只剩10%,请适时增加。");
            } else {
				long sequence = ringBuffer.next();
				ValueEvent e = ringBuffer.get(sequence);
				e.setOperate(event.getOperate());
				e.setValue(event.getValue());
				e.setUser(event.getUser());
				ringBuffer.publish(sequence);
            }
		}
	}
}
