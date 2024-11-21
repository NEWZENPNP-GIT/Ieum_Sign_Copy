package com.ezsign.feb.worker.dao;

import com.ezsign.feb.worker.vo.YE700VO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository("ye700DAO")
public class YE700DAO extends EgovAbstractDAO {

    // 연말정산요약결과 조회
    @SuppressWarnings("unchecked")
    public List<YE700VO> getYE700(YE700VO vo) {
        return (List<YE700VO>) list("ye700DAO.getYE700", vo);
    }

    // 연말정산요약결과 입력
    public void insYE700(YE700VO vo) {
        insert("ye700DAO.insYE700", vo);
    }

    // 연말정산요약결과 삭제
    public int allDelYE700(YE700VO vo) {
        return delete("ye700DAO.allDelYE700", vo);
    }

    // 연말정산요약결과 삭제
    public int updYE700(YE700VO vo) {
        return update("ye700DAO.updYE700", vo);
    }
    
    // 연말정산요약결과 리스트 조회
 	@SuppressWarnings("unchecked")
 	public List<YE700VO> getYE700WorkerList(YE700VO vo) {
 		return (List<YE700VO>) list("ye700DAO.getYE700WorkerList", vo);
 	}
 	
 	// 연말정산요약결과 리스트 조회 개수
 	public int getYE700WorkerListCount(YE700VO vo) {
 		return (Integer)getSqlMapClientTemplate().queryForObject("ye700DAO.getYE700WorkerListCount", vo);
 	}
 	
 	// 근로소득금액 및 세액공액 배분
 	public HashMap callProcDistribution(YE700VO vo) {
 		return (HashMap)getSqlMapClientTemplate().queryForObject("ye700DAO.callProcDistribution", vo);
 	}
 	
}
