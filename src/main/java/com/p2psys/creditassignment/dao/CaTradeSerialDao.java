package com.p2psys.creditassignment.dao;

import java.util.List;
import java.util.Map;

import com.p2psys.creditassignment.domain.CaTradeSerial;

public interface CaTradeSerialDao {
	
	public void add(CaTradeSerial caTradeSerial);
	
	public List<CaTradeSerial> getCatsList(Map<String , Object> map);
	
	public void modifyCa(CaTradeSerial caTradeSerial);
}
