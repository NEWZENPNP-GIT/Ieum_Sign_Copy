package com.ezsign.feb.master.dao;

import com.ezsign.feb.master.vo.YE900VO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ye900DAO")
public class YE900DAO extends EgovAbstractDAO {

    // 관리자정정사유 조회
    public YE900VO getYE900(YE900VO vo) {
        return (YE900VO) select("ye900DAO.getYE900", vo);
    }

    @SuppressWarnings("unchecked")
    public List<YE900VO> getYE900List(YE900VO vo) {
        return (List<YE900VO>) list("ye900DAO.getYE900List", vo);
    }

    // 관리자정정사유 입력
    public void insYE900(YE900VO vo) {
        insert("ye900DAO.insYE900", vo);
    }

    // 관리자정정사유 사용여부 '0'
    public int updYE900Disable(YE900VO vo) {
        return update("ye900DAO.updYE900Disable", vo);
    }
}
