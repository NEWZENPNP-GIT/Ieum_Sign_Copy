package com.ezsign.feb.house.dao;

import com.ezsign.feb.house.vo.YE108VO;
import com.ezsign.feb.special.vo.YE403VO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ye108DAO")
public class YE108DAO extends EgovAbstractDAO {

    // 신용카드 조회
    public YE108VO getYE108(YE108VO vo) {
        return (YE108VO) select("ye108DAO.getYE108", vo);
    }
    
    @SuppressWarnings("unchecked")
	public List<YE108VO> getYE108List(YE108VO vo) {
		return (List<YE108VO>) list("ye108DAO.getYE108", vo);
	}
    
    // 신용카드 (회사자료) 입력
    public void insYE108(YE108VO vo) {
        insert("ye108DAO.insYE108", vo);
    }

    // 신용카드 사용여부 '0'
    public int updYE108Disable(YE108VO vo) {
        return update("ye108DAO.updYE108Disable", vo);
    }

    // 신용카드 전체삭제
    public int allDelYE108(YE108VO vo) {
        return delete("ye108DAO.allDelYE108", vo);
    }

    // 신용카드 Map조회
    @SuppressWarnings("unchecked")
    public List<YE108VO> getYE108Map(YE108VO vo) {
        return (List<YE108VO>) list("ye108DAO.getYE108Map", vo);
    }
    
    // 신용카드 (회사자료) 수정 - 공제불가회사지원금
    public int updYE108(YE108VO vo) {
        return update("ye108DAO.updYE108", vo);
    }
    
    // 신용카드 (회사자료) 삭제 - 공제불가회사지원금
    public int delYE108(YE108VO vo) {
        return delete("ye108DAO.delYE108", vo);
    }
    
}
