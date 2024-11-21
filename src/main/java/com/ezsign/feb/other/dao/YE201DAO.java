package com.ezsign.feb.other.dao;

import com.ezsign.feb.other.vo.YE201VO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ye201DAO")
public class YE201DAO extends EgovAbstractDAO {

    // 소기업.소상공인 공제부금 조회
    public YE201VO getYE201(YE201VO vo) {
        return (YE201VO) select("ye201DAO.getYE201", vo);
    }

    @SuppressWarnings("unchecked")
    public List<YE201VO> getYE201List(YE201VO vo) {
        return (List<YE201VO>) list("ye201DAO.getYE201", vo);
    }

    // 소기업.소상공인 공제부금 입력
    public void insYE201(YE201VO vo) {
        insert("ye201DAO.insYE201", vo);
    }

    // 소기업.소상공인 공제부금 사용여부 '0'
    public int updYE201Disable(YE201VO vo) {
        return update("ye201DAO.updYE201Disable", vo);
    }

    // 소기업.소상공인 공제부금 전체삭제
    public int allDelYE201(YE201VO vo) {
        return delete("ye201DAO.allDelYE201", vo);
    }

    // 소기업.소상공인 공제부금 Map조회
    @SuppressWarnings("unchecked")
    public List<YE201VO> getYE201Map(YE201VO vo) {
        return (List<YE201VO>) list("ye201DAO.getYE201Map", vo);
    }
}
