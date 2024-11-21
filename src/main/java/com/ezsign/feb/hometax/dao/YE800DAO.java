package com.ezsign.feb.hometax.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ezsign.feb.hometax.vo.ARecordVO;
import com.ezsign.feb.hometax.vo.BRecordVO;
import com.ezsign.feb.hometax.vo.CARecordVO;
import com.ezsign.feb.hometax.vo.CRecordVO;
import com.ezsign.feb.hometax.vo.DRecordVO;
import com.ezsign.feb.hometax.vo.FRecordDataVO;
import com.ezsign.feb.hometax.vo.IRecordVO;
import com.ezsign.feb.hometax.vo.YE800VO;
import com.ezsign.feb.house.vo.YE102VO;
import com.ezsign.feb.house.vo.YE103VO;
import com.ezsign.feb.house.vo.YE105VO;
import com.ezsign.feb.house.vo.YE108VO;
import com.ezsign.feb.special.vo.YE401VO;
import com.ezsign.feb.special.vo.YE402VO;
import com.ezsign.feb.special.vo.YE403VO;
import com.ezsign.feb.special.vo.YE404VO;
import com.ezsign.feb.special.vo.YE408VO;
import com.ezsign.feb.worker.vo.YE001VO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("ye800DAO")
public class YE800DAO extends EgovAbstractDAO {

    // 전자신고 조회
    @SuppressWarnings("unchecked")
    public List<YE800VO> getYE800List(YE800VO vo) {
        return (List<YE800VO>) list("ye800DAO.getYE800List", vo);
    }
    
    /**
     * 전자신고 등록
     * 
     * @param vo
     * @return
     */
    public String insYE800(YE800VO vo) {
    	return (String) insert("ye800DAO.insYE800", vo);
    }

    public int getYE800Cnt(YE800VO vo) {
    	return (Integer) select("ye800DAO.getYE800Cnt", vo);
    }
    
    public void updYE800(YE800VO vo) {
    	update("ye800DAO.updYE800", vo);
    }
    
    /**
     * 근로소득자 사용자 아이디 정보를 조회한다. (사업장기준)
     * 
     * @param vo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> getEmpList(YE800VO vo) {
        return (List<Map<String,Object>>) list("ye800DAO.getEmpList", vo);
    }
    
    
    /**
     * A레코드 정보 조회
     * 
     * @param vo
     * @return
     */
    public ARecordVO getARecordInfo(YE800VO vo) {
        return (ARecordVO) select("ye800DAO.getARecordInfo", vo);
    }
    
    
    /**
     * B레코드 정보 조회 
     * 
     * @param vo
     * @return
     */
	public BRecordVO getBRecordInfo(YE800VO vo) {
        return (BRecordVO) select("ye800DAO.getBRecordInfo", vo);
    }
    
    /**
     * C레코드 정보 조회 
     * 
     * @param vo
     * @return
     */
	public CRecordVO getCRecordInfo(YE800VO vo) {
        return (CRecordVO) select("ye800DAO.getCRecordInfo", vo);
    }
	
	/**
     * D레코드 정보 조회 
     * 
     * @param vo
     * @return
     */
	@SuppressWarnings("unchecked")
	public List<DRecordVO> getDRecordInfo(YE800VO vo) {
        return (List<DRecordVO>) list("ye800DAO.getDRecordInfo", vo);
    }
    
	/**
	 * 소득공제명세의 인적사항 조회
	 * 
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<YE001VO> getYe001List(YE800VO vo) {
		return (List<YE001VO>) list("ye800DAO.getYe001List", vo);
	}
	
	/**
	 * 보험료
	 * 
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<YE401VO> getYe401List(YE401VO vo) {
		return (List<YE401VO>) list("ye800DAO.getYe401List", vo);
	}
	
	/**
	 * 의료비
	 * 
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<YE402VO> getYe402List(YE402VO vo) {
		return (List<YE402VO>) list("ye800DAO.getYe402List", vo);
	}
	
	/**
	 * 교육비
	 * 
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<YE403VO> getYe403List(YE403VO vo) {
		return (List<YE403VO>) list("ye800DAO.getYe403List", vo);
	}
	
	/**
	 * 신용카드
	 * 
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<YE108VO> getYe108List(YE108VO vo) {
		return (List<YE108VO>) list("ye800DAO.getYe108List", vo);
	}
	
	/**
	 * 기부금
	 * 
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<YE404VO> getYe404List(YE404VO vo) {
		return (List<YE404VO>) list("ye800DAO.getYe404List", vo);
	}
	
	/**
	 * F 레코드 정보  (연금･저축등 소득·세액 공제명세)
	 * 
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FRecordDataVO> getFRecordList(YE800VO vo) {
		return (List<FRecordDataVO>) list("ye800DAO.getFRecordList", vo);
	}
	
	/**
	 * 월세액 세액공제명세 
	 * 
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<YE105VO> getYe105List(YE800VO vo) {
		return (List<YE105VO>) list("ye800DAO.getYe105List", vo);
	}
	
	/**
	 * 거주자간 주택임차차입금원리금상환액 - 금전소비대차 계약내용 
	 * 
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<YE102VO> getYe102List(YE800VO vo) {
		return (List<YE102VO>) list("ye800DAO.getYe102List", vo);
	}
	
	/**
	 * 거주자간 주택임차차입금원리금상환액 - 임대차 계약내용
	 * 
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<YE103VO> getYe103List(YE800VO vo) {
		return (List<YE103VO>) list("ye800DAO.getYe103List", vo);
	}
	
	/**
	 * 기부금 조정명세 레코드
	 * 
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<YE408VO> getYe408List(YE800VO vo) {
		return (List<YE408VO>) list("ye800DAO.getYe408List", vo);
	}
	
	/**
	 * 해당 연도 기부명세 리스트
	 * 
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<IRecordVO> getIRecordList(YE800VO vo) {
		return (List<IRecordVO>) list("ye800DAO.getIRecordList", vo);
	}
	
	/**
     * CA레코드 정보 조회 
     * 
     * @param vo
     * @return
     */
	@SuppressWarnings("unchecked")
	public List<CARecordVO> getCARecordInfo(YE800VO vo) {
        return (List<CARecordVO>) list("ye800DAO.getCARecordInfo", vo);
    }
}
