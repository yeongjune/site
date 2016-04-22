package com.lottery.service;

import java.util.List;
import java.util.Map;

import com.lottery.model.Lottery;

public interface LotteryService {

	/**
	 * 保存
	 * @param setting
	 * @return
	 */
	Integer save(Lottery lottery);
	
	/**
	 * 修改
	 * @param setting
	 */
	Integer update(Lottery lottery);
	
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

	/**
	 * 进行摇号操作（广二外用，生成随机数和序号，一步完成）
	 * @param lotteryId
	 * @param count 
	 * @return
	 */
	List<String> processLottery(Integer lotteryId, int count);

	/**
	 * 进行摇号操作第一步,生成摇号号码(教育局用)
	 * @param lotteryId
	 * @return
	 */
	Integer processLottery1(Integer lotteryId);

	/**
	 * 摇号操作第二步，生成序号
	 * @param id
	 * @return
	 */
	Integer processLottery2(Integer id);
	
	/**
	 * 进行摇号操作第三步,生成摇号号码(教育局用)
	 * @param lotteryId
	 * @param number 
	 * @return
	 */
	Integer processLottery3(Integer lotteryId, int number);

	
	
	/**
	 * 查询该次摇号的信息
	 * @param id
	 * @return
	 */
	Map<String, Object> load(Integer id);

	
	/**
	 * 修改某个字段
	 * @param map
	 * @return
	 */
	Integer update(Map<String, Object> map);

}
