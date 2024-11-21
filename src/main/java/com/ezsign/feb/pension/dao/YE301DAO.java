package com.ezsign.feb.pension.dao;

import com.ezsign.feb.pension.vo.YE301VO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ye301DAO")
public class YE301DAO extends EgovAbstractDAO {

    // 퇴직연금계좌 조회
    public YE301VO getYE301(YE301VO vo) {
        return (YE301VO) select("ye301DAO.getYE301", vo);
    }

    @SuppressWarnings("unchecked")
    public List<YE301VO> getYE301List(YE301VO vo) {
        return (List<YE301VO>) list("ye301DAO.getYE301", vo);
    }

    // 퇴직연금계좌 입력
    public void insYE301(YE301VO vo) {
        insert("ye301DAO.insYE301", vo);
    }

    // 퇴직연금계좌 사용여부 '0'
    public int updYE301Disable(YE301VO vo) {
        return update("ye301DAO.updYE301Disable", vo);
    }

    // 퇴직연금계좌 전체삭제
    public int allDelYE301(YE301VO vo) {
        return delete("ye301DAO.allDelYE301", vo);
    }

    // 퇴직연금계좌 Map조회
    @SuppressWarnings("unchecked")
    public List<YE301VO> getYE301Map(YE301VO vo) {
        return (List<YE301VO>) list("ye301DAO.getYE301Map", vo);
    }
}
