package com.site.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.base.dao.HQLDao;
import com.base.dao.SQLDao;
import com.base.util.StringUtil;
import com.site.model.Article;
import com.site.model.Image;

@Repository
public class ImageDaoImpl implements com.site.dao.ImageDao {
	
	@Autowired
	private SQLDao sqlDao;
	
	@Autowired
	private HQLDao hqlDao;

	@Override
	public Serializable save(Image image) {
		return hqlDao.save(image);
	}

	@Override
	public Serializable save(Map<String, Object> image) {
		return sqlDao.save(Image.tableName, image);
	}

	@Override
	public int delete(String ids) {
		if (StringUtil.isEmpty(ids)) {
			return 0;
		}
		String sql=" delete from "+Image.tableName +"  where id in ("; 
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
	public int deleteByTempId(String tempIds) {
		if (StringUtil.isEmpty(tempIds)) {
			return 0;
		}
		String sql=" delete from "+Image.tableName +"  where tempId in ("; 
		String [] idsArray=tempIds.split(",");
		List<Object> param=new ArrayList<Object>();
		for (int i=0;i<idsArray.length;i++) {
			sql +=" ? ";
			if (i!=idsArray.length-1) {
				sql += " , ";
			}
			param.add(idsArray[i]);
		}
		sql+=" ) ";
		return sqlDao.update(sql, param.toArray());
	}
	
	@Override
	public int deleteByPath(String paths) {
		if (StringUtil.isEmpty(paths)) {
			return 0;
		}
		String sql=" delete from "+Image.tableName +"  where path in ("; 
		String [] idsArray=paths.split(",");
		List<Object> param=new ArrayList<Object>();
		for (int i=0;i<idsArray.length;i++) {
			sql +=" ? ";
			if (i!=idsArray.length-1) {
				sql += " , ";
			}
			param.add(idsArray[i]);
		}
		sql+=" ) ";
		return sqlDao.update(sql, param.toArray());
	}
	
	@Override
	public int deleteByArticleId(String articleIds) {
		if (StringUtil.isEmpty(articleIds)) {
			return 0;
		}
		String sql=" delete from "+Image.tableName +"  where articleId in ("; 
		String [] idsArray=articleIds.split(",");
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
	public int update(Map<String, Object> image) {
		return sqlDao.updateMap(Image.tableName, "id", image);
	}

	@Override
	public int update(Image image) {
		 hqlDao.update(image);
		 return 1;
	}

	@Override
	public Map<String, Object> load(Integer id) {
		String sql=" select i.* from " + Image.tableName + " i where i.id = ? ";
		return sqlDao.queryForMap(sql, id);
	}

	@Override
	public Map<String, Object> loadByTempId(String tempId) {
		String sql=" select i.* from " + Image.tableName + " i where i.tempId = ? ";
		return sqlDao.queryForMap(sql, tempId);
	}
	
	@Override
	public Map<String, Object> loadByPath(String path) {
		String sql=" select i.* from " + Image.tableName + " i where i.path = ? ";
		return sqlDao.queryForMap(sql, path);
	}

	@Override
	public List<Map<String, Object>> find(Integer articleId,Integer type, String tempId,
			String path) {
		String sql=" select i.* from " + Image.tableName + " i where 1=1 ";
		List<Object> param=new ArrayList<Object>();
		if (articleId!=null&&articleId>0) {
			sql += " and i.articleId = ? ";
			param.add(articleId);
		}
		if (type!=null&&type>0) {
			sql += " and i.type = ? ";
			param.add(type);
		}
		if (!StringUtil.isEmpty(tempId)) {
			sql += " and i.tempId = ? ";
			param.add(tempId);
		}
		if (!StringUtil.isEmpty(path)) {
			sql += " and i.path = ? ";
			param.add(path);
		}
		return sqlDao.queryForList(sql, param.toArray());
	}

	@Override
	public int setImageArticleId(Integer articleId, String imageIds) {
		if (StringUtil.isEmpty(imageIds)) {
			return 0;
		}
		List<Object> param=new ArrayList<Object>();
		String sql=" update "+Image.tableName + " set articleId= ? where id in (";
		param.add(articleId);
		String [] ids=imageIds.split(",");
		for (int i = 0; i < ids.length; i++) {
			sql += " ? ";
			if (i!=ids.length-1) {
				sql += " , ";
			}
			param.add(Integer.parseInt(ids[i]));
		}
		sql += " ) ";
		return sqlDao.update(sql, param.toArray());
		
	}

	@Override
	public List<Map<String, Object>> findDisalbeFile(Integer articleId) {
		String sql=" select * from " + Article.tableName +" where id = ? ";
		Map<String, Object> article=sqlDao.queryForMap(sql, articleId);
		if (article==null) {
			return new ArrayList<Map<String,Object>>();
		}else{
			String tempId=article.get("tempId")+"";
			if (StringUtil.isEmpty(tempId)) {	
				sql=" SELECT i.* FROM "+Image.tableName+" i LEFT JOIN site_article a ON i.articleId=a.id WHERE   "+
						"  (i.articleId = ? AND i.type=2 AND i.path <> a.smallPicUrl ) ";  //多余的头像
				return sqlDao.queryForList(sql, articleId);
			}else{
				sql=" SELECT i.* FROM "+Image.tableName+" i LEFT JOIN site_article a ON i.articleId=a.id WHERE   i.tempId= ? "+
					" AND  ("+
					"  (i.articleId is null or  i.articleId=0 )"+   //多余的头像	
					" or  (i.articleId = ? AND i.type=2 AND i.path <> a.smallPicUrl ) "+   //多余的头像
				    " )";
				return sqlDao.queryForList(sql, tempId, articleId);
			}
		}
	}

	@Override
	public List<Map<String, Object>> findAllDisalbeFile() {
		String sql=" SELECT * FROM "+Image.tableName+"  i WHERE  NOT EXISTS (SELECT a.tempid FROM "+Article.tableName+" a WHERE a.tempId=i.tempId )";
		return sqlDao.queryForList(sql);
	}

	@Override
	public List<Map<String, Object>> findList(List<Integer> articleIds,
			Integer type, String tempId, String path) {
		String sql=" select i.* from " + Image.tableName + " i where 1=1 ";
		List<Object> param=new ArrayList<Object>();
		if ( articleIds != null && articleIds.size() > 0) {
			sql += " and i.articleId in(";
			int index = 0;
			for (Integer id : articleIds) {
				sql += (index >0 ? ",?" : "?" );
				param.add(id);
				index++;
			}
			sql += ")";
		}
		if (type!=null&&type>0) {
			sql += " and i.type = ? ";
			param.add(type);
		}
		if (!StringUtil.isEmpty(tempId)) {
			sql += " and i.tempId = ? ";
			param.add(tempId);
		}
		if (!StringUtil.isEmpty(path)) {
			sql += " and i.path = ? ";
			param.add(path);
		}
		return sqlDao.queryForList(sql, param.toArray());
	}

    @Override
    public void updateSmile(Integer siteId, Integer imageId) {
        String sql = "UPDATE " + Image.tableName + " i, " + Article.tableName + " a " +
                " SET i.smile = (i.smile + 1) " +
                " WHERE a.id = i.articleId AND a.siteId = ? AND i.id = ? ";
        sqlDao.update(sql, siteId, imageId);
    }

	@Override
	public List<String> findMarkPath(Integer articleId, Integer type) {
		// TODO Auto-generated method stub
		String sql=" select i.path from " + Image.tableName + " i where 1=1 ";
		List<Object> param=new ArrayList<Object>();
		if (articleId!=null&&articleId>0) {
			sql += " and i.articleId = ? ";
			param.add(articleId);
		}
		if (type!=null&&type>0) {
			sql += " and i.type = ? ";
			param.add(type);
		}
		return sqlDao.queryForList(sql, String.class, articleId,type);
	}
}
