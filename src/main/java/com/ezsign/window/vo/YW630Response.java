package com.ezsign.window.vo;

import com.ezsign.feb.house.vo.YE106VO;
import com.ezsign.feb.master.vo.YE900VO;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class YW630Response {

    public boolean success;

    public WorkMonth 근무년월;

    public List<YE106VO> 개인연금저축;

    public YE900VO 개인연금저축_정정사유;

    public List<Code> 자료구분코드;
    public List<Code> 연금저축구분;
    public List<Code> 금융회사등명칭;
}
