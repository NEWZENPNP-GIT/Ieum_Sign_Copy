package com.ezsign.window.vo;

import com.ezsign.feb.house.vo.*;
import com.ezsign.feb.master.vo.YE900VO;
import com.ezsign.feb.worker.vo.YE052VO;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class YW900Response {

    public boolean success;

    public WorkMonth 근무년월;

    public boolean 설문지_주택임차차입금_입력여부 = false;
    public boolean 설문지_주택임차차입금_적용여부 = false;
    public boolean 설문지_장기주택저당차입금_입력여부 = false;
    public boolean 설문지_장기주택저당차입금_적용여부 = false;
    public boolean 설문지_월세액공제_입력여부 = false;
    public boolean 설문지_월세액공제_적용여부 = false;

    public List<YE052VO> 보험료;
    public List<YE101VO> 주택임차차입금원리금상환액;
    public List<YE102VO> 금전소비대차계약내용;
    public List<YE103VO> 임대차계약내용;
    public List<YE104VO> 장기주택저당차입금이자상환액;
    public List<YE105VO> 월세액공제;

    public YE900VO 보험료_정정사유;
    public YE900VO 주택임차차입금원리금상환액_정정사유;
    public YE900VO 주택임차차입금원리금상환액_소득공제명세_정정사유;
    public YE900VO 장기주택저당차입금이자상환액_정정사유;
    public YE900VO 월세액공제_정정사유;

    public List<Code> 보험료구분;
    public List<Code> 차입구분;
    public List<Code> 구분코드;
    public List<Code> 유형코드;
}
