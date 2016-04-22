package com.apply.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.apply.dao.JxStudentDao;
import com.apply.model.JxStudent;
import com.apply.vo.JxStudentSearchVo;
import com.base.dao.HQLDao;
import com.base.dao.SQLDao;
import com.base.util.StringUtil;
import com.base.vo.PageList;

@Component
public class JxStudentDaoImpl implements JxStudentDao {
	
	@Autowired
	private HQLDao hqlDao;
	
	@Autowired
	private SQLDao sqlDao;

	@Override
	public Integer saveOrUpdate(JxStudent jxStudent) {
		hqlDao.saveOrUpdate(jxStudent);
		return jxStudent.getId();
	}

	@Override
	public Integer delete(Integer siteId,Integer... id) {
		String sql="update "+JxStudent.tableName +" set isDelete=1 " +
				" where siteId = "+siteId+" and id in ( "+StringUtil.replaceCollectionToString(Arrays.asList(id))+" )";
		return hqlDao.updateBySQL(sql);
	}

	@Override
	public JxStudent getJxStudent(Integer id) {
		return hqlDao.get(JxStudent.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageList<JxStudent> getJxStudentPageList(JxStudentSearchVo jxStudentSearchVo) {
		if (jxStudentSearchVo==null) {
			return null;
		}
		String hql="FROM "+JxStudent.modelName+" as s WHERE s.isDelete=0 AND s.siteId= ? ";
		List<Object> params=new ArrayList<Object>();
		params.add(jxStudentSearchVo.getSiteId());//必须传siteId
		if (!StringUtil.isEmpty(jxStudentSearchVo.getKeyword())) {
			hql +=" AND s.name like ? or s.usedName like ? or s.IDCard like ? ";
			params.add("%"+jxStudentSearchVo.getKeyword()+"%");
			params.add("%"+jxStudentSearchVo.getKeyword()+"%");
			params.add("%"+jxStudentSearchVo.getKeyword()+"%");
		}
		if (!StringUtil.isEmpty(jxStudentSearchVo.getYear())) {
			hql +=" AND DATE_FORMAT(s.createTime,'%Y')= ?  ";
			params.add(jxStudentSearchVo.getYear());
		}
		if (!StringUtil.isEmpty(jxStudentSearchVo.getGender())) {
			hql +=" AND s.gender= ?  ";
			params.add(jxStudentSearchVo.getGender());
		}
		if (!StringUtil.isEmpty(jxStudentSearchVo.getDds())) {
			hql +=" AND s.dds= ?  ";
			params.add(jxStudentSearchVo.getDds());
		}
		if (!StringUtil.isEmpty(jxStudentSearchVo.getYzs())) {
			hql +=" AND s.yzs= ?  ";
			params.add(jxStudentSearchVo.getYzs());
		}
		if (!StringUtil.isEmpty(jxStudentSearchVo.getGzhjfdds())) {
			hql +=" AND s.gzhjfdds= ?  ";
			params.add(jxStudentSearchVo.getGzhjfdds());
		}
		if (!StringUtil.isEmpty(jxStudentSearchVo.getFgzhjxs())) {
			hql +=" AND s.fgzhjxs= ?  ";
			params.add(jxStudentSearchVo.getFgzhjxs());
		}
		hql +=" order by s.createTime desc ";
		return hqlDao.getPageListByHQL(hql, jxStudentSearchVo.getCurrentPage(), jxStudentSearchVo.getPageSize(), params.toArray());
	}

	@Override
	public List<Map<String, Object>> getJxStudentList(JxStudentSearchVo jxStudentSearchVo) {
		String sql=" SELECT s.*" +
				" ,concat(s.nativePlaceProvince,s.nativePlaceCity ) as nativePlace "+
				" ,concat('关系：',s.relationship1,' 姓名：',s.fullName1,' 工作单位：',s.unit1,' 联系电话：',s.telephone1 ) as familyMember1 "+
				" ,concat('关系：',s.relationship2,' 姓名：',s.fullName2,' 工作单位：',s.unit2,' 联系电话：',s.telephone2 ) as familyMember2 "+
				" FROM "+JxStudent.tableName+" AS s "+
				" WHERE s.isDelete=0 AND s.siteId = ? ";
		List<Object> params=new ArrayList<Object>();
		params.add(jxStudentSearchVo.getSiteId());//必须传siteId
		if (!StringUtil.isEmpty(jxStudentSearchVo.getKeyword())) {
			sql +=" AND s.name like ? or s.usedName like ? or s.IDCard like ? ";
			params.add("%"+jxStudentSearchVo.getKeyword()+"%");
			params.add("%"+jxStudentSearchVo.getKeyword()+"%");
			params.add("%"+jxStudentSearchVo.getKeyword()+"%");
		}
		if (!StringUtil.isEmpty(jxStudentSearchVo.getYear())) {
			sql +=" AND DATE_FORMAT(s.createTime,'%Y')= ?  ";
			params.add(jxStudentSearchVo.getYear());
		}
		if (!StringUtil.isEmpty(jxStudentSearchVo.getGender())) {
			sql +=" AND s.gender= ?  ";
			params.add(jxStudentSearchVo.getGender());
		}
		if (!StringUtil.isEmpty(jxStudentSearchVo.getDds())) {
			sql +=" AND s.dds= ?  ";
			params.add(jxStudentSearchVo.getDds());
		}
		if (!StringUtil.isEmpty(jxStudentSearchVo.getYzs())) {
			sql +=" AND s.yzs= ?  ";
			params.add(jxStudentSearchVo.getYzs());
		}
		if (!StringUtil.isEmpty(jxStudentSearchVo.getGzhjfdds())) {
			sql +=" AND s.gzhjfdds= ?  ";
			params.add(jxStudentSearchVo.getGzhjfdds());
		}
		if (!StringUtil.isEmpty(jxStudentSearchVo.getFgzhjxs())) {
			sql +=" AND s.fgzhjxs= ?  ";
			params.add(jxStudentSearchVo.getFgzhjxs());
		}
		if (jxStudentSearchVo!=null && jxStudentSearchVo.getIds()!=null && jxStudentSearchVo.getIds().length>0) {
			sql += " AND s.id in ("+StringUtil.replaceCollectionToString(Arrays.asList(jxStudentSearchVo.getIds()))+")";
		}
		sql +=" order by s.createTime desc ";
		
		return sqlDao.queryForList(sql,params.toArray());
	}

}
