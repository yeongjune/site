package com.site.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.base.dao.HQLDao;
import com.base.dao.SQLDao;
import com.base.util.StringUtil;
import com.base.vo.PageList;
import com.site.dao.FriendlyLinkDao;
import com.site.model.FriendlyLink;

@Repository
public class FriendlyLinkDaoImpl implements FriendlyLinkDao {
	@Autowired
	private SQLDao sqlDao;
	@Autowired
	private HQLDao hqlDao;

	@Override
	public Serializable save(FriendlyLink friendlyLink) {
		
		return hqlDao.save(friendlyLink);
	}

	@Override
	public Serializable save(Map<String, Object> friendlyLink) {
		
		return sqlDao.save(FriendlyLink.tableName, friendlyLink);
	}

	@Override
	public int delete(Integer id) {
		return sqlDao.delete(FriendlyLink.tableName, id);
	}

	@Override
	public int delete(Integer siteId,String ids) {
		if (siteId==null||siteId<1||StringUtil.isEmpty(ids)) {
			return 0;
		}
		String sql=" delete from "+FriendlyLink.tableName +"  where siteId = ? and  id in ("; 
		String [] idsArray=ids.split(",");
		List<Object> param=new ArrayList<Object>();
		param.add(siteId);
		for (int i=0;i<idsArray.length;i++) {
			sql +=" ? ";
			if (i!=idsArray.length-1) {
				sql += " , ";
			}
			param.add(Integer.parseInt(idsArray[i]));
		}
		sql+=" ) ";
		return sqlDao.update(sql, param.toArray());
	}

	@Override
	public int update(Map<String, Object> friendlyLink) {
		
		return this.sqlDao.updateMap(FriendlyLink.tableName, "id", friendlyLink);
	}

	@Override
	public int update(FriendlyLink friendlyLink) {
		this.hqlDao.update(friendlyLink);
		return 1;
	}

	@Override
	public Map<String, Object> load(Integer id) {
		String sql="select f.* from "+FriendlyLink.tableName +" f where f.id=? ";
		return this.sqlDao.queryForMap(sql,id);
	}

	@Override
	public FriendlyLink get(Integer id) {
		return this.hqlDao.get(FriendlyLink.class, id);
	}

	@Override
	public List<Map<String, Object>> findFriendlyLinkList(Integer siteId,
			String keyword, Integer start, Integer limit,String effective) {
		String sql="select f.* from "+FriendlyLink.tableName +" as  f where 1=1 ";
		List<Object> params=new ArrayList<Object>();
		if (siteId!=null&&siteId>0) {
			sql +=" and f.siteId = ? ";
			params.add(siteId);
		}
		if (!StringUtil.isEmpty(keyword)) {
			sql +=" and f.name like ? ";
			params.add("%"+keyword+"%");
		}
		if(StringUtils.isNotEmpty(effective)) {
			sql +=" and f.effective=0 ";
		}
		sql+=" order by f.createTime  desc ";//有效链接
		if(limit!=null && limit>0){
			if (start !=null && start >=1) {
				sql += " limit ? , ?  ";
				params.add(start-1);
				params.add(limit);
			}else{
				sql += " limit 0,? ";
				params.add(limit);
			}
		}else if (start !=null && start >=1 ){
			  sql += " limit ? ";
			  params.add(start-1);
		}
		
		if (params.size()>0) {
			return this.sqlDao.queryForList(sql,params.toArray());
		}else{
			return this.sqlDao.queryForList(sql);
		}
	}

	@Override
	public PageList findFriendlyLinkPageList(Integer currentPage,
			Integer pageSize, Integer siteId, String keyword) {
		String sql="select f.* from "+FriendlyLink.tableName +" f where 1=1 ";
		List<Object> params=new ArrayList<Object>();
		if (siteId!=null&&siteId>0) {
			sql +=" and f.siteId = ? ";
			params.add(siteId);
		}
		if (!StringUtil.isEmpty(keyword)) {
			sql +=" and f.name like ? ";
			params.add("%"+keyword+"%");
		}
		sql+=" order by f.createTime  desc ";
		if (params.size()>0) {
			return this.sqlDao.getPageList(sql, currentPage, pageSize,params.toArray());
		}else{
			return this.sqlDao.getPageList(sql, currentPage, pageSize);
		}
	}

	@Override
	public List<Map<String, Object>> findEffectiveFriendlyLinkList() {
		// TODO Auto-generated method stub
		StringBuilder sql=new StringBuilder();
		sql.append("select id,linkUrl from ").append(FriendlyLink.tableName);
		return this.sqlDao.queryForList(sql.toString());
	}

	@Override
	public int updateEffective(long id, int status) {
		// TODO Auto-generated method stub
		StringBuilder sql=new StringBuilder();
		sql.append("update ").append(FriendlyLink.tableName).append(" set effective=? ").append(" where id=?");
		return this.sqlDao.update(sql.toString(), status,id);
	}
}
