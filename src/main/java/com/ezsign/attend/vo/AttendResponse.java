package com.ezsign.attend.vo;

import java.util.List;

public class AttendResponse {

    public boolean success = false;
    public int total = 0;

    public AttendPropVO attendProp;
    public List<AttendVO> attends;
    public List<ZipcodeVO> zipcodes;
}
