package com.apply.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.apply.model.Certificate;
import com.apply.service.CertificateService;
import com.authority.model.User;
import com.base.config.Init;
import com.base.util.JSONUtil;
import com.base.util.StringUtil;

/**
 * 证件类型管理控制器
 * @author lfq
 *
 */
@Controller
@RequestMapping( value="certificate" )
public class CertificateAction {
	@Autowired
	private CertificateService certificateService;
	/**
	 * 进入证件类型管理界面
	 * @author lifq
	 * @return
	 */
	@RequestMapping( value="index" )
	public String index(HttpServletRequest request,HttpServletResponse response){
		return "apply/certificate/index";
	}
	
	/**
	 * 进入新增/修改证件类型的界面
	 * @author lifq
	 * @param modelMap 
	 * @param id  证件类型ID,修改时需要传该ID
	 * @return
	 */
	@RequestMapping( value="toEdit" )
	public String toEdit(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,Integer id){
		Integer siteId = User.getCurrentSiteId(request);
		if (siteId>0) {
			if (id!=null&&id>0) {
				Certificate certificate=this.certificateService.get(id);
				modelMap.put("certificate", certificate);
			}
		}
		return "apply/certificate/edit";
	}
	
	/**
	 * 新增/修改证件类型
	 * @author lifq
	 * @param id	证件类型ID
	 * @param name	证件名
	 * @param remark 备注
	 */
	@RequestMapping( value="doSave" )
	public void  doSave(HttpServletRequest request,HttpServletResponse response,
			Integer id,String name,String remark){
		Integer siteId = User.getCurrentSiteId(request);
		try {
			if (siteId>0) {
				Map<String, Object>  temp= this.certificateService.findByName(name,siteId);
				if (id!=null&&id>0) {//修改
					if (temp!=null  && !temp.get("id").toString().equals(id.toString())){//判断名字是否已被用
						JSONUtil.print(response,Init.FAIL);
					}else{
						Map<String, Object> certificate=new HashMap<String, Object>();
						certificate.put("id", id);
						certificate.put("name", name);
						certificate.put("remark", remark);
						this.certificateService.update(certificate);
						JSONUtil.print(response,Init.SUCCEED);
					}
				}else{//新增
					if (temp!=null){//判断名字是否已被用
						JSONUtil.print(response,Init.FAIL);
					}else{
						Certificate certificate=new Certificate();
						certificate.setName(name);
						certificate.setRemark(remark);
						certificate.setSiteId(siteId);
						Integer newId= (Integer) this.certificateService.save(certificate);
						JSONUtil.print(response, newId>0?Init.SUCCEED : Init.FAIL);
					}
				}
			}else{
				JSONUtil.print(response,Init.FALSE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JSONUtil.print(response, Init.FAIL);
		}
	}
	
	/**
	 * 删除证件类型
	 * @author lifq
	 * @param ids 证件类型ID，多个用逗号隔开
	 */
	@RequestMapping( value="delete" )
	public void delete(HttpServletRequest request,HttpServletResponse response,String ids){
		try {
			Integer siteId = User.getCurrentSiteId(request);
			if (siteId>0) {
				if (!StringUtil.isEmpty(ids)) {
				    int result=this.certificateService.delete(ids,siteId);
				    JSONUtil.print(response, result>0?Init.SUCCEED : Init.FAIL);
				}else{
					JSONUtil.print(response, Init.FAIL);
				}
			}else{
				JSONUtil.print(response, Init.FALSE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JSONUtil.print(response, Init.FAIL);
		}
	}
	
	/**
	 * 获取证件列表
	 * @author lifq
	 * @param keyword 关键字，根据证件类型名或备注搜索
	 */
	@RequestMapping( value="list" )
	public void list(HttpServletRequest request,HttpServletResponse response,String keyword){
		Integer siteId= User.getCurrentSiteId(request);
		if (siteId>0) {
			List<Map<String, Object>> certificateList=this.certificateService.findCertificateList(keyword, siteId);
			JSONUtil.printToHTML(response, certificateList);
		}
	}
	
	/**
	 * 检索证件名是否可用
	 * @author lifq
	 * @param name 证件
	 * @param id   证件id
	 */
	@RequestMapping( value="checkName")
	public void checkName(HttpServletRequest request,HttpServletResponse response,String name,Integer id){
		try {
			Integer siteId= User.getCurrentSiteId(request);
			if (siteId>0) {
				Map<String, Object>  certificate= this.certificateService.findByName(name,siteId);
				if (certificate!=null && certificate.containsKey("name")) {
					if (id==null) {
						JSONUtil.print(response, Init.FAIL);
					}else if (certificate.get("id").toString().equals(id.toString())){
						JSONUtil.print(response, Init.SUCCEED);
					}else{
						JSONUtil.print(response, Init.FAIL);
					}
				}else{
					JSONUtil.print(response, Init.SUCCEED);
				}
			}else{
				JSONUtil.print(response, Init.FALSE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JSONUtil.print(response, Init.FAIL);
		}
	}
}
