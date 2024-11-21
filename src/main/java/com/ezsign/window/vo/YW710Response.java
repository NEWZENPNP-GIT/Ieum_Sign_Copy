package com.ezsign.window.vo;

import com.ezsign.feb.master.vo.YE900VO;
import com.ezsign.feb.special.vo.*;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class YW710Response {

    public boolean success;

    public WorkMonth 근무년월;

    public String 계약년도 = "";  // TODO 계약년도 지울것 (근무년월에 포함)

    public List<YE401VO> 보험료;
    public List<YE402VO> 의료비;
    public List<YE403VO> 교육비;
    public List<YE404VO> 기부금;
    public List<YE405VO> 이월기부금;
    public List<YE408VO> 기부금_조정명세;
    public Sum 기부금합계_합계;
    public Sum 기부금합계_본인;
    public Sum 기부금합계_배우자;
    public Sum 기부금합계_직계비속;
    public Sum 기부금합계_직계존속;
    public Sum 기부금합계_형제자매;
    public Sum 기부금합계_그외;

    public YE900VO 보험료_정정사유;
    public YE900VO 의료비_정정사유;
    public YE900VO 교육비_정정사유;
    public YE900VO 기부금_정정사유;

    public List<Code> 보험료의료비_자료구분코드;
    public List<Code> 교육비기부금_자료구분코드;
    public List<Code> 보험료구분코드;
    public List<Code> 의료비증빙코드;
    public List<Code> 의료비공제종류코드;
    public List<Code> 의료비유형;
    public List<Code> 교육비공제종류코드;
    public List<Code> 기부금코드;
    public List<Code> 기부내용;
    public List<Family> 부양가족ID;

    public static class Sum {
        public long 총계 = 0;
        public long 법정기부금 = 0;
        public long 정치자금기부금 = 0;
        public long 종교단체외지정기부금 = 0;
        public long 종교단체지정기부금 = 0;
        public long 우리사주조합기부금 = 0;
        public long 기부장려금신청금액 = 0;
        public long 기타 = 0;
    }
}
