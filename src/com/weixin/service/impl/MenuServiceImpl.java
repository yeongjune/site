package com.weixin.service.impl;

import com.weixin.dao.MenuDao;
import com.weixin.model.WeiXinMenu;
import com.weixin.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by dzf on 2015/11/12.
 */
@Service("weiXinMenuServiceImpl")
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;

    @Override
    public List<Map<String, Object>> getAll(Integer siteId) {
        return menuDao.getAll(siteId);
    }

    @Override
    public void save(WeiXinMenu menu) {
        Integer maxSort = menuDao.getMaxSort(menu.getSiteId(), menu.getPid());
        if(maxSort == null) maxSort = 0;
        menu.setSort(maxSort + 1);
        menuDao.save(menu);
    }

    @Override
    public void delete(Integer id) {
        menuDao.delete(id);
    }

    @Override
    public Integer getMenuCountByPid(Integer siteId, Integer pid) {
        return menuDao.getMenuCountByPid(siteId, pid);
    }

    @Override
    public WeiXinMenu get(Integer id) {
        return menuDao.get(id);
    }

    @Override
    public WeiXinMenu get(Integer siteId, Integer id) {
        WeiXinMenu menu = menuDao.get(id);
        if(menu.getSiteId().equals(siteId))
            return menu;
        return null;
    }

    @Override
    public void update(WeiXinMenu menu) {
        menuDao.update(menu);
    }

    @Override
    public int updateSort(Integer id, Integer tarId, Integer type, Integer siteId) {
        int count = 0;
        if(type == 0 ){// 成为子类别
            int maxSort = menuDao.getMaxSort(siteId, tarId);
            count = menuDao.moveToParent(id, tarId, maxSort, siteId);
        }else if(type == 1 || type == 2){
            Map<String, Object> child = menuDao.getMap(siteId, id);
            Map<String, Object> tar = menuDao.getMap(siteId, tarId);
            Integer pid = (Integer) tar.get("pid");
            child.put("pid", pid);
            Integer sort = (Integer) tar.get("sort");
            if(type == 1){
                menuDao.sortPlusOne(pid, sort, siteId);
                child.put("sort", sort + 1);
            }else{
                menuDao.sortPlusOne(pid, sort - 1, siteId);
                child.put("sort", sort);
            }
            count += menuDao.update(child);
        }
        return count;
    }
}
