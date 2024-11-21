package com.ezsign.window.vo;

import com.ezsign.feb.master.vo.YE900VO;
import com.ezsign.feb.pension.vo.YE301VO;
import com.ezsign.feb.pension.vo.YE302VO;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class YW700Request {

    public List<YE301VO> 퇴직연금계좌;
    public List<YE302VO> 연금저축계좌;

    public YE900VO 퇴직연금계좌_정정사유;
    public YE900VO 연금저축계좌_정정사유;
}
