package com.site.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.site.model.Image;

public interface ImageDao {
	/**
	 * 保存图片记录
	 * @param image	image对象
	 * @return
	 */
	Serializable save(Image image);
	/**
	 * 保存图片记录
	 * @param map	image
	 * @return
	 */
	Serializable save(Map<String, Object> image);

	/**
	 * 根据Id删除图片记录
	 * @param ids 	图片ID，多个用逗号隔开
	 * @return		
	 */
	int delete(String ids);
	
	/**
	 * 根据tempId删除图片记录
	 * @param tempIds 	图片tempId，多个用逗号隔开
	 * @return		
	 */
	int deleteByTempId(String tempIds);
	
	/**
	 * 根据path删除图片记录
	 * @param paths 	图片paths，多个用逗号隔开
	 * @return		
	 */
	int deleteByPath(String paths);
	
	/**
	 * 根据articleIds删除图片记录
	 * @param articleIds 	图片articleId，多个用逗号隔开
	 * @return		
	 */
	int deleteByArticleId(String articleIds);
	
	
	/**
	 * 修改图片记录
	 * @param article  
	 * @return
	 */
	int update(Map<String, Object> image);
	
	/**
	 * 修改图片记录
	 * @param article  
	 * @return
	 */
	int update(Image image);
	
	/**
	 * 根据ID获取新闻信息
	 * @param id  
	 * @return
	 */
	Map<String, Object> load(Integer id);
	
	/**
	 * 根据tempId获取新闻信息
	 * @param tempId  图片临时ID
	 * @return
	 */
	Map<String, Object> loadByTempId(String tempId);
	
	/**
	 * 根据path获取新闻信息
	 * @param path  图片path
	 * @return
	 */
	Map<String, Object> loadByPath(String path);
	
	/**
	 * 查找图片
	 * @author lifq
	 * @param articleId 新闻ID
	 * @param type 	      分类：1、图片类型的新闻图片文件； 2、新闻的缩略图文件； 3、新闻的附近文件，4、其他文件 ；不传或传0查所有
	 * @param tempId   图片临时ID
	 * @param path 图片路径，从根目录算起
	 * @return
	 */
	List<Map<String,Object>> find(Integer articleId,Integer type,String tempId,String path);
	/**
	 * 
	 * @param articleId 新闻ID
	 * @param type 分类：1、图片类型的新闻图片文件； 2、新闻的缩略图文件； 3、新闻的附近文件，4、其他文件 ；不传或传0查所有
	 * @return
	 */
	List<String> findMarkPath(Integer articleId,Integer type);
	
	/**
	 * 批量更新image的articleId
	 * @author lifq
	 * @param articleId 更新为的articleId
	 * @param imageIds  要更新的图片ID，多个字符串隔开
	 * @return
	 */
	public int setImageArticleId(Integer articleId,String imageIds);
	
	/**
	 * 获取文章没用的的文件(图片、附件、缩略图等)
	 * 该方法用于保存文章后获取没用的文件来删除掉
	 * @author lifq
	 * @param articleId 文章ID
	 * @return
	 */
	public List<Map<String,Object>> findDisalbeFile(Integer articleId);
	
	/**
	 * 获取所有暂时没跟新闻关联上的文件
	 * @author lifq
	 * @return
	 */
	public List<Map<String,Object>> findAllDisalbeFile();
	
	/**
	 * 查找多个新闻的图片
	 * @author dzf
	 * @param articleIds 新闻ID
	 * @param type 	      分类：1、图片类型的新闻图片文件； 2、新闻的缩略图文件； 3、新闻的附近文件，4、其他文件 ；不传或传0查所有
	 * @param tempId   图片临时ID
	 * @param path 图片路径，从根目录算起
	 * @return
	 */
	List<Map<String, Object>> findList(List<Integer> articleIds, Integer type, String tempId, String path);

    void updateSmile(Integer siteId, Integer imageId);
}
