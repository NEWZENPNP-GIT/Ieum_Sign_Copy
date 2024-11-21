package com.ezsign.feb.special.dao;

import com.ezsign.feb.special.vo.YE407VO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ye407DAO")
public class YE407DAO extends EgovAbstractDAO {

    @SuppressWarnings("unchecked")
    public List<YE407VO> getYE407List(YE407VO vo) {
        return (List<YE407VO>) list("ye407DAO.getYE407List", vo);
    }

    public void insYE407(YE407VO vo) {
        insert("ye407DAO.insYE407", vo);
    }

    public int allDelYE407(YE407VO vo) {
        return delete("ye407DAO.allDelYE407", vo);
    }
}
