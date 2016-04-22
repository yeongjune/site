package com.site.action;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.base.config.Init;
import com.base.util.JSONUtil;
import com.site.model.Label;
import com.site.service.LabelService;

@Controller
@RequestMapping("label")
public class LabelAction {

	@Autowired
	private LabelService service;

	@RequestMapping("index")
	public String index(HttpServletRequest request, HttpServletResponse response){
		return "label/index";
	}
	@RequestMapping("list")
	public void list(HttpServletRequest request, HttpServletResponse response, Integer currentPage, Integer pageSize){
		currentPage = Init.getCurrentPage(currentPage);
		pageSize = Init.getPageSize(pageSize);
		String keyword = request.getParameter("keyword");
		Map<String, Object> pageListMap = service.getListByPage(currentPage, pageSize, keyword);
		JSONUtil.printToHTML(response, pageListMap);
	}
	@RequestMapping(value="add")
	public String add(HttpServletRequest request, HttpServletResponse response){
		return "label/add";
	}
	@RequestMapping(value="save")
	public void save(HttpServletRequest request, HttpServletResponse response){
		String name = request.getParameter("name");
		if(name==null || name.trim().equals("")){
			JSONUtil.print(response, Init.FAIL);
		}else{
			Label label = new Label();
			label.setName(name);
			Serializable id = service.save(label);
			
			if(id!=null){
				JSONUtil.print(response, Init.SUCCEED);
			}else{
				JSONUtil.print(response, Init.FAIL);
			}
		}
	}
	@RequestMapping(value="delete")
	public void delete(HttpServletRequest request, HttpServletResponse response, Integer id){
		if(id==null || id<=0){
			JSONUtil.print(response, Init.FAIL);
		}else{
			int i = service.delete(id);
			JSONUtil.print(response, i>0?Init.SUCCEED:Init.FAIL);
		}
	}
	@RequestMapping(value="edit")
	public String edit(ModelMap map, HttpServletRequest request, HttpServletResponse response, Integer id){
		map.put("id", id);
		return "label/edit";
	}
	@RequestMapping(value="load")
	public void load(HttpServletRequest request, HttpServletResponse response, Integer id){
		Map<String, Object> map = service.load(id);
		JSONUtil.printToHTML(response, map);
	}
	@RequestMapping(value="update")
	public void update(HttpServletRequest request, HttpServletResponse response, Integer id){
		String name = request.getParameter("name");
		if(name==null || name.trim().equals("")){
			JSONUtil.print(response, Init.FAIL);
		}else{
			int i = service.update(id, name);
			JSONUtil.print(response, i>0?Init.SUCCEED:Init.FAIL);
		}
	}
	@RequestMapping(value="nameIsExistWithSelf")
	public void nameIsExistWithSelf(HttpServletRequest request, HttpServletResponse response, Integer id){
		String name = request.getParameter("name");
		if(name==null || name.trim().equals("")){
			JSONUtil.print(response, Init.TRUE);
		}else{
			long i = service.countByNameWithSelf(id, name);
			JSONUtil.print(response, i>0?Init.TRUE:Init.FALSE);
		}
	}
	@RequestMapping(value="nameIsExist")
	public void nameIsExist(HttpServletRequest request, HttpServletResponse response){
		String name = request.getParameter("name");
		if(name==null || name.trim().equals("")){
			JSONUtil.print(response, Init.TRUE);
		}else{
			long i = service.countByName(name);
			JSONUtil.print(response, i>0?Init.TRUE:Init.FALSE);
		}
	}
}
