package com.site.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.base.dao.HQLDao;
import com.base.dao.SQLDao;
import com.base.vo.PageList;
import com.site.dao.MessageDao;
import com.site.model.Message;

/**
 * 
 * @author ah
 *
 */
@Repository
public class MessageDaoImpl implements MessageDao{

	@Autowired
	private HQLDao hqlDao;
	
	@Autowired
	private SQLDao sqlDao;
	
	public Integer save(Message message){
		return (Integer) hqlDao.save(message);
	}

	@Override
	public PageList list(Integer siteId, String keyword, Integer currentPage, Integer pageSize) {
		String hql =	" SELECT " +
							" m.id AS id," +
							" m.name AS name," +
							" m.email AS email," +
							" m.phone AS phone," +
							" m.content AS content," +
							" m.address AS address, " +
							" m.siteId AS siteId, " +
							" m.type AS type, " +
							" m.reply AS reply, " +
							" m.createTime AS createTime " +
						" FROM " +
							Message.modelName + " AS m" +
						" WHERE" +
							" m.siteId = ?" ;
		
		List<Object> param = new ArrayList<Object>();
		param.add(siteId);
		if(keyword != null && !keyword.equals("")){
			hql += " AND m.name LIKE ?";
			param.add("%" + keyword + "%");
		}
		
		hql += " ORDER BY m.createTime DESC";
		
		return hqlDao.getPageListMapByHQL(hql, currentPage, pageSize, param.toArray());
	}

	@Override
	public void delete(String ids) {
		
		String[] idArray = ids.split(",");
		String hql =	" DELETE" +
							" " + Message.modelName +
						" WHERE" +
							" id in (";
		List<Integer> param = new ArrayList<Integer>(); 
		for(String id : idArray){
			hql += " ?,";
			param.add(Integer.parseInt(id));
		}
		
		hql = hql.substring(0, hql.length() - 1) + ")";
		hqlDao.updateByHQL(hql, param.toArray());
	}
	
	@Override
	public Map<String, Object> getMsgById(Integer id){
		String sql = "SELECT " +
						" m.id AS id, " + 
						" m.content AS content, " +
						" m.type AS type, " +
						" m.name AS name, " +
						" m.siteId AS siteId, " +
						" m.reply AS reply " +
					" FROM " + 
						  Message.tableName +
						" AS m " +
						" WHERE m.id = ? ";
		return sqlDao.queryForMap(sql, id);
	}
	
	
	@Override
	public int updateReply(Integer id, String Content){
		String sql = "UPDATE " + Message.tableName + " SET reply = ? WHERE id = ?";
		return sqlDao.update(sql, Content, id);
	}
	
	
	@Override
	public PageList getHistory(Integer siteId, String name, Date createTime, Integer currentPage, Integer pageSize){
		
		String hql = "SELECT " +
				" m.id AS id," +
				" m.name AS name," +
				" m.email AS email," +
				" m.phone AS phone," +
				" m.content AS content," +
				" m.address AS address, " +
				" m.siteId AS siteId, " +
				" m.type AS type, " +
				" m.createTime AS createTime, " +
				" m.reply AS reply " +
			" FROM " + 
				  Message.modelName +
				" AS m " +
				" WHERE m.siteId = ? AND m.name = ? AND m.createTime <= ?" +
				" ORDER BY m.createTime DESC";
		
		return hqlDao.getPageListMapByHQL(hql, currentPage, pageSize, siteId, name, createTime);

	}
	
	
	@Override
	public PageList getMsgByName(Integer siteId, String name, Integer currentPage, Integer pageSize){
		String hql = "SELECT " +
						" m.id AS id," +
						" m.name AS name," +
						" m.email AS email," +
						" m.phone AS phone," +
						" m.content AS content," +
						" m.address AS address, " +
						" m.siteId AS siteId, " +
						" m.type AS type, " +
						" m.createTime AS createTime, " +
						" m.reply AS reply " +
					" FROM " + 
						  Message.modelName +
						" AS m " +
						" WHERE m.siteId = ? AND m.name = ?" +
						" ORDER BY m.createTime DESC";
		return hqlDao.getPageListMapByHQL(hql, currentPage, pageSize, siteId, name);
	}

    @Override
    public PageList findPageList(Integer currentPage, Integer pageSize, Integer siteId) {
        String sql = "SELECT " +
                " m.* " +
                " FROM " +
                Message.tableName +
                " AS m " +
                " WHERE m.siteId = ? and m.reply IS NOT NULL " +
                " ORDER BY m.createTime DESC";
        String countSql = "select count(*) from " + Message.tableName + " m where m.siteId = ? and m.reply IS NOT NULL ";
        return sqlDao.getPageList(countSql, sql, currentPage, pageSize, siteId);
    }

}
