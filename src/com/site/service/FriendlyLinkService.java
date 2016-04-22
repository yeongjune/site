package com.site.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.base.vo.PageList;
import com.site.model.FriendlyLink;

public interface FriendlyLinkService {
	/**
	 * 保存友情链接
	 * @param friendlyLink	FriendlyLink对象
	 * @return
	 */
	Serializable save(FriendlyLink friendlyLink);
	/**
	 * 保存友情链接
	 * @param map	friendlyLink
	 * @return
	 */
	Serializable save(Map<String, Object> friendlyLink);
	
	/**
	 * 根据Id删除友情链接
	 * @param id 	友情链接Id
	 * @return		
	 */
	int delete(Integer id);

	/**
	 * 根据站点Id和友情连接id删除友情链接
	 * @param siteId 站点ID
	 * @param ids 	友情链接Id，多个用逗号隔开
	 * @return		
	 */
	int delete(Integer siteId,String ids);
	
	
	/**
	 * 修改友情链接
	 * @param friendlyLink  
	 * @return
	 */
	int update(Map<String, Object> friendlyLink);
	
	/**
	 * 修改友情链接
	 * @param friendlyLink  
	 * @return
	 */
	int update(FriendlyLink friendlyLink);
	
	/**
	 * 根据ID获取友情链接
	 * @param id  
	 * @return Map
	 */
	Map<String, Object> load(Integer id);
	
	/**
	 * 根据ID获取友情链接
	 * @author lifq
	 * @param id
	 * @return FriendlyLink
	 */
	FriendlyLink get(Integer id);

	/**
	 * 查询友情链接
	 * @author lifq
	 * @param siteId
	 * @param keyword
	 * @param start
	 * @param limit
	 * @return
	 */
	List<Map<String, Object>>	findFriendlyLinkList(Integer siteId,String keyword,Integer start,Integer limit);
	
	/**
	 * 分页查询友情链接
	 * @author lifq
	 * @param currentPage 页数
	 * @param pageSize	   每页条数
	 * @return
	 */
	PageList findFriendlyLinkPageList(Integer currentPage,Integer pageSize,Integer siteId,String keyword);
	/**
	 * 全站查询友情链接
	 * @return
	 */
	List<Map<String, Object>> findEffectiveFriendlyLinkList();
	/**
	 * 
	 * @param id 链接id
	 * @param status 链接状态  0 有效  1无效
	 * @return
	 */
	int updateEffective(long id,int status);
}
