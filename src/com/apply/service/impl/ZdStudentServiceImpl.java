package com.apply.service.impl;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apply.dao.SeatDao;
import com.apply.dao.ZdStudentDao;
import com.apply.model.ApplyInfo;
import com.apply.model.ZdStudent;
import com.apply.service.ZdStudentService;
import com.apply.vo.StudentSearchVo;
import com.base.vo.PageList;

@Service
public class ZdStudentServiceImpl implements ZdStudentService {
	@Autowired
	private ZdStudentDao zdStudentDao;
	@Autowired
	private SeatDao seatDao;

	@Override
	public Serializable save(ZdStudent zdStudent) {
		
		return zdStudentDao.save(zdStudent);
	}

	@Override
	public int delete(String ids,Integer siteId,HttpServletRequest request) {
		List<String> fileList=this.zdStudentDao.findHeadPicUrlByIds(ids);
		int result= zdStudentDao.delete(ids,siteId);
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
		
		return zdStudentDao.update(student);
	}

	@Override
	public ZdStudent get(Integer id) {
		
		return zdStudentDao.get(id);
	}

	@Override
	public Map<String, Object> load(Integer id,Integer siteId) {
		
		return zdStudentDao.load(id,siteId);
	}
	
	@Override
	public Map<String, Object> loadByAccount(String account) {
		
		return zdStudentDao.loadByAccount(account);
	}

	@Override
	public List<Map<String, Object>> findStudentList(StudentSearchVo searchVo) {
		
		return zdStudentDao.findStudentList(searchVo);
	}

	@Override
	public PageList<ZdStudent> findStudentPageList(Integer currentPage, Integer pageSize,
			StudentSearchVo searchVo) {
		
		return zdStudentDao.findStudentPageList(currentPage, pageSize, searchVo);
	}

	@Override
	public String finMaxAccountByName(String name) {
		return zdStudentDao.finMaxAccountByName(name);
	}

	@Override
	public int update(ZdStudent zdStudent) {
	
		return this.zdStudentDao.update(zdStudent);
	}

	@Override
	public int updateStudentStatus(Integer status,String checkRemark, String ids) {
		if (status==1) {
			checkRemark="";
		}else if (status==2) {//审核不通过的直接将录取状态改为不录取
			this.zdStudentDao.updateStudentAdmit(ids, 0);
		}else if (status==3) {//审核回退的将录取状态改为待公布
			this.zdStudentDao.updateStudentAdmit(ids, -1);
		}
		return this.zdStudentDao.updateStudentStatus(status,checkRemark, ids);
	}

	@Override
	public int updateStudentPassword(String ids, String password) {
		return this.zdStudentDao.updateStudentPassword(ids, password);
	}

	@Override
	public int updateStudentAdmit(String ids,String checkRemark, Integer admit) {
		if (checkRemark == null) {
			this.zdStudentDao.updateStudentStatus(1,"", ids);
		}else{
			this.zdStudentDao.updateStudentStatus(1,checkRemark, ids);
		}
		return this.zdStudentDao.updateStudentAdmit(ids, admit);
	}

	@Override
	public List<Map<String, Object>> findScopeReportList(String graduate,String examYear,Integer siteId) {
		return this.zdStudentDao.findScopeReportList(graduate,examYear,siteId);
	}


	@Override
	public List<String> findGraduatesBySite(Integer siteId) {
		return this.zdStudentDao.findGraduatesBySite(siteId);
	}

	@Override
	public List<Map<String, Object>> findApplyReport(String graduate,
			String examYear, Integer siteId) {
	
		return this.zdStudentDao.findApplyReport(graduate, examYear, siteId);
	}

	@Override
	public int updateStudentInterviewDate(String ids, String interviewDate,String batch) {
		return this.zdStudentDao.updateStudentInterviewDate(ids, interviewDate,batch);
	}
	@Override
	public Integer saveOrUpdate(ZdStudent zdStudent) {
		
		return zdStudentDao.saveOrUpdate(zdStudent);
	}

	@Override
	public PageList<ZdStudent> getZdStudentPageList(Integer siteId,
			Integer pageSize, Integer currentPage, String keyword, String year) {
		return zdStudentDao.getZdStudentPageList(siteId,pageSize, currentPage, keyword,year);
	}

	@Override
	public List<Map<String, Object>> getZdStudentList(Integer siteId,
			String year, String keyword, Integer ...id) {
		return zdStudentDao.getZdStudentList(siteId, year, keyword, id);
	}

	@Override
	public ZdStudent getByIDCard(String IDCard) {
		return zdStudentDao.getByIDCard(IDCard);
	}
	
	@Override
	public List<Map<String, Object>> getZdStudent2List(Integer siteId,
			String year, String keyword, Integer ...id) {
		return zdStudentDao.getZdStudent2List(siteId, year, keyword, id);
	}

	@Override
	public int updateStudentRoomNo(String ids, String roomNo) {
		return this.zdStudentDao.updateStudentRoomNo(ids, roomNo);
	}

	@Override
	public int resetStudentInterviewDate(String ids, String batch) {
		return this.zdStudentDao.resetStudentInterviewDate(ids, batch);
	}

}
