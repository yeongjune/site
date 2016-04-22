package com.apply.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.apply.model.CxStudent;
import com.apply.vo.CxStudentSearchVo;
import com.base.vo.PageList;

public interface XinfangService {
	
	/**
	 * 保存
	 * @param map {siteId:, name:, pid:, sort:, level:}
	 * @return
	 */
	Serializable save(Map<String, Object> map,Integer siteId);

	/**
	 * 删除一个
	 * @param id
	 * @return
	 */
	void delete(String id);

	/**
	 * 修改
	 * @param map {id:, name:, type:, pid:, sort:, level:}
	 * @return
	 */
	void update(Map<String, Object> map);
	/**
	 * 查询所有
	 * @param currentPage
	 * @param pageSize
	 * @param name 标题
	 * @param status 回复状态(未回复，已回复)
	 * @param check(未审核，通过，不通过)
	 * @return
	 */
	PageList getPageList(Integer currentPage, Integer pageSize,String name,String status,String check);
	
	/**查找一条数据
	 * @param id
	 * @return
	 */
	Map<String,Object> find(String id);

}
