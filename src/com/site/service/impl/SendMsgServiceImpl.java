package com.site.service.impl;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.dao.SQLDao;
import com.base.util.SendMessagesUtil;
import com.base.util.StringUtil;
import com.base.vo.PageList;
import com.site.model.MsgConfig;
import com.site.model.MsgRecord;
import com.site.service.SendMsgService;
@Service
public class SendMsgServiceImpl implements SendMsgService {

	@Autowired
	private SQLDao dao;

	@Override
	public boolean sendMsg(String mobilesStr, String msgContent,Integer siteId) {
		//调用发送短信接口
		try {
			SendMessagesUtil send = new SendMessagesUtil();
			//剩余短信要大于发送短信数量才可发送
			Integer phoneCount = StringUtil.isEmpty(mobilesStr)?0:mobilesStr.split(",").length;
			String phone[] = mobilesStr.split(",");
			if(this.getLastCount(siteId)>phoneCount){
				send.sendByHttpPost(mobilesStr, msgContent, null);
				//短信发送记录
				for (int i = 0; i < phone.length; i++) {
					this.saveMsgRecord(phone[i], msgContent, 1, siteId);
				}
			}else{
				sendMsg("13725126272", "站点ID为:"+siteId+" 的短信已用完，请及时充值",siteId);
			}

		} catch (Exception e) {
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			System.out.println(f.format(new Date())+":SendMsgServiceImpl.java方法sendMsg发送短信报错!");
			e.printStackTrace();
		}
		return true;
	}


	@Override
	public void saveMsgRecord(String mobilesStr, String msgContent, Integer type,Integer siteId) {
		// TODO Auto-generated method stub
		Map<String,Object> sendMap= new  HashMap<String, Object>();
		sendMap.put("createTime", new Date());
		sendMap.put("content",msgContent);
		sendMap.put("phone", mobilesStr);
		sendMap.put("type", type);
		sendMap.put("siteId", siteId);
		dao.save(MsgRecord.tableName, sendMap);
	}

	@Override
	public Integer getMsgCount(Integer siteId) {
		// TODO Auto-generated method stub
		String sql=" select msgCount from "+MsgConfig.tableName+" where siteId=?";
		return dao.queryForInt(sql,siteId);
	}

	@Override
	public Integer getLastCount(Integer siteId) {
		// TODO Auto-generated method stub
		String sql=" select count(id) from "+MsgRecord.tableName+" where siteId= ?";
		Integer count=dao.queryForInt(sql,siteId);
		if(count==null){
			count = 0;
		}
		Integer lastCount = getMsgCount(siteId)-count;
		return lastCount;
	}

	@Override
	public Object getMsgContent(Integer siteId) {
		String sql=" select msgContent from "+MsgConfig.tableName+" where siteId = ?";
		Map<String,Object> map = dao.queryForMap(sql,siteId);
		return map.get("msgContent");
	}


	@Override
	public String getReceivePhone(Integer siteId) {
		String sql=" select phone from "+MsgConfig.tableName+" where siteId = ?";
		Map<String,Object> map = dao.queryForMap(sql,siteId);
		return map.get("phone").toString();
	}
	
}

