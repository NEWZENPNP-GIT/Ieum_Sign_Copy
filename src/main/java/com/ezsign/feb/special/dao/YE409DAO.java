package com.ezsign.feb.special.dao;

import com.ezsign.feb.special.vo.YE409VO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ye409DAO")
public class YE409DAO extends EgovAbstractDAO {

    @SuppressWarnings("unchecked")
    public List<YE409VO> getYE409List(YE409VO vo) {
        return (List<YE409VO>) list("ye409DAO.getYE409List", vo);
    }

    public void insYE409(YE409VO vo) {
        insert("ye409DAO.insYE409", vo);
    }

    public int allDelYE409(YE409VO vo) {
        return delete("ye409DAO.allDelYE409", vo);
    }
}
