package com.ezsign.feb.etc.dao;

import com.ezsign.feb.etc.vo.YE503VO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ye503DAO")
public class YE503DAO extends EgovAbstractDAO {

    // 외국납부세액공제 조회
    public YE503VO getYE503(YE503VO vo) {
        return (YE503VO) select("ye503DAO.getYE503", vo);
    }

    @SuppressWarnings("unchecked")
    public List<YE503VO> getYE503List(YE503VO vo) {
        return (List<YE503VO>) list("ye503DAO.getYE503", vo);
    }

    // 외국납부세액공제 입력
    public void insYE503(YE503VO vo) {
        insert("ye503DAO.insYE503", vo);
    }

    // 외국납부세액공제 사용여부 '0'
    public int updYE503Disable(YE503VO vo) {
        return update("ye503DAO.updYE503Disable", vo);
    }

    // 외국납부세액공제 전체삭제
    public int allDelYE503(YE503VO vo) {
        return delete("ye503DAO.allDelYE503", vo);
    }

    // 외국납부세액공제 Map조회
    @SuppressWarnings("unchecked")
    public List<YE503VO> getYE503Map(YE503VO vo) {
        return (List<YE503VO>) list("ye503DAO.getYE503Map", vo);
    }
}
