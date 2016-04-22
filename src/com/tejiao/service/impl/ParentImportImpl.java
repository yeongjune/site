package com.tejiao.service.impl;

import com.authority.model.User;
import com.base.config.Init;
import com.base.excel.Import;
import com.base.util.CryptUtil;
import com.base.util.StringUtil;
import com.tejiao.service.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by riicy on 16-1-22.
 */
@Service
public class ParentImportImpl implements Import {

    @Autowired
    private ParentService parentService;

    @Override
    public String invoke(HttpServletRequest request, HttpServletResponse response, List<Map<String, Object>> mapList) {
        Integer siteId = User.getCurrentSiteId(request);
        List<String> accountList = parentService.getAccountList(siteId);
        List<Map<String, Object>> saveList = new ArrayList<>(mapList.size());
        for (Map<String, Object> map : mapList) {
            String account = (String) map.get("account");
            String name = (String) map.get("name");
            String password = (String) map.get("password");
            String email = (String) map.get("email");

            if(StringUtil.isEmpty(account)) return name + " 账号为空";
            if(accountList.contains(account.trim())) return name + " 账号已存在";
            if(account.trim().length() < 6) return name + " 账号长度小于6位";
            password = CryptUtil.MD5encrypt(StringUtil.isEmpty(password) ? "abcd1234" : password.trim());

            Map<String, Object> saveMap = new HashMap<>();
            saveMap.put("account", account);
            saveMap.put("password", password);
            saveMap.put("name", name);
            saveMap.put("email", email);
            saveMap.put("siteId", siteId);
            saveList.add(saveMap);
        }
        parentService.saveAll(saveList);
        return Init.SUCCEED;
    }

    @Override
    public String checkedMethod(HttpServletRequest request, Map<String, Object> map) {
        return Init.SUCCEED;
    }

}
