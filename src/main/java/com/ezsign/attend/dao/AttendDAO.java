package com.ezsign.attend.dao;

import com.ezsign.attend.vo.AttendVO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("attendDAO")
public class AttendDAO extends EgovAbstractDAO {

    @SuppressWarnings("unchecked")
    public List<AttendVO> getAttendList(AttendVO vo) {
        return (List<AttendVO>) list("attendDAO.getAttendList", vo);
    }
    
    public List<AttendVO> getMobileAttendList(AttendVO vo) {
    	return (List<AttendVO>) list("attendDAO.getMobileAttendList", vo);
    }
    
    public List<AttendVO> getMobileUserAttendList(AttendVO vo) {
    	return (List<AttendVO>) list("attendDAO.getMobileUserAttendList", vo);
    }
    
    
    
    public int getAttendListCount(AttendVO vo) {
        return (Integer)getSqlMapClientTemplate().queryForObject("attendDAO.getAttendListCount", vo);
    }

    public void insAttend(AttendVO vo) {
        insert("attendDAO.insAttend", vo);
    }

    public int offAttend(AttendVO vo) {
        return update("attendDAO.offAttend", vo);
    }

    public int updAttend(AttendVO vo) {
        return update("attendDAO.updAttend", vo);
    }
    
    public int updMobileAttend(AttendVO vo) {
        return update("attendDAO.updMobileAttend", vo);
    }
    
    

    @SuppressWarnings("unchecked")
    public List<AttendVO> getAttendSumList(AttendVO vo) {
        return (List<AttendVO>) list("attendDAO.getAttendSumList", vo);
    }
}
