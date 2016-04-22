package com.lottery.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lottery.model.Lottery;

@Repository
public interface LotteryDao {

	/**
	 * 保存
	 * @param lottery
	 * @return
	 */
	Integer save(Lottery lottery);
	
	/**
	 * 修改
	 * @param lottery
	 */
	void update(Lottery lottery);

	/**
	 * 修改
	 * @param lottery
	 */
	Integer update(Map<String, Object> map);
	
	/**
	 * 删除
	 * @param ids
	 */
	Integer delete(String ids);

	/**
	 * 查询所有的摇号
	 * @return
	 */
	List<Map<String, Object>> getList();

	/**
	 * 根据id 查询
	 * @param id
	 * @return
	 */
	Lottery get(Integer id);
}
