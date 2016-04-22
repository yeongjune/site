package com.site.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.base.vo.PageList;

/**
 * 短信
 * @author 
 *
 */
public interface SendMsgService {

	/**发送信息
	 * @param mobilesStr 接收信息的电话号码(多个可以用逗号隔开)
	 * @param msgContent 信息内容
	 * @return true发送成功，false发送失败
	 */
	boolean sendMsg(String mobilesStr, String msgContent,Integer siteId);
	
	/**保存短信的发送记录
	 * @param mobilesStr
	 * @param msgContent
	 * @param type 短信类型1=番禺流管办网上信访
	 * @param siteId 站点id
	 * @param mobilePhone 手机号码
	 */
	void saveMsgRecord(String mobilesStr, String msgContent,Integer type,Integer siteId);
	
	/**获取站点短信的总数
	 * @param siteId 站点id
	 * @return
	 */
	Integer getMsgCount(Integer siteId);
	/**获取站点的默认发送的短信内容
	 * @param siteId 站点id
	 * @return
	 */
	Object getMsgContent(Integer siteId);
	/**获取站点的默认发送的短信内容
	 * @param siteId 站点id
	 * @return
	 */
	String getReceivePhone(Integer siteId);
	
	/**返回已发送短信的数量
	 * @param siteId 站点Id
	 * @return
	 */
	Integer getLastCount(Integer siteId);
}
