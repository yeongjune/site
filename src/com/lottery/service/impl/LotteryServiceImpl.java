package com.lottery.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lottery.dao.LotteryDao;
import com.lottery.dao.StudentDao;
import com.lottery.model.Lottery;
import com.lottery.model.Student;
import com.lottery.service.LotteryService;

@Service
public class LotteryServiceImpl implements LotteryService{

	@Autowired
	private LotteryDao lotteryDao;
	
	@Autowired
	private StudentDao studentDao;
	
	
	@Override
	public Integer save(Lottery lottery) {
		return lotteryDao.save(lottery);
	}

	@Override
	public Integer update(Lottery lottery) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", lottery.getId());
		map.put("title", lottery.getTitle());
		map.put("number", lottery.getNumber());
		map.put("waitNum", lottery.getWaitNum());
		map.put("times", lottery.getTimes());
		map.put("remark", lottery.getRemark());
		return lotteryDao.update(map);
	}

	@Override
	public Integer delete(String ids) {
		Integer count = lotteryDao.delete(ids);
		if(count > 0){//删除成功，删除该次摇号导入的学生
			studentDao.deleteByLotteryId(ids);
		}
		return count;
	}

	@Override
	public List<Map<String, Object>> getList() {
		return lotteryDao.getList();
	}

	@Override
	public Lottery get(Integer id) {
		return lotteryDao.get(id);
	}

	@Override
	public List<String> processLottery(Integer lotteryId, int count) {
		Lottery lottery = this.get(lotteryId);
		List<Map<String, Object>> list = studentDao.getByLotteryId1(lotteryId);
		Integer countTimes = lottery.getNumber()/lottery.getTimes();
		if(lottery.getNumber()%lottery.getTimes() > 0){//如果有余数，每次需要选取多一个
			countTimes++;
		}
		
		//生成随机数
		Random random = new Random();
		for(Map<String, Object> map : list){
			int randomNum = random.nextInt(90000) + 10000;
			map.put("randomNum", randomNum);
		}
		studentDao.saveOrUpdateAll(list);//保存随机数
		
		if(countTimes > list.size()){//如果所有的学生都选完都不够，则把所有学生选中即可
			countTimes = list.size();
		}else if(countTimes > count){//如果最后一次还需要选取的人数不够每次选取的人数多，以最后还需选取的人数为准
			countTimes = count;
		}
		
		List<Map<String, Object>> resultList = studentDao.getOrderByRandomNum(lotteryId);
		List<String> names = new ArrayList<String>();
		List<Map<String, Object>> uList = new ArrayList<Map<String, Object>>();
		for(int i = 0; i < resultList.size(); i++){
			Map<String, Object> student = resultList.get(i);
			if(i < countTimes){
				student.put("status", Student.SELECTED);
				names.add((String) student.get("name"));
			}
			/*else if(i < (countTimes + lottery.getWaitNum())){
				student.put("status", Student.WAITING);
			}*/
			student.put("serialNum", i + 1);
			uList.add(student);
		}
		studentDao.saveOrUpdateAll(uList);
		return names;
	}
	
	@Override
	public Integer processLottery1(Integer lotteryId) {
		List<Map<String, Object>> list = studentDao.getByLotteryId1(lotteryId);//查询未选中的学生
		
		//生成随机数
		Random random = new Random();
		List<Integer> randoms = new ArrayList<Integer>(list.size());
		for(Map<String, Object> map : list){
			if(map.get("randomNum") == null || map.get("randomNum").toString().trim().equals("")){
				int randomNum = random.nextInt(90000) + 10000;
				while (randoms.contains(randomNum)) {
					randomNum = random.nextInt(90000) + 10000;
				}
				randoms.add(randomNum);
				map.put("randomNum", randomNum);
			}
		}
		studentDao.saveOrUpdateAll(list);//保存随机数
		//更新摇号到第一步
		updateStep(lotteryId, 1);
		return list.size();
	}

	@Override
	public Integer processLottery2(Integer lotteryId) {
		List<Map<String, Object>> list = studentDao.getForSerial(lotteryId, 1);
		for(int i = 0; i < list.size(); i++){
			Map<String, Object> map = list.get(i);
			map.put("serialNum", i + 1);
		}
		Integer count = studentDao.saveOrUpdateAll(list);
		updateStep(lotteryId, 2);
		return count;
	}
	
	@Override
	public Integer processLottery3(Integer lotteryId, int number) {
		Lottery lottery = this.get(lotteryId);
		lottery.setNumber(number);
		lottery.setStep(3);//更新摇号步奏到第三步
		this.update(lottery);
		List<Map<String, Object>> resultList = studentDao.getOrderByRandomNum(lotteryId);//所有未被选中的学生按随机数升序排
		List<Map<String, Object>> selectedList = new ArrayList<Map<String, Object>>();
		for(int i = 0; i < number; i++){
			Map<String, Object> student = resultList.get(i);
			student.put("status", Student.SELECTED);
			selectedList.add(student);
		}
		studentDao.saveOrUpdateAll(selectedList);
		return selectedList.size();
	}
	
	private void updateStep(Integer id, Integer step){
		Map<String, Object> uMap = new HashMap<String, Object>();
		uMap.put("id", id);
		uMap.put("step", step);
		lotteryDao.update(uMap);
	}
	
	@Override
	public Map<String, Object> load(Integer id) {
		return studentDao.load(id);
	}

	@Override
	public Integer update(Map<String, Object> map) {
		return lotteryDao.update(map);
	}

}
