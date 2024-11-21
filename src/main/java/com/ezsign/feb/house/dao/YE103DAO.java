package com.ezsign.feb.house.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.feb.house.vo.YE103VO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("ye103DAO")
public class YE103DAO extends EgovAbstractDAO {

	// 거주자간주택임차차입금원리금상환액소득공제명세_임대차계약내용 조회
	public YE103VO getYE103(YE103VO vo) {
		return (YE103VO) select("ye103DAO.getYE103", vo);
	}

	@SuppressWarnings("unchecked")
	public List<YE103VO> getYE103List(YE103VO vo) {
		return (List<YE103VO>) list("ye103DAO.getYE103", vo);
	}

	// 거주자간주택임차차입금원리금상환액소득공제명세_임대차계약내용 입력
	public void insYE103(YE103VO vo) {
		insert("ye103DAO.insYE103", vo);
	}

	// 거주자간주택임차차입금원리금상환액소득공제명세_임대차계약내용 사용여부 '0'
	public int updYE103Disable(YE103VO vo) {
		return update("ye103DAO.updYE103Disable", vo);
	}

	// 거주자간주택임차차입금원리금상환액소득공제명세_임대차계약내용 전체삭제
	public int allDelYE103(YE103VO vo) {
		return delete("ye103DAO.allDelYE103", vo);
	}
}
