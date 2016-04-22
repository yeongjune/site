package com.apply.service.impl;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apply.dao.SeatDao;
import com.apply.dao.ZdStudent2Dao;
import com.apply.model.ZdStudent;
import com.apply.model.ZdStudent2;
import com.apply.service.ZdStudent2Service;
import com.apply.vo.StudentSearchVo;
import com.base.vo.PageList;

@Service
public class ZdStudent2ServiceImpl implements ZdStudent2Service {
	@Autowired
	private ZdStudent2Dao zdStudent2Dao;
	@Autowired
	private SeatDao seatDao;

	@Override
	public Serializable save(ZdStudent2 zdStudent2) {
		
		return zdStudent2Dao.save(zdStudent2);
	}

	@Override
	public int delete(String ids,Integer siteId,HttpServletRequest request) {
		List<String> fileList=this.zdStudent2Dao.findHeadPicUrlByIds(ids);
		int result= zdStudent2Dao.delete(ids,siteId);
		if (result>0) {
			String root=request.getSession().getServletContext().getRealPath("/");
			for (String filePath : fileList) {
				String path=root+filePath;
				File file=new File(path);
				if (file.exists()&&file.isFile()) {
					file.delete();
				}
			}
		}
		seatDao.deleteByStudentIds(ids);
		return result;
	}

	@Override
	public int update(Map<String, Object> student) {
		
		return zdStudent2Dao.update(student);
	}

	@Override
	public ZdStudent2 get(Integer id) {
		
		return zdStudent2Dao.get(id);
	}

	@Override
	public Map<String, Object> load(Integer id,Integer siteId) {
		
		return zdStudent2Dao.load(id,siteId);
	}
	
	@Override
	public Map<String, Object> loadByAccount(String account) {
		
		return zdStudent2Dao.loadByAccount(account);
	}

	@Override
	public List<Map<String, Object>> findStudentList(StudentSearchVo searchVo) {
		
		return zdStudent2Dao.findStudentList(searchVo);
	}

	@Override
	public PageList<ZdStudent2> findStudentPageList(Integer currentPage, Integer pageSize,
			StudentSearchVo searchVo) {
		
		return zdStudent2Dao.findStudentPageList(currentPage, pageSize, searchVo);
	}

	@Override
	public String finMaxAccountByName(String name) {
		return zdStudent2Dao.finMaxAccountByName(name);
	}

	@Override
	public int update(ZdStudent2 zdStudent2) {
	
		return this.zdStudent2Dao.update(zdStudent2);
	}

	@Override
	public int updateStudentStatus(Integer status,String checkRemark, String ids) {
		if (status==1) {
			checkRemark="";
		}else if (status==2) {//审核不通过的直接将录取状态改为不录取
			this.zdStudent2Dao.updateStudentAdmit(ids, 0);
		}else if (status==3) {//审核回退的将录取状态改为待公布
			this.zdStudent2Dao.updateStudentAdmit(ids, -1);
		}
		return this.zdStudent2Dao.updateStudentStatus(status,checkRemark, ids);
	}

	@Override
	public int updateStudentPassword(String ids, String password) {
		return this.zdStudent2Dao.updateStudentPassword(ids, password);
	}

	@Override
	public int updateStudentAdmit(String ids,String checkRemark, Integer admit) {
		if (checkRemark == null) {
			this.zdStudent2Dao.updateStudentStatus(1,"", ids);
		}else{
			this.zdStudent2Dao.updateStudentStatus(1,checkRemark, ids);
		}
		return this.zdStudent2Dao.updateStudentAdmit(ids, admit);
	}

	@Override
	public List<Map<String, Object>> findScopeReportList(String graduate,String examYear,Integer siteId) {
		return this.zdStudent2Dao.findScopeReportList(graduate,examYear,siteId);
	}


	@Override
	public List<String> findGraduatesBySite(Integer siteId) {
		return this.zdStudent2Dao.findGraduatesBySite(siteId);
	}

	@Override
	public List<Map<String, Object>> findApplyReport(String graduate,
			String examYear, Integer siteId) {
	
		return this.zdStudent2Dao.findApplyReport(graduate, examYear, siteId);
	}

	@Override
	public int updateStudentInterviewDate(String ids, String interviewDate,String batch) {
		return this.zdStudent2Dao.updateStudentInterviewDate(ids, interviewDate,batch);
	}
	@Override
	public Integer saveOrUpdate(ZdStudent2 zdStudent2) {
		
		return zdStudent2Dao.saveOrUpdate(zdStudent2);
	}

	@Override
	public PageList<ZdStudent2> getZdStudentPageList(Integer siteId,
			Integer pageSize, Integer currentPage, String keyword, String year) {
		return zdStudent2Dao.getZdStudentPageList(siteId,pageSize, currentPage, keyword,year);
	}

	@Override
	public List<Map<String, Object>> getZdStudentList(Integer siteId,
			String year, String keyword, Integer ...id) {
		return zdStudent2Dao.getZdStudentList(siteId, year, keyword, id);
	}

	@Override
	public ZdStudent2 getByIDCard(String IDCard) {
		return zdStudent2Dao.getByIDCard(IDCard);
	}
	
	@Override
	public int updateStudentRoomNo(String ids, String roomNo) {
		return this.zdStudent2Dao.updateStudentRoomNo(ids, roomNo);
	}

	@Override
	public ZdStudent2 getByAccount(String account) {
		return zdStudent2Dao.getByAccount(account);
	}
}
