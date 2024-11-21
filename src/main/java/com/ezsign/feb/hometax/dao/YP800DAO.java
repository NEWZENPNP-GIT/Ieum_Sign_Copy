package com.ezsign.feb.hometax.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ezsign.feb.hometax.vo.PaymentARecordVO;
import com.ezsign.feb.hometax.vo.PaymentBRecordVO;
import com.ezsign.feb.hometax.vo.PaymentCRecordVO;
import com.ezsign.feb.hometax.vo.YP800VO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("yp800DAO")
public class YP800DAO extends EgovAbstractDAO {
	
	/**
     * 전자신고 등록
     * 
     * @param vo
     * @return
     */
    public String insYP800(YP800VO vo) {
    	return (String) insert("yp800DAO.insYP800", vo);
    }

    @SuppressWarnings("unchecked")
    public List<YP800VO> getYP800List(YP800VO vo) {
        return (List<YP800VO>) list("yp800DAO.getYP800List", vo);
    }
    
    public int getYP800Cnt(YP800VO vo) {
    	return (Integer) select("yp800DAO.getYP800Cnt", vo);
    }
    
    public void updYP800(YP800VO vo) {
    	update("yp800DAO.updYP800", vo);
    }
    
    
	/**
     * A레코드 정보 조회
     * 
     * @param vo
     * @return
     */
    public PaymentARecordVO getARecordInfo(YP800VO vo) {
        return (PaymentARecordVO) select("yp800DAO.getARecordInfo", vo);
    }
 
    /**
     * B레코드 정보 조회 
     * 
     * @param vo
     * @return
     */
	public PaymentBRecordVO getBRecordInfo(YP800VO vo) {
        return (PaymentBRecordVO) select("yp800DAO.getBRecordInfo", vo);
    }
	
	/**
     * C레코드 정보 조회 
     * 
     * @param vo
     * @return
     */
	public PaymentCRecordVO getCRecordInfo(YP800VO vo) {
        return (PaymentCRecordVO) select("yp800DAO.getCRecordInfo", vo);
    }
	
	/**
     * 근로소득자 사용자 아이디 정보를 조회한다. (사업장기준)
     * 
     * @param vo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> getEmpList(YP800VO vo) {
        return (List<Map<String,Object>>) list("yp800DAO.getEmpList", vo);
    }    
	
}
