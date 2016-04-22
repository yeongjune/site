package com.apply.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.apply.model.Xinfang;
import com.apply.service.XinfangService;
import com.base.dao.HQLDao;
import com.base.dao.SQLDao;
import com.base.util.StringUtil;
import com.base.vo.PageList;
import com.site.service.SendMsgService;

@Service
public class XinfangServiceImpl implements XinfangService {
	
	@Autowired
	private HQLDao hqlDao;
	
	@Autowired
	private SQLDao sqlDao;
	@Autowired
	private SendMsgService sendMsgService;

	@Override
	public Serializable save(Map<String, Object> map,Integer siteId) {
		Serializable s = sqlDao.save(Xinfang.tableName, map);
		//每提交一条申请就发送一条提醒信息
		sendMsgService.sendMsg(sendMsgService.getReceivePhone(siteId), sendMsgService.getMsgContent(siteId)+""+map.get("title"),siteId);
		return s;
		
	}

	@Override
	public void delete(String id) {
		String sql = "update "+Xinfang.tableName+" set isDelete = 1 where id = "+id;
		sqlDao.execute(sql);
	}

	@Override
	public void update(Map<String, Object> map) {
		sqlDao.updateMap(Xinfang.tableName, "id", map);
	}

	@Override
	public PageList getPageList(Integer currentPage, Integer pageSize,String name,String status,String check) {
		StringBuffer sql = new StringBuffer();
		sql.append("select *, DATE_FORMAT(createTime,'%Y-%m-%d') as tijiao,DATE_FORMAT(replyTime,'%Y-%m-%d') as huifu  from "+Xinfang.tableName+" where isDelete=0 ");
		List<String> paramList = new ArrayList<String>();
		if (!StringUtil.isEmpty(name)) { 
			sql.append(" AND name like ?");
			paramList.add("%"+name+"%");
		}
		if (!StringUtil.isEmpty(status)) {
			sql.append(" AND status = '"+status+"'");
			//paramList.add("\""+status+"\"");
		}
		if (!StringUtil.isEmpty(check)) {
			sql.append(" AND auditStatus = '"+check+"'");
			//paramList.add("\""+check+"\"");
		}
		//List li = sqlDao.queryForList(sql.toString(),paramList.toArray());
		//PageList l = sqlDao.getPageList(sql.toString()+" order by createTime desc, replyTime desc ", currentPage, pageSize, paramList.toArray());
		return sqlDao.getPageList(sql.toString()+" order by createTime desc, replyTime desc ", currentPage, pageSize, paramList.toArray());
	}

	@Override
	public Map<String, Object> find(String id) {
		StringBuffer sql = new StringBuffer();
		sql.append("select *, DATE_FORMAT(createTime,'%Y-%m-%d') as tijiao,DATE_FORMAT(replyTime,'%Y-%m-%d') as huifu  from "+Xinfang.tableName+" where isDelete=0 and id =? ");
		return sqlDao.queryForMap(sql.toString(),id);
	}
	
	
}
