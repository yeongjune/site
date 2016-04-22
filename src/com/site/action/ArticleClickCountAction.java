package com.site.action;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.base.util.CaptchaUitl;
import com.base.util.JSONUtil;
import com.base.util.StringUtil;
import com.site.service.ArticleClickCountService;
import com.site.service.ArticleService;
import com.site.service.ColumnService;
import com.site.service.SendMsgService;
import com.site.service.SiteService;

@Controller
@RequestMapping("articleClickCount")
public class ArticleClickCountAction {
	
	@Autowired
	private ArticleClickCountService articleClickCountService;
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private ColumnService columnService;

	@Autowired
	private SiteService siteService;
	/**
	 * 保存一条点击记录并增加文章的点击数
	 * @param request
	 * @param response
	 * @param articleId 文章ID 
	 * @param userId 用户ID
	 * @param count 用户点击数量
	 * @throws IOException 
	 */
	@RequestMapping("save")
	public void save(HttpServletRequest request, HttpServletResponse response, 
			Integer articleId, Integer userId) throws IOException{
		boolean result = false;
		if(articleId != null && userId != null){
			int obj = articleClickCountService.save(articleId, userId);
			if(obj > 0){
				result = true;
			}else{
				result = false;
			}
		}	
		response.getWriter().print(result);
	}
	
	/**
	 * 批量更新投票记录
	 * @param request
	 * @param response
	 * @param articleIds
	 * @param userId
	 * @param columnId
	 */
	@RequestMapping("batchSave")
	public void batchSave(HttpServletRequest request, HttpServletResponse response, 
			String articleIds, Integer userId, Integer columnId,String mobilePhone){
		if(articleIds != null && userId != null && columnId != null){
			int result = articleClickCountService.updateClickCountBatch(columnId, userId, articleIds,"");//根据用户Id投票
			JSONUtil.printToHTML(response, result);
		}else if(articleIds != null && StringUtils.isNotBlank(mobilePhone) && columnId != null) {
			Map<String, Object> data=new HashMap<String, Object>();
			if(articleClickCountService.checkExistsMobile(mobilePhone, columnId)>0) {
				data.put("msg", "exist");
				JSONUtil.printToHTML(response,data);//已经投过票
			}else {
				data.put("msg", "success");
				int result = articleClickCountService.updateClickCountBatch(columnId, 0, articleIds,mobilePhone);//根据手机号码投票
				JSONUtil.printToHTML(response, data);
			}
		}
	}
	
	@RequestMapping("index")
	public String index(HttpServletRequest request, HttpServletResponse response){
		return "site/clickcount/index";
	}
	
	/**
	 * 获取栏目可投票项数
	 * @param request
	 * @param response
	 * @param columnId
	 */
	@RequestMapping("checkTotalCount")
	public void checkTotalCount(HttpServletRequest request, HttpServletResponse response, Integer columnId){
		if(columnId != null){
			int num = articleClickCountService.checkTotalCount(columnId);
			JSONUtil.printToHTML(response, num);
		}
	}
	
	/**
	 * 更新新闻的截止时间
	 * @param request
	 * @param response
	 * @param ids
	 * @param lastTime
	 */
	@RequestMapping("updateLastTime")
	public void updateLastTime(HttpServletRequest request, HttpServletResponse response, 
			String ids, String lastTime){
		if(ids != null && lastTime != null){
			int result = articleService.updateLastTime(ids, lastTime);
			if(result > 0){
				JSONUtil.printToHTML(response, true);
			}else {
				JSONUtil.printToHTML(response, false);
			}
		}
	}
	
	/**
	 * 获取可投票项目数
	 * @param request
	 * @param response
	 * @param columnId
	 */
	@RequestMapping(value="getVoteNum")
	public void getVoteNum(HttpServletRequest request, HttpServletResponse response, 
			Integer columnId){
		if(columnId != null){
			int result = columnService.getVoteNum(columnId);
			JSONUtil.printToHTML(response, result);
		}
		
	}
	
	/**
	 * 更新可投票项目数
	 * @param request
	 * @param response
	 * @param columnId
	 * @param voteNum
	 */
	@RequestMapping(value="updateVoteNum")
	public void updateVoteNum(HttpServletRequest request, HttpServletResponse response, 
			Integer columnId, Integer voteNum){
		if(columnId != null && voteNum != null){
			int result = columnService.updateVoteNum(columnId, voteNum);
			JSONUtil.printToHTML(response, result);
		}
	}
	
	/**
	 * 获取某用户在某栏目下已经投票的文章项
	 * @param request
	 * @param response
	 * @param columnId
	 * @param userId
	 */
	@RequestMapping(value="getClickedOption")
	public void getClickedOption(HttpServletRequest request, HttpServletResponse response, 
			Integer columnId, Integer userId){
		if(columnId != null && userId != null){
			List<Map<String,Object>> result = articleClickCountService.getClickedOption(columnId, userId);
			JSONUtil.printToHTML(response, result);
		}
		
	}
	/**
	 *  投票发送手机验证码
	 * @param request
	 * @param response
	 * @param mobilePhone 手机号码
	 */
	@RequestMapping(value="getMessage")
	public void saveMessage(HttpServletRequest request, HttpServletResponse response, String mobilePhone) {
		Map<String, Object> result=new HashMap<String, Object>();
		Integer siteId = siteService.getSiteId(request);
		Date captchaTime = (Date)request.getSession().getAttribute("captchaTime");
		String captcha ="";
		if(captchaTime==null||captchaTime.getTime()<(new Date().getTime()-60000)){//1分钟
			captcha = CaptchaUitl.getCaptcha();
			request.getSession().setAttribute("captcha", captcha);
			request.getSession().setAttribute("captchaTime", new Date());
		}else{
			captcha = (String)request.getSession().getAttribute("captcha");
		}
		if(StringUtils.isNotBlank(mobilePhone)) {
			articleClickCountService.saveMobileNo(mobilePhone, captcha, siteId);
	    	if(StringUtil.checkMobilePhone(mobilePhone)) {//验证手机号
	    	}else {
	    		result.put("msg","手机号码不正确，请重新输入");
	    		JSONUtil.printToHTML(response, result);
	    	}
	    }
	}
	/**
	 * 
	 * @param request
	 * @param response
	 * @param mobilePhone 验证成功的手机号码
	 */
	@RequestMapping(value="saveVote")
	public void saveVoteInfo(HttpServletRequest request, HttpServletResponse response, String captcha,String mobilePhone,String code) {
		Map<String, Object> result=new HashMap<String, Object>();
		String sessionCaptcha = (String)request.getSession().getAttribute("captcha");
		Date captchaTime = (Date)request.getSession().getAttribute("captchaTime");
		if(StringUtils.isNotBlank(code)) {
			if(captchaTime!=null&&captchaTime.getTime()>(new Date().getTime()-600000)&&captcha!=null&&captcha.equals(sessionCaptcha)){//10分钟
				if(StringUtils.isNotBlank(mobilePhone)) {
					if(StringUtil.checkMobilePhone(mobilePhone)) {//验证手机号
						request.getSession().setAttribute("captcha",CaptchaUitl.getCaptcha());//验证成功 那么验证码就失效
					}else {
						result.put("msg","手机号码不正确，请重新输入");
						JSONUtil.printToHTML(response, result);
					}
				}
			}else {
				result.put("msg","手机验证码错误，请重新输入");
				JSONUtil.printToHTML(response, result);
			}			
		}else {
			result.put("msg","验证码不能为空");
			JSONUtil.printToHTML(response,result);
		}
	}
	
}
