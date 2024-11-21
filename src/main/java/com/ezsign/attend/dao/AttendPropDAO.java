package com.ezsign.attend.dao;

import com.ezsign.attend.vo.AttendPropVO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

@Repository("attendPropDAO")
public class AttendPropDAO extends EgovAbstractDAO {

    @SuppressWarnings("unchecked")
    public AttendPropVO getAttendProp(AttendPropVO vo) {
        return (AttendPropVO) select("attendPropDAO.getAttendProp", vo);
    }

    public void insAttendProp(AttendPropVO vo) {
        insert("attendPropDAO.insAttendProp", vo);
    }
}
