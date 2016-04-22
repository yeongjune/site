package com.apply.dao;

import java.io.Serializable;

import com.apply.model.RecruitDate;

public interface RecruitDateDao {
	/**
	 * 保存新增的报名日期
	 * @param recruitDate
	 * @return
	 */
	Serializable save(RecruitDate recruitDate);

	/**
	 *根据ID 批量删除报名日期
	 * @param id 	报名日期ID
	 * @return		
	 */
	int delete(String id);

	/**
	 * 修改一个报名日期信息
	 * @param recruitDate  
	 * @return
	 */
	int update(RecruitDate recruitDate);
	
	
	/**
	 * 根据ID只返回报名日期表数据，
	 * @author lifq
	 * @param id
	 * @return
	 */
	RecruitDate get(Integer id);
	
	
	/**
	 * 查找当前站点的报名日期设置
	 * @author lifq
	 * @param siteId 站点siteId
	 * @return
	 */
	RecruitDate load(Integer siteId);
}
