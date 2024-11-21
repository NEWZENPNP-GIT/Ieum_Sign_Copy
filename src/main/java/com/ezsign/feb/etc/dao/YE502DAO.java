package com.ezsign.feb.etc.dao;

import com.ezsign.feb.etc.vo.YE502VO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ye502DAO")
public class YE502DAO extends EgovAbstractDAO {

    // 주택자금차입금이자세액공제 조회
    public YE502VO getYE502(YE502VO vo) {
        return (YE502VO) select("ye502DAO.getYE502", vo);
    }

    @SuppressWarnings("unchecked")
    public List<YE502VO> getYE502List(YE502VO vo) {
        return (List<YE502VO>) list("ye502DAO.getYE502", vo);
    }

    // 주택자금차입금이자세액공제 입력
    public void insYE502(YE502VO vo) {
        insert("ye502DAO.insYE502", vo);
    }

    // 주택자금차입금이자세액공제 사용여부 '0'
    public int updYE502Disable(YE502VO vo) {
        return update("ye502DAO.updYE502Disable", vo);
    }

    // 주택자금차입금이자세액공제 전체삭제
    public int allDelYE502(YE502VO vo) {
        return delete("ye502DAO.allDelYE502", vo);
    }

    // 주택자금차입금이자세액공제 Map조회
    @SuppressWarnings("unchecked")
    public List<YE502VO> getYE502Map(YE502VO vo) {
        return (List<YE502VO>) list("ye502DAO.getYE502Map", vo);
    }
}
