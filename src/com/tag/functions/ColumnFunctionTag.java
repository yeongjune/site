package com.tag.functions;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.site.service.ColumnService;

@Component
public class ColumnFunctionTag {

	@Autowired
	private ColumnService columnService;
	
	private static ColumnFunctionTag columnFunctionTag;
	
	@PostConstruct
	public void init(){
		columnFunctionTag = this;
		columnFunctionTag.columnService = this.columnService;
	}
	
	/**
	 * 根据ColumnId提取栏目名称
	 * @param id 栏目ID
	 * @return
	 */
	public static String getColumnName(Integer id){
		return (String) columnFunctionTag.columnService.load(id).get("name");
	}

	/**
	 * 根据ColumnId提取栏目名称
	 * @param id 栏目ID
	 * @return
	 */
	public static Map<String, Object> getColumn(Integer id){
		return columnFunctionTag.columnService.load(id);
	}
	
	/**
	 * 提取子栏目
	 * @param siteId 站点ID
	 * @param id 要提取子栏目的父栏目ID
	 * @return
	 */
	public static List<Map<String, Object>> getChildrenColumn(Integer siteId, Integer id){
		List<Map<String, Object>> columnList = columnFunctionTag.columnService.getDescendant(siteId, id);
		ListIterator<Map<String, Object>> listIterator = columnList.listIterator();
		while(listIterator.hasNext()){
			Map<String, Object> tempMap = listIterator.next();
            if(Integer.valueOf(tempMap.get("pid").toString()).intValue() != id.intValue()){
				listIterator.remove();
			}
		}
		columnFunctionTag.columnService.recursionByMenu(columnList);
		return columnList;
	}
	
	/**
	 * 根据栏目ID获取当前栏目级别
	 * @param id 栏目ID
	 * @return
	 */
	public static Integer getColumnLevel(Integer id){
		int i = 1;
		Integer pid = (Integer) columnFunctionTag.columnService.load(id).get("pid");
		while(pid != 0){
			i++;
			pid = (Integer) columnFunctionTag.columnService.load(pid).get("pid");
		}
		return i;
	}
	
	/**
	 * 获取同级的所有栏目，包括自己
	 * @param siteId 站点ID
	 * @param id 栏目ID
	 * @return
	 */
	public static List<Map<String, Object>> getSiblingsColumnAndSelf(Integer siteId, Integer id){
		Integer pid = (Integer) columnFunctionTag.columnService.load(id).get("pid");
		return ColumnFunctionTag.getChildrenColumn(siteId, pid);
	}
	
	/**
	 * 检测childId是否为parentId的子项,或相等
	 * @param parentId
	 * @param childId
	 * @return
	 */
	public static Boolean checkIsRelevant(Integer parentId, Integer childId){
		boolean flag = false;
		if(parentId == null || childId == null || parentId == 0 || childId == 0){ return false; }
		if(parentId.intValue() == childId.intValue()){ return true; }
		Integer pid = childId;
		do{
			pid = (Integer) columnFunctionTag.columnService.load(pid).get("pid");
			if(pid.intValue() == parentId.intValue()){
				flag = true;
				break;
			}
		}while(pid != 0);
		return flag;
	}
	
	/**
	 * 获取父栏目数据
	 * @param id 子栏目ID
	 * @return
	 */
	public static Map<String, Object> getParentColumn(Integer id){
		return columnFunctionTag.columnService.loadParent(id);
	}
	
	
}
