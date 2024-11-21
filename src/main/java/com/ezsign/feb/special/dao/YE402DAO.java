package com.ezsign.feb.special.dao;

import com.ezsign.feb.special.vo.YE402VO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ye402DAO")
public class YE402DAO extends EgovAbstractDAO {

    // 의료비 (국세청 자료/기타자료) 조회
    public YE402VO getYE402(YE402VO vo) {
        return (YE402VO) select("ye402DAO.getYE402", vo);
    }

    @SuppressWarnings("unchecked")
	public List<YE402VO> getYE402List(YE402VO vo) {
		return (List<YE402VO>) list("ye402DAO.getYE402", vo);
	}
    
    // 의료비 (국세청 자료/기타자료/회사자료) 입력
    public void insYE402(YE402VO vo) {
        insert("ye402DAO.insYE402", vo);
    }

    // 의료비 (국세청 자료/기타자료) 사용여부 '0'
    public int updYE402Disable(YE402VO vo) {
        return update("ye402DAO.updYE402Disable", vo);
    }

    // 의료비 (국세청 자료/기타자료) 전체삭제
    public int allDelYE402(YE402VO vo) {
        return delete("ye402DAO.allDelYE402", vo);
    }

    // 의료비 (국세청 자료/기타자료) Map조회
    @SuppressWarnings("unchecked")
    public List<YE402VO> getYE402Map(YE402VO vo) {
        return (List<YE402VO>) list("ye402DAO.getYE402Map", vo);
    }
    
    // 의료비 (회사자료) 수정 - 공제불가회사지원금
    public int updYE402(YE402VO vo) {
        return update("ye402DAO.updYE402", vo);
    }
    
    // 의료비 (회사자료) 삭제 - 공제불가회사지원금
    public int delYE402(YE402VO vo) {
        return delete("ye402DAO.delYE402", vo);
    }
    
}
