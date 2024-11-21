package com.ezsign.push.dao;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.annual.vo.AnnualVacationVO;
import com.ezsign.push.vo.PushVO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;


@Repository("pushDAO")
public class PushDAO extends EgovAbstractDAO {
	
	@SuppressWarnings("unchecked")
	public List<PushVO> getPushList(PushVO vo) {
		return (List<PushVO>) list("pushDAO.getPushList", vo);
	}
	
	@SuppressWarnings("unchecked")
    public PushVO getReadCnt(PushVO vo) {
        return (PushVO) select("pushDAO.getReadCnt", vo);
    }

	@SuppressWarnings("unchecked")
    public PushVO getPushMessage(PushVO vo) {
        return (PushVO) select("pushDAO.getPushMessage", vo);
    }
	
	public int insPushReadYn(PushVO vo) {
		return (Integer)selectByPk("pushDAO.insPushReadYn", vo);
	}
	
	public void insPushRead(PushVO vo) {
		insert("pushDAO.insPushRead", vo);
	}

	@SuppressWarnings("unchecked")
	public List<PushVO> getPushSendList(PushVO vo) {
		return (List<PushVO>) list("pushDAO.getPushSendList", vo);
	}
	
	public int updPushSendList(PushVO vo) {
		return update("pushDAO.updPushSendList", vo);
	}
	
	

	public void insPushSendUser(PushVO vo) {
        insert("pushDAO.insPushSendUser", vo);
    }
	
	public void insPushSendTopic(PushVO vo) {
        insert("pushDAO.insPushSendTopic", vo);
    }
}
