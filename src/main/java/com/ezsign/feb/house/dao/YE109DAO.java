package com.ezsign.feb.house.dao;

import com.ezsign.feb.house.vo.YE109VO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ye109DAO")
public class YE109DAO extends EgovAbstractDAO {

    // 장기집합투자증권저축 조회
    public YE109VO getYE109(YE109VO vo) {
        return (YE109VO) select("ye109DAO.getYE109", vo);
    }

    @SuppressWarnings("unchecked")
    public List<YE109VO> getYE109List(YE109VO vo) {
        return (List<YE109VO>) list("ye109DAO.getYE109", vo);
    }

    // 장기집합투자증권저축 입력
    public void insYE109(YE109VO vo) {
        insert("ye109DAO.insYE109", vo);
    }

    // 장기집합투자증권저축 사용여부 '0'
    public int updYE109Disable(YE109VO vo) {
        return update("ye109DAO.updYE109Disable", vo);
    }

    // 장기집합투자증권저축 전체삭제
    public int allDelYE109(YE109VO vo) {
        return delete("ye109DAO.allDelYE109", vo);
    }

    // 장기집합투자증권저축 Map조회
    @SuppressWarnings("unchecked")
    public List<YE109VO> getYE109Map(YE109VO vo) {
        return (List<YE109VO>) list("ye109DAO.getYE109Map", vo);
    }
}
