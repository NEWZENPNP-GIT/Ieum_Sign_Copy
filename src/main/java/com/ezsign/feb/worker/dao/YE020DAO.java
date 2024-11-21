package com.ezsign.feb.worker.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.feb.worker.vo.YE020VO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("ye020DAO")
public class YE020DAO extends EgovAbstractDAO{
	
	// 연말정산_추가제출서류 조회
	@SuppressWarnings("unchecked")
	public List<YE020VO> getYE020List(YE020VO vo) {
		return (List<YE020VO>)list("ye020DAO.getYE020List", vo);
	}

	// 연말정산_추가제출서류 갯수
	public int getYE020ListCount(YE020VO vo) {
		return (Integer)getSqlMapClientTemplate().queryForObject("ye020DAO.getYE020ListCount", vo);
	}
	
	// 연말정산_추가제출서류 첨부파일 조회
	@SuppressWarnings("unchecked")
	public List<YE020VO> getYE020FileList(YE020VO vo) {
		return (List<YE020VO>)list("ye020DAO.getYE020FileList", vo);
	}

	// 연말정산_추가제출서류첨부파일  갯수
	public int getYE020FileListCount(YE020VO vo) {
		return (Integer)getSqlMapClientTemplate().queryForObject("ye020DAO.getYE020FileListCount", vo);
	}
	
	// 연말정산_추가제출서류 입력
	public void insYE020(YE020VO vo) {
		insert("ye020DAO.insYE020", vo);
	}
	
	// 연말정산_추가제출서류 수정
	public int updYE020(YE020VO vo) {
		return update("ye020DAO.updYE020", vo);
	}

	// 연말정산_추가제출서류 삭제
	public int delYE020(YE020VO vo) {
		return delete("ye020DAO.delYE020", vo);
	}
}
