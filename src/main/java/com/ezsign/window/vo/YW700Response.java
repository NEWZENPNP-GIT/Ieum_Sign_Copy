package com.ezsign.window.vo;

import com.ezsign.feb.master.vo.YE900VO;
import com.ezsign.feb.pension.vo.YE301VO;
import com.ezsign.feb.pension.vo.YE302VO;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class YW700Response {

    public boolean success;

    public WorkMonth 근무년월;

    public List<YE301VO> 퇴직연금계좌;
    public List<YE302VO> 연금저축계좌;

    public YE900VO 퇴직연금계좌_정정사유;
    public YE900VO 연금저축계좌_정정사유;

    public List<Code> 자료구분코드;
    public List<Code> 금융회사등명칭;
    public List<Code> 퇴직연금구분코드;
    public List<Code> 연금저축구분코드;
}
