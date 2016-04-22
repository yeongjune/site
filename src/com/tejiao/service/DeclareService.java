package com.tejiao.service;

import com.base.vo.PageList;
import com.tejiao.model.Declare;

import java.util.List;
import java.util.Map;

/**
 * Created by dzf on 15-12-28.
 */
public interface DeclareService {
    void save(Declare declare);

    /**
     * 加载个人申报表
     * @param parentId 家长id
     * @param siteId 网站id
     * @param keyword 搜索关键字
     * @param status
     * @param date
     * @return
     */
    PageList getPersonalPageList(Integer currentPage, Integer pageSize, Integer parentId, Integer siteId, String keyword, Integer status, String date);

    /**
     *
     * @param state 审核状态
     * @param gender 性别
     * @param schoolId 学校
     * @param grade 年级
     * @param siteId 网站id
     * @param userId 用户id
     * @param type
     * @return
     */
    PageList getCheckList(Integer currentPage, Integer pageSize, String keyword
            , Integer state, Integer gender, Integer schoolId, Integer grade, Integer siteId, Integer userId, Integer type);
    /**
     *获取全部的申报
     * @param state 审核状态
     * @param gender 性别
     * @param schoolId 学校
     * @param grade 年级
     * @param siteId 网站id
     * @param type
     * @return
     */
    PageList getDeclareList(Integer currentPage, Integer pageSize, String keyword
    		, Integer state, Integer gender, Integer schoolId, Integer grade, Integer siteId, Integer type);

    /**
     * 第一次审核
     * @param id
     * @param state
     * @param siteId
     */
    void updateOneCheck(Integer id, Integer state, Integer siteId);

    /**
     * 第二次审核
     * @param code
     * @param id
     * @param state
     * @param siteId
     */
    void updateTwoCheck(Integer id, Integer state, Integer siteId);

    Map<String,Object> getById(Integer id, Integer siteId);

    List<Map<String,Object>> getList(String keyword, Integer state, Integer gender, Integer schoolId
            , Integer grade, Integer siteId, Integer userId, Integer type);
    
    /**升级年级
     * @param siteId
     */
    void updateGrade(Long siteId);

    /**
     * 提交申请
     * @param id 申报id
     * @param parentId 学校用户id
     * @param type 申请类型
     * @param siteId
     */
    void updateApplyState(Integer id, Integer parentId, Integer type, Integer siteId);

    /**
     * 获取审核申请的列表
     * @param currentPage
     * @param pageSize
     * @param keyword
     * @param gender
     * @param schoolId
     * @param siteId
     * @param type
     * @return
     */
    PageList getCheckApplyList(Integer currentPage, Integer pageSize, String keyword, Integer gender, Integer schoolId, Integer siteId, Integer type);

    /**
     * 审核申请
     * @param id 申报id
     * @param state 审核状态
     * @param type 审核类型
     * @param userId 审核用户
     * @param siteId 网站id
     */
    void processCheckApply(Integer id, Integer state, Integer type, Integer userId, Integer siteId);

    /**
     * 删除
     * @param id
     * @param siteId
     */
    void deleteById(Integer id, Integer siteId);

    /**
     * 获取审核申请的列表
     * @param keyword
     * @param gender
     * @param schoolId
     * @param siteId
     * @param type
     * @return
     */
    List<Map<String,Object>> getCheckApplyList(String keyword, Integer gender, Integer schoolId, Integer siteId, Integer type);
}
