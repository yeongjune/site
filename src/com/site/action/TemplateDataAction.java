package com.site.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.base.config.Init;
import com.base.util.JSONUtil;
import com.site.model.TemplateData;
import com.site.service.TemplateDataService;

@Controller
@RequestMapping("templateData")
public class TemplateDataAction {
	
	@Autowired
	private TemplateDataService templateDataService;

	/**
	 * 给模板添加 数据源
	 * @param request
	 * @param response
	 * @param templateId
	 * @param dataId
	 */
	@RequestMapping("save")
	public void save(HttpServletRequest request, HttpServletResponse response, Integer siteId, Integer templateId, Integer[] dataId){
		if(siteId != null && siteId > 0 && templateId != null && templateId > 0 && dataId != null && dataId.length > 0){
			List<TemplateData> templateDataList = new ArrayList<TemplateData>();
			for(int i = 0; i < dataId.length; i++){
				TemplateData templateData = new TemplateData();
				templateData.setSiteId(siteId);
				templateData.setTemplateId(templateId);
				templateData.setDataId(dataId[i]);
				templateDataList.add(templateData);
			}
			templateDataService.update(templateDataList, siteId, templateId);
			JSONUtil.print(response, Init.SUCCEED);
		}else{
			templateDataService.update(null, siteId, templateId);
			JSONUtil.print(response, Init.SUCCEED);
		}
	}
	
	
	
}
