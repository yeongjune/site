package com.apply.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.apply.dao.CertificateDao;
import com.apply.model.Certificate;
import com.base.dao.HQLDao;
import com.base.dao.SQLDao;
import com.base.util.StringUtil;

@Repository
public class CertificateDaoImpl implements CertificateDao {
	@Autowired
	private HQLDao hqlDao;
	
	@Autowired
	private SQLDao sqlDao;

	@Override
	public Serializable save(Certificate certificate) {
		
		return hqlDao.save(certificate);
	}

	@Override
	public int delete(String ids,Integer siteId) {
		if (StringUtil.isEmpty(ids)) {
			return 0;
		}
		String sql=" delete from "+Certificate.tableName +"  where id in ("; 
		String [] idsArray=ids.split(",");
		List<Object> param=new ArrayList<Object>();
		for (int i=0;i<idsArray.length;i++) {
			sql +=" ? ";
			if (i!=idsArray.length-1) {
				sql += " , ";
			}
			param.add(Integer.parseInt(idsArray[i]));
		}
		sql+=" ) ";
		if (siteId!=null) {
			sql += " and siteId = ? ";
			param.add(siteId);
		}
		return sqlDao.update(sql, param.toArray());
	}

	@Override
	public int update(Map<String, Object> certificate) {
		
		return sqlDao.updateMap(Certificate.tableName, "id", certificate);
	}

	@Override
	public Certificate get(Integer id) {
		
		return hqlDao.get(Certificate.class, id);
	}

	@Override
	public List<Map<String, Object>> findCertificateList(String keyword,Integer siteId) {
		String sql=" select c.* from "+Certificate.tableName +" c where 1=1 ";
		List<Object> params=new ArrayList<Object>();
		if (!StringUtil.isEmpty(keyword)) {
			sql += " and ( c.name like ? or c.remark like ? ) ";
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
		}
		if (siteId!=null) {
			sql += " and c.siteId = ? ";
			params.add(siteId);
		}
		if (params.size()>0) {
			return sqlDao.queryForList(sql, params.toArray());
		}else{
			return sqlDao.queryForList(sql);
		}
	}

	@Override
	public Map<String, Object> findByName(String name,Integer siteId) {
		String sql=" select c.id,c.name,c.siteId,c.remark from " + Certificate.tableName  +" as c where c.name= ? and c.siteId = ? ";
		return sqlDao.queryForMap(sql, name,siteId);
	}

}
