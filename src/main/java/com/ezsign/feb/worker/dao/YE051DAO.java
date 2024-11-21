package com.ezsign.feb.worker.dao;

import com.ezsign.feb.worker.vo.YE051VO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ye051DAO")
public class YE051DAO extends EgovAbstractDAO {

    // 연금보혐료 조회
    @SuppressWarnings("unchecked")
    public List<YE051VO> getYE051List(YE051VO vo) {
        return (List<YE051VO>) list("ye051DAO.getYE051", vo);
    }

    // 연금보혐료 입력
    public void insYE051(YE051VO vo) {
        insert("ye051DAO.insYE051", vo);
    }

    // 연금보혐료 사용여부 '0'
    public int updYE051Disable(YE051VO vo) {
        return update("ye051DAO.updYE051Disable", vo);
    }

    // 연금보혐료 전체삭제
    public int allDelYE051(YE051VO vo) {
        return delete("ye051DAO.allDelYE051", vo);
    }

    // 연금보혐료 Map조회
    @SuppressWarnings("unchecked")
    public List<YE051VO> getYE051Map(YE051VO vo) {
        return (List<YE051VO>) list("ye051DAO.getYE051Map", vo);
    }
}
