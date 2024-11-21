package com.ezsign.window.vo;

import com.ezsign.feb.house.vo.YE109VO;
import com.ezsign.feb.master.vo.YE900VO;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class YW660Response {

    public boolean success;

    public WorkMonth 근무년월;

    public List<YE109VO> 장기집합투자증권저축;

    public YE900VO 장기집합투자증권저축_정정사유;

    public List<Code> 자료구분코드;
    public List<Code> 취급기관명;
}
