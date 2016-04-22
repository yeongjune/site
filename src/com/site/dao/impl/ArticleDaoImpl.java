package com.site.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.site.dao.ColumnDao;
import com.site.model.*;
import org.hibernate.annotations.SQLUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.authority.model.User;
import com.base.dao.HQLDao;
import com.base.dao.SQLDao;
import com.base.util.StringUtil;
import com.base.vo.PageList;
import com.site.dao.ArticleDao;
import com.site.vo.ArticleSearchVo;

@Repository
public class ArticleDaoImpl implements ArticleDao {

	@Autowired
	private SQLDao sqlDao;
	
	@Autowired
	private HQLDao hqlDao;

	@Autowired
	private ColumnDao columnDao;

	@Override
	public List<Map<String, Object>> getList(Integer columnId, Integer size,
			Integer sortType, Integer flag) {
		String sql = "select * from "+Article.tableName+" where columnId=?";
		if(flag>0){
			sql += " and recommend=1 ";
		}
		sortType = sortType==null?0:sortType;
		switch (sortType) {
		case 1://按发布时间先后排序
			sql += " order by createTime";
			break;
		case 2://按点击量从高到低排序
			sql += " order by clickCount desc";
			break;
			
		default://按发布时间倒序，最新发布的在最前面
			sql += " order by createTime desc";
			break;
		}
		sql += " limit 0,? ";
		return sqlDao.queryForList(sql, columnId, size);
	}

	@Override
	public PageList getPageList(Integer columnId,
			Integer currentPage, Integer pageSize, Integer sortType, Integer flag) {
		String hql = ""
				+ " FROM"
					+ " Article AS article"
				+ " WHERE"
					+ " article.columnId = ?";
		if(flag>0){
			hql += " and recommend=1 ";
		}
		sortType = sortType==null?0:sortType;
		switch (sortType) {
		case 1://按发布时间先后排序
			hql += " order by createTime";
			break;
		case 2://按点击量从高到低排序
			hql += " order by clickCount desc";
			break;

		default://按发布时间倒序，最新发布的在最前面
			hql += " order by createTime desc";
			break;
		}
		return hqlDao.getPageListByHQL(hql, currentPage, pageSize, columnId);
	}

	@Override
	public Serializable save(Map<String, Object> article) {
		Serializable id=sqlDao.save(Article.tableName, article);
		 return id;
	}

	@Override
	public int delete(String ids) {
		if (StringUtil.isEmpty(ids)) {
			return 0;
		}
		String sql=" delete from "+Article.tableName +"  where id in ("; 
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
		return sqlDao.update(sql, param.toArray());
	}

	@Override
	public int update(Map<String, Object> article) {
		return  sqlDao.updateMap(Article.tableName, "id", article);
	}

	@Override
	public List<Map<String, Object>> findArticleList(ArticleSearchVo searchVo) {
		String sql=" select a.*,u.name,u.account,c.type columnType,c.name columnName,c.alias,r.name recommendName from " + Article.tableName + " as a " +
				" LEFT JOIN "+ User.tableName +" as u on a.userId=u.id "+
				" LEFT JOIN "+ Column.tableName +" as c on a.columnId=c.id "+
				" LEFT JOIN "+ ArticleRecommend.tableName + " as ar on a.id=ar.articleId "+
				" LEFT JOIN "+ Recommend.tableName + " as r on r.id=ar.recommendId "+
				" where (a.checked = 0 or a.checked = 11) ";
		
		List<Object> params=new ArrayList<Object>();
		
		//处理查询条件
		if (searchVo!=null) {
			if(searchVo.isImage()){
				sql += " AND a.smallPicUrl IS NOT NULL AND LENGTH(TRIM(a.smallPicUrl)) > 0 ";
			}
			if (searchVo.getSiteId()!=null&&searchVo.getSiteId()>0) {
				sql+=" and a.siteId= ? ";
				params.add(searchVo.getSiteId());
			}
			//查询单个栏目的的文章
			if (searchVo.getColumnId()!=null&&searchVo.getColumnId()>0) {
				if (searchVo.isIncludeSub()) {
					//以下为先查出栏目ID以及所有子栏目ID
					/*String sqlTemp=" select getColumnChildList( ? ) as columnIds ";//getColumnChildList为数据库函数，返回包括该栏目和子栏目的ID
                    Map<String, Object> idsMap=sqlDao.queryForMap(sqlTemp,searchVo.getColumnId());
                    String columnIds=idsMap==null?"":idsMap.get("columnIds")+"";*/
					
					List<Integer> columnIdList = columnDao.getSelfAndAllChildrenId(searchVo.getColumnId());
					String columnIds = "";
					for (Integer columnId : columnIdList) {
						if(StringUtil.isNotEmpty(columnIds)) columnIds += ",";
						columnIds += columnId;
					}
					if (StringUtil.isNotEmpty(columnIds))
						sql+=" and a.columnId in ( "+columnIds+") ";
				}else{
					sql+=" and a.columnId= ? ";
					params.add(searchVo.getColumnId());
				}
			}
			//查询多个栏目的文章
			if (!StringUtil.isEmpty(searchVo.getColumnIds())) {
				if (!searchVo.isIncludeSub()) {
					String [] columnIds=searchVo.getColumnIds().split(",");
					if (columnIds.length>0) {
						sql+=" and a.columnId in  ( ";
						for (int i = 0; i < columnIds.length; i++) {
							if (i!=columnIds.length) {
								sql +=" ? , ";
							}else{
								sql +=" ? ";
							}
							params.add(columnIds[i]);
						}
						sql+=" ) ";
					}
				}else{
					String [] columnIds=searchVo.getColumnIds().split(",");
					String includeChirdColumnIds="";
					for (int i = 0; i < columnIds.length; i++) {
						List<Integer> columnIdList = columnDao.getSelfAndAllChildrenId(Integer.valueOf(columnIds[i]));
						String tempColumnIds = "";
						for (Integer columnId : columnIdList) {
							if(StringUtil.isNotEmpty(tempColumnIds)) tempColumnIds += ",";
							tempColumnIds += columnId;
						}
						if (!StringUtil.isEmpty(tempColumnIds)) {
							if (StringUtil.isEmpty(includeChirdColumnIds)) {
								includeChirdColumnIds=tempColumnIds;
							}else{
								includeChirdColumnIds+=","+tempColumnIds;
							}
						}
					}
					if (!StringUtil.isEmpty(includeChirdColumnIds)) {
						sql+=" and a.columnId in  ( "+includeChirdColumnIds+" )";
					}
				}
			}
			if (!StringUtil.isEmpty(searchVo.getTitle())) {
				sql+=" and a.title like ?  ";
				params.add("%"+searchVo.getTitle()+"%");
			}
			if (!StringUtil.isEmpty(searchVo.getTitle())) {
				sql+=" and a.content like ? ";
				params.add("%"+searchVo.getTitle()+"%");
			}
			if (!StringUtil.isEmpty(searchVo.getKeyWord())) {
				sql+=" and (a.title like ?  or a.content like ?) ";
				params.add("%"+searchVo.getKeyWord()+"%");
				params.add("%"+searchVo.getKeyWord()+"%");
			}
			if(searchVo.getRecommendId() != null && searchVo.getRecommendId().intValue() != 0){
				sql+=" and r.id = ? ";
				params.add(searchVo.getRecommendId());
			}
			
		}
        sql += " group by a.id ";
		//处理排序规则
		if (searchVo!=null&&searchVo.getSortType()!=null) {
			if (searchVo.getSortType()==0) {
				sql+=" order by a.createTime desc ";
			}else if (searchVo.getSortType()==1) {
				sql+=" order by a.createTime asc ";
			}else if (searchVo.getSortType()==2) {
				sql+=" order by a.clickCount desc ";
			}else if (searchVo.getSortType()==3) {
				sql+=" order by a.updateTime desc ";
			}
		}else{
			sql+=" order by a.createTime desc ";
		}
		
		//处理查询的条数
		if (searchVo!=null) {
		  if(searchVo.getLimit()!=null && searchVo.getLimit()>0){
			if (searchVo.getStart() !=null && searchVo.getStart() >=1) {
				sql += " limit ? , ?  ";
				params.add(searchVo.getStart()-1);
				params.add(searchVo.getLimit());
			}else{
				sql += " limit 0,?";
				params.add(searchVo.getLimit());
			}
		  }else if (searchVo.getStart() !=null && searchVo.getStart() >=1 ){
			  sql += " limit ? ";
			  params.add(searchVo.getStart()-1);
		  }
		}
		if (params.size()>0) {
			return sqlDao.queryForList(sql, params.toArray());
		}else{
			return sqlDao.queryForList(sql);
		}
	}

	@Override
	public PageList findArticlePageList(Integer currentPage, Integer pageSize,ArticleSearchVo searchVo, boolean isSys) {
		String sql=" select a.*,u.name,u.account,c.type columnType,c.name columnName,c.alias,r.name recommendName from " + Article.tableName + " as a " +
				" LEFT JOIN "+ User.tableName + " as u on a.userId=u.id "+
				" LEFT JOIN "+ Column.tableName + " as c on a.columnId=c.id "+
				" LEFT JOIN "+ ArticleRecommend.tableName + " as ar on a.id=ar.articleId "+
				" LEFT JOIN "+ Recommend.tableName + " as r on r.id=ar.recommendId "+
				" where " + ( isSys ? " 1=1 " :" (a.checked = 0 or a.checked = 11) " );
        String countSql = "select count(*) from " + Article.tableName + " as a " +
                " LEFT JOIN "+ User.tableName + " as u on a.userId=u.id "+
                " LEFT JOIN "+ Column.tableName + " as c on a.columnId=c.id "+
                " LEFT JOIN "+ ArticleRecommend.tableName + " as ar on a.id=ar.articleId "+
                " LEFT JOIN "+ Recommend.tableName + " as r on r.id=ar.recommendId "+
                " where " + ( isSys ? " 1=1 " :" (a.checked = 0 or a.checked = 11) " );
		List<Object> params=new ArrayList<Object>();
        String whereSql = "";
		if (searchVo!=null) {
			if(searchVo.isImage()){
				whereSql += " and a.smallPicUrl IS NOT NULL AND LENGTH(TRIM(a.smallPicUrl)) > 0 ";
			}
			if (searchVo.getSiteId()!=null&&searchVo.getSiteId()>0) {
                whereSql+=" and a.siteId= ? ";
				params.add(searchVo.getSiteId());
			}
			//查询单个栏目的文章
			if (searchVo.getColumnId()!=null&&searchVo.getColumnId()>0) {
				if (searchVo.isIncludeSub()) {
					//以下为先查出栏目ID以及所有子栏目ID
					List<Integer> columnIdList = columnDao.getSelfAndAllChildrenId(searchVo.getColumnId());
					String columnIds = "";
					for (Integer columnId : columnIdList) {
						if(StringUtil.isNotEmpty(columnIds)) columnIds += ",";
						columnIds += columnId;
					}
					if (!StringUtil.isEmpty(columnIds)) {
                        whereSql+=" and a.columnId in ( "+columnIds+") ";
					}
				}else{
                    whereSql+=" and a.columnId= ? ";
					params.add(searchVo.getColumnId());
				}
			}
			//查询多个栏目的文章
			if (!StringUtil.isEmpty(searchVo.getColumnIds())) {
				if (!searchVo.isIncludeSub()) {
					String [] columnIds=searchVo.getColumnIds().split(",");
					if (columnIds.length>0) {
                        whereSql+=" and a.columnId in  ( ";
						for (int i = 0; i < columnIds.length; i++) {
							if (i!=columnIds.length) {
                                whereSql +=" ? , ";
							}else{
                                whereSql +=" ? ";
							}
							params.add(columnIds[i]);
						}
                        whereSql+=" ) ";
					}
				}else{
					String [] columnIds=searchVo.getColumnIds().split(",");
					String includeChirdColumnIds="";
					for (int i = 0; i < columnIds.length; i++) {
						List<Integer> columnIdList = columnDao.getSelfAndAllChildrenId(Integer.valueOf(columnIds[i]));
						String tempColumnIds = "";
						for (Integer columnId : columnIdList) {
							if(StringUtil.isNotEmpty(tempColumnIds)) tempColumnIds += ",";
							tempColumnIds += columnId;
						}
						if (!StringUtil.isEmpty(tempColumnIds)) {
							if (StringUtil.isEmpty(includeChirdColumnIds)) {
								includeChirdColumnIds=tempColumnIds;
							}else{
								includeChirdColumnIds+=","+tempColumnIds;
							}
						}
					}
					if (!StringUtil.isEmpty(includeChirdColumnIds)) {
                        whereSql+=" and a.columnId in  ( "+includeChirdColumnIds+" )";
					}
				}
			}
			if (!StringUtil.isEmpty(searchVo.getTitle())) {
                whereSql+=" and a.title like ?  ";
				params.add("%"+searchVo.getTitle()+"%");
			}
			if (!StringUtil.isEmpty(searchVo.getTitle())) {
                whereSql+=" and a.content like ? ";
				params.add("%"+searchVo.getTitle()+"%");
			}
			if (!StringUtil.isEmpty(searchVo.getKeyWord())) {
                whereSql+=" and (a.title like ?  or a.content like ?) ";
				params.add("%"+searchVo.getKeyWord()+"%");
				params.add("%"+searchVo.getKeyWord()+"%");
			}
			if(searchVo.getRecommendId() != null && searchVo.getRecommendId().intValue() != 0){
                whereSql+=" and r.id = ? ";
				params.add(searchVo.getRecommendId());
			}
		}
        String lastSql = " group by a.id ";
		//处理排序规则
		if (searchVo!=null&&searchVo.getSortType()!=null) {
			if (searchVo.getSortType()==0) {
                lastSql+=" order by a.createTime desc ";
			}else if (searchVo.getSortType()==1) {
                lastSql+=" order by a.createTime asc ";
			}else if (searchVo.getSortType()==2) {
                lastSql+=" order by a.clickCount desc ";
			}else if (searchVo.getSortType()==3) {
                lastSql+=" order by a.updateTime desc ";
			}
		}else{
            lastSql+=" order by a.createTime desc ";
		}
		if (params.size()>0) {
			return  sqlDao.getPageList(countSql + whereSql,sql + whereSql + lastSql, currentPage, pageSize, params.toArray());
		}else{
			return  sqlDao.getPageList(countSql + whereSql,sql + whereSql + lastSql, currentPage, pageSize);
		}
	}

	@Override
	public Map<String, Object> load(Integer id) {
		String sql=" select a.*, c.name columnName, u.name userName from " + Article.tableName + " a " 
				+ " left join " + Column.tableName + " c on a.columnId = c.id "
				+ " left join " + User.tableName + " u on a.userId = u.id "
				+ " where a.id= ? ";
		return sqlDao.queryForMap(sql, id);
	}

	@Override
	public int deleteBySiteId(Integer siteId) {
		return sqlDao.delete(Article.tableName, "siteId", siteId);
	}

	@Override
	public int getArticleCount(Integer siteId, Integer columnId) {
		String sql=" select count(1) from "+Article.tableName +" a where 1=1 ";
		List<Object> param=new ArrayList<Object>();
		if (siteId!=null&&siteId>0) {
			sql+=" and a.siteId= ? ";
			param.add(siteId);
		}
		if (columnId!=null&&columnId>0) {
			sql+=" and a.columnId= ? ";
			param.add(columnId);
		}
		if (param.size()>0) {
			return sqlDao.queryForInt(sql, param.toArray());
		}else{
			return sqlDao.queryForInt(sql);
		}
	}

	@Override
	public Article get(Integer id) {
		return hqlDao.get(Article.class, id);
	}

	@Override
	public Map<String, Object> findNextAndPre(Integer articleId) {
		//前一篇
		String sqlPre=" select a.id,a.title,a.smallPicUrl from " + Article.tableName + " as a "
				+" where a.id < ? "
				+" and a.columnId=(select t.columnId from " + Article.tableName + " t where t.id= ? ) "
				+" order by a.id desc limit 1 ";
		//后一篇
		String sqlNext=" select a.id,a.title,a.smallPicUrl from " + Article.tableName  + " as a "
				+" where a.id > ? "
				+" and a.columnId=(select t.columnId from " + Article.tableName + " t where t.id= ? ) " 
				+" order by a.id asc limit 1 ";
		Map<String, Object> result=new HashMap<String, Object>();
		List<Object> param=new ArrayList<Object>();
		param.add(articleId);
		param.add(articleId);
		
		Map<String, Object> pre= sqlDao.queryForMap(sqlPre, param.toArray());
		Map<String, Object> next= sqlDao.queryForMap(sqlNext, param.toArray());
		if (pre!=null) {
			result.put("pre", pre);
		}
		if (next!=null) {
			result.put("next", next);
		}
		return result;
	}

	@Override
	public List<String> getTempIdByArticleIds(String ids) {
		List<String> result=new ArrayList<String>();
		if (StringUtil.isEmpty(ids)) {
			return result;
		} 
		String sql=" select a.tempId  from "+Article.tableName +" a where a.id in ("; 
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
		
		List<Map<String,Object>> list=sqlDao.queryForList(sql,param.toArray());
		if (list!=null&&list.size()>0) {
			for (Map<String, Object> map : list) {
				if (map.containsKey("tempId")) {
					result.add(map.get("tempId")+"");
				}
			}
		}
		return result;
	}

	@Override
	public int updateColumn(Integer columnId, String articleIds) {
		if (columnId==null || StringUtil.isEmpty(articleIds)) {
			return 0;
		}
		String sql=" update  "+Article.tableName +" set columnId = ? where id in ("; 
		String [] idsArray=articleIds.split(",");
		List<Object> param=new ArrayList<Object>();
		param.add(columnId);
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
	public int updateArticleSort(Integer id, Integer flag) {
		int result=0;
		if (id==null||flag==null) {
			return result;
		}
		if (flag==1) {
			String sql=" select a.updateTime from "+Article.tableName +" a "+
						" where a.id <> ? and  a.updateTime >= (select b.updateTime from "+Article.tableName+" b where b.id= ? ) "+
						" and 	a.columnId=(select t.columnId from "+Article.tableName +" t where t.id= ? ) order by a.updateTime asc limit 1 ";
			Date updateTime=this.sqlDao.queryForObject(sql, Date.class, id,id,id);
			if (updateTime!=null) {
				updateTime.setTime(updateTime.getTime()+1000);
				Date d=new Date();
				if (updateTime.getTime()>d.getTime()) {
					updateTime=d;
				}
				sql="update "+Article.tableName + " set updateTime= ? where id = ? ";
				result=this.sqlDao.update(sql, updateTime,id);
			}
		}else if (flag==2) {
			String sql=" select a.updateTime from "+Article.tableName +" a "+
						" where a.id <> ? and  a.updateTime <= (select b.updateTime from "+Article.tableName+" b where b.id= ? ) "+
						" and 	a.columnId=(select t.columnId from "+Article.tableName +" t where t.id= ? ) order by a.updateTime desc limit 1 ";
			Date updateTime=this.sqlDao.queryForObject(sql, Date.class, id,id,id);
			if (updateTime!=null) {
				updateTime.setTime(updateTime.getTime()-1000);
				sql="update "+Article.tableName + " set updateTime= ? where id = ? ";
				result=this.sqlDao.update(sql, updateTime,id);
			}
		}else if (flag==3) {
			String sql="update "+Article.tableName + " set updateTime= ? where id = ? ";
			result=this.sqlDao.update(sql,(new Date()),id);
		}else if (flag==4) {
			String sql="select min(a.updateTime) from "+Article.tableName +" a where a.id <> ? and a.columnId=(select b.columnId from "+Article.tableName +" b where b.id= ? ) " ;
			Date updateTime=this.sqlDao.queryForObject(sql, Date.class, id,id);
			if (updateTime!=null) {
				updateTime.setTime(updateTime.getTime()-1000);
				sql="update "+Article.tableName + " set updateTime= ? where id = ? ";
				result=this.sqlDao.update(sql, updateTime,id);
			}
		}
		return result;
	}

	@Override
	public int updateArticleClickCount(Integer id) {
		String sql = "UPDATE " + Article.tableName + " set clickCount = clickCount + 1 where id = ?";
		return sqlDao.update(sql, id);
	}
	
	@Override
	public int batchUpdateArticleClickCount(String articleIds){
		String sql = "UPDATE " + Article.tableName + " set clickCount = clickCount + 1 where id in (";
		String[] articleIds_arr = articleIds.split(",");
		List<Object> param=new ArrayList<Object>();
		for (int i=0;i<articleIds_arr.length;i++) {
			sql +=" ? ";
			if (i!=articleIds_arr.length-1) {
				sql += " , ";
			}
			param.add(Integer.parseInt(articleIds_arr[i]));
		}
		sql+=" ) ";
		return sqlDao.update(sql, param.toArray());
	}
	
	@Override
	public int updateViewCount(Integer id, Long viewCount){
		String sql = "UPDATE " + Article.tableName + " set viewCount = ? where id = ?";
		return sqlDao.update(sql, viewCount, id);
	}
	
	@Override
	public int updateLastTime(String ids, String lastTime){
		String sql = " UPDATE " + Article.tableName + " SET lastTime = ? WHERE id IN ( ";
		String [] idsArray=ids.split(",");
		List<Object> param=new ArrayList<Object>();
		param.add(lastTime);
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
	public int updateStatus(String ids, String status){
		/*
		String sql = " UPDATE " + Article.tableName + " SET status = ? WHERE id IN ( ";
		String [] idsArray=ids.split(",");
		List<Object> param=new ArrayList<Object>();
		param.add(status);
		for (int i=0;i<idsArray.length;i++) {
			sql +=" ? ";
			if (i!=idsArray.length-1) {
				sql += " , ";
			}
			param.add(Integer.parseInt(idsArray[i]));
		}
		sql+=" ) ";
		return sqlDao.update(sql, param.toArray());
		*/
		String sql = "UPDATE " + Article.tableName + " SET status = ? WHERE id = ?";
		String[] ids_arr = ids.split(",");
		String[] status_arr = status.split(",");
		int updateNum = 0;
		for(int i = 0; i < status_arr.length; i++){
			if("0".equals(status_arr[i])){
				updateNum += sqlDao.update(sql, 1, Integer.parseInt(ids_arr[i]));
			}else{
				updateNum += sqlDao.update(sql, 0, Integer.parseInt(ids_arr[i]));
			}
		}
		return updateNum;
	}

	@Override
	public List<Map<String, Object>> statisticsUserArticle(Integer siteId,
			Integer limitUserNum) {
		String sql = " SELECT " 
					+" COUNT(sa.id) AS userArticleNum, au.account AS account "
					+" FROM site_article AS sa"
					+" LEFT JOIN authority_user AS au ON sa.userId = au.id"
					+" WHERE sa.siteId = ? "
					+" GROUP BY sa.userId "
					+" LIMIT 0,? ";
		return sqlDao.queryForList(sql, siteId, limitUserNum);
	}

	@Override
	public List<Map<String, Object>> statisticsColumnArticle(Integer siteId,
			Integer limitUserNum) {
		String sql = " SELECT COUNT(*) AS articleNum, sc.name "
					+" FROM site_article AS sa "
					+" LEFT JOIN site_column AS sc ON sa.columnId = sc.id "
					+" WHERE sa.siteId = ? "
					+" GROUP BY sa.columnId LIMIT 0,?"; 
		return sqlDao.queryForList(sql, siteId, limitUserNum);
	}

    @Override
    public List<ArticleExtra> getArticleExtraList(Integer siteId) {
        return hqlDao.getListByHQL("from " + ArticleExtra.modelName + " where siteId = ? ", siteId);
    }

    @Override
    public void saveArticleExtraList(List<Map<String, Object>> extraList) {
        sqlDao.save(ArticleExtra.tableName, extraList);
    }

    @Override
    public void deleteAllArticleExtra(Integer siteId) {
        String sql = "DELETE FROM " + ArticleExtra.tableName + " WHERE siteId = ?";
        sqlDao.update(sql, siteId);
    }

    @Override
    public List<Map<String, Object>> getArticleExtraListMap(Integer siteId) {
        String sql = "SELECT * FROM " + ArticleExtra.tableName + " WHERE siteId = ? ";
        return sqlDao.queryForList(sql, siteId);
    }

}
