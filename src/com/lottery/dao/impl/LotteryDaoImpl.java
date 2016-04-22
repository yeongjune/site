package com.lottery.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.base.dao.HQLDao;
import com.base.dao.SQLDao;
import com.lottery.dao.LotteryDao;
import com.lottery.model.Lottery;

@Repository
public class LotteryDaoImpl implements LotteryDao{

	@Autowired
	private HQLDao hqlDao;

	@Autowired
	private SQLDao sqlDao;

	@Override
	public Integer save(Lottery lottery) {
		return (Integer) hqlDao.save(lottery);
	}

	@Override
	public void update(Lottery lottery) {
		hqlDao.update(lottery);
	}

	@Override
	public Integer update(Map<String, Object> map){
		return sqlDao.updateMap(Lottery.tableName, "id", map);
	}
	
	@Override
	public Integer delete(String ids) {
		
		String sql = 	" DELETE  FROM " +
								" " + Lottery.tableName + 
							" WHERE" +
								" id IN(";
		
		if(ids != null && ids.trim().length() > 0){
			List<Object> params = new ArrayList<Object>();
			String[] idArr = ids.split(",");
			for(String id : idArr){
				sql += "?,";
				params.add(Integer.parseInt(id));
			}
			
			sql = sql.substring(0, sql.length() - 1) + ")";
			return sqlDao.update(sql, params.toArray());
		}else{
			return 0;
		}
	}

	@Override
	public List<Map<String, Object>> getList() {
		
		String sql = 	" SELECT" +
								" *" +
							" FROM" +
								" " + Lottery.tableName + 
							"  ORDER BY createTime DESC";
		
		return sqlDao.queryForList(sql);
	}

	@Override
	public Lottery get(Integer id) {
		return hqlDao.get(Lottery.class, id);
	}
	
	
	
}
