package com.apply.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.apply.dao.ApplyInfoDao;
import com.apply.model.ApplyInfo;
import com.base.dao.HQLDao;
import com.base.util.StringUtil;

@Component
public class ApplyInfoDaoImpl implements ApplyInfoDao {
	
	@Autowired
	private HQLDao hqlDao;

	@Override
	public Integer saveOrUpdate(ApplyInfo applyInfo) {
		hqlDao.saveOrUpdate(applyInfo);
		return applyInfo.getId();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ApplyInfo> getApplyInfo(Integer siteId, String applyNo) {
		String hql="FROM "+ApplyInfo.modelName +" AS a where a.isDelete = 0 ";
		List<Object> params=new ArrayList<Object>();
		if (siteId!=null && siteId>0) {
			hql += " and a.siteId = ? ";
			params.add(siteId);
		}
		if (!StringUtil.isEmpty(applyNo)) {
			hql += " and a.applyNo = ? ";
			params.add(applyNo);
		}
		return hqlDao.getListByHQL(hql, params.toArray());
	}

	@Override
	public ApplyInfo get(Integer id) {
		return hqlDao.get(ApplyInfo.class, id);
	}

	@Override
	public Integer delete(Integer siteId, Integer... id) {
		String sql="update "+ApplyInfo.tableName +" set isDelete=1 " +
				" where siteId = "+siteId+" and id in ( "+StringUtil.replaceCollectionToString(Arrays.asList(id))+" )";
		return hqlDao.updateBySQL(sql);
	}

}
