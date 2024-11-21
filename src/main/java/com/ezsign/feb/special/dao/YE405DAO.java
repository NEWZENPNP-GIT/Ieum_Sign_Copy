package com.ezsign.feb.special.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.feb.special.vo.YE405VO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("ye405DAO")
public class YE405DAO extends EgovAbstractDAO {

    // 연말정산_이월기부금 조회
    @SuppressWarnings("unchecked")
    public List<YE405VO> getYE405List(YE405VO vo) {
        return (List<YE405VO>) list("ye405DAO.getYE405List", vo);
    }

    // 연말정산_이월기부금 갯수
    public YE405VO getYE405ListCount(YE405VO vo) {
        return (YE405VO) select("ye405DAO.getYE405ListCount", vo);
    }

    // 연말정산_이월기부금 갯수
    public int getYE405Count(YE405VO vo) {
        return (Integer) select("ye405DAO.getYE405Count", vo);
    }

    // 연말정산_이월기부금 입력
    public void insYE405(YE405VO vo) {
        insert("ye405DAO.insYE405", vo);
    }

    // 연말정산_이월기부금 수정
    public int updYE405(YE405VO vo) {
        return update("ye405DAO.updYE405", vo);
    }

    // 연말정산_이월기부금 삭제
//    public int delYE405(YE405VO vo) {
//        return delete("ye405DAO.delYE405", vo);
//    }

    // 연말정산_이월기부금 사용여부 '0'
    public int updYE405Disable(YE405VO vo) {
        return update("ye405DAO.updYE405Disable", vo);
    }

    // 연말정산_이월기부금 전체삭제
    public int allDelYE405(YE405VO vo) {
        return delete("ye405DAO.allDelYE405", vo);
    }

    // 연말정산_이월기부금 Map조회
    @SuppressWarnings("unchecked")
    public List<YE405VO> getYE405Map(YE405VO vo) {
        return (List<YE405VO>) list("ye405DAO.getYE405Map", vo);
    }
}
