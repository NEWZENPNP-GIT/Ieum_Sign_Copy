package com.ezsign.window.vo;

import com.ezsign.feb.master.vo.YE900VO;
import com.ezsign.feb.special.vo.YE406VO;

@SuppressWarnings("NonAsciiCharacters")
public class YW720Response {

    public boolean success;

    public WorkMonth 근무년월;

    public YE406VO 세액감면;

    public YE900VO 세액감면_정정사유;
}
