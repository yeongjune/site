package com.base.excel.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apply.dao.StudentDao;
import com.authority.model.User;
import com.base.excel.Export;

@Service
public class ApplyExport implements Export {
	@Autowired
	private StudentDao studentDao;

	@Override
	public Object invoke(HttpServletRequest request) {
		Integer siteId = User.getCurrentSiteId(request);
		String graduate=request.getParameter("graduate");
		String examYear=request.getParameter("examYear");
		List<Map<String, Object>> list=studentDao.findApplyReport(graduate, examYear, siteId);
		return list;
	}

}
