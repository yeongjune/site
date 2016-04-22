package com.site.action;

import java.io.File;
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
import com.site.model.Contact;
import com.site.service.ContactService;


/**
 * 联系方式
 * @author lfq
 *
 */
@Controller
@RequestMapping(value="contact")
public class ContactAction {
	@Autowired
	private ContactService contactService;
	
	/**
	 * 进入后台联系方式管理界面
	 * @author lifq
	 * @return
	 */
	@RequestMapping(value="index")
	public String index(HttpServletRequest request,HttpServletResponse response,ModelMap map){
		map.put("siteId", User.getCurrentSiteId(request));
		return "site/contact/index";
	}
	
	/**
	 * 进入联系方式编辑界面
	 * @author lifq
	 * @param id 
	 * @return
	 */
	@RequestMapping(value="toEdit")
	public String toEdit(HttpServletRequest request,HttpServletResponse response,ModelMap map,Integer id){
		Integer siteId=User.getCurrentSiteId(request);
		map.put("siteId", User.getCurrentSiteId(request));
		if (siteId>0&&id!=null) {
			map.put("contact",contactService.load(id));
		}
		return "site/contact/edit";
	}
	
	/**
	 * 新增/修改联系放肆
	 * @author lifq
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="save")
	public void save(HttpServletRequest request,HttpServletResponse response,Contact contact){
		try {
			Integer siteId=User.getCurrentSiteId(request);
			if (siteId==null||siteId<1||StringUtil.isEmpty(contact.getType())
					||StringUtil.isEmpty(contact.getTitle())||StringUtil.isEmpty(contact.getTypeValue())) {
				JSONUtil.print(response, Init.FAIL);
			}else{
				if (contact.getId()==null||contact.getId()==0) {
					contact.setSiteId(siteId);
					contact.setOrderSort(contactService.getNewOrderSort(siteId));
					int result= (Integer) contactService.save(contact);
					JSONUtil.print(response, result>0?Init.SUCCEED:Init.FAIL);
				}else{
					Contact oldContact=contactService.get(contact.getId());
					
					Map<String, Object> contactMap=new HashMap<String, Object>();
					contactMap.put("id", contact.getId());
					contactMap.put("title", contact.getTitle());
					contactMap.put("type", contact.getType());
					contactMap.put("typeValue", contact.getTypeValue());
					int result= contactService.update(contactMap);
					if (result>0 && oldContact.getType().equals("二维码") && !oldContact.getTypeValue().equals(contact.getTypeValue())) {
						String root=request.getSession().getServletContext().getRealPath("/");
						File file=new File(root+"/"+oldContact.getTypeValue());
						if (file.exists()&&file.isFile()) {
							file.delete();
						}
					}
					JSONUtil.print(response, result>0?Init.SUCCEED:Init.FAIL);
				}
			}
		} catch (Exception e) {
			JSONUtil.print(response, Init.FAIL);
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除联系方式
	 * @author lifq
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="delete")
	public void delete(HttpServletRequest request,HttpServletResponse response,String id){
		try {
			Integer siteId=User.getCurrentSiteId(request);
			if (siteId==null||siteId<1||StringUtil.isEmpty(id)) {
				JSONUtil.print(response, Init.FAIL);
			}else{
				Contact oldContact=contactService.get(Integer.parseInt(id));
				int result= contactService.delete(siteId, id);
				if (result>0 && oldContact.getType().equals("二维码")) {//删除文件
					String root=request.getSession().getServletContext().getRealPath("/");
					File file=new File(root+"/"+oldContact.getTypeValue());
					if (file.exists()&&file.isFile()) {
						file.delete();
					}
				}
				JSONUtil.print(response, result>0?Init.SUCCEED:Init.FAIL);
			}
		} catch (Exception e) {
			JSONUtil.print(response, Init.FAIL);
			e.printStackTrace();
		}
	}
	/**
	 * 联系方式列表(不分页)
	 * @author lifq
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="list")
	public void list(HttpServletRequest request,HttpServletResponse response){
		Integer siteId=User.getCurrentSiteId(request);
		if (siteId!=null&&siteId>0) {
			List<Map<String, Object>> list=contactService.findContactList(siteId);
			JSONUtil.printToHTML(response, list);
		}
	}
	

	/**
	 * 改变联系方式排序
	 * @author lifq
	 * @param id 
	 * @param upOrdown 1上移，-1下移
	 */
	@RequestMapping(value="changeOrder")
	public void changeOrder(HttpServletRequest request,HttpServletResponse response,Integer id,Integer upOrdown){
		Integer siteId=User.getCurrentSiteId(request);
		if (siteId!=null&&siteId>0) {
			int result=contactService.updateOrderSort(id, upOrdown);
			JSONUtil.print(response, result>0?Init.SUCCEED:Init.FAIL);
		}
	}
}
