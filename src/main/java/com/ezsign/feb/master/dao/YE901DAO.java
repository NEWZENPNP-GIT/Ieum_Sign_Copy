package com.ezsign.feb.master.dao;

import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.master.vo.YE901VO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository("ye901DAO")
public class YE901DAO extends EgovAbstractDAO {

    // 연말정산_사용자_진행현황_날짜
    @SuppressWarnings("unchecked")
    public List<YE901VO> getUserStatusDate(YE000VO vo) {
        return (List<YE901VO>) list("ye901DAO.getUserStatusDate", vo);
    }

    // 연말정신 진행현황 입력
    public void insYE901(YE901VO vo) {
        insert("ye901DAO.insYE901", vo);
    }
    
    // 연말정산_사용자_진행현황
    @SuppressWarnings("unchecked")
    public List<YE901VO> getYE901List(YE901VO vo) {
        return (List<YE901VO>) list("ye901DAO.getYE901List", vo);
    }
}
