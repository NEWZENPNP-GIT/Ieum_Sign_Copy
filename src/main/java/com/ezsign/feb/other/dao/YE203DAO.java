package com.ezsign.feb.other.dao;

import com.ezsign.feb.other.vo.YE203VO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ye203DAO")
public class YE203DAO extends EgovAbstractDAO {

    // 우리사주조합출연금 조회
    public YE203VO getYE203(YE203VO vo) {
        return (YE203VO) select("ye203DAO.getYE203", vo);
    }
    

    // 우리사주조합출연금벤처 조회
    public YE203VO getYE203_1(YE203VO vo) {
        return (YE203VO) select("ye203DAO.getYE203_1", vo);
    }

    @SuppressWarnings("unchecked")
    public List<YE203VO> getYE203List(YE203VO vo) {
        return (List<YE203VO>) list("ye203DAO.getYE203", vo);
    }

    @SuppressWarnings("unchecked")
    public List<YE203VO> getYE203_1List(YE203VO vo) {
        return (List<YE203VO>) list("ye203DAO.getYE203_1", vo);
    }

    // 우리사주조합출연금 입력
    public void insYE203(YE203VO vo) {
        insert("ye203DAO.insYE203", vo);
    }

    // 우리사주조합출연금 사용여부 '0'
    public int updYE203Disable(YE203VO vo) {
        return update("ye203DAO.updYE203Disable", vo);
    }

    // 우리사주조합출연금 전체삭제
    public int allDelYE203(YE203VO vo) {
        return delete("ye203DAO.allDelYE203", vo);
    }

    // 우리사주조합출연금 조회
    @SuppressWarnings("unchecked")
    public List<YE203VO> getYE203Map(YE203VO vo) {
        return (List<YE203VO>) list("ye203DAO.getYE203Map", vo);
    }
}
