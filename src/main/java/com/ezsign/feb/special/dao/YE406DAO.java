package com.ezsign.feb.special.dao;

import com.ezsign.feb.special.vo.YE406VO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ye406DAO")
public class YE406DAO extends EgovAbstractDAO {

    // 세액감면 조회
    public YE406VO getYE406(YE406VO vo) {
        return (YE406VO) select("ye406DAO.getYE406", vo);
    }

    @SuppressWarnings("unchecked")
    public List<YE406VO> getYE406List(YE406VO vo) {
        return (List<YE406VO>) list("ye406DAO.getYE406", vo);
    }

    // 세액감면 입력
    public void insYE406(YE406VO vo) {
        insert("ye406DAO.insYE406", vo);
    }

    // 세액감면 사용여부 '0'
    public int updYE406Disable(YE406VO vo) {
        return update("ye406DAO.updYE406Disable", vo);
    }

    // 세액감면 전체삭제
    public int allDelYE406(YE406VO vo) {
        return delete("ye406DAO.allDelYE406", vo);
    }

    // 세액감면 조회
    @SuppressWarnings("unchecked")
    public List<YE406VO> getYE406Map(YE406VO vo) {
        return (List<YE406VO>) list("ye406DAO.getYE406Map", vo);
    }
}
