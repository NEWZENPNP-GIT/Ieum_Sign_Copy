package com.ezsign.schedule.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.schedule.vo.ScheduleVO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;


@Repository("scheduleDAO")
public class ScheduleDAO extends EgovAbstractDAO {
	
	public void insSchedule(ScheduleVO vo) {
		insert("scheduleDAO.insSchedule", vo);
	}
	
	public void delSchedule(ScheduleVO vo) {
		delete("scheduleDAO.delSchedule", vo);
	}
	
	public int updSchedule(ScheduleVO vo) {
		return update("scheduleDAO.updSchedule", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<ScheduleVO> getScheduleList(ScheduleVO vo) {
		return (List<ScheduleVO>)list("scheduleDAO.getScheduleList", vo);
	}
	
}
