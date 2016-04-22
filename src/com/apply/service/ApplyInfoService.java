package com.apply.service;

import java.util.List;

import com.apply.model.ApplyInfo;

public interface ApplyInfoService {
	/**
	 * 新增或更新报名信息
	 * @author lfq
	 * @time 2015-3-16
	 * @param JxStudent
	 * @return
	 */
	Integer saveOrUpdate(ApplyInfo applyInfo);
	

	/**
	 * 根据siteId和报名编号获取报名信息列表
	 * @author lfq
	 * @time 2015-3-23
	 * @param siteId
	 * @param applyNo	为空时将为获取该站点下的所有报名信息列表
	 * @return
	 */
	List<ApplyInfo> getApplyInfo(Integer siteId,String applyNo);
	
	
	/**
	 * 根据id获取报名信息
	 * @author lfq
	 * @time 2015-3-23
	 * @param id
	 * @return
	 */
	ApplyInfo get(Integer id);
	
	/**
	 * 根据id删除报名(该删除为假删除)
	 * @author lfq
	 * @time 2015-3-16
	 * @param siteId
	 * @param id
	 * @return
	 */
	Integer delete(Integer siteId,Integer ...id);
}
