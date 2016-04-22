package com.tejiao.dao;

import com.base.vo.PageList;
import com.tejiao.model.Declare;

import java.util.List;
import java.util.Map;

/**
 * Created by dzf on 15-12-28.
 */
public interface DeclareDao {
    void save(Declare declare);

    PageList getPersonalPageList(Integer currentPage, Integer pageSize, Integer parentId, Integer siteId, String keyword, Integer status, String date);

    PageList getPageListOneCheck(Integer currentPage, Integer pageSize, String keyword
            , Integer state, Integer gender, Integer schoolId, Integer grade, Integer siteId, String town, Integer type);

    PageList getPageListTwoCheck(Integer currentPage, Integer pageSize, String keyword
            , Integer state, Integer gender, Integer schoolId, Integer grade, Integer siteId, Integer type);

    void updateOneCheck(Integer id, Integer state, Integer siteId);

    void updateTwoCheck(Integer id, Integer state, Integer siteId, String code);

    Map<String,Object> getById(Integer id, Integer siteId);

    Declare get(Integer id, Integer siteId);

    /**
     * 根据随读编号前缀统计人数
     * @param code
     * @param siteId
     * @return
     */
    int countByPrefixCode(String code, Integer siteId);

    List<Map<String,Object>> getPageListOneList(String keyword, Integer state, Integer gender, Integer schoolId, Integer grade, Integer siteId, String town, Integer type);

    List<Map<String,Object>> getPageListTwoList(String keyword, Integer state, Integer gender, Integer schoolId, Integer grade, Integer siteId, Integer type);
    /**获取全部的申报项目
     * @param currentPage
     * @param pageSize
     * @param keyword
     * @param state 
     * @param gender
     * @param schoolId
     * @param grade
     * @param siteId
     * @param type
     * @return
     */
    PageList getPageListDeclare(Integer currentPage, Integer pageSize, String keyword
            , Integer state, Integer gender, Integer schoolId, Integer grade, Integer siteId, Integer type);
    /**升级年级
     * @param siteId
     */
    void upgrade(Long siteId);

    /**
     * 更新type类型 的提交状态
     * @param id 申报id
     * @param siteId 网站id
     * @param type 1 职中面试申请 2 免计算分数申请
     * @return
     */
    int updateApplyState(Integer id, Integer siteId, Integer type);

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

    void updateApplyState(Integer id, Integer siteId, Integer type, int state);

    void deleteById(Integer id, Integer siteId);

    List<Map<String,Object>> getCheckApplyList(String keyword, Integer gender, Integer schoolId, Integer siteId, Integer type);
}
