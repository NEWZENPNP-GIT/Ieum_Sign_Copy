package com.ezsign.feb.house.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.feb.house.vo.YE102VO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("ye102DAO")
public class YE102DAO extends EgovAbstractDAO {

	// 거주자간주택임차차입금원리금상환액소득공제명세_금전소비대차계약내용 조회
	public YE102VO getYE102(YE102VO vo) {
		return (YE102VO) select("ye102DAO.getYE102", vo);
	}

	@SuppressWarnings("unchecked")
	public List<YE102VO> getYE102List(YE102VO vo) {
		return (List<YE102VO>) list("ye102DAO.getYE102", vo);
	}

	// 거주자간주택임차차입금원리금상환액소득공제명세_금전소비대차계약내용 입력
	public void insYE102(YE102VO vo) {
		insert("ye102DAO.insYE102", vo);
	}

	// 거주자간주택임차차입금원리금상환액소득공제명세_금전소비대차계약내용 사용여부 '0'
	public int updYE102Disable(YE102VO vo) {
		return update("ye102DAO.updYE102Disable", vo);
	}

	// 거주자간주택임차차입금원리금상환액소득공제명세_금전소비대차계약내용 전체삭제
	public int allDelYE102(YE102VO vo) {
		return delete("ye102DAO.allDelYE102", vo);
	}
}
