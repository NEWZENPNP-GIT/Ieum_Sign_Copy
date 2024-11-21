package com.ezsign.feb.special.dao;

import com.ezsign.feb.special.vo.YE403VO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ye403DAO")
public class YE403DAO extends EgovAbstractDAO {

    // 교육비 조회
    public YE403VO getYE403(YE403VO vo) {
        return (YE403VO) select("ye403DAO.getYE403", vo);
    }

    @SuppressWarnings("unchecked")
	public List<YE403VO> getYE403List(YE403VO vo) {
		return (List<YE403VO>) list("ye403DAO.getYE403", vo);
	}
    
    // 교육비 (회사자료) 입력
    public void insYE403(YE403VO vo) {
        insert("ye403DAO.insYE403", vo);
    }

    // 교육비 사용여부 '0'
    public int updYE403Disable(YE403VO vo) {
        return update("ye403DAO.updYE403Disable", vo);
    }

    // 교육비 전체삭제
    public int allDelYE403(YE403VO vo) {
        return delete("ye403DAO.allDelYE403", vo);
    }

    // 교육비 Map조회
    @SuppressWarnings("unchecked")
    public List<YE403VO> getYE403Map(YE403VO vo) {
        return (List<YE403VO>) list("ye403DAO.getYE403Map", vo);
    }
    
    // 의료비 (회사자료) 수정 - 공제불가회사지원금
    public int updYE403(YE403VO vo) {
        return update("ye403DAO.updYE403", vo);
    }
    
    // 의료비 (회사자료) 삭제 - 공제불가회사지원금
    public int delYE403(YE403VO vo) {
        return delete("ye403DAO.delYE403", vo);
    }
    
}
