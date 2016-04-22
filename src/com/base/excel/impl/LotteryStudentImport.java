package com.base.excel.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.config.Init;
import com.base.excel.Import;
import com.base.util.StringUtil;
import com.lottery.service.StudentService;

@Service
public class LotteryStudentImport implements Import{

	@Autowired
	private StudentService studentService;
	
	@Override
	public String invoke(HttpServletRequest request,
			HttpServletResponse response, List<Map<String, Object>> mapList) {
		String lotteryId = request.getParameter("lotteryId");
		
		Set<Integer> removeSet = new HashSet<Integer>();
		String repeatTip = "";
		//去重复帐号(名字和学校相同就表示账号相同)
		for(int i = 0; i < mapList.size(); i++){
			for(int j = i + 1; j < mapList.size(); j++){
//				String iName = mapList.get(i).get("name").toString().trim();
//				String iSchool = mapList.get(i).get("school").toString().trim();
//				String jName = mapList.get(j).get("name").toString().trim();
//				String jSchool = mapList.get(j).get("school").toString().trim();
				
				String iStuCode = mapList.get(i).get("stuCode").toString().trim();
				String jStuCode = mapList.get(j).get("stuCode").toString().trim();
				
				if(iStuCode.equals(jStuCode)){
//					mapList.remove(j);
					String iName = mapList.get(i).get("name").toString();
					String jName = mapList.get(j).get("name").toString();
					repeatTip += "第" + (i + 2) + "行 " + iName + "与" + "第" + (j + 2) + "行" + jName + ",";
					mapList.remove(j);
					removeSet.add(j);
				}
			}
		}
		mapList.removeAll(removeSet);
		
		//检查是否已有相应学生
		ListIterator<Map<String, Object>> listIterator = mapList.listIterator();
		String existName = "";
		while(listIterator.hasNext()){
			Map<String, Object> map = listIterator.next();
			int count = studentService.checkStudent(map.get("stuCode").toString(), Integer.parseInt(lotteryId));
			if(count > 0){
				existName += map.get("name").toString();
				listIterator.remove();
			}else{
				try {
					DateFormat format = new SimpleDateFormat("yyyy/MM/dd"); 
					//不导入摇号号码、序号、
					map.remove("randomNum");
					map.remove("serialNum");
					map.put("lotteryId", lotteryId);
					map.put("status", 0);
					
					if(map.get("birthday") != null && ((String)map.get("birthday")).trim().length() > 0){
						map.put("birthday", format.parse((String) map.get("birthday")));
					}else{
						map.remove("birthday");
					}
				} catch (Exception e) {
					e.printStackTrace();
					return Init.FAIL;
				}
			}
		}
		studentService.saveBatch(mapList);
		System.out.println(repeatTip);
		if(!StringUtil.isEmpty(repeatTip)){
			repeatTip += "学生的全国学籍号相同";
			return repeatTip;
		}else if(!StringUtil.isEmpty(existName)){
			existName += "数据已存在" + existName + "学生的全国学籍号";
			return existName;
		}else{
			return Init.SUCCEED;
		}
	}

	@Override
	public String checkedMethod(HttpServletRequest request,
			Map<String, Object> map) {
		String msg = Init.SUCCEED;
		map.remove("状态");
		if(map.get("姓名") == null || map.get("姓名").toString().trim().equals("")){
			msg = "导入姓名不能为空";
		}
		/*if(map.get("身份证号") == null || map.get("身份证号").toString().trim().equals("")){
			if(Init.SUCCEED.equals(msg)){
				msg = "导入身份证号不能为空";
			}else{
				msg += "导入身份证号不能为空";
			}
		}*/
		/*if(map.get("毕业学校") == null || map.get("毕业学校").toString().trim().equals("")){
			if(Init.SUCCEED.equals(msg)){
				msg = "毕业学校不能为空";
			}else{
				msg += "毕业学校不能为空";
			}
		}*/
		if(map.get("全国学籍号") == null || map.get("全国学籍号").toString().trim().equals("")){
			msg = "全国学籍号不能为空";
		}
		return msg;
	}
	
}
