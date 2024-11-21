package com.ezsign.attend.dao;

import com.ezsign.attend.vo.AttendPlaceVO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("attendPlaceDAO")
public class AttendPlaceDAO extends EgovAbstractDAO {

    @SuppressWarnings("unchecked")
    public List<AttendPlaceVO> getAttendPlaceList(AttendPlaceVO vo) {
        return (List<AttendPlaceVO>) list("attendPlaceDAO.getAttendPlaceList", vo);
    }

    public void insAttendPlace(AttendPlaceVO vo) {
        insert("attendPlaceDAO.insAttendPlace", vo);
    }

    public int updAttendPlace(AttendPlaceVO vo) {
        return update("attendPlaceDAO.updAttendPlace", vo);
    }

    public int delAttendPlace(AttendPlaceVO vo) {
        return delete("attendPlaceDAO.delAttendPlace", vo);
    }
}
