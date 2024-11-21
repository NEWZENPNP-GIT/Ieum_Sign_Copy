package com.ezsign.feb.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.feb.system.vo.YS030VO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;


@Repository("ys030DAO")
public class YS030DAO extends EgovAbstractDAO {

    // 연말정산_사업장 조회
    @SuppressWarnings("unchecked")
    public List<YS030VO> getYS030List(YS030VO vo) {
        return (List<YS030VO>) list("ys030DAO.getYS030List", vo);
    }

    // 연말정산_사업장 총건수
    public int getYS030ListCount(YS030VO vo) {
        return (Integer) selectByPk("ys030DAO.getYS030ListCount", vo);
    }

    // 연말정산_사업장 입력
    public void insYS030(YS030VO vo) {
        insert("ys030DAO.insYS030", vo);
    }

    // 연말정산_사업장 수정
    public int updYS030(YS030VO vo) {
        return update("ys030DAO.updYS030", vo);
    }

    // 연말정산_사업장 삭제
    public int delYS030(YS030VO vo) {
        return update("ys030DAO.delYS030", vo);
    }

    // 연말정산_사업장 종사업자 일련번호 자동증가 값
    public String getYS030TaxNumber(YS030VO vo) {
        return (String) select("ys030DAO.getYS030TaxNumber", vo);
    }

    public String getYS030ID(YS030VO vo) {
        return (String) select("ys030DAO.getYS030ID", vo);
    }
    
    /*
     * 사업장정보 상세조회
     */
    public YS030VO getYS030Detail(YS030VO vo) {
        return (YS030VO) select("ys030DAO.getYS030Detail", vo);
    }
    
}
