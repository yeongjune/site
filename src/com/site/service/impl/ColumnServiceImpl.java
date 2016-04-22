package com.site.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.base.util.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.util.DataUtil;
import com.site.dao.ColumnDao;
import com.site.service.ColumnService;

@Service
public class ColumnServiceImpl implements ColumnService {

	@Autowired
	private ColumnDao dao;

	@Override
	public Serializable save(Integer siteId, String name, Integer pid, String alias, String type, 
			String url, Integer navigation, Integer pageSize, String description, String className) {
		List<Map<String, Object>> children = children(siteId, pid);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("siteId", siteId);
		map.put("name", name);
		map.put("alias", alias);
		map.put("type", type);
		map.put("url", url);
		map.put("pid", pid);
		map.put("voteNum", 0);
		map.put("sort", children==null?0:children.size());
		map.put("pageSize", pageSize);
		map.put("description", description);
		map.put("className", className);
		if(pid==null || pid==0){
			map.put("level", 0);
		}else{
			Map<String, Object> parent = load(pid);
			Integer level = (Integer) parent.get("level");
			map.put("level", level+1);
		}
		map.put("navigation", navigation);
		return dao.save(map);
	}

	@Override
	public int delete(Integer id) {
		return dao.delete(id);
	}

	@Override
	public int update(Integer id, String name, String alias, String type, String url, 
			Integer navigation, Integer pageSize, String description, String className) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("name", name);
		map.put("alias", alias);
		map.put("type", type);
		map.put("url", url);
		map.put("pageSize", pageSize);
		if(navigation!=null)map.put("navigation", navigation);
		map.put("description", description);
		map.put("className", className);
		return dao.update(map);
	}

	@Override
	public List<Map<String, Object>> getList(Integer siteId) {
		List<Map<String, Object>> resultListMap = new ArrayList<Map<String, Object>>();
		DataUtil.getSortBypid(dao.getList(siteId), 0, resultListMap);
		return resultListMap;
	}

	@Override
	public Map<String, Object> load(Integer id) {
		return dao.load(id);
	}

	@Override
	public List<Map<String, Object>> children(Integer siteId, Integer id) {
		id = id==null?0:id;
		List<Map<String, Object>> mapList = getList(siteId);
		List<Map<String, Object>> children = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> map : mapList) {
			if(map!=null && map.get("pid")!=null && map.get("pid").equals(id)){
				children.addAll(sort(map, mapList));
			}
		}
		return children;
	}

	private List<Map<String, Object>> sort(Map<String, Object> map, List<Map<String, Object>> mapList) {
		List<Map<String, Object>> sortMapList = new ArrayList<Map<String,Object>>();
		sortMapList.add(map);
		List<Map<String, Object>> allChildren = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> map2 : mapList) {
			if(map2.get("pid").equals(map.get("id"))){
				List<Map<String, Object>> children = sort(map2, mapList);
				if(children!=null && children.size()>0){
					allChildren.addAll(children);
				}
			}
		}
		if(allChildren!=null && allChildren.size()>0){
			List<Object> childrenIds = new ArrayList<Object>();
			for (Map<String,Object> child : allChildren) {
				if(child==null || child.isEmpty())continue;
				childrenIds.add(child.get("id"));
			}
			map.put("childrenIds", childrenIds);
			sortMapList.addAll(allChildren);
		}
		return sortMapList;
	}

	@Override
	public int updateRelation(Integer id, Integer pid, Integer sort,
			Integer level) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("pid", pid);
		map.put("sort", sort);
		map.put("level", level);
		return dao.update(map);
	}

	@Override
	public List<Map<String, Object>> getNavigationWithChildren(Integer siteId) {
		return dao.navigationWithChildren(siteId);
	}
	
	/**
	 * 递归判断栏目类型  拼接url	
	 * @param mapList
	 */
	@Override
	public void recursionByMenu(List<Map<String, Object>> mapList){
		for(Map<String, Object> map : mapList){
            recursionByMenu(map);
		}
	}

    private void recursionByMenu(Map<String, Object> map){
        List<Map<String, Object>> childrenList = ((List<Map<String, Object>>) map.get("children"));
        if(childrenList != null && childrenList.size() > 0){
            recursionByMenu(childrenList);
        }
        String id = map.get("id") + "";
        String type = map.get("type") + "";
        if("新闻栏目".equals(type) || "图片栏目".equals(type)){
            Integer pageSize = (Integer) map.get("pageSize");
            if(pageSize == null || pageSize < 1 )
                pageSize = 20;
            map.put("url", "/list-" + id + "-1-" + pageSize + ".html");
        }else if("单网页".equals(type)){
            map.put("url", "/single-" + id + ".html");
        }else if("外部链接".equals(type)){

        }
    }

	@Override
	public List<Map<String, Object>> getList(Integer siteId, Integer userId) {
		List<Map<String, Object>> resultListMap = new ArrayList<Map<String, Object>>();
		DataUtil.getSortBypid(dao.getList(siteId, userId), 0, resultListMap);
		return resultListMap;
	}

	@Override
	public int getVoteNum(Integer columnId){
		return dao.getVoteNum(columnId);
	}
	
	@Override
	public int updateVoteNum(Integer columnId, Integer voteNum){
		return dao.updateVoteNum(columnId, voteNum);
	}

	@Override
	public Map<String, Object> loadParent(Integer id) {
		return dao.loadParent(id);
	}

    @Override
    public List<Map<String, Object>> getDescendant(Integer siteId, Integer id) {
        List<Map<String, Object>> result = classifyAll(siteId).get(id);
        if(result == null)
            result = new ArrayList<Map<String, Object>>(1);
        return result;
    }

    @Override
    public Map<String, Object> get(Integer id) {
        Map<String, Object> load = dao.load(id);
        recursionByMenu(load);
        return load;
    }

    private Map<Object, List<Map<String, Object>>> classifyAll(Integer siteId){
        List<Map<String, Object>> list = dao.getList(siteId);
        Map<Object, List<Map<String, Object>>> mapList = ListUtils.classifyMapList("pid", list);
        Map<String, Object> pMap = new HashMap<String, Object>(2);
        pMap.put("id", 0);
        list.add(pMap);
        List<Map<String, Object>> result = null;
        for(Map<String, Object> map : list){
            map.put("children", mapList.get(map.get("id")));
        }
        return mapList;
    }

}
