package com.ezsign.calendar.service;

import java.util.List;

import com.ezsign.calendar.vo.CalendarVO;

public interface CalendarService {
	
	public void insCalendar(CalendarVO vo) throws Exception;

	public void delCalendar(CalendarVO vo) throws Exception;

	public int updCalendar(CalendarVO vo) throws Exception;
	
	public List<CalendarVO> getCalendarList(CalendarVO vo) throws Exception;
	
}
