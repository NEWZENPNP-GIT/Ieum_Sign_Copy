package com.ezsign.attend.service.impl;

import com.ezsign.attend.dao.AttendDAO;
import com.ezsign.attend.service.AttendService;
import com.ezsign.attend.vo.AttendVO;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("attendService")
public class AttendServiceImpl extends AbstractServiceImpl implements AttendService {

    @Resource(name = "attendDAO")
    private AttendDAO attendDAO;

    @Override
    public List<AttendVO> getAttendList(AttendVO vo) {
        return attendDAO.getAttendList(vo);
    }
    
    @Override
    public int getAttendListCount(AttendVO vo) {
        return attendDAO.getAttendListCount(vo);
    }
    
    @Override
    public void insAttend(AttendVO vo) {
        attendDAO.insAttend(vo);
    }

    @Override
    public int offAttend(AttendVO vo) {
        return attendDAO.offAttend(vo);
    }

    @Override
    public int updAttend(AttendVO vo) {
        return attendDAO.updAttend(vo);
    }
    
    @Override
    public int updMobileAttend(AttendVO vo) {
        return attendDAO.updMobileAttend(vo);
    }

    @Override
    public List<AttendVO> getAttendSumList(AttendVO vo) {
        return attendDAO.getAttendSumList(vo);
    }

	@Override
	public List<AttendVO> getMobileAttendList(AttendVO vo) {
		return attendDAO.getMobileAttendList(vo);
	}
	
	@Override
	public List<AttendVO> getMobileUserAttendList(AttendVO vo) {
		return attendDAO.getMobileUserAttendList(vo);
	}
}
