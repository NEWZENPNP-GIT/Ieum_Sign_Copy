package com.ezsign.feb.special.dao;

import com.ezsign.feb.special.vo.YE411VO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ye411DAO")
public class YE411DAO extends EgovAbstractDAO {

    @SuppressWarnings("unchecked")
    public YE411VO getYE411(YE411VO vo) {
        return (YE411VO) select("ye411DAO.getYE411", vo);
    }

    public void insYE411(YE411VO vo) {
        insert("ye411DAO.insYE411", vo);
    }

    public int allDelYE411(YE411VO vo) {
        return delete("ye411DAO.allDelYE411", vo);
    }
}
