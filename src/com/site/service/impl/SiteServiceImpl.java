package com.site.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.site.model.MsgConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.authority.dao.RoleDao;
import com.authority.dao.SiteRoleDao;
import com.authority.dao.UserDao;
import com.authority.dao.UserRoleDao;
import com.base.config.Init;
import com.base.util.CryptUtil;
import com.site.dao.ArticleDao;
import com.site.dao.ColumnDao;
import com.site.dao.DataDao;
import com.site.dao.RecommendDao;
import com.site.dao.SiteDao;
import com.site.dao.TemplateDataDao;
import com.site.dao.TempletDao;
import com.site.service.SiteService;

@Service
public class SiteServiceImpl implements SiteService {

	@Autowired
	private SiteDao dao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private UserRoleDao userRoleDao;
	@Autowired
	private SiteRoleDao siteRoleDao;
	@Autowired
	private TempletDao templateDao;
	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private ColumnDao columnDao;
	@Autowired
	private DataDao dataDao;
	@Autowired
	private RecommendDao recommendDao;
	@Autowired
	private TemplateDataDao templateDataDao;

	@Override
	public Integer getSiteId(String domain) {
		return dao.getSiteId(domain);
	}

	@Override
	public Map<String, Object> getListByPage(Integer currentPage,
			Integer pageSize, String keyword) {
		return dao.getListByPage(currentPage, pageSize, keyword);
	}

	@Override
	public Serializable save(String name, String domain, String directory) {
		Map<String, Object> map = new HashMap<String, Object>(0);
		map.put("name", name);
		map.put("domain", domain);
		map.put("directory", directory);
		map.put("open", 1);
		Date d = new Date();
		map.put("createTime", d);
		map.put("updateTime", d);
		Serializable id = dao.save(map);
		
		Map<String, Object> user = new HashMap<String, Object>();
		user.put("account", "admin");
		user.put("name", "管理员");
		user.put("siteId", id);
		user.put("password", CryptUtil.MD5encrypt("riicy6868"));
		user.put("enable", 1);
		user.put("createTime", d);
		user.put("updateTime", d);
		userDao.save(user);
		
		List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
		String[] templateArray = new String[]{"index", "list", "article", "single"};
		for (String string : templateArray) {
			Map<String, Object> template = new HashMap<String, Object>();
			template.put("siteId", id);
			template.put("name", string);
			mapList.add(template);
		}
		templateDao.save(mapList);
		
		return id;
	}

	@Override
	public int delete(Integer id) {
		roleDao.deleteBySiteId(id);
		userDao.deleteBySiteId(id);
		userRoleDao.deleteBySiteId(id);
		siteRoleDao.deleteBySiteId(id);
		templateDao.deleteBySiteId(id);
		articleDao.deleteBySiteId(id);
		columnDao.deleteBySiteId(id);
		dataDao.deleteBySiteId(id);
		recommendDao.deleteBySiteId(id);
		templateDataDao.deleteBySiteId(id);
		return dao.delete(id);
	}

	@Override
	public int update(Integer id, String name, String domain, String directory, String isUseCheck) {
		Map<String, Object> map = new HashMap<String, Object>(0);
		map.put("id", id);
		map.put("name", name);
		map.put("domain", domain);
		map.put("directory", directory);
		map.put("isUseCheck", isUseCheck);
		Date d = new Date();
		map.put("updateTime", d);
		return dao.update(map);
	}

	@Override
	public Map<String, Object> load(Integer id) {
		return dao.load(id);
	}

	@Override
	public long countByDomainWithSelf(Integer id, String domain) {
		return dao.countByDomainWithSelf(id, domain);
	}

	@Override
	public long countByDomain(String domain) {
		return dao.countByDomain(domain);
	}

	@Override
	public List<Map<String, Object>> getList() {
		return dao.getList();
	}

	@Override
	public int updateOpen(Integer id, Integer status) {
		Map<String, Object> map = new HashMap<String, Object>(0);
		map.put("id", id);
		map.put("open", status);
		return dao.update(map);
	}

	@Override
	public Map<String, Integer> getSiteDomain() {
		List<Map<String, Object>> mapList = getList();
		if(mapList==null || mapList.isEmpty())return null;
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (Map<String, Object> site : mapList) {
			map.put((String)site.get("domain"), (Integer) site.get("id"));
		}
		return map;
	}

	@Override
	public long countByDirectoryWithSelf(Integer id, String directory) {
		return dao.countByDirectoryWithSelf(id, directory);
	}

	@Override
	public long countByDirectory(String directory) {
		return dao.countByDirectory(directory);
	}

	@Override
	public int update(Map<String, Object> map) {
		return dao.update(map);
	}

	@Override
	public Integer getSiteId(HttpServletRequest request) {
//		String domain = request.getRequestURL().toString().replace("http://", "").replace(request.getRequestURI(), "");
		String domain = request.getServerName();
		Integer siteId = dao.getSiteId(domain);
		if(siteId == 0 && !domain.startsWith("www")){
			siteId = dao.getSiteId("www." + domain);
		}
		if (siteId>0) {
			if (request.getSession().getAttribute(Init.SITE)==null) {
				Map<String, Object> site=dao.load(siteId);
				request.getSession().setAttribute(Init.SITE, site);
			}else {
				@SuppressWarnings("unchecked")
				Map<String, Object> sessionSite=(Map<String, Object>) request.getSession().getAttribute(Init.SITE);
				if (!sessionSite.get("id").equals(siteId)) {
					Map<String, Object> site=dao.load(siteId);
					request.getSession().setAttribute(Init.SITE, site);
				}
			}
		}else{
			request.getSession().removeAttribute(Init.SITE);
		}
		return siteId;
	}

    @Override
    public void saveOrUpdateSMSConfigPhone(Integer siteId, String phone) {
        MsgConfig config = dao.getSMSConfig(siteId);
        if(config == null){
            config = new MsgConfig();
            config.setModifyDate(new Date());
            config.setPhone(phone);
            config.setSiteId(siteId);
            dao.saveSMSConfig(config);
        }else {
            config.setPhone(phone);
            config.setModifyDate(new Date());
        }
    }

    @Override
    public MsgConfig getSMSConfig(Integer siteId) {
        return dao.getSMSConfig(siteId);
    }

    @Override
    public void saveOrUpdateSMSConfig(Integer siteId, Integer number, String content) {
        MsgConfig config = dao.getSMSConfig(siteId);
        if(config == null){
            config = new MsgConfig();
            config.setModifyDate(new Date());
            config.setMsgCount(number);
            config.setMsgContent(content);
            config.setSiteId(siteId);
            dao.saveSMSConfig(config);
        }else {
            config.setMsgCount(number);
            config.setMsgContent(content);
        }
    }

}
