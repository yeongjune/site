package com.base.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcOperations;

import com.base.vo.PageList;

public interface SQLDao extends JdbcOperations {

	/**
	 * table表中插入一条数据，字段名为map的key，字段值为map的value
	 * @param table 表名
	 * @param map
	 * @return
	 */
	Serializable save(String table, Map<String, Object> map);
	/**
	 * table表中批量数据，mapList中一个Map为一条记录，字段名为map的key，字段值为map的value
	 * @param table
	 * @param mapList
	 * @return
	 */
	int save(String table, List<Map<String, Object>> mapList);
	/**
	 * 删除table表中字段“id”等于id的记录
	 * @param table 表名
	 * @param id
	 * @return
	 */
	int delete(String table, Serializable id);
	/**
	 * 删除table表中column值等于value的记录
	 * @param table 表名
	 * @param column 字段名
	 * @param value 字段值
	 * @return
	 */
	int delete(String table, String column, Object value);
	/**
	 * 以primaryKey字段作为主键，修改table表对应记录，map的key（除主键）作为要更新的字段、对应的value作为字段新的值
	 * @param table
	 * @param primaryKey
	 * @param map
	 * @return
	 */
	int updateMap(String table, String primaryKey, Map<String, Object> map);
	/**
	 * 
	 * @param table
	 * @param map
	 * @param columns
	 * @return
	 */
	int updateMap(String table, Map<String, Object> map, String...columns);
	/**
	 * 以primaryKey字段作为主键，修改table表对应记录，map的key（除主键）作为要更新的字段、对应的value作为字段新的值
	 * @param table
	 * @param primaryKey
	 * @param mapList
	 * @return
	 */
	int updateMapList(String table, String primaryKey, List<Map<String, Object>> mapList);
	/**
	 * 
	 * @param table
	 * @param mapList
	 * @param column  查询条件
	 * @return
	 */
	int updateMapList(String table, List<Map<String, Object>> mapList,
			String...columns);
	/**
	 * 以primaryKey字段作为主键，保存或修改table表对应记录，map的key作为字段名、对应的value作为字段的值
	 * @param table
	 * @param primaryKey
	 * @param map
	 * @return
	 */
	int saveOrUpdate(String table, String primaryKey, Map<String, Object> map);
	/**
	 * 以primaryKey字段作为主键，保存或修改table表对应记录，map的key作为字段名、对应的value作为字段的值
	 * @param table
	 * @param primaryKey
	 * @param mapList
	 * @return
	 */
	int saveOrUpdate(String table, String primaryKey, List<Map<String, Object>> mapList);
	/**
	 * 分页查询
	 * @param sql
	 * @param currentPage
	 * @param pageSize
	 * @param args
	 * @return {currentPage,pageSize,totalPage,count,list=[]}
	 */
	Map<String, Object> getPageMap(String sql, Integer currentPage,
			Integer pageSize, Object... args);
	
	/**
	 * 分页查询
	 * @param sql
	 * @param currentPage
	 * @param pageSize
	 * @param args
	 * @return PageList对象
	 */
	PageList getPageList(String sql, Integer currentPage,Integer pageSize, Object... args);

    /**
     * 分页查询
     * @param sql
     * @param currentPage
     * @param pageSize
     * @param args
     * @return PageList对象
     */
    PageList getPageList(String countSql, String sql, Integer currentPage,Integer pageSize, Object... args);
}
