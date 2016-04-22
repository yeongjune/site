package com.site.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.base.util.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.base.dao.HQLDao;
import com.base.dao.SQLDao;
import com.base.util.StringUtil;
import com.site.dao.ColumnDao;
import com.site.model.Column;
import com.site.model.RoleColumn;
import com.site.model.UserColumnRole;

@Repository
public class ColumnDaoImpl implements ColumnDao {

	@Autowired
	private SQLDao dao;
	
	@Autowired
	private HQLDao hqlDao;

	@Override
	public Serializable save(Map<String, Object> map) {
		return dao.save(Column.tableName, map);
	}

	@Override
	public int delete(Integer id) {
		Map<String, Object> map = load(id);
		dao.update("update "+Column.tableName+" set pid=? where pid=?", map.get("pid"), id);
		return dao.delete(Column.tableName, id);
	}

	@Override
	public int update(Map<String, Object> map) {
		return dao.updateMap(Column.tableName, "id", map);
	}

	@Override
	public List<Map<String, Object>> getList(Integer siteId) {
		return dao.queryForList("select * from "+Column.tableName+" where siteId=?   order by pid, sort", siteId);
	}

	@Override
	public int update(List<Map<String, Object>> mapList) {
		return dao.updateMapList(Column.tableName, "id", mapList);
	}

	@Override
	public Map<String, Object> load(Integer id) {
		return dao.queryForMap("select * from "+Column.tableName+" where id=?", id);
	}

	@Override
	public List<Map<String, Object>> getYoungerBrothers(Integer id) {
		Map<String, Object> map = load(id);
		return dao.queryForList("select * from "+Column.tableName+" where pid=? and sort>?", map.get("pid"), map.get("sort"));
	}

	@Override
	public int deleteBySiteId(Integer siteId) {
		return dao.delete(Column.tableName, "siteId", siteId);
	}

	@Override
	public List<Map<String, Object>> selfAndParents(Integer siteId, Integer id) {
		List<Map<String, Object>> list = dao.queryForList("select * from "+Column.tableName+" where siteId=? ", siteId);
		if(list!=null && list.size()>0){
			Map<Object, Map<String, Object>> map = new HashMap<Object, Map<String,Object>>();
			for (Map<String, Object> column : list) {
				map.put(column.get("id"), column);
			}
			return selfAndParents(map, id);
		}
		return null;
	}

	private List<Map<String, Object>> selfAndParents(Map<Object, Map<String, Object>> map, Integer id) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(map!=null && !map.isEmpty()){
			Map<String, Object> column = map.get(id);
			if(column != null && column.get("pid")!=null && !column.get("pid").equals(0)){
				list.addAll(selfAndParents(map, (Integer) column.get("pid")));
			}
			list.add(column);
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> navigationWithChildren(Integer siteId) {
		List<Map<String, Object>> navigationColumn = dao.queryForList("select * from "+Column.tableName+" where siteId=? and navigation=1  order by sort asc ", siteId);
		if(navigationColumn==null || navigationColumn.isEmpty())return null;
		Map<Integer, List<Map<String, Object>>> mapListMap = assembleChildren(navigationColumn);
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> map : navigationColumn) {
			if(map==null || map.isEmpty())continue;
			if(map.get("pid")==null || map.get("pid").equals(0)){
				map.put("children", mapListMap.get(map.get("id")));
				list.add(map);
			}
		}
		return list;
	}

	@Override
	public Map<String, Object> selfWithChildren(Integer siteId, int id) {
		Map<String, Object> map = dao.queryForMap("select * from "+Column.tableName+" where siteId=? and id=?", siteId, id);
		if(map==null || map.isEmpty())return null;
		List<Map<String, Object>> navigationColumn = dao.queryForList("select * from "+Column.tableName+" where siteId=?   order by sort asc ", siteId);
		Map<Integer, List<Map<String, Object>>> mapListMap = assembleChildren(navigationColumn);
		//System.out.println(mapListMap);
		map.put("children", mapListMap.get(id));
		return map;
	}

	private Map<Integer, List<Map<String, Object>>> assembleChildren(List<Map<String, Object>> list) {
		Map<Integer, List<Map<String, Object>>> mapListMap = new HashMap<Integer, List<Map<String,Object>>>();
		for (Map<String, Object> map : list) {
			if(map==null || map.isEmpty())continue;
			List<Map<String, Object>> mapList = mapListMap.get(map.get("pid"));
			if(mapList==null){
				mapList = new ArrayList<Map<String,Object>>();
				mapListMap.put((Integer) map.get("pid"), mapList);
			}
			mapList.add(map);
		}
		for (Map<String, Object> map : list) {
			if(map==null || map.isEmpty())continue;
			map.put("children", mapListMap.get(map.get("id")));
		}
		return mapListMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getListByIds(String ids) {
		String hql = "SELECT column.name FROM " + Column.modelName +  " AS column WHERE column.id in (" + ids + ")";
		return hqlDao.getListByHQL(hql);
	}

	@Override
	public List<Map<String, Object>> getList(Integer siteId, Integer userId) {
		String sql = 
			"select " +
			"	c.* " +
			"from " +
			"	"+Column.tableName+" as c, " +
			"	"+RoleColumn.tableName+" as rc, " +
			"	"+UserColumnRole.tableName+" as ucr " +
			"where " +
			"	c.siteId=? " +
			"	and ucr.userId=? " +
			"	and ucr.checked=1 " +
			"	and rc.checked=1 " +
			"	and ucr.roleId=rc.roleId " +
			"	and rc.columnId=c.id " +
			"order by c.pid, c.sort";
		return dao.queryForList(sql, siteId, userId);
	}

	@Override
	public List<Integer> getSelfAndAllChildrenId(int id) {
		if(id <= 0 ) return new ArrayList<>();
		String sql = "SELECT DISTINCT sc2.* FROM site_column sc1, site_column sc2 " +
				"WHERE sc1.id = ? AND sc1.siteId = sc2.siteId ORDER BY sc2.sort";
		List<Map<String, Object>> columnList = dao.queryForList(sql, id);
		Map<Object, Map<String, Object>> columnMap = ListUtils.classifyMapListByMap("id", columnList);
		for (Map<String, Object> column : columnList) {
			Map<String, Object> map = columnMap.get(column.get("pid"));
			if(map == null ) continue;
			List<Integer> childrenIdList = (List<Integer>) map.get("childrenIdList");
			if(childrenIdList == null){
				childrenIdList = new ArrayList<>();
				map.put("childrenIdList", childrenIdList);
			}
			childrenIdList.add((Integer) column.get("id"));
		}
		List<Integer> childrenIdList = new ArrayList<>();
		parseIdList(childrenIdList, columnMap, id);
		childrenIdList.add(id);

		/*String sqlTemp=" select getColumnChildList( ? ) as columnIds ";//getColumnChildList为数据库函数，返回包括该栏目和子栏目的ID
		Map<String, Object> idsMap=dao.queryForMap(sqlTemp,id);
		String columnIds=idsMap==null?"":idsMap.get("columnIds")+"";
		List<Integer> result=new ArrayList<Integer>();
		if (!StringUtil.isEmpty(columnIds)) {
		  List<String> tmpList=Arrays.asList(columnIds.split(","));
		  for (String columnId : tmpList) {
			  result.add(Integer.parseInt(columnId));
		  }
		}*/
		return childrenIdList;
	}
	
	private void parseIdList(List<Integer> childrenIdList, Map<Object, Map<String, Object>> columnMap, Integer id){
		Map<String, Object> c = columnMap.get(id);
		List<Integer> cIdList = (List<Integer>) c.get("childrenIdList");
		if(cIdList != null){
			childrenIdList.addAll(cIdList);
			for (Integer cId : cIdList)
				parseIdList(childrenIdList, columnMap, cId);
		}
	}
	
	@Override
	public int getVoteNum(Integer columnId){
		String sql = "SELECT voteNum FROM " + Column.tableName + " WHERE id = ? ";
		return dao.queryForInt(sql, columnId);
	}
	
	@Override
	public int updateVoteNum(Integer columnId, Integer voteNum){
		String sql = " UPDATE " + Column.tableName + " SET voteNum = ? WHERE id = ?";
		return dao.update(sql, voteNum, columnId);
	}

	@Override
	public Map<String, Object> loadParent(Integer id) {
		String sql = "SELECT * FROM " + Column.tableName + " WHERE id = (SELECT pid FROM " + Column.tableName + " WHERE id = ?);";
		return dao.queryForMap(sql, id);
	}
}
