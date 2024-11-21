package com.ezsign.window.vo;

import com.ezsign.feb.etc.vo.YE501VO;
import com.ezsign.feb.etc.vo.YE502VO;
import com.ezsign.feb.etc.vo.YE503VO;
import com.ezsign.feb.master.vo.YE900VO;

@SuppressWarnings("NonAsciiCharacters")
public class YW730Request {

    public YE501VO 납세조합공제;
    public YE502VO 주택자금차입금이자세액공제;
    public YE503VO 외국납부세액공제;

    public YE900VO 납세조합공제_정정사유;
    public YE900VO 주택자금차입금이자세액공제_정정사유;
    public YE900VO 외국납부세액공제_정정사유;
}
