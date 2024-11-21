package com.ezsign.feb.easyFeb.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ezsign.feb.easyFeb.vo.YE006VO;
import com.ezsign.feb.worker.vo.YE002VO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("ye006DAO")
public class YE006DAO extends EgovAbstractDAO {

    public YE006VO getYE006(Map<String, String> params) {
        return (YE006VO) select("ye006DAO.getYE006", params);
    }

    public Integer getYE006Count(Map<String, String> params) {
        return (Integer) select("ye006DAO.getYE006Count", params);
    }
    
    public void insYE006(YE006VO vo) {
    	insert("ye006DAO.insYE006", vo);
    }
    
    public int updYE006(YE006VO vo) {
        return update("ye006DAO.updYE006", vo);
    }
    
    public int updYE006Admin(YE006VO vo) {
        return update("ye006DAO.updYE006Admin", vo);
    }
    
    public int delYE006(Map<String, String> params) {
        return delete("ye006DAO.delYE006", params);
    }
    
    public YE002VO getYE002(Map<String, String> params) {
        return (YE002VO) select("ye006DAO.getYE002", params);
    }

    public List<YE006VO> getYE006List(Map<String, String> params) {
        return (List<YE006VO>) list("ye006DAO.getYE006List", params);
    }

	public int getYE006ListCount(Map params) {
		return (Integer) select("ye006DAO.getYE006ListCount", params);
	}
    
    public YE006VO getYE006Sum(YE006VO ye006VO) {
        return (YE006VO) select("ye006DAO.getYE006Sum", ye006VO);
    }
}
