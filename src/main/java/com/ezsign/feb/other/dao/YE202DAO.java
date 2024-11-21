package com.ezsign.feb.other.dao;

import com.ezsign.feb.other.vo.YE202VO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ye202DAO")
public class YE202DAO extends EgovAbstractDAO {

    // 투자조합_출자 조회
    @SuppressWarnings("unchecked")
    public List<YE202VO> getYE202List(YE202VO vo) {
        return (List<YE202VO>) list("ye202DAO.getYE202", vo);
    }

    // 투자조합_출자 입력
    public void insYE202(YE202VO vo) {
        insert("ye202DAO.insYE202", vo);
    }

    // 투자조합_출자 사용여부 '0'
    public int updYE202Disable(YE202VO vo) {
        return update("ye202DAO.updYE202Disable", vo);
    }

    // 투자조합_출자 전체삭제
    public int allDelYE202(YE202VO vo) {
        return delete("ye202DAO.allDelYE202", vo);
    }

    // 투자조합_출자 Map조회
    @SuppressWarnings("unchecked")
    public List<YE202VO> getYE202Map(YE202VO vo) {
        return (List<YE202VO>) list("ye202DAO.getYE202Map", vo);
    }
}
