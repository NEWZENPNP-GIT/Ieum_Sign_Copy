package com.ezsign.feb.house.dao;

import com.ezsign.feb.house.vo.YE107VO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ye107DAO")
public class YE107DAO extends EgovAbstractDAO {

    // 주택마련저축 조회
    public YE107VO getYE107(YE107VO vo) {
        return (YE107VO) select("ye107DAO.getYE107", vo);
    }

    @SuppressWarnings("unchecked")
    public List<YE107VO> getYE107List(YE107VO vo) {
        return (List<YE107VO>) list("ye107DAO.getYE107", vo);
    }

    // 주택마련저축 입력
    public void insYE107(YE107VO vo) {
        insert("ye107DAO.insYE107", vo);
    }

    // 주택마련저축 사용여부 '0'
    public int updYE107Disable(YE107VO vo) {
        return update("ye107DAO.updYE107Disable", vo);
    }

    // 주택마련저축 전체삭제
    public int allDelYE107(YE107VO vo) {
        return delete("ye107DAO.allDelYE107", vo);
    }

    // 주택마련저축 Map조회
    @SuppressWarnings("unchecked")
    public List<YE107VO> getYE107Map(YE107VO vo) {
        return (List<YE107VO>) list("ye107DAO.getYE107Map", vo);
    }
}
