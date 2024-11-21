package com.ezsign.feb.house.dao;

import com.ezsign.feb.house.vo.YE101VO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ye101DAO")
public class YE101DAO extends EgovAbstractDAO {

    // 주택임차차입금원리금상환액 조회
    public YE101VO getYE101(YE101VO vo) {
        return (YE101VO) select("ye101DAO.getYE101", vo);
    }

    @SuppressWarnings("unchecked")
    public List<YE101VO> getYE101List(YE101VO vo) {
        return (List<YE101VO>) list("ye101DAO.getYE101", vo);
    }

    // 주택임차차입금원리금상환액 입력
    public void insYE101(YE101VO vo) {
        insert("ye101DAO.insYE101", vo);
    }

    // 주택임차차입금원리금상환액 사용여부 '0'
    public int updYE101Disable(YE101VO vo) {
        return update("ye101DAO.updYE101Disable", vo);
    }

    // 주택임차차입금원리금상환액 전체삭제
    public int allDelYE101(YE101VO vo) {
        return delete("ye101DAO.allDelYE101", vo);
    }

    // 주택임차차입금원리금상환액 Map조회
    @SuppressWarnings("unchecked")
    public List<YE101VO> getYE101Map(YE101VO vo) {
        return (List<YE101VO>) list("ye101DAO.getYE101Map", vo);
    }
}
