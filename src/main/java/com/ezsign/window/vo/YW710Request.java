package com.ezsign.window.vo;

import com.ezsign.feb.master.vo.YE900VO;
import com.ezsign.feb.special.vo.*;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class YW710Request {

    public String 계약ID;
    public String 사용자ID;

    public List<YE401VO> 보험료;
    public List<YE402VO> 의료비;
    public List<YE403VO> 교육비;
    public List<YE404VO> 기부금;
    public List<YE405VO> 이월기부금;
    public List<YE408VO> 기부금_조정명세;

    public YE900VO 보험료_정정사유;
    public YE900VO 의료비_정정사유;
    public YE900VO 교육비_정정사유;
    public YE900VO 기부금_정정사유;
}
