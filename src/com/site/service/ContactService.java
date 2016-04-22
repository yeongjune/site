package com.site.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.site.model.Contact;

/**
 * 联系方式service
 * @author lfq
 *
 */
public interface ContactService {
	/**
	 * 新增联系方式
	 * @param contact	Contact对象
	 * @return
	 */
	Serializable save(Contact contact);
	/**
	 * 新增联系方式
	 * @param map	contact
	 * @return
	 */
	Serializable save(Map<String, Object> contact);
	
	/**
	 * 根据Id删除联系方式
	 * @param id 	联系方式Id
	 * @return		
	 */
	int delete(Integer id);

	/**
	 * 根据站点Id和联系方式id删除联系方式
	 * @param siteId 站点ID
	 * @param ids 	联系方式Id，多个用逗号隔开
	 * @return		
	 */
	int delete(Integer siteId,String ids);
	
	
	/**
	 * 修改联系方式
	 * @param contact  
	 * @return
	 */
	int update(Map<String, Object> contact);
	
	/**
	 * 修改联系方式
	 * @param contact  
	 * @return
	 */
	int update(Contact contact);
	
	/**
	 * 根据ID获取联系方式
	 * @param id  
	 * @return Map
	 */
	Map<String, Object> load(Integer id);
	
	/**
	 * 根据ID获取联系方式
	 * @author lifq
	 * @param id
	 * @return Contact
	 */
	Contact get(Integer id);

	/**
	 * 查询联系方式
	 * @author lifq
	 * @param siteId
	 * @return
	 */
	List<Map<String, Object>>	findContactList(Integer siteId);
	/**
	 * 获取站点的新排序号
	 * @author lfq
	 * @param siteId
	 * @return
	 */
	int getNewOrderSort(Integer siteId);
	
	/**
	 *更新排序
	 * @author lfq
	 * @param id
	 * @param upOrdown -1下移，1上移
	 * @return
	 */
	int updateOrderSort(Integer id,Integer upOrdown);
}
