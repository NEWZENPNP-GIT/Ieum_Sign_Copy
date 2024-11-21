package com.ezsign.feb.worker.dao;

import com.ezsign.feb.worker.vo.YE052VO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ye052DAO")
public class YE052DAO extends EgovAbstractDAO {

    // 특별소득공제_보험료(건강/장기요양/고용보험료) 조회
    public YE052VO getYE052(YE052VO vo) {
        return (YE052VO) select("ye052DAO.getYE052", vo);
    }

    @SuppressWarnings("unchecked")
    public List<YE052VO> getYE052List(YE052VO vo) {
        return (List<YE052VO>) list("ye052DAO.getYE052List", vo);
    }

    // 특별소득공제_보험료(건강/장기요양/고용보험료) 등록
    public void insYE052(YE052VO vo) {
        insert("ye052DAO.insYE052", vo);
    }

    // 특별소득공제_보험료(건강/장기요양/고용보험료) 사용여부 '0'
    public int updYE052Disable(YE052VO vo) {
        return update("ye052DAO.updYE052Disable", vo);
    }

    // 특별소득공제_보험료(건강/장기요양/고용보험료) 전체삭제
    public int allDelYE052(YE052VO vo) {
        return delete("ye052DAO.allDelYE052", vo);
    }

    // 특별소득공제_보험료(건강/장기요양/고용보험료) Map조회
    @SuppressWarnings("unchecked")
    public List<YE052VO> getYE052Map(YE052VO vo) {
        return (List<YE052VO>) list("ye052DAO.getYE052Map", vo);
    }
}
