package com.site.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.site.dao.ContactDao;
import com.site.model.Contact;
import com.site.service.ContactService;

@Service
public class ContactServiceImpl implements ContactService {
	@Autowired
	private ContactDao contactDao;

	@Override
	public Serializable save(Contact contact) {
		
		return contactDao.save(contact);
	}

	@Override
	public Serializable save(Map<String, Object> contact) {
		
		return contactDao.save(contact);
	}

	@Override
	public int delete(Integer id) {
		
		return contactDao.delete(id);
	}

	@Override
	public int delete(Integer siteId, String ids) {
		
		return contactDao.delete(siteId, ids);
	}

	@Override
	public int update(Map<String, Object> contact) {
		
		return contactDao.update(contact);
	}

	@Override
	public int update(Contact contact) {
		
		return contactDao.update(contact);
	}

	@Override
	public Map<String, Object> load(Integer id) {
		
		return contactDao.load(id);
	}

	@Override
	public Contact get(Integer id) {
		
		return contactDao.get(id);
	}

	@Override
	public List<Map<String, Object>> findContactList(Integer siteId) {
		
		return contactDao.findContactList(siteId);
	}

	@Override
	public int getNewOrderSort(Integer siteId) {
		
		return contactDao.getNewOrderSort(siteId);
	}

	@Override
	public int updateOrderSort(Integer id, Integer upOrdown) {
		
		return contactDao.updateOrderSort(id, upOrdown);
	}

}
