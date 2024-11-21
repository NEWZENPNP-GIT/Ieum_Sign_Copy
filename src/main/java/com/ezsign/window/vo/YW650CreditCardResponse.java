package com.ezsign.window.vo;

import com.ezsign.feb.special.vo.YE410VO;
import com.ezsign.feb.special.vo.YE411VO;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class YW650CreditCardResponse {

    public boolean success;

    public WorkMonth 근무년월;

    public List<YE410VO> 신용카드계산결과항목;
    
    public YE411VO 신용카드계산결과금액;
}
