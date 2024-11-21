package com.ezsign.feb.other.dao;

import com.ezsign.feb.other.vo.YE204VO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ye204DAO")
public class YE204DAO extends EgovAbstractDAO {

    // 고용유지중소기업근로자 조회
    public YE204VO getYE204(YE204VO vo) {
        return (YE204VO) select("ye204DAO.getYE204", vo);
    }

    @SuppressWarnings("unchecked")
    public List<YE204VO> getYE204List(YE204VO vo) {
        return (List<YE204VO>) list("ye204DAO.getYE204", vo);
    }

    // 고용유지중소기업근로자 입력
    public void insYE204(YE204VO vo) {
        insert("ye204DAO.insYE204", vo);
    }

    // 고용유지중소기업근로자 사용여부 '0'
    public int updYE204Disable(YE204VO vo) {
        return update("ye204DAO.updYE204Disable", vo);
    }

    // 고용유지중소기업근로자 전체삭제
    public int allDelYE204(YE204VO vo) {
        return delete("ye204DAO.allDelYE204", vo);
    }

    // 고용유지중소기업근로자 Map조회
    @SuppressWarnings("unchecked")
    public List<YE204VO> getYE204Map(YE204VO vo) {
        return (List<YE204VO>) list("ye204DAO.getYE204Map", vo);
    }
}
