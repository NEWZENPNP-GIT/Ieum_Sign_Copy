package com.ezsign.feb.worker.vo;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class YE750Response {

    public boolean success = false;

    public String phoneNum;
    public List<FebYear> febYears;
    public List<YE750VO> list;

    public static class FebYear {
        public String 계약년도;
        public String 계약ID;
    }
}
