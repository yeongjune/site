package com.site.service;

import java.util.Date;
import java.util.Map;

import com.base.vo.PageList;
import com.site.model.Message;

/**
 * @author ah
 *
 */
public interface MessageService {

	/**
	 * 保存
	 * @return
	 */
	Integer save(Message message);

	/**
	 * 查询
	 * 
	 * @param siteId
	 * @param keyword
	 * @return
	 */
	PageList list(Integer siteId, String keyword, Integer currentPage, Integer pageSize);

	/**
	 * 删除
	 * @param ids
	 */
	void delete(String ids);
	
	/**
	 * 通过ID查询留言信息
	 * @param pid
	 * @param currentPage
	 * @param pageSize
	 */
	Map<String, Object> getMsgById(Integer id);
	
	/**
	 * 更新回复内容
	 * @param id
	 * @param Content
	 * @return
	 */
	int updateReply(Integer id, String Content);
	
	
	/**
	 * 根据用户名称和创建时间查询历史记录
	 * @param name
	 * @param createTime
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	PageList getHistory(Integer siteId, String name, Date createTime, Integer currentPage, Integer pageSize);
	
	/**
	 * 通过用户名查询留言记录
	 * @param siteId
	 * @param name
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	PageList getMsgByName(Integer siteId, String name, Integer currentPage, Integer pageSize);
	
}
