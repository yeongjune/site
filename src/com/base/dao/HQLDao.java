package com.base.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.type.Type;

import com.base.vo.PageList;

public interface HQLDao {

	/**
	 * 保存对象
	 * 
	 * @param obj 对象
	 * @return 返回主键
	 */
	Serializable save(Object obj);

	/**
	 * 保存或修改对象
	 * 
	 * @param obj 对象
	 */
	void saveOrUpdate(Object obj);

	/**
	 * 保存或修改对象集合
	 * 
	 * @param objCollection 对象集合
	 */
	void saveOrUpdateAll(Collection<?> objCollection);

	/**
	 * 删除对象
	 * 
	 * @param obj 对象
	 */
	void delete(Object obj);

	/**
	 * 根据ID 删除对象
	 * 
	 * @param c 类
	 * @param id 编号
	 */
	void delete(Class<?> c, Serializable id);

	/**
	 * 删除对象集合
	 * 
	 * @param objCollection 对象集合
	 */
	void deleteAll(Collection<?> objCollection);

	/**
	 * 修改对象
	 * 
	 * @param obj 对象
	 */
	void update(Object obj);

	/**
	 * 当hibernate 缓存有你需要修改的对象的时候，执行此方法
	 * 
	 * @param obj 对象
	 */
	void merge(Object obj);

	/**
	 * 根据ID 查询对象
	 * 
	 * @param id 编号
	 * @return 对象
	 */
	<T> T get(Class<T> c, Serializable id);
	
	/**
	 * 执行HQL 查询Object对象
	 * 
	 * @param hql 查询语句
	 * @param params 动态参数
	 * @return
	 */
	Object getObjectByHQL(String hql, Object... params);
	
	/**
	 * 执行SQL 查询object对象
	 * 
	 * @param sql 查询语句
	 * @param params 动态参数
	 * @return
	 */
	Object getObjectBySQL(String sql, Object... params);
	
	/**
	 * 执行HQL 查询列表
	 * 
	 * @param hql 查询语句
	 * @param params 动态参数
	 * @return
	 */
	List getListByHQL(String hql, Object...params);
	
	/**
	 * 执行HQL 查询列表Map
	 * 
	 * @param hql 查询语句
	 * @param params 动态参数
	 * @return
	 */
	List<Map<String, Object>> getListMapByHQL(String hql, Object... params);
	
	/**
	 * 执行SQL 查询列表Map
	 * 
	 * @param hql 查询语句
	 * @param params 动态参数
	 * @return
	 */
	List<Map<String, Object>> getListMapBySQL(String sql, Object... params);
	
	/**
	 * 执行HQL 根据游标起始值 按数量查询列表
	 * 
	 * @param hql 查询语句
	 * @param start 开始点
	 * @param pageSize 查询数量
	 * @param params 动态参数
	 * @return
	 */
	List getListByHQLAndSize(String hql, Integer start, Integer pageSize, Object... params);
	
	/**
	 * 执行HQL 根据游标起始值 按数量查询列表Map
	 * 
	 * @param hql 查询语句
	 * @param start 开始点
	 * @param pageSize 查询数量
	 * @param params 动态参数
	 * @return
	 */
	List<Map<String, Object>> getListMapByHQL(String hql, Integer start, Integer pageSize, Object... params);
	
	
	/**
	 * 执行SQL 根据游标起始值 按数量查询列表Map
	 * 
	 * @param sql 查询语句
	 * @param start 开始点
	 * @param pageSize 查询数量
	 * @param scalarMap 如果用到统计函数 别名则 需要增加类型指定
	 * @param entityMap 如果希望结果强转成model类型，可以指定对象
	 * @param params 动态参数
	 * @return
	 */
	List<Map<String, Object>> getListMapBySQL(
											String sql, 
											Integer start, 
											Integer pageSize, 
											final Map<String, Type> scalarMap,
											final Map<String, Class<?>> entityMap,
											Object... params);
	
	/**
	 * 执行HQL 查询分页列表数据 
	 * 
	 * @param hql 查询语句
	 * @param currentPage 当前页码
	 * @param pageSize 每页显示数量
	 * @param params 动态参数
	 * @return
	 */
	PageList getPageListByHQL(String hql, int currentPage, int pageSize, Object... params);
	
	/**
	 * 执行HQL 查询分页列表Map数据 
	 * 
	 * @param hql 查询语句
	 * @param currentPage 当前页码
	 * @param pageSize 每页显示数量
	 * @param params 动态参数
	 * @return
	 */
	PageList getPageListMapByHQL(String hql,int currentPage, int pageSize, Object... params);
	
	/**
	 * 执行SQL 查询分页列表Map数据 
	 * 
	 * @param sql 查询语句
	 * @param currentPage 当前页码
	 * @param pageSize 每页显示数量
	 * @param scalarMap 如果用到统计函数 别名则 需要增加类型指定
	 * @param entityMap 如果希望结果强转成model类型，可以指定对象
	 * @param params 动态参数
	 * @return
	 */
	PageList getPageListMapBySQL(
								String sql,  
								int currentPage, 
								int pageSize, 
								Map<String, Type> scalarMap,
								Map<String, Class<?>> entityMap,
								Object... params);
	
	/**
	 * 执行SQL修改语句
	 * 
	 * @param sql 修改语句
	 * @param params 动态参数
	 * @return
	 */
	int updateBySQL(String sql, Object... params);
	
	/**
	 * 执行HQL修改语句
	 * 
	 * @param hql 修改语句
	 * @param params 动态参数
	 * @return
	 */
	int updateByHQL(String hql, Object... params);
	
	/**
	 * 分页操作
	 * 
	 * @param hql hql查询list结果集语句
	 * @param countHQL hql查询总数
	 * @param currentPage 当前页
	 * @param pageSize 每页显示数量
	 * @param o 可变参数 用来填充hql，sql 条件
	 */
	PageList getListPageByHQL(final String hql, final String countHQL,final Integer currentPage, final Integer pageSize, final Object... o);
	
	/**
	 * 取得session
	 * @return
	 */
	Session get();
}
