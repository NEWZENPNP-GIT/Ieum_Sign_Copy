package com.ezsign.attend.vo;

import java.util.List;

public class AttendSumResponse {

    public boolean success = false;
    public List<AttendSum> attendSums;
    public AttendSum attendSumsTotal;
    public AttendSum attendDay;

    public static class AttendSum {
        public String empNo;
        public String empName;
        public String positionName;
        public int[] workMinutes;
        public int workMinuteSum = 0;
    }
}
