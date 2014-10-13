package com.p2psys.creditassignment.dao.jdbc;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.p2psys.creditassignment.dao.CaTradeSerialDao;
import com.p2psys.creditassignment.domain.CaTradeSerial;
import com.p2psys.dao.jdbc.BaseDaoImpl;

@Repository
public class CaTradeSerialDaoImpl extends BaseDaoImpl implements CaTradeSerialDao {

	@Override
	public void add(CaTradeSerial caTradeSerial) {
		insert(caTradeSerial);
	}

	@Override
	public List<CaTradeSerial> getCatsList(Map<String , Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void modifyCa(CaTradeSerial caTradeSerial) {
		this.modify(CaTradeSerial.class, caTradeSerial);
		
	}

	
	
}
