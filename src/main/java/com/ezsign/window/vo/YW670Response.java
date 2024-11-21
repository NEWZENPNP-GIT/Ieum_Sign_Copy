package com.ezsign.window.vo;

import com.ezsign.feb.master.vo.YE900VO;
import com.ezsign.feb.other.vo.YE201VO;
import com.ezsign.feb.other.vo.YE202VO;
import com.ezsign.feb.other.vo.YE203VO;
import com.ezsign.feb.other.vo.YE204VO;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class YW670Response {

    public boolean success;

    public WorkMonth 근무년월;

    public YE201VO 소기업소상공인공제부금;
    public List<YE202VO> 투자조합출자;
    public YE203VO 우리사주조합출연금;
    public YE203VO 우리사주조합출연금벤처;
    public YE204VO 고용유지중소기업근로자;

    public YE900VO 소기업소상공인공제부금_정정사유;
    public YE900VO 투자조합출자_정정사유;
    public YE900VO 우리사주조합출연금_정정사유;
    public YE900VO 고용유지중소기업근로자_정정사유;

    public List<Code> 구분코드;
    public List<Code> 금융회사등명칭;
}
