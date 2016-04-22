package com.tejiao.dao.impl;

import com.base.dao.HQLDao;
import com.base.dao.SQLDao;
import com.base.util.StringUtil;
import com.base.vo.PageList;
import com.tejiao.dao.DeclareDao;
import com.tejiao.model.Declare;
import com.tejiao.model.School;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by dzf on 15-12-28.
 */
@Repository
public class DeclareDaoImpl implements DeclareDao {

    @Autowired
    private HQLDao hqlDao;

    @Autowired
    private SQLDao sqlDao;

    @Override
    public void save(Declare declare) {
        hqlDao.save(declare);
    }

    @Override
    public PageList getPersonalPageList(Integer currentPage, Integer pageSize
            , Integer parentId, Integer siteId, String keyword, Integer status, String date) {
        List<Object> params = new ArrayList<>();
        String sql = "select d.*, s.name as schoolName from " + Declare.tableName + " d " +
                " left join " + School.tableName + " s on(d.schoolId = s.id and s.siteId = ? ) " +
                " where d.siteId = ? and d.parentId = ? ";
        params.add(siteId);
        params.add(siteId);
        params.add(parentId);
        if(StringUtil.isNotEmpty(keyword)){
            sql += " and d.title like ? ";
            params.add("%" + keyword.trim() + "%");
        }
        if(status != null){
            if(status == 0 || status == 2){
                sql += " and d.townCheckState = ? ";
                params.add(status);
            }
            if(status == -1 || status == -2){
                sql += " and d.townCheckState = 1 and d.districtCheckState = ? ";
                params.add(0 - status);
            }
            if(status == 1)
                sql += " and d.townCheckState = 1 and d.districtCheckState = 0 ";
        }
        if(StringUtil.isNotEmpty(date)){
            sql += " and d.createTime like ? ";
            params.add(date + "%");
        }
        sql += " order by d.createTime desc";
        return sqlDao.getPageList(sql, currentPage, pageSize, params.toArray());
    }

    @Override
    public PageList getPageListOneCheck(Integer currentPage, Integer pageSize, String keyword
            , Integer state, Integer gender, Integer schoolId, Integer grade, Integer siteId, String town, Integer type) {
        List<Object> params = new ArrayList<>();
        String sql = "select d.*, s.name as schoolName " +
                " from " + Declare.tableName + " d, " + School.tableName + " s " +
                " where d.schoolId = s.id and d.siteId = ? and s.town = ? ";
        params.add(siteId);
        params.add(town.trim());
        if(state != null){
            sql += " and d.townCheckState = ? ";
            params.add(state);
        }
        if(StringUtil.isNotEmpty(keyword)){
            sql += " and d.title like ? ";
            params.add("%" + keyword.trim() + "%");
        }
        if(gender != null){
            sql += " and d.gender = ? ";
            params.add(gender);
        }
        if(schoolId != null && schoolId > 0){
            sql += " and s.id = ? ";
            params.add(schoolId);
        }
        if(grade != null && grade > 0){
            sql += " and d.grade = ? ";
            params.add(grade);
        }
        if(type != null && type > 0){
            sql += " and d.deformityType = ? ";
            params.add(type);
        }
        sql += " order by d.createTime desc ";
        return sqlDao.getPageList(sql, currentPage, pageSize, params.toArray());
    }

    @Override
    public PageList getPageListTwoCheck(Integer currentPage, Integer pageSize, String keyword
            , Integer state, Integer gender, Integer schoolId, Integer grade, Integer siteId, Integer type) {
        List<Object> params = new ArrayList<>();
        String sql = "select d.*, s.name as schoolName " +
                " from " + Declare.tableName + " d, " + School.tableName + " s " +
                " where d.schoolId = s.id and d.siteId = ? ";
        params.add(siteId);
        if(state != null){
            sql += " and d.districtCheckState = ? ";
            params.add(state);
        }
        if(StringUtil.isNotEmpty(keyword)){
            sql += " and d.title like ? ";
            params.add("%" + keyword.trim() + "%");
        }
        if(gender != null){
            sql += " and d.gender = ? ";
            params.add(gender);
        }
        if(schoolId != null && schoolId > 0){
            sql += " and s.id = ? ";
            params.add(schoolId);
        }
        if(grade != null && grade > 0){
            sql += " and d.grade = ? ";
            params.add(grade);
        }
        if(type != null && type > 0){
            sql += " and d.deformityType = ? ";
            params.add(type);
        }
        sql += " order by d.createTime desc ";
        return sqlDao.getPageList(sql, currentPage, pageSize, params.toArray());
    }
    @Override
    public PageList getPageListDeclare(Integer currentPage, Integer pageSize, String keyword
    		, Integer state, Integer gender, Integer schoolId, Integer grade, Integer siteId, Integer type) {
    	List<Object> params = new ArrayList<>();
    	String sql = "select d.*, s.name as schoolName " +
    			" from " + Declare.tableName + " d, " + School.tableName + " s " +
    			" where d.schoolId = s.id and d.siteId = ? ";
    	params.add(siteId);
    	if(state != null){
    		sql += " and d.districtCheckState = ? ";
    		params.add(state);
    	}
    	if(StringUtil.isNotEmpty(keyword)){
    		sql += " and d.title like ? ";
    		params.add("%" + keyword.trim() + "%");
    	}
    	if(gender != null){
    		sql += " and d.gender = ? ";
    		params.add(gender);
    	}
    	if(schoolId != null && schoolId > 0){
    		sql += " and s.id = ? ";
    		params.add(schoolId);
    	}
    	if(grade != null && grade > 0){
    		sql += " and d.grade = ? ";
    		params.add(grade);
    	}
    	if(type != null && type > 0){
    		sql += " and d.deformityType = ? ";
    		params.add(type);
    	}
    	sql += " order by d.createTime desc ";
    	return sqlDao.getPageList(sql, currentPage, pageSize, params.toArray());
    }

    @Override
    public void updateOneCheck(Integer id, Integer state, Integer siteId) {
        String sql = "UPDATE " + Declare.tableName + " SET townCheckState = ? WHERE id = ? AND siteId = ?";
        sqlDao.update(sql, state, id, siteId);
    }

    @Override
    public void updateTwoCheck(Integer id, Integer state, Integer siteId, String code) {
        String sql = "UPDATE " + Declare.tableName + " SET districtCheckState = ?, code = ? " +
                " WHERE id = ? AND siteId = ? AND townCheckState = 1 ";
        sqlDao.update(sql, state, code, id, siteId);
    }

    @Override
    public Map<String, Object> getById(Integer id, Integer siteId) {
        String sql = "select d.*, s.name as schoolName " +
                " from " + Declare.tableName + " d LEFT JOIN " + School.tableName + " s " +
                " ON d.schoolId = s.id WHERE d.id = ? and d.siteId = ? ";
        return sqlDao.queryForMap(sql, id, siteId);
    }

    @Override
    public Declare get(Integer id, Integer siteId) {
        String hql = "from " + Declare.modelName + " where id = ? and siteId = ? ";
        return (Declare) hqlDao.getObjectByHQL(hql, id, siteId);
    }

    @Override
    public int countByPrefixCode(String code, Integer siteId) {
        String sql = "SELECT COUNT(*) FROM " + Declare.tableName + " WHERE code like ? AND siteId = ?";
        return sqlDao.queryForObject(sql, Integer.class, code + "%", siteId);
    }

    @Override
    public List<Map<String, Object>> getPageListOneList(String keyword, Integer state, Integer gender, Integer schoolId
            , Integer grade, Integer siteId, String town, Integer type) {
        List<Object> params = new ArrayList<>();
        String sql = "select " +
                " d.title, " +
                " d.registry, " +
                " d.name, " +
                " d.gender, " +
                " d.birth, " +
                " d.idcard, " +
                " d.census, " +
                " d.learnTime, " +
                " d.grade, " +
                " d.existence, " +
                " d.guardian, " +
                " d.relation, " +
                " d.phone, " +
                " d.email, " +
                " d.townCheckState, " +
                " d.districtCheckState, " +
                " d.code, " +
                " s.name as schoolName " +
                " from " + Declare.tableName + " d, " + School.tableName + " s " +
                " where d.schoolId = s.id and d.siteId = ? and s.town = ? ";
        params.add(siteId);
        params.add(town.trim());
        if(state != null){
            sql += " and d.townCheckState = ? ";
            params.add(state);
        }
        if(StringUtil.isNotEmpty(keyword)){
            sql += " and d.title like ? ";
            params.add("%" + keyword.trim() + "%");
        }
        if(gender != null){
            sql += " and d.gender = ? ";
            params.add(gender);
        }
        if(schoolId != null && schoolId > 0){
            sql += " and s.id = ? ";
            params.add(schoolId);
        }
        if(grade != null && grade > 0){
            sql += " and d.grade = ? ";
            params.add(grade);
        }
        if(type != null && type > 0){
            sql += " and d.deformityType = ? ";
            params.add(type);
        }
        sql += " order by d.createTime desc ";
        return sqlDao.queryForList(sql, params.toArray());
    }

    @Override
    public List<Map<String, Object>> getPageListTwoList(String keyword, Integer state, Integer gender
            , Integer schoolId, Integer grade, Integer siteId, Integer type) {
        List<Object> params = new ArrayList<>();
        String sql = "select " +
                " d.title, " +
                " d.registry, " +
                " d.name, " +
                " d.gender, " +
                " d.birth, " +
                " d.idcard, " +
                " d.census, " +
                " d.learnTime, " +
                " d.grade, " +
                " d.existence, " +
                " d.guardian, " +
                " d.relation, " +
                " d.phone, " +
                " d.email, " +
                " d.townCheckState, " +
                " d.districtCheckState, " +
                " d.code, " +
                " s.name as schoolName " +
                " from " + Declare.tableName + " d, " + School.tableName + " s " +
                " where d.schoolId = s.id and d.siteId = ? ";
        params.add(siteId);
        if(state != null){
            sql += " and d.districtCheckState = ? ";
            params.add(state);
        }
        if(StringUtil.isNotEmpty(keyword)){
            sql += " and d.title like ? ";
            params.add("%" + keyword.trim() + "%");
        }
        if(gender != null){
            sql += " and d.gender = ? ";
            params.add(gender);
        }
        if(schoolId != null && schoolId > 0){
            sql += " and s.id = ? ";
            params.add(schoolId);
        }
        if(grade != null && grade > 0){
            sql += " and d.grade = ? ";
            params.add(grade);
        }
        if(type != null && type > 0){
            sql += " and d.deformityType = ? ";
            params.add(type);
        }
        sql += " order by d.createTime desc ";
        return sqlDao.queryForList(sql, params.toArray());
    }

	@Override
	public void upgrade(Long siteId) {
		//升级条件： 1.当前年级不能大于9(9=初三)
        String sql = "select " +
                " d.id, " +
                " d.grade, " +
                " d.lastModifyTime " +
                " from " + Declare.tableName + " d ";
        List<Map<String,Object>> list = sqlDao.queryForList(sql);
        for (Map<String, Object> map : list) {
        	if (Integer.valueOf(map.get("grade").toString())<=8) {
				map.put("grade", Integer.valueOf(map.get("grade").toString())+1);
				map.put("lastModifyTime", new Date());
			}
		}
        sqlDao.updateMapList(Declare.tableName, "id", list);
	}

    @Override
    public int updateApplyState(Integer id, Integer siteId, Integer type) {
        Date nowTime = new Date();
        String sql = "UPDATE " + Declare.tableName + " SET ";
        switch (type){
            case 1:
                sql += " applyAudition = 1, applyAuditionTime = ? ";
                break;
            case 2:
                sql += " freeCount = 1, freeCountTime = ? ";
                break;
        }
        sql += " WHERE id = ? AND siteId = ? ";
        switch (type){
            case 1:
                sql += " AND applyAudition = 0 ";
                break;
            case 2:
                sql += " AND freeCount = 0 ";
                break;
        }
        return sqlDao.update(sql, nowTime, id, siteId);
    }

    @Override
    public PageList getCheckApplyList(Integer currentPage, Integer pageSize, String keyword, Integer gender
            , Integer schoolId, Integer siteId, Integer type) {
        List<Object> params = new ArrayList<>();
        String sql = "SELECT d.*, s.name AS schoolName FROM " +
                Declare.tableName + " d " +
                " LEFT JOIN " + School.tableName +
                " s ON(d.schoolId = s.id) WHERE d.siteId = ? ";
        params.add(siteId);
        if(StringUtil.isNotEmpty(keyword)){
            sql += " AND (d.title LIKE ? OR d.name LIKE ? OR s.name LIKE ?) ";
            params.add("%" + keyword.trim() + "%");
            params.add("%" + keyword.trim() + "%");
            params.add("%" + keyword.trim() + "%");
        }
        if(schoolId != null && schoolId > 0){
            sql += " AND d.schoolId = ? ";
            params.add(schoolId);
        }
        if(gender != null && gender > 0){
            sql += " AND d.gender = ? ";
            params.add(gender);
        }
        if(type != null && type > 0){
            switch (type){
                case 1:
                    sql += " AND d.applyAudition > 0 ";
                    break;
                case 2:
                    sql += " AND d.freeCount > 0 ";
                    break;
            }
        }else{
            sql += " AND (d.freeCount > 0 OR d.applyAudition > 0)";
        }
        sql += " ORDER BY d.freeCountTime DESC, d.applyAuditionTime DESC ";
        return sqlDao.getPageList(sql, currentPage, pageSize, params.toArray());
    }

    @Override
    public void updateApplyState(Integer id, Integer siteId, Integer type, int state) {
        Date nowTime = new Date();
        String sql = "UPDATE " + Declare.tableName + " SET ";
        switch (type){
            case 1:
                sql += " applyAudition = "+state+", applyAuditionTime = ? ";
                break;
            case 2:
                sql += " freeCount = "+state+", freeCountTime = ? ";
                break;
        }
        sql += " WHERE id = ? AND siteId = ? ";
        switch (type){
            case 1:
                sql += " AND applyAudition = 1 ";
                break;
            case 2:
                sql += " AND freeCount = 1 ";
                break;
        }
        sqlDao.update(sql, nowTime, id, siteId);
    }

    @Override
    public void deleteById(Integer id, Integer siteId) {
        String sql = "DELETE FROM " + Declare.tableName + " WHERE id = ? AND siteId = ? ";
        sqlDao.update(sql, id, siteId);
    }

    @Override
    public List<Map<String, Object>> getCheckApplyList(String keyword, Integer gender, Integer schoolId, Integer siteId, Integer type) {
        List<Object> params = new ArrayList<>();
        String sql = "SELECT " +
                " d.title, " +
                " d.registry, " +
                " d.name, " +
                " d.gender, " +
                " d.birth, " +
                " d.idcard, " +
                " d.census, " +
                " d.learnTime, " +
                " d.grade, " +
                " d.existence, " +
                " d.guardian, " +
                " d.relation, " +
                " d.phone, " +
                " d.email, " +
                " d.freeCount, " +
                " d.applyAudition, " +
                " s.name as schoolName " +
                " FROM " +
                Declare.tableName + " d " +
                " LEFT JOIN " + School.tableName +
                " s ON(d.schoolId = s.id) WHERE d.siteId = ? ";
        params.add(siteId);
        if(StringUtil.isNotEmpty(keyword)){
            sql += " AND (d.title LIKE ? OR d.name LIKE ? OR s.name LIKE ?) ";
            params.add("%" + keyword.trim() + "%");
            params.add("%" + keyword.trim() + "%");
            params.add("%" + keyword.trim() + "%");
        }
        if(schoolId != null && schoolId > 0){
            sql += " AND d.schoolId = ? ";
            params.add(schoolId);
        }
        if(gender != null && gender > 0){
            sql += " AND d.gender = ? ";
            params.add(gender);
        }
        if(type != null && type > 0){
            switch (type){
                case 1:
                    sql += " AND d.applyAudition > 0 ";
                    break;
                case 2:
                    sql += " AND d.freeCount > 0 ";
                    break;
            }
        }else{
            sql += " AND (d.freeCount > 0 OR d.applyAudition > 0)";
        }
        sql += " ORDER BY d.freeCountTime DESC, d.applyAuditionTime DESC ";
        return sqlDao.queryForList(sql, params.toArray());
    }

}
