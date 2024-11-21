package com.ezsign.window.vo;

import com.ezsign.feb.house.vo.YE108VO;
import com.ezsign.feb.master.vo.YE900VO;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class YW650Response {

    public boolean success;

    public WorkMonth 근무년월;

    public List<YE108VO> 신용카드;

    public YE900VO 신용카드_정정사유;

    public List<Family> 부양가족ID;
    public List<Code> 자료구분코드;
    public List<Code> 기간구분코드;
    public List<Code> 신용카드_자료구분코드;
}
