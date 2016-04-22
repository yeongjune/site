package com.base.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.Type;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.base.dao.HQLDao;
import com.base.vo.PageList;

/**
 * 所有的Dao类都继承这个子类 实现了基本的增删改查
 * 
 * @author lostself
 * 
 */
@Repository
public class HQLDaoImpl extends HibernateDaoSupport implements HQLDao{

	/**
	 * 注入数据源
	 * 
	 * @param sessionFactory
	 */
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public Serializable save(Object obj) {
		return this.getHibernateTemplate().save(obj);
	}

	@Override
	public void saveOrUpdate(Object obj) {
		this.getHibernateTemplate().saveOrUpdate(obj);
	}

	@Override
	public void saveOrUpdateAll(Collection<?> objCollection) {
		this.getHibernateTemplate().saveOrUpdateAll(objCollection);
	}

	@Override
	public void delete(Object obj) {
		this.getHibernateTemplate().delete(obj);
	}

	@Override
	public void delete(Class<?> c, Serializable id) {
		this.getHibernateTemplate().delete(this.get(c, id));
	}

	@Override
	public void deleteAll(Collection<?> objCollection) {
		this.getHibernateTemplate().deleteAll(objCollection);
	}


	@Override
	public void update(Object obj) {
		this.getHibernateTemplate().update(obj);
	}
	
	@Override
	public void merge(Object obj) {
		this.getHibernateTemplate().merge(obj);
	}

	
	@Override
	public <T> T get(Class<T> c, Serializable id) {
		return this.getHibernateTemplate().get(c, id);
	}

	@Override
	public List getListByHQL(String hql, Object... params) {
		return this.getHibernateTemplate().find(hql, params);
	}

	@Override
	public List getListByHQLAndSize(final String hql, final Integer start, final Integer pageSize, final Object... params) {
		
		return this.getHibernateTemplate().execute(new HibernateCallback<List>() {
			@Override
			public List doInHibernate(Session session)throws HibernateException, SQLException {
				
				Query query = session.createQuery(hql);
				// 填充sql 语句参数
				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						query.setParameter(i, params[i]);
					}
				}
				query.setFirstResult(start);
				query.setMaxResults(pageSize);
				List list = query.list();
				return list;
			}
		});
	}

	@Override
	public List<Map<String, Object>> getListMapByHQL(final String hql, final Object... params) {
		
		return this.getHibernateTemplate().execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session session)throws HibernateException, SQLException {
				
				Query query = session.createQuery(hql);
				// 设置返回Map 格式
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				// 填充sql 语句参数
				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						query.setParameter(i, params[i]);
					}
				}
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> list = query.list();
				return list;
			}
		});
	}

	@Override
	public List<Map<String, Object>> getListMapByHQL(final String hql, final Integer start, final Integer pageSize, final Object... params) {
		
		return this.getHibernateTemplate().execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session session)throws HibernateException, SQLException {
				
				Query query = session.createQuery(hql);
				// 设置返回Map 格式
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				// 填充sql 语句参数
				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						query.setParameter(i, params[i]);
					}
				}
				query.setFirstResult(start);
				query.setMaxResults(pageSize);
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> list = query.list();
				return list;
			}
		});
	}

	@Override
	public List<Map<String, Object>> getListMapBySQL(
													final String sql, 
													final Integer start, 
													final Integer pageSize, 
													final Map<String, Type> scalarMap,
													final Map<String, Class<?>> entityMap,
													final Object... params) {
		
		return this.getHibernateTemplate().execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session session)throws HibernateException, SQLException {
				
				SQLQuery sqlQuery = session.createSQLQuery(sql);
				// 设置返回Map 格式
				sqlQuery.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				// 填充sql 语句参数
				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						sqlQuery.setParameter(i, params[i]);
					}
				}
				// 用到统计函数需要对别名指定类型
				if (scalarMap != null) {
					for (String key : scalarMap.keySet()) {
						sqlQuery.addScalar(key, scalarMap.get(key));
					}
				}
				// 是否需要转换回 Model 实体
				if (entityMap != null) {
					for (String key : entityMap.keySet()) {
						sqlQuery.addEntity(key, entityMap.get(key));
					}
				}
				sqlQuery.setFirstResult(start);
				sqlQuery.setMaxResults(pageSize);
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> list = sqlQuery.list();
				return list;
			}
		});
	}

	@Override
	public List<Map<String, Object>> getListMapBySQL(final String sql, final Object... params) {
		return this.getHibernateTemplate().execute(new HibernateCallback<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> doInHibernate(Session session)throws HibernateException, SQLException {
				
				Query query = session.createSQLQuery(sql);
				// 设置返回Map 格式
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				// 填充sql 语句参数
				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						query.setParameter(i, params[i]);
					}
				}
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> list = query.list();
				return list;
			}
		});
	}

	@Override
	public Object getObjectByHQL(String hql, Object... params) {
		List<?> list = new ArrayList<Object>();
		if (params != null) {
			list = this.getHibernateTemplate().find(hql, params);
		} else {
			list = this.getHibernateTemplate().find(hql);
		}
		for (Object object : list) {
			return object;
		}
		return null;
	}

	@Override
	public Object getObjectBySQL(final String sql, final Object... params) {
		return this.getHibernateTemplate().execute(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session)throws HibernateException, SQLException {
				
				Query query = session.createSQLQuery(sql);
				// 填充sql 语句参数
				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						query.setParameter(i, params[i]);
					}
				}
				List<?> list = query.list();
				for (Object object : list) {
					return object;
				}
				return null;
			}
		});
	}

	@Override
	public PageList getPageListByHQL(final String hql, final int currentPage, final int pageSize, final Object... params) {
		return (PageList) this.getHibernateTemplate().execute( 
				new HibernateCallback<PageList>() {
					public PageList doInHibernate(Session session)
							throws HibernateException, SQLException {

						int firstRow = (currentPage - 1) * pageSize;
						Query query = session.createQuery(hql);
						String countHQL ="SELECT count(*) FROM " + hql.replaceFirst(".*?FROM", "").replaceFirst(".*?from", "");
						Query qcount = session.createQuery(countHQL);
						if (params != null) {
							for (int i = 0; i < params.length; i++) {
								query.setParameter(i, params[i]);
								qcount.setParameter(i, params[i]);
							}
						}
						query.setFirstResult(firstRow);
						query.setMaxResults(pageSize);
						List<?> list = new ArrayList<Object>();

						// 查询总记录数
						Long totalSize = (Long) qcount.uniqueResult();

						// 判断是当前页是否超出总页数
						int countPage = (int) (totalSize / pageSize);
						if (totalSize % pageSize != 0) {
							countPage++;
						}

						if (currentPage > countPage) {
							PageList pageList = new PageList(countPage, totalSize, pageSize);
							pageList.setList(list);
							return pageList;
						}

						list = query.list();
						PageList pageList = new PageList(currentPage, totalSize, pageSize);
						pageList.setList(list);

						return pageList;
					}
				});
	}

	@Override
	public PageList getPageListMapByHQL(final String hql, final int currentPage, final int pageSize, final Object... params) {
		return (PageList) this.getHibernateTemplate().execute( 
				new HibernateCallback<PageList>() {
					public PageList doInHibernate(Session session)
							throws HibernateException, SQLException {
						
						int firstRow = (currentPage - 1) * pageSize;
						Query query = session.createQuery(hql);
						String countHQL ="SELECT count(*) FROM " + hql.replaceFirst(".*?FROM", "").replaceFirst(".*?from", "");
						Query qcount = session.createQuery(countHQL);
						if (params != null) {
							for (int i = 0; i < params.length; i++) {
								query.setParameter(i, params[i]);
								qcount.setParameter(i, params[i]);
							}
						}
						query.setFirstResult(firstRow);
						query.setMaxResults(pageSize);
						query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
						List<?> list = new ArrayList<Object>();

						// 查询总记录数
						Long totalSize = (Long) qcount.uniqueResult();
						// 判断是当前页是否超出总页数
						int countPage = (int) (totalSize / pageSize);
						if (totalSize % pageSize != 0) {
							countPage++;
						}
						System.out.println(currentPage);
						if (currentPage > countPage) {
							PageList pageList = new PageList(countPage, totalSize, pageSize);
							query.setFirstResult((countPage - 1) * pageSize);
							list = query.list();
							pageList.setList(list);
							return pageList;
						}

						list = query.list();
						PageList pageList = new PageList(currentPage, totalSize, pageSize);
						pageList.setList(list);

						return pageList;
					}
				});
	}

	@Override
	public PageList getPageListMapBySQL(
										final String sql, 
										final int currentPage, 
										final int pageSize,
										final Map<String, Type> scalarMap,
										final Map<String, Class<?>> entityMap,
										final Object... params) {
		
		return (PageList) this.getHibernateTemplate().execute( 
				new HibernateCallback<PageList>() {
					public PageList doInHibernate(Session session)
							throws HibernateException, SQLException {

						int firstRow = (currentPage - 1) * pageSize;
						SQLQuery sqlQuery = session.createSQLQuery(sql);
						String countHQL ="SELECT count(*) FROM " + sql.replaceFirst(".*?FROM", "").replaceFirst(".*?from", "");
						Query qcount = session.createQuery(countHQL);
						if (params != null) {
							for (int i = 0; i < params.length; i++) {
								sqlQuery.setParameter(i, params[i]);
								qcount.setParameter(i, params[i]);
							}
						}
						sqlQuery.setFirstResult(firstRow);
						sqlQuery.setMaxResults(pageSize);
						sqlQuery.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
						
						// 用到统计函数需要对别名指定类型
						if (scalarMap != null) {
							for (String key : scalarMap.keySet()) {
								sqlQuery.addScalar(key, scalarMap.get(key));
							}
						}
						// 是否需要转换回 Model 实体
						if (entityMap != null) {
							for (String key : entityMap.keySet()) {
								sqlQuery.addEntity(key, entityMap.get(key));
							}
						}
						
						List<?> list = new ArrayList<Object>();

						// 查询总记录数
						Long totalSize = (Long) qcount.uniqueResult();

						// 判断是当前页是否超出总页数
						int countPage = (int) (totalSize / pageSize);
						if (totalSize % pageSize != 0) {
							countPage++;
						}

						if (currentPage > countPage) {
							PageList pageList = new PageList(countPage, totalSize, pageSize);
							pageList.setList(list);
							return pageList;
						}

						list = sqlQuery.list();
						PageList pageList = new PageList(currentPage, totalSize, pageSize);
						pageList.setList(list);

						return pageList;
					}
				});
	}

	@Override
	public int updateBySQL(final String sql, final Object... params) {

		return this.getHibernateTemplate().executeWithNewSession(
				new HibernateCallback<Integer>() {

					public Integer doInHibernate(Session session) throws HibernateException, SQLException {
						SQLQuery sqlQuery = session.createSQLQuery(sql);
						for (int i = 0; i < params.length; i++) {
							sqlQuery.setParameter(i, params[i]);
						}
						return sqlQuery.executeUpdate();
					}
				});

	}

	@Override
	public int updateByHQL(final String hql, final Object... params) {
		return this.getHibernateTemplate().executeWithNewSession(
				new HibernateCallback<Integer>() {
					public Integer doInHibernate(Session session) throws HibernateException, SQLException {
						Query query = session.createQuery(hql);
						for (int i = 0; i < params.length; i++) {
							query.setParameter(i, params[i]);
						}
						return query.executeUpdate();
					}
				});

	}
	
	/**
	 * 分页操作
	 * 
	 * @param hql hql查询list结果集语句
	 * @param countHQL hql查询总数
	 * @param currentPage 当前页
	 * @param pageSize 每页显示数量
	 * @param o 可变参数 用来填充hql，sql 条件
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PageList getListPageByHQL(final String hql, final String countHQL,
			final Integer currentPage, final Integer pageSize, final Object... o) {
		return (PageList) this.getHibernateTemplate().execute( 
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {

						int firstRow = (currentPage - 1) * pageSize;
						Query q = session.createQuery(hql);
						Query qcount = session.createQuery(countHQL);
						if (o != null) {
							for (int i = 0; i < o.length; i++) {
								q.setParameter(i, o[i]);
								qcount.setParameter(i, o[i]);
							}
						}
						q.setFirstResult(firstRow);
						q.setMaxResults(pageSize);
						List list = new ArrayList();

						// 查询总记录数
						Long totalSize = (Long) qcount.uniqueResult();

						// 判断是当前页是否超出总页数
						int countPage = (int) (totalSize / pageSize);
						if (totalSize % pageSize != 0) {
							countPage++;
						}

						if (currentPage > countPage) {
							PageList pageList = new PageList(countPage,
									totalSize, pageSize);
							pageList.setList(list);

							return pageList;
						}

						list = q.list();
						PageList pageList = new PageList(currentPage,
								totalSize, pageSize);
						pageList.setList(list);

						return pageList;
					}
				});
	}

	@Override
	public Session get() {
		return this.getSession();
	}


}
