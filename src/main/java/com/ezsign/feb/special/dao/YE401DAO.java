package com.ezsign.feb.special.dao;

import com.ezsign.feb.special.vo.YE401VO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ye401DAO")
public class YE401DAO extends EgovAbstractDAO {

    // 보험료 (부양가족포함) 조회
    public YE401VO getYE401(YE401VO vo) {
        return (YE401VO) select("ye401DAO.getYE401", vo);
    }

    @SuppressWarnings("unchecked")
    public List<YE401VO> getYE401List(YE401VO vo) {
        return (List<YE401VO>) list("ye401DAO.getYE401", vo);
    }

    // 보험료 (부양가족포함) 입력
    public void insYE401(YE401VO vo) {
        insert("ye401DAO.insYE401", vo);
    }

    // 보험료 (부양가족포함) 사용여부 '0'
    public int updYE401Disable(YE401VO vo) {
        return update("ye401DAO.updYE401Disable", vo);
    }

    // 보험료 (부양가족포함) 전체삭제
    public int allDelYE401(YE401VO vo) {
        return delete("ye401DAO.allDelYE401", vo);
    }

    // 보험료 (부양가족포함) Map조회
    @SuppressWarnings("unchecked")
    public List<YE401VO> getYE401Map(YE401VO vo) {
        return (List<YE401VO>) list("ye401DAO.getYE401Map", vo);
    }
    
    // 보험료 (부양가족포함) 수정
    public int updYE401(YE401VO vo) {
        return update("ye401DAO.updYE401", vo);
    }
    
    // 보험료 (부양가족포함) 삭제
    public int delYE401(YE401VO vo) {
        return delete("ye401DAO.delYE401", vo);
    }
}
