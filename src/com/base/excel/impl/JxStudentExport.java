package com.base.excel.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apply.dao.JxStudentDao;
import com.apply.vo.JxStudentSearchVo;
import com.authority.model.User;
import com.base.excel.Export;
import com.base.util.StringUtil;

/**
 * 导出集贤小学报名数据
 * @author lfq
 * @2015-3-21
 */
@Service
public class JxStudentExport implements Export {
	@Autowired
	private JxStudentDao jxStudentDao;

	@Override
	public Object invoke(HttpServletRequest request) {
		Integer siteId = User.getCurrentSiteId(request);
		String year=request.getParameter("year");
		//String keyword=request.getParameter("keyword");
		String ids=request.getParameter("ids");
		JxStudentSearchVo jxStudentSearchVo=new JxStudentSearchVo();
		jxStudentSearchVo.setSiteId(siteId);
		jxStudentSearchVo.setYear(year);
		if (!StringUtil.isEmpty(ids)) {
			jxStudentSearchVo.setIds(StringUtil.splitToArray(ids, ","));
		}
		
		List<Map<String, Object>> list=jxStudentDao.getJxStudentList(jxStudentSearchVo);
		if (list!=null && list.size()>0) {
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy年MM月dd日");
			for (int i = 0; i < list.size(); i++) {
				list.get(i).put("birthday", dateFormat.format((Date) list.get(i).get("birthday")));
				list.get(i).put("createTime",  dateFormat.format((Date) list.get(i).get("createTime")));
			}
		}
		return list;
	}

}