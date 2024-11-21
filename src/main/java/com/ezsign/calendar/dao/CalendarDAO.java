package com.ezsign.calendar.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.calendar.vo.CalendarVO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;


@Repository("calendarDAO")
public class CalendarDAO extends EgovAbstractDAO {
	
	public void insCalendar(CalendarVO vo) {
		insert("calendarDAO.insCalendar", vo);
	}
	
	public void delCalendar(CalendarVO vo) {
		delete("calendarDAO.delCalendar", vo);
	}
	
	public int updCalendar(CalendarVO vo) {
		return update("calendarDAO.updCalendar", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<CalendarVO> getCalendarList(CalendarVO vo) {
		return (List<CalendarVO>)list("calendarDAO.getCalendarList", vo);
	}
	
}
