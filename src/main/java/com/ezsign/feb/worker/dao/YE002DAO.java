package com.ezsign.feb.worker.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.worker.vo.YE002VO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("ye002DAO")
public class YE002DAO extends EgovAbstractDAO {

    // 연말정산_주/종근무지 전체 조회
    @SuppressWarnings("unchecked")
    public List<YE002VO> getYE002List(YE002VO vo) {
        return (List<YE002VO>) list("ye002DAO.getYE002List", vo);
    }

    // 연말정산_주/종근무지 갯수
    public int getYE002ListCount(YE002VO vo) {
        return (Integer) select("ye002DAO.getYE002ListCount", vo);
    }

    // 연말정산_주/종근무지 조회
    public YE002VO getYE002(YE002VO vo) {
        return (YE002VO) select("ye002DAO.getYE002", vo);
    }

    // 연말정산_주/종근무지 입력
    public void insYE002(YE000VO vo) {
        insert("ye002DAO.insYE002", vo);
    }

    // 연말정산_주/종근무지 수정
    public int updYE002(YE000VO vo) {
        return update("ye002DAO.updYE002", vo);
    }
        
    // 연말정산_주/종근무지 삭제
    public int delYE002(YE000VO vo) {
        return delete("ye002DAO.delYE002", vo);
    }

    // 연말정산_주/종근무지 세액감면조회
    @SuppressWarnings("unchecked")
    public List<YE002VO> getYE002TaxList(YE002VO vo) {
        return (List<YE002VO>) list("ye002DAO.getYE002TaxList", vo);
    }


	// 연말정산_기초설정 수정
	public int getYE002ChkEdit(YE002VO vo) {
		return (Integer) getSqlMapClientTemplate().queryForObject("ye002DAO.getYE002ChkEdit", vo);
	}
	
	/**
	 *
	 * 근로자 주(현)/종(전) 근무지 체크
	 *
	 * @param vo
	 * @return
	 */
	public int getYE002DataCnt(YE002VO vo) {
		return (Integer) select("ye002DAO.getYE002DataCnt", vo);
	}
	
}
