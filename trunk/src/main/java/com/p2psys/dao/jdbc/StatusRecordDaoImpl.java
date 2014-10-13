package com.p2psys.dao.jdbc;
//v1.6.7.2 RDPROJECT-579 xx 2013-12-10 start
//新增
//v1.6.7.2 RDPROJECT-579 xx 2013-12-10 end
import com.p2psys.dao.StatusRecordDao;
import com.p2psys.domain.StatusRecord;

public class StatusRecordDaoImpl extends BaseDaoImpl implements StatusRecordDao {

	@Override
	public long add(StatusRecord model) {
		return insertHoldKey(model);
	}

}
