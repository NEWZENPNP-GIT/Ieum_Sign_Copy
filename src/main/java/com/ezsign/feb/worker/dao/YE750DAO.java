package com.ezsign.feb.worker.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.feb.worker.vo.YE750VO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("ye750DAO")
public class YE750DAO extends EgovAbstractDAO {

    // 연말정산출력물 조회
    @SuppressWarnings("unchecked")
    public List<YE750VO> getYE750List(YE750VO vo) {
        return (List<YE750VO>) list("ye750DAO.getYE750List", vo);
    }

    // 연말정산출력물 입력
    public void insYE750(YE750VO vo) {
        insert("ye750DAO.insYE750", vo);
    }

    // 연말정산출력물 삭제
    public int allDelYE750(YE750VO vo) {
        return delete("ye750DAO.allDelYE750", vo);
    }
    
    //첫로딩시 첫 row 사용자 아이디 조회 
    public YE750VO getYE750UserId(YE750VO vo) {
        return (YE750VO) select("ye750DAO.getYE750UserId", vo);
    }
    
    
    /**
     * 파일ID의 파일 정보를 조회한다.
     * 
     * @param vo
     * @return
     */
    public YE750VO getYE750FileInfo(YE750VO vo) {
    	return (YE750VO) select("ye750DAO.getYE750FileInfo", vo);
    }
    
    /**
     * 연말정산 영수증 파일정보 및 사용자 이메일정보
     * 
     * @param vo
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<YE750VO> getYE750UserList(YE750VO vo) {
        return (List<YE750VO>) list("ye750DAO.getYE750UserList", vo);
    }
    
}
