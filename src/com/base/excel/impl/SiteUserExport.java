package com.base.excel.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.base.excel.Export;

@Service
public class SiteUserExport implements Export {

	@Override
	public Object invoke(HttpServletRequest request) {
		return "succeed";
	}

}
