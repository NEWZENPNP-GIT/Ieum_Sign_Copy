package com.ezsign.feb.easyFeb.vo;

import java.util.List;
import java.util.Map;

import com.ezsign.feb.house.vo.YE105VO;
import com.ezsign.feb.house.vo.YE108VO;
import com.ezsign.feb.master.vo.YE900VO;
import com.ezsign.window.vo.Code;
import com.ezsign.window.vo.Family;
import com.ezsign.window.vo.WorkMonth;

import lombok.Data;

@Data
public class AttachFileVo {

	public boolean success;

    public WorkMonth 근무년월;

    public List<Map> 보험료;
    public List<Map> 의료비;
    public List<Map> 교육비;
    public List<Map> 신용카드;
    public List<Map> 기부금;
    public List<Map> 부양가족;
    public List<Map> 월세액공제;
    public List<Map> 이월기부금;
    public List<Map> 기부금_조정명세;

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
    public List<Code> 자료구분코드;
    public List<Code> 기간구분코드;
    public List<Code> 유형코드;
    
    public List<Family> 부양가족ID;
}
