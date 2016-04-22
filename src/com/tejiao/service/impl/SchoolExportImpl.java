package com.tejiao.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.base.excel.Export;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class SchoolExportImpl implements Export {
    @Override
    public Object invoke(HttpServletRequest request) {
        return null;
    }
    
	public List<String> getArea(HttpServletRequest request){
		List<String> result = new ArrayList<String>();
		result.add("番禺区");
		result.add("越秀区");
		result.add("荔湾区");
		result.add("海珠区");
		result.add("天河区");
		result.add("白云区");
		result.add("黄埔区");
		result.add("花都区");
		result.add("南沙区");
		result.add("萝岗区");
		result.add("从化市");
		result.add("增城市");
		return result;
	}
}
