package com.lottery.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import com.apply.vo.StudentSearchVo;
import com.base.config.Init;
import com.base.util.JSONUtil;
import com.base.vo.PageList;
import com.lottery.model.Lottery;
import com.lottery.model.Student;
import com.lottery.service.LotteryService;
import com.lottery.service.StudentService;

@Controller("lotteryStudent")
@RequestMapping("lottery/student")
public class StudentAction {

	@Autowired
	private StudentService studentService;

	@Autowired
	private LotteryService lotteryService;
	
	@RequestMapping("index")
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap map){
		List<Map<String, Object>> list = lotteryService.getList();
		map.put("list", list);
		return "lottery/student/index";
	}

	@RequestMapping("list")
	public void list(HttpServletRequest request, HttpServletResponse response,Integer currentPage,Integer pageSize,StudentSearchVo searchVo, Integer lotteryId, Integer order){
		PageList pageList= this.studentService.findStudentPageList(currentPage, pageSize, searchVo,lotteryId, order);
		/*if(step != null && step == 2){
			//更新摇号步奏
			Map<String, Object> uMap = new HashMap<String, Object>();
			uMap.put("id", lotteryId);
			uMap.put("step", 2);
			lotteryService.update(uMap);
		}*/
		JSONUtil.printDatePatternHTML(response, pageList, "yyyy-MM-dd");
	}
	
	@RequestMapping("getList")
	public void getList(HttpServletRequest request, HttpServletResponse response,Integer currentPage,Integer pageSize,Integer lotteryId){
		Lottery lottery = this.lotteryService.get(lotteryId);
		Integer order = null;
		if(lottery.getStep() >= 2){
			order = 1;
		}
		PageList pageList= this.studentService.findStudentPageList(currentPage, pageSize, null, lotteryId, order);
		JSONUtil.printDatePatternHTML(response, pageList, "yyyy-MM-dd");
	}
	
	@RequestMapping("add")
	public String add(HttpServletRequest request, HttpServletResponse response, ModelMap map){
		try {
			List<Map<String, Object>> list = lotteryService.getList();
			map.put("list", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/lottery/student/add";
	}
	
	@RequestMapping("save")
	public void save(HttpServletRequest request, HttpServletResponse response, Student student){
		List<Map<String, Object>> list = studentService.getByIDCard(student.getIDCard(), student.getLotteryId());
		Map<String, Object> result = new HashMap<String, Object>();
		if(list.size() > 0){
			result.put("code", Init.FAIL);
		}else{
			Integer id = studentService.save(student);
			result.put("id", id);
			result.put("code", Init.SUCCEED);
		}
		JSONUtil.printToHTML(response, result);
	}
	
	@RequestMapping("toEdit")
	public String toEdit(HttpServletRequest request, HttpServletResponse response, Integer id, ModelMap map){
		Student student = studentService.get(id);
		map.put("student", student);
		List<Map<String, Object>> list = lotteryService.getList();
		map.put("list", list);
		return "/lottery/student/add";
	}
	
	@RequestMapping("update")
	public void update(HttpServletRequest request, HttpServletResponse response, Student student){
		studentService.update(student);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", Init.SUCCEED);
		JSONUtil.printToHTML(response, result);
	}
	
	@RequestMapping("delete")
	public void delete(HttpServletRequest request, HttpServletResponse response, String ids){
		Integer count = studentService.delete(ids);
		if(count > 0){
			JSONUtil.print(response, Init.SUCCEED);
		}else{
			JSONUtil.print(response, Init.FAIL);
		}
	}
	
	@RequestMapping("result1")
	public String result1(ModelMap map){//打印页面
		List<Map<String, Object>> list = lotteryService.getList();
		map.put("list", list);
		map.put("status", Student.SELECTED);
		return "lottery/result/index";
	}

	@RequestMapping("result2")
	public String result2(ModelMap map){//打印页面
		List<Map<String, Object>> list = lotteryService.getList();
		map.put("list", list);
		map.put("status", Student.UNSELECT);
		return "lottery/result/index";
	}

	@RequestMapping("result3")
	public String result3(ModelMap map){//打印页面
		List<Map<String, Object>> list = lotteryService.getList();
		map.put("list", list);
		map.put("status", Student.WAITING);
		return "lottery/result/index";
	}
	
	@RequestMapping("resultList")
	public void resultList(HttpServletRequest request, HttpServletResponse response,Integer currentPage,Integer pageSize,StudentSearchVo searchVo, Integer lotteryId, Integer order){
		PageList pageList= this.studentService.findStudentPageList(currentPage, pageSize, searchVo,lotteryId, order);
		JSONUtil.printDatePatternHTML(response, pageList, "yyyy-MM-dd");
	}
	
	@RequestMapping("toView")
	public String toView(Integer id, ModelMap map){
		Student student = studentService.get(id);
		map.put("student", student);
		map.put("lottery", lotteryService.get(student.getLotteryId()));
		return "lottery/student/view";
	}
	
	public String toResultCheck(){
		return "lottery/student/check";
	}
	
	@RequestMapping("checkResult")
	public void checkResult(HttpServletRequest request, HttpServletResponse response, String IDCard, Integer lotteryId){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> student = studentService.getByIDCard(IDCard, lotteryId);
		resultMap.put("code", Init.SUCCEED);
		resultMap.put("student", student);
		JSONUtil.printDateTimeHTML(response, resultMap);
	}
	
	@RequestMapping("toCheck")
	public String toCheck(String  IDCard, Integer lotteryId,ModelMap map){
		List<Map<String, Object>> list = lotteryService.getList();
		map.put("list", list);
		return "lottery/check";
	}

	@RequestMapping("check")
	public void check(String  IDCard, Integer lotteryId, HttpServletResponse response){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(IDCard != null && IDCard.trim().length() > 0 && lotteryId != null){
			List<Map<String, Object>> students = studentService.getByIDCard(IDCard, lotteryId);
			resultMap.put("student", students.size() > 0 ? students.get(0) : null);
			resultMap.put("code", Init.SUCCEED);
		}else{
			resultMap.put("code", Init.FAIL);
		}
		JSONUtil.printDatePatternHTML(response, resultMap, "yyyy-MM-dd");
	}
	
	@InitBinder  
	   public void initBinder(WebDataBinder binder) {  
	       SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
	       dateFormat.setLenient(false);  
	       binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));  
	   }  
}
