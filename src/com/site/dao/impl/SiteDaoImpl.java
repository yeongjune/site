package com.site.dao.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.base.dao.HQLDao;
import com.site.model.MsgConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.base.dao.SQLDao;
import com.site.dao.SiteDao;
import com.site.model.Site;

@Repository
public class SiteDaoImpl implements SiteDao {

	@Autowired
	private SQLDao dao;

    @Autowired
    private HQLDao hqlDao;

	@Override
	public Integer getSiteId(String domain) {
		return dao.queryForInt("select id from "+Site.tableName+" where domain=?", domain);
	}

	@Override
	public Map<String, Object> getListByPage(Integer currentPage,
			Integer pageSize, String keyword) {
		String kw = keyword==null||keyword.trim().equals("")?"%":"%"+keyword+"%";
		return dao.getPageMap("select * from "+Site.tableName+" where name like ? or domain like ? order by id desc", currentPage, pageSize, kw, kw);
	}

	@Override
	public Serializable save(Map<String, Object> map) {
		return dao.save(Site.tableName, map);
	}

	@Override
	public int delete(Integer id) {
		return dao.delete(Site.tableName, id);
	}

	@Override
	public int update(Map<String, Object> map) {
		return dao.updateMap(Site.tableName, "id", map);
	}

	@Override
	public Map<String, Object> load(Integer id) {
		return dao.queryForMap("select * from "+Site.tableName+" where id=?", id);
	}

	@Override
	public long countByDomainWithSelf(Integer id, String domain) {
		return dao.queryForLong("select count(id) from "+Site.tableName+" where id!=? and domain=?", id, domain);
	}

	@Override
	public long countByDomain(String domain) {
		return dao.queryForLong("select count(id) from "+Site.tableName+" where domain=?", domain);
	}

	@Override
	public List<Map<String, Object>> getList() {
		return dao.queryForList("select * from "+Site.tableName);
	}

	@Override
	public String getName(Integer siteId) {
		return dao.queryForObject("select name from "+Site.tableName+" where id=?", String.class, siteId);
	}

	@Override
	public long countByDirectoryWithSelf(Integer id, String directory) {
		return dao.queryForLong("select count(id) from "+Site.tableName+" where id!=? and directory=?", id, directory);
	}

	@Override
	public long countByDirectory(String directory) {
		return dao.queryForLong("select count(id) from "+Site.tableName+" where directory=?", directory);
	}

    @Override
    public MsgConfig getSMSConfig(Integer siteId) {
        String hql = "from " + MsgConfig.modelName + " where siteId = ? ";
        return (MsgConfig) hqlDao.getObjectByHQL(hql, siteId);
    }

    @Override
    public void saveSMSConfig(MsgConfig config) {
        hqlDao.save(config);
    }

}
