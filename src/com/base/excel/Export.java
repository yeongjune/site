package com.base.excel;

import javax.servlet.http.HttpServletRequest;

public interface Export {

	/**
	 * 返回值说明：
	 * 1：如果要导出多份带标题数据，返回：LinkedHashMap＜String, List＜LinkedHashMap＜String, Object＞＞＞
	 * 	外层LinkedHashMap的key为标题，value（List）将在xls文件创建一个数据区域
	 * 2：如果要导出不带标题数据，返回：List＜LinkedHashMap＜String, Object＞＞
	 * 3：如果要导出一份不带标题数据，返回：List＜LinkedHashMap＜String, Object＞＞
	 * 4：无数据模板导出，返回null
	 * @param request
	 * @return
	 */
	Object invoke(HttpServletRequest request);

}
