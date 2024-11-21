package com.ezsign.feb.pension.dao;

import com.ezsign.feb.pension.vo.YE302VO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ye302DAO")
public class YE302DAO extends EgovAbstractDAO {

    // 연금저축계좌 조회
    public YE302VO getYE302(YE302VO vo) {
        return (YE302VO) select("ye302DAO.getYE302", vo);
    }

    @SuppressWarnings("unchecked")
    public List<YE302VO> getYE302List(YE302VO vo) {
        return (List<YE302VO>) list("ye302DAO.getYE302", vo);
    }

    // 연금저축계좌 입력
    public void insYE302(YE302VO vo) {
        insert("ye302DAO.insYE302", vo);
    }

    // 연금저축계좌 사용여부 '0'
    public int updYE302Disable(YE302VO vo) {
        return update("ye302DAO.updYE302Disable", vo);
    }

    // 연금저축계좌 전체삭제
    public int allDelYE302(YE302VO vo) {
        return delete("ye302DAO.allDelYE302", vo);
    }

    // 연금저축계좌 Map조회
    @SuppressWarnings("unchecked")
    public List<YE302VO> getYE302Map(YE302VO vo) {
        return (List<YE302VO>) list("ye302DAO.getYE302Map", vo);
    }
}
