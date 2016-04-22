package com.apply.service.impl;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apply.dao.SeatDao;
import com.apply.dao.StudentDao;
import com.apply.model.ApplyInfo;
import com.apply.model.Student;
import com.apply.service.StudentService;
import com.apply.vo.StudentSearchVo;
import com.base.vo.PageList;

@Service
public class StudentServiceImpl implements StudentService {
	@Autowired
	private StudentDao studentDao;
	@Autowired
	private SeatDao seatDao;

	@Override
	public Serializable save(Student student) {
		
		return studentDao.save(student);
	}

	@Override
	public int delete(String ids,Integer siteId,HttpServletRequest request) {
		List<String> fileList=this.studentDao.findHeadPicUrlByIds(ids);
		int result= studentDao.delete(ids,siteId);
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
		
		return studentDao.update(student);
	}

	@Override
	public Student get(Integer id) {
		
		return studentDao.get(id);
	}

	@Override
	public Map<String, Object> load(Integer id,Integer siteId) {
		
		return studentDao.load(id,siteId);
	}
	
	@Override
	public Map<String, Object> loadByAccount(String account) {
		
		return studentDao.loadByAccount(account);
	}

	@Override
	public List<Map<String, Object>> findStudentList(StudentSearchVo searchVo) {
		
		return studentDao.findStudentList(searchVo);
	}

	@Override
	public PageList findStudentPageList(Integer currentPage, Integer pageSize,
			StudentSearchVo searchVo) {
		
		return studentDao.findStudentPageList(currentPage, pageSize, searchVo);
	}

	@Override
	public String finMaxAccountByName(String name) {
		return studentDao.finMaxAccountByName(name);
	}

	@Override
	public int update(Student student) {
	
		return this.studentDao.update(student);
	}

	@Override
	public int updateStudentStatus(Integer status,String checkRemark, String ids) {
		if (status==1) {
			checkRemark="";
		}else if (status==2) {//审核不通过的直接将录取状态改为不录取
			this.studentDao.updateStudentAdmit(ids, 0);
		}else if (status==3) {//审核回退的将录取状态改为待公布
			this.studentDao.updateStudentAdmit(ids, -1);
		}
		return this.studentDao.updateStudentStatus(status,checkRemark, ids);
	}

	@Override
	public int updateStudentPassword(String ids, String password) {
		return this.studentDao.updateStudentPassword(ids, password);
	}

	@Override
	public int updateStudentAdmit(String ids,String checkRemark, Integer admit) {
		if (admit==1) {
			this.studentDao.updateStudentStatus(1,"", ids);
		}else{
			this.studentDao.updateStudentStatus(1,checkRemark, ids);
		}
		return this.studentDao.updateStudentAdmit(ids, admit);
	}

	@Override
	public List<Map<String, Object>> findScopeReportList(String graduate,String examYear,Integer siteId) {
		return this.studentDao.findScopeReportList(graduate,examYear,siteId);
	}


	@Override
	public List<String> findGraduatesBySite(Integer siteId) {
		return this.studentDao.findGraduatesBySite(siteId);
	}

	@Override
	public List<Map<String, Object>> findApplyReport(String graduate,
			String examYear, Integer siteId) {
	
		return this.studentDao.findApplyReport(graduate, examYear, siteId);
	}

	@Override
	public int updateStudentInterviewDate(String ids, String interviewDate,String batch) {
		return this.studentDao.updateStudentInterviewDate(ids, interviewDate,batch);
	}

	@Override
	public Map<String, Object> findByIDCard(String idCard, Integer level) {
		return studentDao.findByIDCard(idCard, level);
	}

	@Override
	public Map<String, Object> load4MH(Integer studentId, Integer siteId,
			Integer level) {
		return studentDao.load4MH(studentId,siteId, level);
	}
	
	@Override
	public ApplyInfo getApplyInfo(Integer siteId, Integer level) {
		return this.studentDao.getApplyInfo(siteId, level);
	}

	@Override
	public Map<String, Object> findByAccount(String account, Integer level) {
		return studentDao.findByAccount(account, level);
	}
}
