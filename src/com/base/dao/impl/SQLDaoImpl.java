package com.base.dao.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.base.dao.SQLDao;
import com.base.vo.PageList;

@Repository
public class SQLDaoImpl extends JdbcTemplate implements SQLDao {

	@Resource(name = "dataSource")
	public void setDataSource(DataSource dataSource){
		super.setDataSource(dataSource);
	}

	@Override
	public Map<String, Object> queryForMap(String sql, Object... args)
			throws DataAccessException {
		List<Map<String, Object>> list = queryForList(sql, args);
		if(list!=null && list.size()>0)return list.get(0);
		return null;
	}
	@Override
	public Map<String, Object> queryForMap(String sql)
			throws DataAccessException {
		List<Map<String, Object>> list = queryForList(sql);
		if(list!=null && list.size()>0)return list.get(0);
		return null;
	}
	@Override
	public <T> T queryForObject(String sql, Class<T> requiredType,
			Object... args) throws DataAccessException {
		List<T> list = queryForList(sql, requiredType, args);
		if(list!=null && list.size()>0)return list.get(0);
		return null;
	}
	@Override
	public <T> T queryForObject(String sql, Class<T> requiredType)
			throws DataAccessException {
		List<T> list = queryForList(sql, requiredType);
		if(list!=null && list.size()>0)return list.get(0);
		return null;
	}
	@Override
	public int queryForInt(String sql, Object... args)
	throws DataAccessException {
		Integer result = this.queryForObject(sql, Integer.class, args);
		if(result==null)return 0;
		return result;
	}
	@Override
	public int queryForInt(String sql) throws DataAccessException {
		Integer result = this.queryForObject(sql, Integer.class);
		if(result==null)return 0;
		return result;
	}
	@Override
	public long queryForLong(String sql, Object... args)
			throws DataAccessException {
		Long result = this.queryForObject(sql, Long.class, args);
		if(result==null)return 0;
		return result;
	}
	@Override
	public long queryForLong(String sql) throws DataAccessException {
		Long result = this.queryForObject(sql, Long.class);
		if(result==null)return 0;
		return result;
	}

	@Override
	public Serializable save(final String table, final Map<String, Object> map) {
		if(table==null || table.toString().trim().equals("") || map==null || map.isEmpty())return null;
		KeyHolder keyHolder = new GeneratedKeyHolder();
		update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection conn)
					throws SQLException {
				StringBuffer fieldsBuffer = new StringBuffer();
				StringBuffer valuesBuffer = new StringBuffer();
				List<Object> list = new ArrayList<Object>();
				for (String field : map.keySet()) {
					if(field!=null && !field.toString().trim().equals("")){
						if(fieldsBuffer.toString().equals("")){
							fieldsBuffer.append(field);
							valuesBuffer.append("?");
						}else{
							fieldsBuffer.append(",");
							fieldsBuffer.append(field);
							valuesBuffer.append(",?");
						}
						list.add(map.get(field));
					}
				}
				if(fieldsBuffer.toString().equals(""))return null;
				String sql = "insert into "+table+"("+fieldsBuffer.toString()+") values("+valuesBuffer.toString()+")";
//				int gk = map.get("id")!=null?Statement.RETURN_GENERATED_KEYS:Statement.NO_GENERATED_KEYS;
				PreparedStatement pstat = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				for (int i = 0; i < list.size(); i++) {
					Object x = list.get(i);
					if(x instanceof Integer){
						pstat.setInt(i+1, (Integer) x);
					}else if(x instanceof Long){
						pstat.setLong(i+1, (Long) x);
					}else if(x instanceof Double){
						pstat.setDouble(i+1, (Double) x);
					}else if(x instanceof Float){
						pstat.setFloat(i+1, (Float) x);
					}else if(x instanceof Float){
						pstat.setShort(i+1, (Short) x);
					}else if(x instanceof String){
						pstat.setString(i+1, (String) x);
					}else if(x instanceof Date){
						java.util.Date d = (java.util.Date) x;
						pstat.setTimestamp(i+1, new Timestamp(d.getTime()));
					}else if(x instanceof java.sql.Date){
						pstat.setDate(i+1, (java.sql.Date) x);
					}else if(x instanceof Timestamp){
						pstat.setTimestamp(i+1, (Timestamp) x);
					}else if(x instanceof Boolean){
						pstat.setBoolean(i+1, (Boolean) x);
					}else if(x instanceof Byte){
						pstat.setByte(i+1, (Byte) x);
					}else{
						pstat.setObject(i+1, x);
					}
				}
				return pstat;
			}
		}, keyHolder);
		Number n = keyHolder.getKey();
		if(n==null)return (Serializable) map.get("id");
		return keyHolder.getKey().intValue();
	}

	@Override
	public int save(String table, final List<Map<String, Object>> mapList) {
		long start =System.currentTimeMillis();
		if(table==null || mapList==null || mapList.isEmpty())return 0;
		Set<String> keys = new HashSet<String>();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for (Map<String,Object> map : mapList) {
			if(map==null || map.isEmpty())continue;
			keys.addAll(map.keySet());
			list.add(map);
		}
		final List<String> keyList = new ArrayList<String>(keys);
		String columns = "";
		String values = "";
		for (String key : keyList) {
			if(columns.equals("")){
				columns += key;
				values += "?";
			}else{
				columns += ","+key;
				values += ",?";
			}
		}
		
		String sql = "insert into "+table+"("+columns+") values ";
		List<Object> paramList = new ArrayList<Object>();
		for (Map<String, Object> map : mapList) {
			if(map==null || map.isEmpty())continue;
			if(!sql.equals("insert into "+table+"("+columns+") values "))sql+=",";
			sql += "("+values+") ";
			for (String key : keyList) {
				paramList.add(map.get(key));
			}
		}
		int sum = update(sql, paramList.toArray());
		System.out.println(System.currentTimeMillis()-start);
		return sum;
	}

	@Override
	public int delete(String table, Serializable id) {
		return update("delete from "+table+" where id=?", id);
	}

	@Override
	public int delete(String table, String column, Object value) {
		return update("delete from "+table+" where "+column+"=?", value);
	}

	@Override
	public int updateMap(String table, String primaryKey,
			Map<String, Object> map) {
		if(table==null || primaryKey==null || map==null || map.isEmpty())return 0;
		int index = 0;
		Object[] array = new Object[map.size()];
		String setString = "";
		for (String key : map.keySet()) {
			if(key.equals(primaryKey))continue;
			if(setString.equals("")){
				setString += key+"=?";
			}else{
				setString += ","+key+"=?";
			}
			array[index] = map.get(key);
			index++;
		}
		array[index] = map.get(primaryKey);
		return update("update "+table+" set "+setString+" where "+primaryKey+"=?", array);
	}

	@Override
	public int updateMap(String table, Map<String, Object> map,
			String... columns) {
		if(table==null || columns==null || columns.length<=0 || map==null || map.isEmpty())return 0;
		Set<String> columnSet = new HashSet<String>();
		for (int i = 0; i < columns.length; i++) {
			columnSet.add(columns[i]);
		}
		if(map.keySet().containsAll(columnSet)){
			List<Object> setList = new ArrayList<Object>();
			List<Object> whereList = new ArrayList<Object>();
			String setString = "";
			String whereString = "";
			for (String key : map.keySet()) {
				if(columnSet.contains(key)){
					if(whereString.equals("")){
						whereString += key+"=?";
					}else{
						whereString += " and "+key+"=?";
					}
					whereList.add(map.get(key));
				}else{
					if(setString.equals("")){
						setString += key+"=?";
					}else{
						setString += ","+key+"=?";
					}
					setList.add(map.get(key));
				}
			}
			setList.addAll(whereList);
			if(setList.size()>0){
				Object[] array = new Object[setList.size()];
				for (int i = 0; i < setList.size(); i++) {
					array[i] = setList.get(i);
				}
				return update("update "+table+" set "+setString+" where "+whereString, array);
			}
		}
		return 0;
	}

	@Override
	public int updateMapList(String table, String primaryKey,
			List<Map<String, Object>> mapList) {
		if(table==null || primaryKey==null || mapList==null || mapList.isEmpty())return 0;
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> map : mapList) {
			if(map==null || map.isEmpty())continue;
			list.add(map);
		}
		if(list.size()>0){
			int sum = 0;
			for (int i = 0; i < list.size(); i ++) {
				Map<String,Object> map = list.get(i);
				sum += updateMap(table, primaryKey, map);
			}
			return sum;
		}
		return 0;
	}

	@Override
	public int updateMapList(String table,
			List<Map<String, Object>> mapList, String... columns) {
		if(table==null || columns==null || columns.length<=0 || mapList==null || mapList.isEmpty())return 0;
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> map : mapList) {
			if(map==null || map.isEmpty())continue;
			list.add(map);
		}
		if(list.size()>0){
			int sum = 0;
			for (int i = 0; i < list.size(); i ++) {
				Map<String,Object> map = list.get(i);
				sum += updateMap(table, map, columns);
			}
			return sum;
		}
		return 0;
	}

	@Override
	public int saveOrUpdate(String table, String primaryKey,
			Map<String, Object> map) {
		long count = queryForLong("select count(*) from "+table+" where "+primaryKey+"=?", map.get(primaryKey));
		if(count>0){
			return updateMap(table, primaryKey, map);
		}else{
			save(table, map);
			return 1;
		}
	}

	@Override
	public int saveOrUpdate(String table, String primaryKey,
			List<Map<String, Object>> mapList) {
		if(table==null || primaryKey==null || mapList==null || mapList.isEmpty())return 0;
		List<Object> idList = queryForList("select "+primaryKey+" from "+table, Object.class);
		List<Map<String, Object>> addList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> updateList = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> map : mapList) {
			if(map==null || map.isEmpty())continue;
			if(idList.contains(map.get(primaryKey))){
				updateList.add(map);
			}else{
				addList.add(map);
			}
		}
		int sum = 0;
		if(updateList.size()>0){
			sum += updateMapList(table, primaryKey, updateList);
		}
		if(addList.size()>0){
			sum += save(table, addList);
		}
		return sum;
	}

	@Override
	public Map<String, Object> getPageMap(String sql, Integer currentPage,
			Integer pageSize, Object... args) {
		String countSql = null;
		countSql = "select count(*) from ("+sql+") as alias_name";
		long totalSize = queryForLong(countSql, args);
		Integer totalPage = 0;
		if(totalSize%pageSize==0){
			totalPage = (int) (totalSize/pageSize);
		}else{
			totalPage = (int) (totalSize/pageSize+1);
		}
		currentPage = currentPage>totalPage?1:currentPage;
		sql = sql+" limit "+(currentPage-1)*pageSize+","+pageSize;
		List<Map<String, Object>> list = queryForList(sql, args);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("currentPage", currentPage);
		map.put("pageSize", pageSize);
		map.put("totalPage", totalPage);
		map.put("totalSize", totalSize);
		map.put("list", list);
		return map;
	}

	@Override
	public PageList getPageList(String sql, Integer currentPage,
			Integer pageSize, Object... args) {
		if(pageSize==null){
			pageSize=10;
		}
		if(currentPage==null){
			currentPage=1;
		}
		String countSql = null;
		countSql = "select count(*) from ("+sql+") as alias_name";
		long totalSize = queryForLong(countSql, args);
		Integer totalPage = 0;
		if(totalSize%pageSize==0){
			totalPage = (int) (totalSize/pageSize);
		}else{
			totalPage = (int) (totalSize/pageSize+1);
		}
		currentPage = currentPage>totalPage?1:currentPage;
		sql = sql+" limit "+(currentPage-1)*pageSize+","+pageSize;
		List<Map<String, Object>> list = queryForList(sql, args);
		PageList pageList = new PageList(currentPage,
				totalSize, pageSize);
		pageList.setList(list);
		return pageList;
	}

    @Override
    public PageList getPageList(String countSql, String sql, Integer currentPage, Integer pageSize, Object... args) {
        if(pageSize==null){
            pageSize=10;
        }
        if(currentPage==null){
            currentPage=1;
        }
        long totalSize = queryForLong(countSql, args);
        Integer totalPage;
        if(totalSize%pageSize==0){
            totalPage = (int) (totalSize/pageSize);
        }else{
            totalPage = (int) (totalSize/pageSize+1);
        }
        currentPage = currentPage>totalPage?1:currentPage;
        sql = sql+" limit "+(currentPage-1)*pageSize+","+pageSize;
        List<Map<String, Object>> list = queryForList(sql, args);
        PageList pageList = new PageList(currentPage,
                totalSize, pageSize);
        pageList.setList(list);
        return pageList;
    }

}
