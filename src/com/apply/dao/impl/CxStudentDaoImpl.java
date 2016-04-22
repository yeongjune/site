package com.apply.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.apply.dao.CxStudentDao;
import com.apply.model.CxStudent;
import com.apply.vo.CxStudentSearchVo;
import com.base.dao.HQLDao;
import com.base.dao.SQLDao;
import com.base.util.StringUtil;
import com.base.vo.PageList;

@Component
public class CxStudentDaoImpl implements CxStudentDao {
	
	@Autowired
	private HQLDao hqlDao;
	
	@Autowired
	private SQLDao sqlDao;

	@Override
	public Integer saveOrUpdate(CxStudent cxStudent) {
		hqlDao.saveOrUpdate(cxStudent);
		return cxStudent.getId();
	}

	@Override
	public Integer delete(Integer siteId,Integer... id) {
		String sql="update "+CxStudent.tableName +" set isDelete=1 " +
				" where siteId = "+siteId+" and id in ( "+StringUtil.replaceCollectionToString(Arrays.asList(id))+" )";
		return hqlDao.updateBySQL(sql);
	}

	@Override
	public CxStudent getCxStudent(Integer id) {
		return hqlDao.get(CxStudent.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageList<CxStudent> getCxStudentPageList(CxStudentSearchVo cxStudentSearchVo) {
		if (cxStudentSearchVo==null) {
			return null;
		}
		String hql="FROM "+CxStudent.modelName+" as s WHERE s.isDelete=0 AND s.siteId= ? ";
		List<Object> params=new ArrayList<Object>();
		params.add(cxStudentSearchVo.getSiteId());//必须传siteId
		if (!StringUtil.isEmpty(cxStudentSearchVo.getKeyword())) {
			hql +=" AND s.name like ?   or s.graduation like ? ";
			params.add("%"+cxStudentSearchVo.getKeyword()+"%");
			params.add("%"+cxStudentSearchVo.getKeyword()+"%");
		}
		if (!StringUtil.isEmpty(cxStudentSearchVo.getYear())) {
			hql +=" AND DATE_FORMAT(s.createTime,'%Y')= ?  ";
			params.add(cxStudentSearchVo.getYear());
		}
		if (!StringUtil.isEmpty(cxStudentSearchVo.getGender())) {
			hql +=" AND s.gender= ?  ";
			params.add(cxStudentSearchVo.getGender());
		}
		
		hql +=" order by s.createTime desc ";
		return hqlDao.getPageListByHQL(hql, cxStudentSearchVo.getCurrentPage(), cxStudentSearchVo.getPageSize(), params.toArray());
	}

	@Override
	public List<Map<String, Object>> getCxStudentList(CxStudentSearchVo cxStudentSearchVo) {
		String sql=" SELECT s.* " +
				" FROM "+CxStudent.tableName+" AS s "+
				" WHERE s.isDelete=0 AND s.siteId = ? ";
		List<Object> params=new ArrayList<Object>();
		params.add(cxStudentSearchVo.getSiteId());//必须传siteId
		if (!StringUtil.isEmpty(cxStudentSearchVo.getKeyword())) {
			sql +=" AND s.name like ?  or s.graduation like ? ";
			params.add("%"+cxStudentSearchVo.getKeyword()+"%");
			params.add("%"+cxStudentSearchVo.getKeyword()+"%");
		}
		if (!StringUtil.isEmpty(cxStudentSearchVo.getYear())) {
			sql +=" AND DATE_FORMAT(s.createTime,'%Y')= ?  ";
			params.add(cxStudentSearchVo.getYear());
		}
		if (!StringUtil.isEmpty(cxStudentSearchVo.getGender())) {
			sql +=" AND s.gender= ?  ";
			params.add(cxStudentSearchVo.getGender());
		}
		if (cxStudentSearchVo!=null && cxStudentSearchVo.getIds()!=null && cxStudentSearchVo.getIds().length>0) {
			sql += " AND s.id in ("+StringUtil.replaceCollectionToString(Arrays.asList(cxStudentSearchVo.getIds()))+")";
		}
		sql +=" order by s.createTime desc ";
		
		return sqlDao.queryForList(sql,params.toArray());
	}

}
