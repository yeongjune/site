package com.site.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.authority.model.User;
import com.base.config.Init;
import com.base.util.JSONUtil;
import com.base.util.StringUtil;
import com.site.model.FriendlyLink;
import com.site.service.FriendlyLinkService;

@Controller
@RequestMapping("friendlylink")
public class FriendlyLinkAction {
	@Autowired
	private FriendlyLinkService friendlyLinkService;
	
	
	/**
	 * 进入友情连接管理界面
	 * @author lifq
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="index")
	public String index(HttpServletRequest request,HttpServletResponse response,ModelMap map){
		map.put("siteId", User.getCurrentSiteId(request));
		return "site/friendlylink/index";
	}
	/**
	 * 进入友情连接编辑界面
	 * @author lifq
	 * @param id 
	 * @return
	 */
	@RequestMapping(value="toEdit")
	public String toEdit(HttpServletRequest request,HttpServletResponse response,ModelMap map,Integer id){
		Integer siteId=User.getCurrentSiteId(request);
		map.put("siteId", User.getCurrentSiteId(request));
		if (siteId>0&&id!=null) {
			map.put("friendlyLink",friendlyLinkService.load(id));
		}
		return "site/friendlylink/edit";
	}
	
	/**
	 * 新增/修改友情链接
	 * @author lifq
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="save")
	public void save(HttpServletRequest request,HttpServletResponse response,FriendlyLink friendlyLink){
		try {
			Integer siteId=User.getCurrentSiteId(request);
			if (siteId==null||siteId<1||StringUtil.isEmpty(friendlyLink.getName())||StringUtil.isEmpty(friendlyLink.getLinkUrl())) {
				JSONUtil.print(response, Init.FAIL);
			}else{
				if (friendlyLink.getId()==null||friendlyLink.getId()==0) {
					friendlyLink.setSiteId(siteId);
					friendlyLink.setCreateTime(new Date());
					friendlyLink.setUserId(User.getCurrentUserId(request));
					int result= (Integer) friendlyLinkService.save(friendlyLink);
					JSONUtil.print(response, result>0?Init.SUCCEED:Init.FAIL);
				}else{
					Map<String, Object> friendlyLinkMap=new HashMap<String, Object>();
					friendlyLinkMap.put("id", friendlyLink.getId());
					friendlyLinkMap.put("name", friendlyLink.getName());
					friendlyLinkMap.put("linkUrl", friendlyLink.getLinkUrl());
					int result= friendlyLinkService.update(friendlyLinkMap);
					JSONUtil.print(response, result>0?Init.SUCCEED:Init.FAIL);
				}
			}
		} catch (Exception e) {
			JSONUtil.print(response, Init.FAIL);
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除友情链接
	 * @author lifq
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="delete")
	public void delete(HttpServletRequest request,HttpServletResponse response,String ids){
		try {
			Integer siteId=User.getCurrentSiteId(request);
			if (siteId==null||siteId<1||StringUtil.isEmpty(ids)) {
				JSONUtil.print(response, Init.FAIL);
			}else{
				int result= friendlyLinkService.delete(siteId, ids);
				JSONUtil.print(response, result>0?Init.SUCCEED:Init.FAIL);
			}
		} catch (Exception e) {
			JSONUtil.print(response, Init.FAIL);
			e.printStackTrace();
		}
	}
	
	/**
	 * 友情连接列表(不分页)
	 * @author lifq
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="list")
	public void list(HttpServletRequest request,HttpServletResponse response,String keyword){
		Integer siteId=User.getCurrentSiteId(request);
		if (siteId!=null&&siteId>0) {
			List<Map<String, Object>> list=friendlyLinkService.findFriendlyLinkList(siteId, keyword, null, null);
			JSONUtil.printToHTML(response, list);
		}
	}
}
