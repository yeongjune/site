package com.tejiao.service.impl;

import com.authority.model.User;
import com.base.excel.Export;
import com.tejiao.service.DeclareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by dzf on 2016/3/16.
 */
@Service
public class DeclareApplyExportImpl implements Export {

    @Autowired
    private DeclareService declareService;

    @Override
    public Object invoke(HttpServletRequest request) {
        Integer siteId = User.getCurrentSiteId(request);
        String keyword = request.getParameter("keyword");

        Integer gender = parseInt(request.getParameter("gender"));
        Integer schoolId = parseInt(request.getParameter("schoolId"));
        Integer type = parseInt(request.getParameter("type"));
        List<Map<String, Object>> list = declareService.getCheckApplyList(keyword, gender, schoolId, siteId, type);
        String[] applyState = new String[]{"没有申请", "未审核", "通过", "不通过"};
        String[] grades = new String[]{"", "一年级", "二年级", "三年级", "四年级", "五年级", "六年级", "初中一年级", "初中二年级", "初中三年级"};
        for (Map<String, Object> map : list) {
            map.put("gender", map.get("gender").equals(1) ? "男" : "女");
            map.put("existence", map.get("existence").equals(1) ? "是" : "否");
            map.put("grade", grades[((Integer) map.get("grade"))]);
            map.put("freeCount", applyState[(Integer) map.get("freeCount")]);
            map.put("applyAudition", applyState[(Integer) map.get("applyAudition")]);
        }
        return list;
    }

    private Integer parseInt(String str){
        if(Pattern.matches("^[0-9]+$", str))
            return Integer.valueOf(str);
        return null;
    }

}
