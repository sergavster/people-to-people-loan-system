package com.p2psys.dao.jdbc;

import java.util.List;

import org.apache.log4j.Logger;

import com.p2psys.dao.NoticeDao;
import com.p2psys.domain.Notice;
import com.p2psys.model.SearchParam;

public class NoticeDaoImpl extends BaseDaoImpl implements  NoticeDao {
	private static Logger logger = Logger.getLogger(NoticeDaoImpl.class);

	
	@Override
	public List<Notice> getList(int start, int pernum, SearchParam param) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int getListCount(SearchParam param) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void add(Notice n){
		//v1.6.7.1 RDPROJECT-314 liukun 2013-11-13 start
		/*String sql="insert into dw_notice(type,status,sent_user,recevie_user,content,result) " +
				"values(?,?,?,?,?,?)";
		this.getJdbcTemplate().update(sql,n.getName(),n.getStatus(),n.getSend_userid(),n.getReceive_userid(),n.getContent(),n.getResult());*/
		/*String sql="insert into dw_notice(nid,type,sent_user,recevie_user,status,title,content,result) " +
				"values(?,?,?,?,?,?,?,?)";
		this.getJdbcTemplate().update(sql,n.getNid(),n.getType(),n.getSend_userid(),n.getReceive_userid(),n.getStatus(),n.getTitle(),n.getContent(),n.getResult());*/
		//v1.6.7.1 RDPROJECT-314 liukun 2013-11-13 start
		
		//v1.6.7.1 RDPROJECT-522 liukun 2013-12-03 start
		
		String sql="insert into dw_notice(nid,type,sent_user,recevie_user,status,title,content,result,addtime,receive_addr) " +
				"values(?,?,?,?,?,?,?,?,?,?)";
		this.getJdbcTemplate().update(sql,n.getNid(),n.getType(),n.getSend_userid(),n.getReceive_userid(),n.getStatus(),n.getTitle(),n.getContent(),n.getResult(),n.getAddtime(),n.getReceive_addr());
		//v1.6.7.1 RDPROJECT-522 liukun 2013-12-03 end
		
	}

}
