package com.ezsign.attend.service;

import com.ezsign.attend.vo.AttendVO;

import java.util.List;

public interface AttendService {

    List<AttendVO> getAttendList(AttendVO vo);
    
    List<AttendVO> getMobileAttendList(AttendVO vo);
    
    List<AttendVO> getMobileUserAttendList(AttendVO vo);
    
    int  getAttendListCount(AttendVO vo);

    void insAttend(AttendVO vo);

    int offAttend(AttendVO vo);

    int updAttend(AttendVO vo);
    
    int updMobileAttend(AttendVO vo);

    List<AttendVO> getAttendSumList(AttendVO vo);
}
