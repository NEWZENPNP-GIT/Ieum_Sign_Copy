package com.ezsign.attend.service;

import com.ezsign.attend.vo.AttendPlaceVO;

import java.util.List;

public interface AttendPlaceService {

    List<AttendPlaceVO> getAttendPlaceList(AttendPlaceVO vo);

    void insAttendPlace(AttendPlaceVO vo);

    int updAttendPlace(AttendPlaceVO vo);

    int delAttendPlace(AttendPlaceVO vo);
}
