package com.p2psys.dao.jdbc;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.p2psys.dao.AccountBackDao;
import com.p2psys.domain.AccountBack;
import com.p2psys.model.AccountBackModel;
import com.p2psys.model.SearchParam;
import com.p2psys.util.DateUtils;
import com.p2psys.util.StringUtils;
/**
 * 扣款管理Dao实现类
 * 
 
 * @version 1.0
 * @since 2013-12-12
 */
public class AccountBackDaoImpl extends BaseDaoImpl implements AccountBackDao{
	 private Logger logger =Logger.getLogger(AccountBackDaoImpl.class);
	 String queryRechargeSql=" from dw_account_back as p1 " +
	            "left join dw_user as p2 on p1.user_id=p2.user_id " +
	            "left join dw_user as verify_kefu on p1.verify_userid=verify_kefu.user_id  where 1=1 ";
    @Override
    public void addBack(AccountBack b) {
    	this.insert(b);
    }
    @Override
    public int getBackCount(SearchParam param) {
        StringBuffer sql=new StringBuffer("select count(p1.id) ");
        sql.append(queryRechargeSql);
        Map<String, Object> map = new HashMap<String, Object>();
        if (param != null) {
			if (!StringUtils.isBlank(param.getUsername())) {
				sql.append(" and p2.username like concat('%',:username,'%')");
				map.put("username", param.getUsername());
			}
			if (!StringUtils.isBlank(param.getRealname())) {
				sql.append(" and p2.realname like concat('%',:realname,'%')");
				map.put("realname", param.getRealname());
			}
			if (!StringUtils.isBlank(param.getDotime1())) {
				sql.append(" and p1.addtime >= :dotime1");
				map.put("dotime1", param.getSearchTime(param.getDotime1(),0));
			}
			if (!StringUtils.isBlank(param.getDotime2())) {
				sql.append(" and p1.addtime <= :dotime2");
				map.put("dotime2", param.getSearchTime(param.getDotime2(),1));
			}
			if (!StringUtils.isBlank(param.getStatus())) {
				sql.append(" and p1.status = :status");
				map.put("status", Integer.parseInt(param.getStatus()));
			}
		}
		
		logger.debug("BackDao.getBackCount():"+sql.toString());
		return getNamedParameterJdbcTemplate().queryForInt(sql.toString(), map);
    }

    @Override
    public List<AccountBack> getBackList(int start, int end, SearchParam param) {
        StringBuffer sql=new StringBuffer();
        sql=new StringBuffer("select p1.*,p1.money,verify_kefu.username as verify_username,p2.username,p2.realname  ");
        sql.append(queryRechargeSql);
        Map<String, Object> map = new HashMap<String, Object>();
        if (param != null) {
			if (!StringUtils.isBlank(param.getUsername())) {
				sql.append(" and p2.username like concat('%',:username,'%')");
				map.put("username", param.getUsername());
			}
			if (!StringUtils.isBlank(param.getRealname())) {
				sql.append(" and p2.realname like concat('%',:realname,'%')");
				map.put("realname", param.getRealname());
			}
			if (!StringUtils.isBlank(param.getDotime1())) {
				sql.append(" and p1.addtime >= :dotime1");
				map.put("dotime1", param.getSearchTime(param.getDotime1(),0));
			}
			if (!StringUtils.isBlank(param.getDotime2())) {
				sql.append(" and p1.addtime <= :dotime2");
				map.put("dotime2", param.getSearchTime(param.getDotime2(),1));
			}
			if (!StringUtils.isBlank(param.getStatus())) {
				sql.append(" and p1.status = :status");
				map.put("status", Integer.parseInt(param.getStatus()));
			}
		}
		sql.append(" order by p1.id desc ");
		if (end > 0) {// 分页查询，若为0，则取所有（导出报表时取所有）
			sql.append(" LIMIT :start,:pernum");
			map.put("start", start);
			map.put("pernum",end);
		}
		logger.debug("BackDao.getBackList():"+sql.toString());
		return getNamedParameterJdbcTemplate().query(sql.toString(), map, getBeanMapper(AccountBackModel.class));
    }
    @Override
    public AccountBackModel getBack(long id) {
        StringBuffer sb=new StringBuffer();
        sb=new StringBuffer("select p1.*,verify_kefu.username as verify_username,p1.money,p2.username,p2.realname ");
        sb.append(queryRechargeSql).append(" and p1.id=:id ");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        String sql=sb.toString();
        logger.debug("BackDao.getBack(id):"+sql.toString());
        return this.getNamedParameterJdbcTemplate().queryForObject(sql, map, getBeanMapper(AccountBackModel.class));
    }
    @Override
    public void updateBack(AccountBackModel b) {
    	String sql="update dw_account_back set status=:status,verify_userid=:verify_userid,verify_time=:verify_time,verify_remark=:verify_remark where id=:id";
    	logger.debug("BackDao.updateBack(b):"+sql.toString());
    	SqlParameterSource ps = new BeanPropertySqlParameterSource(b);
		this.getNamedParameterJdbcTemplate().update(sql.toString(), ps);
    }
}
