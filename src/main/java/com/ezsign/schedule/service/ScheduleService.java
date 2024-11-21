package com.ezsign.schedule.service;

import java.util.List;

import com.ezsign.schedule.vo.ScheduleVO;


public interface ScheduleService {
	
	public void insSchedule(ScheduleVO vo, String bizname) throws Exception;

	public void delSchedule(ScheduleVO vo) throws Exception;

	public int updSchedule(ScheduleVO vo) throws Exception;
	
	public List<ScheduleVO> getScheduleList(ScheduleVO vo) throws Exception;
	
}
