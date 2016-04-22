package com.site.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.base.dao.HQLDao;
import com.base.dao.SQLDao;
import com.base.util.StringUtil;
import com.base.vo.PageList;
import com.site.dao.SiteUserDao;
import com.site.model.SiteUser;
import com.site.vo.SiteUserSearchVo;

@Repository
public class SiteUserDaoImpl implements SiteUserDao {

	@Autowired
	private HQLDao hqlDao;

	@Autowired
	private SQLDao sqlDao;

	@Override
	public Serializable save(SiteUser siteUser) {

		return hqlDao.save(siteUser);
	}

	@Override
	public Serializable save(Map<String, Object> siteUser) {

		return sqlDao.save(SiteUser.tableName, siteUser);
	}
	
	@Override
	public int saveBatch(List<Map<String, Object>> siteUserList) {
		return sqlDao.save(SiteUser.tableName, siteUserList);
	}

	@Override
	public int update(SiteUser siteUser) {
		hqlDao.update(siteUser);
		return 1;
	}

	@Override
	public int update(Map<String, Object> siteUser) {

		return sqlDao.updateMap(SiteUser.tableName, "id", siteUser);
	}

	@Override
	public int delete(Integer... id) {
		if (id == null || id.length < 1) {
			return 0;
		}
		if (id.length == 1) {
			return sqlDao.delete(SiteUser.tableName, id[0]);
		} else {
			String sql = " delete from " + SiteUser.tableName
					+ "  where id in (";
			List<Object> param = new ArrayList<Object>();
			for (int i = 0; i < id.length; i++) {
				sql += " ? ";
				if (i != id.length - 1) {
					sql += " , ";
				}
				param.add(id[i]);
			}
			sql += " ) ";
			return sqlDao.update(sql, param.toArray());
		}
	}

	@Override
	public SiteUser get(Integer id) {

		return hqlDao.get(SiteUser.class, id);
	}

	@Override
	public Map<String, Object> load(Integer id) {
		String sql = " select u.* from " + SiteUser.tableName
				+ "  u where u.id = ? ";
		return sqlDao.queryForMap(sql, id);
	}

	@Override
	public PageList findSiteUserPageList(SiteUserSearchVo searchVo,
			Integer pageSize, Integer currentPage) {
		String sql = " select u.* from " + SiteUser.tableName + "  u ";
		if (searchVo == null) {
			return sqlDao.getPageList(sql, currentPage, pageSize);
		}
		sql += " where 1=1  ";
		List<Object> params = new ArrayList<Object>();
		if (!StringUtil.isEmpty(searchVo.getAcount())) {
			sql += " and u.acount like ? ";
			params.add("%" + searchVo.getAcount() + "%");
		}
		if (!StringUtil.isEmpty(searchVo.getName())) {
			sql += " and u.name like ? ";
			params.add("%" + searchVo.getName() + "%");
		}
		if (!StringUtil.isEmpty(searchVo.getKeyword())) {
			sql += " and (u.name like ? or u.acount like ? or u.email like ?)";
			params.add("%" + searchVo.getKeyword() + "%");
			params.add("%" + searchVo.getKeyword() + "%");
			params.add("%" + searchVo.getKeyword() + "%");
		}
		if (!StringUtil.isEmpty(searchVo.getAllAcount())) {
			sql += " and u.acount = ? ";
			params.add(searchVo.getAllAcount());
		}
		if (searchVo.getStatus() != null) {
			sql += " and u.status = ? ";
			params.add(searchVo.getStatus());
		}
		if (searchVo.getSiteId()!=null) {
			sql += " and u.siteId = ? ";
			params.add(searchVo.getSiteId());
		}
		if(searchVo.getDepartment()!=null && !searchVo.getDepartment().equals("")){
			sql += " and u.department = ? ";
			params.add(searchVo.getDepartment());
		}
		if(searchVo.getStartTime()!=null && !searchVo.getStartTime().equals("")){
			sql += " and u.createTime >= ? ";
			params.add(searchVo.getStartTime());
		}
		if(searchVo.getEndTime()!=null && !searchVo.getEndTime().equals("")){
			sql += " and u.createTime <= ? ";
			params.add(searchVo.getEndTime());
		}
		return sqlDao.getPageList(sql, currentPage, pageSize, params.toArray());
	}

	@Override
	public List<Map<String, Object>> findSiteUserList(
			SiteUserSearchVo searchVo, Integer start, Integer limit) {
		String sql = " select u.* from " + SiteUser.tableName + "  u ";
		List<Object> params = new ArrayList<Object>();
		
		//处理查询条件
		if (searchVo != null) {
			sql += " where 1=1 ";
			if (!StringUtil.isEmpty(searchVo.getAcount())) {
				sql += " and u.acount like ? ";
				params.add("%" + searchVo.getAcount() + "%");
			}
			if (!StringUtil.isEmpty(searchVo.getName())) {
				sql += " and u.name like ? ";
				params.add("%" + searchVo.getName() + "%");
			}
			if (!StringUtil.isEmpty(searchVo.getKeyword())) {
				sql += " and (u.name like ? or u.acount like ? or u.email like ?)";
				params.add("%" + searchVo.getKeyword() + "%");
				params.add("%" + searchVo.getKeyword() + "%");
				params.add("%" + searchVo.getKeyword() + "%");
			}
			if (!StringUtil.isEmpty(searchVo.getAllAcount())) {   
				sql += " and u.acount = ? ";
				params.add(searchVo.getAllAcount());
			}
			if (searchVo.getStatus() != null) {
				sql += " and u.status = ? ";
				params.add(searchVo.getStatus());
			}
			if (searchVo.getSiteId()!=null) {
				sql += " and u.siteId = ? ";
				params.add(searchVo.getSiteId());
			}
		}
		// 处理查询的条数
		if (limit != null && limit > 0) {
			if (start != null && start >= 1) {
				sql += " limit ? , ?  ";
				params.add(start - 1);
				params.add(limit);
			} else {
				sql += " limit 0,? ";
				params.add(limit);
			}
		} else if (start != null && start >= 1) {
			sql += " limit ? ";
			params.add(start - 1);
		}
		
		//执行查询
		if (params.size()>0) {
			return sqlDao.queryForList(sql, params.toArray());
		}else{
			return sqlDao.queryForList(sql);
		}
	}

	@Override
	public int checkPassword(Integer id, String password){
		String sql = "SELECT COUNT(*) FROM " + SiteUser.tableName + " WHERE id = ? AND password = ?";
		return sqlDao.queryForInt(sql, id, password);
	}

	@Override
	public List<Map<String, Object>> findDepartmentTypes(Integer siteId) {
		String sql = "SELECT department " 
				+ " FROM " + SiteUser.tableName 
				+ " WHERE siteId = ? AND department IS NOT NULL AND department != '' " 
				+ " GROUP BY department";
		return sqlDao.queryForList(sql, siteId);
	}

	@Override
	public int checkAccount(Integer siteId, String account) {
		String sql = " SELECT COUNT(*) FROM " + SiteUser.tableName + " WHERE siteId = ? AND acount = ? ";
		return sqlDao.queryForInt(sql, siteId, account);
	}
	
	
}
