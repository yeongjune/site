package com.site.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.base.dao.HQLDao;
import com.base.dao.SQLDao;
import com.base.util.StringUtil;
import com.site.dao.ContactDao;
import com.site.model.Contact;

@Repository
public class ContactDaoImpl implements ContactDao {
	@Autowired
	private SQLDao sqlDao;
	
	@Autowired
	private HQLDao hqlDao;
	
	@Override
	public Serializable save(Contact contact) {
		
		return hqlDao.save(contact);
	}

	@Override
	public Serializable save(Map<String, Object> contact) {
		
		return sqlDao.save(Contact.tableName, contact);
	}

	@Override
	public int delete(Integer id) {
		
		return sqlDao.delete(Contact.tableName, id);
	}

	@Override
	public int delete(Integer siteId, String ids) {
		if (siteId==null||siteId<1||StringUtil.isEmpty(ids)) {
			return 0;
		}
		String sql=" delete from "+Contact.tableName +"  where siteId = ? and  id in ("; 
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
	public int update(Map<String, Object> contact) {
		
		return sqlDao.updateMap(Contact.tableName, "id", contact);
	}

	@Override
	public int update(Contact contact) {
		 hqlDao.update(contact);
		 return 1;
	}

	@Override
	public Map<String, Object> load(Integer id) {
		String sql=" select c.* from " +Contact.tableName+ " c where c.id = ? ";
		return this.sqlDao.queryForMap(sql,id);
	}

	@Override
	public Contact get(Integer id) {
		return hqlDao.get(Contact.class, id);
	}

	@Override
	public List<Map<String, Object>> findContactList(Integer siteId) {
		String sql=" select c.* from "+Contact.tableName+"  c where c.siteId = ? order by orderSort asc ";
		return sqlDao.queryForList(sql, siteId);
	}

	@Override
	public int updateOrderSort(Integer id, Integer upOrdown) {
		int result=0;
		if (upOrdown==-1) {
			String sql="select c.id,c.orderSort from "+Contact.tableName+ " c "+
					" where c.siteId =(select t.siteId from "+Contact.tableName+" t where t.id= ? ) "+
					" and c.id <> ? and c.orderSort < (select t.orderSort from "+Contact.tableName+" t where t.id= ? ) "+
					" order by c.orderSort desc limit 1 ";
			Map<String, Object> nextMap=sqlDao.queryForMap(sql, id,id,id);
			if (nextMap!=null) {
				Integer nextOrder=Integer.parseInt(nextMap.get("orderSort")+"");
				nextMap.put("orderSort", this.get(id).getOrderSort());
				result=this.update(nextMap);
				if (result>0) {
					Map<String, Object> currentMap=new HashMap<String, Object>();
					currentMap.put("id", id);
					currentMap.put("orderSort", nextOrder);
					
					result=this.update(currentMap);
				}
			}
		}else if(upOrdown==1){
			String sql="select c.id,c.orderSort from "+Contact.tableName+ " c "+
					" where c.siteId =(select t.siteId from "+Contact.tableName+" t where t.id= ? ) "+
					" and c.id <> ? and c.orderSort > (select t.orderSort from "+Contact.tableName+" t where t.id= ? ) "+
					" order by c.orderSort asc limit 1 ";
			Map<String, Object> prevMap=sqlDao.queryForMap(sql, id,id,id);
			if (prevMap!=null) {
				Integer nextOrder=Integer.parseInt(prevMap.get("orderSort")+"");
				prevMap.put("orderSort", this.get(id).getOrderSort());
				result=this.update(prevMap);
				if (result>0) {
					Map<String, Object> currentMap=new HashMap<String, Object>();
					currentMap.put("id", id);
					currentMap.put("orderSort", nextOrder);
					result=this.update(currentMap);
				}
			}
		}
		return 1;
	}

	@Override
	public int getNewOrderSort(Integer siteId) {
		String sql=" select max(orderSort) from "+Contact.tableName+" where siteId = ? ";
		Integer maxOrder= sqlDao.queryForObject(sql, Integer.class,siteId);
		if (maxOrder==null) {
			maxOrder=1;
		}else{
			maxOrder++;
		}
		return maxOrder;
	}

}
