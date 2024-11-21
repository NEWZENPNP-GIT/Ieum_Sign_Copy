package com.ezsign.feb.house.dao;

import com.ezsign.feb.house.vo.YE106VO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ye106DAO")
public class YE106DAO extends EgovAbstractDAO {

    // 개인연금저축 조회
    public YE106VO getYE106(YE106VO vo) {
        return (YE106VO) select("ye106DAO.getYE106", vo);
    }

    @SuppressWarnings("unchecked")
    public List<YE106VO> getYE106List(YE106VO vo) {
        return (List<YE106VO>) list("ye106DAO.getYE106", vo);
    }

    // 개인연금저축 입력
    public void insYE106(YE106VO vo) {
        insert("ye106DAO.insYE106", vo);
    }

    // 개인연금저축 사용여부 '0'
    public int updYE106Disable(YE106VO vo) {
        return update("ye106DAO.updYE106Disable", vo);
    }

    // 개인연금저축 전체삭제
    public int allDelYE106(YE106VO vo) {
        return delete("ye106DAO.allDelYE106", vo);
    }

    // 개인연금저축 Map조회
    @SuppressWarnings("unchecked")
    public List<YE106VO> getYE106Map(YE106VO vo) {
        return (List<YE106VO>) list("ye106DAO.getYE106Map", vo);
    }
}
