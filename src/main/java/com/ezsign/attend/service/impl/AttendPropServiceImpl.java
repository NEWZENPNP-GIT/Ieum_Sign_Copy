package com.ezsign.attend.service.impl;

import com.ezsign.attend.dao.AttendPropDAO;
import com.ezsign.attend.service.AttendPropService;
import com.ezsign.attend.vo.AttendPropVO;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("attendPropService")
public class AttendPropServiceImpl extends AbstractServiceImpl implements AttendPropService {

    @Resource(name = "attendPropDAO")
    private AttendPropDAO attendPropDAO;

    @Override
    public AttendPropVO getAttendProp(AttendPropVO vo) {
        return attendPropDAO.getAttendProp(vo);
    }

    @Override
    public void insAttendProp(AttendPropVO vo) {
        attendPropDAO.insAttendProp(vo);
    }
}
