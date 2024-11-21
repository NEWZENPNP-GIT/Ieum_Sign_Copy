package com.ezsign.feb.worker.dao;

import org.springframework.stereotype.Repository;

import com.ezsign.feb.master.vo.YP000VO;
import com.ezsign.feb.worker.vo.YP002VO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("yp002DAO")
public class YP002DAO extends EgovAbstractDAO {

	
	// 간이지급명세서_주/종근무지 상세 조회
    public YP002VO getYP002(YP002VO vo) {
        return (YP002VO) select("yp002DAO.getYP002", vo);
    }
    
    // 간이지급명세서_주/종근무지 입력
    public void insYP002(YP000VO vo) {
        insert("yp002DAO.insYP002", vo);
    }

    // 간이지급명세서_주/종근무지 수정
    public int updYP002(YP000VO vo) {
        return update("yp002DAO.updYP002", vo);
    }
}
