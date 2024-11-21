package com.ezsign.feb.master.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ezsign.feb.master.vo.YE000VO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;


@Repository("ye000DAO")
public class YE000DAO extends EgovAbstractDAO {

	// 연말정산_관리자_진행관리
	@SuppressWarnings("unchecked")
	public List<YE000VO> getUserStatusList(YE000VO vo) {
		return (List<YE000VO>)list("ye000DAO.getUserStatusList", vo);
	}
	
	// 연말정산_관리자_진행관리 개수
	public int getUserStatusListCount(YE000VO vo) {
		return (Integer)getSqlMapClientTemplate().queryForObject("ye000DAO.getUserStatusListCount", vo);
	}
	
	// 연말정산_관리자_진행관리_통계
	@SuppressWarnings("unchecked")
	public List<YE000VO> getUserStatusSum(YE000VO vo) {
		return (List<YE000VO>)list("ye000DAO.getUserStatusSum", vo);
	}

	// 연말정산_사용자_근무년월
	@SuppressWarnings("unchecked")
	public List<YE000VO> getUserWorkMonth(YE000VO vo) {
		return (List<YE000VO>)list("ye000DAO.getUserWorkMonth", vo);
	}
	
	// 연말정산_근로자상세정보 조회
	public YE000VO getYE000(YE000VO vo) {
		return (YE000VO) select("ye000DAO.getYE000", vo);
	}
	
	// 연말정산_근로자상세정보 리스트 조회
	@SuppressWarnings("unchecked")
	public List<YE000VO> getYE000List(YE000VO vo) {
		return (List<YE000VO>)list("ye000DAO.getYE000List", vo);
	}
	
	public int getYE000ListCount(YE000VO vo) {
		return (Integer)getSqlMapClientTemplate().queryForObject("ye000DAO.getYE000ListCount", vo);
	}
	
	//  연말정산_근로자상세정보 입력
	public void insYE000(YE000VO vo) {
		insert("ye000DAO.insYE000", vo);
	}
	
	// 연말정산_근로자상세정보 수정
	public int updYE000(YE000VO vo) {
		return update("ye000DAO.updYE000", vo);
	}
	
	// 연말정산_근로자상세정보 삭제
	public int delYE000(YE000VO vo) {
		return delete("ye000DAO.delYE000", vo);
	}
	
	// 연말정산_근로자 도움 리스트 조회
	@SuppressWarnings("unchecked")
	public List<YE000VO> getWorkerAssistList(YE000VO vo) {
		return (List<YE000VO>)list("ye000DAO.getWorkerAssistList", vo);
	} 

	// 연말정산_근로자 도움 리스트 조회
	@SuppressWarnings("unchecked")
	public List<YE000VO> getWorkerAssistDeptList(YE000VO vo) {
		return (List<YE000VO>)list("ye000DAO.getWorkerAssistDeptList", vo);
	} 

	// 연말정산_근로자 수정여부
	public int getYE000ChkEdit(YE000VO vo) {
		return (Integer) select("ye000DAO.getYE000ChkEdit", vo);
	}

	// 연말정산_근로자 수정여부_관리자용
	public int getYE000AdminChkEdit(YE000VO vo) {
		return (Integer) select("ye000DAO.getYE000AdminChkEdit", vo);
	}
	
	// 종전금무지 체크
	public Integer getYE000WorkplaceCount(Map<String, String> params) {
		return (Integer) select("ye000DAO.getYS000WorkplaceCount", params);
	}
	
	/**
	 *
	 * 계약정보 존재여부 체크
	 *
	 * @param paramsVO
	 * @return
	 */
	public Integer getYE000DataCount(YE000VO paramsVO) {
		return (Integer) select("ye000DAO.getYE000DataCount", paramsVO);		
	}
	
	/**
	 *
	 * 계약정보 상세조회
	 *
	 * @param paramsVO
	 * @return
	 */
	public YE000VO getYE000DataDetail(YE000VO paramsVO) {
		return (YE000VO) select("ye000DAO.getYE000DataDetail", paramsVO);
	}

	/**
	 *
	 * 계약정보 존재여부 체크
	 *
	 * @param vo
	 * @return
	 */
	public Integer getYE000NotModified(YE000VO vo) {
		return (Integer) select("ye000DAO.getYE000NotModified", vo);		
	}
	

}
