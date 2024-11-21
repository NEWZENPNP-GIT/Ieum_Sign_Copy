package com.ezsign.window.vo;

import com.ezsign.feb.master.vo.YE900VO;
import com.ezsign.feb.worker.vo.YE051VO;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class YW600Response {

    public boolean success;

    public WorkMonth 근무년월;

    public YE051VO 국민연금보험료;
    public List<YE051VO> 공적연금보험료;

    public YE900VO 국민연금보험료_정정사유;
    public YE900VO 공적연금보험료_정정사유;

    public List<Code> 보험료구분;
}
