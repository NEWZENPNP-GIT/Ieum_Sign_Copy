package com.ezsign.window.vo;

import com.ezsign.feb.master.vo.YP000VO;

import lombok.Data;

@Data
public class WorkMonthPayment {

	public String 계약ID;
    public String 사용자ID;
    public String 근무시기;
    public String 사용자이름;

    public String 계약년도;
    public String 진행상태코드;

    public boolean 세대주여부;
    public boolean 외국인단일세율적용;

    // 사용자 총급여/근로소득금액
    public String 총급여;
    public String 근로소득금액;

    // 사용자 근무년월
    public String M1;
    public String M2;
    public String M3;
    public String M4;
    public String M5;
    public String M6;
    public String M7;
    public String M8;
    public String M9;
    public String M10;
    public String M11;
    public String M12;

    public WorkMonthPayment() {

    }

    public WorkMonthPayment(YP000VO vo) {
        if (vo != null) {
            계약ID = vo.get계약ID();
            사용자ID = vo.get사용자ID();
            근무시기 = vo.get근무시기();
            사용자이름 = vo.getEmpName();

            계약년도 = vo.getFebYear();
            진행상태코드 = vo.get진행상태코드();

            세대주여부 = "1".equals(vo.get세대주여부());
            
            외국인단일세율적용 = "1".equals(vo.get외국인단일세율적용());
            
            총급여 = vo.get총급여();
            근로소득금액 = vo.get근로소득금액();

            M1 = vo.getM1();
            M2 = vo.getM2();
            M3 = vo.getM3();
            M4 = vo.getM4();
            M5 = vo.getM5();
            M6 = vo.getM6();
            M7 = vo.getM7();
            M8 = vo.getM8();
            M9 = vo.getM9();
            M10 = vo.getM10();
            M11 = vo.getM11();
            M12 = vo.getM12();
        }
    }
    
}
