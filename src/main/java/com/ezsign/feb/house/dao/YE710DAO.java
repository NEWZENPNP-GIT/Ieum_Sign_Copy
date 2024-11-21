package com.ezsign.feb.house.dao;

import com.ezsign.feb.house.vo.YE710VO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

@Repository("ye710DAO")
public class YE710DAO extends EgovAbstractDAO {

    // 주택관련소득공제신청확인서 조회
    public YE710VO getYE710(YE710VO vo) {
        return (YE710VO) select("ye710DAO.getYE710", vo);
    }

    // 주택관련소득공제신청확인서 입력
    public void insYE710(YE710VO vo) {
        insert("ye710DAO.insYE710", vo);
    }

    // 주택관련소득공제신청확인서 사용여부 '0'
    public int updYE710Disable(YE710VO vo) {
        return update("ye710DAO.updYE710Disable", vo);
    }
}
