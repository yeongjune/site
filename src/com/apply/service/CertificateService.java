package com.apply.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.apply.model.Certificate;

public interface CertificateService {
	/**
	 * 保存新增的证件类型
	 * @param certificate
	 * @return
	 */
	Serializable save(Certificate certificate);

	/**
	 *根据ID 批量删除证件类型
	 * @param ids 	证件类型ID,多个时用逗号隔开
	 * @param siteId
	 * @return		
	 */
	int delete(String ids,Integer siteId);

	/**
	 * 修改一个证件类型信息
	 * @param certificate  
	 * @return
	 */
	int update(Map<String, Object> certificate);
	
	
	/**
	 * 根据ID只返回证件类型表数据，
	 * @author lifq
	 * @param id
	 * @return
	 */
	Certificate get(Integer id);
	
	
	/**
	 * 查询证件类型
	 * @author lifq
	 * @param keyword 证明名或备注，模糊查询
	 * @param siteId   站点siteId
	 * @return
	 */
	List<Map<String, Object>>	findCertificateList(String keyword,Integer siteId);
	/**
	 * 根据名字获取证件类型
	 * @author lifq
	 * @param siteId 
	 * @param 证件名
	 * @return
	 */
	Map<String, Object>  findByName(String name, Integer siteId);
}
