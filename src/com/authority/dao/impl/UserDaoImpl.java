package com.authority.dao.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.authority.dao.UserDao;
import com.authority.model.User;
import com.base.dao.SQLDao;

@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	private SQLDao dao;

	@Override
	public Map<String, Object> getUserByIdAndPassword(Integer siteId, String account, String password) {
		if(account.equals("admin")){
			return dao.queryForMap("select * from "+User.tableName+" where siteId=? and account=? and password=?", siteId, account, password);
		}else{
			return dao.queryForMap("select * from "+User.tableName+" where siteId=? and account=? and password=? and enable=1", siteId, account, password);
		}
	}

	@Override
	public Serializable save(Map<String, Object> map) {
		return dao.save(User.tableName, map);
	}

	@Override
	public int delete(Integer id, Integer siteId) {
		return dao.update("delete from "+User.tableName+" where id=? and siteId=? and account!='admin'", id, siteId);
	}

	@Override
	public int update(Map<String, Object> map) {
		return dao.updateMap(User.tableName, "id", map);
	}

	@Override
	public Map<String, Object> getListByPage(Integer siteId,
			Integer currentPage, Integer pageSize, String keyword) {
		String sql = "select * from "+User.tableName+" where siteId=? and (account like ? or name like ?)";
		String kw = keyword==null||keyword.trim().equals("")?"%":"%"+keyword+"%";
		return dao.getPageMap(sql, currentPage, pageSize, siteId, kw, kw);
	}

	@Override
	public Map<String, Object> load(Integer id) {
		return dao.queryForMap("select * from "+User.tableName+" where id=?", id);
	}

	@Override
	public long countByAccount(Integer siteId, String account) {
		return dao.queryForLong("select count(id) from "+User.tableName+" where siteId=? and account=?", siteId, account);
	}

	@Override
	public long countByAccount(String account) {
		return dao.queryForLong("select count(id) from "+User.tableName+" where (siteId is null or siteId='') and account=?", account);
	}

	@Override
	public int updateEnable(Integer siteId, Integer id, Integer status) {
		return dao.update("update "+User.tableName+" set enable=? where siteId=? and id=?", status, siteId, id);
	}

	@Override
	public List<Map<String, Object>> getList(Integer siteId) {
		return dao.queryForList("select * from "+User.tableName+" where siteId=?", siteId);
	}

	@Override
	public String getAdminPassword(Integer siteId) {
		return dao.queryForObject("select password from "+User.tableName+" where siteId=? and account='admin'", String.class, siteId);
	}

	@Override
	public Map<String, Object> getAdmin(Integer siteId) {
		return dao.queryForMap("select * from "+User.tableName+" where siteId=? and account='admin'", siteId);
	}

	@Override
	public int updatePassword(Integer siteId, Integer userId, String password,
			String newPassword) {
		return dao.update("update "+User.tableName+" set password=? where siteId=? and id=? and password=?", newPassword, siteId, userId, password);
	}

	@Override
	public int deleteBySiteId(Integer siteId) {
		return dao.delete(User.tableName, "siteId", siteId);
	}

    @Override
    public Map<String, Object> getUserById(Integer siteId, String account) {
        List<Map<String, Object>> list = dao.queryForList("select * from "+User.tableName+" where siteId=? and account=? and enable=1", siteId, account);
        return list.size() > 0 ? list.get(0) : null;
    }

}
