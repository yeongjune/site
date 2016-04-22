package com.site.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.authority.model.User;
import com.base.config.Init;
import com.base.util.JSONUtil;
import com.base.vo.PageList;
import com.site.model.Message;
import com.site.service.MessageService;
import com.site.service.SiteService;

/**
 * 留言版处理
 * 
 * @author ah
 *
 */
@Controller
@RequestMapping("message")
public class MessageAction {
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private SiteService siteService;
	
	@RequestMapping("save")
	public void save(HttpServletRequest request, HttpServletResponse response, Message message){
		Integer siteId = null;
		if(message.getSiteId()!=null ||( message.getSiteId()!=null && message.getSiteId() > 0) ){
			siteId = message.getSiteId();
		}else {
			siteId = siteService.getSiteId(request);
		}
		String result=Init.FAIL;
		if (siteId!=null&&siteId>0) {
			message.setSiteId(siteId);
			message.setCreateTime(new Date());
			int newId= messageService.save(message);
			result = newId > 0 ? Init.SUCCEED : Init.FAIL;
		}
		JSONUtil.print(response, result);
	}
	@RequestMapping("saveMessage")
	public void saveMessage(HttpServletRequest request, HttpServletResponse response, Message message){
		String valideCode=request.getParameter("code")==null?StringUtils.EMPTY:request.getParameter("code");//验证码
		Integer siteId = null;
		if(message.getSiteId()!=null ||( message.getSiteId()!=null && message.getSiteId() > 0) ){
			siteId = message.getSiteId();
		}else {
			siteId = siteService.getSiteId(request);
		}
		String result=Init.FAIL;
			if(StringUtils.isNotEmpty(valideCode)&&StringUtils.equalsIgnoreCase(valideCode, request.getSession().getAttribute("validCode")+"")) {
				if (siteId!=null&&siteId>0) {
					message.setSiteId(siteId);
					message.setCreateTime(new Date());
					int newId= messageService.save(message);
					result = newId > 0 ? Init.SUCCEED : Init.FAIL;
				}				
			}
		JSONUtil.print(response, result);
	}
	@RequestMapping("index")
	public String index(HttpServletRequest request, HttpServletResponse response){
		return "/site/message/index";
	}
	
	@RequestMapping("list")
	public void list(HttpServletRequest request, HttpServletResponse response, String keyword, Integer currentPage, Integer pageSize){
		Integer siteId = User.getCurrentSiteId(request);
		if (siteId!=null&&siteId>0) {
			if(currentPage == null){
				currentPage = Integer.parseInt((String) Init.get("currentPage").get("value"));
			}
			if(pageSize == null){
				pageSize = Integer.parseInt((String) Init.get("pageSize").get("value"));
			}
			PageList pageList = messageService.list(siteId, keyword,currentPage,pageSize);
			JSONUtil.printDateTimeHTML(response, pageList);
		}
	}
	
	@RequestMapping("delete")
	public void delete(HttpServletRequest request, HttpServletResponse response, String ids){
		messageService.delete(ids);
		JSONUtil.print(response, Init.SUCCEED);
	}
	
	@RequestMapping("checkAndReply")
	public String checkAndReply(HttpServletRequest request, HttpServletResponse response){
		return "/site/message/checkAndReply";
	}
	
	@RequestMapping("history")
	public String history(HttpServletRequest request, HttpServletResponse response){
		return "/site/message/history";
	}
	
	/**
	 * 通过ID获取子留言
	 * @param request
	 * @param response
	 * @param pid
	 * @param currentPage
	 * @param pageSize
	 */
	@RequestMapping("getMsgById")
	public void getMsgById(HttpServletRequest request, HttpServletResponse response, 
			Integer id){
			if(id != null){
				Map<String, Object> obj = messageService.getMsgById(id);
				JSONUtil.printToHTML(response, obj);
			}
	}
	
	/**
	 * 更新留言回复
	 * @param request
	 * @param response
	 * @param id
	 * @param Content
	 */
	@RequestMapping("updateReply")
	public void updateReply(HttpServletRequest request, HttpServletResponse response, 
			Integer id, String content){
		if(id != null && content != null){
			int result = messageService.updateReply(id, content);
			if(result > 0){
				JSONUtil.printToHTML(response, true);
			}else{
				JSONUtil.printToHTML(response, false);
			}
		}
	}
	
	/**
	 * 通过用户名称和
	 * @param request
	 * @param response
	 * @param name
	 * @param createTime
	 * @param currentPage
	 * @param pageSize
	 */
	@RequestMapping("getHistory")
	public void getHistory(HttpServletRequest request, HttpServletResponse response,
			String name, String createTime, Integer siteId, Integer currentPage, Integer pageSize){
		if(name != null && createTime != null){
			
			siteId = siteId == null ? siteService.getSiteId(request) : siteId;
			//将createTime转为date类型
			Date date = null;
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				date = sdf.parse(createTime);
			} catch (ParseException e) {
				e.printStackTrace();
				JSONUtil.printToHTML(response, "false");
			}
			//获取分页数据
			currentPage = currentPage == null ? Integer.parseInt((String) Init.get("currentPage").get("value")) : currentPage;
			pageSize = pageSize == null ? 5 : pageSize;
			//执行查询
			PageList pageList = messageService.getHistory(siteId, name, date, currentPage, pageSize);
			JSONUtil.printToHTML(response, pageList);
		}
		
	}
	
	@RequestMapping("getMsgByName")
	public void getMsgByName(HttpServletRequest request, HttpServletResponse response,
			String name, Integer siteId, Integer currentPage, Integer pageSize){
		if(name != null){
			siteId = siteId == null ? siteService.getSiteId(request) : siteId;
			currentPage = currentPage == null ? Integer.parseInt((String) Init.get("currentPage").get("value")) : currentPage;
			pageSize = pageSize == null ? 5 : pageSize;
			
			PageList pageList = messageService.getMsgByName(siteId, name, currentPage, pageSize);
			JSONUtil.printToHTML(response, pageList);
		}
	}
	
}
