package com.ezsign.feb.worker.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.feb.worker.vo.YE013VO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("ye013DAO")
public class YE013DAO extends EgovAbstractDAO{
	
	// 연말정산_공제불가회사지원금 조회
	@SuppressWarnings("unchecked")
	public List<YE013VO> getYE013List(YE013VO vo) {
		return (List<YE013VO>)list("ye013DAO.getYE013List", vo);
	}

	// 연말정산_공제불가회사지원금  갯수
	public YE013VO getYE013ListCount(YE013VO vo) {
		return (YE013VO) select("ye013DAO.getYE013ListCount", vo);
	}
	
	// 연말정산_공제불가회사지원금  입력
	public void insYE013(YE013VO vo) {
		insert("ye013DAO.insYE013", vo);
	}
	
	// 연말정산_공제불가회사지원금  수정
	public int updYE013(YE013VO vo) {
		return update("ye013DAO.updYE013", vo);
	}

	// 연말정산_공제불가회사지원금  삭제
	public int delYE013(YE013VO vo) {
		return delete("ye013DAO.delYE013", vo);
	}
}
