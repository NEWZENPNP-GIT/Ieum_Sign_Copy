package com.ezsign.window.vo;

import com.ezsign.feb.special.vo.YE407VO;
import com.ezsign.feb.special.vo.YE409VO;
import com.ezsign.feb.special.vo.YE410VO;
import com.ezsign.feb.special.vo.YE411VO;
import com.ezsign.feb.worker.vo.YE700VO;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class YW800Request {

    public String 계약ID;
    public String 사용자ID;

    public List<YE700VO> 연말정산요약;

    public List<YE407VO> 기부금계산결과;
    
    public List<YE409VO> 의료비계산결과;
    
    public List<YE410VO> 신용카드계산결과항목;
    
    public YE411VO 신용카드계산결과금액;

    public String 소득세적용률;
    public String 연말정산분납여부;
    
    public YW800Response 연발정산조회;
}
