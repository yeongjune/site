package com.base.excel.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apply.dao.CxStudentDao;
import com.apply.vo.CxStudentSearchVo;
import com.authority.model.User;
import com.base.excel.Export;
import com.base.util.StringUtil;

/**
 * 导出集贤小学报名数据
 * @author lfq
 * @2015-3-21
 */
@Service
public class CxStudentExport implements Export {
	@Autowired
	private CxStudentDao cxStudentDao;

	@Override
	public Object invoke(HttpServletRequest request) {
		Integer siteId = User.getCurrentSiteId(request);
		String year=request.getParameter("year");
		//String keyword=request.getParameter("keyword");
		String ids=request.getParameter("ids");
		CxStudentSearchVo cxStudentSearchVo=new CxStudentSearchVo();
		cxStudentSearchVo.setSiteId(siteId);
		cxStudentSearchVo.setYear(year);
		if (!StringUtil.isEmpty(ids)) {
			cxStudentSearchVo.setIds(StringUtil.splitToArray(ids, ","));
		}
		
		List<Map<String, Object>> list=cxStudentDao.getCxStudentList(cxStudentSearchVo);
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