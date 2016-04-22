package com.apply.action;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import com.apply.model.Xinfang;
import com.apply.service.XinfangService;
import com.authority.model.User;
import com.base.config.Init;
import com.base.util.JSONUtil;
import com.base.vo.PageList;
import com.qq.connect.utils.json.JSONObject;
import com.site.service.SiteService;

/**
 * 信访
 * @author 
 * @time 2015-3-16
 *
 */
@Controller
@RequestMapping(value="xinfangAction")
public class XinfangAction {
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private XinfangService xinfangService;
	
	@RequestMapping( value="save" )
	public void save(HttpServletRequest request,HttpServletResponse response,Xinfang xinfang){
		Map<String, Object> result=new HashMap<String, Object>();
		Integer siteId = siteService.getSiteId(request);
		String valideCode=request.getParameter("code")==null?StringUtils.EMPTY:request.getParameter("code");//验证码
		try {
			if(StringUtils.isNotEmpty(valideCode)) {
				if(valideCode.equals(request.getSession().getAttribute("validCode")+"")) {
					if (siteId>0) {
						Map<String,Object> map = new HashMap<String, Object>();
						map.put("siteId", siteId);
						map.put("createTime", new Date());
						map.put("ip", request.getRemoteAddr());
						map.put("name", xinfang.getName());
						map.put("type",xinfang.getType());
						map.put("companyName",xinfang.getCompanyName());
						map.put("fax",xinfang.getFax());
						map.put("phone",xinfang.getPhone());
						map.put("email",xinfang.getEmail());
						map.put("suggest",xinfang.getSuggest());
						map.put("title",xinfang.getTitle());
						map.put("no", createNo());
						map.put("status", "未回复");
						map.put("auditStatus", "未审核");
						map.put("isDelete", "0");
						xinfangService.save(map,siteId);
						result.put("code", Init.SUCCEED);
						result.put("msg", "申请成功");
					}else{
						result.put("code", Init.FALSE);
						result.put("msg", "未进入任何站点,请刷新");
					}
				}else {
					result.put("flag", "3");
					result.put("msg", "验证码错误");
				}
			}else {
				result.put("flag", "3");
				result.put("msg", "验证码不能为空");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONUtil.printDateTimeHTML(response, result);
	}
	@RequestMapping( value="update" )
	public void update(HttpServletRequest request,HttpServletResponse response,Xinfang xinfang,String id){
		try {
			Map<String,Object> map = xinfangService.find(id);
			map.put("name", xinfang.getName());
			map.put("type",xinfang.getType() );
			map.put("companyName",xinfang.getCompanyName() );
			map.put("fax",xinfang.getFax() );
			map.put("email",xinfang.getEmail() );
			map.put("suggest",xinfang.getSuggest() );
			map.put("title",xinfang.getTitle() );
			
			map.put("id",xinfang.getId());
			map.put("reply",xinfang.getReply() );
			if(!"".equals(xinfang.getReply())){
				map.put("status","已回复");
				map.put("reply",xinfang.getReply() );
				map.put("replyUser",xinfang.getReplyUser());
				map.put("replyTime",new Date() );
			}
			map.put("phone",xinfang.getPhone() );
			map.put("auditStatus",xinfang.getAuditStatus());
			map.remove("tijiao");
			map.remove("huifu");
			xinfangService.update(map);
			JSONUtil.print(response, "succeed");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 跳转到首页
	 * @return
	 */
	@RequestMapping("index")
	public String index(ModelMap map){
		return "apply/xinfang/index";
	}
	/**
	 * 跳转到修改页面
	 * @return
	 */
	@RequestMapping("toEdit")
	public String toEdit(ModelMap map,String id){
		map.put("project", xinfangService.find(id));
		return "apply/xinfang/edit";
	}
	/**
	 * 获取列表数据
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("list")
	public void list(HttpServletRequest request, HttpServletResponse response, Integer currentPage, Integer pageSize,
			String name,String startTime,String endTime,String status,String type1,String type2){
		try {
			currentPage = Init.getCurrentPage(currentPage);
			pageSize = Init.getPageSize(pageSize);
			PageList pageList = xinfangService.getPageList(currentPage, pageSize, name,null,null);
			JSONUtil.printToHTML(response, pageList);
			JSONUtil.print(response, Init.SUCCEED);
		} catch (Exception e) {
			JSONUtil.print(response, Init.FAIL);
			e.printStackTrace();
		}
	}
	/**
	 * 获取列表数据（网站前台显示）
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("getList")
	public void getList(HttpServletRequest request, HttpServletResponse response, Integer currentPage, Integer pageSize,String name){
		try {
			currentPage = Init.getCurrentPage(currentPage);
			pageSize = Init.getPageSize(pageSize);
			PageList pageList = xinfangService.getPageList(currentPage, pageSize, name,"已回复","通过");
			JSONUtil.printToHTML(response, pageList.getList());
		} catch (Exception e) {
			JSONUtil.print(response, Init.FAIL);
			e.printStackTrace();
		}
	}
	/**
	 * 删除 信息
	 * @author
	 * @time 2015-3-17
	 * @param ids
	 */
	@RequestMapping( value="delete" )
	public void delete(HttpServletRequest request,HttpServletResponse response,String ids){
		xinfangService.delete(ids);
		JSONUtil.print(response, "succeed");
	}
	
	/**
	 * 进入 详情界面
	 * @author lfq
	 * @time 2015-3-17
	 * @param request
	 * @param response
	 * @param modelMap
	 * @param id
	 * @return
	 */
	@RequestMapping( value="view")
	public String view(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,String id){
		Integer siteId = User.getCurrentSiteId(request);
		try {
			if (siteId>0 && id!=null) {
				modelMap.put("project", xinfangService.find(id));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "apply/xinfang/view";
	}
	@RequestMapping( value="toWebView")
	public void toWebView(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,String id){
		try {
			Map<String,Object> map = xinfangService.find(id);
			JSONUtil.printToHTML(response, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**创建时间编号
	 * @return(2015-8-24 12:30:09)--20150824123009
	 */
	public String createNo(){
		  Calendar c = Calendar.getInstance();    //获取东八区时间
		  Integer year = c.get(Calendar.YEAR);    //获取年
		  Integer month = c.get(Calendar.MONTH) + 1;   //获取月份，0表示1月份
		  Integer day = c.get(Calendar.DAY_OF_MONTH);    //获取当前天数
		  Integer time = c.get(Calendar.HOUR_OF_DAY);       //获取当前小时
		  Integer min = c.get(Calendar.MINUTE);          //获取当前分钟
		  Integer xx = c.get(Calendar.SECOND);          //获取当前秒
		  String str = year+""+(month.toString().length()==2?month:"0"+month)+(day.toString().length()==2?day:"0"+day)+(time.toString().length()==2?time:"0"+time)+((min.toString().length()==2?min:"0"+min))+((xx.toString().length()==2?xx:"0"+xx));
		  return str;
	}
}
