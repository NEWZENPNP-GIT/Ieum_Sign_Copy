package com.ezsign.attend.service.impl;

import com.ezsign.attend.dao.AttendPlaceDAO;
import com.ezsign.attend.service.AttendPlaceService;
import com.ezsign.attend.vo.AttendPlaceVO;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("attendPlaceService")
public class AttendPlaceServiceImpl extends AbstractServiceImpl implements AttendPlaceService {

    @Resource(name = "attendPlaceDAO")
    private AttendPlaceDAO attendPlaceDAO;

    @Override
    public List<AttendPlaceVO> getAttendPlaceList(AttendPlaceVO vo) {
        return attendPlaceDAO.getAttendPlaceList(vo);
    }

    @Override
    public void insAttendPlace(AttendPlaceVO vo) {
        attendPlaceDAO.insAttendPlace(vo);
    }

    @Override
    public int updAttendPlace(AttendPlaceVO vo) {
        return attendPlaceDAO.updAttendPlace(vo);
    }

    @Override
    public int delAttendPlace(AttendPlaceVO vo) {
        return attendPlaceDAO.delAttendPlace(vo);
    }
}
