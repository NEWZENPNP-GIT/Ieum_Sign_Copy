package com.ezsign.feb.master.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.feb.master.vo.YP000VO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("yp000DAO")
public class YP000DAO extends EgovAbstractDAO {

 
	// 간이지급명세서_근로자상세정보 조회
	public YP000VO getYP000(YP000VO vo) {
			return (YP000VO) select("yp000DAO.getYP000", vo);
	}
		
	/**
	 * 간이지급명세서 근로자 정보 리스트
	 * 
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<YP000VO> getYP000List(YP000VO vo) {
		return (List<YP000VO>)list("yp000DAO.getYP000List", vo);
	}
	
	/**
	 * 간이지급명세서 근로자 정보 리스트 카운트
	 * 
	 * @param vo
	 * @return
	 */
	public int getYP000ListCount(YP000VO vo) {
		return (Integer)select("yp000DAO.getYP000ListCount", vo);
	}
	
	/**
	 *
	 * 계약정보 존재여부 체크
	 *
	 * @param paramsVO
	 * @return
	 */
	public Integer getYP000DataCount(YP000VO paramsVO) {
		return (Integer) select("yp000DAO.getYP000DataCount", paramsVO);		
	}
	
	
	//  간이지급명세서_근로자상세정보 입력
	public void insYP000(YP000VO vo) {
		insert("yp000DAO.insYP000", vo);
	}
	
	// 간이지급명세서_근로자상세정보 수정
	public int updYP000(YP000VO vo) {
		return update("yp000DAO.updYP000", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<YP000VO> getPaymentUserStatusList(YP000VO vo) {
		return (List<YP000VO>)list("yp000DAO.getPaymentUserStatusList", vo);
	}
	
	public int getPaymentUserStatusListCount(YP000VO vo) {
		return (Integer)select("yp000DAO.getPaymentUserStatusListCount", vo);
	}
	
	// 간이지급명세서_사용자_근무년월
	@SuppressWarnings("unchecked")
	public List<YP000VO> getPaymentUserWorkMonth(YP000VO vo) {
		return (List<YP000VO>)list("yp000DAO.getPaymentUserWorkMonth", vo);
	}
	
}
