package com.apply.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apply.dao.ExamDao;
import com.apply.dao.SeatDao;
import com.apply.dao.StudentDao;
import com.apply.model.Exam;
import com.apply.service.ExamService;
import com.base.util.DataUtil;
import com.base.util.StringUtil;
import com.base.vo.PageList;

@Service
public class ExamServiceImpl implements ExamService {
	@Autowired
	private ExamDao examDao;
	@Autowired
	private SeatDao seatDao;
	@Autowired
	private StudentDao studentDao;

	@Override
	public Serializable save(Exam exam) {
		
		return examDao.save(exam);
	}

	@Override
	public Serializable save(Map<String, Object> exam) {
		
		return examDao.save(exam);
	}

	@Override
	public int update(Exam exam) {
		
		return examDao.update(exam);
	}

	@Override
	public int update(Map<String, Object> exam) {
		
		return examDao.update(exam);
	}

	@Override
	public int delete(Integer id) {
		if (id==null||id<0) {
			return 0;
		}
		int result=examDao.delete(id);
		if (result>0) {
			seatDao.deleteByExam(id);
		}
		return result;
	}
	
	@Override
	public int delete(Integer siteId,Integer id) {
		if (siteId==null||siteId<0||id==null||id<0) {
			return 0;
		}
		int result=examDao.deleteBySiteAndId(siteId, id);
		if (result>0) {
			seatDao.deleteByExam(id);
		}
		return result;
	}

	@Override
	public Exam get(Integer id) {
		
		return examDao.get(id);
	}

	@Override
	public Map<String, Object> load(Integer id) {
		
		return examDao.load(id);
	}

	@Override
	public List<Map<String, Object>> findExamList(Integer siteId,
			String examName) {
		
		return examDao.findExamList(siteId, examName);
	}

	@Override
	public PageList findExamPageList(Integer currentPage, Integer pageSize,
			Integer siteId, String examName) {
		return examDao.findExamPageList(currentPage, pageSize, siteId, examName);
	}

	@Override
	public int saveStudentSeat(Integer examId, String studentIds) {
		int result=0;
		Exam exam=examDao.get(examId);
		if (exam!=null) {
			if (StringUtil.isEmpty(studentIds)) {
				 this.seatDao.deleteByExam(examId);
				 return 1;
			}
			String [] studIds=studentIds.split(",");
			if (studIds.length>0) {
				List<Map<String, Object>> listSeat=new ArrayList<Map<String,Object>>();
				for (int i = 0; i < studIds.length; i++) {
					Integer stuId=Integer.parseInt(studIds[i]);
					studentDao.getStudentTestCard(stuId, exam.getExamDate());//生成学生准考证
					if (!seatDao.hasExist(exam.getSiteId() , examId, stuId)) {
						Map<String, Object> seatMap=new HashMap<String, Object>();
						seatMap.put("examId", examId);
						seatMap.put("siteId", exam.getSiteId());
						seatMap.put("studentId", stuId);
						listSeat.add(seatMap);
					}
				}
				if (listSeat.size()>0) {
					seatDao.save(listSeat);
				}
				result=1;
			}
			List<String> deleteSeatIds=new ArrayList<String>();
			List<Map<String, Object>> seatList=seatDao.findSeatList(exam.getSiteId(), examId, null, null, null, null, null, null, null);
			if (seatList!=null) {
				List<String> seleStudentList=Arrays.<String>asList(studIds);
				for (int i = 0; i < seatList.size(); i++) {
					String tempStudentId=seatList.get(i).get("studentId")+"";
					if (!seleStudentList.contains(tempStudentId)) {
						deleteSeatIds.add(tempStudentId);
					}		
				}
				if (deleteSeatIds.size()>0) {
					String tempStuIds="";
					for (int i = 0; i < deleteSeatIds.size(); i++) {
						tempStuIds += " "+deleteSeatIds.get(i);
						if (i!=deleteSeatIds.size()-1) {
							tempStuIds += ",";
						}
					}
					seatDao.deleteByExamAndStudent(examId, tempStuIds);
				}
			}
		}
		return result;
	}

	@Override
	public int updateSeatNoAndRoomNo(Integer examId,Boolean clearAllSeatNO) {
		int result=0;
		Exam exam=examDao.get(examId);
		if (exam!=null) {
			if (clearAllSeatNO) {
				seatDao.deleteSeatNoByExam(examId);//清空座位号
			}
			
			List<Map<String, Object>> allSeatList=seatDao.findSeatList(exam.getSiteId(), examId, null, null, null, null, null, null, null);
			List<Map<String, Object>> updateSeatList=new ArrayList<Map<String,Object>>();//需要生成座位号的座位
			for (Map<String, Object> seat : allSeatList) {
				if (StringUtil.isEmpty(seat.get("seatNo")+"")||"null".equals(seat.get("seatNo")+"")) {
					updateSeatList.add(seat);
				}
			}
			if (updateSeatList.size()>0) {
				for (Map<String, Object> seatMap : updateSeatList) {
					//查处当前考试已生成的最后一个试室号
					Map<String, Object> lastSeatMap=this.seatDao.findMaxSeatByExam(examId);
					String newRoomNo="01";
					String newSeatNo="01";
					if (lastSeatMap!=null) {
						int maxRoom=Integer.parseInt(lastSeatMap.get("roomNo")+"");
						int maxSeat=Integer.parseInt(lastSeatMap.get("seatNo")+"");
						if (maxSeat<exam.getEveryRoomSeatCount()-1) {
							newRoomNo="0"+maxRoom;
							newSeatNo="0"+(maxSeat+1);
						}else{
							newRoomNo="0"+(maxRoom+1);
						}
					}
					seatMap.put("roomNo",newRoomNo);
					seatMap.put("seatNo",newSeatNo);
				}
				result=seatDao.update(DataUtil.getListMapByKeys(updateSeatList, new String[]{"id","roomNo","seatNo"}));
			}
		}
		return result;
	}
	@Override
	public int updateDeployRoom(Integer examId) {
		int result=0;
		Exam exam=examDao.get(examId);
		if (exam!=null) {
			//seatDao.deleteByExam(examId);//先清空之前的
			List<Map<String, Object>> studentList=studentDao.findCurrentStudentList(exam.getSiteId());
			if (studentList.isEmpty()) {
				return 1;
			}
			int count=0;//当前考试已有的座位数
			List<Map<String, Object>> allSeatList=seatDao.findSeatList(exam.getSiteId(), examId, null, null, null, null, null, null, null);
			if (allSeatList !=null && allSeatList.size()>0) {
				for (Map<String, Object> seat : allSeatList) {
					boolean has=false;//判断该考场是否已经有该考生
					for (Map<String, Object> student : studentList) {
						if (student.get("id").equals(seat.get("studentId"))) {
							has=true;
							break;
						}
					}
					if (has) {
						DataUtil.deleteListMapByKey(studentList, "id", seat.get("studentId")+"", true);
					}
				}
				count=allSeatList.size();
			}
			
			int roomCount=exam.getRoomCount();//考场数
			String [] examNos=new String[roomCount];//考场号
			
			for (int i = 0; i < examNos.length; i++) {
				examNos[i]="0"+(i+1);
			}
			List<Map<String, Object>> seatList=new ArrayList<Map<String,Object>>();//分配座位记录
			if (count>0) {
				count++;
			}
			for (int i = 0; i < studentList.size(); i++,count++) {
				Map<String, Object> seatMap=new HashMap<String, Object>();
				seatMap.put("siteId", exam.getSiteId());
				seatMap.put("examId", exam.getId());
				seatMap.put("studentId", studentList.get(i).get("id"));
				seatMap.put("roomNo", examNos[count%examNos.length]);	 //考场号
				seatMap.put("watingRoomNo", examNos[count%examNos.length]);//候考试
				seatList.add(seatMap);
			}
			if (seatList.size()==0) {
				return 1;
			}else{
				result= seatDao.save(seatList);
			}
		}
		return result;
	}
}
