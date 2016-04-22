package com.tejiao.service.impl;

import com.base.vo.PageList;
import com.tejiao.dao.DeclareDao;
import com.tejiao.dao.SchoolDao;
import com.tejiao.dao.UserTypeDao;
import com.tejiao.model.Declare;
import com.tejiao.model.School;
import com.tejiao.model.UserType;
import com.tejiao.service.DeclareService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by dzf on 15-12-28.
 */
@Service
public class DeclareServiceImpl implements DeclareService {

    @Autowired
    private DeclareDao declareDao;

    @Autowired
    private UserTypeDao userTypeDao;

    @Autowired
    private SchoolDao schoolDao;

    @Override
    public void save(Declare declare) {
        declareDao.save(declare);
    }

    @Override
    public PageList getPersonalPageList(Integer currentPage, Integer pageSize, Integer parentId, Integer siteId, String keyword, Integer status, String date) {
        return declareDao.getPersonalPageList(currentPage, pageSize, parentId, siteId, keyword, status, date);
    }

    @Override
    public PageList getCheckList(Integer currentPage, Integer pageSize, String keyword
            , Integer state, Integer gender, Integer schoolId, Integer grade, Integer siteId, Integer userId, Integer type) {
        PageList pageList = null;
        UserType userType = userTypeDao.getByUserId(userId, siteId);
        if(userType != null){
            switch (userType.getIsAdmin()){
                case 0:
                    pageList = declareDao.getPageListOneCheck(currentPage, pageSize, keyword
                            , state, gender, schoolId, grade, siteId, userType.getTown(), type);
                    break;
                case 1:
                    pageList = declareDao.getPageListTwoCheck(currentPage, pageSize, keyword
                            , state, gender, schoolId, grade, siteId, type);
                    break;
            }
        }
        return pageList;
    }
    
    @Override
    public PageList getDeclareList(Integer currentPage, Integer pageSize, String keyword
    		, Integer state, Integer gender, Integer schoolId, Integer grade, Integer siteId, Integer type) {
    	PageList pageList = declareDao.getPageListDeclare(currentPage, pageSize, keyword, state, gender, schoolId, grade, siteId, type);
    	return pageList;
    }

    @Override
    public void updateOneCheck(Integer id, Integer state, Integer siteId) {
        declareDao.updateOneCheck(id, state, siteId);
    }

    @Override
    public void updateTwoCheck(Integer id, Integer state, Integer siteId) {
        StringBuilder code = new StringBuilder();
        if(state == 1){
            String[] lv = {"", "A", "B", "C", "D", "E", "F", "G", "H"};
            Declare declare = declareDao.get(id, siteId);
            // 代号首字母
            if(declare.getExistence() == 0){
                code.append(lv[declare.getAppraiseResult()]);
            }else{
                code.append(lv[declare.getDeformityType()]);
            }
            School school = schoolDao.get(declare.getSchoolId());
            code.append(school.getArea());
            code.append(school.getCode());
            Date learnTime = declare.getLearnTime();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(learnTime);
            String year = String.valueOf(calendar.get(Calendar.YEAR));
            code.append(year.substring(year.length() - 2));
            calendar.setTime(new Date());
            year = String.valueOf(calendar.get(Calendar.YEAR));
            code.append(year.substring(year.length() - 2));
            char[] count = String.valueOf(declareDao.countByPrefixCode(code.toString(), siteId) + 1).toCharArray();
            char[] chars = "000".toCharArray();
            for (int i = 1; i <= count.length; i++)
                chars[chars.length - i] = count[count.length - i];
            code.append(String.valueOf(chars));
        }
        declareDao.updateTwoCheck(id, state, siteId, code.toString());
    }

    @Override
    public Map<String, Object> getById(Integer id, Integer siteId) {
        return declareDao.getById(id, siteId);
    }

    @Override
    public List<Map<String, Object>> getList(String keyword, Integer state, Integer gender, Integer schoolId
            , Integer grade, Integer siteId, Integer userId, Integer type) {
        List<Map<String, Object>>  list = null;
        UserType userType = userTypeDao.getByUserId(userId, siteId);
        if(userType != null){
            switch (userType.getIsAdmin()){
                case 0:
                    list = declareDao.getPageListOneList(keyword
                            , state, gender, schoolId, grade, siteId, userType.getTown(), type);
                    break;
                case 1:
                    list = declareDao.getPageListTwoList(keyword
                            , state, gender, schoolId, grade, siteId, type);
                    break;
            }
        }
        return list;
    }

	@Override
	public void updateGrade(Long siteId) {
		declareDao.upgrade(siteId);
	}

    @Override
    public void updateApplyState(Integer id, Integer parentId, Integer type, Integer siteId) {
        Declare declare = declareDao.get(id, siteId);
        if(declare.getParentId().equals(parentId)){
            declareDao.updateApplyState(id, siteId, type);
        }
    }

    @Override
    public PageList getCheckApplyList(Integer currentPage, Integer pageSize, String keyword, Integer gender, Integer schoolId, Integer siteId, Integer type) {
        return declareDao.getCheckApplyList(currentPage, pageSize, keyword, gender, schoolId, siteId, type);
    }

    @Override
    public void processCheckApply(Integer id, Integer state, Integer type, Integer userId, Integer siteId) {
        declareDao.updateApplyState(id, siteId, type, state + 1);
    }

    @Override
    public void deleteById(Integer id, Integer siteId) {
        declareDao.deleteById(id, siteId);
    }

    @Override
    public List<Map<String, Object>> getCheckApplyList(String keyword, Integer gender, Integer schoolId, Integer siteId, Integer type) {
        return declareDao.getCheckApplyList(keyword, gender, schoolId, siteId, type);
    }

}
