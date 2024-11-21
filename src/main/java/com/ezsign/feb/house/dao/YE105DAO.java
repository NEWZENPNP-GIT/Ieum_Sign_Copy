package com.ezsign.feb.house.dao;

import com.ezsign.feb.house.vo.YE105VO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ye105DAO")
public class YE105DAO extends EgovAbstractDAO {

    // 월세액공제 조회
    public YE105VO getYE105(YE105VO vo) {
        return (YE105VO) select("ye105DAO.getYE105", vo);
    }

    @SuppressWarnings("unchecked")
    public List<YE105VO> getYE105List(YE105VO vo) {
        return (List<YE105VO>) list("ye105DAO.getYE105", vo);
    }

    // 월세액공제 입력
    public void insYE105(YE105VO vo) {
        insert("ye105DAO.insYE105", vo);
    }

    // 월세액공제 사용여부 '0'
    public int updYE105Disable(YE105VO vo) {
        return update("ye105DAO.updYE105Disable", vo);
    }

    // 월세액공제 전체삭제
    public int allDelYE105(YE105VO vo) {
        return delete("ye105DAO.allDelYE105", vo);
    }

    // 월세액공제 Map조회
    @SuppressWarnings("unchecked")
    public List<YE105VO> getYE105Map(YE105VO vo) {
        return (List<YE105VO>) list("ye105DAO.getYE105Map", vo);
    }
    
    // 월세액공제 수정
    public int updYE105(YE105VO vo) {
        return update("ye105DAO.updYE105", vo);
    }
    
    // 월세액공제 삭제
    public int delYE105(YE105VO vo) {
        return delete("ye105DAO.delYE105", vo);
    }
}
