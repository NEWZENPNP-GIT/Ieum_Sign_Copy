package com.ezsign.feb.special.dao;

import com.ezsign.feb.special.vo.YE410VO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ye410DAO")
public class YE410DAO extends EgovAbstractDAO {

    @SuppressWarnings("unchecked")
    public List<YE410VO> getYE410List(YE410VO vo) {
        return (List<YE410VO>) list("ye410DAO.getYE410List", vo);
    }

    public void insYE410(YE410VO vo) {
        insert("ye410DAO.insYE410", vo);
    }

    public int allDelYE410(YE410VO vo) {
        return delete("ye410DAO.allDelYE410", vo);
    }
}
