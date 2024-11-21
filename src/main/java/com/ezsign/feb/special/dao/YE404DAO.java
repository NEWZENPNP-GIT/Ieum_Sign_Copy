package com.ezsign.feb.special.dao;

import com.ezsign.feb.special.vo.YE404VO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ye404DAO")
public class YE404DAO extends EgovAbstractDAO {

    // 연말정산_사내기부금 조회
    @SuppressWarnings("unchecked")
    public List<YE404VO> getYE404List(YE404VO vo) {
        return (List<YE404VO>) list("ye404DAO.getYE404List", vo);
    }

    // 연말정산_사내기부금 갯수
    public YE404VO getYE404ListCount(YE404VO vo) {
        return (YE404VO) select("ye404DAO.getYE404ListCount", vo);
    }

    // 연말정산_사내기부금 입력
    public void insYE404(YE404VO vo) {
        insert("ye404DAO.insYE404", vo);
    }

    // 연말정산_기부금 수정
    public int updYE404(YE404VO vo) {
        return update("ye404DAO.updYE404", vo);
    }

    // 연말정산_기부금 삭제
    public int delYE404(YE404VO vo) {
        return delete("ye404DAO.delYE404", vo);
    }

    // 연말정산_사내기부금 사용여부 '0'
    public int updYE404Disable(YE404VO vo) {
        return update("ye404DAO.updYE404Disable", vo);
    }

    // 연말정산_사내기부금 전체삭제
    public int allDelYE404(YE404VO vo) {
        return delete("ye404DAO.allDelYE404", vo);
    }

    // 연말정산_사내기부금 Map조회
    @SuppressWarnings("unchecked")
    public List<YE404VO> getYE404Map(YE404VO vo) {
        return (List<YE404VO>) list("ye404DAO.getYE404Map", vo);
    }
}
