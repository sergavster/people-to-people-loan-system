package com.p2psys.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;

import com.p2psys.dao.CommentDao;
import com.p2psys.domain.Comments;
import com.p2psys.model.BorrowComments;
import com.p2psys.model.SearchParam;

public class CommentDaoImp extends BaseDaoImpl implements CommentDao {
	private Logger logger = Logger.getLogger(CommentDaoImp.class);

	@Override
	public List getCommentList(long borrowid) {
		String sql;
		List list = new ArrayList();
		if (borrowid > 0) {
			sql = "select cm.*,us.username,us.card_pic1 from dw_comment as cm LEFT JOIN dw_user as us on cm.user_id=us.user_id where article_id=? and cm.status=1";
		} else {
			sql = "select cm.*,us.username,us.card_pic1 from dw_comment as cm LEFT JOIN dw_user as us on cm.user_id=us.user_id";
		}
		logger.debug("Sql:" + sql);
		list = getJdbcTemplate().query(sql, new Object[] { borrowid }, getBeanMapper(BorrowComments.class));
		return list;
	}

	@Override
	public int getCommentCount(long borrowid) {
		String sql = "SELECT COUNT(id) from dw_comment where article_id=? and status=1";
		String sql2 = "SELECT COUNT(id) from dw_comment";
		if (borrowid > 0) {
			return getJdbcTemplate()
					.queryForInt(sql, new Object[] { borrowid });
		} else {
			if (borrowid == -1) {
				return getJdbcTemplate().queryForInt(sql2);
			}
		}

		return -3;
	}

	@Override
	public int addComment(Comments c) {
		String sql = "insert into dw_comment(pid,user_id,module_code,article_id,comment,flag,`order`,status,addtime,addip) values(?,?,?,?,?,?,?,?,?,?)";
		return getJdbcTemplate().update(
				sql,
				new Object[] { c.getPid(), c.getUser_id(), c.getModule_code(),
						c.getArticle_id(), c.getComment(), c.getFlag(),
						c.getOrder(), c.getStatus(), c.getAddtime(),
						c.getAddip() });
	}

	@Override
	public int deleteCommentByCommentId(long id) {
		String sql = "delete from dw_comment where id=?";
		return getJdbcTemplate().update(sql);
	}

	@Override
	public int deleteCommentByBorrowId(long Borrowid) {
		String sql="delete from dw_comment where article_id=?";
		return getJdbcTemplate().update(sql);
	}

	@Override
	public int deleteCommentByUserId(long userid) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.p2psys.dao.CommentDao#updateCommentByUserId(long) 根据ID修改状态
	 */
	@Override
	public void updateCommentByID(Comments c) {
		String sql = "update dw_comment set status=? where id=?";
		getJdbcTemplate().update(sql, c.getStatus(), c.getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.p2psys.dao.CommentDao#deleteCommentArticleID(long) 根据id删除
	 */
	@Override
	public void deleteCommentID(long id) {
		String sql = "delete from dw_comment where id=?";
		getJdbcTemplate().update(sql, id);
	}

	@Override
	public Comments getCommentByid(long id) {
		String sql = "select * from dw_comment where id=?";
		Comments comments = null;
		try {
			comments = getJdbcTemplate().queryForObject(sql,
					new Object[] { id }, new RowMapper<Comments>() {

						@Override
						public Comments mapRow(ResultSet rs, int rowNum)
								throws SQLException {
							Comments comments = new Comments();
							comments.setId(rs.getInt("id"));
							comments.setComment(rs.getString("comment"));
							return comments;
						}

					});
		} catch (DataAccessException ea) {
			logger.debug("数据库查询结果为空！");
		}
		return comments;
	}

	@Override
	public int getCommentCount(SearchParam param) {
		String selectSql = "SELECT COUNT(p1.user_id) FROM dw_comment p1 LEFT JOIN dw_user p2 ON p1.user_id=p2.user_id WHERE 1=1 ";
		String searchSql = param.getSearchParamSql();
		// v1.6.7.2 RDPROJECT-554 zza start
//		searchSql = searchSql.replace("p1", "p2");
		// v1.6.7.2 RDPROJECT-554 zza end
		StringBuffer sb = new StringBuffer(selectSql);
		String querySql = sb.append(searchSql).toString();
		logger.debug("getCommentCount:" + querySql);
		int total = 0;
		total = count(querySql);
		return total;
	}

	@Override
	public List getCommentCounts(int start, int end, SearchParam param) {
		String selectSql = "SELECT p1.id, p1.comment, p2.user_id , p2.username ,p1.addtime ,p1.status FROM dw_comment p1 ,dw_user p2 WHERE p1.user_id=p2.user_id";
		String searchSql = param.getSearchParamSql();
		// v1.6.7.2 RDPROJECT-554 zza start
//		searchSql = searchSql.replace("p1", "p2");
		// v1.6.7.2 RDPROJECT-554 zza end
		String limitSql = " limit ?,?";
		String groupSql=" group by p1.id ";
		String orderSql=" order by p1.id desc ";
		StringBuffer sb = new StringBuffer(selectSql);
		String querySql = sb.append(searchSql).append(groupSql).append(orderSql).append(limitSql).toString();
		List list = getJdbcTemplate().query(querySql,
				new Object[] { start, end }, new RowMapper<Comments>() {

					@Override
					public Comments mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						BorrowComments comments = new BorrowComments();
						comments.setId(rs.getInt("id"));
						comments.setUser_id(rs.getInt("user_id"));
						comments.setAddtime(rs.getString("addtime"));
						comments.setStatus(rs.getInt("Status"));
						comments.setComment(rs.getString("comment"));
						comments.setUsername(rs.getString("username"));
						
						return comments;
					}

				});
		return list;
	}

}
