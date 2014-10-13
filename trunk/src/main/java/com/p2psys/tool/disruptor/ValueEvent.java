package com.p2psys.tool.disruptor;

import com.lmax.disruptor.EventFactory;
import com.p2psys.domain.User;

public final class ValueEvent
{
	//操作
	private String operate;
    private long value;
    private User user;
    
    public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public long getValue()
    {
        return value;
    }

    public void setValue(final long value)
    {
        this.value = value;
    }

    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public final static EventFactory<ValueEvent> EVENT_FACTORY = new EventFactory<ValueEvent>()
    {
        public ValueEvent newInstance()
        {
            return new ValueEvent();
        }
    };
}