package com.site.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface ColumnDao {

	/**
	 * 保存新增的栏目
	 * @param map {siteId:, name:, pid:, sort:, level:}
	 * @return
	 */
	Serializable save(Map<String, Object> map);

	/**
	 * 删除一个栏目
	 * @param id
	 * @return
	 */
	int delete(Integer id);

	/**
	 * 修改一个栏目信息
	 * @param map {id:, name:, type:, pid:, sort:, level:}
	 * @return
	 */
	int update(Map<String, Object> map);

	/**
	 * 查询指定站点的所有栏目
	 * @param siteId
	 * @return [{id:, siteId:, name:, type:, pid:, sort:, level:}]
	 */
	List<Map<String, Object>> getList(Integer siteId);

	/**
	 * 批量修改栏目数据
	 * @param mapList
	 * @return
	 */
	int update(List<Map<String, Object>> mapList);

	/**
	 * 查询一个栏目数据
	 * @param id
	 * @return
	 */
	Map<String, Object> load(Integer id);

	/**
	 * 查询排序在指定栏目后边的同级栏目
	 * @param id
	 * @return
	 */
	List<Map<String, Object>> getYoungerBrothers(Integer id);

	/**
	 * 删除指定站点所有栏目
	 * @param siteId
	 * @return
	 */
	int deleteBySiteId(Integer siteId);

	/**
	 * 查询自身及所有父级栏目
	 * @param siteId
	 * @param id
	 * @return
	 */
	List<Map<String, Object>> selfAndParents(Integer siteId, Integer id);

	/**
	 * 查询指定站点所有一级导航栏目及其所有子导航栏目
	 * @param siteId
	 * @return [{id, siteId, type, url, name, alias, pid, sort, level, navigation, children:[{}]}]
	 */
	List<Map<String, Object>> navigationWithChildren(Integer siteId);

	/**
	 * 查询指定站点指定栏目及其所有子栏目
	 * @param siteId
	 * @param id
	 * @return {id, siteId, type, url, name, alias, pid, sort, level, children:[{}]}
	 */
	Map<String, Object> selfWithChildren(Integer siteId, int id);
	
	/**
	 * 根据id集合查询列表
	 * @param ids
	 * @return
	 */
	List<String> getListByIds(String ids);

	/**
	 * 根据用户栏目角色查询栏目集合
	 * @param siteId
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getList(Integer siteId, Integer userId);

	/**
	 * 查询栏目和其所有子栏目的Id
	 * @author lifq
	 * @param id 栏目Id
	 * @return
	 */
	List<Integer> getSelfAndAllChildrenId(int id);
	
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
}
