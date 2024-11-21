package com.ezsign.window.vo;

import com.ezsign.feb.house.vo.YE107VO;
import com.ezsign.feb.master.vo.YE900VO;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class YW640Response {

    public boolean success;

    public WorkMonth 근무년월;

    public boolean 설문지_주택마련저축_입력여부 = false;
    public boolean 설문지_주택마련저축_적용여부 = false;

    public List<YE107VO> 주택마련저축;

    public YE900VO 주택마련저축_정정사유;

    public List<Code> 자료구분코드;
    public List<Code> 주택마련저축구분;
    public List<Code> 금융회사등명칭;
}
