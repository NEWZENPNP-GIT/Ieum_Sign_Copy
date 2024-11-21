package com.ezsign.calendar.service.impl;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.calendar.dao.CalendarDAO;
import com.ezsign.calendar.service.CalendarService;
import com.ezsign.calendar.vo.CalendarVO;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@Service("calendarService")
public class CalendarServiceImpl extends AbstractServiceImpl implements CalendarService {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name="calendarDAO")
	private CalendarDAO calendarDAO;
	
	public static void main(String[] args) {
		
	}
	
	@Override
	public void insCalendar(CalendarVO vo) throws Exception {
		calendarDAO.insCalendar(vo);
	}

	@Override
	public void delCalendar(CalendarVO vo) throws Exception {
		calendarDAO.delCalendar(vo);
	}

	@Override
	public int updCalendar(CalendarVO vo) throws Exception {
		return calendarDAO.updCalendar(vo);
	}
	
	@Override
	public List<CalendarVO> getCalendarList(CalendarVO vo) throws Exception {
		List<CalendarVO> dataList = new ArrayList<CalendarVO>();
		dataList = calendarDAO.getCalendarList(vo);
		
		return dataList;		
	}
	
}
