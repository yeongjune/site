package com.site.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface ColumnService {

	/**
	 * 保存新增的栏目
	 * @param siteId
	 * @param name
	 * @param pid
	 * @param navigation 
	 * @param type 
	 * @param alias 
	 * @param url 
	 * @param sort
	 * @param level
	 * @param pageSize
	 * @param description
	 * @param className
	 * @return
	 */
	Serializable save(Integer siteId, String name, Integer pid, String alias, String type, String url, Integer navigation, Integer pageSize, String description, String className);

	/**
	 * 删除一个栏目
	 * @param id
	 * @return
	 */
	int delete(Integer id);

	/**
	 * 修改一个栏目信息
	 * @param id
	 * @param name
	 * @param alias
	 * @param type
	 * @param url 
	 * @param navigation
	 * @param pageSize
	 * @param description
	 * @param className
	 * @return
	 */
	int update(Integer id, String name, String alias, String type, String url, Integer navigation, Integer pageSize, String description, String className);

	/**
	 * 查询指定站点的所有栏目
	 * @param siteId
	 * @return
	 */
	List<Map<String, Object>> getList(Integer siteId);

	/**
	 * 查询一个栏目信息
	 * @param id
	 * @return
	 */
	Map<String, Object> load(Integer id);

	/**
	 * 查询所有子栏目
	 * @param id
	 * @return
	 */
	List<Map<String, Object>> children(Integer siteId, Integer id);

	/**
	 * 修改栏目关系
	 * @param id
	 * @param pid
	 * @param sort
	 * @param level
	 * @return
	 */
	int updateRelation(Integer id, Integer pid, Integer sort, Integer level);
	
	/**
	 * 获取站点的导航菜单
	 * @author lifq
	 * @param siteId
	 * @return
	 */
	List<Map<String, Object>> getNavigationWithChildren(Integer siteId);
	/**
	 * @param mapList
	 * @author lifq
	 * 递归判断栏目类型  拼接url	
	 */
	void recursionByMenu(List<Map<String, Object>> mapList);

	/**
	 * 根据用户栏目角色查询栏目集合
	 * @param siteId
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getList(Integer siteId, Integer userId);
	
	/**
	 * 获取栏目可选投票项数
	 * @param columnId
	 * @return
	 */
	int getVoteNum(Integer columnId);
	
	/**
	 * 设置栏目可选投票项数
	 * @param columnId
	 * @param voteNum 可选投票项数
	 * @return
	 */
	int updateVoteNum(Integer columnId, Integer voteNum);

	/**
	 * 加载父栏目的数据
	 * @param id 自栏目ID
	 * @return
	 */
	Map<String, Object> loadParent(Integer id);

    /**
     * 查询所有子孙后代栏目
     * @param id
     * @return
     */
    List<Map<String, Object>> getDescendant(Integer siteId, Integer id);

    /**
     * 获取栏目数据，并处理url
     * @param id
     * @return
     */
    Map<String,Object> get(Integer id);
}
