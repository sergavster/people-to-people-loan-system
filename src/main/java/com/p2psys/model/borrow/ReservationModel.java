package com.p2psys.model.borrow;

import com.p2psys.domain.Reservation;


public class ReservationModel extends Reservation{
	
	private static final long serialVersionUID = 6227166783859660460L;
	//借款人姓名
	private String username;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
}
