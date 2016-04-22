package com.lottery.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.apply.vo.StudentSearchVo;
import com.base.config.Init;
import com.base.util.JSONUtil;
import com.lottery.model.Lottery;
import com.lottery.service.LotteryService;

@Controller
@RequestMapping("lottery")
public class LotteryAction {

	@Autowired
	private LotteryService lotteryService;
	
	/**
	 * 跳转到首页
	 * @return
	 */
	@RequestMapping("index")
	public String index(ModelMap map){
		List<Map<String, Object>> list = lotteryService.getList();
		map.put("list", list);
		/*if(list.size() < 2 && list.size() > 0 ){
			Map<String, Object> lottery = list.get(0);
			if(lottery.get("step") != null  && ((Integer)lottery.get("step")) > 0){
				map.put("id", lottery.get("id"));
				map.put("step", lottery.get("step"));
				if(((Integer) lottery.get("step")) > 2){
					map.put("hide", true);
				}
				return "lottery/student/list";
			}
		}*/
		return "lottery/lottery/index";
	}

	/**
	 * 加载id对应摇号的信息
	 * @param request
	 * @param response
	 * @param id
	 */
	@RequestMapping("load")
	public void load(HttpServletRequest request, HttpServletResponse response, Integer id){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(id != null){
			Map<String, Object> lottery = lotteryService.load(id);
			Integer selected = 0;
			if(lottery.get("selected") != null){
				selected = Integer.parseInt(lottery.get("selected") + "");
			}else{
				lottery.put("selected", 0);
			}
			Integer number = (Integer) lottery.get("number");
			
//			Integer count = number%times > 0 ? (number/times + 1) : number/times;//每次需要选出的人数
//			Integer lotteryTimes = selected%count > 0 ? (selected/count + 1) : (selected/count);//计算已经摇号的次数
//			lottery.put("lotteryTimes",  lotteryTimes);
			
			lottery.put("selected", selected);
			lottery.put("number", number);
			resultMap.put("lottery", lottery);
			resultMap.put("code", Init.SUCCEED);
		}else{
			resultMap.put("code", Init.FAIL);
		}
		JSONUtil.printToHTML(response, resultMap);
	}
	
	/**
	 * 跳转到首页
	 * @return
	 */
	@RequestMapping("lotterys")
	public String lotterys(){
		return "lottery/lottery/list";
	}
	
	/**
	 * 首页列表
	 * @param request
	 * @param response
	 * @param currentPage
	 * @param pageSize
	 * @param searchVo
	 */
	@RequestMapping("list")
	public void list(HttpServletRequest request, HttpServletResponse response,Integer currentPage,Integer pageSize,StudentSearchVo searchVo){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = this.lotteryService.getList();
		resultMap.put("list", list);
		resultMap.put("code", Init.SUCCEED);
		JSONUtil.printToHTML(response, resultMap);
	}
	
	/**
	 * 跳转到新建页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("toAdd")
	public String toAdd(HttpServletRequest request, HttpServletResponse response){
		return "lottery/lottery/add";
	}

	/**
	 * 新建保存
	 * @param request
	 * @param response
	 * @param lottery
	 */
	@RequestMapping("save")
	public void save(HttpServletRequest request, HttpServletResponse response, Lottery lottery){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Integer id = lotteryService.save(lottery);
		if(id > 0){
			resultMap.put("id", id);
			resultMap.put("code", Init.SUCCEED);
		}else{
			resultMap.put("code", Init.FAIL);
		}
		JSONUtil.printToHTML(response, resultMap);
	}
	
	/**
	 * 跳转到修改页面
	 * @param request
	 * @param response
	 * @param id
	 * @param map
	 * @return
	 */
	@RequestMapping("toEdit")
	public String toEdit(HttpServletRequest request, HttpServletResponse response, Integer id, ModelMap map){
		Lottery lottery = lotteryService.get(id);
		map.put("lottery", lottery);
		return "lottery/lottery/add";
	}

	/**
	 * 修改
	 * @param request
	 * @param response
	 * @param lottery
	 */
	@RequestMapping("update")
	public void update(HttpServletRequest request, HttpServletResponse response, Lottery lottery){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Integer count = lotteryService.update(lottery);
		if(count > 0){
			resultMap.put("code", Init.SUCCEED);
		}else{
			resultMap.put("code", Init.FAIL);
		}
		JSONUtil.printToHTML(response, resultMap);
	}

	/**
	 * 删除
	 * @param request
	 * @param response
	 * @param ids
	 */
	@RequestMapping("delete")
	public void delete(HttpServletRequest request, HttpServletResponse response, String ids){
		Integer count = lotteryService.delete(ids);
		if(count > 0){
			JSONUtil.print(response, Init.SUCCEED);
		}else{
			JSONUtil.print(response, Init.FAIL);
		}
	}
	
	@RequestMapping("lotteryNum")
	public String lotteryNum(ModelMap map, Integer id){
		map.put("id", id);
		return "lottery/lottery/lotteryNum";
	}
	
	/**
	 * 摇号操作(广二外用)
	 */
	@RequestMapping("processLottery")
	public void processLottery(HttpServletRequest request, HttpServletResponse response, Integer lotteryId){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> lottery = lotteryService.load(lotteryId);
		Integer selected = 0;
		if(lottery.get("selected") != null){
			selected = Integer.parseInt(lottery.get("selected") + "");//已选中人数
		}
		Integer number = (Integer) lottery.get("number");
		if(selected < number){//选中人数小于需要选出的人数才能进行摇号
			List<String> names = lotteryService.processLottery(lotteryId, number - selected);
			resultMap.put("code", Init.SUCCEED);
			resultMap.put("num", names.size());
			resultMap.put("names", names);
		}
		JSONUtil.printToHTML(response, resultMap);
	}
	
	/**
	 * 摇号操作第一步,生成随机数(南沙教育局)
	 */
	@RequestMapping("processLottery1")
	public void processLottery1(HttpServletRequest request, HttpServletResponse response, Integer lotteryId){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> lottery = lotteryService.load(lotteryId);
		if(lottery.get("number") == null || lottery.get("number").toString().equals("")){
			lotteryService.processLottery1(lotteryId);
			resultMap.put("code", Init.SUCCEED);
		}else{
			resultMap.put("code", Init.FAIL);
			resultMap.put("msg", "已进行摇号操作，不能重复摇号！");
		}
		JSONUtil.printToHTML(response, resultMap);
	}

	/**
	 * 摇号后跳转到列表页面
	 */
	@RequestMapping("toList")
	public String toList(ModelMap map, Integer id){
		Lottery lottery = lotteryService.get(id);
		map.put("step", lottery.getStep());
		if(lottery.getStep() > 2){
			map.put("hide", true);
		}
		map.put("id", id);
		return "lottery/student/list";
	}
	
	/**
	 * 排序操作，生成序号
	 */
	@RequestMapping("processLottery2")
	public void processLottery2(HttpServletResponse response, Integer id){
		lotteryService.processLottery2(id);
		JSONUtil.print(response, Init.SUCCEED);
	}
	
	/**
	 * 摇号操作第三步，设置摇号人数(南沙教育局)
	 */
	@RequestMapping("processLottery3")
	public void processLottery3(HttpServletRequest request, HttpServletResponse response, Integer id, Integer number){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", "1");
		Lottery lottery = lotteryService.get(id);
		if(lottery.getStep() == 2){
			lotteryService.processLottery3(id, number);
			resultMap.put("code", Init.SUCCEED);
		}else{
			resultMap.put("msg", "必须先进行排序才能设置摇号人数");
		}
		JSONUtil.printToHTML(response, resultMap);
	}
	
}
