package com.authority.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface PublicUrlService {

	/**
	 * 查询所有url
	 * @return
	 */
	List<Map<String, Object>> load();

	/**
	 * 修改url是否是开放权限
	 * @param request
	 * @param id
	 * @param checked
	 * @return
	 */
	int update(HttpServletRequest request, String id, Integer checked);

}
