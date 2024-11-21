package com.ezsign.feb.house.dao;

import com.ezsign.feb.house.vo.YE104VO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ye104DAO")
public class YE104DAO extends EgovAbstractDAO {

    // 장기주택저당차입금이자상환액 조회
    public YE104VO getYE104(YE104VO vo) {
        return (YE104VO) select("ye104DAO.getYE104", vo);
    }

    @SuppressWarnings("unchecked")
    public List<YE104VO> getYE104List(YE104VO vo) {
        return (List<YE104VO>) list("ye104DAO.getYE104", vo);
    }

    // 장기주택저당차입금이자상환액 입력
    public void insYE104(YE104VO vo) {
        insert("ye104DAO.insYE104", vo);
    }

    // 장기주택저당차입금이자상환액 사용여부 '0'
    public int updYE104Disable(YE104VO vo) {
        return update("ye104DAO.updYE104Disable", vo);
    }

    // 장기주택저당차입금이자상환액 전체삭제
    public int allDelYE104(YE104VO vo) {
        return delete("ye104DAO.allDelYE104", vo);
    }

    // 장기주택저당차입금이자상환액 Map조회
    @SuppressWarnings("unchecked")
    public List<YE104VO> getYE104Map(YE104VO vo) {
        return (List<YE104VO>) list("ye104DAO.getYE104Map", vo);
    }
}
