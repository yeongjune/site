package com.base.excel.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.excel.Export;
import com.base.util.StringUtil;
import com.lottery.model.Student;
import com.lottery.service.StudentService;

/**
 * 导入摇号学生的模板
 * @author ah
 * @2015-3-21
 */
@Service
public class LotteryStudentExport implements Export {

	@Autowired
	private StudentService studentService;
	
	@Override
	public Object invoke(HttpServletRequest request) {
//		Integer siteId = User.getCurrentSiteId(request);
//		String year=request.getParameter("year");
//		//String keyword=request.getParameter("keyword");
//		String ids=request.getParameter("ids");
//		CxStudentSearchVo cxStudentSearchVo=new CxStudentSearchVo();
//		cxStudentSearchVo.setSiteId(siteId);
//		cxStudentSearchVo.setYear(year);
//		if (!StringUtil.isEmpty(ids)) {
//			cxStudentSearchVo.setIds(StringUtil.splitToArray(ids, ","));
//		}
//		
//		List<Map<String, Object>> list=cxStudentDao.getCxStudentList(cxStudentSearchVo);
//		if (list!=null && list.size()>0) {
//			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy年MM月dd日");
//			for (int i = 0; i < list.size(); i++) {
//				list.get(i).put("birthday", dateFormat.format((Date) list.get(i).get("birthday")));
//				list.get(i).put("createTime",  dateFormat.format((Date) list.get(i).get("createTime")));
//			}
//		}
		Integer status = Student.SELECTED;
		String lotteryId = request.getParameter("lotteryId");
		String ids = request.getParameter("ids");
		String statusStr = request.getParameter("status");
		String title = request.getParameter("title");
		String orderStr = request.getParameter("order");
		Integer order = null;
		if(!StringUtil.isEmpty(orderStr))order = Integer.parseInt(orderStr); 
		if(!StringUtil.isEmpty(statusStr)) status = Integer.parseInt(statusStr);
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if("all".equals(ids) && lotteryId != null && lotteryId.trim().length() > 0){
			list = studentService.getByStatus(status, Integer.parseInt(lotteryId), order);
		}else if(ids != null && ids.trim().length() > 0){
			list = studentService.getByIds(ids);
		}
		if (list!=null && list.size()>0) {
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy年MM月dd日");
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				map.remove("birthday");
				map.remove("stuNo");
				
				statusStr = status > 0 ? ( status > 1 ? "候补" : "已选中") : "未选中";
				
				map.put("status", statusStr);
				/*if(map.get("birthday") != null){
					map.put("birthday", dateFormat.format((Date) map.get("birthday")));
				}*/
			}
		}
		LinkedHashMap<String, List<Map<String, Object>>> exportMap = new LinkedHashMap<String, List<Map<String,Object>>>();
		exportMap.put(title, list);
		return exportMap;
	}

}